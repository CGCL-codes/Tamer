package org.openswift.interpreter.integration;

import java.io.IOException;
import org.antlr.runtime.RecognitionException;
import org.openswift.interpreter.SwiftParser;
import org.openswift.interpreter.integration.base.AbstractInterpreterSwiftTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class will test the user header block. 
 * 
 * {3: User Header Block} 
 * This is an optional block and has the following structure: 
 * {3:  {113:xxxx}  {108:abcdefgh12345678}     } 
 * (a)      (b)             (c) 
 * a) 
 * 3: = Block ID (always 3) 
 * b) 
 * 113:xxxx = Optional banking priority code 
 * c) 
 * This is the Message User Reference (MUR) used by applications for reconciliation with ACK. 
 * 
 * @author Luca Li Greci
 */
@Test(groups = { "integration" })
public class UserBlockTest extends AbstractInterpreterSwiftTest {

    @Test(dataProvider = "validDataSet")
    public void validUserBlockTest(String userBlockTest) throws IOException, RecognitionException {
        SwiftParser parser = createParser(userBlockTest);
        parser.userHeaderBlock();
    }

    @Test(expectedExceptions = RecognitionException.class, dataProvider = "invalidDataSet")
    public void invalidUserBlockTest(String userBlockTest) throws IOException, RecognitionException {
        SwiftParser parser = createParser(userBlockTest);
        parser.userHeaderBlock();
    }

    /**
	 * @see org.openswift.interpreter.integration.base.AbstractInterpreterSwiftTest#createInvalidDataSet()
	 */
    @Override
    @DataProvider(name = "invalidDataSet")
    public Object[][] createInvalidDataSet() {
        return new Object[][] { new Object[] { "{3:{113:}{108:ABCDEFGH12345678}}}" }, new Object[] { "{3:{113:EBA}{108:}}}" }, new Object[] { "{3:{113:}{108:}}}" }, new Object[] { "{3:{108:}}}" }, new Object[] { "{3:{113:}}}" }, new Object[] { "{3:}}" } };
    }

    /**
	 * @see org.openswift.interpreter.integration.base.AbstractInterpreterSwiftTest#createValidDataSet()
	 */
    @Override
    @DataProvider(name = "validDataSet")
    public Object[][] createValidDataSet() {
        return new Object[][] { new Object[] { "{3:{113:EBA}{108:ABCDEFGH12345678}}}" }, new Object[] { "{3:{108:ABCDEFGH12345678}}}" } };
    }
}
