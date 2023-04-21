package org.neuralObjectz.mind;

import org.neuralObjectz.exceptions.CounterMismatchException;

public class Mind {

    private int height, length, numberOfLayers, depth;

    private Layer[] layer;

    private boolean black;

    private OutputLayer outputLayer;

    static final boolean DEBUG = false;

    public Mind(int height, int length, int numberOfLayers, int depth, boolean black) {
        this.height = height;
        this.length = length;
        this.numberOfLayers = numberOfLayers;
        this.depth = depth;
        this.black = black;
        layer = new Layer[numberOfLayers];
        for (int i = 0; i < numberOfLayers - 1; i++) {
            layer[i] = new Layer(height, length, depth, i);
            layer[i].reset();
        }
        layer[numberOfLayers - 1] = outputLayer = new OutputLayer(height, length, depth, numberOfLayers - 1);
        outputLayer.reset();
        for (int i = 0; i < numberOfLayers; i++) {
            int j = 0;
            while (i + j + 1 < numberOfLayers && j < depth) {
                layer[i + j + 1].attachInputLayer(layer[i], j);
                j++;
            }
        }
    }

    public void verify() {
        layer[0].fire();
        for (int i = 0; i < numberOfLayers; i++) {
            layer[i].fireIfValue();
        }
        layer[numberOfLayers - 1].dump();
    }

    public Output pounderUpon(Input myInput) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                if (black) {
                    layer[0].setValue(i, j, myInput.getBlackValue(i, j));
                } else {
                    layer[0].setValue(i, j, myInput.getWhiteValue(i, j));
                }
            }
        }
        if (DEBUG) {
            layer[0].dump();
        }
        layer[0].fire();
        for (int i = 1; i < numberOfLayers; i++) {
            if (DEBUG) {
                layer[i].dump();
            }
            layer[i].fireIfValue();
        }
        Output result = outputLayer.readOutput();
        layer[0].reset();
        for (int i = 1; i < numberOfLayers; i++) {
            layer[i].reset();
        }
        System.gc();
        return result;
    }

    public void learn(Output desiredOutput, int velocity) throws CounterMismatchException {
        outputLayer.learn(desiredOutput, velocity);
    }
}
