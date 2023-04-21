package uk.ac.shef.wit.trex.dataset.view.filter;

import uk.ac.shef.wit.trex.dataset.view.DatasetView;

/**
 * When applied, this view ranks the features of the original dataset according to the modified laplace measure and
 * return a dataset that contains only a certain top percentage of those features.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class FeaturesFilterLaplaceModified extends FeaturesFilterStatistical {

    public FeaturesFilterLaplaceModified(final int relevantRatio) {
        super(relevantRatio);
    }

    public FeaturesFilterLaplaceModified(final int relevantRatio, final DatasetView nested) {
        super(relevantRatio, nested);
    }

    public String getDescription() {
        return new StringBuffer("modified laplace selecting ").append(_relevantRatio).append(" of the total features").toString();
    }

    protected double calculateMeasure(final int featureCountPos, final int featureCountNeg, final int countPos, final int countNeg) {
        return ((double) featureCountPos + 1.0) / (countPos + featureCountNeg + 2.0);
    }
}
