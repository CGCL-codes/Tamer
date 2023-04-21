package org.hibernate.jsr303.tck.tests.validatorfactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import org.hibernate.jsr303.tck.tests.validatorfactory.CustomConstraintValidatorTest.Dummy;
import org.hibernate.jsr303.tck.tests.validatorfactory.CustomConstraintValidatorTest.SecondDummy;
import javax.validation.Validator;

/**
 * {@link AbstractGwtValidatorFactory} implementation that uses
 * {@link com.google.gwt.validation.client.GwtValidation GwtValidation}.
 */
public final class TckTestValidatorFactory extends AbstractGwtValidatorFactory {

    /**
   * Marker Interface for {@link GWT#create(Class)}.
   */
    @GwtValidation(value = { Dummy.class, SecondDummy.class })
    public static interface GwtValidator extends Validator {
    }

    @Override
    public AbstractGwtValidator createValidator() {
        return GWT.create(GwtValidator.class);
    }
}
