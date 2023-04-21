package org.xaware.server.connector.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.xaware.testing.util.BaseTestCase;

/**
 * @author hcurtis
 * 
 */
public class TestDocLiteralSoapServlet extends BaseTestCase {

    private SoapServletTestHelper testHelper = null;

    private static final String dataFolder = "data/org/xaware/server/connector/servlet/";

    private static final boolean dumpResults = false;

    private static final boolean writeExpected = false;

    /**
     * @param name
     */
    public TestDocLiteralSoapServlet(final String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        testHelper = new SoapServletTestHelper(this.getClass(), dataFolder, XASoapStyle.DOC_LIT);
    }

    /**
     * Test basic features of SOAP Doc/Literal message. Parameters and input XML must be submitted to the BizDocument
     */
    public void testBasicDocLitSoapResponse() {
        testHelper.setTestMethodName("testBasicRpcSoapResponse");
        final File msgBodyFile = new File(dataFolder + "/basicSoapDocLit.xml");
        assertTrue("Message body file, " + msgBodyFile.getAbsolutePath() + ", does not exist", msgBodyFile.exists());
        try {
            testHelper.evaluateRequest(new FileInputStream(msgBodyFile), "basicSoapDocLit_ExpectedResponse.xml");
        } catch (final FileNotFoundException e) {
            fail("File " + dataFolder + "/basicSoapDocLit.xml not found");
        }
    }

    /**
     * Test a SOAP Doc/Literal message that has no parameters or input XML supplied. The BizDocument will execute with
     * no parameters
     */
    public void testNoParamsDocLitSoapResponse() {
        testHelper.setTestMethodName("testNoParamsDocLitSoapResponse");
        final File msgBodyFile = new File(dataFolder + "/noParamsSoapDocLit.xml");
        assertTrue("Message body file, " + msgBodyFile.getAbsolutePath() + ", does not exist", msgBodyFile.exists());
        try {
            testHelper.evaluateRequest(new FileInputStream(msgBodyFile), "noParamsSoapDocLit_ExpectedResponse.xml", dumpResults, writeExpected);
        } catch (final FileNotFoundException e) {
            fail("File " + dataFolder + "/noParamsSoapDocLit.xml not found");
        }
    }

    /**
     * Test a SOAP Doc/Literal message that has only one parameter supplied, the first one in the BizDocument, and no
     * input XML supplied. The BizDocument will execute with the first parameter and missing input XML
     */
    public void testFirstParamDocLitSoapResponse() {
        testHelper.setTestMethodName("testFirstParamDocLitSoapResponse");
        final File msgBodyFile = new File(dataFolder + "/firstParamSoapDocLit.xml");
        assertTrue("Message body file, " + msgBodyFile.getAbsolutePath() + ", does not exist", msgBodyFile.exists());
        try {
            testHelper.evaluateRequest(new FileInputStream(msgBodyFile), "firstParamSoapDocLit_ExpectedResponse.xml", dumpResults, writeExpected);
        } catch (final FileNotFoundException e) {
            fail("File " + dataFolder + "/firstParamSoapDocLit.xml not found");
        }
    }

    /**
     * Test a SOAP Doc/Literal message that has only one parameter supplied, the second one in the BizDocument, and no
     * input XML supplied. The BizDocument will execute with the second parameter supplied, the first parameter as well
     * as the input XML is missing
     */
    public void testSecondParamDocLitSoapResponse() {
        testHelper.setTestMethodName("testSecondParamDocLitSoapResponse");
        final File msgBodyFile = new File(dataFolder + "/secondParamSoapDocLit.xml");
        assertTrue("Message body file, " + msgBodyFile.getAbsolutePath() + ", does not exist", msgBodyFile.exists());
        try {
            testHelper.evaluateRequest(new FileInputStream(msgBodyFile), "secondParamSoapDocLit_ExpectedResponse.xml", dumpResults, writeExpected);
        } catch (final FileNotFoundException e) {
            fail("File " + dataFolder + "/secondParamSoapDocLit.xml not found");
        }
    }

    /**
     * Test a SOAP Doc/Literal message that has both parameters supplied. the input XML is not supplied. The BizDocument
     * will execute with both parameters supplied, the input XML is missing
     */
    public void testBothParamDocLitSoapResponse() {
        testHelper.setTestMethodName("testBothParamDocLitSoapResponse");
        final File msgBodyFile = new File(dataFolder + "/bothParamsSoapDocLit.xml");
        assertTrue("Message body file, " + msgBodyFile.getAbsolutePath() + ", does not exist", msgBodyFile.exists());
        try {
            testHelper.evaluateRequest(new FileInputStream(msgBodyFile), "bothParamsSoapDocLit_ExpectedResponse.xml", dumpResults, writeExpected);
        } catch (final FileNotFoundException e) {
            fail("File " + dataFolder + "/bothParamsSoapDocLit.xml not found");
        }
    }

    /**
     * Test a SOAP Doc/Literal message that has neither parameters supplied, but the input XML is provided in the
     * message. The BizDocument will execute with the missing parameters and the supplied input XML.
     */
    public void testOnlyInputXMLDocLitSoapResponse() {
        testHelper.setTestMethodName("testOnlyInputXMLDocLitSoapResponse");
        final File msgBodyFile = new File(dataFolder + "/onlyInputXMLSoapDocLit.xml");
        assertTrue("Message body file, " + msgBodyFile.getAbsolutePath() + ", does not exist", msgBodyFile.exists());
        try {
            testHelper.evaluateRequest(new FileInputStream(msgBodyFile), "onlyInputXMLSoapDocLit_ExpectedResponse.xml", dumpResults, writeExpected);
        } catch (final FileNotFoundException e) {
            fail("File " + dataFolder + "/onlyInputXMLSoapDocLit.xml not found");
        }
    }
}
