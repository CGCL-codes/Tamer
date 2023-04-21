package huf.data.test;

import huf.misc.tester.Tester;

/**
 * Class that runs all huf.data tests.
 */
public class Test {

    /**
	 * Run test suite.
	 *
	 * @param t tester
	 */
    public Test(Tester t) {
        t.skipClass("huf.data.IContainer");
        t.skipClass("huf.data.Iterator");
        t.skipClass("huf.data.Null");
        new ArrayIteratorTest(t);
        new TreeIteratorTest(t);
        new IteratorUnionTest(t);
        new ChainTest(t);
        new ContainerTest(t);
        t.skipClass("huf.data.ListItem");
        new LinkedListTest(t);
        new PairTest(t);
        new MapTest(t);
        new ListSetTest(t);
        new SetTest(t);
        new PropertiesTest(t);
        new WBooleanTest(t);
        new WByteTest(t);
        new WDoubleTest(t);
        new WFloatTest(t);
        new WIntTest(t);
        new WLongTest(t);
        new WShortTest(t);
        new DataUtilsTest(t);
        new huf.data.sort.test.Test(t);
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
    }
}
