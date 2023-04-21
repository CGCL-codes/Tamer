package abbot.tester;

import java.awt.Point;
import junit.extensions.abbot.TestHelper;
import junit.framework.TestCase;

public class JTableHeaderLocationTest extends TestCase {

    public void testParsePoint() {
        JTableHeaderLocation loc = new JTableHeaderLocation();
        String parse = "(1,1)";
        assertEquals("Badly parsed: " + parse, new JTableHeaderLocation(new Point(1, 1)), loc.parse(parse));
    }

    public void testParseColumn() {
        JTableHeaderLocation loc = new JTableHeaderLocation();
        String parse = "[1]";
        assertEquals("Badly parsed: " + parse, new JTableHeaderLocation(1), loc.parse(parse));
        parse = " [ 10 ] ";
        assertEquals("Badly parsed: " + parse, new JTableHeaderLocation(10), loc.parse(parse));
    }

    public void testParseValue() {
        JTableHeaderLocation loc = new JTableHeaderLocation();
        String parse = "\"some name\"";
        assertEquals("Badly parsed: " + parse, new JTableHeaderLocation(parse.substring(1, parse.length() - 1)), loc.parse(parse));
    }

    public JTableHeaderLocationTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, JTableHeaderLocationTest.class);
    }
}
