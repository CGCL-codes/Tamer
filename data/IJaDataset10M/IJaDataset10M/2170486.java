package uk.org.ogsadai.activity.management;

import junit.framework.TestCase;
import org.easymock.MockControl;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityOutputsException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.extension.ResourceFactoryActivity;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.pipeline.ActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceIDAlreadyAssignedException;
import uk.org.ogsadai.resource.datasource.DataSourceResource;

/**
 * Test class for <code>CreateDataSourceActivity</code>.
 * 
 *
 * @author The OGSA-DAI Project Team
 */
public class CreateDataSourceActivityTest extends TestCase {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2002 - 2007.";

    /** The Activity being tested. */
    private Activity mActivity;

    /**
     * Creates test case.
     * 
     * @param name
     *            name of test case
     */
    public CreateDataSourceActivityTest(final String name) {
        super(name);
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(CreateDataSourceActivityTest.class);
    }

    protected void setUp() throws Exception {
        mActivity = new CreateDataSourceActivity();
        ActivityDescriptor descriptor = new SimpleActivityDescriptor("anyActivityNameWillDo", "anyInstanceNameWillDo");
        mActivity.setActivityDescriptor(descriptor);
    }

    /**
     * Tests the activity when no input is provided.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testWithNoInput() throws Exception {
        String resourceIDAsString = "my.resource";
        ResourceID resourceID = new ResourceID(resourceIDAsString);
        MockOutputPipe output = new MockOutputPipe(new Object[] { resourceIDAsString });
        mActivity.addOutput(CreateDataSourceActivity.OUTPUT, output);
        MockControl controlDataSourceResource = MockControl.createControl(DataSourceResource.class);
        DataSourceResource mockDataSourceResource = (DataSourceResource) controlDataSourceResource.getMock();
        MockControl controlResourceFactory = MockControl.createControl(ResourceFactory.class);
        ResourceFactory mockResourceFactory = (ResourceFactory) controlResourceFactory.getMock();
        mockResourceFactory.createDataSourceResource();
        controlResourceFactory.setReturnValue(mockDataSourceResource);
        mockResourceFactory.addResource(mockDataSourceResource);
        controlResourceFactory.setReturnValue(resourceID);
        ((ResourceFactoryActivity) mActivity).setResourceFactory(mockResourceFactory);
        controlResourceFactory.replay();
        controlDataSourceResource.replay();
        mActivity.process();
        output.verify();
        controlResourceFactory.verify();
        controlDataSourceResource.verify();
    }

    /**
     * Tests the activity when an input is used to specify resource IDs.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testWithInput() throws Exception {
        final int NUM_INPUTS = 10;
        ResourceID[] resourceIDs = new ResourceID[NUM_INPUTS];
        Object[] inputBlocks = new Object[NUM_INPUTS];
        Object[] outputBlocks = new Object[NUM_INPUTS];
        for (int i = 0; i < NUM_INPUTS; ++i) {
            resourceIDs[i] = new ResourceID("my.resource.id" + i);
            inputBlocks[i] = resourceIDs[i].toString();
            outputBlocks[i] = resourceIDs[i].toString();
        }
        MockInputPipe input = new MockInputPipe(inputBlocks);
        mActivity.addInput(CreateDataSourceActivity.INPUT_RESOURCE_ID, input);
        MockOutputPipe output = new MockOutputPipe(outputBlocks);
        mActivity.addOutput(CreateDataSourceActivity.OUTPUT, output);
        MockControl controlResourceFactory = MockControl.createControl(ResourceFactory.class);
        ResourceFactory mockResourceFactory = (ResourceFactory) controlResourceFactory.getMock();
        for (int i = 0; i < NUM_INPUTS; ++i) {
            MockControl controlDataSourceResource = MockControl.createControl(DataSourceResource.class);
            DataSourceResource mockDataSourceResource = (DataSourceResource) controlDataSourceResource.getMock();
            mockResourceFactory.createDataSourceResource();
            controlResourceFactory.setReturnValue(mockDataSourceResource);
            mockResourceFactory.addResource(resourceIDs[i], mockDataSourceResource);
        }
        ((ResourceFactoryActivity) mActivity).setResourceFactory(mockResourceFactory);
        controlResourceFactory.replay();
        mActivity.process();
        output.verify();
        controlResourceFactory.verify();
    }

    /**
     * Tests that activity throws the correct exception when an input of
     * incorrect type is used to specify resource IDs.
     * 
     * @throws Exception
     *             if an unexpected error occurs.
     */
    public void testWithInputNoString() throws Exception {
        Integer[] inputBlocks = new Integer[] { new Integer(1) };
        MockInputPipe input = new MockInputPipe(inputBlocks);
        mActivity.addInput(CreateDataSourceActivity.INPUT_RESOURCE_ID, input);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addOutput(CreateDataSourceActivity.OUTPUT, output);
        MockControl controlResourceFactory = MockControl.createControl(ResourceFactory.class);
        ResourceFactory mockResourceFactory = (ResourceFactory) controlResourceFactory.getMock();
        MockControl controlDataSourceResource = MockControl.createControl(DataSourceResource.class);
        DataSourceResource mockDataSourceResource = (DataSourceResource) controlDataSourceResource.getMock();
        mockResourceFactory.createDataSourceResource();
        controlResourceFactory.setReturnValue(mockDataSourceResource);
        ((ResourceFactoryActivity) mActivity).setResourceFactory(mockResourceFactory);
        controlResourceFactory.replay();
        try {
            mActivity.process();
            fail("Must throw ActivityUserException");
        } catch (ActivityUserException e) {
            assertTrue("Must be InvalidInputValueException", e instanceof InvalidInputValueException);
        }
    }

