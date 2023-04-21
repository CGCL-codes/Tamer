package org.enerj.apache.commons.collections.map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for tests.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 209680 $ $Date: 2005-07-08 01:04:28 +0100 (Fri, 08 Jul 2005) $
 * 
 * @author Stephen Colebourne
 */
public class TestAll extends TestCase {

    public TestAll(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestAll.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestCaseInsensitiveMap.suite());
        suite.addTest(TestCompositeMap.suite());
        suite.addTest(TestDefaultedMap.suite());
        suite.addTest(TestFlat3Map.suite());
        suite.addTest(TestHashedMap.suite());
        suite.addTest(TestIdentityMap.suite());
        suite.addTest(TestLinkedMap.suite());
        suite.addTest(TestLRUMap.suite());
        suite.addTest(TestMultiKeyMap.suite());
        suite.addTest(TestReferenceMap.suite());
        suite.addTest(TestReferenceIdentityMap.suite());
        suite.addTest(TestStaticBucketMap.suite());
        suite.addTest(TestSingletonMap.suite());
        suite.addTest(TestFixedSizeMap.suite());
        suite.addTest(TestFixedSizeSortedMap.suite());
        suite.addTest(TestLazyMap.suite());
        suite.addTest(TestLazySortedMap.suite());
        suite.addTest(TestListOrderedMap.suite());
        suite.addTest(TestListOrderedMap2.suite());
        suite.addTest(TestMultiValueMap.suite());
        suite.addTest(TestPredicatedMap.suite());
        suite.addTest(TestPredicatedSortedMap.suite());
        suite.addTest(TestTransformedMap.suite());
        suite.addTest(TestTransformedSortedMap.suite());
        suite.addTest(TestUnmodifiableMap.suite());
        suite.addTest(TestUnmodifiableOrderedMap.suite());
        suite.addTest(TestUnmodifiableSortedMap.suite());
        return suite;
    }
}
