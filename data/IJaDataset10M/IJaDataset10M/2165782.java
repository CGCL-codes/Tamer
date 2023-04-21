package com.rapidminer.operator.learner.tree;

import java.util.List;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeInt;

/**
 * <p>This operator learns decision trees from both nominal and numerical data.
 * Decision trees are powerful classification methods which often can also
 * easily be understood. This decision tree learner works similar to Quinlan's
 * C4.5 or CART.</p>
 * 
 * <p>The actual type of the tree is determined by the criterion, e.g. using 
 * gain_ratio or Gini for CART / C4.5.</p>
 * 
 * @rapidminer.index C4.5
 * @rapidminer.index CART
 *  
 * @author Sebastian Land, Ingo Mierswa
 * @version $Id: DecisionTreeLearner.java,v 1.14 2008/05/09 19:22:53 ingomierswa Exp $
 */
public class ParallelDecisionTreeLearner extends DecisionTreeLearner {

    public static final String PARAMETER_NUMBER_OF_THREADS = "number_of_threads";

    public ParallelDecisionTreeLearner(OperatorDescription description) {
        super(description);
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeInt(PARAMETER_NUMBER_OF_THREADS, "The number of parallel threads used for computation of split point", 1, Integer.MAX_VALUE));
        return types;
    }

    protected TreeBuilder getTreeBuilder(ExampleSet exampleSet) throws OperatorException {
        return new ParallelTreeBuilder(createCriterion(getParameterAsDouble(PARAMETER_MINIMAL_GAIN)), getTerminationCriteria(exampleSet), getPruner(), getSplitPreprocessing(), new DecisionTreeLeafCreator(), getParameterAsBoolean(PARAMETER_NO_PRE_PRUNING), getParameterAsInt(PARAMETER_NUMBER_OF_PREPRUNING_ALTERNATIVES), getParameterAsInt(PARAMETER_MINIMAL_SIZE_FOR_SPLIT), getParameterAsInt(PARAMETER_MINIMAL_LEAF_SIZE), this);
    }
}
