package org.enerj.apache.commons.collections.iterators;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for all iterator tests.
 * 
 * @version $Revision: 400329 $ $Date: 2006-05-06 17:10:31 +0100 (Sat, 06 May 2006) $
 * 
 * @author Rodney Waldhoff
 */
public class TestAll extends TestCase {

    public TestAll(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestArrayIterator.suite());
        suite.addTest(TestArrayIterator2.suite());
        suite.addTest(TestArrayListIterator.suite());
        suite.addTest(TestArrayListIterator2.suite());
        suite.addTest(TestObjectArrayIterator.suite());
        suite.addTest(TestObjectArrayListIterator.suite());
        suite.addTest(TestObjectArrayListIterator2.suite());
        suite.addTest(TestCollatingIterator.suite());
        suite.addTest(TestFilterIterator.suite());
        suite.addTest(TestFilterListIterator.suite());
        suite.addTest(TestIteratorChain.suite());
        suite.addTest(TestListIteratorWrapper.suite());
        suite.addTest(TestLoopingIterator.suite());
        suite.addTest(TestLoopingListIterator.suite());
        suite.addTest(TestReverseListIterator.suite());
        suite.addTest(TestSingletonIterator.suite());
        suite.addTest(TestSingletonIterator2.suite());
        suite.addTest(TestSingletonListIterator.suite());
        suite.addTest(TestObjectGraphIterator.suite());
        suite.addTest(TestUniqueFilterIterator.suite());
        suite.addTest(TestUnmodifiableIterator.suite());
        suite.addTest(TestUnmodifiableListIterator.suite());
        suite.addTest(TestUnmodifiableMapIterator.suite());
        suite.addTest(TestUnmodifiableOrderedMapIterator.suite());
        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestAll.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }
}
