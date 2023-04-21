package uk.org.ogsadai.activity.block;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityOutputsException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.DAICloneNotSupportedException;
import uk.org.ogsadai.exception.MalformedListBeginException;
import uk.org.ogsadai.exception.MalformedListEndException;

/**
 * This activity accepts any block as its input and sends the same block
 * reference or a clone of the block to all its outputs. 
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>input</code>. Type: any data type.
 * The data input that will be written to all the outputs. This is a mandatory input. 
 * </li>
 * <li><code>clone</code>. Type: {@link java.lang.Boolean}. It specifies whether
 * the same block reference or a clone of the block will be written to all the outputs.
 * This is an optional
 * input, if omitted then the default value is <code>false</code>.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li><code>output</code>. Type: any data type. The values
 * read from the <code>input</code> input either as references
 * or clone objects. 
 * Two or more outputs must be provided. </li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li> None </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>One block from input <code>clone</code> is consumed (if provided) 
 * before reading from the <code>input</code> input.
 * </li>
 * </ul>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li> None </li>
 * </ul>
 * <p>
 * Target data resource:
 * </p>
 * <ul>
 * <li> None </li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>This activity accepts any block as its input and sends the same block
 * reference or a clone of the block to all its outputs. By block it is meant
 * a logical block (i.e a single-valued block, or an OGSA-DAI list of blocks
 * or an OGSA-DAI list of list of blocks and so on.)
 * </li>
 * <li>
 * List markers are not cloned even if clone value is
 * set to true.
 * </li>
 * <li>If cloning is switched on and an input value is not cloneable an error is 
 * raised.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class TeeActivity extends IterativeActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(TeeActivity.class);

    /**
     * Activity input name (<code>input</code>) - data to be written
     * to all outputs. (any data type).
     */
    public static final String INPUT_DATA = "input";

    /**
     * Activity input name (<code>clone</code>) - whether object
     * references or cloned objects will be produced. ({@link
     * java.lang.Boolean}). 
     */
    public static final String INPUT_CLONE = "clone";

    /**
     * Activity output name (<code>output</code>) - Produced data
     * (any data type).
     */
    public static final String OUTPUT_STREAMS = "output";

    /** Block reader for data input. */
    private BlockReader mInputData;

    /** Block reader for clone input (optional - default value false). */
    private ActivityInput mCloneInput;

    /** The list of output block writers. */
    private List mOutputs;

    /** Current value of the clone input. */
    private boolean mClone;

    /** Current list depth on the data input .*/
    private int mListDepth;

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateInput(INPUT_DATA);
        mInputData = getInput(INPUT_DATA);
        mCloneInput = new TypedOptionalActivityInput(INPUT_CLONE, Boolean.class, Boolean.FALSE);
        if (hasInput(INPUT_CLONE)) {
            mCloneInput.setBlockReader(getInput(INPUT_CLONE));
        }
        mOutputs = getOutputs(OUTPUT_STREAMS);
        if (mOutputs.size() < 2) {
            throw new InvalidActivityOutputsException(2, OUTPUT_STREAMS);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration() throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        try {
            Object block = mInputData.read();
            if (block == ControlBlock.NO_MORE_DATA) {
                raiseErrorIfCloneHasMoreData();
                iterativeStageComplete();
            } else if (block == ControlBlock.LIST_BEGIN) {
                if (mListDepth == 0) {
                    mClone = getNextCloneInput();
                }
                mListDepth++;
                writeToOutputs(block, false);
            } else if (block == ControlBlock.LIST_END) {
                if (mListDepth <= 0) {
                    throw new MalformedListEndException(INPUT_DATA);
                } else {
                    mListDepth--;
                    writeToOutputs(block, false);
                }
            } else if (mListDepth >= 1) {
                writeToOutputs(block, mClone);
            } else if (mListDepth == 0) {
                mClone = getNextCloneInput();
                writeToOutputs(block, mClone);
            }
        } catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        } catch (DAICloneNotSupportedException e) {
            throw new ActivityUserException(e);
        } catch (MalformedListEndException e) {
            throw new ActivityUserException(e);
        } catch (PipeClosedException e) {
            iterativeStageComplete();
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        if (mListDepth > 0) {
            throw new ActivityUserException(new MalformedListBeginException(INPUT_DATA));
        }
        if (mListDepth < 0) {
            throw new ActivityUserException(new MalformedListEndException(INPUT_DATA));
        }
    }

    /**
     * Tries to clone an object. If cloning is not supported by the object an
     * exception is raised.
     * 
     * @param sourceObject
     *            The object to be cloned
     * @return A clone of the <code>sourceObject</code>
     * @throws DAICloneNotSupportedException
     *             if cloning is not supported
     */
    private Object cloneObject(final Object sourceObject) throws DAICloneNotSupportedException {
        Class objectClass = sourceObject.getClass();
        try {
            Method method = objectClass.getMethod("clone", (Class[]) null);
            Object cloned = method.invoke(sourceObject, (Object[]) null);
            return cloned;
        } catch (Exception e) {
            throw new DAICloneNotSupportedException(objectClass.getName(), e);
        }
    }

    /**
     * Returns the next value from the clone input as a boolean.
     * 
     * @return the next value from the clone input
     * @throws ActivityTerminatedException
     *             if the pipe was terminated
     * @throws ActivityProcessingException
     *             if there was a processing error
     * @throws ActivityUserException
     *             if a user exception occurred when reading from the input
     */
    private boolean getNextCloneInput() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        Object block = mCloneInput.read();
        if (block == ControlBlock.NO_MORE_DATA) {
            throw new UnmatchedActivityInputsException(new String[] { INPUT_DATA, INPUT_CLONE }, new Object[] { "data", block });
        } else {
            return ((Boolean) block).booleanValue();
        }
    }

    /**
     * Writes a block to all outputs. The block is cloned before writing if
     * <code>clone</code> is <code>true</code>.
     * 
     * @param block
     *            block to write to the outputs
     * @param clone
     *            indicates whether the block is cloned before writing
     * @throws DAICloneNotSupportedException
     *             if <code>block</code> doesn't support cloning
     * @throws PipeClosedException
     *             if a pipe is closed
     * @throws PipeIOException
     *             if there was a problem writing to a pipe
     * @throws PipeTerminatedException
     *             if a pipe has been terminated
     */
    private void writeToOutputs(final Object block, final boolean clone) throws DAICloneNotSupportedException, PipeClosedException, PipeIOException, PipeTerminatedException {
        Iterator it = mOutputs.iterator();
        while (it.hasNext()) {
            BlockWriter output = (BlockWriter) it.next();
            Object outputBlock;
            if (clone) {
                outputBlock = cloneObject(block);
            } else {
                outputBlock = block;
            }
            output.write(outputBlock);
        }
    }

    /**
     * Checks that the clone input contains no more data.
     * 
     * @throws ActivityUserException
     *             if a user exception was raised when trying to read from the
     *             clone input
     * @throws ActivityProcessingException
     *             if a internal error occurred at the input
     * @throws ActivityTerminatedException
     *             if the pipe was terminated
     */
    private void raiseErrorIfCloneHasMoreData() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        if (!mCloneInput.isInfinite()) {
            Object block = mCloneInput.read();
            if (block != ControlBlock.NO_MORE_DATA) {
                throw new UnmatchedActivityInputsException(new String[] { INPUT_DATA, INPUT_CLONE }, new Object[] { ControlBlock.NO_MORE_DATA, block });
            }
        }
    }
}
