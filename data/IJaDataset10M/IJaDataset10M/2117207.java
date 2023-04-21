package fr.insa.rennes.pelias.pcreator.editors.chains.models;

import org.eclipse.ui.views.properties.IPropertySource;
import fr.insa.rennes.pelias.framework.ServiceOutput;
import fr.insa.rennes.pelias.pcreator.editors.chains.propertysources.ServiceOutputPropertySource;

/**
 * Modèle représentant une sortie de service
 * @author Julien
 *
 */
public class ServiceOutputModel extends ChainComponentOutputModel {

    /**
	 * Les propriétés étroitement liées à l'objet représenté par le modèle.
	 * Constantes utilisées par le PropertyView
	 */
    public static final String PROPERTY_NAME = "ServiceOutputName";

    public static final String PROPERTY_DESCRIPTION = "ServiceOutputDescription";

    public static final String PROPERTY_MIMETYPE = "ServiceOutputMIMEType";

    ServiceOutput output;

    public ServiceOutputModel(ServiceOutput s, int i) {
        output = s;
        index = i;
    }

    public ServiceOutput getContent() {
        return output;
    }

    @Override
    public String getMIMEType() {
        return output.getMIMEType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySource.class) return new ServiceOutputPropertySource(output);
        return null;
    }
}
