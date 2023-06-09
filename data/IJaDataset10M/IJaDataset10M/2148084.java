package com.rapidminer.operator.performance;

import java.util.Iterator;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.math.Averagable;
import com.rapidminer.tools.math.MathFunctions;

/**
 * Calculates the cross-entropy for the predictions of a classifier.
 *  
 * @author Ingo Mierswa
 */
public class CrossEntropy extends MeasuredPerformance {

    private static final long serialVersionUID = 8341971882780129465L;

    /** The value of the criterion. */
    private double value = Double.NaN;

    private double counter = 1.0d;

    /** Clone constructor. */
    public CrossEntropy() {
    }

    public CrossEntropy(CrossEntropy c) {
        super(c);
        this.value = c.value;
        this.counter = c.counter;
    }

    /** Calculates the margin. */
    @Override
    public void startCounting(ExampleSet exampleSet, boolean useExampleWeights) throws OperatorException {
        super.startCounting(exampleSet, useExampleWeights);
        Iterator<Example> reader = exampleSet.iterator();
        this.value = 0.0d;
        Attribute labelAttr = exampleSet.getAttributes().getLabel();
        Attribute weightAttribute = null;
        if (useExampleWeights) weightAttribute = exampleSet.getAttributes().getWeight();
        while (reader.hasNext()) {
            Example example = reader.next();
            String trueLabel = example.getNominalValue(labelAttr);
            double confidence = example.getConfidence(trueLabel);
            double weight = 1.0d;
            if (weightAttribute != null) weight = example.getValue(weightAttribute);
            this.value -= weight * MathFunctions.ld(confidence);
            this.counter += weight;
        }
    }

    /** Does nothing. Everything is done in {@link #startCounting(ExampleSet, boolean)}. */
    @Override
    public void countExample(Example example) {
    }

    @Override
    public double getExampleCount() {
        return counter;
    }

    @Override
    public double getMikroVariance() {
        return Double.NaN;
    }

    @Override
    public double getMikroAverage() {
        return value / counter;
    }

    /** Returns the fitness. */
    @Override
    public double getFitness() {
        return -1 * getAverage();
    }

    @Override
    public String getName() {
        return "cross-entropy";
    }

    @Override
    public String getDescription() {
        return "The cross-entropy of a classifier, defined as the sum over the logarithms of the true label's confidences divided by the number of examples";
    }

    @Override
    public void buildSingleAverage(Averagable performance) {
        CrossEntropy other = (CrossEntropy) performance;
        this.value += other.value;
        this.counter += other.counter;
    }

    /** Returns the super class implementation of toString(). */
    @Override
    public String toString() {
        return super.toString();
    }
}
