package org.openwar.victory.network.pack.validation;

import org.openwar.victory.network.pack.Property;

/**
 * Interface for custom property validators.
 * @author Bart van Heukelom
 */
public interface PropertyValidator {

    /**
     * Validate a property.
     * @param property The property.
     * @return Whether it is valid.
     */
    boolean validate(Property property);
}
