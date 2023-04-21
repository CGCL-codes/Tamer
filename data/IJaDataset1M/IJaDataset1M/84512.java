package neuralNetwork.neuralNetworkStructures;

import neuralNetwork.FeedForwardNN;
import neuralNetwork.NeuronLayer;
import neuralNetwork.Perceptron;
import neuralNetwork.perceptrons.SigmoidPerceptron;

/**
 * A Feed-Forward Neural Network with 2 hidden layers.
 * 
 * @author Amos Yuen
 * @version 1.00 - 9 July 2008
 */
public class NeuralNetwork2L extends FeedForwardNN {

    public NeuralNetwork2L() {
        super(32);
        Perceptron[] layer1 = new Perceptron[32];
        layer1[0] = new SigmoidPerceptron(inputs[0], inputs[4], inputs[5]);
        layer1[1] = new SigmoidPerceptron(inputs[1], inputs[5], inputs[6]);
        layer1[2] = new SigmoidPerceptron(inputs[2], inputs[6], inputs[7]);
        layer1[3] = new SigmoidPerceptron(inputs[3], inputs[7]);
        layer1[4] = new SigmoidPerceptron(inputs[4], inputs[0], inputs[8]);
        layer1[5] = new SigmoidPerceptron(inputs[5], inputs[0], inputs[1], inputs[8], inputs[9]);
        layer1[6] = new SigmoidPerceptron(inputs[6], inputs[1], inputs[2], inputs[9], inputs[10]);
        layer1[7] = new SigmoidPerceptron(inputs[7], inputs[2], inputs[3], inputs[10], inputs[11]);
        layer1[8] = new SigmoidPerceptron(inputs[8], inputs[4], inputs[5], inputs[12], inputs[13]);
        layer1[9] = new SigmoidPerceptron(inputs[9], inputs[5], inputs[6], inputs[13], inputs[14]);
        layer1[10] = new SigmoidPerceptron(inputs[10], inputs[6], inputs[7], inputs[14], inputs[15]);
        layer1[11] = new SigmoidPerceptron(inputs[11], inputs[7], inputs[15]);
        layer1[12] = new SigmoidPerceptron(inputs[12], inputs[8], inputs[16]);
        layer1[13] = new SigmoidPerceptron(inputs[13], inputs[8], inputs[9], inputs[16], inputs[17]);
        layer1[14] = new SigmoidPerceptron(inputs[14], inputs[9], inputs[10], inputs[17], inputs[18]);
        layer1[15] = new SigmoidPerceptron(inputs[15], inputs[10], inputs[11], inputs[18], inputs[19]);
        layer1[16] = new SigmoidPerceptron(inputs[16], inputs[12], inputs[13], inputs[20], inputs[21]);
        layer1[17] = new SigmoidPerceptron(inputs[17], inputs[13], inputs[14], inputs[21], inputs[22]);
        layer1[18] = new SigmoidPerceptron(inputs[18], inputs[14], inputs[15], inputs[22], inputs[23]);
        layer1[19] = new SigmoidPerceptron(inputs[19], inputs[15], inputs[23]);
        layer1[20] = new SigmoidPerceptron(inputs[20], inputs[16], inputs[24]);
        layer1[21] = new SigmoidPerceptron(inputs[21], inputs[16], inputs[17], inputs[24], inputs[25]);
        layer1[22] = new SigmoidPerceptron(inputs[22], inputs[17], inputs[18], inputs[25], inputs[26]);
        layer1[23] = new SigmoidPerceptron(inputs[23], inputs[18], inputs[19], inputs[26], inputs[27]);
        layer1[24] = new SigmoidPerceptron(inputs[24], inputs[20], inputs[21], inputs[28], inputs[29]);
        layer1[25] = new SigmoidPerceptron(inputs[25], inputs[21], inputs[22], inputs[29], inputs[30]);
        layer1[26] = new SigmoidPerceptron(inputs[26], inputs[22], inputs[23], inputs[30], inputs[31]);
        layer1[27] = new SigmoidPerceptron(inputs[27], inputs[23], inputs[31]);
        layer1[28] = new SigmoidPerceptron(inputs[28], inputs[24]);
        layer1[29] = new SigmoidPerceptron(inputs[29], inputs[24], inputs[25]);
        layer1[30] = new SigmoidPerceptron(inputs[30], inputs[25], inputs[26]);
        layer1[31] = new SigmoidPerceptron(inputs[31], inputs[26], inputs[27]);
        layers.add(new NeuronLayer<Perceptron>(layer1));
        Perceptron[] layer2 = new Perceptron[4];
        layer2[0] = new SigmoidPerceptron(layer1[0], layer1[1], layer1[4], layer1[5], layer1[8], layer1[9], layer1[12], layer1[13]);
        layer2[1] = new SigmoidPerceptron(layer1[2], layer1[3], layer1[6], layer1[7], layer1[10], layer1[11], layer1[14], layer1[15]);
        layer2[2] = new SigmoidPerceptron(layer1[16], layer1[17], layer1[20], layer1[21], layer1[24], layer1[25], layer1[28], layer1[29]);
        layer2[3] = new SigmoidPerceptron(layer1[18], layer1[19], layer1[22], layer1[23], layer1[26], layer1[27], layer1[30], layer1[31]);
        layers.add(new NeuronLayer<Perceptron>(layer2));
        layers.trimToSize();
        output = new SigmoidPerceptron(layer2);
    }
}
