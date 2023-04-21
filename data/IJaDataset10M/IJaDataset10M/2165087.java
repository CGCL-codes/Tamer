package org.encog.ml.factory.method;

import java.util.List;
import org.encog.EncogError;
import org.encog.ml.MLMethod;
import org.encog.ml.factory.parse.ArchitectureLayer;
import org.encog.ml.factory.parse.ArchitectureParse;
import org.encog.neural.NeuralNetworkError;
import org.encog.neural.pnn.BasicPNN;
import org.encog.neural.pnn.PNNKernelType;
import org.encog.neural.pnn.PNNOutputMode;
import org.encog.util.ParamsHolder;

/**
 * A factory to create PNN networks.
 */
public class PNNFactory {

    /**
	 * The max layer count.
	 */
    public static final int MAX_LAYERS = 3;

    /**
	 * Create a PNN network.
	 * @param architecture THe architecture string to use.
	 * @param input The input count. 
	 * @param output The output count.
	 * @return The RBF network.
	 */
    public final MLMethod create(final String architecture, final int input, final int output) {
        final List<String> layers = ArchitectureParse.parseLayers(architecture);
        if (layers.size() != MAX_LAYERS) {
            throw new EncogError("PNN Networks must have exactly three elements, " + "separated by ->.");
        }
        final ArchitectureLayer inputLayer = ArchitectureParse.parseLayer(layers.get(0), input);
        final ArchitectureLayer pnnLayer = ArchitectureParse.parseLayer(layers.get(1), -1);
        final ArchitectureLayer outputLayer = ArchitectureParse.parseLayer(layers.get(2), output);
        final int inputCount = inputLayer.getCount();
        final int outputCount = outputLayer.getCount();
        PNNKernelType kernel;
        PNNOutputMode outmodel;
        if (pnnLayer.getName().equalsIgnoreCase("c")) {
            outmodel = PNNOutputMode.Classification;
        } else if (pnnLayer.getName().equalsIgnoreCase("r")) {
            outmodel = PNNOutputMode.Regression;
        } else if (pnnLayer.getName().equalsIgnoreCase("u")) {
            outmodel = PNNOutputMode.Unsupervised;
        } else {
            throw new NeuralNetworkError("Unknown model: " + pnnLayer.getName());
        }
        final ParamsHolder holder = new ParamsHolder(pnnLayer.getParams());
        final String kernelStr = holder.getString("KERNEL", false, "gaussian");
        if (kernelStr.equalsIgnoreCase("gaussian")) {
            kernel = PNNKernelType.Gaussian;
        } else if (kernelStr.equalsIgnoreCase("reciprocal")) {
            kernel = PNNKernelType.Reciprocal;
        } else {
            throw new NeuralNetworkError("Unknown kernel: " + kernelStr);
        }
        final BasicPNN result = new BasicPNN(kernel, outmodel, inputCount, outputCount);
        return result;
    }
}
