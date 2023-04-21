package skycastle.spikes.pojomodel.agent;

import skycastle.util.propertyaccess.metadata.PropertyMetadatas;
import java.io.Serializable;

/**
 * Metadata about an action, can be used when creating an UI that contains a widget for invoking it.
 * <p/>
 * Can be sent from the server to the client, so it should be serializable and not hold any references to outside classes.
 *
 * @author Hans H�ggstr�m
 */
public interface ActionMetadata extends Serializable {

    /**
     * @return unique identifier of the action.
     */
    String getActionIdentifier();

    /**
     * @return metadata of the action parameters.
     */
    PropertyMetadatas getParameterMetadatas();
}
