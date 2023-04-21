package uips.events.javaScript;

import uips.events.IClient;
import uips.instances.Instance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import uips.models.Model;
import uips.models.ModelUpdateType;
import uips.models.ModelVariant;
import uips.support.Log;
import uips.uipdocuments.PropertyEntry;
import uips.support.Messages;
import uips.uipserver.XmlClient;

/**
 * Instance of this class is delivered to event handler, by that instance
 * is possible to update, invalidate models and get model values belongs
 * to connected client that sent event.
 * <br><br>
 * Based on Miroslav Macik's C# version of UIProtocolServer
 *
 * @author Miroslav Macik (macikm1@fel.cvut.cz, CTU Prague,  FEE)
 * @author Jindrich Basek (basekjin@fel.cvut.cz, CTU Prague,  FEE)
 */
public class JavaScriptClient implements IClient {

    /**
     * Reference to Instance that belongs event
     */
    private final Instance instanceReference;

    /**
     * Reference to client that sent event
     */
    private final XmlClient clientReference;

    /**
     * <code>JavaScriptClient</code> constructor
     *
     * @param clientReference Reference to client that sent event
     * @param instanceReference Reference to Instance that belongs event
     */
    public JavaScriptClient(XmlClient clientReference, Instance instanceReference) {
        this.instanceReference = instanceReference;
        this.clientReference = clientReference;
    }

    /**
     * Update one model property of base model variant with given ID,
     * model update type is set to partial
     *
     * @param modelId id of updated model
     * @param name name of property
     * @param key key of property
     * @param value valule of property
     * @return Map of updated properties (key - property name, value - property value)
     */
    @Override
    public Map<String, String> UpdateModel(String modelId, String name, String key, String value) {
        return UpdateModel(modelId, null, name, key, value);
    }

    /**
     * Update one model property of base model variant with given ID,
     * interpolation can be set to this update
     *
     * @param modelId id of updated model
     * @param name name of property
     * @param key key of property
     * @param value valule of property
     * @param duration duration of model interpolation
     * @param interpolation type of interpoletion
     * @param updateType update type of model (value from <code>ModelUpdateType</code> enumeration)
     * @return Map of updated properties (key - property name, value - property value)
     */
    @Override
    public Map<String, String> UpdateModel(String modelId, String name, String key, String value, int duration, String interpolation, ModelUpdateType updateType) {
        return UpdateModel(modelId, null, name, key, value, duration, interpolation, updateType);
    }

    /**
     * Update one model property of base model variant with given ID,
     * model update type is set to partial
     *
     * @param modelId id of updated model
     * @param name name of property
     * @param value valule of property
     * @return Map of updated properties (key - property name, value - property value
     */
    @Override
    public Map<String, String> UpdateModel(String modelId, String name, String value) {
        Map<String, String> modelVariant = null;
        return UpdateModel(modelId, modelVariant, name, value);
    }

    /**
     * Returns value of model property with given name from base model variant with given id
     *
     * @param modelId id of model
     * @param name name of the property
     * @return value of property if found otherwise null
     */
    @Override
    public String GetModelValue(String modelId, String name) {
        return GetModelValue(modelId, null, name);
    }

    /**
     * Returns key of model property with given name from base model variant with given id
     *
     * @param modelId id of model
     * @param name name of the property
     * @return key of property if found otherwise null
     */
    @Override
    public String GetModelKey(String modelId, String name) {
        return GetModelKey(modelId, null, name);
    }

    /**
     * Returns map of values of model properties with given name prefix
     * from base model variant with given id
     *
     * @param modelId id of model
     * @param namePrefix prefix of model property name
     * @return map of values of model properties with given name prefix
     */
    @Override
    public Map<String, String> GetModelValues(String modelId, String namePrefix) {
        return GetModelValues(modelId, null, namePrefix);
    }

