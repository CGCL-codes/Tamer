package org.encog.neural.som.training.basic.neighborhood;

/**
 * A very simple neighborhood function that will return 1.0 (full effect) for
 * the winning neuron, and 0.0 (no change) for everything else.
 * 
 * @author jheaton
 * 
 */
public class NeighborhoodSingle implements NeighborhoodFunction {

    /**
	 * Determine how much the current neuron should be affected by training
	 * based on its proximity to the winning neuron.
	 * 
	 * @param currentNeuron
	 *            THe current neuron being evaluated.
	 * @param bestNeuron
	 *            The winning neuron.
	 * @return The ratio for this neuron's adjustment.
	 */
    public double function(final int currentNeuron, final int bestNeuron) {
        if (currentNeuron == bestNeuron) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    /**
	 * The radius for this neighborhood function is always 1.
	 * @return The radius.
	 */
    public double getRadius() {
        return 1;
    }

    /**
	 * Set the radius.  This type does not use a radius, so this has no effect.
	 * 
	 * @param radius
	 *            The radius.
	 */
    public void setRadius(final double radius) {
    }
}
