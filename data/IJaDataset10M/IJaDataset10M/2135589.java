package org.incava.jagol;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;
import org.incava.log.Log;

public class TestBooleanOption extends TestCase {

    BooleanOption opt = new BooleanOption("boolopt", "this is the description of boolopt");

    public TestBooleanOption(String name) {
        super(name);
    }

    public void testDefaultNull() {
        assertEquals("boolopt", opt.getLongName());
        assertEquals("this is the description of boolopt", opt.getDescription());
        assertNull("default value", opt.getValue());
    }

    public void testDefaultValue() {
        BooleanOption opt = new BooleanOption("boolopt", "this is the description of boolopt", Boolean.TRUE);
        assertEquals("default value", Boolean.TRUE, opt.getValue());
    }

    public void testShortName() {
        opt.setShortName('n');
        assertEquals('n', opt.getShortName());
    }

    public void testSetBooleanValue() {
        opt.setValue(Boolean.TRUE);
        assertEquals("option value", Boolean.TRUE, opt.getValue());
        opt.setValue(Boolean.FALSE);
        assertEquals("option value", Boolean.FALSE, opt.getValue());
    }

    public void testSetFromArgsListPositive() {
        List args = new ArrayList();
        try {
            boolean processed = opt.set("--boolopt", args);
            assertEquals("option processed", true, processed);
            assertEquals("option value", Boolean.TRUE, opt.getValue());
            assertEquals("argument removed from list", 0, args.size());
        } catch (OptionException ite) {
            fail("failure is not an option");
        }
    }

    public void testSetFromArgsListNegativeDash() {
        List args = new ArrayList();
        try {
            boolean processed = opt.set("--no-boolopt", args);
            assertEquals("option processed", true, processed);
            assertEquals("option value", Boolean.FALSE, opt.getValue());
            assertEquals("argument removed from list", 0, args.size());
        } catch (OptionException ite) {
            fail("failure is not an option");
        }
    }

    public void testSetFromArgsListNegativeNoDash() {
        List args = new ArrayList();
        try {
            boolean processed = opt.set("--noboolopt", args);
            assertEquals("option processed", true, processed);
            assertEquals("option value", Boolean.FALSE, opt.getValue());
            assertEquals("argument removed from list", 0, args.size());
        } catch (OptionException ite) {
            fail("failure is not an option");
        }
    }
}
