package net.sf.joafip.autosave.spellcheck;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class SpellCheckException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3735427947716224960L;

    public SpellCheckException() {
        super();
    }

    public SpellCheckException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SpellCheckException(final String message) {
        super(message);
    }

    public SpellCheckException(final Throwable cause) {
        super(cause);
    }
}
