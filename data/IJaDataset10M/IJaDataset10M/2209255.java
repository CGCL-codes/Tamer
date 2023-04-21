package org.xBaseJ.test;

import junit.framework.TestCase;
import org.xBaseJ.fields.NumField;

public class NumTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(NumTest.class);
    }

    public void testDouble() {
        try {
            NumField nf = new NumField("name", 6, 2);
            double a = -50000000.36;
            nf.put(a);
            assertEquals(nf.get(), "-00.36");
            a = 50000000.36;
            nf.put(a);
            assertEquals(nf.get(), "000.36");
            a = -.36;
            nf.put(a);
            assertEquals(nf.get(), "  -.36");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testNull() {
        try {
            NumField nf = new NumField("name", 6, 2);
            nf.put("");
            assertEquals(nf.get(), "");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
