package uk.ac.shef.wit.trex.validation;

import java.util.Iterator;

/**
 * Builds precision, recall and f-measure scores.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class ScorePrecisionAndRecallBuilder {

    protected ScorePrecisionAndRecall _score = new ScorePrecisionAndRecall();

    public ScorePrecisionAndRecallBuilder() {
    }

    public ScorePrecisionAndRecallBuilder(final ScorePrecisionAndRecall[] scores) {
        for (int i = 0; i < scores.length; ++i) for (Iterator it = scores[i]._classes.iterator(); it.hasNext(); ) {
            final Object theClass = it.next();
            addTruePositives(theClass, scores[i].getTruePositives(theClass));
            addFalsePositives(theClass, scores[i].getFalsePositives(theClass));
            addFalseNegatives(theClass, scores[i].getFalseNegatives(theClass));
        }
    }

    public ScorePrecisionAndRecall getResult() {
        return _score;
    }

    public ScorePrecisionAndRecallBuilder addTruePositives(final Object theClass) {
        return addTruePositives(theClass, 1);
    }

    public ScorePrecisionAndRecallBuilder addTruePositives(final Object theClass, final int count) {
        _score._classes.add(theClass);
        _score._tp.put(theClass, _score._tp.getInt(theClass) + count);
        _score._tp.put(ScorePrecisionAndRecall.OVERALL, _score._tp.getInt(ScorePrecisionAndRecall.OVERALL) + count);
        return this;
    }

    public ScorePrecisionAndRecallBuilder addFalsePositives(final Object theClass) {
        return addFalsePositives(theClass, 1);
    }

    public ScorePrecisionAndRecallBuilder addFalsePositives(final Object theClass, final int count) {
        _score._classes.add(theClass);
        _score._fp.put(theClass, _score._fp.getInt(theClass) + count);
        _score._fp.put(ScorePrecisionAndRecall.OVERALL, _score._fp.getInt(ScorePrecisionAndRecall.OVERALL) + count);
        return this;
    }

    public ScorePrecisionAndRecallBuilder addFalseNegatives(final Object theClass) {
        return addFalseNegatives(theClass, 1);
    }

    public ScorePrecisionAndRecallBuilder addFalseNegatives(final Object theClass, final int count) {
        _score._classes.add(theClass);
        _score._fn.put(theClass, _score._fn.getInt(theClass) + count);
        _score._fn.put(ScorePrecisionAndRecall.OVERALL, _score._fn.getInt(ScorePrecisionAndRecall.OVERALL) + count);
        return this;
    }
}
