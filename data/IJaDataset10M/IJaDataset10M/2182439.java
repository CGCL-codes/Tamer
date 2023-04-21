package si.mk.util.gui;

import junit.framework.TestCase;

public class NumFmtTest extends TestCase {

    public void testFormatters() {
        double x = 0.1;
        String res;
        x = 0.12;
        res = NumFmt.coord(x);
        assertEquals("0.12", res);
        x = 0.123;
        res = NumFmt.coord(x);
        assertEquals("0.123", res);
        x = 0.1246;
        res = NumFmt.coord(x);
        assertEquals("0.125", res);
        x = 0.123456;
        res = NumFmt.coord(x);
        assertEquals("0.123", res);
        x = 1;
        res = NumFmt.coord(x);
        assertEquals("1", res);
        x = 12;
        res = NumFmt.coord(x);
        assertEquals("12", res);
        x = 123;
        res = NumFmt.coord(x);
        assertEquals("123", res);
        x = 1234;
        res = NumFmt.coord(x);
        assertEquals("1234", res);
        x = 12345;
        res = NumFmt.coord(x);
        assertEquals("12345", res);
        x = 123456;
        res = NumFmt.coord(x);
        assertEquals("123456", res);
        x = 1.1;
        res = NumFmt.coord(x);
        assertEquals("1.1", res);
        x = 1.12;
        res = NumFmt.coord(x);
        assertEquals("1.12", res);
        x = 1.123;
        res = NumFmt.coord(x);
        assertEquals("1.123", res);
        x = 1.1236;
        res = NumFmt.coord(x);
        assertEquals("1.124", res);
        x = 123456.1;
        res = NumFmt.coord(x);
        assertEquals("123456.1", res);
        x = 123456.12;
        res = NumFmt.coord(x);
        assertEquals("123456.12", res);
        x = 123456.123;
        res = NumFmt.coord(x);
        assertEquals("123456.123", res);
        x = 123456.1234;
        res = NumFmt.coord(x);
        assertEquals("123456.123", res);
        x = 123456.12356;
        res = NumFmt.coord(x);
        assertEquals("123456.124", res);
        x = 123456.123456;
        res = NumFmt.coord(x);
        assertEquals("123456.123", res);
    }

    public void testParsers() {
        double x = NumFmt.parse("1");
        x = NumFmt.parse("1.1");
        assertEquals(1.1, x, 1e-9);
        x = NumFmt.parse("1.12");
        assertEquals(1.12, x, 1e-9);
        x = NumFmt.parse("1.123");
        assertEquals(1.123, x, 1e-9);
        x = NumFmt.parse("1.1234");
        assertEquals(1.1234, x, 1e-9);
        x = NumFmt.parse("1.123456789");
        assertEquals(1.123456789, x, 1e-16);
        x = NumFmt.parse("12345678.9876543");
        assertEquals(12345678.9876543, x, 1e-9);
        System.out.println(x);
    }
}
