package at.neckam;

public class PreDemoApp {

    public static void main(String[] args) {
        int anzahl = 5;
        if (++anzahl == 6) {
            System.out.println("Erst gerechnet: " + anzahl);
        } else {
            System.out.println("Erst verglichen: " + anzahl);
        }
    }
}
