package uk.org.ogsadai.activity.io;

import java.io.InputStream;
import java.io.Reader;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.MockInputPipe;
import uk.org.ogsadai.util.IOUtilities;

public class ReaderListActivityInputTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007";

    /** The object being tested. */
    private ActivityInput mInput;

    /**
     * Creates test case.
     * 
     * @param name
     *     Name of test case
     */
    public ReaderListActivityInputTest(final String name) {
        super(name);
    }

    /**
     * Runs the test cases.
     * 
     * @param args
     *     Not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ReaderListActivityInputTest.class);
    }

    protected void setUp() throws Exception {
        Object[] data = new Object[] { ControlBlock.LIST_BEGIN, ControlBlock.LIST_BEGIN, "data1".toCharArray(), "data2".toCharArray(), ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "data3".toCharArray(), "data4".toCharArray(), ControlBlock.LIST_END, ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, ControlBlock.LIST_BEGIN, "data5".toCharArray(), "data6".toCharArray(), ControlBlock.LIST_END, ControlBlock.LIST_BEGIN, "data7".toCharArray(), "data8".toCharArray(), ControlBlock.LIST_END, ControlBlock.LIST_END };
        BlockReader blockReader = new MockInputPipe(data);
        mInput = new ReaderListActivityInput("myInput");
        mInput.setBlockReader(blockReader);
    }

    public void testRead() throws Exception {
        Object block = mInput.read();
        assertTrue("The first ListIterator block was expected", block instanceof ListIterator);
        ListIterator list = (ListIterator) block;
        assertEquals("The first input stream does not contain the expected data", "data1data2", new String(IOUtilities.readData((Reader) list.nextValue())));
        assertEquals("The second input stream does not contain the expected data", "data3data4", new String(IOUtilities.readData((Reader) list.nextValue())));
        assertEquals("Null should be returned after the input streams have finished.", null, list.nextValue());
        block = mInput.read();
        assertTrue("The first InputStreamList block was expected", block instanceof ListIterator);
        list = (ListIterator) block;
        assertEquals("The first input stream does not contain the expected data", "data5data6", new String(IOUtilities.readData((Reader) list.nextValue())));
        assertEquals("The second input stream does not contain the expected data", "data7data8", new String(IOUtilities.readData((Reader) list.nextValue())));
        assertEquals("Null should be returned after the input streams have finished.", null, list.nextValue());
        assertEquals("NO_MORE_DATA should be returned after the input streams lists.", ControlBlock.NO_MORE_DATA, mInput.read());
    }
}
