package laborki02;

import java.io.*;
import java.net.*;

public class Zad04_Klient {

    static final int port = 1666;

    public static void main(String[] args) {
        Socket gniazdoKlienta = null;
        try {
            gniazdoKlienta = new Socket("localhost", port);
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        PrintWriter wyjscieGniazdo = null;
        try {
            wyjscieGniazdo = new PrintWriter(gniazdoKlienta.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        BufferedReader wejscieGniazdo = null;
        try {
            wejscieGniazdo = new BufferedReader(new InputStreamReader(gniazdoKlienta.getInputStream()));
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        BufferedReader wejscieKlawiatura = null;
        wejscieKlawiatura = new BufferedReader(new InputStreamReader(System.in));
        String liniaWejsciowaKlawiatura = null;
        try {
            while (true) {
                System.out.print("Wysylana wiadomosc: ");
                liniaWejsciowaKlawiatura = wejscieKlawiatura.readLine();
                wyjscieGniazdo.println(liniaWejsciowaKlawiatura);
                System.out.println("Otrzymana wiadomosc: " + wejscieGniazdo.readLine());
                if (liniaWejsciowaKlawiatura.equals("FLǕGGȦ∂NKđ€ČHIŒβǾLβÊN")) break;
            }
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        wyjscieGniazdo.close();
        try {
            wejscieGniazdo.close();
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia");
            System.exit(-1);
        }
        try {
            gniazdoKlienta.close();
        } catch (IOException e) {
            System.err.println("Wystapil wejscia/wyjscia funkcji zamykania polaczen");
        }
    }
}
