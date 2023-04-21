package uk.org.ogsadai.activity.delivery;

import junit.framework.TestCase;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.delivery.HTTPTestConstants;
import uk.org.ogsadai.activity.io.ActivityIOException;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.pipeline.ActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.test.TestProperties;

/**
 * <code>ObtainFromHTTPActivity</code> test class. This class expects  
 * test properties to be provided in a file whose location is
 * specified in a system property,
 * <code>ogsadai.test.properties</code>. The following properties need
 * to be provided:
 * <ul>
 * <li>
 * <code>http.host</code> - HTP host e.g. <code>www.myplace.org.uk</code>.
 * </li>
 * <li>
 * <code>http.dir.hello</code> - directory under the above e.g. <code>
 * dirOne</code>
 * </li>
 * <li>
 * <code>http.file.hello</code> - file under the above e.g. <code>
 * fileOne.txt</code>. The file should contain the text <code>Hello
 * World!</code>. 
 * </li>
 * <li>
 * <code>http.dir.bye</code> - directory under the above e.g. <code>
 * dirTwo</code>
 * </li>
 * <li>
 * <code>http.file.bye</code> - file under the above e.g. <code>
 * fileTwo.txt</code>. The file should contain the text <code>Goodbye
 * Cruel World!</code>. 
 * </li>
 * </ul>
 * <p>
 * Basically the following two files should be available:
 * <code>http://http.host/http.dir.1/http.file1</code>
 * (e.g. <code>http://www.myplace.org.uk/dirOne/fileOne.txt</code>
 * <code>http://http.host/http.dir.2/http.file2</code>
 * (e.g. <code>http://www.myplace.org.uk/dirTwo/fileTwo.txt</code>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ObtainFromHTTPActivityTest extends TestCase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2002-2009";

    /** HTTP host property name. */
    private static final String HTTP_HOST = "http.host";

    /** "Hello World" file directory name. */
    private static final String DIR_HELLO = "http.dir.hello";

    /** "Hello World" file name. */
    private static final String FILE_HELLO = "http.file.hello";

    /** "Goodbye Cruel World" file directory name. */
    private static final String DIR_BYE = "http.dir.bye";

    /** "Goodbye Cruel World" file name. */
    private static final String FILE_BYE = "http.file.bye";

    /** Test properties. */
    private final TestProperties mProperties;

    /** HTTP host. */
    private String mHTTPHost;

    /** "Hello World" file directory. */
    private String mHelloDir;

    /** "Hello World" file. */
    private String mHelloFile;

    /** "Goodbye Cruel World" file directory. */
    private String mByeDir;

    /** "Goodbye Cruel World" file. */
    private String mByeFile;

    /** Activity to run tests against. */
    private Activity mActivity;

    /**
     * Constructor.
     *
     * @param name
     *     Test case name.
     * @throws Exception
     *     If any problems arise in reading the test properties.
     */
    public ObtainFromHTTPActivityTest(final String name) throws Exception {
        super(name);
        mProperties = new TestProperties();
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *     Not used.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ObtainFromHTTPActivityTest.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     *     If any test properties are missing.
     */
    protected void setUp() throws Exception {
        mHTTPHost = mProperties.getProperty(HTTPTestConstants.HTTP_HOST);
        mHelloDir = mProperties.getProperty(HTTPTestConstants.DIR_HELLO);
        mHelloFile = mProperties.getProperty(HTTPTestConstants.FILE_HELLO);
        mByeDir = mProperties.getProperty(HTTPTestConstants.DIR_BYE);
        mByeFile = mProperties.getProperty(HTTPTestConstants.FILE_BYE);
        mActivity = new ObtainFromHTTPActivity();
        ActivityDescriptor descriptor = new SimpleActivityDescriptor("obtainFromHTTP", "myObtainFromHTTP");
        mActivity.setActivityDescriptor(descriptor);
    }

    /**
     * Tests the standard behaviour of the activity.
     * 
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testProcessActivity() throws Exception {
        String[] files = { mHelloFile, mByeFile };
        String[] host = { mHTTPHost + "/" + mHelloDir, mHTTPHost + "/" + mByeDir };
        Object[] expectedData = { ControlBlock.LIST_BEGIN, "Hello World!\n".getBytes(), ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "Goodbye Cruel World!\n".getBytes(), ControlBlock.LIST_END };
        MockInputPipe inputFilename = new MockInputPipe(files);
        MockInputPipe inputHost = new MockInputPipe(host);
        MockOutputPipe output = new MockOutputPipe(expectedData);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_FILENAME, inputFilename);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_HOST, inputHost);
        mActivity.addOutput(ObtainFromHTTPActivity.OUTPUT_DATA, output);
        mActivity.process();
        inputFilename.verify();
        inputHost.verify();
        output.verify();
    }

    /**
     * Tests the activity with an unknown filename.
     *
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testProcessActivityWrongFilename() throws Exception {
        String[] files = { "noSuchFile.txt" };
        String[] host = { mHTTPHost + "/" + mHelloDir };
        Object[] expectedData = { ControlBlock.LIST_BEGIN, "Hello World!\n".getBytes(), ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "Goodbye Cruel World!\n".getBytes(), ControlBlock.LIST_END };
        MockInputPipe inputFilename = new MockInputPipe(files);
        MockInputPipe inputHost = new MockInputPipe(host);
        MockOutputPipe output = new MockOutputPipe(expectedData);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_FILENAME, inputFilename);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_HOST, inputHost);
        mActivity.addOutput(ObtainFromHTTPActivity.OUTPUT_DATA, output);
        try {
            mActivity.process();
            fail("Expected ActivityIOException");
        } catch (ActivityIOException e) {
        }
    }

    /**
     * Tests the activity with an unknown host.
     *
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testProcessActivityWrongHost() throws Exception {
        String[] files = { mHelloFile };
        String[] host = { "noSuchHost" };
        Object[] expectedData = { ControlBlock.LIST_BEGIN, "Hello World!\n".getBytes(), ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "Goodbye Cruel World!\n".getBytes(), ControlBlock.LIST_END };
        MockInputPipe inputFilename = new MockInputPipe(files);
        MockInputPipe inputHost = new MockInputPipe(host);
        MockOutputPipe output = new MockOutputPipe(expectedData);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_FILENAME, inputFilename);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_HOST, inputHost);
        mActivity.addOutput(ObtainFromHTTPActivity.OUTPUT_DATA, output);
        try {
            mActivity.process();
            fail("Expected ActivityIOException");
        } catch (ActivityIOException e) {
        }
    }

    /**
     * Tests the activity with no inputs or outputs.
     *
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testUserError() throws Exception {
        try {
            mActivity.process();
            fail("Expected ActivityUserException");
        } catch (ActivityUserException e) {
        }
    }

    /**
     * Tests the activity when an output pipe is closed by
     * a consumer. This should cause the activity to close early
     * without raising an error.
     * 
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testProcessActivityWithEarlyClosure() throws Exception {
        String[] files = { mHelloFile };
        String[] host = { mHTTPHost + "/" + mHelloDir };
        Object[] expectedData = {};
        MockInputPipe inputFilename = new MockInputPipe(files);
        MockInputPipe inputHost = new MockInputPipe(host);
        MockOutputPipe output = new MockOutputPipe(expectedData);
        output.closeForReading();
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_FILENAME, inputFilename);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_HOST, inputHost);
        mActivity.addOutput(ObtainFromHTTPActivity.OUTPUT_DATA, output);
        mActivity.process();
        output.verify();
    }

    /**
     * Tests the activity with no output data.
     * 
     * @throws Exception
     *     If an unexpected error occurs.
     */
    public void testNoOutput() throws Exception {
        String[] files = { mHelloFile };
        String[] host = { mHTTPHost + "noSuchDir" };
        MockInputPipe inputFilename = new MockInputPipe(files);
        MockInputPipe inputHost = new MockInputPipe(host);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_FILENAME, inputFilename);
        mActivity.addInput(ObtainFromHTTPActivity.INPUT_HOST, inputHost);
        try {
            mActivity.process();
            fail("Expected ActivityUserException");
        } catch (ActivityUserException e) {
        }
    }
}
