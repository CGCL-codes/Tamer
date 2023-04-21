package bioweka.analyzers;

import bioweka.core.components.AbstractDebugableComponent;

/**
 * Abstract base class for sequence analyzers.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractSequenceAnalyzer extends AbstractDebugableComponent implements SequenceAnalyzer {

    /**
     * The name of the tokenizer used to parse the sequence.
     */
    public static final String TOKENIZER = "token";

    /**
     * Initializes the sequence analyzer.
     */
    public AbstractSequenceAnalyzer() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public Object clone() {
        return super.clone();
    }
}
