package org.mvc.validators;

public abstract class AbstractValidator implements Validator {

    private String message;

    public String getMessage() {
        return this.message;
    }

    public boolean validate(Object value) {
        boolean result = this._validate(value);
        if (!result) {
            this.message = this._getMessage();
        }
        return result;
    }

    protected abstract String _getMessage();

    protected abstract boolean _validate(Object value);
}
