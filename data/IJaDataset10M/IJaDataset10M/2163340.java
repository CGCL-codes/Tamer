package at.gp.web.jsf.extval.beanval.form.validation;

import javax.validation.ConstraintValidatorContext;

/**
 * @author Gerhard Petracek
 */
public abstract class ViolationMessageAwareFormBean implements FormBean {

    protected abstract String getViolationMessageDescriptor();

    protected abstract boolean isValid();

    public final boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = isValid();
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(getViolationMessageDescriptor()).addConstraintViolation();
        }
        return isValid;
    }
}
