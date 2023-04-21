package uips.events;

import java.util.Map;
import uips.models.ModelUpdateType;

/**
 * Instance of this class is delivered to event handler, by that instance is
 * possible to sent updates to all connected clients, ivalidate models for all
 * connected clients and get properties from models common for all clients.
 * <br><br>
 * Based on Miroslav Macik's C# version of UIProtocolServer
 *
 * @author Miroslav Macik (macikm1@fel.cvut.cz, CTU Prague, FEE)
 * @author Jindrich Basek, (basekjin@fel.cvut.cz, CTU Prague, FEE)
 */
public interface IServer {

    /**
     * Returns map of client objects for clients of selected class
     *
     * @param filter class of clients
     * @return map of client objects for clients of selected class
     */
    public Map<String, IClient> SelectClients(String filter);

    /**
     * Write message to log from event handler
     *
     * @param message Message to log
     */
    public void LogMessage(String message);

    /**
     * Update one model property of base model variant with given ID, model
     * update type is set to partial
     *
     * @param modelId id of updated model
     * @param name name of property
     * @param value valule of property
     * @return Map of updated properties
     * (key - property name, value - property value)
     */
    public Map<String, String> UpdateModel(String modelId, String name, String value);

    /**
     * Update one model property of base model variant with given ID,
     * model update type can be set to this update
     *
     * @param modelId id of updated model
     * @param name name of property
     * @param value valule of property
     * @param updateType update type of model
     * (value from <code>ModelUpdateType</code> enumeration)
     * @return Map of updated properties (key - property name,
     * value - property value)
     */
    public Map<String, String> UpdateModel(String modelId, String name, String value, ModelUpdateType updateType);

    /**
     * Returns value of model property with given name from base model variant
     * with given id
     *
     * @param modelId id of model
     * @param name name of the property
     * @return value of property if found otherwise null
     */
    public String GetModelValue(String modelId, String name);

    /**
     * Returns key of model property with given name from base model variant
     * with given id
     *
     * @param modelId id of model
     * @param name name of the property
     * @return key of property if found otherwise null
     */
    public String GetModelKey(String modelId, String name);

    /**
     * Returns map of values of model properties with given name prefix
     * from base model variant with given id
     *
     * @param modelId id of model
     * @param namePrefix prefix of model property name
     * @return map of values of model properties with given name prefix
     */
    public Map<String, String> GetModelValues(String modelId, String namePrefix);

    /**
     * Ivalidate base model variant with given id
     *
     * @param modelId model id
     */
    public void InvalidateModel(String modelId);

    /**
     * Update one model property of model variant with given ID and
     * model variant value, model update type is set to partial
     *
     * @param modelId id of updated model
     * @param modelVariant model variant values
     * @param name name of property
     * @param value valule of property
     * @return Map of updated properties (key - property name, value - property value
     */
    public Map<String, String> UpdateModel(String modelId, Map<String, String> modelVariant, String name, String value);

    /**
     * Update one model property of model variant with given ID and model
     * variant value, model update type can be set to this update
     *
     * @param modelId id of updated model
     * @param modelVariant model variant values
     * @param name name of property
     * @param value valule of property
     * @param updateType update type of model
     * (value from <code>ModelUpdateType</code> enumeration)
     * @return Map of updated properties (key - property name,
     * value - property value)
     */
    public Map<String, String> UpdateModel(String modelId, Map<String, String> modelVariant, String name, String value, ModelUpdateType updateType);

    /**
     * Returns value of model property with given name from model variant with
     * given ID and model variant value
     *
     * @param modelId id of model
     * @param modelVariant model variant values
     * @param name name of the property
     * @return value of property if found otherwise null
     */
    public String GetModelValue(String modelId, Map<String, String> modelVariant, String name);

    /**
     * Returns key of model property with given name from model variant with
     * given ID and model variant value
     *
     * @param modelId id of model
     * @param modelVariant model variant values
     * @param name name of the property
     * @return key of property if found otherwise null
     */
    public String GetModelKey(String modelId, Map<String, String> modelVariant, String name);

    /**
     * Returns map of values of model properties with given name prefix
     * from model variant with given ID and model variant value
     *
     * @param modelId id of model
     * @param modelVariant model variant values
     * @param namePrefix prefix of model property name
     * @return map of values of model properties with given name prefix
     */
    public Map<String, String> GetModelValues(String modelId, Map<String, String> modelVariant, String namePrefix);

    /**
     * Ivalidate model variant with given ID and model variant value
     *
     * @param modelId model id
     * @param modelVariant model variant values
     */
    public void InvalidateModel(String modelId, Map<String, String> modelVariant);

    /**
     * Returns server properties
     *
     * @return server properties
     */
    public Map<String, String> Properties();

    /**
     * Sends action with requested ID to client.
     * 
     * @param actionId ID of action
     * @return true if action found and sent, otherwise false
     */
    public boolean SendAction(String actionId);

    /**
     * Sends interface with requested class to client.
     * 
     * @param interfaceClass class of interface
     * @return true if interface found and sent, otherwise false
     */
    public boolean SendInterface(String interfaceClass);
}
