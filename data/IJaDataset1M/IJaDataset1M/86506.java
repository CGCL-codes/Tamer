package JFlex.tests;

import JFlex.*;
import junit.framework.TestCase;

/**
 * Unit tests for JFlex.RegExp 
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class RegExpTests extends TestCase implements sym {

    /**
   * Constructor for RegExpTests.
   * 
   * @param name the test name
   */
    public RegExpTests(String name) {
        super(name);
    }

    public void testrevString() {
        assertTrue(RegExp.revString("blah").equals("halb"));
    }

    public void testCharClass() {
        Macros m = new Macros();
        RegExp e1 = new RegExp1(CCLASS, new Interval('a', 'z'));
        RegExp e2 = new RegExp1(CHAR, new Character('Z'));
        RegExp e3 = new RegExp1(CCLASS, new Interval('0', '9'));
        m.insert("macro", e3);
        RegExp s = new RegExp1(STAR, e1);
        RegExp u = new RegExp1(MACROUSE, "macro");
        RegExp b = new RegExp2(BAR, e2, u);
        assertTrue(e1.isCharClass(m));
        assertTrue(e2.isCharClass(m));
        assertTrue(b.isCharClass(m));
        assertTrue(!s.isCharClass(m));
        assertTrue(u.isCharClass(m));
    }
}
