package org.jcryptool.analysis.entropy.calc;

import java.util.Enumeration;
import java.util.Hashtable;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

/**
 * @author Matthias Mensch
 */
public class EntropyData {

    /**
     * enthaelt den uebergebenen Quelltext als String
     */
    private String sourceText;

    private String filteredText;

    private String alphabet;

    /**
     * Anzahl der unterschiedlichen Zeichen im Alphabet:
     */
    private int lengthAlphabet;

    /**
     * enthaelt die absolute Haeufigkeit aller Zeichen des gefilterten Text Schluessel =
     * Zeichentupel (String), Wert = Anzahl des Zeichen (int)
     */
    private Hashtable<String, Integer> absFreq;

    /**
     * enthaelt die relative Haeufigkeit aller Zeichen des normalisierten Text Schluessel =
     * Zeichentupel (String), Wert = relative Haeufigkeit (double)
     */
    private Hashtable<String, Double> relFreq;

    /**
     * Die zum EntropyData gehoerige Instanz der Klasse EntropyTupelFreq. Genaue Dokumentation siehe
     * EntropyTupelFreq.java
     */
    private EntropyTupelFreq letterFreqTable;

    public EntropyData(String text, TransformData modifySettings) {
        sourceText = text;
        filteredText = Transform.transformText(text, modifySettings);
        alphabet = reduceToDifferentChars(filteredText);
        lengthAlphabet = alphabet.length();
        relFreq = new Hashtable<String, Double>();
        absFreq = new Hashtable<String, Integer>();
    }

    /**
     * Get-Methode, liefert den urspruenglichen Text.
     *
     * @return sourceText
     */
    public String getSourceText() {
        return sourceText;
    }

    /**
     * Get-Methode, liefert den gefilterten Text.
     *
     * @return filteredText
     */
    public String getFilteredText() {
        return filteredText;
    }

    /**
     * Get-Methode, liefert eine Hashtable mit den absoluten Haeufigkeiten von n-tupeln
     *
     * @return Hashtable absFreq
     */
    public Hashtable<String, Integer> getAbsoluteFrequency() {
        return absFreq;
    }

    /**
     * get-Methode, liefert eine Hashtable mit den relativen Haeufigkeiten von n-Tupeln.
     *
     * @return Hashtable relFreq
     */
    public Hashtable<String, Double> getRelativeFrequency() {
        return relFreq;
    }

