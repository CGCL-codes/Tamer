package com.google.gwt.sample.validation.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.validation.shared.ClientGroup;
import com.google.gwt.sample.validation.shared.Person;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 * {@link AbstractGwtValidatorFactory} that creates the specified {@link GwtValidator}.
 */
public final class SampleValidatorFactory extends AbstractGwtValidatorFactory {

    /**
   * Validator marker for the Validation Sample project. Only the classes listed
   * in the {@link GwtValidation} annotation can be validated.
   */
    @GwtValidation(value = Person.class, groups = { Default.class, ClientGroup.class })
    public interface GwtValidator extends Validator {
    }

    @Override
    public AbstractGwtValidator createValidator() {
        return GWT.create(GwtValidator.class);
    }
}
