package net.sourceforge.wildlife.ui.properties.pop;

import net.sourceforge.wildlife.core.WildLifeCorePlugin;
import net.sourceforge.wildlife.core.conf.pop.XMLPopAnimal;
import net.sourceforge.wildlife.core.conf.pop.XMLPopFood;
import net.sourceforge.wildlife.core.conf.pop.XMLPopulation;
import net.sourceforge.wildlife.ui.properties.SectionAdvanced;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * 
 */
public class XMLPopulationPropertySource implements IPropertySource2 {

    /**
	 *
	 */
    private XMLPopulation _element;

    /**
	 *
	 */
    private static final String PROPERTY_ANIMAL = "XMLPop.animal";

    private static final String PROPERTY_FOOD = "XMLPop.food";

    /**
	 *
	 */
    private IPropertyDescriptor _descriptors[];

    /**
	 * Creates a new EnvironmentFilePropertySource.
	 *
	 * @param element  the element whose properties this instance represents
	 */
    @SuppressWarnings("unused")
    public XMLPopulationPropertySource(IFile element) {
        super();
        _element = WildLifeCorePlugin.getDefault().getPopulation(element);
        if (_element != null) {
            int size = _element.card_animals() + _element.card_food();
            _descriptors = new IPropertyDescriptor[size];
            int index = 0;
            for (XMLPopAnimal env : _element.get_all_animals()) {
                PropertyDescriptor descriptor = new PropertyDescriptor(PROPERTY_ANIMAL + '_' + index, "Animal");
                descriptor.setCategory(SectionAdvanced.CATEGORY_COMPOSITE);
                _descriptors[index++] = descriptor;
            }
            for (XMLPopFood link : _element.get_all_food()) {
                PropertyDescriptor descriptor = new PropertyDescriptor(PROPERTY_FOOD + '_' + (index - _element.card_animals()), "Food");
                descriptor.setCategory(SectionAdvanced.CATEGORY_COMPOSITE);
                _descriptors[index++] = descriptor;
            }
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
        if (PROPERTY_ANIMAL.equals(buffer[0])) {
            return new XMLPopAnimalPropertySource(_element.get_animals(Integer.parseInt(buffer[1])), Integer.parseInt(buffer[1]));
        } else if (PROPERTY_FOOD.equals(buffer[0])) {
            return new XMLPopFoodPropertySource(_element.get_food(Integer.parseInt(buffer[1])), Integer.parseInt(buffer[1]));
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
