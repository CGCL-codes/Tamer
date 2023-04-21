package cyk.model.exceptions;

/**
 * Exception für eine ungültige Regel.
 * 
 * @author David
 *
 */
@SuppressWarnings("serial")
public class RuleException extends Exception {

    /**
	 * Erzeugt eine neue RuleException
	 * @param s
	 * 		zu übergebender Text
	 */
    public RuleException(String s) {
        super(s);
    }
}
