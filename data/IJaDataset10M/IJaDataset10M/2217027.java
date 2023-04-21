package org.gwtoolbox.widget.client.form.validation;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Uri Boness
 */
public class ValidationResult {

    private List<String> errorMessages;

    public ValidationResult() {
        this((List<String>) null);
    }

    public ValidationResult(String errorMessage) {
        this();
        addErrorMessage(errorMessage);
    }

    public ValidationResult(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public boolean isValid() {
        return errorMessages == null;
    }

    public void addErrorMessage(String message) {
        if (errorMessages == null) {
            errorMessages = new ArrayList<String>();
        }
        errorMessages.add(message);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void merge(ValidationResult otherResult) {
        if (otherResult.isValid()) {
            return;
        }
        if (errorMessages == null) {
            errorMessages = new ArrayList<String>();
        }
        errorMessages.addAll(otherResult.errorMessages);
    }
}