    /**
     * Tests that the correct exception is thrown if the input specfies a 
     * resource ID that is already assigned.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testAlreadyAssignedResourceID() throws Exception {
        ResourceID resourceID = new ResourceID("my.resource.id");
        MockInputPipe input = new MockInputPipe(new Object[] { resourceID.toString() });
        mActivity.addInput(CreateDataSourceActivity.INPUT_RESOURCE_ID, input);
        MockOutputPipe output = new MockOutputPipe(new Object[] { resourceID.toString() });
        mActivity.addOutput(CreateDataSourceActivity.OUTPUT, output);
        MockControl controlResourceFactory = MockControl.createControl(ResourceFactory.class);
        ResourceFactory mockResourceFactory = (ResourceFactory) controlResourceFactory.getMock();
        MockControl controlDataSourceResource = MockControl.createControl(DataSourceResource.class);
        DataSourceResource mockDataSourceResource = (DataSourceResource) controlDataSourceResource.getMock();
        mockResourceFactory.createDataSourceResource();
        controlResourceFactory.setReturnValue(mockDataSourceResource);
        mockResourceFactory.addResource(resourceID, mockDataSourceResource);
        controlResourceFactory.setThrowable(new ResourceIDAlreadyAssignedException(resourceID));
        ((ResourceFactoryActivity) mActivity).setResourceFactory(mockResourceFactory);
        controlResourceFactory.replay();
        try {
            mActivity.process();
            fail("Must throw ActivityUserException");
        } catch (ActivityUserException e) {
            Throwable cause = e.getCause();
            assertTrue("cause must not be null", cause != null);
            assertTrue("cause must be ResourceIDAlreadyAssignedException", cause instanceof ResourceIDAlreadyAssignedException);
            ResourceIDAlreadyAssignedException idAssignedEx = (ResourceIDAlreadyAssignedException) cause;
            assertEquals("Exception must contain the correct resource ID", resourceID, idAssignedEx.getResourceID());
        }
        controlResourceFactory.verify();
    }

    /**
     * Tests that the correct exception is thrown if the activity is configured
     * with no output.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testWithNoOutput() throws Exception {
        ResourceID resourceID = new ResourceID("my.resource.id");
        MockInputPipe input = new MockInputPipe(new Object[] { resourceID.toString() });
        mActivity.addInput(CreateDataSourceActivity.INPUT_RESOURCE_ID, input);
        MockControl controlResourceFactory = MockControl.createControl(ResourceFactory.class);
        ResourceFactory mockResourceFactory = (ResourceFactory) controlResourceFactory.getMock();
        MockControl controlDataSourceResource = MockControl.createControl(DataSourceResource.class);
        DataSourceResource mockDataSourceResource = (DataSourceResource) controlDataSourceResource.getMock();
        mockResourceFactory.createDataSourceResource();
        controlResourceFactory.setReturnValue(mockDataSourceResource);
        mockResourceFactory.addResource(resourceID, mockDataSourceResource);
        ((ResourceFactoryActivity) mActivity).setResourceFactory(mockResourceFactory);
        controlResourceFactory.replay();
        try {
            mActivity.process();
            fail("Must throw InvalidActivityOutputsException");
        } catch (InvalidActivityOutputsException e) {
            assertEquals("Exception must refer to the OUTPUT output", CreateDataSourceActivity.OUTPUT, e.getOutputName());
            assertEquals("Exception must specify that one output was required", 1, e.getExpectedNumberOfOutputs());
        }
    }
}
