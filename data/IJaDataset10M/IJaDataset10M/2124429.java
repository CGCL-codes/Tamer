package call2arms.services;

import call2arms.parser.BF2sParser;
import call2arms.parser.Parser;

/**
 *
 * @author Alex
 */
public abstract class ParserService {

    static Parser _parser = new BF2sParser();

    public static Parser getParser() {
        return _parser;
    }
}
