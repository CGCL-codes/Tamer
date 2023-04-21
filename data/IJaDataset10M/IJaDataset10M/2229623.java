package org.jostraca.directive.test;

import org.jostraca.directive.DirectiveParser;
import org.jostraca.directive.DirectiveException;
import org.jostraca.util.StandardException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.util.Vector;
import java.util.Hashtable;

/** <b>Description:</b><br>
 *  Test cases for Pair.
 */
public class DirectiveParserTest extends TestCase {

    public DirectiveParserTest(String pName) {
        super(pName);
    }

    public static TestSuite suite() {
        return new TestSuite(DirectiveParserTest.class);
    }

    public static void main(String[] pArgs) {
        TestRunner.run(suite());
    }

    public void testParse() throws Exception {
        DirectiveParser dp = new DirectiveParser();
        Vector unamednames = new Vector();
        unamednames.addElement("arg1");
        unamednames.addElement("arg2");
        Hashtable nameddefaults = new Hashtable();
        nameddefaults.put("arg1", "v1");
        nameddefaults.put("arg2", "v2");
        Hashtable ca;
        dp.parse(" @blah arg1=\"a 'x' \\\" \\\\ \\\" \" ");
        assertEquals("blah", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "a 'x' \" \\ \" ");
        ca.put("arg2", "v2");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse(" @blah  arg1='a \"x\" \\' \\\\ \\'' ");
        assertEquals("blah", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "a \"x\" ' \\ '");
        ca.put("arg2", "v2");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse("@ blah  un arg2=ns arg2= 02 ");
        assertEquals("blah", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "un");
        ca.put("arg2", "02");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse(" un arg2=ns arg2= 02 ");
        assertEquals("", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "un");
        ca.put("arg2", "02");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse(" @ blah 'u n 01' un02 arg2=' 02 '");
        assertEquals("blah", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "u n 01");
        ca.put("arg2", " 02 ");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse(" 'u n 01' un02 arg2=' 02 '");
        assertEquals("", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "u n 01");
        ca.put("arg2", " 02 ");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse("@blah not_an_id not_an_arg()=all_the_one ");
        assertEquals("blah", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "not_an_id");
        ca.put("arg2", "not_an_arg()=all_the_one");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse("@blah @not_a_dname ignored_arg=all_the_one arg2=@av2");
        assertEquals("blah", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "@not_a_dname");
        ca.put("arg2", "@av2");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse("@foo-bar_ =av1 arg2='av2='");
        assertEquals("foo-bar_", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "=av1");
        ca.put("arg2", "av2=");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse("@foo-bar_ =av1 arg2='av2='");
        assertEquals("foo-bar_", dp.getName());
        ca = new Hashtable();
        ca.put("arg1", "=av1");
        ca.put("arg2", "av2=");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
        dp.parse("@foo/bar un/01 /un02/ ");
        assertEquals("foo", dp.getName());
        assertEquals("foo/bar", dp.getFullName());
        assertEquals("foo", dp.getNameElements()[0]);
        assertEquals("bar", dp.getNameElements()[1]);
        ca = new Hashtable();
        ca.put("arg1", "un/01");
        ca.put("arg2", "/un02/");
        assertEquals(ca.get("arg1"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg1"));
        assertEquals(ca.get("arg2"), dp.getCanonicalArgs(unamednames, nameddefaults).get("arg2"));
    }

    public void testSyntaxErrors() throws Exception {
        DirectiveParser dp = new DirectiveParser();
        try {
            dp.parse(" 'a\\a' ");
        } catch (DirectiveException de) {
            assertEquals(DirectiveException.CODE_parse, de.getCode());
            assertEquals(StandardException.Code.CAT_user, de.getCat());
        }
    }
}
