package it.bfp.sark.companion;

import it.bfp.sark.common.ResourceUtils;
import it.bfp.sark.input.InputException;
import it.bfp.sark.input.impl.InputImpl;
import it.bfp.sark.model.impl.ModelImpl;
import java.net.URI;
import java.util.Map;

/**
 * This object is configured from the SarK cartridge
 * XML file.
 *
 * @author BFP 
 */
public class CompanionInput extends InputImpl {

    /**
     * Memorizza le proprieta'
     */
    Map<String, String> properties = null;

    @Override
    public void init(Map<String, String> props) throws InputException {
        try {
            this.properties = props;
            setModel(props.get("databaseName"));
        } catch (CompanionException e) {
            throw new InputException("Load xml error. " + e.getMessage());
        }
    }

    /**
     * Sets the <code>mappingsUri</code> which is the URI to the SQL types to model type mappings.
     * 
     * @param xmlUri the uri of the model to set.
     * @throws CompanionException
     */
    private void setModel(String xmlUri) throws CompanionException {
        URI uri = ResourceUtils.toUri(xmlUri);
        super.setModel((ModelImpl) XmlModel.getInstance(uri).getModel());
        super.getModel().setName(this.properties.get("connectionName"));
    }
}
