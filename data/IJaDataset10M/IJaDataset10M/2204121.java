package net.sourceforge.wildlife.ui.properties.node;

import net.sourceforge.wildlife.core.components.actor.Animal;
import net.sourceforge.wildlife.ui.properties.SectionAdvanced;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * 
 */
public class AnimalPropertySource implements IPropertySource2 {

    /**
	 *
	 */
    private Animal _element;

    /**
	 *
	 */
    private static final String PROPERTY_FAMILY = "Node.family";

    private static final String PROPERTY_CATEGORY = "Node.category";

    private static final String PROPERTY_FUNCTION = "Node.function";

    private static final String PROPERTY_CONDITION = "Node.condition";

    private static final String PROPERTY_TYPE = "Node.type";

    private static final String PROPERTY_NAME = "Node.name";

    /**
	 *
	 */
    private IPropertyDescriptor _descriptors[];

    /**
	 * Creates a new NodePropertySource.
	 *
	 * @param element  the element whose properties this instance represents
	 */
    public AnimalPropertySource(Animal element) {
        super();
        _element = element;
        if (_element != null) {
            int static_fields = 6;
            _descriptors = new IPropertyDescriptor[static_fields];
            _descriptors[0] = new TextPropertyDescriptor(PROPERTY_FAMILY, "Family");
            ((TextPropertyDescriptor) _descriptors[0]).setCategory(SectionAdvanced.CATEGORY_BASIC);
            _descriptors[1] = new TextPropertyDescriptor(PROPERTY_CATEGORY, "Category");
            ((TextPropertyDescriptor) _descriptors[1]).setCategory(SectionAdvanced.CATEGORY_BASIC);
            _descriptors[2] = new TextPropertyDescriptor(PROPERTY_FUNCTION, "Function");
            ((TextPropertyDescriptor) _descriptors[2]).setCategory(SectionAdvanced.CATEGORY_BASIC);
            _descriptors[3] = new TextPropertyDescriptor(PROPERTY_CONDITION, "Condition");
            ((TextPropertyDescriptor) _descriptors[3]).setCategory(SectionAdvanced.CATEGORY_BASIC);
            _descriptors[4] = new TextPropertyDescriptor(PROPERTY_TYPE, "Type");
            ((TextPropertyDescriptor) _descriptors[4]).setCategory(SectionAdvanced.CATEGORY_BASIC);
            _descriptors[5] = new TextPropertyDescriptor(PROPERTY_NAME, "Name");
            ((TextPropertyDescriptor) _descriptors[5]).setCategory(SectionAdvanced.CATEGORY_BASIC);
        }
        initProperties();
    }

    /**
	 *
	 */
    protected void firePropertyChanged(String propName, Object value) {
    }

    /**
	 *
	 */
    private void initProperties() {
    }

    /**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
    public Object getEditableValue() {
        return this;
    }

    /**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    /**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(Object)
	 */
    public Object getPropertyValue(Object name) {
        String buffer[] = ((String) name).split("_");
        if (PROPERTY_FAMILY.equals(buffer[0])) {
            return _element.get_family();
        } else if (PROPERTY_CATEGORY.equals(buffer[0])) {
            return _element.get_category();
        } else if (PROPERTY_FUNCTION.equals(buffer[0])) {
            return _element.get_function();
        } else if (PROPERTY_CONDITION.equals(buffer[0])) {
            return _element.get_condition();
        } else if (PROPERTY_TYPE.equals(buffer[0])) {
            return _element.get_type();
        } else if (PROPERTY_NAME.equals(buffer[0])) {
            return _element.get_name();
        }
        return null;
    }

    /**
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(Object)
	 */
    public boolean isPropertySet(Object id) {
        return false;
    }

    /**
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(Object)
	 */
    public void resetPropertyValue(Object id) {
    }

    /**
	 * @see org.eclipse.ui.views.properties.IPropertySource2#isPropertyResettable(Object)
	 */
    public boolean isPropertyResettable(Object id) {
        return true;
    }

    /**
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(Object, Object)
	 */
    public void setPropertyValue(Object name, Object value) {
        firePropertyChanged((String) name, value);
    }
}
