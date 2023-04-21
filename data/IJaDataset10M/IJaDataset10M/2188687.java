package net.sourceforge.piqle.neuralnetwork.nu.construction;

import java.util.List;
import net.sourceforge.piqle.neuralnetwork.nu.Layer;
import net.sourceforge.piqle.neuralnetwork.nu.configuration.NNOutputLayer;
import net.sourceforge.piqle.neuralnetwork.nu.impl.Node;
import net.sourceforge.piqle.neuralnetwork.nu.impl.NodeImpl;
import net.sourceforge.piqle.neuralnetwork.nu.impl.OutputLayer;
import net.sourceforge.piqle.util.DerivableFunction;
import net.sourceforge.piqle.util.InvertibleFunction;
import com.google.inject.Inject;

public class OutputLayerFactory extends AbstractLayerFactory {

    @Inject
    public OutputLayerFactory(@NNOutputLayer DerivableFunction<Double, Double> activationFunction, NodeFactory nodeFactory) {
        super(activationFunction, nodeFactory);
    }

    protected void addNodes(int previousLayerSize, int currentLayerSize, List<Node> nodes) {
        for (int j = 0; j < currentLayerSize; j++) {
            nodes.add(nodeFactory.create(previousLayerSize, activationFunction));
        }
    }

    @Override
    protected Layer createLayer(List<Node> nodes) {
        return new OutputLayer(nodes, (InvertibleFunction<Double, Double>) activationFunction);
    }
}
