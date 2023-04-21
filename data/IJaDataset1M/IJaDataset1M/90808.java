package nl.ru.rd.facedetection.nnbfd.tests;

import nl.ru.rd.facedetection.nnbfd.neuralnetwork.BNLayer;
import nl.ru.rd.facedetection.nnbfd.neuralnetwork.Input;
import nl.ru.rd.facedetection.nnbfd.neuralnetwork.InputLayer;
import nl.ru.rd.facedetection.nnbfd.neuralnetwork.NeuralNetwork;
import nl.ru.rd.facedetection.nnbfd.neuralnetwork.SigmoidActivationfunction;

/**
 * A simple NeuralNetwork used in testing binary operations (AND, OR, XOR etc.)
 * 
 * Comprises of an input, hidden and output layer.
 * 
 * @author Wouter Geraedts (s0814857)
 */
public class SimpleNetwork extends NeuralNetwork {

    private static final long serialVersionUID = -277447142232382774L;

    public SimpleNetwork() {
        super();
        this.initialize();
    }

    private void initialize() {
        InputLayer inputLayer = new InputLayer();
        for (int i = 0; i < 2; i++) {
            Input input = new Input(0.0);
            this.registerInput(input);
            inputLayer.addInput(input);
        }
        this.registerLayer(inputLayer);
        BNLayer firstLayer = new BNLayer(inputLayer, 2, new SigmoidActivationfunction());
        this.registerLayer(firstLayer);
        BNLayer hiddenLayer = new BNLayer(firstLayer, 2, new SigmoidActivationfunction());
        this.registerLayer(hiddenLayer);
        BNLayer outputLayer = new BNLayer(hiddenLayer, 1, new SigmoidActivationfunction());
        this.registerLayer(outputLayer);
        this.registerOutputLayer(outputLayer);
    }
}
