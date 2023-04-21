package jaxlib.swing.binding;

import javax.annotation.Nullable;

/**
 * Validator that will be invoked before a value of a component will be transferred to the target bean.
 * <p>
 * For each bound component the validator will be resolved by looking for an ancestor that either is
 * implementing this interface or having a {@link javax.swing.JComponent#getClientProperty(Object) client property}
 * where the key is {@code ComponentBindingValidator.class}.
 * </p><p>
 * Please note that there is no default implementation of this interface - the requirements are too different from
 * application to application.
 * </p>
 * 
 * @see ComponentPropertyBinding#getComponentBindingValidator()
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: ComponentBindingValidator.java 2885 2011-03-01 04:12:21Z joerg_wassmer $
 */
public interface ComponentBindingValidator {

    /**
   * This method will be invoked if the conversion of a component's value to the property type of the target bean
   * failed.
   *
   * @param binding
   *  the property binding, where the left bean is the target, and the right is the source component.
   * @param componentValue
   *  the value of the component,never null.
   *
   * @since JaXLib 1.0
   */
    public <B> void conversionFailed(ComponentPropertyBinding<B, ?> binding, Object componentValue, Exception cause);

    /**
   * Apply validations and decide whether to call the property setter of the target bean.
   *
   * @param binding
   *  the property binding, where the left bean is the target, and the right is the source component.
   * @param v
   *  the property value for the target bean, already converted to the required type.
   *
   * @return
   *  whether to call the property setter of the target bean.
   *
   * @since JaXLib 1.0
   */
    public <B> boolean validate(ComponentPropertyBinding<B, ?> binding, @Nullable Object v);
}
