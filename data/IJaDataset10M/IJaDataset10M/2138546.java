package jp.go.aist.six.test.util.search;

import jp.go.aist.six.util.search.Function;
import jp.go.aist.six.util.search.Relation;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * TestNG tests.
 *
 * @author  Akihito Nakamaura, AIST
 * @version $Id: EnumTest.java 91 2010-06-14 03:07:34Z nakamura5akihito $
 */
public class EnumTest {

    /**
     */
    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
    }

    /**
     */
    @Test(groups = { "util.search", "enum" }, alwaysRun = true)
    public void print() throws Exception {
        Reporter.log("\n// util.search: enum test //", true);
        Reporter.log("Function:", true);
        for (Function e : Function.values()) {
            Reporter.log("  @ toString()=" + e.toString(), true);
            Reporter.log("  @ name=" + e.name(), true);
        }
        Reporter.log("Relation:", true);
        for (Relation e : Relation.values()) {
            Reporter.log("  @ toString()=" + e.toString(), true);
            Reporter.log("  @       name=" + e.name(), true);
            Reporter.log("  @   operator=" + e.operator(), true);
        }
    }
}
