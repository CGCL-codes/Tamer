package Wider;

/**
 * Einfache exception die geworfen wird, falls in der Abarbeitung
 * der Eingabe ein fehler auftritt.
 * @see Scanner
 * @see Parser
 * @see Calculator
 */
public class WiderException extends Exception {

    public WiderException(String s) {
        super(s);
    }
}
