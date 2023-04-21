package org.neuroph.netbeans.visual.widgets;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Neuron;

/**
 *
 * @author Kasia
 */
public class NeuronWidget extends IconNodeWidget {

    private Neuron neuron;

    private List<ConnectionWidget> connections;

    public NeuronWidget(Scene scene, Neuron neuron) {
        super(scene);
        connections = new ArrayList<ConnectionWidget>();
        this.neuron = neuron;
    }

    public Neuron getNeuron() {
        return neuron;
    }

    public void addConnection(ConnectionWidget cw) {
        connections.add(cw);
    }

    public void removeAllConnections() {
        connections.clear();
    }

    public List<ConnectionWidget> getConnections() {
        return connections;
    }
}
