package net.sf.compositor.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.compositor.util.StackProbe;

public class StringUtilTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(StackProbe.getMyClass());
    }

    public void testTagEscape() {
        assertEquals("Wrong escaped tag.", "&lt;b&gt;Bold&lt;/a&gt;", StringUtil.tagEscape("<b>Bold</a>"));
    }

    public void testToSafeString() {
        assertEquals("Wrong safe string (1).", "", StringUtil.toSafeString(null));
        assertEquals("Wrong safe string (2).", "", StringUtil.toSafeString(""));
        assertEquals("Wrong safe string (3).", "foo", StringUtil.toSafeString("foo"));
    }

    public void testIndent_1() {
        assertEquals("Wrong indented string (1).", "\txxx", StringUtil.indent("xxx"));
        assertEquals("Wrong indented string (2).", "\t\taaa", StringUtil.indent("aaa", 2));
        assertEquals("Wrong indented string (3).", "\tline1" + Env.NL + "\tline2", StringUtil.indent("line1\nline2"));
        assertEquals("Wrong indented string (4).", "\tline1" + Env.NL + "\t\tline2" + Env.NL + "\tline3", StringUtil.indent("line1\n\tline2\nline3"));
        assertEquals("Wrong indented string (5).", "\tline1" + Env.NL + "\tline2" + Env.NL, StringUtil.indent("line1\nline2\n"));
        assertEquals("Wrong indented string (6).", "\tline1" + Env.NL + "\t\tline2" + Env.NL + "\tline3" + Env.NL, StringUtil.indent("line1\r\tline2\rline3\r"));
        assertEquals("Wrong indented string (7).", "\t", StringUtil.indent(""));
    }

    public void testIndent_2() {
        assertEquals("Wrong indented string (1).", "  xxx", StringUtil.indent("xxx", 1, "  "));
        assertEquals("Wrong indented string (2).", "    aaa", StringUtil.indent("aaa", 2, "  "));
        assertEquals("Wrong indented string (3).", "  line1" + Env.NL + "  line2", StringUtil.indent("line1\nline2", 1, "  "));
        assertEquals("Wrong indented string (4).", "    line1" + Env.NL + "    \tline2" + Env.NL + "    line3", StringUtil.indent("line1\n\tline2\nline3", 2, "  "));
        assertEquals("Wrong indented string (5).", "---line1" + Env.NL + "---line2" + Env.NL, StringUtil.indent("line1\nline2\n", 3, "-"));
        assertEquals("Wrong indented string (6).", "+-+-line1" + Env.NL + "+-+-\tline2" + Env.NL + "+-+-line3" + Env.NL, StringUtil.indent("line1\r\tline2\rline3\r", 2, "+-"));
        assertEquals("Wrong indented string (7).", "!!!!", StringUtil.indent("", 2, "!!"));
    }
}
