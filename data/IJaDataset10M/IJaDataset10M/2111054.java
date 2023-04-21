package org.apache.harmony.regex.tests.java.util.regex;

import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestLevel;
import junit.framework.TestCase;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

/**
 * Tests Pattern compilation modes and modes triggered in pattern strings
 * 
 */
@TestTargetClass(Pattern.class)
public class ModeTest extends TestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "The test verifies compile(String regex) and compile(String regex, int flags) methods with Pattern.CASE_INSENSITIVE mode.", method = "compile", args = { java.lang.String.class }), @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "The test verifies compile(String regex) and compile(String regex, int flags) methods with Pattern.CASE_INSENSITIVE mode.", method = "compile", args = { java.lang.String.class, int.class }) })
    public void testCase() throws PatternSyntaxException {
        Pattern p;
        Matcher m;
        p = Pattern.compile("([a-z]+)[0-9]+");
        m = p.matcher("cAT123#dog345");
        assertTrue(m.find());
        assertEquals("dog", m.group(1));
        assertFalse(m.find());
        p = Pattern.compile("([a-z]+)[0-9]+", Pattern.CASE_INSENSITIVE);
        m = p.matcher("cAt123#doG345");
        assertTrue(m.find());
        assertEquals("cAt", m.group(1));
        assertTrue(m.find());
        assertEquals("doG", m.group(1));
        assertFalse(m.find());
        p = Pattern.compile("(?i)([a-z]+)[0-9]+");
        m = p.matcher("cAt123#doG345");
        assertTrue(m.find());
        assertEquals("cAt", m.group(1));
        assertTrue(m.find());
        assertEquals("doG", m.group(1));
        assertFalse(m.find());
    }

    @TestTargets({ @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "The test verifies compile(String regex) and compile(String regex, int flags) methods with Pattern.MULTILINE mode.", method = "compile", args = { java.lang.String.class }), @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "The test verifies compile(String regex) and compile(String regex, int flags) methods with Pattern.MULTILINE mode.", method = "compile", args = { java.lang.String.class, int.class }) })
    public void testMultiline() throws PatternSyntaxException {
        Pattern p;
        Matcher m;
        p = Pattern.compile("^foo");
        m = p.matcher("foobar");
        assertTrue(m.find());
        assertTrue(m.start() == 0 && m.end() == 3);
        assertFalse(m.find());
        m = p.matcher("barfoo");
        assertFalse(m.find());
        p = Pattern.compile("foo$");
        m = p.matcher("foobar");
        assertFalse(m.find());
        m = p.matcher("barfoo");
        assertTrue(m.find());
        assertTrue(m.start() == 3 && m.end() == 6);
        assertFalse(m.find());
        p = Pattern.compile("^foo([0-9]*)", Pattern.MULTILINE);
        m = p.matcher("foo1bar\nfoo2foo3\nbarfoo4");
        assertTrue(m.find());
        assertEquals("1", m.group(1));
        assertTrue(m.find());
        assertEquals("2", m.group(1));
        assertFalse(m.find());
        p = Pattern.compile("foo([0-9]*)$", Pattern.MULTILINE);
        m = p.matcher("foo1bar\nfoo2foo3\nbarfoo4");
        assertTrue(m.find());
        assertEquals("3", m.group(1));
        assertTrue(m.find());
        assertEquals("4", m.group(1));
        assertFalse(m.find());
        p = Pattern.compile("(?m)^foo([0-9]*)");
        m = p.matcher("foo1bar\nfoo2foo3\nbarfoo4");
        assertTrue(m.find());
        assertEquals("1", m.group(1));
        assertTrue(m.find());
        assertEquals("2", m.group(1));
        assertFalse(m.find());
        p = Pattern.compile("(?m)foo([0-9]*)$");
        m = p.matcher("foo1bar\nfoo2foo3\nbarfoo4");
        assertTrue(m.find());
        assertEquals("3", m.group(1));
        assertTrue(m.find());
        assertEquals("4", m.group(1));
        assertFalse(m.find());
    }
}
