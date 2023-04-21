package uk.org.ogsadai.activity.block;

import java.util.Arrays;
import java.util.Iterator;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.activity.MockOutputPipe;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Test for byte array resize activity.
 *
 * @author The OGSA-DAI Team.
 */
public class ByteArraysResizeActivityTest extends TestCase {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Activity to test */
    private ByteArraysResizeActivity mActivity;

    /**
     * Constructor.
     * 
     * @param arg0
     *            not used.
     */
    public ByteArraysResizeActivityTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mActivity = new ByteArraysResizeActivity();
        mActivity.setActivityDescriptor(new SimpleActivityDescriptor("resize"));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests resizing one list of byte arrays into smaller arrays.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testOneList() throws Exception {
        String input1 = "This is a test. ";
        Object[] data = { ControlBlock.LIST_BEGIN, (input1 + input1 + input1).getBytes(), (input1 + input1 + input1).getBytes(), ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Integer(input1.length()));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        mActivity.process();
        Iterator actual = output.getActualBlocks().iterator();
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, actual.next());
        for (int i = 0; i < 6; i++) {
            assertTrue(actual.hasNext());
            assertTrue(Arrays.equals(input1.getBytes(), (byte[]) actual.next()));
        }
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_END, actual.next());
        assertTrue(!actual.hasNext());
    }

    /**
     * Tests resizing two smaller byte arrays into one big array.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testLargeArray() throws Exception {
        String input1 = "This is a test. ";
        Object[] data = { ControlBlock.LIST_BEGIN, (input1 + input1 + input1).getBytes(), (input1 + input1 + input1).getBytes(), ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Integer(1000));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        mActivity.process();
        Iterator actual = output.getActualBlocks().iterator();
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, actual.next());
        assertTrue(actual.hasNext());
        assertEquals(input1 + input1 + input1 + input1 + input1 + input1, new String((byte[]) actual.next()));
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_END, actual.next());
        assertTrue(!actual.hasNext());
    }

    /**
     * Tests resizing one list of byte arrays with the last expected array
     * smaller than the block size.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testOneListWithSmallLastArray() throws Exception {
        String input1 = "This is a test. ";
        String input2 = "Hello World.";
        Object[] data = { ControlBlock.LIST_BEGIN, (input1 + input1 + input1).getBytes(), input2.getBytes(), ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Integer(input1.length()));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        mActivity.process();
        Iterator actual = output.getActualBlocks().iterator();
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, actual.next());
        assertTrue(actual.hasNext());
        assertEquals(input1, new String((byte[]) actual.next()));
        assertTrue(actual.hasNext());
        assertEquals(input1, new String((byte[]) actual.next()));
        assertTrue(actual.hasNext());
        assertEquals(input1, new String((byte[]) actual.next()));
        assertTrue(actual.hasNext());
        assertEquals(input2, new String((byte[]) actual.next()));
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_END, actual.next());
        assertTrue(!actual.hasNext());
    }

    /**
     * Tests multiple lists.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testMultipleLists() throws Exception {
        String input1 = "This is a test. ";
        String input2 = "Hello World. This is a test. Hello World. A test.";
        Object[] data = { ControlBlock.LIST_BEGIN, (input1 + input1 + input1).getBytes(), (input1 + input1 + input1).getBytes(), ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, input2.getBytes(), ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Object[] { new Integer(input1.length()), new Integer(5) });
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        mActivity.process();
        Iterator actual = output.getActualBlocks().iterator();
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_BEGIN, actual.next());
        for (int i = 0; i < 6; i++) {
            assertTrue(actual.hasNext());
            assertEquals(input1, new String((byte[]) actual.next()));
        }
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_END, actual.next());
        assertEquals(ControlBlock.LIST_BEGIN, actual.next());
        int iterations = input2.length() / 5;
        for (int i = 0; i < iterations; i++) {
            assertTrue(actual.hasNext());
            assertEquals(input2.substring(i * 5, (i + 1) * 5), new String((byte[]) actual.next()));
        }
        assertTrue(actual.hasNext());
        assertEquals(input2.substring(iterations * 5), new String((byte[]) actual.next()));
        assertTrue(actual.hasNext());
        assertEquals(ControlBlock.LIST_END, actual.next());
        assertTrue(!actual.hasNext());
    }

    /**
     * Tests processing an empty list.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testEmptyList() throws Exception {
        Object[] data = { ControlBlock.LIST_BEGIN, ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Integer(20));
        MockOutputPipe output = new MockOutputPipe(new Object[] { ControlBlock.LIST_BEGIN, ControlBlock.LIST_END });
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        mActivity.process();
        output.verify();
    }

    /**
     * Tests passing a negative array size as parameter.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testNegativeArraySize() throws Exception {
        Object[] data = { ControlBlock.LIST_BEGIN, ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Integer(-5));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        try {
            mActivity.process();
            fail("ArraySizeNegativeException expected");
        } catch (ActivityUserException e) {
            assertEquals(ErrorID.ARRAY_SIZE_INVALID, e.getErrorID());
        }
    }

    /**
     * Tests passing a negative array size as parameter.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testArraySizeZero() throws Exception {
        Object[] data = { ControlBlock.LIST_BEGIN, ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Integer(0));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        try {
            mActivity.process();
            fail("ArraySizeInvalidException expected");
        } catch (ActivityUserException e) {
            assertEquals(ErrorID.ARRAY_SIZE_INVALID, e.getErrorID());
            assertEquals(0, ((Integer) e.getParameters()[0]).intValue());
            assertEquals("sizeInBytes", e.getParameters()[1]);
        }
    }

    /**
     * Tests that the activity is terminated when receiving a terminated
     * exception by the pipe.
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testTerminated() throws Exception {
        Object[] data = { ControlBlock.LIST_BEGIN, "HelloWorld".getBytes(), ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        input.raisePipeTerminatedException(true);
        MockInputPipe size = new MockInputPipe(new Integer(5));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        try {
            mActivity.process();
            fail("ActivityTerminatedException expected.");
        } catch (ActivityTerminatedException e) {
        }
    }

    /**
     * Tests passing input parameters of the wrong type!
     * 
     * @throws Exception
     *             if an unexpected error occurs
     */
    public void testInvalidInputTypes() throws Exception {
        String inputString = "This is a test. ";
        Object[] data = { ControlBlock.LIST_BEGIN, new Integer(12), ControlBlock.LIST_END };
        MockInputPipe input = new MockInputPipe(data);
        MockInputPipe size = new MockInputPipe(new Integer(10));
        MockOutputPipe output = new MockOutputPipe(new Object[] {});
        mActivity.addInput("data", input);
        mActivity.addInput("sizeInBytes", size);
        mActivity.addOutput("result", output);
        try {
            mActivity.process();
            fail("ActivityUserException expected.");
        } catch (ActivityUserException e) {
        }
    }
}
