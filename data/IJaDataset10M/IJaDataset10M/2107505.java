package uk.org.ogsadai.client.toolkit;

import java.util.Date;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.DataValue;

/**
 * Data iterator wrapper that goes around a normal data value iterator and 
 * throws an exception when there is no more data.  This is useful when we have
 * a data stream that is know to end with in error and we have an iterator that
 * gives access to the data produced prior to the error.
 *
 * @author The OGSA-DAI Project Team
 */
public class StreamErrorDataValueIterator implements DataValueIterator {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2007.";

    /** DataValueIterator being wrapped */
    private DataValueIterator mIterator;

    /**
     * Constructor.  The iterator constructed act like the give iterator but
     * will throw a <code>DataStreamErrorException</code> when the given 
     * iterator's data runs out.
     * 
     * @param iterator iterator to wrap.
     */
    public StreamErrorDataValueIterator(final DataValueIterator iterator) {
        mIterator = iterator;
    }

    public boolean hasNext() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        if (mIterator.hasNext()) {
            return true;
        } else {
            throw new DataStreamErrorException();
        }
    }

    public DataValue next() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.next();
    }

    public boolean nextAsBoolean() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsBoolean();
    }

    public byte[] nextAsByteArray() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsByteArray();
    }

    public char[] nextAsCharArray() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsCharArray();
    }

    public Date nextAsDate() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsDate();
    }

    public double nextAsDouble() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsDouble();
    }

    public float nextAsFloat() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsFloat();
    }

    public int nextAsInt() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsInt();
    }

    public long nextAsLong() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsLong();
    }

    public String nextAsString() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        hasNext();
        return mIterator.nextAsString();
    }
}
