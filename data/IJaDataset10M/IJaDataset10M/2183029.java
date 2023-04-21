package org.ufacekit.ui.beanform;

import org.eclipse.core.databinding.DataBindingContext;
import org.ufacekit.core.databinding.common.AttributeDescriptor;
import org.ufacekit.ui.core.form.UIForm;

/**
 * A form implementation for models following the JavaBean specification
 *
 * @since 1.0
 *
 */
public class BeanForm extends UIForm {

    /**
	 * A new bean form with a new default {@link DataBindingContext}
	 *
	 * @since 1.0
	 */
    public BeanForm() {
        this(new DataBindingContext());
    }

    /**
	 * A new bean with the custom databinding context
	 *
	 * @param databindingContext
	 * @since 1.0
	 */
    public BeanForm(DataBindingContext databindingContext) {
        super(databindingContext);
    }

    /**
	 * A helper method to create the binding data for a detail value
	 *
	 * @param name
	 *            the name of property
	 * @param type
	 *            the class type of property
	 * @return the attribute descriptor
	 */
    public AttributeDescriptor detailValue(String name, Class<?> type) {
        return new BeanAttributeDescriptor.DetailValue(name, type);
    }

    /**
	 * A helper method to create the binding data for a detail list
	 *
	 * @param property
	 *            the name of property
	 * @param type
	 *            the class type of property
	 * @return the attribute descriptor
	 */
    public AttributeDescriptor detailList(String property, Class<?> type) {
        return new BeanAttributeDescriptor.DetailList(property, type);
    }
}
