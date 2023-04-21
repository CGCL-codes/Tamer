package org.datanucleus.store.db4o;

import org.datanucleus.properties.PersistencePropertyValidator;

/**
 * Validator for persistence properties used by DB4O.
 */
public class DB4OPropertyValidator implements PersistencePropertyValidator {

    /**
     * Validate the specified property.
     * @param name Name of the property
     * @param value Value
     * @return Whether it is valid
     */
    public boolean validate(String name, Object value) {
        if (name == null) {
            return false;
        } else if (name.equals("datanucleus.db4o.outputFile")) {
            if (value instanceof String) {
                return true;
            }
        }
        return false;
    }
}
