package org.axsl.psR;

/**
 * <p>EncodingVector implementations represent what PostScript and PDF call
 * an Encoding, that is a mapping from character to glyph index that is
 * based on the character's location in an array (vector).</p>
 *
 * <p>To explain the terminology used, the aXSL superinterface {@link Encoding}
 * represents a superset of the PostScript/PDF concept, by including other
 * encoding schemes as well, specifically CMaps.
 */
public interface EncodingVector extends Encoding {

    /**
     * Method useful for determining what, if any, EncodingVector should be
     * used as the base encoding when writing this EncodingVector in PDF.
     * In PDF, an EncodingVector can be written as a set of differences from
     * one of the standard PDF EncodingVectors. (There does not seem to be a
     * similar capability in PostScript).
     * @return The best base-encoding that should be used when writing this
     * encoding to PostScript, or null if this encoding vector should be
     * written in its entirety.
     */
    EncodingVector bestBaseEncodingPDF();
}
