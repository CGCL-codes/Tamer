package net.sf.myra.datamining.function;

import java.util.List;
import net.sf.myra.datamining.AbstractObjectiveFunction;
import net.sf.myra.datamining.Evaluator;
import net.sf.myra.datamining.Rule;
import net.sf.myra.datamining.data.Dataset;
import net.sf.myra.datamining.data.Instance;
import net.sf.myra.datamining.data.Label;
import net.sf.myra.framework.Cost;
import net.sf.myra.framework.NumericCost;
import net.sf.myra.framework.Trail;

/**
 * This class represents the hierarchical F-measure function.
 * 
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2148 $ $Date:: 2009-06-14 13:56:57#$
 */
public class hFmeasureFunction extends AbstractObjectiveFunction {

    /**
	 * Default constructor.
	 * 
	 * @param dataset the dataset used in the evaluation.
	 */
    public hFmeasureFunction(Dataset dataset) {
        super(dataset);
    }

    public Cost evaluate(Trail trail) {
        Rule rule = (Rule) trail;
        List<Instance> instances = Evaluator.findCoveredCases(rule, getDataset());
        Label pLabel = rule.getConsequent();
        int correct = 0;
        int predicted = 0;
        int total = 0;
        for (Instance instance : instances) {
            Label tLabel = instance.getLabel();
            correct += (Math.max(tLabel.intersect(pLabel) - 1, 0));
            predicted += (Math.max(pLabel.cardinality() - 1, 0));
            total += (tLabel.cardinality() - 1);
        }
        double hP = correct / (double) predicted;
        double hR = correct / (double) total;
        double hF = (2 * hP * hR) / (hP + hR);
        return new NumericCost(NumericCost.isNumber(hF) ? hF : 0.0);
    }
}
