package org.jreliability.tutorial.boiler;

/**
 * The {@code Pump} models a pump that is used to pump water from the {@code
 * Boiler} to its destination.
 * 
 * @author glass
 * 
 */
public class Pump extends BoilerComponent {

    /**
	 * Constructs a {@code Pump} with a given name.
	 * 
	 * @param name
	 *            the name of the pump
	 */
    public Pump(String name) {
        super(name);
    }
}
