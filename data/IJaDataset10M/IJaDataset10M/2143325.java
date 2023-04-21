package uk.org.ogsadai.client.toolkit.activities.generic;

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
 * GenericTupleJoin is a client toolkit proxy for
 * <code>uk.org.ogsadai.activity.generic.GenericTupleJoin</code>
 * which allows a JavaScript (or other script but this requires additional <a href="https://scripting.dev.java.net/">
 * script engines</a> to be added to the configuration) to be used to perform a join of
 * two tuple streams.
 * <p>
 * Activity Inputs:
 * </p>
 * <ul>
 * <li><code>language</code>: a <code>java.lang.String</code> specifying the 
 * scripting language to support. Currently JavaScript are supported by 
 * Java 1.6R2 out of the box but additional scripting languages may be supported 
 * by adding suitable jars. There are no explicit checks in the code that check for 
 * the language type.</li>
 * <li><code>script</code>: a <code>java.lang.String</code> representation of the 
 * actual script that is to be run.</li>
 * <li><code>data1</code>: Type: OGSA-DAI list of <code>uk.org.ogsadai.tuple.Tuple</code> 
 * with the first item in the list an instance of <code>uk.org.ogsadai.metadata.MetadataWrapper</code>
 * containing a <code>uk.org.ogsadai.tuple.TupleMetadata</code> object. This is the first of
 * the two data streams that are to be merged using the script.</li>
 * <li><code>data2</code>: Type: OGSA-DAI list of <code>uk.org.ogsadai.tuple.Tuple</code> with the 
 * first item in the list an instance of <code>uk.org.ogsadai.metadata.MetadataWrapper</code> 
 * containing a <code>uk.org.ogsadai.tuple.TupleMetadata</code> object. This is the second of the 
 * two data streams that are to be merged using the script.</li>
 * </ul>
 * <p>
 * Activity Outputs:
 * </p>
 * <ul>
 * <li><code>result</code>: Type: OGSA-DAI list of <code>uk.org.ogsadai.tuple.Tuple</code> 
 * with the first item in the list an instance of <code>uk.org.ogsadai.metadata.MetadataWrapper</code> 
 * containing a <code>uk.org.ogsadai.tuple.TupleMetadata</code> object. This is the output produced 
 * by the script of the two data streams that were joined.</li>
 * </ul>
 * <p>
 * Configuration parameters: None.
 * </p>
 * <p>
 * Activity input/output ordering: None.
 * </p>
 * <p>
 * Activity contracts: None.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>
 * <p>Scripts that are executed by the <code>TupleJoinActivity</code> must implement the 
 * following three methods:</p>
 * <p><pre>
 * TupleMetadata getMetadata(TupleMetadata inputMetadata1, TupleMetadata inputMetadata2);
 * List&lt;Tuple&gt; process(Tuple tuple1, Tuple tuple2);
 * List&lt;Tuple&gt; flush();
 * </pre></p>
 * </li>
 * <li>
 * <p>An example ruby script that could be used is:</p>
 * <p><pre>
 * ## Ruby script for generic join of two tuples if the first two columns sound alike.
 * 
 * ## Soundex code taken from: http://snippets.dzone.com/posts/show/4530
 * ## Comments there say it may have a bug and suggest some improvments. These are
 * ## not included and that is not really the point of this demo.
 * 
 * class String
 * 
 *     SoundexChars = 'BPFVCSKGJQXZDTLMNR'
 *     SoundexNums  = '111122222222334556'
 *     SoundexCharsEx = '^' + SoundexChars
 *     SoundexCharsDel = '^A-Z'
 *
 *   # desc: http://en.wikipedia.org/wiki/Soundex
 *   def soundex(census = true)
 *       str = upcase.delete(SoundexCharsDel).squeeze
 *
 *       str[0 .. 0] + str[1 .. -1].
 *           delete(SoundexCharsEx).
 *           tr(SoundexChars, SoundexNums)[0 .. (census ? 2 : -1)].
 *           ljust(3, '0') rescue ''
 *   end
 * end
 *
 *
 * include_class Java::uk.org.ogsadai.activity.generic.TupleHelper;
 * include_class Java::uk.org.ogsadai.tuple.TupleTypes;
 *
 *
 * def getMetadata(inputMetadata1, inputMetadata2)    # An array of multpile things?
 *  # We're doing a join here so we meed a helper to build a new metadata
 *  TupleHelper.createTupleMetadata(inputMetadata1, inputMetadata2);
 * end
 *
 * def process(tuple1, tuple2)
 *  if tuple1.getString(0).soundex == tuple2.getString(0).soundex
 *    tuple = TupleHelper.createTuple(tuple1, tuple2);  ## New tuple helper method
 *    Java::java.util.Arrays.asList([tuple].to_java);   
 *  elsif
 *    Java::java.util.Collections.emptyList;
 *  end
 * end
 *
 * # do we want flush for this?  Ideally not if we would wish to parallelise this.
 * # This parallelization requirement may make it useful to have a separete pattern
 * #for when this is needed.  Need to think about this.  Must look at next grid example
 * #again to see how it works
 *
 * def flush()
 * Java::java.util.Collections.emptyList;
 * end 
 * </pre></p>
 * </li>
 * </ul>
 * 
 * @author ADMIRE Project
 *
 */
