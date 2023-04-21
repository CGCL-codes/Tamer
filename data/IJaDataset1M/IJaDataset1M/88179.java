package de.maramuse.soundcomp.parser;

import de.maramuse.soundcomp.parser.SCParser.ParserVal;

/**
 * this symbol class represents a "parameter change" command
 */
public class ParamChange extends ParserVal {

    ParamChange(String s) {
        super(SCParser.PARAMCHANGE, s);
    }
}
