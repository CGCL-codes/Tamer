package net.sf.brico.cmd.base.command;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class AllTestSuite extends TestCase {

    /**
    * Command-line interface.
    */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
    * Get the suite of tests
    */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName("net.sf.brico.cmd.base.command");
        suite.addTest(new TestSuite(TestCommandForwarder.class));
        return suite;
    }

    /**
    * Construct a new instance.
    */
    public AllTestSuite(String name) {
        super(name);
    }
}
