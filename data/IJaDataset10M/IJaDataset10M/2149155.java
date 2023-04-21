package de.jost_net.OBanToo.Dtaus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * E-Satz - Datei-Nachsatz
 * 
 * @author Heiner Jostkleigrewe
 * 
 */
public class ESatz extends Satz {

    /**
   * Feld e01, 4 Bytes, numerisch, Satzl�ngenfeld, Konstant 0128
   */
    private String eSatzlaenge = "0128";

    /**
   * Feld e02, 1 Byte, alpha, Satzart, Konstant E
   */
    private String eSatzart = "E";

    /**
   * Feld e04, 7 Bytes, numerisch, Anzahl der Datens�tze C, Abstimm-Daten
   */
    private int eAnzahlC = 0;

    /**
   * Feld e06, 17 Bytes, numerisch, Summe der Kontonummern, Abstimm-Unterlage
   */
    private BigInteger eSummeKontonummern = new BigInteger("0");

    /**
   * Feld e07, 17 Bytes, numerisch, Summe der Bankleitzahlen, Abstimm-Unterlage
   */
    private BigInteger eSummeBankleitzahlen = new BigInteger("0");

    /**
   * Feld e08, 13 Bytes, numerisch, Summe der Euro-Betr�ge aus den Datens�tzen C
   * (Feld 12)
   */
    private BigInteger eSummeBetraege = new BigInteger("0");

    public ESatz() {
    }

    /**
   * Konstruktor mit der �bergabe eines zu parsenden Satzes
   * 
   * @param satz
   */
    public ESatz(String satz, int toleranz) throws DtausException {
        satz = codingFromDtaus(satz, toleranz);
        try {
            validCharacters(satz);
        } catch (DtausException e) {
            throw new DtausException(e.getMessage() + " im E-Satz");
        }
        if (!satz.substring(0, 4).equals(eSatzlaenge)) {
            throw new DtausException(DtausException.E_SATZLAENGENFELD_FEHLERHAFT);
        }
        if (!satz.substring(4, 5).equals(eSatzart)) {
            throw new DtausException(DtausException.E_SATZART_FEHLERHAFT);
        }
        setAnzahlDatensaetze(satz.substring(10, 17));
        setSummeKontonummern(satz.substring(30, 47));
        setSummeBankleitzahlen(satz.substring(47, 64));
        setSummeBetraege(satz.substring(64, 77));
    }

    public void setAnzahlDatensaetze(String value) throws DtausException {
        try {
            eAnzahlC = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new DtausException(DtausException.E_ANZAHL_CSAETZE_FEHLERHAFT, value);
        }
    }

    public int getAnzahlDatensaetze() {
        return eAnzahlC;
    }

    public void setSummeKontonummern(String value) {
        eSummeKontonummern = new BigInteger(value);
    }

    public BigInteger getSummeKontonummern() {
        return eSummeKontonummern;
    }

    public void setSummeBankleitzahlen(String value) {
        eSummeBankleitzahlen = new BigInteger(value);
    }

    public BigInteger getSummeBankleitzahlen() {
        return this.eSummeBankleitzahlen;
    }

    public void setSummeBetraege(String value) throws DtausException {
        try {
            eSummeBetraege = new BigInteger(value);
        } catch (NumberFormatException e) {
            throw new DtausException(DtausException.E_SUMME_BETRAEGE_FEHLERHAFT, value);
        }
    }

    public BigInteger getSummeBetraege() {
        return this.eSummeBetraege;
    }

    public void add(CSatz csatz) {
        this.eAnzahlC++;
        this.eSummeBankleitzahlen = this.eSummeBankleitzahlen.add(new BigInteger(csatz.getBlzEndbeguenstigt() + ""));
        this.eSummeKontonummern = this.eSummeKontonummern.add(new BigInteger(csatz.getKontonummer() + ""));
        this.eSummeBetraege = this.eSummeBetraege.add(new BigInteger(csatz.getBetragInCent() + ""));
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeBytes("0128");
        dos.writeBytes("E");
        dos.writeBytes(Tool.space(5));
        dos.writeBytes(Tool.formatKontollAnzahl(this.eAnzahlC));
        dos.writeBytes(Tool.formatKontrollSumme(new BigInteger("0")));
        dos.writeBytes(Tool.formatKontroll17(this.eSummeKontonummern));
        dos.writeBytes(Tool.formatKontroll17(this.eSummeBankleitzahlen));
        dos.writeBytes(Tool.formatKontrollSumme(this.eSummeBetraege));
        dos.writeBytes(Tool.space(51));
    }

    public String toString() {
        return "Satzlaenge=" + eSatzlaenge + ", Satzart=" + eSatzart + ", Anzahl C-S�tze=" + eAnzahlC + ", Summe Kontonummern=" + eSummeKontonummern.toString() + ", Summe Bankleitzahlen=" + eSummeBankleitzahlen.toString() + ", Summe Betr�ge=" + eSummeBetraege;
    }
}
