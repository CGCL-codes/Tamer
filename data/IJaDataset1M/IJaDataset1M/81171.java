package ee.webAppToolkit.parameters;

public class ValidationResult {

    private boolean _validated;

    private Object _originalValue;

    private String _errorMessage;

    public ValidationResult() {
        this(null);
    }

    public ValidationResult(String errorMessage) {
        this(null, errorMessage);
    }

    public ValidationResult(Object originalValue, String errorMessage) {
        _validated = errorMessage == null;
        _originalValue = originalValue;
        setErrorMessage(errorMessage);
    }

    public boolean getValidated() {
        return _validated;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        _validated = errorMessage == null;
        _errorMessage = errorMessage;
    }

    public Object getOriginalValue() {
        return _originalValue;
    }

    public void setOriginalValue(Object originalValue) {
        _originalValue = originalValue;
    }

    public boolean isList() {
        return this instanceof ValidationResultContainer;
    }

    public ValidationResultContainer asList() {
        if (isList()) {
            return (ValidationResultContainer) this;
        } else {
            throw new RuntimeException("Can not return ValidationResult as a list, please use the isList method before calling this method.");
        }
    }

    public boolean isMap() {
        return this instanceof ValidationResults;
    }

    public ValidationResults asMap() {
        if (isMap()) {
            return (ValidationResults) this;
        } else {
            throw new RuntimeException("Can not return ValidationResult as a map, please use the isMap method before calling this method.");
        }
    }

    @Override
    public String toString() {
        return "[ValidationResult(validated=" + getValidated() + (getValidated() ? "" : ", errorMessage=\"" + getErrorMessage() + "\", originalValue=\"" + getOriginalValue() + "\"") + "]";
    }
}
