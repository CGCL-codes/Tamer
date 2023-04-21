package uk.org.ogsadai.activity.delivery;

import org.easymock.MockControl;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.datasource.DataSourceResource;
import junit.framework.TestCase;

/**
 * Test class for <code>WriteToDataSourceActivityTest</code>.
 *
 * @author The OGSA-DAI Project Team
 */
public class WriteToDataSourceActivityTest extends TestCase {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2002 - 2007.";

    /**
     * Constructor.
     * 
     * @param arg0 not used.
     */
    public WriteToDataSourceActivityTest(String arg0) {
        super(arg0);
    }

    /**
     * Run test for command line.
     * 
     * @param args not used.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(WriteToDataSourceActivityTest.class);
    }

    /**
     * Tests basic functionality.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testBasicBehaviour() throws Exception {
        final int NUM_BLOCKS = 1000;
        Object[] dataStream = new Object[NUM_BLOCKS];
        for (int i = 0; i < dataStream.length; ++i) {
            dataStream[i] = "Hello" + i;
        }
        MockInputPipe mockInputPipe = new MockInputPipe(dataStream);
        MockControl controlDataSourceResource = MockControl.createControl(DataSourceResourceAndResourceAccessor.class);
        DataSourceResourceAndResourceAccessor mockDataSourceResource = (DataSourceResourceAndResourceAccessor) controlDataSourceResource.getMock();
        for (int i = 0; i < dataStream.length; i++) {
            mockDataSourceResource.write(dataStream[i]);
        }
        mockDataSourceResource.closeForWriting();
        controlDataSourceResource.replay();
        WriteToDataSourceActivity activity = new WriteToDataSourceActivity();
        activity.addInput(WriteToDataSourceActivity.INPUT, mockInputPipe);
        activity.setTargetResourceAccessor(mockDataSourceResource);
        activity.process();
        mockInputPipe.verify();
        controlDataSourceResource.verify();
    }

    /**
     * Tests the activity behaviour when the pipe throws a pipe terminated
     * exception.
     * 
     * @throws Exception
     */
    public void testPipeThrowsTerminatedException() throws Exception {
        MockInputPipe mockInputPipe = new MockInputPipe(new Object[] {});
        mockInputPipe.raisePipeTerminatedException(true);
        MockOutputPipe mockOutputPipe = new MockOutputPipe(new Object[] {});
        MockControl controlDataSourceResource = MockControl.createControl(DataSourceResourceAndResourceAccessor.class);
        DataSourceResourceAndResourceAccessor mockDataSourceResource = (DataSourceResourceAndResourceAccessor) controlDataSourceResource.getMock();
        mockDataSourceResource.closeForWritingDueToError();
        controlDataSourceResource.replay();
        WriteToDataSourceActivity activity = new WriteToDataSourceActivity();
        activity.addInput(WriteToDataSourceActivity.INPUT, mockInputPipe);
        activity.setTargetResourceAccessor(mockDataSourceResource);
        try {
            activity.process();
            fail("process() must throw an ActivityTerminatedException");
        } catch (ActivityTerminatedException e) {
        }
        mockInputPipe.verify();
        controlDataSourceResource.verify();
    }

    /**
     * Test the activity behaviour when the pipe throws an PipeIO error.
     * 
     * @throws Exception  
     */
    public void testPipeThrowsPipeIOException() throws Exception {
        MockInputPipe mockInputPipe = new MockInputPipe(new Object[] {});
        mockInputPipe.raisePipeIOException(true);
        MockOutputPipe mockOutputPipe = new MockOutputPipe(new Object[] {});
        MockControl controlDataSourceResource = MockControl.createControl(DataSourceResourceAndResourceAccessor.class);
        DataSourceResourceAndResourceAccessor mockDataSourceResource = (DataSourceResourceAndResourceAccessor) controlDataSourceResource.getMock();
        WriteToDataSourceActivity activity = new WriteToDataSourceActivity();
        activity.addInput(WriteToDataSourceActivity.INPUT, mockInputPipe);
        activity.setTargetResourceAccessor(mockDataSourceResource);
        try {
            activity.process();
            fail("process() must throw an ActivityPipeProcessingException");
        } catch (ActivityPipeProcessingException e) {
        } catch (Exception e) {
            fail("Should not get any exception other " + "than ActivityPipeProcessingException");
        }
    }

    /**
     * Interface used to allow us to mock two interfaces in one object.
     */
    private interface DataSourceResourceAndResourceAccessor extends DataSourceResource, ResourceAccessor {
    }
}
