package org.middleheaven.core.bootstrap.activation;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows for {@link ServiceActivator}s to be added and removed from it as an collection. Then when scanned loads the activators present.
 */
public class SetActivatorScanner extends AbstractActivatorScanner {

    private Map<String, Class<? extends ServiceActivator>> units = new HashMap<String, Class<? extends ServiceActivator>>();

    /**
	 * 
	 * Constructor.
	 */
    public SetActivatorScanner() {
    }

    /**
	 * Adds and {@link ServiceActivator} class.
	 * 
	 * @param activatorType the class of the activator.
	 * @return this object.
	 */
    public SetActivatorScanner addActivator(Class<? extends ServiceActivator> activatorType) {
        if (!this.units.containsKey(activatorType.getName())) {
            this.units.put(activatorType.getName(), activatorType);
        }
        return this;
    }

    /**
	 * Removed and {@link ServiceActivator} class.
	 * 
	 * @param activatorType the class of the activator.
	 * @return  this object.
	 */
    public SetActivatorScanner removeActivator(Class<? extends ServiceActivator> activatorType) {
        this.units.remove(activatorType.getName());
        return this;
    }

    @Override
    public void scan() {
        for (Class<? extends ServiceActivator> activatorType : units.values()) {
            fireDeployableFound(activatorType);
        }
    }

    public boolean contains(Class<? extends ServiceActivator> activatorType) {
        return units.containsKey(activatorType.getName());
    }

    public String toString() {
        return units.toString();
    }
}
