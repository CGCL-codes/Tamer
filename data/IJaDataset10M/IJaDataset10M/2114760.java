package org.nakedobjects.applib;

import org.nakedobjects.applib.Title;
import org.nakedobjects.applib.TitledObject;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TitleTests extends TestCase {

    String test;

    Title t;

    ObjectWithTitle e;

    String companyName;

    public TitleTests(final String name) {
        super(name);
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(TitleTests.class));
    }

    protected void setUp() {
        test = "Fred";
        t = new Title(test);
        assertEquals(test, t.toString());
        companyName = "ABC Co.";
        e = new ObjectWithTitle();
        e.setupTitle(companyName);
    }

    public void testAppend() {
        t.append("");
        assertEquals("add empty string", test, t.toString());
        t.append("Smith");
        test += (" " + "Smith");
        assertEquals("append simple string", test, t.toString());
        t.append(",", "");
        assertEquals("append empty string with delimiter", test, t.toString());
        t.append(",", (TitledObject) null);
        assertEquals("append null with delimiter", test, t.toString());
        t.append(",", "Xyz Ltd.");
        test += (", " + "Xyz Ltd.");
        assertEquals("append string with delimiter", test, t.toString());
    }

    public void testAppendObjects() {
        t.append((TitledObject) null);
        assertEquals("append company name", test, t.toString());
        t.append(e);
        assertEquals("append company name", test + " " + companyName, t.toString());
    }

    public void testAppendObjectsWithDefaults() {
        t.append(e, "none");
        assertEquals("concat company name", test + " " + companyName, t.toString());
    }

    public void testAppendObjectsWithDefaults2() {
        t.append((TitledObject) null, "none");
        assertEquals("concat company name", test + " " + "none", t.toString());
    }

    public void testAppendObjectsWithJoiner() {
        t.append(",", (TitledObject) null);
        assertEquals(test, t.toString());
        t.append(",", e);
        assertEquals("concat company name", test + ", " + companyName, t.toString());
    }

    public void testAppendStrings() {
        t.append("");
        assertEquals("add empty string", test, t.toString());
        t.append("Smith");
        test += (" " + "Smith");
        assertEquals("append simple string", test, t.toString());
        t.append(",", "");
        assertEquals("append empty string with delimiter", test, t.toString());
        t.append(",", "Xyz Ltd.");
        test += (", " + "Xyz Ltd.");
        assertEquals("append string with delimiter", test, t.toString());
    }

    public void testAppendValue() {
        ObjectWithTitle s = new ObjectWithTitle();
        t.append(s);
        assertEquals("append empty TextString", test, t.toString());
        t.append(new ObjectWithTitle("square"));
        assertEquals("append empty TextString", test + " " + "square", t.toString());
    }

    public void testConcatObjects() {
        t.concat(e);
        assertEquals("concat company name", test + companyName, t.toString());
    }

    public void testConcatObjectsWithDefaults() {
        t.concat(e, "none");
        assertEquals("concat company name", test + companyName, t.toString());
    }

    public void testConcatObjectsWithDefaults2() {
        e = null;
        t.concat(e, "none");
        assertEquals("concat company name", test + "none", t.toString());
    }

    public void testConcatStrings() {
        t.concat("");
        assertEquals("add empty string", test, t.toString());
        t.concat("Smith");
        test += "Smith";
        assertEquals("concat simple string", test, t.toString());
        t.concat((TitledObject) null);
        assertEquals("concat null with delimiter", test, t.toString());
    }

    public void testConstructors() {
        Title t = new Title();
        assertEquals("empty title", "", t.toString());
        t = new Title("Test");
        assertEquals("for string", "Test", t.toString());
        ObjectWithTitle s = new ObjectWithTitle();
        t = new Title(s);
        assertEquals("for empty TextString object", "", t.toString());
        ObjectWithTitle e = new ObjectWithTitle();
        e.setupTitle("Tada");
        t = new Title(e);
        assertEquals("for object", "Tada", t.toString());
        t = new Title(e, "test");
        assertEquals("for object (with default)", "Tada", t.toString());
        t = new Title(null, "test");
        assertEquals("for no object (with default)", "test", t.toString());
    }

    public void testTruncate() {
        String text1 = "This is a";
        String text2 = " long title";
        String fullText = text1 + text2;
        Title t = new Title(fullText);
        t.truncate(7);
        assertEquals(fullText, t.toString());
        t.truncate(5);
        assertEquals(fullText, t.toString());
        t.truncate(3);
        assertEquals(text1 + "...", t.toString());
        try {
            t.truncate(0);
            fail("Exception expected");
        } catch (IllegalArgumentException ee) {
        }
    }
}
