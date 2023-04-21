package net.sourceforge.annovalidator.validation.exception;

import java.text.MessageFormat;

public class ValidatorClassException extends RuntimeException {

    private static final long serialVersionUID = 7778157803955403959L;

    private static final String MESSAGE_TEMPLATE = "Could not instantiate a validator of class {0}: {1}";

    private String message;

    public ValidatorClassException(Class<?> validatorClass, Exception e) {
        super(validatorClass.getCanonicalName(), e);
        message = MessageFormat.format(MESSAGE_TEMPLATE, new Object[] { validatorClass.getSimpleName(), e.getClass().getSimpleName() });
    }

    @Override
    public String getMessage() {
        return message;
    }
}
