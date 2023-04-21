package org.simbrain.network.networks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.simbrain.network.interfaces.Network;
import org.simbrain.network.interfaces.Neuron;
import org.simbrain.network.interfaces.RootNetwork;
import org.simbrain.network.layouts.Layout;
import org.simbrain.network.neurons.PointNeuron;

/**
 * <b>KwtaNetwork</b> implements a k Winner Take All network. The k neurons
 * receiving the most excitatory input will become active. The network
 * determines what level of inhibition across all network neurons will result in
 * those k neurons being active about threshold. From O'Reilley and Munakata,
 * Computational Explorations in Cognitive Neuroscience, p. 110. All page
 * references below are are to this book.
 */
public class KWTA extends Network {

    /** k, that is, number of neurons to win a competition. */
    private int k = 1;

    /**
     * Determines the relative contribution of the k and
     * k+1 node to the threshold conductance.
     */
    private double q = 0.25;

    /**
     * Current inhibitory conductance to be applied to all neurons in the
     * subnetwork.
     */
    private double inhibitoryConductance;

    /**
     * Default constructor.
     */
    public KWTA() {
    }

    /**
     * Copy constructor.
     *
     * @param newRoot new root network
     * @param oldNet old network.
     */
    public KWTA(RootNetwork newRoot, KWTA oldNet) {
        super(newRoot, oldNet);
        setK(oldNet.getK());
    }

    /**
     * Default constructor.
     *
     * @param layout for layout of Neurons.
     * @param k for the number of Neurons in the Kwta Network.
     * @param root reference to RootNetwork.
     */
    public KWTA(final RootNetwork root, final int k, final Layout layout) {
        super();
        this.setRootNetwork(root);
        for (int i = 0; i < k; i++) {
            addNeuron(new Neuron(this, new PointNeuron()));
        }
        layout.layoutNeurons(this);
    }

    /**
     * Update the kwta network. Sort the neurons by excitatory conductance,
     * determine the threshold conductance, apply this conductance to all point
     * neurons, and update the point neurons.
     */
    public void update() {
        sortNeurons();
        setCurrentThresholdCurrent();
        updateAllNeurons();
    }

    /**
     * See p. 101, equation 3.3.
     */
    private void setCurrentThresholdCurrent() {
        double highest = ((PointNeuron) this.getNeuronList().get(k).getUpdateRule()).getInhibitoryThresholdConductance();
        double secondHighest = ((PointNeuron) this.getNeuronList().get(k - 1).getUpdateRule()).getInhibitoryThresholdConductance();
        inhibitoryConductance = secondHighest + q * (highest - secondHighest);
        for (Neuron neuron : this.getNeuronList()) {
            ((PointNeuron) neuron.getUpdateRule()).setInhibitoryConductance(inhibitoryConductance);
        }
    }

    /**
     * Sort neurons by their excitatory conductance. See p. 101.
     */
    private void sortNeurons() {
        Collections.sort(this.getNeuronList(), new PointNeuronComparator());
    }

    /**
     * Used to sort PointNeurons by excitatory conductance.
     */
    class PointNeuronComparator implements Comparator<Neuron> {

        /**
         * {@inheritDoc}
         */
        public int compare(Neuron neuron1, Neuron neuron2) {
            return (int) ((PointNeuron) neuron1.getUpdateRule()).getExcitatoryConductance() - (int) ((PointNeuron) neuron1.getUpdateRule()).getExcitatoryConductance();
        }
    }

    @Override
    public ArrayList<Neuron> getNeuronList() {
        return (ArrayList<Neuron>) super.getNeuronList();
    }

    /**
     * Returns the initial number of neurons.
     *
     * @return the initial number of neurons
     */
    public int getK() {
        return k;
    }

    /**
     * @param k The k to set.
     */
    public void setK(final int k) {
        if (k < 1) {
            this.k = 1;
        } else if (k >= getNeuronCount()) {
            this.k = getNeuronCount() - 1;
        } else {
            this.k = k;
        }
    }
}
