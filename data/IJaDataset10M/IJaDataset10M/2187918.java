package org.meta.common;

import java.io.*;

/**
 * Well the standard <code>StreamTokenizer</code> class does not detect
 * numbers which are in scientific notation (surpricingly!). So this is
 * an entension class which claims to do the same.
 *
 * @author  V.Ganesh
 * @version 2.0 (Part of MeTA v2.0)
 */
public class ScientificStreamTokenizer extends StreamTokenizer {

    /** Creates a new instance of ScientificStreamTokenizer */
    public ScientificStreamTokenizer(Reader r) {
        super(r);
        resetSyntax();
        eolIsSignificant(false);
        wordChars('$', 'z');
        commentChar('#');
        quoteChar('"');
        quoteChar('\'');
        whitespaceChars(0, ' ');
    }

    /**
     * overridden nextToken() method
     *
     * @return token type
     */
    @Override
    public int nextToken() throws IOException {
        super.ttype = super.nextToken();
        if (super.ttype == TT_WORD) {
            try {
                super.nval = Double.parseDouble(super.sval);
                super.ttype = TT_NUMBER;
            } catch (Exception ignored) {
            }
        }
        return super.ttype;
    }
}
