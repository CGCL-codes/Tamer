package fr.insa.rennes.pelias.pcreator.editors.chains.propertysources;

import java.util.ArrayList;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import fr.insa.rennes.pelias.framework.InputType;
import fr.insa.rennes.pelias.pcreator.Application;
import fr.insa.rennes.pelias.pcreator.editors.chains.models.ChainInputModel;

public class ChainInputPropertySource implements IPropertySource {

    ChainInputModel model;

    public ChainInputPropertySource(ChainInputModel model) {
        this.model = model;
    }

    public Object getEditableValue() {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> propertiesDescriptors = new ArrayList<IPropertyDescriptor>();
        propertiesDescriptors.add(new TextPropertyDescriptor(ChainInputModel.PROPERTY_NAME, "Nom"));
        propertiesDescriptors.add(new TextPropertyDescriptor(ChainInputModel.PROPERTY_TYPE, "Type"));
        if (model.getContent().getType() == InputType.Batch) propertiesDescriptors.add(new TextPropertyDescriptor(ChainInputModel.PROPERTY_MIME, "Type MIME"));
        return (IPropertyDescriptor[]) propertiesDescriptors.toArray(new IPropertyDescriptor[] {});
    }

    public Object getPropertyValue(Object id) {
        if (id.equals(ChainInputModel.PROPERTY_NAME)) return model.getContent().getName();
        if (id.equals(ChainInputModel.PROPERTY_TYPE)) return model.getContent().getType().toString();
        if (id.equals(ChainInputModel.PROPERTY_MIME)) return model.getContent().getMIMEType(Application.getCurrentServiceRepository(), Application.getCurrentChainRepository());
        return null;
    }

    public boolean isPropertySet(Object id) {
        return false;
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
        if (id.equals(ChainInputModel.PROPERTY_NAME)) {
            String oldValue = model.getContent().getName();
            model.getContent().setName((String) value);
            model.getListeners().firePropertyChange(ChainInputModel.PROPERTY_NAME, oldValue, value);
        }
    }
}
