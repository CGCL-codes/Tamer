package net.sourceforge.syncyoursecrets.util;

/**
 * The Class ParseException signals errors when parsing the xml content.
 */
public class SysParseException extends SysXmlBaseException {

    /**
	 * Generated Id.
	 */
    private static final long serialVersionUID = -6011268570687577159L;

    /**
	 * Instantiates a new parses the exception.
	 */
    public SysParseException() {
    }

    /**
	 * Instantiates a new parses the exception.
	 * 
	 * @param message
	 *            the message
	 */
    public SysParseException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new parses the exception.
	 * 
	 * @param cause
	 *            the cause
	 */
    public SysParseException(Throwable cause) {
        super(cause);
    }

    /**
	 * Instantiates a new parses the exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
    public SysParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
