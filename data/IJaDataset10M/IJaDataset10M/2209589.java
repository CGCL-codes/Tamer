package pobs.parser;

import pobs.PContext;
import pobs.PMatch;
import pobs.PObject;
import pobs.PParser;
import pobs.PScanner;

/**
 * Wrapper of a {@link PObject} makes it a {@link PParser}.
 * 
 * @author Franz-Josef Elmer
 */
public class PWrapper extends PParser {

    private final PObject parser;

    public PWrapper(PObject parser) {
        this.parser = parser;
    }

    protected PMatch parse(PScanner input, long begin, PContext context) {
        return parser.process(input, begin, context);
    }
}
