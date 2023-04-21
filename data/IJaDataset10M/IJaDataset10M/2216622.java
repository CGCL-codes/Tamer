package net.javlov;

/**
 * Interface provided such that Javlov entities that use neural networks become indepent of
 * specific package implementations for neural networks. Users can pick their favourite neural
 * network package and write an adapter class such that it implements the Javlov NeuralNet
 * interface to use it with Javlov.
 * 
 * @author Matthijs Snel
 *
 */
public interface NeuralNet extends Function {

    public void feedForward(double[] input);

    public double[] getOutput();

    public int getOutputSize();
}
