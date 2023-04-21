package at.neckam;

public class VarargsApp {

    public static void main(String args[]) {
        int ergebnis = summe(1, 10, 100);
        System.out.println("Ergebnis: " + ergebnis);
    }

    public static int summe(int... summand) {
        int summe = 0;
        for (int i = 0; 1 < summand.length; i++) {
            summe += summand[i];
        }
        return summe;
    }
}
