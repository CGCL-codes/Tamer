package component_interfaces.semanticmm4u.realization.compositor.provided;

import component_interfaces.semanticmm4u.realization.compositor.realization.ITemporalOperator;

/**
 * A delay. Describes a simple delay.
 * 
 */
public interface IDelay extends ITemporalOperator {

    /**
	 * Gets the current delay.
	 * 
	 * @return the current delay
	 */
    public int getDelay();

    /**
	 * Sets the delay.
	 * 
	 * @param myDelay
	 *            the new delay
	 */
    public void setDelay(int myDelay);
}
