package net.sourceforge.nrl.parser.ast.constraints.impl;

import net.sourceforge.nrl.parser.NRLParserTestCase;
import net.sourceforge.nrl.parser.ast.constraints.IDecimalNumber;
import net.sourceforge.nrl.parser.ast.impl.NRLActionParser;

public class DecimalNumberTest extends NRLParserTestCase {

    public void testParse() throws Exception {
        NRLActionParser parser = getParserFor("5.0");
        IDecimalNumber number = (IDecimalNumber) parser.constraint().getTree();
        assertEquals(5.0, number.getNumber(), 0);
        parser = getParserFor("-5.0");
        number = (IDecimalNumber) parser.constraint().getTree();
        assertEquals(-5, number.getNumber(), 0);
    }
}
