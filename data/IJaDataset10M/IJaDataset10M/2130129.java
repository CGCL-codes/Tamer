package fhi.bg.logik.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import fhi.bg.fachklassen.*;
import fhi.bg.logik.SL10_SpielVerlassen;

public class T13_S03_50_Runden extends S03_Base {

    @Test
    public void testRundenSpieler() {
        try {
            int runde = 0;
            long sum = 0;
            while (BeerGame.get().getAktuellesSpiel().getAktuelleRunde().getRundenNummer() < 46) {
                long time = System.nanoTime();
                bestelleRunde(runde++, new int[] { 4, 4, 4, 4, 4 });
                bestaetigen("lagerverwaltungBestaetigt");
                bestelleRunde(runde++, new int[] { 1, 1, 1, 1, 1 });
                bestaetigen("lagerverwaltungBestaetigt");
                bestelleRunde(runde++, new int[] { 1, 1, 1, 1, 1 });
                bestaetigen("lagerverwaltungBestaetigt");
                bestelleRunde(runde++, new int[] { 1, 1, 1, 1, 1 });
                bestaetigen("lagerverwaltungBestaetigt");
                assertTrue(spiel.wartetAufEreigniskarte());
                assertTrue(spiel.aktiviereEreigniskarte(Ereigniskarte.getEreigniskarte(9)));
                sum += System.nanoTime() - time;
                System.out.println((System.nanoTime() - time) / 1000.0);
            }
            System.out.println("Gesamt: " + sum / 1000.0f);
            SL10_SpielVerlassen.verlasseSpiel(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.toString(), false);
        }
        assertTrue(true);
    }

    public static void main(String[] s) throws InvalidParameterException, KeinSpielAktivException {
        T13_S03_50_Runden t = new T13_S03_50_Runden();
        t.setup();
        t.testRundenSpieler();
        t.fin();
    }
}
