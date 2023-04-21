package org.simbrain.network.interfaces;

import org.simbrain.network.interfaces.RootNetwork.TimeType;

/**
 * <b>SpikingNeuron</b> is the superclass for spiking neuron types (e.g.
 * integrate and fire) with functions common to spiking neurons. For example a
 * boolean hasSpiked field is used in the gui to indicate that this neuron has
 * spiked.
 */
public abstract class SpikingNeuronUpdateRule extends NeuronUpdateRule {

    /** Parent neuron. */
    private Neuron parentNeuron;

    /** Time of last spike.  */
    private double lastSpikeTime;

    /** Whether a spike has occurred in the current time. */
    private boolean hasSpiked;

    @Override
    public void clear(Neuron neuron) {
        super.clear(neuron);
        setLastSpikeTime(0);
    }

    /**
     * {@inheritDoc}
     */
    public TimeType getTimeType() {
        return TimeType.CONTINUOUS;
    }

    /**
     * {@inheritDoc}
     */
    public void init(Neuron neuron) {
        this.parentNeuron = neuron;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void update(Neuron neuron);

    /**
     * @param hasSpiked the hasSpiked to set
     */
    public void setHasSpiked(final boolean hasSpiked) {
        if (hasSpiked == true) {
            lastSpikeTime = parentNeuron.getRootNetwork().getTime();
        }
        this.hasSpiked = hasSpiked;
    }

    /**
     * Whether the neuron has spiked in this instant or not.
     *
     * @return true if the neuron spiked.
     */
    public boolean hasSpiked() {
        return hasSpiked;
    }

    /**
     * @return the lastSpikeTime
     */
    public double getLastSpikeTime() {
        return lastSpikeTime;
    }

    /**
     * @param lastSpikeTime the lastSpikeTime to set
     */
    public void setLastSpikeTime(double lastSpikeTime) {
        this.lastSpikeTime = lastSpikeTime;
    }
}
