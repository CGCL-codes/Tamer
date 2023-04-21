package net.sf.wwusmart.algorithms.combination;

import net.sf.wwusmart.algorithms.framework.Parameter;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author thilo
 * @version $Rev: 777 $
 */
public class CombinationFunctionBayesAlgorithm extends CombinationByFunction {

    @Override
    public String getName() {
        return "Bayes Combination Rule";
    }

    @Override
    public String getDescription() {
        return "Uses the product of the similarity indeces of several algorithms as combined similarity index.";
    }

    @Override
    double function(double[] values) {
        double prod = 1.;
        for (double factor : values) {
            prod *= factor;
        }
        return prod;
    }

    @Override
    public List<Parameter> getPerMatchParameters() {
        return new Vector<Parameter>();
    }
}
