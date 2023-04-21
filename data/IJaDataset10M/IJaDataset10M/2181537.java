package com.itextpdf.text.pdf;

/**
 *
 * @author  psoares
 */
public class PdfXConformanceException extends RuntimeException {

    private static final long serialVersionUID = 9199144538884293397L;

    /** Creates a new instance of PdfXConformanceException. */
    public PdfXConformanceException() {
    }

    /**
     * Creates a new instance of PdfXConformanceException.
     * @param s
     */
    public PdfXConformanceException(String s) {
        super(s);
    }
}
