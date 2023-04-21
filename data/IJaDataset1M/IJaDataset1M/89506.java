package org.incava.jagol;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;
import org.incava.log.Log;

public class TestIntegerOption extends TestCase {

    IntegerOption opt = new IntegerOption("intopt", "this is the description of intopt");

    public TestIntegerOption(String name) {
        super(name);
    }

    public void testDefaultNull() {
        assertEquals("intopt", opt.getLongName());
        assertEquals("this is the description of intopt", opt.getDescription());
        assertNull("default value", opt.getValue());
    }

    public void testDefaultValue() {
        IntegerOption opt = new IntegerOption("intopt", "this is the description of intopt", new Integer(1012));
        assertEquals("default value", new Integer(1012), opt.getValue());
    }

    public void testSetIntegerValue() {
        opt.setValue(new Integer(14));
        assertEquals("option value", new Integer(14), opt.getValue());
    }

    public void testSetInvalidValueString() {
        try {
            opt.setValue("fred");
            fail("exception expected");
        } catch (InvalidTypeException ite) {
        }
    }

    public void testSetInvalidValueFloatingPoint() {
        try {
            opt.setValue("1.4");
            fail("exception expected");
        } catch (InvalidTypeException ite) {
        }
    }

    public void testSetValidValueNegative() {
        try {
            opt.setValue("-987");
            assertEquals("option value", new Integer(-987), opt.getValue());
        } catch (InvalidTypeException ite) {
            fail("exception not expected");
        }
    }

    public void testSetFromArgsListEqual() {
        List args = new ArrayList();
        try {
            boolean processed = opt.set("--intopt=444", args);
            assertEquals("option processed", true, processed);
            assertEquals("option value", new Integer(444), opt.getValue());
            assertEquals("argument removed from list", 0, args.size());
        } catch (OptionException ite) {
            fail("failure is not an option");
        }
    }

    public void testSetFromArgsListSeparateString() {
        List args = new ArrayList();
        args.add("41");
        try {
            boolean processed = opt.set("--intopt", args);
            assertEquals("option processed", true, processed);
            assertEquals("option value", new Integer(41), opt.getValue());
            assertEquals("argument removed from list", 0, args.size());
        } catch (OptionException ite) {
            fail("failure is not an option");
        }
    }

    public void testSetFromLongerArgsListEqual() {
        List args = new ArrayList();
        args.add("--anotheropt");
        try {
            boolean processed = opt.set("--intopt=666", args);
            assertEquals("option processed", true, processed);
            assertEquals("option value", new Integer(666), opt.getValue());
            assertEquals("argument removed from list", 1, args.size());
        } catch (OptionException ite) {
            fail("failure is not an option");
        }
    }

    public void testSetFromLongerArgsListSeparateString() {
        List args = new ArrayList();
        args.add("1234");
        args.add("--anotheropt");
        try {
            boolean processed = opt.set("--intopt", args);
            assertEquals("option processed", true, processed);
            assertEquals("option value", new Integer(1234), opt.getValue());
            assertEquals("argument removed from list", 1, args.size());
        } catch (OptionException ite) {
            fail("failure is not an option");
        }
    }

    public void testSetInvalidValueDanglingEquals() {
        List args = new ArrayList();
        args.add("--anotheropt");
        try {
            boolean processed = opt.set("--intopt=", args);
            fail("exception expected");
        } catch (OptionException ite) {
        }
    }
}
