package de.maramuse.soundcomp.parser;

import de.maramuse.soundcomp.parser.SCParser.ParserVal;

/**
 * this symbol class represents a closing brace
 */
public class RBrace extends ParserVal {

    RBrace(String s) {
        super(SCParser.RBRACE, s);
    }
}
