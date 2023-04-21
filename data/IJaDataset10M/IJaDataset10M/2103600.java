package de.kumpe.hadooptimizer.examples.neurons;

import java.io.Serializable;
import java.util.Collection;
import de.kumpe.hadooptimizer.EvaluationResult;
import de.kumpe.hadooptimizer.Evaluator;
import de.kumpe.hadooptimizer.examples.neurons.Neuron.Input;
import de.kumpe.hadooptimizer.impl.ReportingHalterWrapper.Reporter;

public class NeuronalNetEvaluator implements Evaluator<double[]>, Reporter<double[]> {

    private static final long serialVersionUID = 1L;

    public static class Sample implements Serializable {

        private static final long serialVersionUID = 1L;

        private final double[] inputValues;

        private final double outputValue;

        public Sample(final double[] inputValues, final double outputValue) {
            this.inputValues = inputValues;
            this.outputValue = outputValue;
        }
    }

    final InputValue[] inputValues;

    final Neuron.Input[] inputs;

    final Value output;

    final Sample[] samples;

    public NeuronalNetEvaluator(final InputValue[] inputValues, final Input[] inputs, final Value output, final Sample[] samples) {
        this.inputValues = inputValues;
        this.inputs = inputs;
        this.output = output;
        this.samples = samples;
    }

    public void print(final double[] weights) {
        for (int i = 0; i < inputs.length; i++) {
            inputs[i].setWeight(weights[i]);
        }
        for (final Sample sample : samples) {
            for (int i = 0; i < inputValues.length; i++) {
                final double inputValue = sample.inputValues[i];
                if (0 < i) {
                    System.out.print(", ");
                }
                System.out.print(inputValue);
                inputValues[i].set(inputValue);
            }
            System.out.printf(" => %f%n", output.value());
        }
    }

    @Override
    public double evaluate(final double[] weights) {
        double error = 0;
        for (final Sample sample : samples) {
            final double diff = evaluate(weights, sample.inputValues) - sample.outputValue;
            error += diff * diff;
        }
        return error;
    }

    double evaluate(final double[] weights, final double[] inputValues) {
        for (int i = 0; i < this.inputValues.length; i++) {
            this.inputValues[i].set(inputValues[i]);
        }
        for (int i = 0; i < inputs.length; i++) {
            inputs[i].setWeight(weights[i]);
        }
        return output.value();
    }

    @Override
    public void report(final Collection<EvaluationResult<double[]>> evaluationResults) {
        final double[] weights = evaluationResults.iterator().next().getIndividual();
        print(weights);
    }
}
