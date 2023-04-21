package uk.org.ogsadai.activity.block;

import java.util.ArrayList;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.BufferedPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.Pipe;
import uk.org.ogsadai.activity.io.StringBlockWriter;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Test case for <code>ListConcatenate</code> activity
 * @author OGSA-DAI team
 */
public class ListConcatenateActivityTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** The Activity being tested. */
    private Activity mActivity;

    /**
     * Creates test case.
     * 
     * @param name
     *            name of test case
     */
    public ListConcatenateActivityTest(final String name) {
        super(name);
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ListConcatenateActivityTest.class);
    }

    protected void setUp() throws Exception {
        mActivity = new ListConcatenateActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("listConcat"));
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivity() throws Exception {
        Object[] blocks_1 = { ControlBlock.LIST_BEGIN, "1", "2", ControlBlock.LIST_END };
        Object[] blocks_2 = { ControlBlock.LIST_BEGIN, "3", "4", ControlBlock.LIST_END };
        Object[] blocks_3 = { ControlBlock.LIST_BEGIN, "5", "6", ControlBlock.LIST_END };
        MockInputPipe inputOne = new MockInputPipe(blocks_1);
        MockInputPipe inputTwo = new MockInputPipe(blocks_2);
        MockInputPipe inputThree = new MockInputPipe(blocks_3);
        MockOutputPipe output = new MockOutputPipe(new Object[] { ControlBlock.LIST_BEGIN, "1", "2", "3", "4", "5", "6", ControlBlock.LIST_END });
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputOne);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputTwo);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputThree);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
        mActivity.process();
        inputOne.verify();
        inputTwo.verify();
        inputThree.verify();
        output.verify();
    }

    /**
     * Test case with multiple lists on each input
     * @throws Exception
     */
    public void testProcessActivityMultipleLists() throws Exception {
        Object[] blocks_1 = { ControlBlock.LIST_BEGIN, "1", "2", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "a", "b", ControlBlock.LIST_END };
        Object[] blocks_2 = { ControlBlock.LIST_BEGIN, "3", "4", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "c", "d", ControlBlock.LIST_END };
        Object[] blocks_3 = { ControlBlock.LIST_BEGIN, "5", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "e", ControlBlock.LIST_END };
        MockInputPipe inputOne = new MockInputPipe(blocks_1);
        MockInputPipe inputTwo = new MockInputPipe(blocks_2);
        MockInputPipe inputThree = new MockInputPipe(blocks_3);
        MockOutputPipe output = new MockOutputPipe(new Object[] { ControlBlock.LIST_BEGIN, "1", "2", "3", "4", "5", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "a", "b", "c", "d", "e", ControlBlock.LIST_END });
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputOne);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputTwo);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputThree);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
        mActivity.process();
        inputOne.verify();
        inputTwo.verify();
        inputThree.verify();
        output.verify();
    }

    /**
     * @throws Exception
     */
    public void testProcessActivityMultipleListsWithMetadata() throws Exception {
        Object[] blocks_1 = { ControlBlock.LIST_BEGIN, createODMetadata("col1"), "1", "2", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, createODMetadata("col2"), "a", "b", ControlBlock.LIST_END };
        Object[] blocks_2 = { ControlBlock.LIST_BEGIN, createODMetadata("col1"), "3", "4", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, createODMetadata("col2"), "c", "d", ControlBlock.LIST_END };
        Object[] blocks_3 = { ControlBlock.LIST_BEGIN, createODMetadata("col1"), "5", "6", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, createODMetadata("col2"), "e", "f", ControlBlock.LIST_END };
        MockInputPipe inputOne = new MockInputPipe(blocks_1);
        MockInputPipe inputTwo = new MockInputPipe(blocks_2);
        MockInputPipe inputThree = new MockInputPipe(blocks_3);
        MockOutputPipe output = new MockOutputPipe(new Object[] { ControlBlock.LIST_BEGIN, createODMetadata("col1"), "1", "2", "3", "4", "5", "6", ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, createODMetadata("col2"), "a", "b", "c", "d", "e", "f", ControlBlock.LIST_END });
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputOne);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputTwo);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputThree);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
        mActivity.process();
        inputOne.verify();
        inputTwo.verify();
        inputThree.verify();
        output.verify();
    }

    /**
     * Tests the processing of the activity.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testProcessActivityLessInputs() throws Exception {
        Object[] blocks_1 = { ControlBlock.LIST_BEGIN, "1", "2", ControlBlock.LIST_END };
        MockInputPipe inputOne = new MockInputPipe(blocks_1);
        StringBlockWriter output = new StringBlockWriter();
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, inputOne);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (InvalidActivityInputsException e) {
        }
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
        MockInputPipe input1 = new MockInputPipe(new String("this"));
        MockInputPipe input2 = new MockInputPipe(new String("is"));
        MockOutputPipe output = new MockOutputPipe(new String[] {});
        output.closeForReading();
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input1);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input2);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
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

    /**
     * Tests the case of no output provided
     * @throws Exception
     */
    public void testNoOutput() throws Exception {
        MockInputPipe input1 = new MockInputPipe(new String("this"));
        MockInputPipe input2 = new MockInputPipe(new String("is"));
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input1);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input2);
        try {
            mActivity.process();
            fail("Expected an ActivityUserException to be raised.");
        } catch (ActivityUserException e) {
        }
    }

    /**
     * Tests that we get the expected behaviour concatenating inputs that are
     * all in error.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorsOnAllInputs() throws Exception {
        Pipe input1 = new BufferedPipe("pipe1", 10);
        Pipe input2 = new BufferedPipe("pipe2", 10);
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        input1.closeForWritingDueToError();
        input2.closeForWritingDueToError();
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input1);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input2);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
        try {
            mActivity.process();
            fail("Expected an DataError");
        } catch (DataError e) {
        }
    }

    /**
     * Tests that we get the expected behaviour concatenating inputs that are
     * all in error when the partial flag is true.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorsOnAllInputsWithPartial() throws Exception {
        Pipe input1 = new BufferedPipe("pipe1", 10);
        Pipe input2 = new BufferedPipe("pipe2", 10);
        MockInputPipe partialInput = new MockInputPipe(new Object[] { Boolean.TRUE });
        MockOutputPipe output = new MockOutputPipe(new Object[] { ControlBlock.LIST_BEGIN, ControlBlock.LIST_END });
        input1.closeForWritingDueToError();
        input2.closeForWritingDueToError();
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input1);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input2);
        mActivity.addInput(ListConcatenateActivity.INPUT_PARTIAL, partialInput);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
        mActivity.process();
        output.verify();
    }

    /**
     * Tests that we get the expected behaviour when one of the inputs is in
     * error and the partial input flag is set.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorInOneInputWithPartial() throws Exception {
        Pipe input1 = new BufferedPipe("pipe1", 10);
        Pipe input2 = new BufferedPipe("pipe2", 10);
        MockInputPipe partialInput = new MockInputPipe(new Object[] { Boolean.TRUE });
        MockOutputPipe output = new MockOutputPipe(new Object[] { ControlBlock.LIST_BEGIN, new Integer(1), new Integer(2), ControlBlock.LIST_END });
        input1.closeForWritingDueToError();
        input2.write(ControlBlock.LIST_BEGIN);
        input2.write(new Integer(1));
        input2.write(new Integer(2));
        input2.write(ControlBlock.LIST_END);
        input2.closeForWriting();
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input1);
        mActivity.addInput(ListConcatenateActivity.INPUT_NAME, input2);
        mActivity.addInput(ListConcatenateActivity.INPUT_PARTIAL, partialInput);
        mActivity.addOutput(ListConcatenateActivity.OUTPUT_NAME, output);
        mActivity.process();
        output.verify();
    }

    /**
     * Auxiliary method to create TupleMetadata
     * 
     * @param colName
     *            the column name
     * @return the produced TupleMetadata
     */
    private MetadataWrapper createODMetadata(String colName) {
        ArrayList cdlist = new ArrayList();
        cdlist.add(new SimpleColumnMetadata(colName, TupleTypes._INT, 0, 0, 10));
        SimpleTupleMetadata metadata = new SimpleTupleMetadata(cdlist);
        return new MetadataWrapper(metadata);
    }
}
