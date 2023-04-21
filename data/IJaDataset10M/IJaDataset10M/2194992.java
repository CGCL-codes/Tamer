package org.neuroph.contrib;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.plugins.LabelsPlugin;

/**
 *<pre>
 * Interactive Activation Controller neural network.
 * Still under development, at the moment contains hard-coded Jets and Sharks example.
 * See http://www.itee.uq.edu.au/~cogs2010/cmc/chapters/IAC/ for more info
 *</pre>
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class IACNetwork extends NeuralNetwork {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new Interactive Activation Neural Network
     */
    public IACNetwork() {
        this.createNetwork();
    }

    private void createNetwork() {
        this.addLayer(new Layer());
        this.addLayer(new Layer());
        this.addLayer(new Layer());
        this.addLayer(new Layer());
        this.addLayer(new Layer());
        this.addLayer(new Layer());
        this.addLayer(new Layer());
        LabelsPlugin labels = (LabelsPlugin) this.getPlugin("LabelsPlugin");
        Layer layer = this.getLayerAt(0);
        IACNeuron neuron = new IACNeuron();
        labels.setLabel(neuron, "Art");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Rick");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Sam");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Ralph");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Lance");
        layer.addNeuron(neuron);
        ConnectionFactory.fullConnect(layer, -0.3);
        layer = this.getLayerAt(1);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Jets");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Sharks");
        layer.addNeuron(neuron);
        ConnectionFactory.fullConnect(layer, -0.3);
        layer = this.getLayerAt(2);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "20's");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "30's");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "40's");
        layer.addNeuron(neuron);
        ConnectionFactory.fullConnect(layer, -0.3);
        layer = this.getLayerAt(3);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "J.H.");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "H.S.");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "College");
        layer.addNeuron(neuron);
        ConnectionFactory.fullConnect(layer, -0.3);
        layer = this.getLayerAt(4);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Single");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Married");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Divorced");
        layer.addNeuron(neuron);
        ConnectionFactory.fullConnect(layer, -0.3);
        layer = this.getLayerAt(5);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Pusher");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Burglar");
        layer.addNeuron(neuron);
        neuron = new IACNeuron();
        labels.setLabel(neuron, "Bookie");
        layer.addNeuron(neuron);
        ConnectionFactory.fullConnect(layer, -0.3);
        layer = this.getLayerAt(6);
        neuron = new IACNeuron();
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(2), 0.1);
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(0), 0.1);
        this.createConnection(this.getLayerAt(0).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(2).getNeuronAt(2), neuron, 0.1);
        this.createConnection(this.getLayerAt(3).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(4).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(5).getNeuronAt(0), neuron, 0.1);
        neuron = new IACNeuron();
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(1), 0.1);
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(1), 0.1);
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(1), 0.1);
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(1), 0.1);
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(2), 0.1);
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(1), 0.1);
        this.createConnection(this.getLayerAt(0).getNeuronAt(1), neuron, 0.1);
        this.createConnection(this.getLayerAt(1).getNeuronAt(1), neuron, 0.1);
        this.createConnection(this.getLayerAt(2).getNeuronAt(1), neuron, 0.1);
        this.createConnection(this.getLayerAt(3).getNeuronAt(1), neuron, 0.1);
        this.createConnection(this.getLayerAt(4).getNeuronAt(2), neuron, 0.1);
        this.createConnection(this.getLayerAt(5).getNeuronAt(1), neuron, 0.1);
        neuron = new IACNeuron();
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(2), 0.1);
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(2), 0.1);
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(2), 0.1);
        this.createConnection(this.getLayerAt(0).getNeuronAt(2), neuron, 0.1);
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(2).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(3).getNeuronAt(2), neuron, 0.1);
        this.createConnection(this.getLayerAt(4).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(5).getNeuronAt(2), neuron, 0.1);
        neuron = new IACNeuron();
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(3), 0.1);
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(1), 0.1);
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(0), 0.1);
        this.createConnection(this.getLayerAt(0).getNeuronAt(3), neuron, 0.1);
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(2).getNeuronAt(1), neuron, 0.1);
        this.createConnection(this.getLayerAt(3).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(4).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(5).getNeuronAt(0), neuron, 0.1);
        neuron = new IACNeuron();
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(4), 0.1);
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(0), 0.1);
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(1), 0.1);
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(1), 0.1);
        this.createConnection(this.getLayerAt(0).getNeuronAt(4), neuron, 0.1);
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(2).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(3).getNeuronAt(0), neuron, 0.1);
        this.createConnection(this.getLayerAt(4).getNeuronAt(1), neuron, 0.1);
        this.createConnection(this.getLayerAt(5).getNeuronAt(1), neuron, 0.1);
        ConnectionFactory.fullConnect(layer, -0.3);
        labels.setLabel(this, "IAC Test");
        this.setInputNeurons(this.getLayerAt(0).getNeurons());
    }
}
