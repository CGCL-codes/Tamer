package Business;

import gui.CSpielfeld;
import Datenhaltung.CDatenhaltung;

/**
 * 
 * @author Olli
 *
 */
public class CKISchlagdame extends CKI {

    CKISchlagdame(int tiefe, CBusiness business, CRegel regel) {
        super(tiefe, business, regel);
    }

    public static final int ZEILEN = 8;

    public static final int SPALTEN = 8;

    public static final int ANZAHL_STEINE_PRO_FARBE = 12;

    public static final int MAX_BEWERTUNG = -100;

    public static final int MIN_BEWERTUNG = 100;

    public static final int WERTUNG_NEUTRAL = (MAX_BEWERTUNG + MIN_BEWERTUNG) / 2;

    public static final int STEIN_WERT = 5, DAME_WERT = STEIN_WERT * 2;

    /**
	 * getSpielfeldBewertung
	 * 
	 * bewertet einen Spielstand, aus Sicht von Schwarz :
	 * 				1 .. miserabel/beinahe verloren
	 * 			 50 .. ausgeglichen
	 * 			100 .. hervorragend/schir gar gewonnen
	 * 
	 * Durch Umsetzen von const ZEILEN , SPALTEN auch f�r Dame-Int geeignet.
	 * 
	 * @param spielfeld
	 * @return
	 */
    public int getSpielfeldBewertung(byte[][] spielfeld) {
        int wertung;
        int countDamenSchwarz = 0, countDamenWeiss = 0;
        int countSteineSchwarz = 0, countSteineWeiss = 0;
        boolean[][] startfelderSchwarz;
        boolean[][] startfelderWeiss;
        int iMoeglZuegeSchwarz, iMoeglZuegeWeiss;
        float fVerhMoeglZuegeSchwarz, fVerhMoeglZuegeWeiss;
        int pluspunkte = 0, minuspunkte = 0;
        int GesamtPunkteMax = 0;
        float verhaeltnis = 0;
        int wertungsPunkte;
        float relativWertung;
        float BewertungRange = MAX_BEWERTUNG - MIN_BEWERTUNG;
        int iZeile, iSpalte;
        final int WK_EXTREM = 100;
        final int WK_SEHR = 70;
        final int WK_MITTEL = 45;
        final int WK_ETWAS = 25;
        final int WK_KAUM = 10;
        final int IDEALER_ZUG = MAX_BEWERTUNG;
        final int SELBSTMORD = MIN_BEWERTUNG;
        wertung = WERTUNG_NEUTRAL;
        pluspunkte = 0;
        minuspunkte = 0;
        business.getMoeglicheStartfelder(spielfeld, true);
        startfelderSchwarz = business.Startfelder;
        business.getMoeglicheStartfelder(spielfeld, false);
        startfelderWeiss = business.Startfelder;
        iMoeglZuegeSchwarz = 0;
        iMoeglZuegeWeiss = 0;
        for (iZeile = 0; iZeile < ZEILEN; iZeile++) for (iSpalte = 0; iSpalte < SPALTEN; iSpalte++) {
            if (startfelderSchwarz[iZeile][iSpalte]) iMoeglZuegeSchwarz++;
            if (startfelderWeiss[iZeile][iSpalte]) iMoeglZuegeWeiss++;
            switch(spielfeld[iZeile][iSpalte]) {
                case CDatenhaltung.FELD_DAME_SCHWARZ:
                    countDamenSchwarz++;
                    break;
                case CDatenhaltung.FELD_DAME_WEISS:
                    countDamenWeiss++;
                    break;
                case CDatenhaltung.FELD_STEIN_SCHWARZ:
                    countSteineSchwarz++;
                    break;
                case CDatenhaltung.FELD_STEIN_WEISS:
                    countSteineWeiss++;
                    break;
                default:
                    break;
            }
        }
        if ((countDamenWeiss + countSteineWeiss) == 0) return IDEALER_ZUG;
        if ((countDamenSchwarz + countSteineSchwarz) == 0) return SELBSTMORD;
        if (iMoeglZuegeSchwarz < 1) return SELBSTMORD;
        if (iMoeglZuegeWeiss < 1) return IDEALER_ZUG;
        GesamtPunkteMax += ANZAHL_STEINE_PRO_FARBE * STEIN_WERT * WK_MITTEL;
        pluspunkte += countSteineSchwarz * STEIN_WERT * WK_MITTEL;
        minuspunkte += countSteineWeiss * STEIN_WERT * WK_MITTEL;
        GesamtPunkteMax += ANZAHL_STEINE_PRO_FARBE * DAME_WERT * WK_MITTEL;
        pluspunkte += countDamenSchwarz * DAME_WERT * WK_MITTEL;
        minuspunkte += countDamenWeiss * DAME_WERT * WK_MITTEL;
        GesamtPunkteMax += 100 * WK_SEHR;
        if (countDamenSchwarz != countDamenWeiss) if (countDamenSchwarz > countDamenWeiss) if (countDamenSchwarz > (countDamenWeiss + 1)) pluspunkte += 100 * WK_SEHR; else pluspunkte += 50 * WK_SEHR; else if (countDamenSchwarz < (countDamenWeiss - 1)) minuspunkte += 100 * WK_SEHR; else minuspunkte += 50 * WK_SEHR;
        GesamtPunkteMax += 100 * WK_MITTEL;
        if (countSteineSchwarz != countSteineWeiss) if (countSteineSchwarz > countSteineWeiss) if (countSteineSchwarz > (countSteineWeiss + 2)) pluspunkte += 100 * WK_MITTEL; else pluspunkte += 50 * WK_MITTEL; else if (countSteineSchwarz < (countSteineWeiss - 2)) minuspunkte += 100 * WK_MITTEL; else minuspunkte += 50 * WK_MITTEL;
        GesamtPunkteMax += 100 * WK_MITTEL;
        if (countSteineWeiss > 0) {
            verhaeltnis = (float) countSteineSchwarz / (float) countSteineWeiss;
            if (verhaeltnis > 1.0001) if (verhaeltnis >= 1.25) pluspunkte += 100 * WK_MITTEL; else pluspunkte += 20 * WK_MITTEL; else if (verhaeltnis < 0.9999) if (verhaeltnis <= 0.75) minuspunkte += 100 * WK_MITTEL; else minuspunkte += 20 * WK_MITTEL; else ;
        } else {
            if (countSteineSchwarz > 0) {
                pluspunkte += 100 * WK_MITTEL;
            }
        }
        fVerhMoeglZuegeSchwarz = ((float) iMoeglZuegeSchwarz) / ((float) (countDamenSchwarz + countSteineSchwarz));
        fVerhMoeglZuegeWeiss = ((float) iMoeglZuegeWeiss) / ((float) (countDamenWeiss + countSteineWeiss));
        GesamtPunkteMax += 100 * WK_KAUM;
        if (fVerhMoeglZuegeSchwarz > fVerhMoeglZuegeWeiss) {
            pluspunkte += 100 * WK_KAUM;
        } else if (fVerhMoeglZuegeSchwarz < fVerhMoeglZuegeWeiss) {
            minuspunkte += 100 * WK_KAUM;
        }
        wertungsPunkte = pluspunkte - minuspunkte;
        relativWertung = ((float) wertungsPunkte) / GesamtPunkteMax;
        wertung = WERTUNG_NEUTRAL + (int) (relativWertung * (BewertungRange / 2) - .5f);
        if (wertung <= SELBSTMORD) {
            wertung = SELBSTMORD + 1;
        }
        if (wertung >= IDEALER_ZUG) {
            wertung = IDEALER_ZUG - 1;
        }
        return wertung;
    }

