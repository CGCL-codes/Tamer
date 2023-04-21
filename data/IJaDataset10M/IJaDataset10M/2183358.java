package com.rapidminer.operator.features.weighting;

import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeWeights;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorCapability;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.preprocessing.discretization.BinDiscretization;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.tools.OperatorService;
import com.rapidminer.tools.math.ContingencyTableTools;

/**
 * This operator calculates the relevance of a feature by computing 
 * for each attribute of the input example set the value of the 
 * chi-squared statistic with respect to the class attribute.
 * 
 * @author Ingo Mierswa
 */
public class ChiSquaredWeighting extends AbstractWeighting {

    public ChiSquaredWeighting(OperatorDescription description) {
        super(description);
    }

    @Override
    protected AttributeWeights calculateWeights(ExampleSet exampleSet) throws OperatorException {
        Attribute label = exampleSet.getAttributes().getLabel();
        if (!label.isNominal()) {
            throw new UserError(this, 101, "chi squared test", label.getName());
        }
        BinDiscretization discretization = null;
        try {
            discretization = OperatorService.createOperator(BinDiscretization.class);
        } catch (OperatorCreationException e) {
            throw new UserError(this, 904, "Discretization", e.getMessage());
        }
        int numberOfBins = getParameterAsInt(BinDiscretization.PARAMETER_NUMBER_OF_BINS);
        discretization.setParameter(BinDiscretization.PARAMETER_NUMBER_OF_BINS, numberOfBins + "");
        exampleSet = discretization.doWork(exampleSet);
        int maximumNumberOfNominalValues = 0;
        for (Attribute attribute : exampleSet.getAttributes()) {
            if (attribute.isNominal()) {
                maximumNumberOfNominalValues = Math.max(maximumNumberOfNominalValues, attribute.getMapping().size());
            }
        }
        if (numberOfBins < maximumNumberOfNominalValues) {
            getLogger().warning("Number of bins too small, was " + numberOfBins + ". Set to maximum number of occurring nominal values (" + maximumNumberOfNominalValues + ")");
            numberOfBins = maximumNumberOfNominalValues;
        }
        double[][][] counters = new double[exampleSet.getAttributes().size()][numberOfBins][label.getMapping().size()];
        Attribute weightAttribute = exampleSet.getAttributes().getWeight();
        int exampleCounter = 0;
        double[] temporaryCounters = new double[label.getMapping().size()];
        for (Example example : exampleSet) {
            double weight = 1.0d;
            if (weightAttribute != null) {
                weight = example.getValue(weightAttribute);
            }
            int labelIndex = (int) example.getLabel();
            temporaryCounters[labelIndex] += weight;
            exampleCounter++;
        }
        for (int k = 0; k < counters.length; k++) {
            for (int i = 0; i < temporaryCounters.length; i++) {
                counters[k][0][i] = temporaryCounters[i];
            }
        }
        for (Example example : exampleSet) {
            int labelIndex = (int) example.getLabel();
            double weight = 1.0d;
            if (weightAttribute != null) {
                weight = example.getValue(weightAttribute);
            }
            int attributeCounter = 0;
            for (Attribute attribute : exampleSet.getAttributes()) {
                int attributeIndex = (int) example.getValue(attribute);
                counters[attributeCounter][attributeIndex][labelIndex] += weight;
                counters[attributeCounter][0][labelIndex] -= weight;
                attributeCounter++;
            }
        }
        AttributeWeights weights = new AttributeWeights(exampleSet);
        int attributeCounter = 0;
        for (Attribute attribute : exampleSet.getAttributes()) {
            double weight = ContingencyTableTools.getChiSquaredStatistics(ContingencyTableTools.deleteEmpty(counters[attributeCounter]), false);
            weights.setWeight(attribute.getName(), weight);
            attributeCounter++;
        }
        return weights;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeInt(BinDiscretization.PARAMETER_NUMBER_OF_BINS, "The number of bins used for discretization of numerical attributes before the chi squared test can be performed.", 2, Integer.MAX_VALUE, 10));
        return types;
    }

    @Override
    public boolean supportsCapability(OperatorCapability capability) {
        switch(capability) {
            case BINOMINAL_ATTRIBUTES:
            case POLYNOMINAL_ATTRIBUTES:
            case NUMERICAL_ATTRIBUTES:
            case BINOMINAL_LABEL:
            case POLYNOMINAL_LABEL:
                return true;
            default:
                return false;
        }
    }
}