    /**
     * Ivalidate base model variant with given id
     *
     * @param modelId model id
     */
    @Override
    public void InvalidateModel(String modelId) {
        try {
            instanceReference.getModelManager().invalidateModel(modelId, clientReference);
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
    }

    /**
     * Update one model property of model variant with given ID and model
     * variant values, model update type is set to partial
     *
     * @param modelId id of updated model
     * @param modelVariant model variant values
     * @param name name of property
     * @param key key of property
     * @param value valule of property
     * @return Map of updated properties (key - property name, value - property value)
     */
    @Override
    public Map<String, String> UpdateModel(String modelId, Map<String, String> modelVariant, String name, String key, String value) {
        try {
            PropertyEntry itemUpdated = new PropertyEntry(name, key, value);
            List<PropertyEntry> valuesToUpdate = new ArrayList<PropertyEntry>(1);
            valuesToUpdate.add(itemUpdated);
            List<PropertyEntry> updatedItems = instanceReference.getModelManager().updateModel(modelId, valuesToUpdate, PropertyEntry.mapToPropertyListConvert(modelVariant), ModelUpdateType.partial, clientReference, 0, null);
            Map<String, String> updatedValuesDict = new HashMap<String, String>();
            for (PropertyEntry item : updatedItems) {
                updatedValuesDict.put(item.getName(), item.getValue());
            }
            return updatedValuesDict;
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
        return null;
    }

    /**
     * Update one model property of model variant with given ID and model
     * variant values, interpolation can be set to this update
     *
     * @param modelId id of updated model
     * @param modelVariant model variant values
     * @param name name of property
     * @param key key of property
     * @param value valule of property
     * @param duration duration of model interpolation
     * @param interpolation type of interpoletion
     * @param updateType update type of model (value from <code>ModelUpdateType</code> enumeration)
     * @return Map of updated properties (key - property name, value - property value)
     */
    @Override
    public Map<String, String> UpdateModel(String modelId, Map<String, String> modelVariant, String name, String key, String value, int duration, String interpolation, ModelUpdateType updateType) {
        try {
            PropertyEntry itemUpdated = new PropertyEntry(name, key, value);
            List<PropertyEntry> valuesToUpdate = new ArrayList<PropertyEntry>(1);
            valuesToUpdate.add(itemUpdated);
            List<PropertyEntry> updatedItems = instanceReference.getModelManager().updateModel(modelId, valuesToUpdate, PropertyEntry.mapToPropertyListConvert(modelVariant), updateType, clientReference, duration, interpolation);
            Map<String, String> updatedValuesDict = new HashMap<String, String>();
            for (PropertyEntry item : updatedItems) {
                updatedValuesDict.put(item.getName(), item.getValue());
            }
            return updatedValuesDict;
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
        return null;
    }

    /**
     * Update one model property of model variant with given ID and model
     * variant values, model update type is set to partial
     *
     * @param modelId id of updated model
     * @param modelVariant model variant values
     * @param name name of property
     * @param value valule of property
     * @return Map of updated properties (key - property name, value - property value
     */
    @Override
    public Map<String, String> UpdateModel(String modelId, Map<String, String> modelVariant, String name, String value) {
        try {
            PropertyEntry itemUpdated = new PropertyEntry(name, null, value);
            List<PropertyEntry> valuesToUpdate = new ArrayList<PropertyEntry>(1);
            valuesToUpdate.add(itemUpdated);
            List<PropertyEntry> updatedItems = instanceReference.getModelManager().updateModel(modelId, valuesToUpdate, PropertyEntry.mapToPropertyListConvert(modelVariant), ModelUpdateType.partial, clientReference, 0, null);
            Map<String, String> updatedValuesDict = new HashMap<String, String>();
            for (PropertyEntry item : updatedItems) {
                updatedValuesDict.put(item.getName(), item.getValue());
            }
            return updatedValuesDict;
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
        return null;
    }

    /**
     * Returns value of model property with given name from model variant with
     * given ID and model variant values
     *
     * @param modelId id of model
     * @param modelVariant model variant values
     * @param name name of the property
     * @return value of property if found otherwise null
     */
    @Override
    public String GetModelValue(String modelId, Map<String, String> modelVariant, String name) {
        try {
            Model desiredModel = instanceReference.getModelManager().getModel(modelId, clientReference);
            if (desiredModel != null) {
                PropertyEntry mi = null;
                if (modelVariant == null || (modelVariant != null && modelVariant.isEmpty())) {
                    mi = desiredModel.getModelItem(name);
                } else {
                    ModelVariant desiredVariant;
                    if ((desiredVariant = desiredModel.getModelVariant(PropertyEntry.mapToPropertyListConvert(modelVariant))) != null) {
                        mi = desiredVariant.getModelItem(name);
                    }
                }
                if (mi != null) {
                    String modelItemValue = mi.getValue();
                    if (modelItemValue != null) {
                        return modelItemValue;
                    }
                    Log.write(Level.WARNING, "JavaScriptClient", "GetModelValues", Messages.getString("HandlerCanNotReturnNullValue"), clientReference);
                }
            } else {
                Log.write(Level.WARNING, "JavaScriptClient", "GetModelValue", String.format(Messages.getString("HandlerModelNotFound"), modelId), clientReference);
            }
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
        return null;
    }

    /**
     * Returns key of model property with given name from model variant with
     * given ID and model variant values
     *
     * @param modelId id of model
     * @param modelVariant model variant values
     * @param name name of the property
     * @return key of property if found otherwise null
     */
    @Override
    public String GetModelKey(String modelId, Map<String, String> modelVariant, String name) {
        try {
            Model desiredModel = instanceReference.getModelManager().getModel(modelId, clientReference);
            if (desiredModel != null) {
                PropertyEntry mi = null;
                if (modelVariant == null || (modelVariant != null && modelVariant.isEmpty())) {
                    mi = desiredModel.getModelItem(name);
                } else {
                    ModelVariant desiredVariant;
                    if ((desiredVariant = desiredModel.getModelVariant(PropertyEntry.mapToPropertyListConvert(modelVariant))) != null) {
                        mi = desiredVariant.getModelItem(name);
                    }
                }
                if (mi != null) {
                    String modelItemKey = mi.getKey();
                    return modelItemKey;
                }
            } else {
                Log.write(Level.WARNING, "JavaScriptClient", "GetModelKey", String.format(Messages.getString("HandlerModelNotFound"), modelId), clientReference);
            }
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
        return null;
    }

    /**
     * Returns map of values of model properties with given name prefix from
     * model variant with given ID and model variant values
     *
     * @param modelId id of model
     * @param modelVariant model variant values
     * @param namePrefix prefix of model property name
     * @return map of values of model properties with given name prefix
     */
    @Override
    public Map<String, String> GetModelValues(String modelId, Map<String, String> modelVariant, String namePrefix) {
        Map<String, String> foundItems = new HashMap<String, String>();
        try {
            Model desiredModel = instanceReference.getModelManager().getModel(modelId, clientReference);
            if (desiredModel != null) {
                List<PropertyEntry> properties = new ArrayList<PropertyEntry>();
                if (modelVariant == null || (modelVariant != null && modelVariant.isEmpty())) {
                    properties = desiredModel.getValues();
                } else {
                    ModelVariant desiredVariant;
                    if ((desiredVariant = desiredModel.getModelVariant(PropertyEntry.mapToPropertyListConvert(modelVariant))) != null) {
                        properties = desiredVariant.getValues();
                    }
                }
                for (PropertyEntry mi : properties) {
                    if (mi.getName().startsWith(namePrefix)) {
                        foundItems.put(mi.getName(), mi.getValue());
                    }
                }
            } else {
                Log.write(Level.WARNING, "JavaScriptClient", "GetModelValues", String.format(Messages.getString("HandlerModelNotFound"), modelId), clientReference);
            }
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
        return foundItems;
    }

    /**
     * Ivalidate model variant with given ID and model variant values
     *
     * @param modelId model id
     * @param modelVariant model variant values
     */
    @Override
    public void InvalidateModel(String modelId, Map<String, String> modelVariant) {
        try {
            instanceReference.getModelManager().invalidateModel(modelId, clientReference, PropertyEntry.mapToPropertyListConvert(modelVariant));
        } catch (Exception ex) {
            Log.write(Level.WARNING, ex.toString(), clientReference);
        }
    }

    /**
     * Returns class of connected client
     *
     * @return class of connected client
     */
    @Override
    public String Class() {
        return clientReference.getClientInfo().getClientClass();
    }

    /**
     * Returns map with client properties sent during connection hadshake
     *
     * @return map with client properties sent during connection hadshake
     */
    @Override
    public Map<String, String> Properties() {
        return clientReference.getClientInfo().getProperties();
    }

    /**
     * Sends action with requested ID to client.
     * 
     * @param actionId ID of action
     * @return true if action found and sent, otherwise false
     */
    @Override
    public boolean SendAction(String actionId) {
        return instanceReference.getActionManager().sendAction(clientReference, actionId);
    }

    /**
     * Sends interface with requested class to client.
     * 
     * @param interfaceClass class of interface
     * @return true if interface found and sent, otherwise false
     */
    @Override
    public boolean SendInterface(String interfaceClass) {
        return instanceReference.getActionManager().sendAction(clientReference, interfaceClass);
    }
}
