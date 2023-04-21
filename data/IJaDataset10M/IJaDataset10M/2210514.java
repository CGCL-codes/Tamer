package org.oclc.da.ndiipp.extraction.exceptions;

/**
 * 
 */
public class InvalidPDFResourceException extends InvalidResourceException {

    /**
     * Invalid PDF Resource Exception
     */
    public InvalidPDFResourceException() {
    }

    /**
     * Invalid PDF Resource Exception
	 * @param string Supplied string
     */
    public InvalidPDFResourceException(String string) {
        super(string);
    }

    /**
     * Invalid PDF Resource Exception
     * @param throwable Supplied exception
     */
    public InvalidPDFResourceException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Invalid PDF Resource Exception
     * @param string Supplied string
     * @param throwable Supplied exception
     */
    public InvalidPDFResourceException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
