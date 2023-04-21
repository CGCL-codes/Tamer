package com.jgoodies.validation.util;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;

/**
 * A default implementation of the {@link ValidationResultModel} interface
 * that holds a ValidationResult.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.8 $
 */
public class DefaultValidationResultModel extends AbstractValidationResultModel {

    /**
     * Holds this model's validation result.
     */
    private ValidationResult validationResult;

    /**
     * Constructs a DefaultValidationResultModel initialized
     * with an empty validation result.
     */
    public DefaultValidationResultModel() {
        validationResult = ValidationResult.EMPTY;
    }

    /**
     * Returns this model's validation result.
     *
     * @return the current validation result
     */
    public final ValidationResult getResult() {
        return validationResult;
    }

    /**
     * Sets a new validation result and notifies all registered listeners
     * about changes of the result itself and the properties for severity,
     * errors and messages. This method is typically invoked at the end of
     * the <code>#validate()</code> method.
     *
     * @param newResult  the validation result to be set
     *
     * @throws NullPointerException if the new result is {@code null}
     *
     * @see #getResult()
     * @see ValidationResultModelContainer#setResult(ValidationResult)
     */
    public void setResult(ValidationResult newResult) {
        if (newResult == null) {
            throw new NullPointerException("The new result must not be null.");
        }
        ValidationResult oldResult = getResult();
        validationResult = newResult;
        firePropertyChanges(oldResult, newResult);
    }
}
