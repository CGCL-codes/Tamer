package org.odlabs.wiquery.core.options;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;

/**
 * $Id: $
 * <p>
 * Wraps a {@link Boolean} to be generated as a JavaScript string.
 * <p>
 * Example:
 * <p>
 * The {@link Boolean} <code>true</code> should be rendered as <code>true</code>
 * </p>
 * </p> </p>
 * 
 * @author Lionel Armanet
 * @author Ernesto Reinaldo Barreiro
 * @since 0.5
 */
public class BooleanOption extends AbstractOption<Boolean> {

    private static final long serialVersionUID = -5938430089917100476L;

    /**
	 * Builds a new instance of {@link BooleanOption}.
	 * 
	 * @param value
	 *            the wrapped {@link Boolean}
	 */
    public BooleanOption(Boolean value) {
        super(value);
    }

    /**
	 * Builds a new instance of {@link BooleanOption}.
	 * 
	 * @param value
	 *            the wrapped {@link String}
	 */
    public BooleanOption(IModel<Boolean> value) {
        super(value);
    }

    @Override
    public String toString() {
        Boolean value = getValue();
        return value != null ? Boolean.toString(value) : null;
    }

    public IModelOption<Boolean> wrapOnAssignment(Component component) {
        if (getModel() instanceof IComponentAssignedModel<?>) return new BooleanOption(((IComponentAssignedModel<Boolean>) getModel()).wrapOnAssignment(component));
        return this;
    }
}
