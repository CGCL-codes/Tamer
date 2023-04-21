package org.annj.test;

import org.annj.*;
import org.annj.GA.*;

/**
 * Tests the NeuronLayer class
 * 
 * @author Bradley Schleusner
 */
public class NeuronLayerTest implements ITestClass {

    /**
	 * Tests the NeuronLayer class
	 */
    public boolean isWorking() {
        boolean result = true;
        Configuration config = new Configuration();
        config.setFitnessEvaluator(new DefaultFitnessEvaluator());
        config.setNeuronLayers(new int[] { 4 });
        config.setOutputRange(0, 255);
        config.setNeuronOutputFunction(new BinaryOutputFunction());
        Neuron neuron = new Neuron(config, 4, new double[] { 1.0d, 1.0d, 1.0d, 1.0d }, 128);
        NeuronLayer nLayer = new NeuronLayer(config, 4, 4, new Neuron[] { neuron, (Neuron) neuron.clone(), (Neuron) neuron.clone(), (Neuron) neuron.clone() });
        double[] output = nLayer.compute(new double[] { 0, 255, 0, 0 });
        if (output.length != 4) {
            System.out.println("Wrong output size.");
            result = false;
        }
        for (int i = 0; i < 4; i++) {
            if (output[i] != 255) {
                System.out.println("Wrong output for neuron " + i + "\toutput was " + output[i]);
                result = false;
            }
        }
        return result;
    }
}
