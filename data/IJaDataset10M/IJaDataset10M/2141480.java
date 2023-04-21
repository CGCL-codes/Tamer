package org.openswift.interpreter.lexer.base;

import static org.testng.Assert.assertEquals;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.Token;
import org.openswift.interpreter.SwiftLexer;

/**
 * @author Luca Li Greci
 */
public abstract class AbstractSwiftLexerTest {

    private SwiftLexer lexer;

    public abstract int getExpectedTokenType();

    public abstract void executeLexerRule(SwiftLexer lexer) throws Exception;

    public abstract Object[][] createInvalidDataSet();

    public abstract Object[][] createValidDataSet();

    protected void assertRangeOfCharacters(char fromChar, char toChar) throws Exception {
        for (char c = fromChar; c <= toChar; ++c) assertRecognize(c);
    }

    protected void assertRecognize(char c) throws Exception {
        assertRecognize(String.valueOf(c));
    }

    protected void assertRecognize(String text) throws Exception {
        Token token = recognize(text);
        assertEquals(token.getText(), text);
    }

    private Token recognize(String text) throws Exception {
        SwiftLexer lexer = setLexerStream(text);
        executeLexerRule(lexer);
        Token nextToken = lexer.emit();
        assertEquals(nextToken.getType(), getExpectedTokenType());
        lexer = setLexerStream(text);
        return lexer.nextToken();
    }

    protected AbstractSwiftLexerTest() {
        lexer = new SwiftLexer();
    }

    private SwiftLexer setLexerStream(String text) {
        lexer.setCharStream(new ANTLRStringStream(text));
        return lexer;
    }
}
