package org.jpedal.external;

/**
 * allow user to recieve raw glyph data as generated
 */
public interface GlyphTracker {

    /**
     * pass user the low-level details
     * @param trm - the Trm matrix (x,y is Trm[2][0], Trm[2][1]), other values are width (usually Trm[0][0] unless
     * rotated when could be Trm[0][1]) and height (usually Trm[1][1] or sometimes Trm[1][0]) Trm is defined in PDF
     * specification
     * @param rawInt - value found in TJ command (usually encoded)
     * @param displayValue - unicode display value from rawInt
     * @param extractionValue - unicode display value from rawInt
     */
    void addGlyph(float[][] trm, int rawInt, String displayValue, String extractionValue);
}
