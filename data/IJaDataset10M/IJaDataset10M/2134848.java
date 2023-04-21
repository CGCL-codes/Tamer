package net.sf.etl.parsers.internal.lexer;

import net.sf.etl.parsers.Lexer;
import net.sf.etl.parsers.LexerFactory;

/**
 * A factory for default lexer.
 * 
 * @author const
 */
public class DefaultLexerFactory extends LexerFactory {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Lexer newLexer() {
        return new DefaultLexer();
    }
}
