package uk.ac.shef.wit.trex.validation;

import uk.ac.shef.wit.trex.TrexException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Splits the input data into <i>n-1</i> train folds and 1 test fold and rotates.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class ValidationNCross extends ValidationMethodAbstract {

    public static final int DEFAULT_NUM_FOLDS = 4;

    public ValidationNCross() {
        this(DEFAULT_NUM_FOLDS);
    }

    public ValidationNCross(int numFolds) {
        super(numFolds);
    }

    public String toString() {
        return getNumFolds() + " cross-validation folds";
    }

    protected Set[] generateSplit(final ValidationMeasure measure, final int fold) throws TrexException {
        final Object[] sources = measure.getSources();
        final Set train = new TreeSet(), test = new TreeSet();
        final int start = fold * sources.length / getNumFolds();
        final int end = (fold + 1) * sources.length / getNumFolds();
        for (int pos = 0; pos < sources.length; ++pos) {
            if (pos < start || pos >= end) train.add(sources[pos]); else test.add(sources[pos]);
        }
        return new Set[] { train, test };
    }
}
