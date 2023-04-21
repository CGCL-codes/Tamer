package info.bliki.wiki.addon.test.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TeXTest extends AddonTestSupport {

    public TeXTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TeXTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testTeX001() {
        assertEquals("\n" + "<p>A <span class=\"tex\"><img src=\"http://www.mathtran.org/cgi-bin/mathtran?D=3&amp;tex=%5Csin+%5Cfrac%7B1%7D%7Bx%7D\" alt=\"\\sin \\frac{1}{x}\" /></span> formula test</p>", wikiModel.render("A <tex>\\sin \\frac{1}{x}</tex> formula test"));
    }

    public void testCTeX001() {
        assertEquals("\n" + "<p>A <span class=\"ctex\"><img src=\"http://www.mathtran.org/cgi-bin/mathtran?D=5&amp;tex=%5Cdisplaystyle+%5Csin+%5Cfrac%7B1%7D%7Bx%7D\" alt=\"\\sin \\frac{1}{x}\" /></span> formula test</p>", wikiModel.render("A <ctex d=\"5\">\\sin \\frac{1}{x}</ctex> formula test"));
    }
}
