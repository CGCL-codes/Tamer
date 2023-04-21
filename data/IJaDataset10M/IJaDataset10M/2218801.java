package uk.org.ogsadai.client.toolkit.activities.delivery;

import org.easymock.MockControl;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ModeType;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.RequestBuilder;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.client.toolkit.activities.test.SingleOutputTestActivity;
import junit.framework.TestCase;

/**
 * Test case for ObtainFromDataSource class.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ObtainFromDataSourceTest extends TestCase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2009.";

    /** Test Activity. */
    ObtainFromDataSource mActivity;

    /**
     * {@inheritDoc}
     */
    public void setUp() {
        mActivity = new ObtainFromDataSource();
    }

    /**
     * Constructor.
     * 
     * @param arg0 not used.
     */
    public ObtainFromDataSourceTest(String arg0) {
        super(arg0);
    }

    /**
     * Test the inputs.
     * 
     * @throws Exception
     */
    public void testConnectInputs() throws Exception {
        SingleOutputTestActivity inputActivity = new SingleOutputTestActivity();
        mActivity.connectURLInput(inputActivity.getResultOutput());
        mActivity.connectResourceIDInput(inputActivity.getResultOutput());
        mActivity.connectModeInput(inputActivity.getResultOutput());
        mActivity.connectNumOfBlocksInput(inputActivity.getResultOutput());
        ActivityInput[] inputs = mActivity.getInputs();
        assertEquals(5, inputs.length);
        assertEquals(0, inputs[0].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_URL));
        assertEquals(0, inputs[1].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_RESOURCE_ID));
        assertEquals(0, inputs[2].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_MODE));
        assertEquals(0, inputs[3].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_NUM_OF_BLOCKS));
        ActivityOutput[] outputs = mActivity.getOutputs();
        assertEquals(1, outputs.length);
        assertEquals(0, outputs[0].getOutputName().compareTo(mActivity.OUTPUT_DATA));
        DeliverToRequestStatus drs = new DeliverToRequestStatus();
        drs.connectInput(mActivity.getDataOutput());
        MockControl builderControl;
        builderControl = MockControl.createControl(RequestBuilder.class);
        RequestBuilder builder = (RequestBuilder) builderControl.getMock();
        builder.mustValidateState();
        builderControl.setReturnValue(true);
        builder.addActivity(null, null, null, null);
        builderControl.setMatcher(MockControl.ALWAYS_MATCHER);
        builderControl.replay();
        mActivity.buildRequest(builder);
        builderControl.verify();
    }

    /**
     * Test the inputs.
     * 
     * @throws Exception
     */
    public void testAddInputs() throws Exception {
        mActivity.addURL("http://localhost/DataSource");
        mActivity.addResourceID(new ResourceID("test"));
        mActivity.addMode(ModeType.FULL);
        mActivity.addNumOfBlocks(42);
        ActivityInput[] inputs = mActivity.getInputs();
        assertEquals(5, inputs.length);
        assertEquals(0, inputs[0].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_URL));
        assertEquals(0, inputs[1].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_RESOURCE_ID));
        assertEquals(0, inputs[2].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_MODE));
        assertEquals(0, inputs[3].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_NUM_OF_BLOCKS));
        ActivityOutput[] outputs = mActivity.getOutputs();
        assertEquals(1, outputs.length);
        assertEquals(0, outputs[0].getOutputName().compareTo(mActivity.OUTPUT_DATA));
        DeliverToRequestStatus drs = new DeliverToRequestStatus();
        drs.connectInput(mActivity.getDataOutput());
        MockControl builderControl;
        builderControl = MockControl.createControl(RequestBuilder.class);
        RequestBuilder builder = (RequestBuilder) builderControl.getMock();
        builder.mustValidateState();
        builderControl.setReturnValue(true);
        builder.addActivity(null, null, null, null);
        builderControl.setMatcher(MockControl.ALWAYS_MATCHER);
        builderControl.replay();
        mActivity.buildRequest(builder);
        builderControl.verify();
    }
}
