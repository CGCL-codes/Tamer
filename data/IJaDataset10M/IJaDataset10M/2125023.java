package ibbt.sumo.vann.features;

import ibbt.sumo.vann.gui.images.Images;
import ibbt.sumo.vann.neuralNetworkModel.IEdgeListener;
import ibbt.sumo.vann.neuralNetworkModel.NNEdge;
import ibbt.sumo.vann.neuralNetworkModel.NeuralNetwork;
import java.awt.Color;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphConstants;

/**
 * Colors the edges blue or red depending on the sign of the weight.
 * @author Nathan Henckes
 */
public class EdgeRBColorer extends AFeature implements IEdgeListener {

    @Override
    public JInternalFrame getFrame() {
        return null;
    }

    @Override
    public Icon getIcon() {
        return Images.EDGE_RB_COLORER.getIcon();
    }

    @Override
    public String getName() {
        return "Edge Red/Blue colorer";
    }

    @Override
    public void register(NeuralNetwork network, JGraph graph) {
        network.addEdgeListener(this);
        super.register(network, graph);
    }

    @Override
    public void unregister() {
        getNetwork().removeEdgeListener(this);
        super.unregister();
    }

    @Override
    public void delayChanged(NNEdge edge, double oldDelay) {
    }

    /**
	 * Sets the color of the edge to blue on positive weight and red on negative weight.
	 */
    @Override
    public void weightChanged(NNEdge edge, double oldWeight) {
        Color col = null;
        if (edge.getWeight() >= 0.0) {
            col = Color.BLUE;
        } else {
            col = Color.RED;
        }
        Map<String, Object> map = edge.getEdge().getAttributesMap();
        GraphConstants.setLineColor(map, col);
        edge.getEdge().setAttributesMap(map);
    }

    @Override
    public void setupEdge(NNEdge edge) {
    }
}
