package exception;

public class KundennummerBereitsVergebenException extends Exception {

    private static final long serialVersionUID = 100L;

    public KundennummerBereitsVergebenException(int aKundenNummer) {
        super("Die gewünschte Kundennummer \"" + aKundenNummer + "\" ist bereits vergeben, bitte wählen sie eine neue Nummer");
    }
}
