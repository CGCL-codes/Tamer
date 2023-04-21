package org.web3d.x3d.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

/**
 * JUnit-based and XMLUnit-based tests for X3dHeaderChecker.java
 * @author <a href="mailto:tdnorbra@nps.edu?subject=org.web3d.x3d.tools.X3dHeaderCheckerTest">Terry Norbraten</a>
 * @version $Id: X3dHeaderCheckerTest.java 9270 2011-08-29 01:04:58Z brutzman $
 *
 * <p>
 *   <dt><b>History:</b>
 *   <pre><b>
 *     Date:     15 September 2006
 *     Time:     1602
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=org.web3d.x3d.tools.x3db.X3dCanonicalizerTest">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) Initial
 *
 *     Date:     22 September 2006
 *     Time:     1932
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=org.web3d.x3d.tools.x3db.X3dCanonicalizerTest">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) Added testReadOnly() to test for exception to be thrown on a
 *                  read-only file
 *
 *     Date:     27 FEB 2007
 *     Time:     2213
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=org.web3d.x3d.tools.x3db.X3dCanonicalizerTest">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) testReadOnly() modified to include first XMLUnit test.  Will
 *                  not now throw exception to pass.  Simply tests for graceful
 *                  JVM exit without any attempted file modifications.
 *               2) Added test method for testing the setting of a non-canonical
 *                  DTD
 *
 *     Date:     15 MAR 2007
 *     Time:     1629
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=org.web3d.x3d.tools.x3db.X3dCanonicalizerTest">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) Added test for optional Schema validation switch setting
 *               2) Strengthened tests for null arguments
 * 
 *     Date:     02 DEC 2007
 *     Time:     0214Z
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=org.web3d.x3d.tools.x3db.X3dCanonicalizerTest">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) Added support for X3D 3.2
 * 
 *     Date:     28 AUG 2011
 *     Author:   Don Brutzman
 *     Comments: 1) Added support for X3D 3.3
 *   </b></pre>
 * </p>
 * @see org.web3d.x3d.tools.X3dHeaderChecker
 */
public class X3dHeaderCheckerTest extends XMLTestCase {

    /** log4j logger instance */
    static Logger log = Logger.getLogger(X3dHeaderCheckerTest.class);

    private String testFilesDir = "/www.web3d.org/x3d/tools/canonical/test/testFiles/";

