package wejsciowka02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.*;

public class Klient {

    public static void main(String[] s) {
        int registryPort = 8000;
        Interfejs server = null;
        try {
            System.setSecurityManager(new RMISecurityManager());
            server = (Interfejs) Naming.lookup("//localhost:" + registryPort + "/Serwer");
            InputStreamReader wejscie = new InputStreamReader(System.in);
            BufferedReader bufor = new BufferedReader(wejscie);
            System.out.print("Podaj liczbe: ");
            String liczba = "";
            try {
                liczba = bufor.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Suma skladowych liczby \"" + liczba + "\" to " + server.sumaSkladowych(liczba));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
