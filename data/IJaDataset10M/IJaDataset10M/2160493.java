package picoevo.app.RecurrentNeuralNetOptimisation;

import java.util.ArrayList;
import picoevo.core.*;
import picoevo.core.evolution.*;
import picoevo.core.representation.*;
import picoevo.ext.*;
import picoevo.ext.representation.*;
import picoevo.toolbox.*;
import piconode.core.arc.WeightedArc;
import piconode.core.node.RecurrentNeuralNetwork;
import piconode.core.node.RecurrentNeuron;
import piconode.ext.*;

public class EvaluationOperator_RNNforTemporalXor extends EvaluationOperator_Individual {

    public EvaluationOperator_RNNforTemporalXor() {
        super();
    }

    public EvaluationOperator_RNNforTemporalXor(String __name) {
        super(__name);
    }

    public void evaluate(Object __o) {
        super.evaluate(__o);
        Individual_SimpleIndividual individual = (Individual_SimpleIndividual) __o;
        double[] l = (double[]) ((Element_StaticArray_Double) individual.getElementAt(0)).getArray();
        ArrayList weights = new ArrayList();
        for (int i = 0; i != l.length; i++) weights.add(new Double(l[i]));
        individual.setFitness(this.computeFitness(weights, individual.getOwner().getGenerationNumber()));
    }

    private double computeFitness(ArrayList __weights, int __gen) {
        RecurrentNeuralNetwork network = new RecurrentNeuralNetwork(new ActivationFunction_logisticSigmoid());
        RecurrentNeuron in1 = new RecurrentNeuron(network, new ActivationFunction_logisticSigmoid());
        in1.setName("in1");
        RecurrentNeuron hidden1 = new RecurrentNeuron(network, new ActivationFunction_logisticSigmoid());
        hidden1.setName("hidden1");
        RecurrentNeuron hidden2 = new RecurrentNeuron(network, new ActivationFunction_logisticSigmoid());
        hidden1.setName("hidden2");
        RecurrentNeuron virtualIn2 = new RecurrentNeuron(network, new ActivationFunction_copy());
        hidden1.setName("virtualIn2");
        RecurrentNeuron out1 = new RecurrentNeuron(network, new ActivationFunction_threshold());
        out1.setName("out1");
        network.registerInputNeuron(in1);
        network.registerOutputNeuron(out1);
        network.registerArc(new WeightedArc(in1, hidden1));
        network.registerArc(new WeightedArc(in1, hidden2));
        network.registerArc(new WeightedArc(in1, virtualIn2));
        network.registerArc(new WeightedArc(virtualIn2, hidden1));
        network.registerArc(new WeightedArc(virtualIn2, hidden2));
        network.registerArc(new WeightedArc(hidden1, out1));
        network.registerArc(new WeightedArc(hidden2, out1));
        network.initNetwork();
        network.setAllArcsWeightValues(__weights);
        int nbevaluation = 100;
        int success = 0;
        ArrayList inputValuesList = new ArrayList();
        inputValuesList.add(new Double(0));
        network.step(inputValuesList);
        network.step(inputValuesList);
        network.step(inputValuesList);
        double notSoOldValue = 0;
        double oldValue = 0;
        double olderValue = 0;
        for (int i = 0; i != nbevaluation; i++) {
            double newValue = (double) ((int) (Math.random() + 0.5));
            inputValuesList.clear();
            inputValuesList.add(new Double(newValue));
            network.step(inputValuesList);
            olderValue = oldValue;
            oldValue = notSoOldValue;
            notSoOldValue = newValue;
            boolean result = false;
            if ((oldValue > 0.5 && olderValue < 0.5) || (oldValue < 0.5 && olderValue > 0.5)) result = true;
            if ((out1.getOutputValue() > 0.5 && result == true) || (out1.getOutputValue() < 0.5 && result == false)) success++;
        }
        return ((double) success / (double) nbevaluation);
    }
}