public class GenericTupleJoin extends BaseActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2010.";

    /** Default activity name. */
    private static final ActivityName DEFAULT_NAME = new ActivityName("uk.org.ogsadai.GenericTupleJoin");

    /** The scripting language to be used - JavaScript for Java 1.6R2 (other languages will include more configuraiton). */
    public static final String INPUT_LANGUAGE = "language";

    /** The actual script to be run. */
    public static final String INPUT_SCRIPT = "script";

    /** Input data stream to be joined. */
    public static final String INPUT_DATA1 = "data1";

    /** Input data stream to be joined. */
    public static final String INPUT_DATA2 = "data2";

    /** Output stream with the joined output from the two input streams. */
    public static final String OUTPUT_RESULT = "result";

    /** Scripting language. */
    private ActivityInput mInputLanguage;

    /** The script to be run. */
    private ActivityInput mInputScript;

    /** First input tuple streams. */
    private ActivityInput mInputData1;

    /** Second input tuple stream. */
    private ActivityInput mInputData2;

    /** Joined output tuple stream. */
    private ActivityOutput mResultOutput;

    /**
     * Constructor.
     */
    public GenericTupleJoin() {
        this(DEFAULT_NAME);
    }

    /**
     * Constructor.
     * 
     * @param name
     *        The id name for this activity.
     */
    public GenericTupleJoin(ActivityName name) {
        super(name);
        mInputLanguage = new SimpleActivityInput(INPUT_LANGUAGE);
        mInputScript = new SimpleActivityInput(INPUT_SCRIPT);
        mInputData1 = new SimpleActivityInput(INPUT_DATA1);
        mInputData2 = new SimpleActivityInput(INPUT_DATA2);
        mResultOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    /**
     * Get the activity inputs.
     */
    @Override
    protected ActivityInput[] getInputs() {
        return new ActivityInput[] { mInputLanguage, mInputScript, mInputData1, mInputData2 };
    }

    /**
     * Get the activity outputs.
     */
    @Override
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mResultOutput };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateIOState() throws ActivityIOIllegalStateException {
    }

    /**
     * Connects the <code>data1</code> input to the output of another activity.  
     * This is a required input that expects to receive lists of tuples.
     * 
     * @param output output to read from.
     */
    public void connectData1Input(SingleActivityOutput output) {
        mInputData1.connect(output);
    }

    /**
     * Connects the <code>data2</code> input to the output of another activity.  
     * This is a required input that expects to receive lists of tuples.
     * 
     * @param output output to read from.
     */
    public void connectData2Input(SingleActivityOutput output) {
        mInputData2.connect(output);
    }

    /**
     * Adds a script.
     * 
     * @param script
     *            script to execute
     */
    public void addScript(String script) {
        mInputScript.add(new StringData(script));
    }

    /**
     * Connects the input the specifies the script.
     * 
     * @param output
     *            output to connect
     */
    public void connectScriptInput(SingleActivityOutput output) {
        mInputScript.connect(output);
    }

    /**
     * Adds a language to the input.
     * 
     * @param language
     *            script language identifier
     */
    public void addLanguage(String language) {
        mInputLanguage.add(new StringData(language));
    }

    /**
     * Connects the input that specifies the script language.
     * 
     * @param output
     *            output to connect
     */
    public void connectLanguageInput(SingleActivityOutput output) {
        mInputLanguage.connect(output);
    }

    /**
     * Gets the result output.  The result will be a list of <code>Tuple</code>
     * objects with the first object in the list being a 
     * <code>MetadataWrapper</code> object wrapping a 
     * <code>TupleMetadata</code> object. 
     * 
     * @return SingleActivityOutput ActivityOutput written to.
     */
    public SingleActivityOutput getResultOutput() {
        return mResultOutput.getSingleActivityOutputs()[0];
    }
}
