package org.ufacekit.ui.uform;

import org.eclipse.core.databinding.DataBindingContext;
import org.ufacekit.core.databinding.common.AttributeDescriptor;
import org.ufacekit.ui.core.form.UIForm;

/**
 * Create a form for use with an UBean-Model
 *
 * @since 1.0
 *
 */
public class UBeanForm extends UIForm {

    /**
	 * Create a new form with a default databinding context
	 *
	 * @since 1.0
	 */
    public UBeanForm() {
        this(new DataBindingContext());
    }

    /**
	 * Create a new form with a custom databinding context
	 *
	 * @param databindingContext
	 *            the databinding context
	 * @since 1.0
	 */
    public UBeanForm(DataBindingContext databindingContext) {
        super(databindingContext);
    }

    /**
	 * A helper method to create the binding data for a detail value
	 *
	 * @param featureId
	 *            the feature to observe
	 * @param type
	 *            the type of the attribute
	 * @return the attribute descriptor
	 */
    public AttributeDescriptor detailValue(int featureId, Class<?> type) {
        return new UBeanAttributeDescriptor.DetailValue(featureId, type);
    }

    /**
	 * A helper method to create the binding data for a detail list
	 *
	 * @param featureId
	 *            the feature to observe
	 * @param type
	 *            the type of the attribute
	 * @return the attribute descriptor
	 */
    public AttributeDescriptor detailList(int featureId, Class<?> type) {
        return new UBeanAttributeDescriptor.DetailList(featureId, type);
    }
}
