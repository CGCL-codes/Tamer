package uk.org.ogsadai.activity.transform;

import java.io.File;
import java.io.IOException;
import javax.xml.transform.TransformerException;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.InvalidActivityOutputsException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.test.TestHelper;
import uk.org.ogsadai.util.FileUtilities;

/**
 * Tests the <code>XSLTransformationActivity</code> class.
 *
 * @author The OGSA-DAI Project Team.
 */
public class XSLTransformActivityTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2009.";

    /** Mame of file containing the expected results of the transformation. */
    public static final String RESULTS_FILENAME = "results.html";

    /** Name of file containing the XML to be transformed. */
    public static final String XML_FILENAME = "resourceSet.xml";

    /** Name of file containing the XSLT transform. */
    public static final String XSLT_FILENAME = "transform.xsl";

    /** File containing the expected results of the transformation. */
    public File mResultsFile = null;

    /** File containing the XML to be transformed. */
    public File mXMLFile = null;

    /** File containing the XSLT transform. */
    public File mXSLTFile = null;

    /** The activity being tested. */
    private Activity mActivity;

    /**
     * Creates test case.
     * 
     * @param name
     *            name of test case
     */
    public XSLTransformActivityTest(final String name) {
        super(name);
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(XSLTransformActivityTest.class);
    }

    protected void setUp() throws Exception {
        mActivity = new XSLTransformActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("anything"));
        mResultsFile = TestHelper.getFileFromClassName(getClass(), RESULTS_FILENAME);
        mXMLFile = TestHelper.getFileFromClassName(getClass(), XML_FILENAME);
        mXSLTFile = TestHelper.getFileFromClassName(getClass(), XSLT_FILENAME);
    }

    /**
     * Test the core functionality of the activity
     * 
     * @throws Exception
     */
    public void testOneIteration() throws Exception {
        char[] xslt = FileUtilities.toCharArray(mXSLTFile.getPath());
        char[] xml = FileUtilities.toCharArray(mXMLFile.getPath());
        char[] results = FileUtilities.toCharArray(mResultsFile.getPath());
        Object[] xsltBlocks = { ControlBlock.LIST_BEGIN, xslt, ControlBlock.LIST_END };
        Object[] xmlBlocks = { ControlBlock.LIST_BEGIN, xml, ControlBlock.LIST_END };
        Object[] expectedOutput = { ControlBlock.LIST_BEGIN, results, ControlBlock.LIST_END };
        MockInputPipe xsltInput = new MockInputPipe(xsltBlocks);
        MockInputPipe xmlInput = new MockInputPipe(xmlBlocks);
        MockOutputPipe charOutput = new MockOutputPipe(expectedOutput);
        mActivity.addInput(XSLTransformActivity.INPUT_XSLT, xsltInput);
        mActivity.addInput(XSLTransformActivity.INPUT_XML, xmlInput);
        mActivity.addOutput(XSLTransformActivity.OUTPUT_RESULT, charOutput);
        mActivity.process();
        charOutput.verify();
    }

    /**
     * Test the core functionality of the activity
     * 
     * @throws Exception
     */
    public void testMultipleIterations() throws Exception {
        char[] xslt = FileUtilities.toCharArray(mXSLTFile.getPath());
        char[] xml = FileUtilities.toCharArray(mXMLFile.getPath());
        char[] results = FileUtilities.toCharArray(mResultsFile.getPath());
        Object[] xsltBlocks = { ControlBlock.LIST_BEGIN, xslt, ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, xslt, ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, xslt, ControlBlock.LIST_END };
        Object[] xmlBlocks = { ControlBlock.LIST_BEGIN, xml, ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, xml, ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, xml, ControlBlock.LIST_END };
        Object[] expectedOutput = { ControlBlock.LIST_BEGIN, results, ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, results, ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, results, ControlBlock.LIST_END };
        MockInputPipe xsltInput = new MockInputPipe(xsltBlocks);
        MockInputPipe xmlInput = new MockInputPipe(xmlBlocks);
        MockOutputPipe charOutput = new MockOutputPipe(expectedOutput);
        mActivity.addInput(XSLTransformActivity.INPUT_XSLT, xsltInput);
        mActivity.addInput(XSLTransformActivity.INPUT_XML, xmlInput);
        mActivity.addOutput(XSLTransformActivity.OUTPUT_RESULT, charOutput);
        mActivity.process();
        charOutput.verify();
    }

    /**
     * Tests that an exception is thrown when the XSL transform is not a valid
     * document.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testMalformedXSL() throws Exception {
        char[] xslt = "This is not XML".toCharArray();
        char[] xml = FileUtilities.toCharArray(mXMLFile.getPath());
        Object[] xsltBlocks = { ControlBlock.LIST_BEGIN, xslt, ControlBlock.LIST_END };
        Object[] xmlBlocks = { ControlBlock.LIST_BEGIN, xml, ControlBlock.LIST_END };
        Object[] expectedOutput = { ControlBlock.LIST_BEGIN };
        MockInputPipe xsltInput = new MockInputPipe(xsltBlocks);
        MockInputPipe xmlInput = new MockInputPipe(xmlBlocks);
        MockOutputPipe charOutput = new MockOutputPipe(expectedOutput);
        mActivity.addInput(XSLTransformActivity.INPUT_XSLT, xsltInput);
        mActivity.addInput(XSLTransformActivity.INPUT_XML, xmlInput);
        mActivity.addOutput(XSLTransformActivity.OUTPUT_RESULT, charOutput);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
            assertEquals(ErrorID.XML_TRANSFORMATION_ERROR, e.getErrorID());
        }
        charOutput.verify();
    }

    /**
     * Tests that an exception is thrown when the XSL transform is not a valid
     * document.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testMalformedXML() throws Exception {
        char[] xslt = FileUtilities.toCharArray(mXSLTFile.getPath());
        char[] xml = "This is not XML".toCharArray();
        Object[] xsltBlocks = { ControlBlock.LIST_BEGIN, xslt, ControlBlock.LIST_END };
        Object[] xmlBlocks = { ControlBlock.LIST_BEGIN, xml, ControlBlock.LIST_END };
        Object[] expectedOutput = { ControlBlock.LIST_BEGIN };
        MockInputPipe xsltInput = new MockInputPipe(xsltBlocks);
        MockInputPipe xmlInput = new MockInputPipe(xmlBlocks);
        MockOutputPipe charOutput = new MockOutputPipe(expectedOutput);
        mActivity.addInput(XSLTransformActivity.INPUT_XSLT, xsltInput);
        mActivity.addInput(XSLTransformActivity.INPUT_XML, xmlInput);
        mActivity.addOutput(XSLTransformActivity.OUTPUT_RESULT, charOutput);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
            assertEquals(ErrorID.XML_TRANSFORMATION_ERROR, e.getErrorID());
        }
        charOutput.verify();
    }

    /**
     * Tests that an exception is thrown when the XSL transform is not a valid
     * document.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testWrongXMLInputType() throws Exception {
        char[] xslt = FileUtilities.toCharArray(mXSLTFile.getPath());
        Object[] xsltBlocks = { ControlBlock.LIST_BEGIN, xslt, ControlBlock.LIST_END };
        Object[] xmlBlocks = { ControlBlock.LIST_BEGIN, new int[1], ControlBlock.LIST_END };
        Object[] expectedOutput = new Object[] { ControlBlock.LIST_BEGIN };
        MockInputPipe xsltInput = new MockInputPipe(xsltBlocks);
        MockInputPipe xmlInput = new MockInputPipe(xmlBlocks);
        MockOutputPipe charOutput = new MockOutputPipe(expectedOutput);
        mActivity.addInput(XSLTransformActivity.INPUT_XSLT, xsltInput);
        mActivity.addInput(XSLTransformActivity.INPUT_XML, xmlInput);
        mActivity.addOutput(XSLTransformActivity.OUTPUT_RESULT, charOutput);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
            assertEquals(ErrorID.XML_TRANSFORMATION_ERROR, e.getErrorID());
            assertTrue(e.getCause() instanceof TransformerException);
            assertTrue(e.getCause().getCause() instanceof IOException);
            assertTrue(e.getCause().getCause().getCause() instanceof InvalidInputValueException);
        }
        charOutput.verify();
    }

    /**
     * Tests the case where no output is provided.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testNoOutput() throws Exception {
        MockInputPipe xsltInput = new MockInputPipe(new Object[] {});
        MockInputPipe xmlInput = new MockInputPipe(new Object[] {});
        mActivity.addInput(XSLTransformActivity.INPUT_XSLT, xsltInput);
        mActivity.addInput(XSLTransformActivity.INPUT_XML, xmlInput);
        try {
            mActivity.process();
            fail("Expected an InvalidActivityOutputsException to be raised.");
        } catch (InvalidActivityOutputsException e) {
            assertEquals("result", e.getOutputName());
            assertEquals(1, e.getExpectedNumberOfOutputs());
        }
    }

    /**
     * Tests the case where the activity is processed with no output attached.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testNoInput() throws Exception {
        try {
            mActivity.process();
            fail("Expected an InvalidActivityInputsException to be raised.");
        } catch (InvalidActivityInputsException e) {
        }
    }
}
