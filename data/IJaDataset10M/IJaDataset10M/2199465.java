package de.maramuse.soundcomp.parser;

import de.maramuse.soundcomp.parser.SCParser.ParserVal;

/**
 * This symbol class represents the "as" keyword
 */
public class As extends ParserVal {

    As(String s) {
        super(SCParser.AS, s);
    }
}
