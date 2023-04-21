package pobs.parser;

import pobs.PContext;
import pobs.PMatch;
import pobs.PScanner;

/**
 * Matches if the next character on the input matches any of a specified
 * characters.
 * Uses case sensitivity directive.
 * BNF: <code>parser := 'a' | 'b' | .. </code> (where any number of characters
 * may be specified) 
 * @author	Martijn W. van der Lee
 */
public class PSet extends pobs.PParser {

    private java.lang.String set;

    /**
     * Sole constructor.
     * @param	set		a string containing the set of characters.
     */
    public PSet(java.lang.String set) {
        this.set = set;
        setErrorInfo("a character from '" + set + "'");
    }

    public PMatch parse(PScanner input, long begin, PContext context) {
        if (begin < input.length() && set.indexOf(context.getDirective().convert(input.charAt(begin))) >= 0) {
            return PMatch.TRUE;
        }
        return PMatch.FALSE;
    }
}
