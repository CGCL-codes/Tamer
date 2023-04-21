package net.sourceforge.numbertrans.languages.malayalam;

import net.sourceforge.numbertrans.framework.parser.CharacterMappedCardinalParser;

public class MalayalamCardinalParser extends CharacterMappedCardinalParser {

    public static final char[] DIGITS = { '൦', '൧', '൨', '൩', '൪', '൫', '൬', '൭', '൮', '൯' };

    /**
         * Create a new <code>MalayalamCardinalParser</code> object.
         */
    public MalayalamCardinalParser() {
        super(DIGITS);
    }
}
