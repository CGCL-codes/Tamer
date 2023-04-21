package org.axsl.pdfW;

/**
 * A PDF Explicit Destination.
 */
public interface PDFExplicitDestination {

    /**
     * Create an implementation-specific PDFNamedDestination instance that
     * points to this explicit destination.
     * @param name The name that should be assigned to this named destination.
     * @return An implementation-specific PDFExplicitDestination instance.
     */
    PDFNamedDestination createPDFNamedDestination(String name);
}
