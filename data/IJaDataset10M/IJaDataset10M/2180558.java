package com.ryanm.config;

/**
 * Objects that are interested in monitoring the Configurator itself,
 * rather than the values of the variables controlled by the
 * configurator, should implement this interface
 * 
 * @author ryanm
 */
public interface ConfiguratorListener {

    /**
	 * Is called by the configurator when a variable is enabled or
	 * disabled
	 * 
	 * @param name
	 *           The name of the variable
	 * @param enabled
	 *           true if the variable is enabled, false otherwise
	 */
    public void variableStatusChanged(String name, boolean enabled);

    /**
	 * Called when a variable has been added to the configurator
	 * 
	 * @param variable
	 *           The name of the variable, or the Configurator object
	 */
    public void variableAdded(Object variable);

    /**
	 * Called when a variable is removed from the Configurator
	 * 
	 * @param variable
	 *           the variable or subconfigurator that was removed
	 */
    public void variableRemoved(Object variable);

    /**
	 * Called when a variable has its type set
	 * 
	 * @param variable
	 *           The name of the variable that has been typed
	 */
    public void variableTyped(String variable);

    /**
	 * Called when a variable has a range applied to it
	 * 
	 * @param variable
	 *           The name of the variable that has been bounded
	 */
    public void variableRanged(String variable);

    /**
	 * Called when a variable has a description applied to it
	 * 
	 * @param variable
	 *           The name of the variable that was described
	 */
    public void variableDescribed(String variable);

    /**
	 * Called when the configurator is described
	 * 
	 * @param description
	 *           The new description string
	 */
    public void configuratorDescribed(String description);
}
