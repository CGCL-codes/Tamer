package uk.org.ogsadai.activity.transform;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.SimpleMockOutputPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Test case for the <code>TupleSplitActivity</code>.
 * 
 * @author The OGSA-DAI Project Team
 */
public class TupleSplitActivityTest extends TestCase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** tuple to be split. */
    private Tuple mTuple;

    /** The Activity being tested. */
    private TupleSplitActivity mActivity;

    /**
     * Creates test case.
     * 
     * @param name
     *            name of test case
     */
    public TupleSplitActivityTest(final String name) {
        super(name);
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TupleSplitActivityTest.class);
    }

    /**
     * (non-Javadoc).
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        mActivity = new TupleSplitActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("tupleSplit"));
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivity() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        SimpleMockOutputPipe output1 = new SimpleMockOutputPipe(4);
        SimpleMockOutputPipe output2 = new SimpleMockOutputPipe(4);
        SimpleMockOutputPipe output3 = new SimpleMockOutputPipe(4);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.process();
        output1.verify();
        output2.verify();
        output3.verify();
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testActualOutput() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        Object[] expected1 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(0), mTuple.getObject(0), ControlBlock.LIST_END };
        Object[] expected2 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(1), mTuple.getObject(1), ControlBlock.LIST_END };
        Object[] expected3 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(2), mTuple.getObject(2), ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected1);
        MockOutputPipe output2 = new MockOutputPipe(expected2);
        MockOutputPipe output3 = new MockOutputPipe(expected3);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.process();
        output1.verify();
        output2.verify();
        output3.verify();
    }

    /**
     * Tests the processing of the activity without any input.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testNoInput() throws Exception {
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), ControlBlock.LIST_END });
        Object[] expected = new Object[] { ControlBlock.LIST_BEGIN, ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected);
        MockOutputPipe output2 = new MockOutputPipe(expected);
        MockOutputPipe output3 = new MockOutputPipe(expected);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.process();
        output1.verify();
        output2.verify();
        output3.verify();
    }

    /**
     * Tests the processing of the activity. Should work normally and just
     * ignor the extra columns of the tuples.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivityMoreTupleColumns() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        SimpleMockOutputPipe output1 = new SimpleMockOutputPipe(4);
        SimpleMockOutputPipe output2 = new SimpleMockOutputPipe(4);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.process();
        output1.verify();
        output2.verify();
    }

    /**
     * Tests the processing of the activity. Should throw an
     * ActivityUSerException.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivityMoreOutputs() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        SimpleMockOutputPipe output1 = new SimpleMockOutputPipe(4);
        SimpleMockOutputPipe output2 = new SimpleMockOutputPipe(4);
        SimpleMockOutputPipe output3 = new SimpleMockOutputPipe(4);
        SimpleMockOutputPipe output4 = new SimpleMockOutputPipe(4);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output4);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
        }
    }

    /**
     * Tests the case of no output provided.
     * 
     * @throws Exception
     */
    public void testNoOutput() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
        }
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

    /**
     * Tests that using the named columns in the default order works correctly.
     * 
     * @throws Exception
     */
    public void testNamedDefaultOrderOutput() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        Object[] expected1 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(0), mTuple.getObject(0), ControlBlock.LIST_END };
        Object[] expected2 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(1), mTuple.getObject(1), ControlBlock.LIST_END };
        Object[] expected3 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(2), mTuple.getObject(2), ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected1);
        MockOutputPipe output2 = new MockOutputPipe(expected2);
        MockOutputPipe output3 = new MockOutputPipe(expected3);
        Object[] indexOrder = new Object[] { ControlBlock.LIST_BEGIN, new String("col1"), new String("col2"), new String("col3"), ControlBlock.LIST_END };
        MockInputPipe inputLevel = new MockInputPipe(indexOrder);
        mActivity.addInput(TupleSplitActivity.INPUT_SPLIT, inputLevel);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.process();
        output1.verify();
        output2.verify();
        output3.verify();
    }

    /**
     * Tests that using the indexed columns in the 
     * default order works correctly.
     * 
     * @throws Exception
     */
    public void testIndexedDefaultOrderOutput() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        Object[] expected1 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(0), mTuple.getObject(0), ControlBlock.LIST_END };
        Object[] expected2 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(1), mTuple.getObject(1), ControlBlock.LIST_END };
        Object[] expected3 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(2), mTuple.getObject(2), ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected1);
        MockOutputPipe output2 = new MockOutputPipe(expected2);
        MockOutputPipe output3 = new MockOutputPipe(expected3);
        Object[] indexOrder = new Object[] { ControlBlock.LIST_BEGIN, new Integer(0), new Integer(1), new Integer(2), ControlBlock.LIST_END };
        MockInputPipe inputLevel = new MockInputPipe(indexOrder);
        mActivity.addInput(TupleSplitActivity.INPUT_SPLIT, inputLevel);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.process();
        output1.verify();
        output2.verify();
        output3.verify();
    }

    /**
     * Tests that using the named columns in non-default order works correctly.
     * 
     * @throws Exception
     */
    public void testNamedOtherOrderOutput() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        Object[] expected1 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(0), mTuple.getObject(0), ControlBlock.LIST_END };
        Object[] expected2 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(1), mTuple.getObject(1), ControlBlock.LIST_END };
        Object[] expected3 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(2), mTuple.getObject(2), ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected1);
        MockOutputPipe output2 = new MockOutputPipe(expected2);
        MockOutputPipe output3 = new MockOutputPipe(expected3);
        Object[] indexOrder = new Object[] { ControlBlock.LIST_BEGIN, new String("col3"), new String("col1"), new String("col2"), ControlBlock.LIST_END };
        MockInputPipe inputLevel = new MockInputPipe(indexOrder);
        mActivity.addInput(TupleSplitActivity.INPUT_SPLIT, inputLevel);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.process();
        output1.verify();
        output2.verify();
        output3.verify();
    }

    /**
     * Tests that using the indexed columns in non-default
     *  order works correctly.
     * 
     * @throws Exception
     */
    public void testIndexedOtherOrderOutput() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        Object[] expected1 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(0), mTuple.getObject(0), ControlBlock.LIST_END };
        Object[] expected2 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(1), mTuple.getObject(1), ControlBlock.LIST_END };
        Object[] expected3 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(2), mTuple.getObject(2), ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected1);
        MockOutputPipe output2 = new MockOutputPipe(expected2);
        MockOutputPipe output3 = new MockOutputPipe(expected3);
        Object[] indexOrder = new Object[] { ControlBlock.LIST_BEGIN, new Integer(2), new Integer(0), new Integer(1), ControlBlock.LIST_END };
        MockInputPipe inputLevel = new MockInputPipe(indexOrder);
        mActivity.addInput(TupleSplitActivity.INPUT_SPLIT, inputLevel);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.process();
        output1.verify();
        output2.verify();
        output3.verify();
    }

    /**
     * Tests that the correct error is thrown when an invalid column
     * name is input as part of the split order.
     * 
     * @throws Exception
     */
    public void testInvalidNamedOrder() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        Object[] expected1 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(0), mTuple.getObject(0), ControlBlock.LIST_END };
        Object[] expected2 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(1), mTuple.getObject(1), ControlBlock.LIST_END };
        Object[] expected3 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(2), mTuple.getObject(2), ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected1);
        MockOutputPipe output2 = new MockOutputPipe(expected2);
        MockOutputPipe output3 = new MockOutputPipe(expected3);
        Object[] indexOrder = new Object[] { ControlBlock.LIST_BEGIN, new String("col1"), new String("col2"), new String("col8"), ControlBlock.LIST_END };
        MockInputPipe inputLevel = new MockInputPipe(indexOrder);
        mActivity.addInput(TupleSplitActivity.INPUT_SPLIT, inputLevel);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        try {
            mActivity.process();
        } catch (ActivityUserException e) {
            assertEquals("should be column not found", ErrorID.COLUMN_NOT_FOUND_ERROR, e.getErrorID());
        }
    }

    /**
     * Tests that the correct error is thrown when an invalid column
     * index is input as part of the split order.
     * 
     * @throws Exception
     */
    public void testInvalidIndexedOrder() throws Exception {
        mTuple = createODTuple();
        MockInputPipe tuples = new MockInputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata(), mTuple, mTuple, ControlBlock.LIST_END });
        Object[] expected1 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(0), mTuple.getObject(0), ControlBlock.LIST_END };
        Object[] expected2 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(1), mTuple.getObject(1), ControlBlock.LIST_END };
        Object[] expected3 = new Object[] { ControlBlock.LIST_BEGIN, mTuple.getObject(2), mTuple.getObject(2), ControlBlock.LIST_END };
        MockOutputPipe output1 = new MockOutputPipe(expected1);
        MockOutputPipe output2 = new MockOutputPipe(expected2);
        MockOutputPipe output3 = new MockOutputPipe(expected3);
        Object[] indexOrder = new Object[] { ControlBlock.LIST_BEGIN, new Integer(1), new Integer(2), new Integer(4), ControlBlock.LIST_END };
        MockInputPipe inputLevel = new MockInputPipe(indexOrder);
        mActivity.addInput(TupleSplitActivity.INPUT_SPLIT, inputLevel);
        mActivity.addInput(TupleSplitActivity.INPUT_DATA, tuples);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output1);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output2);
        mActivity.addOutput(TupleSplitActivity.OUTPUT_RESULT, output3);
        try {
            mActivity.process();
        } catch (ActivityUserException e) {
            assertEquals("should be column not found", ErrorID.COLUMN_NOT_FOUND_ERROR, e.getErrorID());
        }
    }

    /**
     * Creates a metadata wrapper.
     * @return the created metadata wrapper
     */
    private MetadataWrapper createODMetadata() {
        ArrayList cdlist = new ArrayList();
        cdlist.add(new SimpleColumnMetadata("col1", TupleTypes._INT, 0, 0, 10));
        cdlist.add(new SimpleColumnMetadata("col2", TupleTypes._STRING, 0, 0, 10));
        cdlist.add(new SimpleColumnMetadata("col3", TupleTypes._SHORT, 0, 0, 10));
        SimpleTupleMetadata metadata = new SimpleTupleMetadata(cdlist);
        return new MetadataWrapper(metadata);
    }

    /**
     * Creates a tuple.
     * @return the created tuple
     */
    private Tuple createODTuple() {
        List list = new ArrayList();
        list.add(new Integer(4576382));
        list.add("Activity Test String");
        list.add(new Short((short) 42));
        return new SimpleTuple(list);
    }
}
