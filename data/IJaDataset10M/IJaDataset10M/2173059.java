package uk.org.ogsadai.activity.delivery;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.Pipe;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import junit.framework.Assert;

/**
 * A mock input object to assist with unit testing activities. It takes an array
 * of objects. Each object can be either a ControlBlock or an InputStream. If 
 * there is a <code>ControlBlock</code> at the current position, then a call to 
 * <code>read()</code> returns this block as it is. If there is an InputStream
 * at the current position instead, then an array of bytes is read from the 
 * input stream and returned to the caller. The maximum size of this array is 
 * BUFFER_SIZE. As long as end of stream has not been reached, subsequent calls 
 * to <code>read</code> will keep returning data from this stream.
 * 
 * @author The OGSA-DAI Project Team
 */
public class MockInputPipeStream implements Pipe {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    /** Buffer size. */
    private static final int BUFFER_SIZE = 1000;

    /** A list of the blocks of input data. List&lt;Object&gt; */
    private final List mData = new ArrayList();

    /** Indicates whether or not the input has been closed. */
    private boolean mClosed;

    /** Pipe instance name. */
    private String mInstanceName;

    /** Counter for auto-generating instance names. */
    private static int mCounter = 1;

    /** Flag indicating that the read method should raise an exception. */
    private boolean mRaiseIOException;

    /** Flag indicating that the read method should raise an exception. */
    private boolean mRaiseTerminatedException;

    /** Array used to read the data. */
    private byte[] mBytes;

    /**
     * Create a mock input containing a single block of input data.
     * 
     * @param data
     *            the block of data
     */
    public MockInputPipeStream(final Object data) {
        mInstanceName = "MockInputPipe" + mCounter++;
        mData.add(data);
    }

    /**
     * Create a mock input containing a sequence of blocks of data.
     * 
     * @param data
     *            an array of blocks of data. The blocks will become available
     *            from the input in array-index order.
     */
    public MockInputPipeStream(final Object[] data) {
        mData.addAll(Arrays.asList(data));
        mBytes = new byte[BUFFER_SIZE];
    }

    /**
    * {@inheritDoc}
    */
    public int getNumBlocksReadable() {
        int size = mData.size();
        if (size == 0) {
            return 1;
        }
        return size;
    }

    /**
    * {@inheritDoc}
    */
    public Object peek() throws PipeIOException, PipeTerminatedException {
        if (mRaiseIOException) {
            throw new PipeIOException();
        } else if (mRaiseTerminatedException) {
            throw new PipeTerminatedException("MockPipe");
        }
        if (mData.size() == 0) {
            return ControlBlock.NO_MORE_DATA;
        }
        return mData.get(0);
    }

    /**
    * {@inheritDoc}
    */
    public Object read() throws PipeIOException, PipeTerminatedException {
        if (mRaiseIOException) {
            throw new PipeIOException();
        } else if (mRaiseTerminatedException) {
            throw new PipeTerminatedException("MockPipe");
        }
        while (true) {
            if (mData.size() == 0) {
                return ControlBlock.NO_MORE_DATA;
            }
            if (mData.get(0) instanceof ControlBlock) {
                return mData.remove(0);
            }
            try {
                int bytesRead = ((InputStream) mData.get(0)).read(mBytes);
                if (bytesRead == -1) {
                    mData.remove(0);
                    continue;
                } else {
                    byte[] result = new byte[bytesRead];
                    System.arraycopy(mBytes, 0, result, 0, bytesRead);
                    return result;
                }
            } catch (IOException e) {
                throw new PipeIOException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void closeForReading() {
        if (mClosed) {
            throw new IllegalStateException("The input has already been closed.");
        }
        mClosed = true;
    }

    /**
     * Verifies that all the input data has been consumed and the input has been
     * closed. If not, the test will fail.
     */
    public void verify() {
        Assert.assertEquals("The input still contains blocks of input data.", 0, mData.size());
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return mInstanceName;
    }

    /**
     * {@inheritDoc}
     */
    public void closeForWriting() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void closeForWritingDueToError() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void write(final Object block) throws PipeClosedException, PipeIOException, PipeTerminatedException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean couldWriteBlock() {
        throw new UnsupportedOperationException("Not implemented in mock class");
    }

    /**
     * Indicate whether or not the <code>read</code> method should raise a
     * <code>PipeIOException</code>.
     * 
     * @param flag
     *            <code>true</code> to raise an exception, <code>false</code>
     *            otherwise
     */
    public void raisePipeIOException(final boolean flag) {
        mRaiseIOException = flag;
    }

    /**
     * Indicate whether or not the <code>read</code> method should raise a
     * <code>PipeTerminatedException</code> as if the enclosing request had
     * been terminated.
     * 
     * @param flag
     *            <code>true</code> to raise an exception, <code>false</code>
     *            otherwise
     */
    public void raisePipeTerminatedException(final boolean flag) {
        mRaiseTerminatedException = flag;
    }
}
