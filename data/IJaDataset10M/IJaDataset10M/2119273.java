package org.codecover.instrumentation.java15.syntaxtree;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;

/**
 * @author Christoph Müller
 * @version 1.0 ($Id: AllTests.java 1 2007-12-12 17:37:26Z t-scheller $)
 */
public class AllTests extends TestSuite {

    /**
     * Starts all the test cases hierarchical using the JUnit swingui TestRunner.
     * 
     * @param args
     *            "swing" &rarr; use {@link TestRunner}<br>
     *            "text" &rarr; use {@link junit.textui.TestRunner}<br>
     *            nothing &rarr; use {@link TestRunner}<br>
     */
    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            args = new String[] { "swing" };
        }
        if (args[0].toLowerCase().equals("swing")) {
            TestRunner.run(AllTests.class);
        } else if (args[0].toLowerCase().equals("text")) {
            junit.textui.TestRunner.run(AllTests.suite());
        } else {
            System.out.println("usage: \"AllTests (swing | text)\"");
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getCanonicalName());
        suite.addTestSuite(NodeTokenTest.class);
        return suite;
    }
}
