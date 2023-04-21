package org.rickmurphy.monitor;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class SORNParser extends antlr.LLkParser implements SORNParserTokenTypes {

    protected SORNParser(TokenBuffer tokenBuf, int k) {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }

    public SORNParser(TokenBuffer tokenBuf) {
        this(tokenBuf, 1);
    }

    protected SORNParser(TokenStream lexer, int k) {
        super(lexer, k);
        tokenNames = _tokenNames;
    }

    public SORNParser(TokenStream lexer) {
        this(lexer, 1);
    }

    public SORNParser(ParserSharedInputState state) {
        super(state, 1);
        tokenNames = _tokenNames;
    }

    public final void run() throws RecognitionException, TokenStreamException {
        try {
            switch(LA(1)) {
                case ALPHA:
                    {
                        match(ALPHA);
                        break;
                    }
                case NEWLINE:
                    {
                        match(NEWLINE);
                        break;
                    }
                default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_0);
        }
    }

    public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "ALPHA", "NEWLINE", "NUMERIC" };

    private static final long[] mk_tokenSet_0() {
        long[] data = { 2L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
}
