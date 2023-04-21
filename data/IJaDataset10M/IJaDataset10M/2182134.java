package com.buschmais.maexo.test.common.mbeans;

public interface StandardMBean {

    /**
	 * Sets the new attribute.
	 * 
	 * @param newAttribute
	 *            The new attribute.
	 */
    public void setAttribute(String newAttribute);

    /**
	 * Returns the attribute.
	 * 
	 * @return the attribute.
	 */
    public String getAttribute();

    /**
	 * This is a dummy method for testing.
	 * 
	 * @param value
	 *            A boolean value.
	 * @return Another boolean value.
	 */
    public boolean operation(boolean value);
}
