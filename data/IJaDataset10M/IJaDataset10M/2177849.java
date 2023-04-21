package net.sourceforge.nrl.parser.ast.constraints.impl;

import net.sourceforge.nrl.parser.NRLParserTestCase;
import net.sourceforge.nrl.parser.ast.IModelReference;
import net.sourceforge.nrl.parser.ast.constraints.IConcatenatedReport;
import net.sourceforge.nrl.parser.ast.constraints.IIntegerNumber;
import net.sourceforge.nrl.parser.ast.constraints.ILiteralString;
import net.sourceforge.nrl.parser.ast.impl.NRLActionParser;

public class ConcatenatedReportImplTest extends NRLParserTestCase {

    public void testParse() throws Exception {
        NRLActionParser parser = getParserFor("'a'");
        IConcatenatedReport rep = (IConcatenatedReport) parser.concatenatedReport().getTree();
        assertEquals(1, rep.getExpressions().size());
        assertTrue(rep.getExpressions().get(0) instanceof ILiteralString);
        parser = getParserFor("'a' + foobar + 23");
        rep = (IConcatenatedReport) parser.concatenatedReport().getTree();
        assertEquals(3, rep.getExpressions().size());
        assertTrue(rep.getExpressions().get(0) instanceof ILiteralString);
        assertTrue(rep.getExpressions().get(1) instanceof IModelReference);
        assertTrue(rep.getExpressions().get(2) instanceof IIntegerNumber);
    }
}
