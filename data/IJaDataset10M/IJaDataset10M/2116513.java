package org.qtitools.qti.validation;

import java.io.Serializable;

/**
 * Every object which supports validation must implement this interface.
 * 
 * @author Jiri Kajaba
 */
public interface Validatable extends Serializable {

    /**
	 * Validates this object.
	 *
	 * @return validation result
	 */
    public ValidationResult validate();
}
