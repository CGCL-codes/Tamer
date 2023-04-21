package com.lowagie.text.pdf.interfaces;

public interface PdfXConformance {

    /**
     * Sets the PDF/X conformance level.
     * Allowed values are PDFX1A2001, PDFX32002, PDFA1A and PDFA1B.
     * It must be called before opening the document.
     * @param pdfxConformance the conformance level
     */
    public void setPDFXConformance(int pdfxConformance);

    /**
	 * Getter for the PDF/X Conformance value.
	 * @return the pdfxConformance
	 */
    public int getPDFXConformance();

    /**
     * Checks if the PDF/X Conformance is necessary.
     * @return true if the PDF has to be in conformance with any of the PDF/X specifications
     */
    public boolean isPdfX();
}
