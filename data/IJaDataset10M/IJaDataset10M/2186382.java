package org.carlmanaster.allelogram.model.tests;

import junit.framework.TestSuite;

public class TestAll extends TestSuite {

    public static TestSuite suite() {
        TestAll suite = new TestAll();
        suite.addTestSuite(BinGuesserTest.class);
        suite.addTestSuite(BinTest.class);
        suite.addTestSuite(ClassifierTest.class);
        suite.addTestSuite(ClassificationTest.class);
        suite.addTestSuite(GenotypeClassificationPredicateTest.class);
        suite.addTestSuite(GenotypeComparatorTest.class);
        suite.addTestSuite(GenotypeTest.class);
        suite.addTestSuite(SettingsTest.class);
        suite.addTestSuite(CircularListTest.class);
        suite.addTestSuite(BinNamerTest.class);
        return suite;
    }
}
