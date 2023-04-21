package uk.org.ogsadai.activity.io;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;

/**
 * The interface for reading data from a pipe. The object reading the data is
 * refered to as the <em>consumer</em>.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface InputBlockReader {

    /**
     * Reads a block of data from the pipe. This operation blocks until a block
     * of data is available or the pipe is closed.
     * 
     * @return the data block or <code>ControlBlock.NO_MORE_DATA</code> if the
     *         pipe has been closed and there is no more data to read
     *
     * @throws ActivityUserException
     *      if an error occurs which is user related
     * @throws ActivityProcessingException
     *      if an error occurs during the processing of the reading
     * @throws ActivityTerminatedException
     *      if an error occurs due to unexpected termination
     */
    public Object read() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException;

    /**
     * Invoked by the consumer of data from the pipe to indicate that no more 
     * data is required. This closes the pipe for consumption and any remaining 
     * data within the pipe should be discarded.
     */
    public void closeForReading();
}
