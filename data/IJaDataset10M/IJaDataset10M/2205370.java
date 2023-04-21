package org.hibernate.jsr303.tck.tests.constraints.groups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import org.hibernate.jsr303.tck.tests.constraints.groups.DefaultGroupRedefinitionTest.AddressWithDefaultInGroupSequence;
import javax.validation.Validator;

/**
 * ValidatorFactory for
 * {@link DefaultGroupRedefinitionTest#testGroupSequenceContainingDefault()}
 */
public final class GroupSequenceContainingDefaultValidatorFactory extends AbstractGwtValidatorFactory {

    /**
   * Validator for
   * {@link DefaultGroupRedefinitionTest#testGroupSequenceContainingDefault()}
   */
    @GwtValidation(value = { AddressWithDefaultInGroupSequence.class })
    public static interface GroupSequenceContainingDefaultValidator extends Validator {
    }

    @Override
    public AbstractGwtValidator createValidator() {
        return GWT.create(GroupSequenceContainingDefaultValidator.class);
    }
}
