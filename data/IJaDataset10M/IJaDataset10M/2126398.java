package com.google.gwt.validation.client;

import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import com.google.gwt.validation.client.impl.GwtValidationContext;
import java.util.Set;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;

/**
 * Tests for {@link GwtValidatorContext}.
 */
public class GwtValidatorContextTest extends ValidationClientGwtTestCase {

    private final class DummyGwtValidatorFactory extends AbstractGwtValidatorFactory {

        @Override
        public AbstractGwtValidator createValidator() {
            return new DummyValidator();
        }
    }

    private static class DummyValidator extends AbstractGwtValidator {

        @Override
        public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validate(GwtValidationContext<T> context, Object object, Class<?>... groups) throws ValidationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected ConstraintValidatorFactory getConstraintValidatorFactory() {
            return super.getConstraintValidatorFactory();
        }

        @Override
        protected MessageInterpolator getMessageInterpolator() {
            return super.getMessageInterpolator();
        }

        @Override
        protected TraversableResolver getTraversableResolver() {
            return super.getTraversableResolver();
        }
    }

    private ValidatorFactory validatorFactory;

    private ValidatorContext validatorContext;

    public void testCustom() throws Exception {
        final TraversableResolver traversableResolver = new GwtTraversableResolver();
        final ConstraintValidatorFactory constraintValidatorFactory = new GwtConstraintValidatorFactory();
        final MessageInterpolator messageInterpolator = new GwtMessageInterpolator();
        validatorContext.constraintValidatorFactory(constraintValidatorFactory).messageInterpolator(messageInterpolator).traversableResolver(traversableResolver);
        assertContext(traversableResolver, constraintValidatorFactory, messageInterpolator);
    }

    public void testDefault() throws Exception {
        assertContext(validatorFactory.getTraversableResolver(), validatorFactory.getConstraintValidatorFactory(), validatorFactory.getMessageInterpolator());
    }

    public void testNull() throws Exception {
        validatorContext.constraintValidatorFactory(null).messageInterpolator(null).traversableResolver(null);
        assertContext(validatorFactory.getTraversableResolver(), validatorFactory.getConstraintValidatorFactory(), validatorFactory.getMessageInterpolator());
    }

    public void testReset() throws Exception {
        final TraversableResolver traversableResolver = new GwtTraversableResolver();
        final ConstraintValidatorFactory constraintValidatorFactory = new GwtConstraintValidatorFactory();
        final MessageInterpolator messageInterpolator = new GwtMessageInterpolator();
        validatorContext.constraintValidatorFactory(constraintValidatorFactory).messageInterpolator(messageInterpolator).traversableResolver(traversableResolver);
        validatorContext.constraintValidatorFactory(null).messageInterpolator(null).traversableResolver(null);
        assertContext(validatorFactory.getTraversableResolver(), validatorFactory.getConstraintValidatorFactory(), validatorFactory.getMessageInterpolator());
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        validatorFactory = new DummyGwtValidatorFactory();
        validatorContext = validatorFactory.usingContext();
    }

    private void assertContext(final TraversableResolver traversableResolver, final ConstraintValidatorFactory constraintValidatorFactory, final MessageInterpolator messageInterpolator) {
        final DummyValidator validator = getDummyValidatorFromContext();
        assertSame(messageInterpolator, validator.getMessageInterpolator());
        assertSame(constraintValidatorFactory, validator.getConstraintValidatorFactory());
        assertSame(traversableResolver, validator.getTraversableResolver());
    }

    private DummyValidator getDummyValidatorFromContext() {
        return ((DummyValidator) validatorContext.getValidator());
    }
}