    public X3dHeaderCheckerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() {
        XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
        XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Override
    protected void tearDown() {
    }

    /**
     * These will test the main() method, which will also test constructor
     * functionality for each of these cases as a by-product
     * @return a Test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(X3dHeaderCheckerTest.class);
        return suite;
    }

    /** Test of main method rejection of null arguments */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullArgs() {
        log.info("testNullArgs");
        try {
            X3dHeaderChecker.main(new String[] {});
        } catch (NullPointerException e) {
            log.warn(e);
        }
    }

    /** Test of main method rejection of an invalid arguments */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs1() {
        System.out.println();
        log.info("testInvalidArgs1");
        try {
            X3dHeaderChecker.main(new String[] { "-ia", testFilesDir + "HelloWorld.x3d" });
        } catch (IllegalArgumentException e) {
            log.warn(e);
        }
    }

    /** Test of main method rejection of an invalid file extension */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs2() {
        System.out.println();
        log.info("testInvalidArgs2");
        try {
            X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "HelloWorld.x3e" });
        } catch (IllegalArgumentException e) {
            log.warn(e);
        }
    }

    /** 
     * Test of main method rejection of an invalid flag argument and file
     * extension
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs3() {
        System.out.println();
        log.info("testInvalidArgs3");
        try {
            X3dHeaderChecker.main(new String[] { "-", "f", testFilesDir + "HelloWorld.x3e" });
        } catch (IllegalArgumentException e) {
            log.warn(e);
        }
    }

    /** Test of main method rejection of test due to a non-existent file */
    @org.junit.Test(expected = FileNotFoundException.class)
    public void testFileNotFound() {
        System.out.println();
        log.info("testFileNotFound");
        try {
            X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "TestFileNotFound.x3d" });
        } catch (Exception e) {
            log.warn(e);
        }
    }

    /** Test of main method processing of a read-only file.  DTD is not expected
     * to change, but the X3dHeaderChecker should still validate the DTD.
     */
    public void testReadOnly() {
        System.out.println();
        log.info("testReadOnly");
        X3dHeaderChecker x3dDtdCheck1 = new X3dHeaderChecker(new String[] { "-f", testFilesDir + "TestReadOnly.x3d" });
        X3dHeaderChecker x3dDtdCheck2 = new X3dHeaderChecker(new String[] { "-t", testFilesDir + "TestReadOnly.x3d" });
        try {
            assertXMLEqual(x3dDtdCheck1.getScene(), x3dDtdCheck2.getActiveScene());
            log.info("Both control and test X3D Strings are equivalent");
        } catch (SAXException ex) {
            log.fatal(ex);
        } catch (IOException ex) {
            log.fatal(ex);
        }
        x3dDtdCheck1.setScene(null);
        x3dDtdCheck2.setScene(null);
    }

    /** Test of main method rejection of an invalid X3D header */
    @org.junit.Test(expected = RuntimeException.class)
    public void testInvalidX3DHeader() {
        System.out.println();
        log.info("testInvalidX3DHeader");
        try {
            X3dHeaderChecker.main(new String[] { "-t", testFilesDir + "TestNonValidX3DHeader.x3d" });
        } catch (RuntimeException e) {
            log.warn(e);
        }
    }

    /** Test of main method setting of the 3.0 transitional DTD */
    public void testSet30Transitional() {
        System.out.println();
        log.info("testSet30Transitional");
        X3dHeaderChecker.main(new String[] { "-t", testFilesDir + "Test30DTD.x3d" });
    }

    /**
     * Test of main method, of class org.web3d.x3d.tools.X3dHeaderChecker.  Tests
     * and reports the DOCTYPE already set for 3.0 transitional DTD. </p>
     */
    public void testSet30TransitionalWithTransitional() {
        System.out.println();
        log.info("testSet30TransitionalWithTransitional");
        X3dHeaderChecker.main(new String[] { "-t", testFilesDir + "Test30DTD.x3d" });
    }

    /** Test of main method setting of the 3.0 final DTD */
    public void testSet30Final() {
        System.out.println();
        log.info("testSet30Final");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "Test30DTD.x3d" });
    }

    /**
     * Test of main method, of class org.web3d.x3d.tools.X3dHeaderChecker.  Tests
     * and reports the DOCTYPE already set for 3.0 final DTD. </p>
     */
    public void testSet30FinalWithFinal() {
        System.out.println();
        log.info("testSet30FinalWithFinal");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "Test30DTD.x3d" });
    }

    /** Test of main method setting of the 3.1 transitional DTD */
    public void testSet31Transitional() {
        System.out.println();
        log.info("testSet31Transitional");
        X3dHeaderChecker.main(new String[] { "-t", testFilesDir + "Test31DTD.x3d" });
    }

    /**
     * Test of main method, of class org.web3d.x3d.tools.X3dHeaderChecker.  Tests
     * and reports the DOCTYPE already set for 3.1 transitional DTD. </p>
     */
    public void testSet31TransitionalWithTransitional() {
        System.out.println();
        log.info("testSet31TransitionalWithTransitional");
        X3dHeaderChecker.main(new String[] { "-t", testFilesDir + "Test31DTD.x3d" });
    }

    /** Test of main method setting of the 3.1 final DTD */
    public void testSet31Final() {
        System.out.println();
        log.info("testSet31Final");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "Test31DTD.x3d" });
    }

    /**
     * Test of main method, of class org.web3d.x3d.tools.X3dHeaderChecker.  Tests
     * and reports the DOCTYPE already set for 3.1 final DTD. </p>
     */
    public void testSet31FinalWithFinal() {
        System.out.println();
        log.info("testSet31FinalWithFinal");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "Test31DTD.x3d" });
    }

    /** Test of main method setting of the 3.2 final DTD */
    public void testSet32Final() {
        System.out.println();
        log.info("testSet32Final");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "Test32DTD.x3d" });
    }

    /** Test of main method setting of the 3.3 final DTD */
    public void testSet33Final() {
        System.out.println();
        log.info("testSet33Final");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "Test33DTD.x3d" });
    }

    /**
     * Test of main method, of class org.web3d.x3d.tools.X3dHeaderChecker.  Tests
     * and reports the DOCTYPE already set for 3.2 final DTD. </p>
     */
    public void testSet32FinalWithFinal() {
        System.out.println();
        log.info("testSet32FinalWithFinal");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "Test32DTD.x3d" });
    }

    /** Test of main method setting of a non-canonical DTD */
    public void testSetNonCanonicalDTD() {
        System.out.println();
        log.info("testSetNonCanonicalDTD");
        X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "TestNonCanonicalDTD.x3d" });
        log.info("Remember to reset DTD with whitespace to prepare for retest");
    }

    /** Test of main method rejection of a scene containing multiple DTDs.  The
     * Validator is expected to throw a SAXException. </p>
     */
    @org.junit.Test(expected = Exception.class)
    public void testMultDTDs() {
        System.out.println();
        log.info("testMultDTDs");
        try {
            X3dHeaderChecker.main(new String[] { "-f", "-v", testFilesDir + "TestMultDTDs.x3d" });
        } catch (Exception e) {
            log.warn(e);
        }
    }

    /** 
     * Test of main method rejection of a scene containing a non 3.0, or 3.1 DTD
     */
    @org.junit.Test(expected = RuntimeException.class)
    public void testNonStandardDTD() {
        System.out.println();
        log.info("testNonStandardDTD");
        try {
            X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "TestNonStandardDTD.x3d" });
        } catch (RuntimeException re) {
            log.warn(re);
        }
    }

    /** Test of main method rejection of a scene containing no DTD */
    @org.junit.Test(expected = Exception.class)
    public void testNoDTD() {
        System.out.println();
        log.info("testNoDTD");
        try {
            X3dHeaderChecker.main(new String[] { "-f", testFilesDir + "TestNoDTD.x3d" });
        } catch (Exception e) {
            log.info(e);
        }
    }

    /** Test of main method parsing for validate switch */
    public void testValidateSwitch() {
        System.out.println();
        log.info("testValidateSwitch");
        X3dHeaderChecker.main(new String[] { "-f", "-v", testFilesDir + "HelloWorld.x3d" });
    }
}
