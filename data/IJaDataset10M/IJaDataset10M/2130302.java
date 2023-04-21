package net.sf.myra.hantminer.probabilistic;

import net.sf.myra.datamining.Rule;
import net.sf.myra.datamining.data.Label;
import net.sf.myra.datamining.data.Dataset.Instance;
import net.sf.myra.datamining.model.RuleList;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2044 $ $Date:: 2009-03-09 12:02:31#$
 */
public class ProbabilisticRuleList extends RuleList {

    private static final long serialVersionUID = 450002538007785073L;

    @Override
    public Label classify(Instance instance) {
        throw new UnsupportedOperationException("Probabilistic rule list.");
    }

    public double[] probabilities(Instance instance) {
        for (Rule rule : getRules()) {
            if (rule.isCovered(instance)) {
                return ((ProbabilisticRule) rule).getProbabiblities();
            }
        }
        throw new IllegalArgumentException("Could not classify the instance");
    }
}
