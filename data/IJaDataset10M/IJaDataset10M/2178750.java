package bioweka.classifiers.sequence.alignments;

import org.biojava.utils.process.InputHandler;
import org.biojava.utils.process.OutputHandler;
import weka.core.Instance;
import weka.core.Instances;
import bioweka.classifiers.sequence.alignments.parsers.AlignmentParser;
import bioweka.classifiers.sequence.alignments.parsers.AlignmentParserHandler;
import bioweka.classifiers.sequence.alignments.parsers.AlignmentParserProperty;
import bioweka.evaluators.ScoreEvaluator;

/**
 * Abstract base class for alignment classifiers that use an alignment parser.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractAlignmentParserClassifier extends AbstractExternalDatabaseAlignmentClassifier implements AlignmentParserHandler {

    /**
     * The property for the alignment parser.
     */
    private AlignmentParserProperty parserProperty = null;

    /**
     * The instance of the alignment parser to read the alignment output.
     */
    private AlignmentParser alignmentParser = null;

    /**
     * The instance of the alignment parser to write the alignment input.
     */
    private AlignmentParser alignmentWriter = null;

    /**
     * Initializes the alignment parser classifier.
     * @param defaultAlignCommand the default and initial align command
     * @param defaultBuildCommand the default and initial build command
     * @param defaultInfoCommand default and initial info command
     * @param defaultParser the default and initial alignment parser
     * @throws NullPointerException if one of the parameters is 
     * <code>null</code>.
     */
    public AbstractAlignmentParserClassifier(String defaultAlignCommand, String defaultBuildCommand, String defaultInfoCommand, AlignmentParser defaultParser) throws NullPointerException {
        super(defaultAlignCommand, defaultBuildCommand, defaultInfoCommand);
        parserProperty = new AlignmentParserProperty(defaultParser);
        manager().addProperty(parserProperty);
    }

    /**
     * {@inheritDoc}
     */
    public void buildClassifier(Instances data) throws Exception {
        alignmentParser = (AlignmentParser) getParser().clone();
        alignmentWriter = (AlignmentParser) getParser().clone();
        alignmentWriter.setSequenceAttribute(sequenceProperty().getSequenceAttribute(data));
        super.buildClassifier(data);
    }

    /**
     * {@inheritDoc}
     */
    protected OutputHandler outputHandler(ScoreEvaluator scoreEvaluator) {
        alignmentParser.initialize(scoreEvaluator);
        return alignmentParser;
    }

    /**
     * {@inheritDoc}
     */
    protected InputHandler inputHandler(Instances data) {
        alignmentWriter.initialize(data);
        return alignmentWriter;
    }

    /**
     * {@inheritDoc}
     */
    protected InputHandler inputHandler(Instance instance) {
        alignmentWriter.initialize(instance);
        return alignmentWriter;
    }

    /**
     * {@inheritDoc}
     */
    public AlignmentParser getParser() {
        return parserProperty.getParser();
    }

    /**
     * {@inheritDoc}
     */
    public void setParser(AlignmentParser parser) throws Exception {
        parserProperty.setParser(parser);
    }

    /**
     * {@inheritDoc}
     */
    public String parserTipText() {
        return parserProperty.parserTipText();
    }
}
