package nl.adaptivity.parser.streams;

import nl.adaptivity.parser.BufferedTokenStream;
import nl.adaptivity.parser.ForwardingTokenStream;
import nl.adaptivity.parser.PeekBuffer;
import nl.adaptivity.parser.UnexpectedTokenException;
import nl.adaptivity.parser.languages.AccessorTokens;
import nl.adaptivity.parser.languages.CharStreamEnum;
import nl.adaptivity.parser.tokens.CharToken;
import nl.adaptivity.parser.tokens.OperatorToken;

/**
 * A parser that parses accessors for symbols.
 * 
 * @author Paul de Vrieze
 * @version 0.1 $Revision: 477 $
 */
public class AccessorParser extends ForwardingTokenStream<AccessorTokens, OperatorToken<AccessorTokens>, CharStreamEnum, CharToken> {

    /**
   * @param pParentStream
   */
    public AccessorParser(final BufferedTokenStream<? extends CharToken, CharStreamEnum> pParentStream) {
        super(pParentStream);
    }

    /** {@inheritDoc} */
    @Override
    protected OperatorToken<AccessorTokens> readNextToken(final BufferedTokenStream<? extends CharToken, CharStreamEnum> pBuffer) throws UnexpectedTokenException {
        final PeekBuffer<? extends CharToken, CharStreamEnum> peek = pBuffer.peek();
        final CharToken firstToken = peek.getNextToken();
        if (firstToken == null) {
            return null;
        }
        switch(firstToken.getChar()) {
            case '[':
                {
                    peek.take();
                    return new OperatorToken<AccessorTokens>(AccessorTokens.ARRAYACCESSOPEN, firstToken.getPos());
                }
            case ']':
                {
                    peek.take();
                    return new OperatorToken<AccessorTokens>(AccessorTokens.ARRAYACCESSCLOSE, firstToken.getPos());
                }
            case '.':
                {
                    peek.take();
                    return new OperatorToken<AccessorTokens>(AccessorTokens.OBJECTACCESS, firstToken.getPos());
                }
            case '(':
                {
                    peek.take();
                    return new OperatorToken<AccessorTokens>(AccessorTokens.FUNCTIONACCESSOPEN, firstToken.getPos());
                }
            case ',':
                {
                    peek.take();
                    return new OperatorToken<AccessorTokens>(AccessorTokens.FUNCTIONACCESSSEPARATOR, firstToken.getPos());
                }
            case ')':
                {
                    peek.take();
                    return new OperatorToken<AccessorTokens>(AccessorTokens.FUNCTIONACCESSCLOSE, firstToken.getPos());
                }
            default:
                {
                    return null;
                }
        }
    }
}
