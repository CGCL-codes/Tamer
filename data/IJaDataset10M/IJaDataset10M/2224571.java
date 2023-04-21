package uk.org.ogsadai.client.toolkit.activities.transform;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.StringData;

/**
 * This activity accepts an OGSA-DAI list as input and splits this list into a
 * sequence of smaller lists of a specified length.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>input</code>. Type: OGSA-DAI list with optionally the first item in the
 * list an instance of {@link uk.org.ogsadai.metadata.MetadataWrapper}. This is
 * the data set which will be split into smaller lists. This is a mandatory
 * input.</li>
 * <li>
 * <code>size</code>. Type: {@link java.lang.Number}. The length of the output
 * lists. The integer value must be a greater than zero. This is a mandatory
 * input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>output</code>. Type: OGSA-DAI list with the first item in the list
 * an instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} if the input
 * list contained metadata. The produced output lists have length at most
 * <code>size</code>.</li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: <code>size</code> is read before a list from
 * <code>input</code> is streamed through.
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
 * <li>This activity reads an OGSA-DAI list and splits it into a sequence of
 * lists of length <code>size</code>.</li>
 * <li>If the list contains metadata it is copied to each of the resulting
 * lists.</li>
 * <li>The last list may contain less than <code>size</code> elements.
 * <li>
 * For example, (in all the examples we use '<code>{</code>' to denote the list
 * begin marker and '<code>}</code>' to denote the list end marker):
 * 
 * <pre>
 * input: { metadata "A" "B" "C" "D" "E" "F" "G" "H" "I" "J" }
 * size: 4
 * output: { metadata "A" "B" "C" "D" } { metadata "E" "F" "G" "H" } { metadata "I" "J" }
 * </pre>
 * 
 * <pre>
 * input: { 1 2 3 4 5 6 7 8 9 10 11 }
 * size: 3
 * output: { 1 2 3 } { 4 5 6 } { 7 8 9 } { 10 11 }
 * </pre>
 * 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListSplit extends BaseActivity {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2011.";

    /** Default activity name */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.ListSplit");

    /** Activity input name for the output list size. */
    public static final String INPUT_SIZE = "size";

    /** Activity input name for the data input. */
    public static final String INPUT_DATA = "input";

    /** Name of the activity output. */
    public static final String OUTPUT = "output";

    /** List size input */
    private final ActivityInput mSizeInput;

    /** Data input */
    private final ActivityInput mDataInput;

    /** Data output */
    private final ActivityOutput mOutput;

    /**
     * Constructor.
     */
    public ListSplit() {
        super(DEFAULT_ACTIVITY_NAME);
        mSizeInput = new SimpleActivityInput(INPUT_SIZE);
        mDataInput = new SimpleActivityInput(INPUT_DATA);
        mOutput = new SimpleActivityOutput(OUTPUT);
    }

    /**
     * Adds a new list size.
     * 
     * @param size Size of the output lists.
     */
    public void addListSize(final String size) {
        mSizeInput.add(new StringData(size));
    }

    /**
     * Connects the list size input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectSizeInput(final SingleActivityOutput output) {
        mSizeInput.connect(output);
    }

    /**
     * Connects the data input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectDataInput(final SingleActivityOutput output) {
        mDataInput.connect(output);
    }

    /**
     * Gets the output.
     * 
     * @return data output.
     */
    public SingleActivityOutput getOutput() {
        return mOutput.getSingleActivityOutputs()[0];
    }

    protected void validateIOState() throws ActivityIOIllegalStateException {
    }

    protected ActivityInput[] getInputs() {
        return new ActivityInput[] { mSizeInput, mDataInput };
    }

    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mOutput };
    }
}
