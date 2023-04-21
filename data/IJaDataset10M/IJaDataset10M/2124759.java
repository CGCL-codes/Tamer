package org.neuroph.netbeans.visual.support;

import org.neuroph.netbeans.visual.widgets.NeuralNetworkWidget;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;
import java.awt.Point;
import java.util.List;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.widgets.NeuronConnectionWidget;

/**
 *
 * @author Damir
 */
public class FunctionConnectProvider implements ConnectProvider {

    private NeuralNetworkWidget nnet;

    Connection connection;

    public FunctionConnectProvider(NeuralNetworkWidget nnet) {
        this.nnet = nnet;
    }

    @Override
    public boolean isSourceWidget(Widget source) {
        return source instanceof NeuronWidget && source != null ? true : false;
    }

    @Override
    public ConnectorState isTargetWidget(Widget src, Widget trg) {
        Neuron srcNeuron = null;
        Neuron trgNeuron = null;
        if (src != trg && src instanceof NeuronWidget && trg instanceof NeuronWidget && src.getParentWidget() != trg.getParentWidget()) {
            List<Widget> layers = nnet.getChildren();
            for (int i = 0; i < layers.size(); i++) {
                List<Widget> neurons = layers.get(i).getChildren();
                for (int j = 0; j < neurons.size(); j++) {
                    if (neurons.get(j).equals((Object) src)) {
                        srcNeuron = ((NeuronWidget) neurons.get(j)).getNeuron();
                    }
                }
            }
            for (int i = 0; i < layers.size(); i++) {
                List<Widget> neurons = layers.get(i).getChildren();
                for (int j = 0; j < neurons.size(); j++) {
                    if (neurons.get(j).equals((Object) trg)) {
                        trgNeuron = ((NeuronWidget) neurons.get(j)).getNeuron();
                    }
                }
            }
            if (srcNeuron != null && trgNeuron != null) {
                nnet.getNeuralNetwork().createConnection(srcNeuron, trgNeuron, 0);
                connection = new Connection(srcNeuron, trgNeuron);
            }
            return ConnectorState.ACCEPT;
        }
        return ConnectorState.REJECT_AND_STOP;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene arg0) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene arg0, Point arg1) {
        return null;
    }

    @Override
    public void createConnection(Widget source, Widget target) {
        NeuronConnectionWidget conn = new NeuronConnectionWidget(nnet.getScene(), connection, (NeuronWidget) source, (NeuronWidget) target);
        conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
        conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
        ((NeuronWidget) source).addConnection(conn);
        ((NeuronWidget) target).addConnection(conn);
        NeuralNetworkGraphScene.getConnectionLayer().addChild(conn);
        nnet.getScene().validate();
    }
}
