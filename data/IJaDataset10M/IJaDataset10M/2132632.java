package jp.go.aist.six.test.oval.model;

import jp.go.aist.six.oval.model.Component;
import jp.go.aist.six.oval.model.Family;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;

/**
 * Tests: oval.model.Component.
 *
 * @author	Akihito Nakamura, AIST
 * @version $Id: ComponentTests.java 2274 2012-04-02 06:29:06Z nakamura5akihito@gmail.com $
 */
public class ComponentTests {

    /**
     */
    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
    }

    /**
     */
    @org.testng.annotations.Test(groups = { "oval.model" }, alwaysRun = true)
    public void testComponent() throws Exception {
        Reporter.log("\n//////////////////////////////////////////////////////////", true);
        Reporter.log("*** target class: " + Component.class.getName(), true);
        Reporter.log("* listing all the names and values...", true);
        for (Component c : Component.values()) {
            Reporter.log("name=: " + c.name() + ", value=" + c.value(), true);
        }
        Reporter.log("* #enum constants: " + Component.values().length, true);
    }

    /**
     */
    @org.testng.annotations.Test(groups = { "oval.model" }, alwaysRun = true)
    public void testFamily() throws Exception {
        Reporter.log("\n//////////////////////////////////////////////////////////", true);
        Reporter.log("*** target class: " + Family.class.getName(), true);
        Reporter.log("* listing all the names and values...", true);
        for (Family f : Family.values()) {
            Reporter.log("name=: " + f.name() + ", value=" + f.value(), true);
        }
        Reporter.log("* #enum constants: " + Family.values().length, true);
    }
}
