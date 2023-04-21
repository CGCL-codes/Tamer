package com.metanology.mde.core.metaModel;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test all classes in 
 * com.metanology.mde.core.metaModel
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.metanology.mde.core.metaModel");
        suite.addTest(new TestSuite(AssociationTest.class));
        suite.addTest(new TestSuite(AssociationRoleTest.class));
        suite.addTest(new TestSuite(AttributeTest.class));
        suite.addTest(new TestSuite(CardinalityEnumTest.class));
        suite.addTest(new TestSuite(ClassifierTest.class));
        suite.addTest(new TestSuite(CommentTest.class));
        suite.addTest(new TestSuite(ComponentTest.class));
        suite.addTest(new TestSuite(ContainmentEnumTest.class));
        suite.addTest(new TestSuite(GeneralizationTest.class));
        suite.addTest(new TestSuite(MetaClassTest.class));
        suite.addTest(new TestSuite(MetaObjectTest.class));
        suite.addTest(new TestSuite(ModelTest.class));
        suite.addTest(new TestSuite(ModelElementTest.class));
        suite.addTest(new TestSuite(OperationTest.class));
        suite.addTest(new TestSuite(OveridabilityEnumTest.class));
        suite.addTest(new TestSuite(PackageTest.class));
        suite.addTest(new TestSuite(ParameterTest.class));
        suite.addTest(new TestSuite(PersistenceEnumTest.class));
        suite.addTest(new TestSuite(RealizationTest.class));
        suite.addTest(new TestSuite(ScopeEnumTest.class));
        suite.addTest(new TestSuite(SubsystemTest.class));
        suite.addTest(new TestSuite(TagTest.class));
        return suite;
    }
}
