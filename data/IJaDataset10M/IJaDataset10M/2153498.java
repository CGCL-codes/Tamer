package de.fuberlin.wiwiss.ng4j.trig;

import java.io.Reader;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import de.fuberlin.wiwiss.ng4j.trig.parser.TriGAntlrLexer;
import de.fuberlin.wiwiss.ng4j.trig.parser.TriGAntlrParser;
import de.fuberlin.wiwiss.ng4j.trig.parser.TriGAntlrParserTokenTypes;

/**
 * The formal interface to the TriG parser.  Wraps up the antlr parser and lexer.
 * @author		Andy Seaborne
 * @author Richard Cyganiak (richard@cyganiak.de)
 * @version 	$Id: TriGParser.java,v 1.5 2010/02/25 14:28:22 hartig Exp $
 */
public class TriGParser implements TriGAntlrParserTokenTypes {

    private TriGAntlrLexer lexer = null;

    private TriGAntlrParser parser = null;

    public TriGParser(Reader r, TriGParserEventHandler h) {
        this.lexer = new TriGAntlrLexer(r);
        this.parser = createParser(h);
    }

    /**
	 * Runs the parsing process by calling the top level parser rule
	 */
    public void parse() throws RecognitionException, TokenStreamException {
        this.parser.document();
    }

    private TriGAntlrParser createParser(TriGParserEventHandler handler) {
        TriGAntlrParser result = new TriGAntlrParser(this.lexer);
        result.setEventHandler(handler);
        result.setLexer(this.lexer);
        return result;
    }
}