    public int getBewertung(byte[][] spielfeld, boolean bFuerSchwarz) {
        int wertung;
        wertung = getSpielfeldBewertung(spielfeld);
        if (bFuerSchwarz) ; else {
            wertung = WERTUNG_NEUTRAL - wertung;
        }
        return wertung;
    }

    public int getBewertungFuerSchwarz(byte[][] spielfeld) {
        final int MULT_DAME = 8;
        final int MULT_STEIN = 1;
        int bewertung = 0;
        boolean bWeissHatVerloren = true;
        boolean bSchwarzHatVerloren = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (spielfeld[i][j] == CDatenhaltung.FELD_DAME_SCHWARZ) {
                    bewertung += MULT_DAME;
                    bSchwarzHatVerloren = false;
                } else if (spielfeld[i][j] == CDatenhaltung.FELD_DAME_WEISS) {
                    bewertung -= MULT_DAME;
                    bWeissHatVerloren = false;
                } else if (spielfeld[i][j] == CDatenhaltung.FELD_STEIN_SCHWARZ) {
                    bewertung += MULT_STEIN;
                    bSchwarzHatVerloren = false;
                } else if (spielfeld[i][j] == CDatenhaltung.FELD_STEIN_WEISS) {
                    bewertung -= MULT_STEIN;
                    bWeissHatVerloren = false;
                }
            }
        }
        if (bWeissHatVerloren) {
            return MAX_BEWERTUNG;
        }
        if (bSchwarzHatVerloren) {
            return MIN_BEWERTUNG;
        }
        return bewertung;
    }

    public int getBewertungFuerWeiss(byte[][] spielfeld) {
        final int MULT_DAME = 8;
        final int MULT_STEIN = 1;
        int bewertung = 0;
        boolean bWeissHatVerloren = true;
        boolean bSchwarzHatVerloren = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (spielfeld[i][j] == CDatenhaltung.FELD_DAME_SCHWARZ) {
                    bewertung -= MULT_DAME;
                    bSchwarzHatVerloren = false;
                } else if (spielfeld[i][j] == CDatenhaltung.FELD_DAME_WEISS) {
                    bewertung += MULT_DAME;
                    bWeissHatVerloren = false;
                } else if (spielfeld[i][j] == CDatenhaltung.FELD_STEIN_SCHWARZ) {
                    bewertung -= MULT_STEIN;
                    bSchwarzHatVerloren = false;
                } else if (spielfeld[i][j] == CDatenhaltung.FELD_STEIN_WEISS) {
                    bewertung += MULT_STEIN;
                    bWeissHatVerloren = false;
                }
            }
        }
        if (bWeissHatVerloren) {
            return MIN_BEWERTUNG;
        }
        if (bSchwarzHatVerloren) {
            return MAX_BEWERTUNG;
        }
        return bewertung;
    }
}
