package uk.org.ogsadai.activity.transform;

import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.SimpleMockOutputPipe;
import uk.org.ogsadai.activity.io.StringBlockWriter;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;

/**
 * Test case for the <code>StringTokenizerActivity</code>.
 * 
 * @author The OGSA-DAI Project Team
 */
public class StringTokenizeActivityTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** The Activity being tested. */
    private StringTokenizeActivity mActivity;

    /**
     * Creates test case.
     * 
     * @param name
     *            name of test case
     */
    public StringTokenizeActivityTest(final String name) {
        super(name);
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(StringTokenizeActivityTest.class);
    }

    protected void setUp() throws Exception {
        mActivity = new StringTokenizeActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("stringTokenizer"));
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivity() throws Exception {
        MockInputPipe delimeter = new MockInputPipe(new String(":"));
        MockInputPipe data = new MockInputPipe(new String("this:is:a:test"));
        SimpleMockOutputPipe output = new SimpleMockOutputPipe(6);
        mActivity.addInput(StringTokenizeActivity.INPUT_SEPARATOR, delimeter);
        mActivity.addInput(StringTokenizeActivity.INPUT_DATA, data);
        mActivity.addOutput(StringTokenizeActivity.OUTPUT_TOKENS, output);
        mActivity.process();
        output.verify();
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testActualOutput() throws Exception {
        MockInputPipe delimeter = new MockInputPipe(new String(":"));
        MockInputPipe data = new MockInputPipe(new String("this:is:a:test"));
        StringBlockWriter output = new StringBlockWriter();
        mActivity.addInput(StringTokenizeActivity.INPUT_SEPARATOR, delimeter);
        mActivity.addInput(StringTokenizeActivity.INPUT_DATA, data);
        mActivity.addOutput(StringTokenizeActivity.OUTPUT_TOKENS, output);
        mActivity.process();
        String actual = output.getOutputData();
        String expected = new String("LIST_BEGINthisisatestLIST_END");
        assertEquals(expected, actual);
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testEmptyResults() throws Exception {
        MockInputPipe delimeter = new MockInputPipe(new String("."));
        MockInputPipe data = new MockInputPipe(new String("this:is:a:test"));
        SimpleMockOutputPipe output = new SimpleMockOutputPipe(2);
        mActivity.addInput(StringTokenizeActivity.INPUT_SEPARATOR, delimeter);
        mActivity.addInput(StringTokenizeActivity.INPUT_DATA, data);
        mActivity.addOutput(StringTokenizeActivity.OUTPUT_TOKENS, output);
        mActivity.process();
        output.verify();
    }

    /**
     * Tests the case where an activity is processed but the output pipe has
     * been closed by the consumer. This should cause the activity to close
     * early without raising an error.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivityWithEarlyClosure() throws Exception {
        MockInputPipe delimeter = new MockInputPipe(new String("."));
        MockInputPipe data = new MockInputPipe(new String("this:is:a:test"));
        MockOutputPipe output = new MockOutputPipe(new String[] {});
        output.closeForReading();
        mActivity.addInput(StringTokenizeActivity.INPUT_SEPARATOR, delimeter);
        mActivity.addInput(StringTokenizeActivity.INPUT_DATA, data);
        mActivity.addOutput(StringTokenizeActivity.OUTPUT_TOKENS, output);
        mActivity.process();
        output.verify();
    }

    /**
     * Tests the case where the activity is processed with no output attached.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testUserError() throws Exception {
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
        }
    }
}
