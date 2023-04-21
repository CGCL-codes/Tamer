package bioweka.classifiers.sequence.alignments.parsers;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import bioweka.core.BioWekaUtils;
import bioweka.core.debuggers.Debugger;
import bioweka.core.inspection.Report;
import bioweka.evaluators.ScoreEvaluator;

/**
 * Alignment parser for BLAST output.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.16 $
 */
public class BlastParser extends AbstractSimpleAlignmentParser {

    /**
     * The unique class identifier.
     */
    private static final long serialVersionUID = 3760559780531747122L;

    /**
     * The description of the BLAST parser component.
     */
    public static final String BLAST_PARSER_GLOBAL_INFO = "Parser for BLAST output.";

    /**
     * The description of the ignore perfect local matches property.
     */
    public static final String IGNORE_PERFECT_LOCAL_MATCHES_TIP_TEXT = "Ignore sequences that are 100 % identical to the template sequence" + "with respect to the *local* alignment.";

    /**
     * The name of the ignore perfect local matches property.
     */
    public static final String IGNORE_PERFECT_LOCAL_MATCHES_PROPERTY_NAME = "ignorePerfectLocalMatches";

    /**
     * The option flag to set the ignore perfect local matches property.
     */
    public static final String IGNORE_PERFECT_LOCAL_MATCHES_OPTION_FLAG = "X";

    /**
     * The default value for the ignore perfect local matches property.
     */
    public static final boolean IGNORE_PERFECT_LOCAL_MATCHES_DEFAULT_VALUE = false;

    /**
     * Flag wheater (100 %) identical target sequences should be ignored.
     */
    private boolean ignorePerfectLocalMatches = IGNORE_PERFECT_LOCAL_MATCHES_DEFAULT_VALUE;

    /**
     * Initializes the BLAST parser;
     */
    public BlastParser() {
        super();
    }

    /**
     * Indicates if sequences that are reported to be 100 % identical in the 
     * <b>local</b> alignment should be ignored.
     * @return <code>true</code> if the identical sequences should be ignored,
     * <code>false</code> otherwise.
     */
    public boolean getIgnorePerfectLocalMatches() {
        return ignorePerfectLocalMatches;
    }

    /**
     * Specifies if sequences that are reported to be 100 % identical in the 
     * <b>local</b> alignment should be ignored.
     * @param ignorePerfectLocalMatches <code>true</code> if the identical 
     * sequences should be ignored,
     * <code>false</code> otherwise.
     */
    public void setIgnorePerfectLocalMatches(boolean ignorePerfectLocalMatches) {
        getDebugger().config(IGNORE_PERFECT_LOCAL_MATCHES_PROPERTY_NAME, Boolean.valueOf(ignorePerfectLocalMatches));
        this.ignorePerfectLocalMatches = ignorePerfectLocalMatches;
    }

    /**
     * Returns the description of the ignore perfect local matches property.
     * @return a human readable text
     */
    public String ignorePerfectLocalMatchesTipText() {
        return IGNORE_PERFECT_LOCAL_MATCHES_TIP_TEXT;
    }

    /**
     * Writes out an instance using a writer
     * @param writer the writer 
     * @param instance the instance
     * @throws Exception if the instance could not be written out.
     */
    private void write(BufferedWriter writer, Instance instance) throws Exception {
        String sequence = retrieveSequence(instance);
        double classValue = instance.classValue();
        double weight = instance.weight();
        String output = ">" + Double.toString(classValue) + " " + Double.toString(weight) + "\n" + sequence + "\n";
        getDebugger().info("write:" + output);
        writer.write(output);
    }

