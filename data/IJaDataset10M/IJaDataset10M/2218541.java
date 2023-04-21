package org.oxbow.form.validation;

public interface IFormValidationListener<T> {

    void validationCompleted(FormValidationEvent<T> e);
}
