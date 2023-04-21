package com.hp.hpl.jena.rdf.model.spec.test;

import junit.framework.*;

public class TestModelSpecPackage extends TestCase {

    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTest(TestModelSpecRDB.suite());
        result.addTest(TestModelSpecWithSchema.suite());
        result.addTest(TestModelSpec.suite());
        result.addTest(TestModelSpecMore.suite());
        result.addTest(TestModelSpecsWithRuleSets.suite());
        result.addTest(TestModelSpecFactory.suite());
        result.addTest(TestModelSource.suite());
        result.addTest(TestModelSpecImplLoadFiles.suite());
        return result;
    }
}