    /**
     * {@inheritDoc}
     */
    protected void write(Instances data) throws Exception {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOutput()));
        try {
            Enumeration instances = data.enumerateInstances();
            while (instances.hasMoreElements()) {
                write(writer, (Instance) instances.nextElement());
            }
        } catch (Exception e) {
            getDebugger().severe(e);
            throw e;
        } finally {
            writer.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void write(Instance instance) throws Exception {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOutput()));
        try {
            write(writer, instance);
        } catch (Exception e) {
            getDebugger().severe(e);
            throw e;
        } finally {
            writer.close();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    protected void parse(ScoreEvaluator scoreEvaluator) throws Exception {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(getInput()));
        try {
            parse(reader, scoreEvaluator);
        } finally {
            reader.close();
        }
    }

    /**
     * Parses the BLAST output given by a reader object and fills the score
     * evaluator.
     * @param reader the reader
     * @param scoreEvaluator the score evaluator
     * @throws Exception if the output could not be parsed.
     */
    protected void parse(LineNumberReader reader, ScoreEvaluator scoreEvaluator) throws Exception {
        String line = null;
        StringBuffer descriptionLine = null;
        double classValue = Double.NaN;
        double weight = 1.0;
        double scoreValue = Double.NaN;
        Debugger debugger = getDebugger();
        while ((line = reader.readLine()) != null) {
            debugger.info("Line " + reader.getLineNumber() + ": " + line);
            if (line.startsWith(">")) {
                descriptionLine = new StringBuffer();
                descriptionLine.append(line.substring(1));
            } else if (descriptionLine != null) {
                if (line.trim().startsWith("Length")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(descriptionLine.toString());
                    descriptionLine = null;
                    classValue = Double.NaN;
                    weight = 1.0;
                    if (stringTokenizer.hasMoreTokens()) {
                        String token = stringTokenizer.nextToken();
                        classValue = Double.parseDouble(token);
                    }
                    if (stringTokenizer.hasMoreTokens()) {
                        String token = stringTokenizer.nextToken();
                        weight = Double.parseDouble(token);
                    }
                } else {
                    descriptionLine.append(line);
                }
            } else if (line.trim().startsWith("Score =") && !Double.isNaN(classValue)) {
                scoreValue = Double.parseDouble(line.split("\\s+", 5)[3]);
            } else if (line.trim().startsWith("Identities") && !Double.isNaN(classValue)) {
                if ("(100%),".equals(line.split("\\s+", 6)[4])) {
                    if (!ignorePerfectLocalMatches) {
                        debugger.warning("Line " + reader.getLineNumber() + ": 100 % identity match found.");
                        scoreEvaluator.registerScore(classValue, scoreValue, weight);
                    }
                } else {
                    scoreEvaluator.registerScore(classValue, scoreValue, weight);
                }
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public String globalInfo() {
        return BLAST_PARSER_GLOBAL_INFO;
    }

    /**
     * {@inheritDoc}
     */
    public void inspect(Report report) throws NullPointerException {
        super.inspect(report);
        report.append(IGNORE_PERFECT_LOCAL_MATCHES_PROPERTY_NAME, Boolean.valueOf(ignorePerfectLocalMatches));
    }

    /**
     * {@inheritDoc}
     */
    public void getOptions(FastVector options) {
        super.getOptions(options);
        if (ignorePerfectLocalMatches != IGNORE_PERFECT_LOCAL_MATCHES_DEFAULT_VALUE) {
            options.addElement("-" + IGNORE_PERFECT_LOCAL_MATCHES_OPTION_FLAG);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void listOptions(FastVector options) {
        super.listOptions(options);
        options.addElement(new Option(BioWekaUtils.formatDescription(IGNORE_PERFECT_LOCAL_MATCHES_TIP_TEXT, Boolean.toString(IGNORE_PERFECT_LOCAL_MATCHES_DEFAULT_VALUE)), IGNORE_PERFECT_LOCAL_MATCHES_OPTION_FLAG, 0, "-" + IGNORE_PERFECT_LOCAL_MATCHES_OPTION_FLAG));
    }

    /**
     * {@inheritDoc}
     */
    public void setOptions(String[] options) throws Exception {
        super.setOptions(options);
        setIgnorePerfectLocalMatches(Utils.getFlag(IGNORE_PERFECT_LOCAL_MATCHES_OPTION_FLAG, options));
    }
}
