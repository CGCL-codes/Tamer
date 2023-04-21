package laborki02;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Zad05_Serwer extends Thread {

    static final int port = 1666;

    static final String[] odpowiedzi = { "niee ;P", "nie!", "sorry, nie :(", "niestety nie ;]", "nie qwaaa!!", "eee???", "absolutnie... nie.", "hmmm...", "zastanowie sie :P" };

    private Socket gniazdoKlienta = null;

    public Zad05_Serwer(Socket gniazdoKlienta) {
        this.gniazdoKlienta = gniazdoKlienta;
    }

    public void run() {
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
        Random rand = new Random();
        int k = 0;
        try {
            while ((liniaWejsciowa = wejscie.readLine()) != null) {
                System.out.print("[Tomek] Dostalem wlasnie wiadomosc: \"" + liniaWejsciowa + "\". Odpowiedzialem: \"");
                k = rand.nextInt(odpowiedzi.length);
                System.out.println(odpowiedzi[k] + "\"");
                wyjscie.println(odpowiedzi[k]);
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
        } catch (IOException e) {
            System.err.println("Wystapil wejscia/wyjscia funkcji zamykania polaczen");
        }
    }

    public static void main(String[] args) {
        boolean nasluchuje = true;
        ServerSocket gniazdoSerwera = null;
        try {
            gniazdoSerwera = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Wystapil blad wejscia/wyjscia. Nie mozna ustanowic nasluchu na danym porcie");
            System.exit(-1);
        }
        while (nasluchuje) {
            try {
                new Zad05_Serwer(gniazdoSerwera.accept()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            gniazdoSerwera.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
