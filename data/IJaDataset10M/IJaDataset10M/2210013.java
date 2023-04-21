package fr.insa.rennes.pelias.pcreator.editors.chains.propertysources;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import fr.insa.rennes.pelias.framework.ExecutionMode;
import fr.insa.rennes.pelias.framework.Service;
import fr.insa.rennes.pelias.framework.ServiceInput;
import fr.insa.rennes.pelias.framework.ServiceOutput;
import fr.insa.rennes.pelias.pcreator.editors.chains.models.ServiceModel;

/**
 * Gère l'affichage de toutes les propriétés d'un service 
 * pour le PropertyView
 * @author Julien
 *
 */
public class ServicePropertySource implements IPropertySource {

    /**
	 * Le service dont on veut visionner les propriétés
	 */
    private ServiceModel model;

    /**
	 * Comme Service est un élément composé de plusieurs autres éléments
	 * on stocke toutes les propertySource qu'on affiche (entrées, sorties, etc.)
	 */
    private List<IPropertySource> properties;

    /**
	 * @param service
	 */
    public ServicePropertySource(ServiceModel model) {
        this.model = model;
        properties = new ArrayList<IPropertySource>();
        Service service = model.getContent();
        for (ServiceInput input : service.getInputs()) {
            ServiceInputPropertySource in = new ServiceInputPropertySource(input);
            properties.add(in);
        }
        for (ServiceOutput output : service.getOutputs()) {
            properties.add(new ServiceOutputPropertySource(output));
        }
        for (Integer code : service.getErrors().keySet()) {
            properties.add(new ErrorPropertySource(code, service.getErrors().get(code)));
        }
    }

    public Object getEditableValue() {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> propertiesDescriptors = new ArrayList<IPropertyDescriptor>();
        propertiesDescriptors.add(new PropertyDescriptor(ServiceModel.PROPERTY_LABEL, "Nom"));
        propertiesDescriptors.add(new PropertyDescriptor(ServiceModel.PROPERTY_DESCRIPTION, "Description"));
        propertiesDescriptors.add(new PropertyDescriptor(ServiceModel.PROPERTY_LOCATION, "Emplacement"));
        propertiesDescriptors.add(new PropertyDescriptor(ServiceModel.PROPERTY_PLATFORM, "Plateforme"));
        propertiesDescriptors.add(new PropertyDescriptor(ServiceModel.PROPERTY_TYPE, "Type"));
        propertiesDescriptors.add(new PropertyDescriptor(ServiceModel.PROPERTY_VERSION, "Version"));
        String[] labels = { ExecutionMode.CompleteBatch.toString(), ExecutionMode.UnitBatch.toString() };
        propertiesDescriptors.add(new ComboBoxPropertyDescriptor(ServiceModel.PROPERTY_EXECUTIONMODE, "Mode d'exécution", labels));
        return (IPropertyDescriptor[]) propertiesDescriptors.toArray(new IPropertyDescriptor[] {});
    }

    public Object getPropertyValue(Object id) {
        if (id.equals(ServiceModel.PROPERTY_LABEL)) return model.getContent().getLabel();
        if (id.equals(ServiceModel.PROPERTY_DESCRIPTION)) return model.getContent().getDescription();
        if (id.equals(ServiceModel.PROPERTY_LOCATION)) return model.getContent().getLocation();
        if (id.equals(ServiceModel.PROPERTY_PLATFORM)) return model.getContent().getPlatform();
        if (id.equals(ServiceModel.PROPERTY_TYPE)) return model.getContent().getType();
        if (id.equals(ServiceModel.PROPERTY_VERSION)) return new String(model.getContent().getVersion().getMajor() + "." + model.getContent().getVersion().getMinor());
        if (id.equals(ServiceModel.PROPERTY_EXECUTIONMODE)) {
            if (model.getCall().getExecutionMode().toString().equals(ExecutionMode.UnitBatch.toString())) return 1; else return 0;
        }
        return null;
    }

    /**
	 * Renvoi si la valeur de la propriété a été changée depuis sa valeur initiale
	 * On ne gère pas les propriétés par défaut, on retourne toujours faux
	 */
    public boolean isPropertySet(Object id) {
        return false;
    }

    /**
	 * Reset la valeur de la propriété à sa valeur par défaut
	 * On ne gère pas les valeurs par défaut, on ne fait rien ici
	 */
    public void resetPropertyValue(Object id) {
    }

    /**
	 * Définit la valeur d'une propriété modifiable
	 * Nos propriétés n'étant pas modifiables via cette interface, on ne fait rien
	 */
    public void setPropertyValue(Object id, Object value) {
        if (id.equals(ServiceModel.PROPERTY_EXECUTIONMODE)) {
            int oldValue;
            if (model.getCall().getExecutionMode().toString().equals(ExecutionMode.UnitBatch.toString())) oldValue = 1; else oldValue = 0;
            if (value.equals(0)) model.setExecutionMode(ExecutionMode.CompleteBatch); else model.setExecutionMode(ExecutionMode.UnitBatch);
            model.getListeners().firePropertyChange(ServiceModel.PROPERTY_EXECUTIONMODE, oldValue, value);
        }
    }
}
