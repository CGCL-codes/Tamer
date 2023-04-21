package net.sourceforge.ondex.parser.ec;

import net.sourceforge.ondex.parser.AbstractONDEXParserTest;

/**
 * Test for the EC database parser.
 * 
 * @author taubertj
 *
 */
public class ParserTest extends AbstractONDEXParserTest {

    public ParserTest() {
        super(Parser.class);
    }

    @Override
    public String getInputDir() {
        return System.getProperty("ondex.dir") + "/importdata/ec";
    }

    @Override
    public void postParserTest() throws Exception {
        super.writeOutputAndTestTheResult();
    }

    @Override
    public void preParserTest() {
        pa.addOption("Deleted", Boolean.TRUE);
    }
}
