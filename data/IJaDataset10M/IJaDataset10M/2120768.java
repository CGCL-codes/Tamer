package uk.org.ogsadai.activity.util;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.block.ActivityOutOfBoundsException;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;

/**
 * A test activity that echos its input to its output and then optionally 
 * reports an error at the end of the stream, or instead after a specified 
 * number of values in the stream.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>input</code>. Type: {@link java.lang.Object}. Objects to be echoed to
 * the output.
 * </li>
 * <li>
 * <code>errorAt</code>. Type: {@link java.lang.Integer}. Specifies when the
 * activity should report an 
 * {@link uk.org.ogsadai.activity.ActivityUserException}. A value of -2 means
 * no exception should be throw, -1 means throw an exception at the end of
 * the input data stream and any positive integer means throw an exception after
 * outputting that many objects.  This is an optional input.  The default is
 * to not thrown an error.
 * </li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>output</code>. Type: OGSA-DAI list of {@link java.lang.Object}. 
 * The objects received on the <code>input</code> input.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * Echos in input objects to the output and throws an 
 * {@link uk.org.ogsadai.activity.ActivityUserException} when specified to do
 * so my the value of the <code>errorAt</code> input.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class EchoActivity extends IterativeActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007.";

    /** Input name. */
    public static final String INPUT = "input";

    /** Input name. */
    public static final String ERROR_AT = "errorAt";

    /** Output name. */
    public static final String OUTPUT = "output";

    /** This flag is used to signify that the echo activity should not add 
     * any errors. */
    public static final int NEVER = -2;

    /** This flag is used to signify that the echo activity should add an error 
     * at the end of the Data. */
    public static final int END = -1;

    /** Output block writer. */
    private BlockWriter mOutput;

    /** Holds the input value on when to throw an error or not. */
    private Integer mErrorAt = null;

    /** Records the currnet block number. */
    private int mBlockNumber = 0;

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(OUTPUT);
        mOutput = getOutput(OUTPUT);
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration() throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        int errorAt = getErrorAt();
        Object block = null;
        try {
            BlockReader inputReader = getInput(INPUT);
            while ((block = inputReader.read()) != ControlBlock.NO_MORE_DATA) {
                if (mBlockNumber == errorAt) {
                    throwError();
                } else {
                    mOutput.write(block);
                    mBlockNumber++;
                }
            }
            iterativeStageComplete();
        } catch (PipeClosedException e) {
            iterativeStageComplete();
        } catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        if (getErrorAt() == END) {
            throwError();
        }
        mOutput.closeForWriting();
    }

    /**
     * Returns the block number when an error should be thrown.
     * 
     * @return the block number when an error should be thrown
     * 
     * @throws ActivityUserException
     *             if the settings specified by the user prevent processing from
     *             completing
     * @throws ActivityProcessingException
     *             if an internal error prevents processing from completing
     * @throws ActivityTerminatedException
     *             if activity processing is terminated at an intermediate stage
     */
    protected int getErrorAt() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        if (mErrorAt == null) {
            mErrorAt = new Integer(NEVER);
            BlockReader inputErrorReader = getInput(ERROR_AT);
            if (inputErrorReader != null) {
                Object block = null;
                try {
                    if ((block = inputErrorReader.read()) != ControlBlock.NO_MORE_DATA) {
                        mErrorAt = ((Integer) block);
                    }
                } catch (ClassCastException e) {
                    throw new ActivityUserException(e);
                } catch (PipeIOException e) {
                    throw new ActivityPipeProcessingException(e);
                } catch (PipeTerminatedException e) {
                    throw new ActivityTerminatedException();
                }
            }
        }
        return mErrorAt.intValue();
    }

    /**
     * Generate and throw an ActivityUserException. 
     * 
     * @throws ActivityUserException
     *             the activity user exception
     */
    protected void throwError() throws ActivityUserException {
        throw new ActivityOutOfBoundsException(0, 10, 20);
    }
}
