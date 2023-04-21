package nl.adaptivity.parser.eval;

import nl.adaptivity.parser.BufferedTokenStream;
import nl.adaptivity.parser.languages.CharStreamEnum;
import nl.adaptivity.parser.streams.ExpressionParser;
import nl.adaptivity.parser.tokens.CharToken;

/**
 * A interface for a factory that creates expression parser objects.
 * 
 * @author Paul de Vrieze
 * @version 0.1 $Revision: 477 $
 */
public interface ExpressionParserFactory {

    /**
   * Create a new {@link ExpressionParser} for the given stream.
   * 
   * @param pStream The stream that will be parsed.
   * @return A parser for this stream.
   */
    ExpressionParser getExpressionParser(BufferedTokenStream<? extends CharToken, CharStreamEnum> pStream);
}
