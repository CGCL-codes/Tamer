package lexer;

import org.junit.Test;
import org.junit.Assert;

public class LexerTests {

    @Test
    public void simpleTest() {
        UniversalLexer lexer = UniversalLexer.create("a + 2");
        Assert.assertEquals("a", lexer.getLexeme("a"));
        Assert.assertEquals("+", lexer.getLexeme("\\+"));
        Assert.assertEquals("2", lexer.getLexeme("[0-9]+"));
    }
}
