package org.freeworld.jmultiplug.validation.impl.base;

import java.util.ArrayList;
import java.util.List;
import org.freeworld.jmultiplug.intl.IntlString;
import org.freeworld.jmultiplug.validation.core.ValidationToken;
import org.freeworld.jmultiplug.validation.core.Validator;

public class And implements Validator {

    private IntlString failureMessage = null;

    private Object failureCode = null;

    private IntlString successMessage = null;

    private Object successCode = null;

    private List<Validator> validators = new ArrayList<Validator>();

    private boolean continueFromFailure = false;

    public void setFailureCode(Object failureCode) {
        this.failureCode = failureCode;
    }

    public Object getFailureCode() {
        return failureCode;
    }

    public void setSuccessCode(Object successCode) {
        this.successCode = successCode;
    }

    public Object getSuccessCode() {
        return successCode;
    }

    public void setFailureMessage(IntlString failureMessage) {
        this.failureMessage = failureMessage;
    }

    public IntlString getFailureMessage() {
        return failureMessage;
    }

    public void setSuccessMessage(IntlString successMessage) {
        this.successMessage = successMessage;
    }

    public IntlString getSuccessMessage() {
        return successMessage;
    }

    public void setContinueOnFailure(boolean continueFromFailure) {
        this.continueFromFailure = continueFromFailure;
    }

    public boolean getContinueOnFailure() {
        return continueFromFailure;
    }

    public void addValidator(Validator validator) {
        if (validator != null) validators.add(validator);
    }

    public void setValidators(List<Validator> validators) {
        if (validators != null) for (int i = 0; i < validators.size(); i++) addValidator(validators.get(i));
    }

    public void validate(ValidationContext context, List<ValidationToken> tokens) {
    }
}
