package skat09.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import skat09.spielart.Ramsch;
import skat09.spielkarte.Farbe;
import skat09.spielkarte.Spielkarte;
import skat09.spielkarte.Wert;

public class RamschTest {

    Ramsch ramsch = new Ramsch();

    @Test
    public void gespielteKartePruefen() {
        ArrayList<Spielkarte> blatt = new ArrayList<Spielkarte>();
        blatt.add(new Spielkarte(Farbe.PIK, Wert.ACHT));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.NEUN));
        blatt.add(new Spielkarte(Farbe.HERZ, Wert.DAME));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.BUBE));
        Spielkarte[] gespielteKarten = new Spielkarte[3];
        Spielkarte zuPruefendeKarte = new Spielkarte(Farbe.KREUZ, Wert.ASS);
        assertTrue(ramsch.gespielteKartePruefen(blatt, gespielteKarten, zuPruefendeKarte));
    }

    @Test
    public void gespielteKartePruefen2() {
        ArrayList<Spielkarte> blatt = new ArrayList<Spielkarte>();
        blatt.add(new Spielkarte(Farbe.PIK, Wert.ACHT));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.NEUN));
        blatt.add(new Spielkarte(Farbe.HERZ, Wert.DAME));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.BUBE));
        Spielkarte[] gespielteKarten = new Spielkarte[3];
        gespielteKarten[0] = new Spielkarte(Farbe.KREUZ, Wert.BUBE);
        Spielkarte zuPruefendeKarte = new Spielkarte(Farbe.KREUZ, Wert.ASS);
        assertFalse(ramsch.gespielteKartePruefen(blatt, gespielteKarten, zuPruefendeKarte));
    }

    @Test
    public void gespielteKartePruefen3() {
        ArrayList<Spielkarte> blatt = new ArrayList<Spielkarte>();
        blatt.add(new Spielkarte(Farbe.PIK, Wert.ACHT));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.NEUN));
        blatt.add(new Spielkarte(Farbe.HERZ, Wert.DAME));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.BUBE));
        Spielkarte[] gespielteKarten = new Spielkarte[3];
        gespielteKarten[0] = new Spielkarte(Farbe.KREUZ, Wert.BUBE);
        Spielkarte zuPruefendeKarte = new Spielkarte(Farbe.HERZ, Wert.BUBE);
        assertTrue(ramsch.gespielteKartePruefen(blatt, gespielteKarten, zuPruefendeKarte));
    }

    @Test
    public void gespielteKartePruefen4() {
        ArrayList<Spielkarte> blatt = new ArrayList<Spielkarte>();
        blatt.add(new Spielkarte(Farbe.PIK, Wert.ACHT));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.NEUN));
        blatt.add(new Spielkarte(Farbe.HERZ, Wert.DAME));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.BUBE));
        Spielkarte[] gespielteKarten = new Spielkarte[3];
        gespielteKarten[0] = new Spielkarte(Farbe.KREUZ, Wert.SIEBEN);
        Spielkarte zuPruefendeKarte = new Spielkarte(Farbe.KREUZ, Wert.ASS);
        assertTrue(ramsch.gespielteKartePruefen(blatt, gespielteKarten, zuPruefendeKarte));
    }

    @Test
    public void bubeBedienenTest() {
        ArrayList<Spielkarte> blatt = new ArrayList<Spielkarte>();
        blatt.add(new Spielkarte(Farbe.PIK, Wert.ACHT));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.NEUN));
        Spielkarte[] gespielteKarten = new Spielkarte[3];
        Spielkarte pruefen = new Spielkarte(Farbe.HERZ, Wert.BUBE);
        assertTrue(ramsch.bubeBedienen(blatt, gespielteKarten, pruefen));
    }

    @Test
    public void bubeBedienenTest2() {
        ArrayList<Spielkarte> blatt = new ArrayList<Spielkarte>();
        blatt.add(new Spielkarte(Farbe.PIK, Wert.ACHT));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.NEUN));
        blatt.add(new Spielkarte(Farbe.HERZ, Wert.DAME));
        Spielkarte[] gespielteKarten = new Spielkarte[3];
        gespielteKarten[0] = new Spielkarte(Farbe.KREUZ, Wert.BUBE);
        Spielkarte pruefen = new Spielkarte(Farbe.HERZ, Wert.ASS);
        assertTrue(ramsch.bubeBedienen(blatt, gespielteKarten, pruefen));
    }

    @Test
    public void bubeBedienenTest3() {
        ArrayList<Spielkarte> blatt = new ArrayList<Spielkarte>();
        blatt.add(new Spielkarte(Farbe.PIK, Wert.ACHT));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.NEUN));
        blatt.add(new Spielkarte(Farbe.HERZ, Wert.DAME));
        blatt.add(new Spielkarte(Farbe.PIK, Wert.BUBE));
        Spielkarte[] gespielteKarten = new Spielkarte[3];
        gespielteKarten[0] = new Spielkarte(Farbe.KREUZ, Wert.BUBE);
        Spielkarte pruefen = new Spielkarte(Farbe.HERZ, Wert.ASS);
        assertFalse(ramsch.bubeBedienen(blatt, gespielteKarten, pruefen));
    }

    @Test
    public void hoehereKarteTest() {
        Spielkarte karte1 = new Spielkarte(Farbe.HERZ, Wert.BUBE);
        Spielkarte karte2 = new Spielkarte(Farbe.KARO, Wert.BUBE);
        assertEquals(karte1, ramsch.hoehereKarte(karte1, karte2));
    }

    @Test
    public void hoehereKarteTest2() {
        Spielkarte karte1 = new Spielkarte(Farbe.HERZ, Wert.BUBE);
        Spielkarte karte2 = new Spielkarte(Farbe.HERZ, Wert.ACHT);
        assertEquals(karte1, ramsch.hoehereKarte(karte1, karte2));
    }

    @Test
    public void hoehereKarteTest3() {
        Spielkarte karte1 = new Spielkarte(Farbe.HERZ, Wert.SIEBEN);
        Spielkarte karte2 = new Spielkarte(Farbe.HERZ, Wert.BUBE);
        assertEquals(karte2, ramsch.hoehereKarte(karte1, karte2));
    }

    @Test
    public void hoehereKarteTest4() {
        Spielkarte karte1 = new Spielkarte(Farbe.HERZ, Wert.DAME);
        Spielkarte karte2 = new Spielkarte(Farbe.HERZ, Wert.KOENIG);
        assertEquals(karte2, ramsch.hoehereKarte(karte1, karte2));
    }

    @Test
    public void hoehereKarteTest5() {
        Spielkarte karte1 = new Spielkarte(Farbe.PIK, Wert.ZEHN);
        Spielkarte karte2 = new Spielkarte(Farbe.HERZ, Wert.BUBE);
        assertEquals(karte2, ramsch.hoehereKarte(karte1, karte2));
    }

    @Test
    public void hoehereKarteTest6() {
        Spielkarte karte1 = new Spielkarte(Farbe.HERZ, Wert.SIEBEN);
        Spielkarte karte2 = new Spielkarte(Farbe.HERZ, Wert.SECHS);
        assertEquals(karte1, ramsch.hoehereKarte(karte1, karte2));
    }

    @Test
    public void hoehereKarteTest7() {
        Spielkarte karte1 = new Spielkarte(Farbe.KARO, Wert.NEUN);
        Spielkarte karte2 = new Spielkarte(Farbe.HERZ, Wert.DAME);
        assertEquals(karte1, ramsch.hoehereKarte(karte1, karte2));
    }

    @Test
    public void sortiereKarteTest() {
        Spielkarte karte1 = new Spielkarte(Farbe.KREUZ, Wert.BUBE);
        Spielkarte karte2 = new Spielkarte(Farbe.PIK, Wert.BUBE);
        assertEquals(karte1, ramsch.sortiereKarte(karte1, karte2));
    }

    @Test
    public void sortiereKarte2Test() {
        Spielkarte karte1 = new Spielkarte(Farbe.KREUZ, Wert.BUBE);
        Spielkarte karte2 = new Spielkarte(Farbe.PIK, Wert.ASS);
        assertEquals(karte1, ramsch.sortiereKarte(karte1, karte2));
    }

    @Test
    public void sortiereKarte3Test() {
        Spielkarte karte1 = new Spielkarte(Farbe.KREUZ, Wert.NEUN);
        Spielkarte karte2 = new Spielkarte(Farbe.PIK, Wert.BUBE);
        assertEquals(karte2, ramsch.sortiereKarte(karte1, karte2));
    }

    @Test
    public void sortiereKarte4Test() {
        Spielkarte karte1 = new Spielkarte(Farbe.KREUZ, Wert.ZEHN);
        Spielkarte karte2 = new Spielkarte(Farbe.PIK, Wert.ASS);
        assertEquals(karte1, ramsch.sortiereKarte(karte1, karte2));
    }

    @Test
    public void sortiereKarte5Test() {
        Spielkarte karte1 = new Spielkarte(Farbe.KARO, Wert.BUBE);
        Spielkarte karte2 = new Spielkarte(Farbe.KARO, Wert.ASS);
        assertEquals(karte1, ramsch.sortiereKarte(karte1, karte2));
    }

    @Test
    public void sortiereKarte6Test() {
        Spielkarte karte1 = new Spielkarte(Farbe.PIK, Wert.ZEHN);
        Spielkarte karte2 = new Spielkarte(Farbe.PIK, Wert.ASS);
        assertEquals(karte2, ramsch.sortiereKarte(karte1, karte2));
    }

    @Test
    public void toStringTest() {
        assertEquals("Ramsch", ramsch.toString());
    }

    @Test
    public void karteBewertenTest() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.SIEBEN);
        assertEquals(7, ramsch.karteBewerten(karte));
    }

    @Test
    public void karteBewertenTest2() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.ACHT);
        assertEquals(8, ramsch.karteBewerten(karte));
    }

    @Test
    public void karteBewertenTest3() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.NEUN);
        assertEquals(9, ramsch.karteBewerten(karte));
    }

    @Test
    public void karteBewertenTest4() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.ZEHN);
        assertEquals(10, ramsch.karteBewerten(karte));
    }

    @Test
    public void karteBewertenTest5() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.DAME);
        assertEquals(11, ramsch.karteBewerten(karte));
    }

    @Test
    public void karteBewertenTest6() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.KOENIG);
        assertEquals(12, ramsch.karteBewerten(karte));
    }

    @Test
    public void karteBewertenTest7() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.ASS);
        assertEquals(13, ramsch.karteBewerten(karte));
    }

    @Test
    public void karteBewertenTest8() {
        Spielkarte karte = new Spielkarte(Farbe.HERZ, Wert.BUBE);
        assertEquals(15, ramsch.karteBewerten(karte));
    }
}
