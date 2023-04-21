package be.bzbit.framework.domain.webflow.validator;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import be.bzbit.framework.domain.model.DomainObject;

/**
 * Inherit this class if you want a WebFlow validator POJO to use Hibernate
 * Validator
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy: Jurgen.Lust $
 *
 * @version $LastChangedRevision: 3 $
 */
public abstract class AbstractDomainObjectValidator<T extends DomainObject> {

    /**
     * Validates the given DomainObject and creates JSF messages for the errors
     *
     * @param <T> the type of the DomainObject
     * @param object the object to validate
     * @return if validation is successful
     */
    @SuppressWarnings("unchecked")
    protected boolean validate(final ValidationContext validationContext, final T object) {
        final MessageContext messageContext = validationContext.getMessageContext();
        final ClassValidator validator = getValidator(object.getClass());
        InvalidValue[] invalidValues = validator.getInvalidValues(object);
        for (final InvalidValue invalidValue : invalidValues) {
            reportInvalidValue(invalidValue, messageContext);
        }
        return invalidValues.length == 0;
    }

    @SuppressWarnings("unchecked")
    private ClassValidator getValidator(final Class cl) {
        return new ClassValidator(cl);
    }

    private void reportInvalidValue(final InvalidValue invalidValue, final MessageContext messageContext) {
        messageContext.addMessage(new MessageBuilder().error().source(invalidValue.getPropertyName()).defaultText(invalidValue.getPropertyName() + ": " + invalidValue.getMessage()).build());
    }

    /**
     * Validates the given properties of the DomainObject and creates JSF messages for the errors
     *
     * @param <T> the type of the DomainObject
     * @param object the object to validate
     * @param propertyNames the names of the properties to validate
     * @return if validation is successful
     */
    @SuppressWarnings("unchecked")
    protected boolean validate(final ValidationContext validationContext, final T object, String... propertyNames) {
        final ClassValidator validator = getValidator(object.getClass());
        boolean isValid = true;
        for (final String propertyName : propertyNames) {
            if (!validate(validator, validationContext, object, propertyName)) {
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Validates the given property of the DomainObject and creates JSF messages for the errors
     *
     * @param <T> the type of the DomainObject
     * @param object the object to validate
     * @param propertyName the name of the property to validate
     * @return if validation is successful
     */
    @SuppressWarnings("unchecked")
    private boolean validate(final ClassValidator validator, final ValidationContext validationContext, final T object, final String propertyName) {
        final MessageContext messageContext = validationContext.getMessageContext();
        InvalidValue[] invalidValues = validator.getInvalidValues(object, propertyName);
        for (final InvalidValue invalidValue : invalidValues) {
            reportInvalidValue(invalidValue, messageContext);
        }
        return invalidValues.length == 0;
    }
}
