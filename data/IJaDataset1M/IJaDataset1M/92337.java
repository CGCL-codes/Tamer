package net.openchrom.chromatogram.msd.identifier.supplier.peak.exceptions;

public class NameMustNotBeNullException extends Exception {

    /**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
    private static final long serialVersionUID = -3183603008562746739L;

    public NameMustNotBeNullException() {
        super();
    }

    public NameMustNotBeNullException(String message) {
        super(message);
    }
}