    /**
     * Get-Methode, liefert eine Instanz der Klasse EntropyTupelFreq
     *
     * @return EntropyTupelFreq
     */
    public EntropyTupelFreq getFreqTable() {
        return letterFreqTable;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public int getLengthAlphabet() {
        return lengthAlphabet;
    }

    public int getLengthFilteredText() {
        return filteredText.length();
    }

    /**
     * berechnet die bedingte und absolute Entropie und nutzt dabei 'filteredText' als Nachricht.
     * Daraus resultierende Informationen werden berechnet und in einem double-Arraysiehe
     * gespeichert, siehe return Parameter.
     *
     * @param n zu beruecksichtigenden Tupellaengen der bedingten Entropie
     * @return double[] Ergebnis [0]:F_n, [1]:G_n, [2]:Anzahl unterschiedlicher n-tupel, [3]:Anzahl
     *         moeglicher n-tupel, [4]:maximale Entropie, [5]:relative Entropie bezueglich F_n,
     *         [6]:absolute Entropie geteilt durch n, [7]:Redundanz bezgl. F_n, [8]:Redundanz bezgl.
     *         G_n, [9]:Zuwachsfaktor von G_n-1 auf G_n
     */
    public double[] calcEntropy(int n) {
        String ntupel;
        double rf;
        double crf;
        double numDifTupels = 0.0;
        double[] result = new double[EntropyCalc.MATRIXCOLUMS];
        for (int j = 0; j < result.length; j++) {
            result[j] = 0.0;
        }
        relFreq.clear();
        absFreq.clear();
        if (n > 1) {
            countAbsFreq(n - 1);
        }
        countAbsFreq(n);
        if (n > 1) {
            countRelFreq(n - 1, this.filteredText.length());
        }
        countRelFreq(n, this.filteredText.length());
        Enumeration<String> e = relFreq.keys();
        while (e.hasMoreElements()) {
            ntupel = e.nextElement().toString();
            if (ntupel.length() == n) {
                rf = Double.parseDouble(relFreq.get(ntupel).toString());
                crf = calcCondProb(ntupel);
                result[0] += (rf * EntropyData.log2(crf));
                result[1] += (rf * EntropyData.log2(rf));
                numDifTupels += (double) 1;
            }
        }
        if (n == 1) {
            letterFreqTable = new EntropyTupelFreq(this.lengthAlphabet, 1, relFreq);
        }
        result[2] = numDifTupels;
        result[3] = Math.pow((double) this.lengthAlphabet, (double) n);
        result[4] = EntropyData.log2((double) this.lengthAlphabet);
        result[0] = Math.abs(result[0]);
        result[4] = Math.abs(result[4]);
        result[1] = Math.abs(result[1]);
        result[5] = result[0] / result[4];
        result[6] = (result[1] / n);
        result[7] = 1.0 - result[5];
        result[8] = 1.0 - ((result[1] / n) / result[4]);
        if (n == 1) {
            result[9] = 0.0;
        } else {
            result[9] = (result[1] - (result[1] - result[0])) / (result[1] - result[0]);
        }
        return result;
    }

    private String reduceToDifferentChars(String text) {
        String t = text;
        for (int i = 0; i < t.length(); i++) {
            t = t.substring(0, i + 1).concat(removeChar(t.substring(i + 1), t.charAt(i)));
        }
        return t;
    }

    private static String removeChar(String text, char c) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != c) result += text.charAt(i);
        }
        return result;
    }

    /**
     * gibt die relative Haeufigkeit des ntupels zurueck, sofern diese in der Hashtable relFreq
     * hinterlegt ist. Sonst wird -1.0 zurueck gegeben.
     *
     * @param ntupel String, dessen relative Haeufigkeit zurueck gegeben werden soll
     * @return double Wert, realtive haeufigkeit des angefragten n-Tupels.
     */
    public double getRelFrequency(String ntupel) {
        double result;
        if (relFreq.containsKey(ntupel)) {
            result = Double.parseDouble(relFreq.get(ntupel).toString());
        } else {
            result = -1.0;
        }
        return result;
    }

    /**
     * berechnet die absoluten Haeufigkeiten der gefundenen n-Tupeln. Diese werden in der Hashtable
     * absFreq gespeichert.
     *
     * @param n Laenge der zu betrachtenden n-Tupel
     */
    private void countAbsFreq(int n) {
        String ntupel;
        for (int i = 0; i <= (filteredText.length() - n); i++) {
            ntupel = filteredText.substring(i, (i + n));
            insertInAbsFreq(ntupel);
        }
        if (n > 1) {
            for (int i = 0; i <= (n - 2); i++) {
                ntupel = filteredText.substring(filteredText.length() - (n - (i + 1))) + filteredText.substring(0, (1 + i));
                insertInAbsFreq(ntupel);
            }
        }
    }

    /**
     * Hilfsmethode zum einfuegen eines Tupels in die Hashtable absFreq. Ist das Tupel bereits
     * vorhanden, wird der zugehoerige Zaehler inkrementiert, ansonsten wird das Tupel neu
     * eingefuegt.
     *
     * @param ntupel einzufuegendes bzw zu inkrementierendes Tupel
     */
    private void insertInAbsFreq(String ntupel) {
        int counter = 0;
        if (!(absFreq.containsKey(ntupel))) {
            absFreq.put(ntupel, (int) 1);
        } else {
            counter = ((Integer) absFreq.get(ntupel)).intValue();
            counter++;
            absFreq.put(ntupel, counter);
        }
    }

    /**
     * berechnet die relativen Haeufigkeiten von n-Tupeln. Diese werden in Hashtable relFreq
     * gespeichert.
     *
     * @param n Laenge der zu betrachtenden n-Tupel
     */
    private void countRelFreq(int n, double length) {
        String ntupel;
        double rf;
        Enumeration<String> e = absFreq.keys();
        while (e.hasMoreElements()) {
            ntupel = e.nextElement().toString();
            if (ntupel.length() == n) {
                int anzahl = Integer.parseInt(absFreq.get(ntupel).toString());
                rf = ((double) anzahl) / (double) length;
                relFreq.put(ntupel, rf);
            }
        }
    }

    /**
     * berechnet die bedingte Wahrscheinlichkeit (conditional probability) des n-ten Zeichens eines
     * n-tupels, indem es dem vorhergehenden (n-1)-Tupels die Auftrittswahrscheinlichkeit des
     * Nachfolgebuchstabens zuordnet. Bei n=1 wird die Auftrittswahrscheinlichkeit dieses
     * Buchstabens zurueck gegeben.
     *
     * @param ntupel zu betrachtendes n-tupel, von dem die Auftrittswahrscheinlichkeit des n-ten
     *        Buchstabens berechnet werden soll.
     * @return Aufftrittswahrscheinlichkeit des n-ten Zeichens als double Wert.
     */
    private double calcCondProb(String ntupel) {
        if (ntupel.length() == 1) return Double.parseDouble(relFreq.get(ntupel).toString());
        double res = 0.0;
        String ktupel = ntupel.substring(0, ntupel.length() - 1);
        double rfntupel = Double.parseDouble(relFreq.get(ntupel).toString());
        double rfktupel = Double.parseDouble(relFreq.get(ktupel).toString());
        res = (rfntupel / rfktupel);
        return res;
    }

    /**
     * gibt den Logarithmus vom Argument a zur Basis 2 zurueck
     *
     * @param a Argument der Logarithmusfunktion
     * @return double Wert, der Logarithmus von a zur Basis 2
     */
    public static double log2(double a) {
        return (Math.log(a) / Math.log((double) 2));
    }

    /**
     * rundet den Wert d auf l-viele Nachkommastellen
     *
     * @param d zu rundender double Wert,
     * @param l Anzahl der Nachkommastellen
     * @return double Wert, auf l Nachkomaastellen gerundeter Wert von d
     */
    public static double roundDouble(double d, int l) {
        double result = d * Math.pow(10.0, (double) l);
        result = Math.rint(result);
        result = result / Math.pow(10.0, (double) l);
        return result;
    }

    /**
     * liefert das Ergebnis von x hoch p als Integer-Wert.
     *
     * @param x Basis
     * @param p Exponent
     * @return Integer Wert von x hoch p.
     */
    public static int intPower(int x, int p) {
        int result = x;
        for (int i = 1; i < p; i++) {
            result *= x;
        }
        return result;
    }
}
