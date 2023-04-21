package de.swm.commons.mobile.client.validation.impl;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.ui.HasValue;
import de.swm.commons.mobile.client.validation.IErrorOutputElement;
import de.swm.commons.mobile.client.validation.IValidator;

/**
 * Hleper class for validation.
 * @author wiese.daniel
 * <br>copyright (C) 2012, SWM Services GmbH
 *
 * @param <T> the type of the field
 */
public class ValidationHelper<T> {

    private List<IValidator<T>> validators = new ArrayList<IValidator<T>>();

    private List<IErrorOutputElement> validatorErrorOutputs = new ArrayList<IErrorOutputElement>();

    /**
	 * Adds a validator to this Ui-Widget
	 * 
	 * @param validatorToAdd
	 *            the validtors to add
	 */
    public void addValidator(IValidator<T> validatorToAdd) {
        this.validators.add(validatorToAdd);
    }

    /**
	 * Add an error output element where error messages are displayed
	 * 
	 * @param errorOutputToAdd
	 *            the error output element to add
	 */
    public void addErrorOuptut(IErrorOutputElement errorOutputToAdd) {
        this.validatorErrorOutputs.add(errorOutputToAdd);
    }

    /**
	 * Will remove all registered validators.
	 */
    public void removeAllValidators() {
        this.validators.clear();
    }

    /**
	 * Executes the validator.
	 * @param owner the owning field
	 * @return true if valid.
	 */
    public boolean validate(HasValue<T> owner) {
        boolean hasErrors = false;
        if (!validators.isEmpty()) {
            for (IValidator<T> validator : validators) {
                final String errorMessage = validator.validate(owner);
                if (errorMessage != null) {
                    hasErrors = true;
                    for (IErrorOutputElement validatorErrorOutput : this.validatorErrorOutputs) {
                        validatorErrorOutput.appendErrorMessage(owner, errorMessage);
                    }
                } else {
                    for (IErrorOutputElement validatorErrorOutput : this.validatorErrorOutputs) {
                        validatorErrorOutput.clearErrorMessage(owner);
                    }
                }
            }
        }
        return hasErrors;
    }
}
