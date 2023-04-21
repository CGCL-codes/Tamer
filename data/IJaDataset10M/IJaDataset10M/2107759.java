package uk.org.ogsadai.converters.tuple;

import java.util.NoSuchElementException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * A class to convert the contents of <code>uk.org.ogsadai.tuple.Tuple</code>
 * objects into blocks of data.
 * 
 * @author The OGSA-DAI Project Team
 * @see TupleHandler
 */
public class TupleConverter {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007.";

    /** The result set that is being converted into blocks. */
    private final BlockReader mTupleInput;

    /** The handler for converting different parts of the result set. */
    private final TupleHandler mHandler;

    /** The first block of the tuple (metadata) */
    private final Object mFirstBlock;

    /** The string buffer that holds the next block of data to be returned. */
    private StringBuffer mBlock;

    /** Flag used to track whether the header has been handled. */
    private boolean mHeader = true;

    /** Flag used to track whether the footer has been handled. */
    private boolean mFooter = true;

    /** the tuple metadata */
    private TupleMetadata mMetadata;

    /**
     * Creates a new <code>ResultSetConverter</code>.
     * 
     * @param input
     *            input stream which produces the tuples to convert
     * @param firstBlock 
     *            the first block from the input
     * @param handler
     *            handler to use for converting the result set
     * @throws IllegalArgumentException if <code>results</code> or
     * <code>handler</code> are <code>null</code>.
     */
    public TupleConverter(final BlockReader input, final Object firstBlock, final TupleHandler handler) {
        if (input == null) {
            throw new IllegalArgumentException("results must not be null");
        }
        if (firstBlock == null) {
            throw new IllegalArgumentException("first block must not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler must not be null");
        }
        mTupleInput = input;
        mFirstBlock = firstBlock;
        mHandler = handler;
    }

    /**
     * Indicates whether or not there are any more blocks of data to be read.
     * 
     * @return <code>true</code> if there are, <code>false</code>
     * otherwise. 
     * @throws TupleHandlerException
     *     If a problem occurs when accessing the ResultSet.
     */
    public boolean hasNext() throws TupleHandlerException {
        if (mHeader) {
            processNextBlock();
        }
        return mBlock != null;
    }

    /**
     * Gets the next block of converted data.
     * 
     * @return a <code>String</code> representing data
     * @throws NoSuchElementException
     *     If there are no more blocks of data.
     * @throws TupleHandlerException
     *     If a problem occurs when accessing the ResultSet.
     */
    public String next() throws TupleHandlerException {
        if (hasNext()) {
            final StringBuffer block = mBlock;
            processNextBlock();
            return block.toString();
        } else throw new NoSuchElementException();
    }

    /**
     * Generates the next block of data from the result set and saves a
     * reference to it using <code>mBlock</code>. If there are no more blocks
     * then the reference is set to <code>null</code>.
     * 
     * @throws TupleHandlerException
     *     If a problem occurs when accessing the ResultSet.
     */
    private void processNextBlock() throws TupleHandlerException {
        final StringBuffer strbuf = createStringBuffer();
        Tuple tuple = null;
        try {
            if (!mHeader) {
                Object block = mTupleInput.read();
                if (block == ControlBlock.NO_MORE_DATA) {
                    tuple = null;
                } else {
                    tuple = (Tuple) block;
                }
            }
        } catch (DataError e) {
            throw new TupleHandlerException(e);
        } catch (PipeIOException e) {
            throw new TupleHandlerException(e);
        } catch (PipeTerminatedException e) {
            throw new TupleHandlerException(e);
        }
        if (mHeader) {
            mHeader = false;
            mMetadata = (TupleMetadata) mFirstBlock;
            mHandler.header(strbuf, mMetadata);
            mBlock = strbuf;
        } else if (mFooter && tuple != null) {
            processRow(strbuf, tuple, mMetadata);
            mBlock = strbuf;
        } else if (mFooter) {
            mFooter = false;
            mHandler.footer(strbuf);
            mBlock = strbuf;
        } else {
            mBlock = null;
        }
    }

    /**
     * Processes a tuple, adding the converted data to the string buffer.
     * 
     * @param strbuf
     *            buffer to add data to
     * @param tuple
     *            the current tuple
     * @param metadata
     *            metadata tuple
     * 
     * @throws TupleHandlerException
     *             If a problem occurs when accessing the ResultSet or writing
     *             data to the byte stream.
     */
    private void processRow(final StringBuffer strbuf, final Tuple tuple, final TupleMetadata metadata) throws TupleHandlerException {
        mHandler.rowStart(strbuf);
        final int columns = metadata.getColumnCount();
        for (int column = 0; column < columns; column++) {
            mHandler.columnStart(strbuf);
            mHandler.field(strbuf, tuple, metadata, column);
            mHandler.columnEnd(strbuf);
        }
        mHandler.rowEnd(strbuf);
    }

    /**
     * Creates and returns a string buffer used for storing
     * the string representation of the next block of data. This method has protected
     * accessibility so that it can be overridden for testing
     * purposes.
     * 
     * @return a byte stream
     */
    protected StringBuffer createStringBuffer() {
        return new StringBuffer();
    }
}
