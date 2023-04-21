package uk.org.ogsadai.activity;

import junit.framework.Assert;
import uk.org.ogsadai.activity.io.Pipe;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;

/**
 * A mock output object to assist with unit testing activities.
 * 
 * @author The OGSA-DAI Project Team
 */
public class SimpleMockOutputPipe implements Pipe {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Number of the expected blocks of data. */
    private long mExpectedBlocks;

    /** Number of the actual blocks of data. */
    private long mActualBlocks;

    /** Indicates whether or not the pipe has been closed for reading. */
    private boolean mClosedForReading;

    /** Indicates whether or not the output has been closed. */
    private boolean mClosedForWriting;

    /** Pipe instance name. */
    private String mInstanceName;

    /** Counter for auto-generating instance names. */
    private static int mCounter = 1;

    /**
     * Constructs a mock output that expects the specified number of blocks
     * to be put into it. The test will fail immediately the output receives an
     * unexpected block of data.
     * 
     * @param numberOfBlocks expected number of blocks.
     */
    public SimpleMockOutputPipe(final long numberOfBlocks) {
        mInstanceName = "MockInputPipe" + mCounter++;
        mExpectedBlocks = numberOfBlocks;
    }

    /**
     * {@inheritDoc}
     */
    public void write(Object data) throws PipeClosedException {
        if (mClosedForReading) {
            throw new PipeClosedException();
        }
        mActualBlocks++;
        Assert.assertTrue("More blocks of output data have been produced than was expected.", mActualBlocks <= mExpectedBlocks);
    }

    /**
     * {@inheritDoc}
     */
    public void closeForWriting() {
        if (mClosedForWriting) {
            throw new IllegalStateException("The input has already been closed.");
        }
        mClosedForWriting = true;
    }

    /**
     * {@inheritDoc}
     */
    public void closeForWritingDueToError() {
        if (mClosedForWriting) {
            throw new IllegalStateException("The input has already been closed.");
        }
        mClosedForWriting = true;
    }

    /**
     * Verifies that the expected output data has been put into the output and 
     * that the output has been closed. If not, the test will fail.
     */
    public void verify() {
        Assert.assertEquals("The expected number of output blocks have not been produced.", mExpectedBlocks, mActualBlocks);
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
    public void closeForReading() {
        mClosedForReading = true;
    }

    /**
     * {@inheritDoc}
     */
    public int getNumBlocksReadable() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public Object peek() throws PipeIOException, PipeTerminatedException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public Object read() throws PipeIOException, PipeTerminatedException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean couldWriteBlock() {
        throw new UnsupportedOperationException();
    }
}
