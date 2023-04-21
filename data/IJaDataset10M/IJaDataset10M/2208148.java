package uk.org.ogsadai.client.toolkit.activities.block;

import java.util.Calendar;
import java.util.Date;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.BinaryData;
import uk.org.ogsadai.data.BooleanData;
import uk.org.ogsadai.data.CharData;
import uk.org.ogsadai.data.DateData;
import uk.org.ogsadai.data.DoubleData;
import uk.org.ogsadai.data.FloatData;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.LongData;
import uk.org.ogsadai.data.StringData;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.block.ListRandomSplitActivity</code>. This
 * activity splits an input stream into N random partitions. The activity
 * operates at a specified level of granularity which must be greater than zero.
 * The partitions are not necessarily of the same size. A random seed may be
 * provided so that a particular partitioning can be reproduced.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list (can be nested) of any data type.
 * The data input that will be randomly split. This is a mandatory input. 
 * The OGSA-DAI list must be of at least the granularity provided by the <code>granularity</code> input.
 * </li>
 * <li><code>seed</code>. Type: {@link java.lang.Long}. It specifies a seed for
 * the random generator so that a particular partition can be reproduced.
 * This is an optional
 * input, if omitted then the default value is <code>null</code>.
 * </li>
 * <li><code>granularity</code>. Type: {@link java.lang.Integer}</li>.
 * It specifies the granularity level at which the activity operates.
 * This is an optional input, if omitted then the default value is 1(i.e.
 * an OGSA-DAI list).
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li><code>data</code>. Type: This is exactly the same type as the input <code>data</code>.
 * The data read from the <code>data</code> input split across a number of outputs.
 * Two or more outputs must be provided. </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.ListRandomSplit</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>One block each from input <code>seed</code> and from input
 * <code>granularity</code> is consumed (if provided) before the data.</li>
 * </ul>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>The server-side activity reads a logical block from the data input and
 * randomly sends it to one of the data outputs. It operates on the elements
 * inside a list, at the specified granularity.</li>
 * <li>If a metadata block is encountered, preceding the data blocks, then the
 * metadata is sent to each output. For example 
 * (note that on all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker), 
 * if the input is<br>
 * &nbsp;&nbsp;<code>{ metadata data1 data2 data3 data4 }</code><br> 
 * a random split (at granularity 1) 
 * with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ metadata data1 data4 }</code><br>
 * on output 1 and<br>
 * &nbsp;&nbsp;<code>{ metadata data2 data3 }</code><br> on output 2.
 * <li>For example, a random split of<br>
 * &nbsp;&nbsp;<code>{ 1 2 3 } { A B C D E }</code><br>
 * at granularity 1 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ 1 2 } { B C E }</code><br>
 * on output 1 and<br>
 * &nbsp;&nbsp;<code>{3 } { A D }</code> on output 2.
 * <li>Another example, a random split of<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } } { { A } { B } { C } { D } { E } }</code><br>
 * at granularity 1 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } } { { B } { C } { E } }</code><br>
 * on output 1 and <br>
 * &nbsp;&nbsp;<code>{ { 3 6 } } { { A } { D } }</code><br>
 * on output 2.
 * </li>
 * <li>Another example, a random split of<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } } { { A } { B } { C } { D } { E } }</code><br>
 *  at granularity 0 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } }</code><br>
 * on output 1 and<br> 
 * &nbsp;&nbsp;<code>{ { A } { B } { C } { D } { E } }</code><br>
 * on output 2.
 * </li>
 * <li>Another example, a random split of<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } } { { A } { B } { C } { D } { E } }</code><br>
 * at granularity 2 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ { 1 } { 2 } { 3 } } { { } { B } { C } { } { E } }</code><br>
 * on output 1 and<br>
 * &nbsp;&nbsp;<code>{ { 4 } { 5 } { 6 } } { { A } { } { } { D } { } }</code><br>
 * on output 2.
 * </li>
 * <li>A random seed can be specified to reproduce a particular partition of
 * the input data set.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListRandomSplit extends BaseActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Default activity name. */
    public static final ActivityName DEFAULT_NAME = new ActivityName("uk.org.ogsadai.ListRandomSplit");

    /**
     * Activity input name (<code>data</code>) - Data to be split
     * (OGSA-DAI list of any data type).
     */
    private static final String INPUT_DATA = "data";

    /**
     * Activity input name (<code>seed</code>) - The seed
     * (<code>Long</code>).
     */
    private static final String INPUT_RANDOM_SEED = "seed";

    /**
     * Activity input name (<code>granularity</code>) - The granularity level 
     * to apply the partioning to (<code>Integer</code>).
     */
    private static final String INPUT_GRANULARITY = "granularity";

    /**
     * Activity output name (<code>data</code>) - Produced data
     * (OGSA-DAI list of any data type).
     */
    private static final String OUTPUT_DATA = "data";

    /** Data input. */
    private ActivityInput mDataInput;

    /** Granularity input. */
    private ActivityInput mGranularityInput;

    /** Random seed input. */
    private ActivityInput mRandomSeedInput;

    /** Data output. */
    private ActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public ListRandomSplit() {
        this(DEFAULT_NAME);
    }

    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName the activity name
     */
    public ListRandomSplit(ActivityName activityName) {
        super(activityName);
        mDataInput = new SimpleActivityInput(INPUT_DATA);
        mGranularityInput = new SimpleActivityInput(INPUT_GRANULARITY, true);
        mRandomSeedInput = new SimpleActivityInput(INPUT_RANDOM_SEED, true);
        mOutput = new SimpleActivityOutput(OUTPUT_DATA);
    }

    /**
     * Sets the set of output occurences.
     * 
     * @param count
     *            the count of output occurences
     */
    public void setNumberOfOutputs(int count) {
        mOutput.setNumberOfOutputs(count);
    }

    /**
     * Adds a {@link java.lang.String} to the data input.
     * 
     * @param input
     *            String value to add to input.
     */
    public void addInput(final String input) {
        mDataInput.add(new StringData(input));
    }

    /**
     * Adds a char array to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final char[] input) {
        mDataInput.add(new CharData(input));
    }

    /**
     * Adds a byte array to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final byte[] input) {
        mDataInput.add(new BinaryData(input));
    }

    /**
     * Adds a {@link java.lang.Float} to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final Float input) {
        addInput(input.floatValue());
    }

    /**
     * Adds a float to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final float input) {
        mDataInput.add(new FloatData(input));
    }

    /**
     * Adds a {@link java.lang.Double} to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final Double input) {
        addInput(input.doubleValue());
    }

    /**
     * Adds a double to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final double input) {
        mDataInput.add(new DoubleData(input));
    }

    /**
     * Adds an {@link java.lang.Integer} to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final Integer input) {
        addInput(input.intValue());
    }

    /**
     * Adds an int to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final int input) {
        mDataInput.add(new IntegerData(input));
    }

    /**
     * Adds a {@link java.lang.Long} to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final Long input) {
        addInput(input.longValue());
    }

    /**
     * Adds a long to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final long input) {
        mDataInput.add(new LongData(input));
    }

    /**
     * Adds a {@link java.lang.Boolean} to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final Boolean input) {
        addInput(input.booleanValue());
    }

    /**
     * Adds a boolean to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final boolean input) {
        mDataInput.add(new BooleanData(input));
    }

    /**
     * Adds a {@link java.util.Date} to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final Date input) {
        mDataInput.add(new DateData(input));
    }

    /**
     * Adds a {@link java.sql.Time} to the data input.
     * 
     * @param input
     *            the input
     */
    public void addInput(final Calendar input) {
        addInput(input.getTime());
    }

    /**
     * Adds list start marker to the data input.
     */
    public void addListBeginToInput() {
        mDataInput.add(ListBegin.VALUE);
    }

    /**
     * Adds list end marker to the data input.
     * 
     */
    public void addListEndToInput() {
        mDataInput.add(ListEnd.VALUE);
    }

    /**
     * Connects the data input to the given output.
     * 
     * @param output
     */
    public void connectDataInput(SingleActivityOutput output) {
        mDataInput.connect(output);
    }

    /**
     * Adds a granularity at which the random split operates.
     * 
     * @param granularity granularity 
     */
    public void addGranularity(int granularity) {
        mGranularityInput.add(new IntegerData(granularity));
    }

    /**
     * Connects the granularity input to the given output.
     * 
     * @param output
     */
    public void connectGranularityInput(SingleActivityOutput output) {
        mGranularityInput.connect(output);
    }

    /**
     * Adds a random seed to configure the random generator.
     * 
     * @param seed
     */
    public void addRandomSeed(long seed) {
        mRandomSeedInput.add(new LongData(seed));
    }

    /**
     * Connects the seed input to the given output.
     * 
     * @param output
     */
    public void connectRandomSeedInput(SingleActivityOutput output) {
        mRandomSeedInput.connect(output);
    }

    /**
     * Returns the data output at the index.
     * 
     * @param index
     *            index of the output
     * @return data output
     */
    public SingleActivityOutput getOutput(int index) {
        return mOutput.getSingleActivityOutputs()[index];
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs() {
        return new ActivityInput[] { mDataInput, mGranularityInput, mRandomSeedInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mOutput };
    }

    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException {
    }
}
