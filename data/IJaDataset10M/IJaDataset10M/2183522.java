package my.projects.dda;

import java.io.*;

/**
 * @author FrankvanAssem
 */
public class AmdahlDemo {

    public static void main(String[] args) {
        System.out.println("Dit programma berekent speedup-factor en efficiency volgens de wet van Amdahl: ");
        int aantal = 0;
        double seqDeel = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("wat is het aantal computers: ");
            String input = in.readLine();
            aantal = Integer.parseInt(input);
            System.out.print("hoe groot is het gedeelte van het programma dat sequentieel uitgevoerd moet worden: ");
            input = in.readLine();
            seqDeel = Double.parseDouble(input);
        } catch (IOException e) {
            e.getMessage();
        } catch (NumberFormatException e) {
            e.getMessage();
        }
        System.out.println("aantal computers: " + aantal);
        System.out.println("sequentieel deel: " + seqDeel);
        System.out.println("speedup factor: " + Amdahl.berekenSpeedupFactor(seqDeel, aantal));
        System.out.println("efficiency: " + Amdahl.berekenEfficiency(seqDeel, aantal));
    }
}
