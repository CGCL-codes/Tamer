package com.tcmj.common.tools.text;

import java.util.Random;

/**
 * Extension to java.util.Random to provide methods to create random words,
 * charactersand hex values in various formats.
 * @author tcmj - Thomas Deutsch
 * @since 2003
 * @version $Revision: $
 */
public class RandomStrings extends Random {

    private static final long serialVersionUID = -3193018567428231419L;

    /**
     * Alle Ziffern des Hexadezimal-Systems.
     */
    private static final char HEXCHARS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * Alle Ziffern des Alphabets (gross und klein).
     */
    private static final char STRCHARS[] = { 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', 'g', 'G', 'h', 'H', 'i', 'I', 'j', 'J', 'k', 'K', 'l', 'L', 'm', 'M', 'n', 'N', 'o', 'O', 'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S', 't', 'T', 'u', 'U', 'v', 'V', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z' };

    /**
     * Alle Kleinbuchstaben.
     */
    private static final char SCHARS[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /**
     * alle Grossbuchstaben.
     */
    private static final char BCHARS[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    /**
     * Buchstaben zur Verwendung der Wortgenerierung.
     */
    private static final char[][] RANDOM_STR = { { 'a', 'e', 'i', 'o', 'u' }, { 'b', 'd', 'f', 'g', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'w' } };

    /**
     * Konstruktor. ruft die Superklasse auf.
     */
    public RandomStrings() {
        super();
    }

    /**
     * Konstruktor. ruft die Superklasse auf.
     * @param init Zahl zur Initialisierung des Zufallszahlengenerators
     */
    public RandomStrings(long init) {
        super(init);
    }

    /**
     * Gibt eine Hexadezimal-Zahl per Zufall zurueck.
     * @return Hexadezimalzahl
     */
    public String nextHex() {
        String hex;
        int a = Math.abs(nextInt()) % 16;
        int b = Math.abs(nextInt()) % 16;
        hex = String.valueOf(HEXCHARS[a]) + String.valueOf(HEXCHARS[b]);
        return hex;
    }

    /**
     * Gibt ein Zeichen per Zufall zurueck.
     * @return 1 Zeichen im Bereich von a-z und A-Z
     */
    public char nextCharacter() {
        int r = this.nextInt(STRCHARS.length);
        return STRCHARS[r];
    }

    /**
     * Gibt ein kleines Zeichen per Zufall zurueck.
     * @return 1 Zeichen im Bereich von a-z
     */
    public char nextCharLowerCase() {
        int r = this.nextInt(SCHARS.length);
        return SCHARS[r];
    }

    /**
     * Gibt ein grosses Zeichen per Zufall zurueck.
     * @return 1 Zeichen im Bereich von A-Z
     */
    public char nextCharUpperCase() {
        int r = this.nextInt(BCHARS.length);
        return BCHARS[r];
    }

    /**
     * Gibt einen String mit Buchstaben per Zufall zurueck.
     * @param len Laenge des benoetigten Strings
     * @return Zeichen im Bereich von a-z und A-Z
     */
    public String nextString(int len) {
        String back = "";
        for (int i = 0; i < len; i++) {
            int r = this.nextInt(STRCHARS.length);
            back = back.concat(String.valueOf(STRCHARS[r]));
        }
        return back;
    }

    /**
     * Gibt einen String mit kleinen Buchstaben per Zufall zurueck.
     * @param len Laenge des benoetigten Strings
     * @return Zeichen im Bereich von a-z
     */
    public String nextStringLowerCase(int len) {
        String back = "";
        for (int i = 0; i < len; i++) {
            int r = this.nextInt(SCHARS.length);
            back = back.concat(String.valueOf(SCHARS[r]));
        }
        return back;
    }

    /**
     * Gibt einen String mit Buchstaben per Zufall zurueck.
     * @param len Laenge des benoetigten Strings
     * @return Zeichen im Bereich von a-z und A-Z
     */
    public String nextStringUpperCase(int len) {
        String back = "";
        for (int i = 0; i < len; i++) {
            int r = this.nextInt(BCHARS.length);
            back = back.concat(String.valueOf(BCHARS[r]));
        }
        return back;
    }

    /**
     * Gibt einen String mit Buchstaben per Zufall zurueck.
     * Dabei ist der erste Buchstabe gross und alle anderen klein.
     * @param len Laenge des benoetigten Strings
     * @return Zeichen im Bereich von a-z und A-Z
     */
    public String nextStringCapitalized(int len) {
        String back = "";
        if (len > 0) {
            int r = this.nextInt(BCHARS.length);
            back = String.valueOf(BCHARS[r]);
            for (int i = 1; i < len; i++) {
                r = this.nextInt(SCHARS.length);
                back = back.concat(String.valueOf(SCHARS[r]));
            }
        }
        return back;
    }

    /**
     * Erzeugt ein Wort bestimmter Laenge in Kleinbuchstaben.<br>
     * Es werden Woerter erzeugt, die lesbar sind. Dies wird dadurch erreicht,
     * dass immer abwechselnd ein Vokal und ein Konsonant zusammengesetzt wird.<br>
     * Es werden nicht alle Buchstaben des Alphabets verwendet.
     * @param length Laenge des zu erzeugenden Wortes
     * @return das Zufalls-Wort
     */
    public String randomWordCapitalized(int length) {
        char[] res = new char[length];
        int toggle = 1, max = 0;
        for (int i = 0; i < length; i++) {
            max = RANDOM_STR[toggle].length;
            if (i == 0) {
                char lowchar = RANDOM_STR[toggle][nextInt(max)];
                res[i] = Character.toUpperCase(lowchar);
            } else {
                res[i] = RANDOM_STR[toggle][nextInt(max)];
            }
            toggle = 1 - toggle;
        }
        return new String(res);
    }

    /**
     * Erzeugt ein Wort bestimmter Laenge in Kleinbuchstaben.<br>
     * Es werden Woerter erzeugt, die lesbar sind. Dies wird dadurch erreicht,
     * dass immer abwechselnd ein Vokal und ein Konsonant zusammengesetzt wird.<br>
     * Es werden nicht alle Buchstaben des Alphabets verwendet.
     * @param length Laenge des zu erzeugenden Wortes
     * @return das Zufalls-Wort
     */
    public String randomWord(int length) {
        char[] res = new char[length];
        int toggle = 1, max = 0;
        for (int i = 0; i < length; i++) {
            max = RANDOM_STR[toggle].length;
            res[i] = RANDOM_STR[toggle][nextInt(max)];
            toggle = 1 - toggle;
        }
        return new String(res);
    }
}
