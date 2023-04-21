package org.jcryptool.visual.zeroknowledge.algorithm;

import java.math.BigInteger;
import java.util.Random;

/**
 * Klasse, die eine statische Methode zum Erstellen einer Primzahl bereitstellt
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Primzahlen {

    /**
     * Methode zum Generieren einer Primzahl im Bereich ]2^obergrenze, 2^untergrenze[
     * 
     * @param untergrenze dualer Logarithmus der unteren Intervallgrenze
     * @param obergrenze dualer Logarithmus der oberen Intervallgrenze
     * @return BigInteger, dessen Wert eine Primzahl im Bereich ]2^obergrenze, 2^untergrenze[ ist
     */
    public static BigInteger primzahl(int untergrenze, int obergrenze) {
        Random r = new Random();
        if (untergrenze >= obergrenze) {
            throw new IllegalArgumentException("Error.UPPER_LOWER");
        }
        BigInteger tmp;
        int anzahl = untergrenze + 1 + new Random().nextInt(obergrenze - untergrenze);
        tmp = BigInteger.probablePrime(anzahl, r);
        return tmp;
    }
}
