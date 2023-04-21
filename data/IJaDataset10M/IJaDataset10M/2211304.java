package huf.unibus.xmlbridge.test;

import huf.misc.tester.Tester;

/**
 * Class that runs all huf.unibus.xmlbridge tests.
 */
public class Test {

    /**
	 * Run test suite.
	 *
	 * @param t tester
	 */
    public Test(Tester t) {
        new XmlBridgeTest(t);
    }

    /**
	 * Run tests from command line.
	 *
	 * @param args ignored
	 */
    public static void main(String[] args) {
        Tester t = new Tester();
        new Test(t);
        t.totals();
        System.exit(0);
    }
}
