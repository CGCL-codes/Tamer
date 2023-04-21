package org.ufacekit.ui.beanform;

import java.beans.PropertyChangeEvent;
import org.eclipse.core.databinding.DataBindingContext;
import org.ufacekit.core.databinding.common.AttributeDescriptor;
import org.ufacekit.ui.core.form.UIForm;

/**
 * A form implementation for models following the POJOs (plain old java objects)
 * that conform to idea of an object with getters and setters but does not
 * provide {@link PropertyChangeEvent property change events} on change. This
 * {@link UIForm} is identical to {@link BeanForm} except for this fact.
 *
 * @since 1.0
 *
 */
public class PojoForm extends UIForm {

    /**
	 * A new pojo form with a new default {@link DataBindingContext}
	 *
	 * @since 1.0
	 */
    public PojoForm() {
        this(new DataBindingContext());
    }

    /**
	 * A new pojo with the custom databinding context
	 *
	 * @param databindingContext
	 * @since 1.0
	 */
    public PojoForm(DataBindingContext databindingContext) {
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
        return new PojoAttributeDescriptor.DetailValue(name, type);
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
        return new PojoAttributeDescriptor.DetailList(property, type);
    }
}
