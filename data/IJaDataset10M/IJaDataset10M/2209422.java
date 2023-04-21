package ibbt.sumo.vann.features;

import ibbt.sumo.vann.neuralNetworkModel.IEdgeListener;
import ibbt.sumo.vann.neuralNetworkModel.INodeListener;
import ibbt.sumo.vann.neuralNetworkModel.NNEdge;
import ibbt.sumo.vann.neuralNetworkModel.NNNode;
import ibbt.sumo.vann.neuralNetworkModel.NeuralNetwork;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import org.jgraph.JGraph;

/**
 * Sets the ToolTips of the GraphEdge to the weight and the GraphNode to the bias
 * @author Nathan Henckes
 */
public class ToolTipSetters extends AFeature implements IEdgeListener, INodeListener {

    @Override
    public JInternalFrame getFrame() {
        return null;
    }

    /**
	 * Always returns false, making it an invisible feature
	 */
    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getName() {
        return "ToolTip Setter";
    }

    @Override
    public void register(NeuralNetwork network, JGraph graph) {
        network.addEdgeListener(this);
        network.addNodeListener(this);
        super.register(network, graph);
    }

    @Override
    public void unregister() {
        this.getNetwork().removeEdgeListener(this);
        this.getNetwork().removeNodeListener(this);
        super.unregister();
    }

    @Override
    public void delayChanged(NNEdge edge, double oldDelay) {
    }

    @Override
    public void setupEdge(NNEdge edge) {
    }

    /**
	 * Set ToolTip of edge to weight
	 */
    @Override
    public void weightChanged(NNEdge edge, double oldWeight) {
        edge.getEdge().setToolTip(Double.toString(edge.getWeight()));
    }

    /**
	 * Set ToolTip of node to bias
	 */
    @Override
    public void biasChanged(NNNode node, double oldBias) {
        node.getNode().setToolTip(Double.toString(node.getBias()));
    }

    @Override
    public void setupNode(NNNode node) {
    }
}
