package org.sodeja.parsec.examples.bp;

import java.io.Reader;
import org.sodeja.parsec.lexer.AbstractLexer;
import org.sodeja.parsec.lexer.LexerHelper;

public class BPLexer extends AbstractLexer<String> {

    public BPLexer(Reader originalReader) {
        super(originalReader);
    }

    @Override
    protected String createTokenFrom(String str) {
        return str;
    }

    @Override
    protected void tokenizeDelegate(char ch) {
        if (Character.isWhitespace(ch)) {
            return;
        }
        if (Character.isDigit(ch)) {
            LexerHelper.readNumberToken(this, ch);
            return;
        }
        if (Character.isJavaIdentifierStart(ch)) {
            LexerHelper.readIdentifier(this, ch);
            return;
        }
        if (ch == '\"') {
            LexerHelper.readString(this, ch);
            return;
        }
        tokens.add(String.valueOf(ch));
    }
}
