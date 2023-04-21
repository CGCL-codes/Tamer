package uk.org.ogsadai.client.toolkit.activities.util;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.BooleanData;
import uk.org.ogsadai.data.IntegerData;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.file.CounterActivity</code>.
 * This activity outputs integers
 * from 1 up to a specified maximum.  After the final integer has been output
 * the stream can either be terminated normally or with an error.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>max</code>. Type: {@link java.lang.Integer}.
 * The highest integer up to which all the intermediate integers will be written to the
 * output. This is a mandatory input.
 * </li>
 * <li> <code>endWithError</code>. Type: {@link java.lang.Boolean}.
 * Whether the activity will terminate by throwing an error or not. This is an optional input which
 * defaults to <code>false</code>.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li><code>output</code>. Type: <code>{@link java.lang.Integer}</code>.
 * All the integer values between 1 and the value provided by <code>max</code> input.
 * </li>
 * </ul>
 * <p>
 * Expected name on server: 
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.Counter
 * </code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>This activity outputs integers
 * from 1 up to a specified maximum.  After the final integer has been output
 * the stream can either be terminated normally or with an error.
 * </li>
 * <li>This activity is primarily used for testing purposes.
 * </li>
 * <li>This activity also writes a warning to the request status for each integer
 * written to the output. This allows the system tests to test the warning
 * functionality.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class Counter extends BaseActivity implements Activity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.Counter");

    /** Counter maximum. */
    private SimpleActivityInput mMaxInput;

    /** Optional end with error flag. */
    private SimpleActivityInput mEndWithErrorInput;

    /** Output. */
    private SimpleActivityOutput mOutput;

    /**
     * Constructor.
     */
    public Counter() {
        super(DEFAULT_ACTIVITY_NAME);
        mMaxInput = new SimpleActivityInput("max");
        mEndWithErrorInput = new SimpleActivityInput("endWithError", SimpleActivityInput.OPTIONAL);
        mOutput = new SimpleActivityOutput("output");
    }

    /**
     * Connects the maximum input to the output of another activity.
     * 
     * @param output output to connect to.
     */
    public void connectMaxInput(final SingleActivityOutput output) {
        mMaxInput.connect(output);
    }

    /**
     * Add a new maximum value the counter should count to.
     * 
     * @param maxCount maximum value the counter should count to.
     */
    public void addMax(int maxCount) {
        mMaxInput.add(new IntegerData(maxCount));
    }

    /**
     * Connects the 'end with error' input to the output of another activity.
     * 
     * @param output output to connect to.
     */
    public void connectEndWithErrorInput(final SingleActivityOutput output) {
        mEndWithErrorInput.connect(output);
    }

    /**
     * Add a boolean flag to say whether the count should end on an error.
     * 
     * @param endOnError <code>true</code> if the count should end on a error,
     *                   <code>false</code> otherwise.
     */
    public void addEndOnError(boolean endOnError) {
        mEndWithErrorInput.add(new BooleanData(endOnError));
    }

    /**
     * Gets the output.
     * 
     * @return output.
     */
    public SingleActivityOutput getOutput() {
        return mOutput.getSingleActivityOutputs()[0];
    }

    /**
     * Determines whether there is any more data on the output.
     * 
     * @return <code>true</code> if there is more data, <code>false</code> 
     *         otherwise.
     *         
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     */
    public boolean hasNextOutput() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        return mOutput.getDataValueIterator().hasNext();
    }

    /**
     * Gets the next output value.
     * 
     * @return next output value.
     * 
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     */
    public int nextOutput() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        return mOutput.getDataValueIterator().nextAsInt();
    }

    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException {
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs() {
        return new ActivityInput[] { mMaxInput, mEndWithErrorInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mOutput };
    }
}
