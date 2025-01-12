package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import problema1.Angajat;

public class Main {

    public static List<Angajat> citesteAngajatiDinJSON(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.readValue(new File(filePath), new TypeReference<List<Angajat>>() {});
    }

    public static void main(String[] args) {
        try {
            List<Angajat> angajati = citesteAngajatiDinJSON("src/main/resources/angajati.json");
            Scanner scanner = new Scanner(System.in);
            int anulTrecut = LocalDate.now().getYear() - 1;

            int optiune;
            do {
                // Afișare meniu
                System.out.println("\nMeniu:");
                System.out.println("1. Afișează lista de angajați");
                System.out.println("2. Afișează angajații cu salariu peste 2500");
                System.out.println("3. Afișează angajații din aprilie anul trecut cu funcție de conducere");
                System.out.println("4. Afișează angajații fără funcție de conducere, ordonați descrescător după salariu");
                System.out.println("5. Afișează numele angajaților cu majuscule");
                System.out.println("6. Afișează salariile mai mici de 3000");
                System.out.println("7. Afișează datele primului angajat");
                System.out.println("8. Afișează statistici despre salarii");
                System.out.println("9. Verifică existența unui 'Ion'");
                System.out.println("10. Număr de angajați angajați vara anului trecut");
                System.out.println("0. Ieșire");

                // Citirea opțiunii
                System.out.print("Alege o opțiune: ");
                optiune = scanner.nextInt();

                switch (optiune) {
                    case 1:
                        angajati.forEach(System.out::println);
                        break;
                    case 2:
                        System.out.println("\nAngajați cu salariu peste 2500:");
                        angajati.stream()
                                .filter(angajat -> angajat.getSalariu() > 2500)
                                .forEach(System.out::println);
                        break;
                    case 3:
                        System.out.println("\nAngajați din aprilie anul trecut cu funcție de conducere:");
                        angajati.stream()
                                .filter(angajat -> angajat.getDataAngajarii().getYear() == anulTrecut)
                                .filter(angajat -> angajat.getDataAngajarii().getMonth() == Month.APRIL)
                                .filter(angajat -> angajat.getPost().toLowerCase().contains("șef") ||
                                        angajat.getPost().toLowerCase().contains("manager"))
                                .forEach(System.out::println);
                        break;
                    case 4:
                        System.out.println("\nAngajați fără funcție de conducere (ordonare descrescătoare a salariilor):");
                        angajati.stream()
                                .filter(angajat -> !angajat.getPost().toLowerCase().contains("șef") &&
                                        !angajat.getPost().toLowerCase().contains("director"))
                                .sorted(Comparator.comparing(Angajat::getSalariu).reversed())
                                .forEach(System.out::println);
                        break;
                    case 5:
                        System.out.println("\nNume angajați cu majuscule:");
                        angajati.stream()
                                .map(angajat -> angajat.getNume().toUpperCase())
                                .forEach(System.out::println);
                        break;
                    case 6:
                        System.out.println("\nSalarii mai mici de 3000:");
                        angajati.stream()
                                .filter(angajat -> angajat.getSalariu() < 3000)
                                .map(Angajat::getSalariu)
                                .forEach(System.out::println);
                        break;
                    case 7:
                        System.out.println("\nPrimul angajat:");
                        angajati.stream()
                                .min(Comparator.comparing(Angajat::getDataAngajarii))
                                .ifPresentOrElse(
                                        System.out::println,
                                        () -> System.out.println("Nu există angajați.")
                                );
                        break;
                    case 8:
                        System.out.println("\nStatistici salarii:");
                        var statistici = angajati.stream()
                                .collect(Collectors.summarizingDouble(Angajat::getSalariu));
                        System.out.println("Salariu mediu: " + statistici.getAverage());
                        System.out.println("Salariu minim: " + statistici.getMin());
                        System.out.println("Salariu maxim: " + statistici.getMax());
                        break;
                    case 9:
                        System.out.println("\nExistența unui 'Ion':");
                        angajati.stream()
                                .map(Angajat::getNume)
                                .filter(nume -> nume.equalsIgnoreCase("Ion"))
                                .findAny()
                                .ifPresentOrElse(
                                        nume -> System.out.println("Firma are cel puțin un Ion angajat."),
                                        () -> System.out.println("Firma nu are niciun Ion angajat.")
                                );
                        break;
                    case 10:
                        System.out.println("\nNumăr de angajați angajați vara anului trecut:");
                        long angajatiVara = angajati.stream()
                                .filter(angajat -> angajat.getDataAngajarii().getYear() == anulTrecut)
                                .filter(angajat -> angajat.getDataAngajarii().getMonth().getValue() >= 6 &&
                                        angajat.getDataAngajarii().getMonth().getValue() <= 8)
                                .count();
                        System.out.println(angajatiVara);
                        break;
                    case 0:
                        System.out.println("Ieșire...");
                        break;
                    default:
                        System.out.println("Opțiune invalidă. Reîncearcă.");
                }
            } while (optiune != 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
