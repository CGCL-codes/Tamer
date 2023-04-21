package net.sourceforge.coffea.editors.propertysource;

import net.sourceforge.coffea.uml2.model.IElementService;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/** 
 * UML element service as a source of selection
 * @param <E>
 * Type of service for the selected UML element
 */
public abstract class ElementPropertySource<E extends IElementService> implements IPropertySource {

    /** Service for the edited UML element */
    protected E editedElementService;

    /**
	 * Construction of an UML element handler source of selection
	 * @param elementHandler
	 * Selected element handler
	 */
    protected ElementPropertySource(E elementHandler) {
        editedElementService = elementHandler;
    }

    public Object getEditableValue() {
        return editedElementService.getSimpleName();
    }

    public abstract IPropertyDescriptor[] getPropertyDescriptors();

    public abstract Object getPropertyValue(Object id);

    public abstract boolean isPropertySet(Object id);

    public abstract void resetPropertyValue(Object id);

    public abstract void setPropertyValue(Object id, Object value);
}
