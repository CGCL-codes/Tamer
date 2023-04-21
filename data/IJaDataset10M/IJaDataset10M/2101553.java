package org.openswift.interpreter.lexer;

import org.antlr.runtime.RecognitionException;
import org.openswift.interpreter.SwiftLexer;
import org.openswift.interpreter.lexer.base.AbstractSwiftLexerTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Luca Li Greci
 */
public class StartUserHeaderBlockTest extends AbstractSwiftLexerTest {

    @Override
    public void executeLexerRule(SwiftLexer lexer) throws Exception {
        lexer.mSTART_USER_HEADER_BLOCK();
    }

    @Override
    public int getExpectedTokenType() {
        return SwiftLexer.START_USER_HEADER_BLOCK;
    }

    @Test(groups = { "lexer" }, dataProvider = "validDataSet")
    public void validBasicHeaderBlock(String basicHeaderBlock) throws Exception {
        assertRecognize(basicHeaderBlock);
    }

    @Test(groups = { "lexer" }, dataProvider = "invalidDataSet", expectedExceptions = RecognitionException.class)
    public void invalidBasicHeaderBlock(String basicHeaderBlock) throws Exception {
        assertRecognize(basicHeaderBlock);
    }

    @Override
    @DataProvider(name = "invalidDataSet")
    public Object[][] createInvalidDataSet() {
        return new Object[][] { new Object[] { "{" }, new Object[] { "3:" }, new Object[] { "{33:" }, new Object[] { "{1:" }, new Object[] { "{2:" }, new Object[] { "{4:" }, new Object[] { "{5:" }, new Object[] { "{XX:" } };
    }

    @Override
    @DataProvider(name = "validDataSet")
    public Object[][] createValidDataSet() {
        return new Object[][] { new Object[] { "{3:" } };
    }
}
