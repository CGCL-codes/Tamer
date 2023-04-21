package uk.org.ogsadai.activity.io;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.exception.MalformedListBeginException;

/**
 * Activity input for reading lists of 
 * <code>uk.org.ogsadai.activity.io.InputStream</code> objects.
 * </p><p>
 * The underlying data stream must contain data in one of the following forms:  
 * <pre>[ [ byte[] byte[] ... ] [ byte[] byte[] ... ] ... ] [ [ byte[] byte[] ... ] [ byte[] byte[] ... ] ... ] ...</pre>
 * or
 * <pre>[ BLOB BLOB ... ] [ BLOB BLOB ... ] ...</pre>
 * </p><p>
 * The <code>read()</code> method will return blocks of type
 * <code>uk.org.ogsadai.activity.io.InputStream</code>. It is 
 * <strong>important to note</strong> that these objects will raise
 * OGSA-DAI-specific <code>java.io.IOExceptions</code> to indicate termination,
 * processing errors and user errors as defined in the 
 * <code>uk.org.ogsadai.activity.io.InputStream</code> class.
 * 
 * @author OGSA-DAI team
 */
public class InputStreamListActivityInput implements ActivityInput {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007";

    /** Name of the input. */
    private final String mInputName;

    /** Wrapped block reader */
    private BlockReader mReader;

    /** Flag to track whether the input reader has been closed. */
    private boolean mClosed = false;

    /**
     * Create a new input block reader wrapping the specified reader.
     * 
     * @param inputName
     *            input name
     */
    public InputStreamListActivityInput(final String inputName) {
        mInputName = inputName;
    }

    public Object read() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        if (mClosed) {
            throw new IllegalStateException("The activity input has been closed.");
        }
        try {
            Object block = mReader.read();
            if (block != ControlBlock.NO_MORE_DATA) {
                if (block == ControlBlock.LIST_BEGIN) {
                    block = new InputStreamListIterator(mInputName, mReader);
                } else {
                    throw new ActivityUserException(new MalformedListBeginException(mInputName));
                }
            }
            return block;
        } catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
    }

    public void close() {
        mClosed = true;
        mReader.closeForReading();
    }

    public String getInputName() {
        return mInputName;
    }

    public void setBlockReader(BlockReader reader) {
        mReader = reader;
    }

    public boolean isInfinite() {
        return false;
    }

    public boolean isRequired() {
        return true;
    }
}
