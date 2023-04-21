package uk.ac.ed.ph.jqtiplus.validation;

import uk.ac.ed.ph.jqtiplus.control.ValidationContext;
import java.io.Serializable;

/**
 * Every object which supports validation must implement this interface.
 * 
 * @author Jiri Kajaba
 */
public interface Validatable extends Serializable {

    /**
     * Validates this object.
     * @param context TODO
     *
     * @return validation result
     */
    public ValidationResult validate(ValidationContext context);
}
