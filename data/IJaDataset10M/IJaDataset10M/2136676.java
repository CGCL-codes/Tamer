package de.maramuse.soundcomp.parser;

import de.maramuse.soundcomp.parser.SCParser.ParserVal;

/**
 * this symbol class represents the "scale reference" command. Not yet implemented, defaults to "scale"
 */
public class ScaleRef extends ParserVal {

    ScaleRef(String s) {
        super(SCParser.SCALE, s);
    }
}
