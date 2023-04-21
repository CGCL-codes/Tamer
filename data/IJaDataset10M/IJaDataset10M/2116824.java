package uk.org.ogsadai.service.cxf.request;

import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityStatus;
import uk.org.ogsadai.activity.request.status.SimpleRequestStatus;
import uk.org.ogsadai.data.BinaryData;
import uk.org.ogsadai.data.BooleanData;
import uk.org.ogsadai.data.CharData;
import uk.org.ogsadai.data.DataValue;
import uk.org.ogsadai.data.DoubleData;
import uk.org.ogsadai.data.FloatData;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.LongData;
import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;
import uk.org.ogsadai.service.cxf.types.ActivityStatusType;
import uk.org.ogsadai.service.cxf.types.DataType;
import uk.org.ogsadai.service.cxf.types.RequestStatusType;
import uk.org.ogsadai.service.cxf.types.ResultType;

/**
 * Tests for factory that creates an beans from a request status.
 *
 * @author The OGSA-DAI Team.
 */
public class CXFRequestStatusBeanFactoryTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010.";

    /** Factory to test */
    private CXFRequestStatusBeanFactory mFactory;

    /** Request status to convert into a bean */
    private SimpleRequestStatus mRequestStatus;

    /**
     * Constructor.
     * @param arg0
     */
    public CXFRequestStatusBeanFactoryTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ResourceID requestID = new ResourceID("request");
        mRequestStatus = new SimpleRequestStatus(requestID);
        mFactory = new CXFRequestStatusBeanFactory();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests creating a bean.
     *
     */
    public void testCreateBean() {
        ActivityInstanceName activity1 = new ActivityInstanceName("activity1");
        byte[] bytes = new byte[] { 1, 2, 3, 4 };
        DataValue binaryData = new BinaryData(bytes);
        DataValue charData = new CharData(new char[] { 'A', 'B', 'C', 'a', 'b', 'c' });
        LongData longData = new LongData(2459056456L);
        IntegerData integerData = new IntegerData(245905);
        BooleanData booleanData = new BooleanData(true);
        FloatData floatData = new FloatData(24.563423f);
        DoubleData doubleData = new DoubleData(298456.21236);
        mRequestStatus.addResult(activity1, "result1", binaryData);
        mRequestStatus.addResult(activity1, "result1", charData);
        mRequestStatus.addResult(activity1, "result1", ListBegin.VALUE);
        mRequestStatus.addResult(activity1, "result1", longData);
        mRequestStatus.addResult(activity1, "result1", integerData);
        mRequestStatus.addResult(activity1, "result1", floatData);
        mRequestStatus.addResult(activity1, "result1", booleanData);
        mRequestStatus.addResult(activity1, "result1", doubleData);
        mRequestStatus.addResult(activity1, "result1", ListEnd.VALUE);
        mRequestStatus.setRequestExecutionStatus(RequestExecutionStatus.COMPLETED);
        mRequestStatus.setActivityStatus(activity1, ActivityStatus.ERROR);
        DAIException exception = new ActivityProcessingException(ErrorID.ACTIVITY_IO_EXCEPTION);
        mRequestStatus.setActivityError(activity1, exception);
        Object obj = mFactory.createBean(mRequestStatus);
        assertTrue(obj instanceof RequestStatusType);
        RequestStatusType requestStatus = (RequestStatusType) obj;
        ActivityStatusType[] activityStatus = requestStatus.getActivity().toArray(new ActivityStatusType[requestStatus.getActivity().size()]);
        assertEquals(1, activityStatus.length);
        assertEquals(activity1.toString(), activityStatus[0].getInstanceName());
        assertEquals(ActivityStatus.ERROR.toString(), activityStatus[0].getStatus().value());
        ResultType[] results = requestStatus.getResult().toArray(new ResultType[requestStatus.getResult().size()]);
        assertEquals(1, results.length);
        assertEquals(activity1.toString(), results[0].getActivityInstanceName());
        DataType[] data = results[0].getData().toArray(new DataType[results[0].getData().size()]);
        assertEquals(9, data.length);
        assertEquals("4", new String(bytes), new String(data[0].getBinary()));
        assertEquals(charData.toString(), data[1].getCharArray());
        assertNotNull(data[2].getListBegin());
        assertEquals(longData.getLong(), data[3].getLong().longValue());
        assertEquals(integerData.getInteger(), data[4].getInt().intValue());
        assertEquals(floatData.getFloat(), data[5].getFloat().floatValue(), 0);
        assertEquals(booleanData.getBoolean(), data[6].isBoolean().booleanValue());
        assertEquals(doubleData.getDouble(), data[7].getDouble().doubleValue(), 0);
        assertNotNull(data[8].getListEnd());
    }
}
