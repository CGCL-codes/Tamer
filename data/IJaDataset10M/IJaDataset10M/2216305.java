package piconode.debug;

import java.util.ArrayList;
import piconode.core.arc.ArcForBackPropLearning;
import piconode.core.node.FeedForwardNeuralNetworkForBackPropLearning;
import piconode.core.node.NeuronForBackPropLearning;
import piconode.ext.ActivationFunction_HyperbolicTangent;
import piconode.ext.ActivationFunction_Linear;
import piconode.ext.ActivationFunction_LogisticSigmoid;
import piconode.toolbox.Tools;

/**
 * @author nicolas
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Sandbox {

    /**
	 * This example shows how to initialize and use a simple neural network with
	 * only feed-forwarding the signal through the net architecture.
	 */
    public static void launchExampleOne_InitialisingAndSignalFeedforwarding() {
        System.out.println("This example shows how to initialize and use a simple neural network with only feed-forwarding the signal through the net architecture.");
        FeedForwardNeuralNetworkForBackPropLearning network = new FeedForwardNeuralNetworkForBackPropLearning(true);
        NeuronForBackPropLearning in1 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid());
        NeuronForBackPropLearning in2 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid());
        NeuronForBackPropLearning hidden1 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid());
        NeuronForBackPropLearning out1 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid());
        network.registerInputNeuron(in1);
        network.registerInputNeuron(in2);
        network.registerOutputNeuron(out1);
        network.registerArc(new ArcForBackPropLearning(in1, hidden1, Tools.getArcWeightRandomInitValue()));
        network.registerArc(new ArcForBackPropLearning(in2, hidden1, Tools.getArcWeightRandomInitValue()));
        network.registerArc(new ArcForBackPropLearning(hidden1, out1, Tools.getArcWeightRandomInitValue()));
        network.initNetwork();
        ArrayList inputValuesList = new ArrayList();
        inputValuesList.add(new Double(0.5));
        inputValuesList.add(new Double(0.5));
        network.step(inputValuesList);
        System.out.println("Output value : " + out1.getValue());
    }

    /**
	 * This example shows how to use simple for learning the XOR operator with
	 * back-propagation and a simple one hidden layer neural network.
	 */
    public static void launchExampleTwo_IntialisationAndLearningTheXorFunction() {
        System.out.println("This example shows how to use simple for learning the XOR operator with back-propagation and a simple one hidden layer neural network.");
        FeedForwardNeuralNetworkForBackPropLearning network = new FeedForwardNeuralNetworkForBackPropLearning(true);
        NeuronForBackPropLearning in1 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid(1), "in1");
        NeuronForBackPropLearning in2 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid(1), "in2");
        NeuronForBackPropLearning hidden1 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid(1), "hidden");
        NeuronForBackPropLearning out1 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid(1), "output");
        NeuronForBackPropLearning out2 = new NeuronForBackPropLearning(network, new ActivationFunction_LogisticSigmoid(1), "outputParasite");
        network.registerInputNeuron(in1);
        network.registerInputNeuron(in2);
        network.registerOutputNeuron(out1);
        network.registerOutputNeuron(out2);
        network.registerArc(new ArcForBackPropLearning(in1, hidden1, Tools.getArcWeightRandomInitValue()));
        network.registerArc(new ArcForBackPropLearning(in2, hidden1, Tools.getArcWeightRandomInitValue()));
        network.registerArc(new ArcForBackPropLearning(hidden1, out1, Tools.getArcWeightRandomInitValue()));
        network.registerArc(new ArcForBackPropLearning(in1, out1, Tools.getArcWeightRandomInitValue()));
        network.registerArc(new ArcForBackPropLearning(in2, out1, Tools.getArcWeightRandomInitValue()));
        network.registerArc(new ArcForBackPropLearning(in2, out2, Tools.getArcWeightRandomInitValue()));
        network.initNetwork();
        network.backprop_setEtaLearningRate(1);
        in1.setLearningNodeFlag(true);
        in2.setLearningNodeFlag(true);
        hidden1.setLearningNodeFlag(true);
        out1.setLearningNodeFlag(true);
        out2.setLearningNodeFlag(false);
        ArrayList learningSetForXorTest = new ArrayList();
        learningSetForXorTest.add(new Double(0));
        learningSetForXorTest.add(new Double(0));
        learningSetForXorTest.add(new Double(1));
        learningSetForXorTest.add(new Double(0));
        learningSetForXorTest.add(new Double(0));
        learningSetForXorTest.add(new Double(1));
        learningSetForXorTest.add(new Double(1));
        learningSetForXorTest.add(new Double(1));
        ArrayList labelSetForXorTest = new ArrayList();
        labelSetForXorTest.add(new Double(0));
        labelSetForXorTest.add(new Double(1));
        labelSetForXorTest.add(new Double(1));
        labelSetForXorTest.add(new Double(0));
        network.displayInformation();
        for (int i = 0; i != 10000; i++) {
            ArrayList inputXorValuesList = new ArrayList();
            inputXorValuesList.add(learningSetForXorTest.get((i * 2) % 8));
            inputXorValuesList.add(learningSetForXorTest.get((i * 2 + 1) % 8));
            network.step(inputXorValuesList);
            ArrayList correctOutputValue = new ArrayList();
            correctOutputValue.add(new Double(((Double) (labelSetForXorTest.get(i % 4))).doubleValue()));
            if ((i % 1000) <= 3) {
                if ((i % 1000) == 0) {
                    System.out.println("** iteration " + i / 4 + " ** : ");
                }
                System.out.println("( " + learningSetForXorTest.get((i * 2) % 8) + " xor " + learningSetForXorTest.get((i * 2 + 1) % 8) + " ) -> " + out1.getValue() + " [should be " + ((Double) (labelSetForXorTest.get(i % 4))).doubleValue() + " -- squared error is " + network.estimateSquaredError(correctOutputValue) + "]");
            }
            network.performBackPropagationLearning(correctOutputValue);
        }
        System.out.println("Stopped learning.");
        network.displayInformation();
    }

    /**
	 * This example shows how to use simple for learning the sinus function with
	 * back-propagation and a simple one hidden layer neural network.
	 */
    public static void launchExampleThree_IntialisationAndLearningTheSinusFunction() {
        System.out.println("This example shows how to use simplenet for learning the sine function with back-propagation and a simple one hidden layer neural network.\n\n");
        FeedForwardNeuralNetworkForBackPropLearning network = new FeedForwardNeuralNetworkForBackPropLearning(true);
        NeuronForBackPropLearning in1 = new NeuronForBackPropLearning(network, new ActivationFunction_Linear(), "in1");
        NeuronForBackPropLearning hidden1 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(1)");
        NeuronForBackPropLearning hidden2 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(2)");
        NeuronForBackPropLearning hidden3 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(3)");
        NeuronForBackPropLearning hidden4 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(4)");
        NeuronForBackPropLearning out1 = new NeuronForBackPropLearning(network, new ActivationFunction_Linear(), "output");
        network.registerInputNeuron(in1);
        network.registerOutputNeuron(out1);
        network.registerArc(new ArcForBackPropLearning(in1, hidden1, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(in1, hidden2, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(in1, hidden3, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(in1, hidden4, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(hidden1, out1, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(hidden2, out1, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(hidden3, out1, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(hidden4, out1, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.registerArc(new ArcForBackPropLearning(in1, out1, Tools.getArcWeightRandomInitValue(-1, 2)));
        network.initNetwork();
        network.backprop_setEtaLearningRate(0.2);
        in1.setLearningNodeFlag(true);
        hidden1.setLearningNodeFlag(true);
        hidden2.setLearningNodeFlag(true);
        hidden3.setLearningNodeFlag(true);
        hidden4.setLearningNodeFlag(true);
        out1.setLearningNodeFlag(true);
        network.displayInformation();
        ArrayList inputSinusValuesList = new ArrayList();
        ArrayList correctOutputSinusValuesList = new ArrayList();
        System.out.println("\n###create learning examples###");
        int k = 0;
        double index_init = -1, index_stop = 1, index_step = 0.125;
        for (double d = index_init; d < index_stop; d += index_step) {
            System.out.print("\ntraining example " + k + " : sin(" + d + ") = " + Math.sin(d));
            inputSinusValuesList.add(new Double(d));
            correctOutputSinusValuesList.add(new Double(Math.sin(d)));
            k++;
        }
        double previousEstimatedError = 0;
        System.out.println("\n\n###start learning###");
        System.out.println("# learning cycle # estimated squared error # error on test set #");
        for (int i = 0; i != 500; i++) {
            double estimatedError = 0;
            double errorOnTestSet = 0;
            System.out.print(i + " \t");
            for (int j = 0; j != inputSinusValuesList.size(); j++) {
                ArrayList inputSinusValue = new ArrayList();
                inputSinusValue.add((inputSinusValuesList.get(j)));
                network.step(inputSinusValue);
                ArrayList correctOutputSinusValue = new ArrayList();
                correctOutputSinusValue.add(correctOutputSinusValuesList.get(j));
                estimatedError += network.estimateSquaredError(correctOutputSinusValue);
                double errortemp = network.estimateSquaredError(correctOutputSinusValue);
                network.performBackPropagationLearning(correctOutputSinusValue);
            }
            System.out.print(estimatedError / inputSinusValuesList.size() + " \t");
            if (previousEstimatedError == estimatedError / inputSinusValuesList.size()) {
                System.out.println("\nTerminated : Network is stuck in either local or global maxima.");
                network.displayInformation();
                System.exit(0);
            }
            previousEstimatedError = estimatedError / inputSinusValuesList.size();
            for (int j = 0; j != inputSinusValuesList.size(); j++) {
                ArrayList inputSinusValue = new ArrayList();
                double randomInput = (Math.random() - 1) * 2;
                inputSinusValue.add(new Double(randomInput));
                network.step(inputSinusValue);
                ArrayList correctOutputSinusValue = new ArrayList();
                correctOutputSinusValue.add(new Double(Math.sin(randomInput)));
                errorOnTestSet += network.estimateSquaredError(correctOutputSinusValue);
            }
            System.out.print(errorOnTestSet / inputSinusValuesList.size());
            System.out.print("\n");
        }
        System.out.println("Stopped learning.\n");
        System.out.println("Final classifier results (test on learning set - debug purpose)");
        for (int i = 0; i != inputSinusValuesList.size(); i++) {
            double value = ((Double) (inputSinusValuesList.get(i))).doubleValue();
            ArrayList inputSinusValue = new ArrayList();
            inputSinusValue.add(new Double(value));
            network.step(inputSinusValue);
            System.out.println("test on example " + value + " : sin(" + value + ") = " + network.getOutputNeuronAt(0).getValue() + " (correct value is " + Math.sin(value) + ")");
        }
        System.out.println();
        network.displayInformation();
    }

    /**
	 * This example shows how to use simple for learning the sinus function with
	 * back-propagation and a simple one hidden layer neural network.
	 */
    public static void test() {
        System.out.println("This example shows how to use simplenet for learning the sine function with back-propagation and a simple one hidden layer neural network.\n\n");
        FeedForwardNeuralNetworkForBackPropLearning network = new FeedForwardNeuralNetworkForBackPropLearning(true);
        NeuronForBackPropLearning in1 = new NeuronForBackPropLearning(network, new ActivationFunction_Linear(), "in1");
        NeuronForBackPropLearning hidden1 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(1)");
        NeuronForBackPropLearning hidden2 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(2)");
        NeuronForBackPropLearning hidden3 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(3)");
        NeuronForBackPropLearning hidden4 = new NeuronForBackPropLearning(network, new ActivationFunction_HyperbolicTangent(), "hidden(4)");
        NeuronForBackPropLearning out1 = new NeuronForBackPropLearning(network, new ActivationFunction_Linear(), "output");
        network.registerInputNeuron(in1);
        network.registerOutputNeuron(out1);
        network.registerArc(new ArcForBackPropLearning(in1, hidden1, 1));
        network.registerArc(new ArcForBackPropLearning(in1, hidden2, 2));
        network.registerArc(new ArcForBackPropLearning(in1, hidden3, 3));
        network.registerArc(new ArcForBackPropLearning(in1, hidden4, 4));
        network.registerArc(new ArcForBackPropLearning(hidden1, out1, 5));
        network.registerArc(new ArcForBackPropLearning(hidden2, out1, 6));
        network.registerArc(new ArcForBackPropLearning(hidden3, out1, 7));
        network.registerArc(new ArcForBackPropLearning(hidden4, out1, 8));
        network.registerArc(new ArcForBackPropLearning(in1, out1, 9));
        network.initNetwork();
        network.backprop_setEtaLearningRate(0.2);
        in1.setLearningNodeFlag(true);
        hidden1.setLearningNodeFlag(true);
        hidden2.setLearningNodeFlag(true);
        hidden3.setLearningNodeFlag(true);
        hidden4.setLearningNodeFlag(true);
        out1.setLearningNodeFlag(true);
        network.displayInformation();
        ArrayList values = new ArrayList();
        for (int i = 0; i != 9 + 5; i++) {
            values.add(new Double(i));
        }
        network.setAllArcsWeightValues(values);
        ArrayList returnValues = network.getWeightsFromAllArcs();
        System.out.println("in");
        for (int i = 0; i != 9 + 5; i++) {
            System.out.print("[" + ((Double) values.get(i)).doubleValue() + "]");
        }
        System.out.println("\nOut");
        for (int i = 0; i != 9 + 5; i++) {
            System.out.print("[" + ((Double) returnValues.get(i)).doubleValue() + "]");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        double startTime = System.currentTimeMillis();
        System.out.println("Running...");
        launchExampleThree_IntialisationAndLearningTheSinusFunction();
        System.out.println("\nTerminated (" + ((System.currentTimeMillis() - startTime) / 1000) + "s elapsed).");
    }
}
