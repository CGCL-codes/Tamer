package rj.tools.jcsc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run JUnit tests against certain test sources
 *
 * @author Ralph Jocham
 * @version $Revision: 1.8 $
 */
public class JcscFieldTest extends TestCase {

    /**
    *  <code>suite</code> returns the test suite
    *
    * @return a <code>Test</code> value
    */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(JcscFieldTest.class);
        return suite;
    }

    /**
    * Creates a new <code>JcscFieldTest</code> instance.
    *
    * @param name a <code>String</code> value
    */
    public JcscFieldTest(String name) {
        super(name);
    }

    /**
    * test the one field per line rule
    */
    public void testOnly1FieldDecPerLine() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/OneVariableDecPerLine.xava", JcscTestUtil.getDefaultRules());
        assertEquals(2, rf.getViolationsCount());
    }

    /**
    * test the public field allowed rule
    */
    public void testAllowPublicField() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PublicField.xava", JcscTestUtil.getDefaultRules());
        assertEquals(1, rf.getViolationsCount());
    }

    /**
    * test the protected field allowed rule
    */
    public void testAllowProtectedField() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/ProtectedField.xava", JcscTestUtil.getDefaultRules());
        assertEquals(0, rf.getViolationsCount());
    }

    /**
    * test the package field allowed rule
    */
    public void testAllowPackageField() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PackageField.xava", JcscTestUtil.getDefaultRules());
        assertEquals(0, rf.getViolationsCount());
    }

    /**
    * test the field declaration modifier order rule
    */
    public void testFieldDeclModOrder() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/FieldDeclModOrder.xava", JcscTestUtil.getDefaultRules());
        assertEquals(5, rf.getViolationsCount());
    }

    /**
    * test the arrays at type rule
    */
    public void testArrayAtType() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/ArrayAtType.xava", JcscTestUtil.getDefaultRules());
        assertEquals(4, rf.getViolationsCount());
    }

    /**
    * test the private instance field regexp rule
    */
    public void testPriInsFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PriInsField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the package instance field regexp rule
    */
    public void testPacInsFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PacInsField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the protected instance field regexp rule
    */
    public void testProInsFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/ProInsField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the public instance field regexp rule
    */
    public void testPubInsFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PubInsField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(9, rf.getViolationsCount());
    }

    /**
    * test the private static field regexp rule
    */
    public void testPriStaFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PriStaField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the package static field regexp rule
    */
    public void testPacStaFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PacStaField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the protected static field regexp rule
    */
    public void testProStaFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/ProStaField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the public static field regexp rule
    */
    public void testPubStaFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PubStaField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(9, rf.getViolationsCount());
    }

    /**
    * test the private static final field regexp rule
    */
    public void testPriStaFinFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PriStaFinField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the package static final field regexp rule
    */
    public void testPacStaFinFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PacStaFinField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the protected static final field regexp rule
    */
    public void testProStaFinFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/ProStaFinField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the public static final field regexp rule
    */
    public void testPubStaFinFieldRegex() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/PubStaFinField.xava", JcscTestUtil.getJCSCRules());
        assertEquals(3, rf.getViolationsCount());
    }

    /**
    * test the field block place rule
    */
    public void testFieldBlockPlace() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/FieldBlockPlace1.xava", JcscTestUtil.getDefaultRules());
        assertEquals(0, rf.getViolationsCount());
        rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/FieldBlockPlace2.xava", JcscTestUtil.getDefaultRules());
        assertEquals(1, rf.getViolationsCount());
        rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/FieldBlockPlace3.xava", JcscTestUtil.getDefaultRules());
        assertEquals(1, rf.getViolationsCount());
    }

    /**
    * test the field order rule
    */
    public void testFieldOrder() {
        JUnitResultsFormatter rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/FieldOrder1.xava", JcscTestUtil.getDefaultRules());
        assertEquals(0, rf.getViolationsCount());
        rf = JcscTestUtil.runTest("junit-src/rj/tools/jcsc/FieldOrder2.xava", JcscTestUtil.getDefaultRules());
        assertEquals(2, rf.getViolationsCount());
    }
}
