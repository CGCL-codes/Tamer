package dryven.model.validation;

public abstract class AbstractSimpleValidator extends AbstractValidator {

    @Override
    public void validate(ValidationFieldDescription desc, ValidationFormValueCollection form, ValidationReporter reporter) {
        String value = form.getValue(desc.getName());
        validateString(value);
        validate(form.getValue(desc.getName()), reporter);
    }

    public abstract void validate(String value, ValidationReporter reporter);
}
