package org.datanucleus.store.mapped.mapping;

import org.datanucleus.state.ObjectProviderFactory;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.ObjectProvider;

/**
 * Mapping for a serialised PersistenceCapable object.
 * Extends ObjectMapping since that provides the basic serialisation mechanism,
 * adding on the addition of StateManagers to the serialised object whenever it is required.
 */
public class SerialisedPCMapping extends SerialisedMapping {

    /**
     * Method to populate parameter positions in a PreparedStatement with this object
     * @param ec The Object Manager
     * @param preparedStatement The Prepared Statement
     * @param exprIndex The parameter positions to populate
     * @param value The value of the PC to use in populating the parameter positions
     */
    public void setObject(ExecutionContext ec, Object preparedStatement, int[] exprIndex, Object value) {
        setObject(ec, preparedStatement, exprIndex, value, null, mmd.getAbsoluteFieldNumber());
    }

    /**
     * Method to populate parameter positions in a PreparedStatement with this object
     * @param ec The Object Manager
     * @param preparedStatement The Prepared Statement
     * @param exprIndex The parameter positions to populate
     * @param value The value of the PC to use in populating the parameter positions
     * @param ownerSM State Manager for the owning object
     * @param fieldNumber field number of this object in the owning object
     */
    public void setObject(ExecutionContext ec, Object preparedStatement, int[] exprIndex, Object value, ObjectProvider ownerSM, int fieldNumber) {
        if (value != null) {
            ObjectProvider embSM = ec.findObjectProvider(value);
            if (embSM == null || ec.getApiAdapter().getExecutionContext(value) == null) {
                embSM = ObjectProviderFactory.newForEmbedded(ec, value, false, ownerSM, fieldNumber);
            }
        }
        ObjectProvider sm = null;
        if (value != null) {
            sm = ec.findObjectProvider(value);
        }
        if (sm != null) {
            sm.setStoringPC();
        }
        getDatastoreMapping(0).setObject(preparedStatement, exprIndex[0], value);
        if (sm != null) {
            sm.unsetStoringPC();
        }
    }

    /**
     * Method to extract the value of the PersistenceCapable from a ResultSet.
     * @param ec The ObjectManager
     * @param resultSet The ResultSet
     * @param exprIndex The parameter positions in the result set to use.
     * @return The (deserialised) PersistenceCapable object
     */
    public Object getObject(ExecutionContext ec, Object resultSet, int[] exprIndex) {
        return getObject(ec, resultSet, exprIndex, null, mmd.getAbsoluteFieldNumber());
    }

    /**
     * Method to extract the value of the PersistenceCapable from a ResultSet.
     * @param ec The ObjectManager
     * @param resultSet The ResultSet
     * @param exprIndex The parameter positions in the result set to use.
     * @param ownerSM The owning object
     * @param fieldNumber Absolute number of field in owner object
     * @return The (deserialised) PersistenceCapable object
     */
    public Object getObject(ExecutionContext ec, Object resultSet, int[] exprIndex, ObjectProvider ownerSM, int fieldNumber) {
        Object obj = getDatastoreMapping(0).getObject(resultSet, exprIndex[0]);
        if (obj != null) {
            ObjectProvider embSM = ec.findObjectProvider(obj);
            if (embSM == null || ec.getApiAdapter().getExecutionContext(obj) == null) {
                ObjectProviderFactory.newForEmbedded(ec, obj, false, ownerSM, fieldNumber);
            }
        }
        return obj;
    }
}
