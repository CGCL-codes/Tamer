package com.hp.hpl.jena.regression;

import junit.framework.TestSuite;
import com.hp.hpl.jena.rdf.model.test.ModelTestBase;

public class NewRegression extends ModelTestBase {

    public NewRegression(String name) {
        super(name);
    }

    public static TestSuite suite() {
        TestSuite result = new TestSuite(NewRegression.class);
        result.addTest(NewRegressionLiterals.suite());
        result.addTest(NewRegressionResources.suite());
        result.addTest(NewRegressionStatements.suite());
        result.addTest(NewRegressionContainers.suite());
        result.addTest(NewRegressionAddAndContains.suite());
        result.addTest(NewRegressionGet.suite());
        result.addTest(NewRegressionObjects.suite());
        result.addTest(NewRegressionStatements.suite());
        result.addTest(NewRegressionAddModel.suite());
        result.addTest(NewRegressionListSubjects.suite());
        result.addTest(NewRegressionSelector.suite());
        result.addTest(NewRegressionSeq.suite());
        result.addTest(NewRegressionSet.suite());
        result.addTest(NewRegressionResourceMethods.suite());
        result.addTest(NewRegressionStatementMethods.suite());
        result.addTest(NewRegressionBagMethods.suite());
        result.addTest(NewRegressionAltMethods.suite());
        result.addTest(NewRegressionSeqMethods.suite());
        return result;
    }

    public void testNothing() {
    }
}
