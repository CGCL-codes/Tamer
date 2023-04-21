package net.sourceforge.nrl.parser.type;

import junit.framework.Test;

/**
 * Test suite for the entire <code>com.modeltwozero.nrl.parser.type</code>
 * package.
 * 
 * @author Christian Nentwich
 */
public class TestSuite extends junit.framework.TestSuite {

    public static Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite("Type Checking");
        suite.addTestSuite(ActionTypeCheckerTest.class);
        suite.addTestSuite(ConstraintTypeCheckerTest.class);
        suite.addTestSuite(TypeMappingTest.class);
        suite.addTestSuite(XmlTypeMappingTest.class);
        return suite;
    }
}
