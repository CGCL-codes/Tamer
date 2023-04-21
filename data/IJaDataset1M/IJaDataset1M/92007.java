package uk.org.ogsadai.client.toolkit.activities.delivery;

import junit.framework.TestCase;
import org.easymock.MockControl;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.RequestBuilder;
import uk.org.ogsadai.client.toolkit.activities.test.SingleOutputTestActivity;

/**
 * Test case for ObtainFromHTTP class.
 * 
 * @author The OGSA-DAI Project Team
 *
 */
public class ObtainFromHTTPTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2009.";

    /** TupleProjection activity*/
    ObtainFromHTTP mActivity;

    public void setUp() {
        mActivity = new ObtainFromHTTP();
    }

    /**
     * Constructor
     * @param arg0 not used.
     */
    public ObtainFromHTTPTest(String arg0) {
        super(arg0);
    }

    /**
     * Test the inputs
     * @throws Exception
     */
    public void testInputs() throws Exception {
        SingleOutputTestActivity inputActivity = new SingleOutputTestActivity();
        mActivity.connectFilenameInput(inputActivity.getResultOutput());
        mActivity.connectHostInput(inputActivity.getResultOutput());
        ActivityInput[] inputs = mActivity.getInputs();
        assertEquals(2, inputs.length);
        assertEquals(0, inputs[0].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_FILENAME));
        assertEquals(0, inputs[1].getSingleActivityInputs()[0].getInputDescriptor().getInputName().compareTo(mActivity.INPUT_HOST));
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
