package org.tzi.use.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import junit.framework.TestCase;

/**
 * Test Report class.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public class ReportTest extends TestCase {

    public void test1() {
        Report r = new Report(4, "[ $c = $r, $r, $l ]");
        r.addRuler('-');
        r.addRow();
        r.addCell("foo");
        r.addCell(new Integer(3));
        r.addCell(new Double(1.2));
        r.addCell(Boolean.FALSE);
        r.addRow();
        r.addCell("foobar");
        r.addCell(new Integer(453453));
        r.addCell(new Double(-1.245345345));
        r.addCell(Boolean.TRUE);
        r.addRow();
        r.addCell("line");
        r.addCell(new Integer(555));
        r.addCell(new Double(999.0));
        r.addCell(Boolean.TRUE);
        r.addRuler('=');
        StringWriter sw1 = new StringWriter();
        PrintWriter p1 = new PrintWriter(sw1);
        r.printOn(p1);
        p1.flush();
        StringWriter sw2 = new StringWriter();
        PrintWriter p2 = new PrintWriter(sw2);
        p2.println("----------------------------------------");
        p2.println("[  foo   =      3,          1.2, false ]");
        p2.println("[ foobar = 453453, -1.245345345, true  ]");
        p2.println("[  line  =    555,        999.0, true  ]");
        p2.println("========================================");
        p2.flush();
        assertEquals(sw1.toString(), sw2.toString());
    }
}
