package laborki03_zadania;

import java.rmi.*;
import java.text.*;
import java.util.*;

public class Klient {

    public static void main(String[] s) {
        int registryPort = 8000;
        InterfejsSerwera server = null;
        try {
            System.setSecurityManager(new RMISecurityManager());
            server = (InterfejsSerwera) Naming.lookup("//localhost:" + registryPort + "/Serwer");
            int n = 18;
            int min = 32;
            int max = 75;
            server.wypelnijTablice(n, min, max);
            System.out.println("Wygenerowane liczby dla tablicy: " + server.wyswietlTablice());
            System.out.println("Element min w tablicy: " + server.wezMin());
            System.out.println("Element max w tablicy : " + server.wezMax());
            String[][] slowa = { { "przez", "przez rzeczy trudne do gwiazd" }, { "bez", "przez rzeczy trudne do gwiazd" } };
            for (int i = 0; i <= 1; i++) if (server.czyJestPodciagiem(slowa[i][0], slowa[i][1])) {
                System.out.println("Slowo \"" + slowa[i][0] + "\" znajduje sie w slowie \"" + slowa[i][1] + "\".");
            } else System.out.println("Slowo \"" + slowa[i][0] + "\" nie znajduje sie w slowie \"" + slowa[i][1] + "\".");
            int fib = 3;
            System.out.println("Liczba o indeksie rownym " + fib + " w ciagu Fibonacciego to: " + server.liczbaZCiaguFibonacciego(fib) + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
