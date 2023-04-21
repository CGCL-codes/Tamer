package dxc;

import dxc.junit.AllTests;
import junit.textui.TestRunner;

/**
 * Main class to run the jasmin tests.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Running all tests...");
            TestRunner.run(AllTests.suite());
        } else {
            System.out.println("Running selected tests...");
            TestRunner.main(args);
        }
        Runtime.getRuntime().halt(0);
    }
}
