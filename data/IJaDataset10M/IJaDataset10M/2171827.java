package laborki02;

import java.io.*;
import java.net.*;

public class Zad04_Serwer {

    static final int port = 1666;

    public static void main(String[] args) {
        ServerSocket gniazdoSerwera = null;
        try {
            gniazdoSerwera = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia. Nie mozna ustanowic nasluchu na danym porcie");
            System.exit(-1);
        }
        Socket gniazdoKlienta = null;
        try {
            gniazdoKlienta = gniazdoSerwera.accept();
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia funkcji akceptacji polaczan");
            System.exit(-1);
        }
        PrintWriter wyjscie = null;
        try {
            wyjscie = new PrintWriter(gniazdoKlienta.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        BufferedReader wejscie = null;
        try {
            wejscie = new BufferedReader(new InputStreamReader(gniazdoKlienta.getInputStream()));
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        String liniaWejsciowa = null;
        try {
            while ((liniaWejsciowa = wejscie.readLine()) != null) {
                System.out.println("Dostalem i odsylam z powrotem nastepujaca wiadomosc: " + liniaWejsciowa);
                wyjscie.println(liniaWejsciowa);
                if (liniaWejsciowa.equals("FLǕGGȦ∂NKđ€ČHIŒβǾLβÊN")) break;
            }
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        wyjscie.close();
        try {
            wejscie.close();
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        try {
            gniazdoKlienta.close();
            gniazdoSerwera.close();
        } catch (IOException e) {
            System.err.println("Wystapil wejscia/wyjscia funkcji zamykania polaczen");
        }
    }
}
