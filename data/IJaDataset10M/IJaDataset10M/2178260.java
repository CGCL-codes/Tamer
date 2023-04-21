package org.datanucleus.store.neodatis.fieldmanager;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.datanucleus.api.ApiAdapter;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.ObjectProviderFactory;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.fieldmanager.AbstractFieldManager;
import org.datanucleus.store.neodatis.NeoDatisUtils;
import org.neodatis.odb.ODB;

/**
 * Field manager that starts from the source object and for all fields will assign StateManagers to all
 * related PersistenceCapable objects found (unless already managed), assuming they are in P_CLEAN state.
 **/
public class AssignStateManagerFieldManager extends AbstractFieldManager {

    /** StateManager for the owning object whose fields are being fetched. */
    private final ObjectProvider sm;

    /** NeoDatis ODB to use when assigning any StateManagers down the object graph. */
    private final ODB odb;

    /**
     * Constructor.
     * @param sm The state manager for the object.
     **/
    public AssignStateManagerFieldManager(ObjectProvider sm, ODB odb) {
        this.sm = sm;
        this.odb = odb;
    }

    /**
     * Utility method to process the passed persistable object.
     * We know that this object has no StateManager when it comes in here.
     * @param fieldNumber Absolute field number
     * @param pc The persistable object
     */
    protected void processPersistable(int fieldNumber, Object pc) {
        ExecutionContext ec = sm.getExecutionContext();
        ObjectProvider theSM = null;
        AbstractClassMetaData acmd = ec.getMetaDataManager().getMetaDataForClass(pc.getClass(), ec.getClassLoaderResolver());
        Object id = NeoDatisUtils.getIdentityForObject(pc, acmd, ec, odb);
        theSM = ObjectProviderFactory.newForPersistentClean(ec, id, pc);
        theSM.provideFields(theSM.getClassMetaData().getAllMemberPositions(), new AssignStateManagerFieldManager(theSM, odb));
    }

    /**
     * Method to store an object field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeObjectField(int fieldNumber, Object value) {
        if (value != null) {
            ExecutionContext ec = sm.getExecutionContext();
            ApiAdapter api = ec.getApiAdapter();
            AbstractMemberMetaData fmd = sm.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
            if (api.isPersistable(value)) {
                ObjectProvider valueSM = ec.findObjectProvider(value);
                if (valueSM == null) {
                    processPersistable(fieldNumber, value);
                }
            } else if (value instanceof Collection) {
                if (fmd.hasCollection() && fmd.getCollection().elementIsPersistent()) {
                    Collection coll = (Collection) value;
                    Iterator iter = coll.iterator();
                    while (iter.hasNext()) {
                        Object element = iter.next();
                        if (api.isPersistable(element)) {
                            ObjectProvider elementSM = sm.getExecutionContext().findObjectProvider(element);
                            if (elementSM == null) {
                                processPersistable(fieldNumber, element);
                            }
                        }
                    }
                }
            } else if (value instanceof Map) {
                if (fmd.hasMap()) {
                    if (fmd.getMap().keyIsPersistent()) {
                        Map map = (Map) value;
                        Set keys = map.keySet();
                        Iterator iter = keys.iterator();
                        while (iter.hasNext()) {
                            Object mapKey = iter.next();
                            if (api.isPersistable(mapKey)) {
                                ObjectProvider keySM = sm.getExecutionContext().findObjectProvider(mapKey);
                                if (keySM == null) {
                                    processPersistable(fieldNumber, mapKey);
                                }
                            }
                        }
                    }
                    if (fmd.getMap().valueIsPersistent()) {
                        Map map = (Map) value;
                        Collection values = map.values();
                        Iterator iter = values.iterator();
                        while (iter.hasNext()) {
                            Object mapValue = iter.next();
                            if (api.isPersistable(mapValue)) {
                                ObjectProvider valueSM = sm.getExecutionContext().findObjectProvider(mapValue);
                                if (valueSM == null) {
                                    processPersistable(fieldNumber, mapValue);
                                }
                            }
                        }
                    }
                }
            } else if (value instanceof Object[]) {
                if (fmd.hasArray() && fmd.getArray().elementIsPersistent()) {
                    for (int i = 0; i < Array.getLength(value); i++) {
                        Object element = Array.get(value, i);
                        if (api.isPersistable(element)) {
                            ObjectProvider elementSM = sm.getExecutionContext().findObjectProvider(element);
                            if (elementSM == null) {
                                processPersistable(fieldNumber, element);
                            }
                        }
                    }
                }
            } else {
            }
        }
    }

    /**
     * Method to store a boolean field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeBooleanField(int fieldNumber, boolean value) {
    }

    /**
     * Method to store a byte field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeByteField(int fieldNumber, byte value) {
    }

    /**
     * Method to store a char field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeCharField(int fieldNumber, char value) {
    }

    /**
     * Method to store a double field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeDoubleField(int fieldNumber, double value) {
    }

    /**
     * Method to store a float field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeFloatField(int fieldNumber, float value) {
    }

    /**
     * Method to store an int field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeIntField(int fieldNumber, int value) {
    }

    /**
     * Method to store a long field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeLongField(int fieldNumber, long value) {
    }

    /**
     * Method to store a short field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeShortField(int fieldNumber, short value) {
    }

    /**
     * Method to store a string field.
     * @param fieldNumber Number of the field (absolute)
     * @param value Value of the field
     */
    public void storeStringField(int fieldNumber, String value) {
    }
}
