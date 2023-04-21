package org.sourceforge.jemm.client;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.sourceforge.jemm.JEMMInternalException;
import org.sourceforge.jemm.client.field.FieldKey;
import org.sourceforge.jemm.client.types.Attribute;
import org.sourceforge.jemm.client.types.EntityAttribute;
import org.sourceforge.jemm.client.types.PrimitiveAttribute;
import org.sourceforge.jemm.database.ClassId;
import org.sourceforge.jemm.database.ClassInfo;
import org.sourceforge.jemm.database.FieldInfo;
import org.sourceforge.jemm.database.FieldType;
import org.sourceforge.jemm.database.GetObjectResponse;
import org.sourceforge.jemm.database.ObjectState;
import org.sourceforge.jemm.database.ObjectSyncResponse;
import org.sourceforge.jemm.lifecycle.MethodListener;
import org.sourceforge.jemm.lifecycle.ShadowTypeObject;
import org.sourceforge.jemm.lifecycle.ShadowTypeObjectImpl;
import org.sourceforge.jemm.lifecycle.ShadowUserObject;
import org.sourceforge.jemm.lifecycle.ShadowUserObjectImpl;
import org.sourceforge.jemm.types.ID;
import org.sourceforge.jemm.util.JEMMObject;
import org.sourceforge.jemm.util.JEMMType;

/**
 * A Factory that converts and sets fields onto 
 * @author Paul Keeble
 *
 */
public class JEMMObjectFieldMapper {

    MethodListener listener;

    TypeRequestHandler typeRequestHandler;

    public JEMMObjectFieldMapper(MethodListener listener, TypeRequestHandler typeRequestHandler) {
        this.listener = listener;
        this.typeRequestHandler = typeRequestHandler;
    }

    /**
	 * Creates a new JEMMObject from the response along with introduced Tracked IDs.
	 * 
	 * @param resp The object response
	 * @param ci The class information for the object to be created
	 * @return The newly created JEMMObject
	 */
    public JEMMObject create(GetObjectResponse resp, ClassInfo ci) {
        if (ci.isType()) return createTypeObject(resp, resp.getClassId(), ci); else return createUserObject(resp, ci);
    }

    private JEMMObject createTypeObject(GetObjectResponse resp, ClassId classId, ClassInfo ci) {
        try {
            Class<?> clazz = Class.forName(ci.getClassName());
            Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { ShadowTypeObject.class });
            constructor.setAccessible(true);
            ObjectState objState = resp.getObjectState();
            ShadowTypeObjectImpl so = new ShadowTypeObjectImpl(null, objState.getJemmId(), classId, typeRequestHandler);
            JEMMType obj = (JEMMType) constructor.newInstance(so);
            so.setObject(obj);
            mapFields(objState.getFieldValues(), obj);
            return obj;
        } catch (Exception e) {
            throw new JEMMInternalException("Unable to construct object of type " + ci.getClassName(), e);
        }
    }

    private JEMMObject createUserObject(GetObjectResponse resp, ClassInfo ci) {
        try {
            Class<?> clazz = Class.forName(ci.getClassName());
            Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { ShadowUserObject.class });
            ObjectState objState = resp.getObjectState();
            ShadowUserObjectImpl so = new ShadowUserObjectImpl(listener, objState.getJemmId(), objState.getClientVersion());
            JEMMObject obj = (JEMMObject) constructor.newInstance(so);
            so.setUserObject(obj);
            mapFields(objState.getFieldValues(), obj);
            return obj;
        } catch (Exception e) {
            throw new JEMMInternalException("Unable to construct object", e);
        }
    }

    /**
	 * Gets the Synchronisation data from a JEMMObject.
	 * 
	 * @param jo The object data to retrieve.
	 * @return The sync data for the given object.
	 */
    public ObjectState getSyncData(JEMMObject jo) {
        ObjectAccessor oa = new ObjectAccessor(jo);
        Entity e = new Entity(jo);
        Map<FieldInfo, Object> fields = oa.getAllFieldsMap();
        ObjectState data = new ObjectState(e.getID(), e.getVersion(), fields);
        return data;
    }

    /**
	 * Updates the object with new meta and FieldData from the response.
	 * 
	 * Updates all primitive values. Updates and uninitialised Object fields with
	 * the new ID.
	 * 
	 * Returns any 
	 * @param jo The user object to update. 
	 * @param response The response data to use for update.
	 */
    public Map<FieldInfo, ID> syncResponseUpdateObject(JEMMObject jo, ObjectSyncResponse response) {
        Entity e = new Entity(jo);
        e.setVersion(response.getNewVersionNo());
        try {
            mapFields(response.getUpdatedFields(), jo);
            return null;
        } catch (ClassNotFoundException e1) {
            throw new JEMMInternalException("Unable to update object", e1);
        }
    }

    /**
	 * Updates all the primitive values and uninitialised fields on a JEMMObject.
	 * 
	 * @param jo
	 * @param state
	 */
    public void refreshPrimitiveState(JEMMObject jo, ObjectState state) {
        if (jo instanceof JEMMType) {
            JEMMType t = (JEMMType) jo;
            refreshPrimitiveState(new JEMMTypeWrapper(t), state);
        } else if (Entity.isEntity(jo)) {
            refreshPrimitiveState(new Entity(jo), state);
        }
    }

    private void refreshPrimitiveState(Entity e, ObjectState state) {
        e.setVersion(state.getClientVersion());
        try {
            Map<FieldInfo, Object> primitiveFields = getPrimitiveFields(state.getFieldValues());
            mapFields(primitiveFields, e.getObject());
            Map<FieldKey, ID> unitialisedObjectFields = new HashMap<FieldKey, ID>();
            for (EntityAttribute attrib : e.getFinalizedAttributes()) {
                unitialisedObjectFields.put(attrib.getFieldKey(), attrib.getID());
            }
            Map<FieldInfo, Object> uninitFieldState = getUninitialisedFields(unitialisedObjectFields, state.getFieldValues());
            mapFields(uninitFieldState, e.getObject());
        } catch (ClassNotFoundException e1) {
            throw new JEMMInternalException("Unable to refresh object", e1);
        }
    }

    private void refreshPrimitiveState(JEMMTypeWrapper t, ObjectState state) {
    }

    Map<FieldInfo, Object> getPrimitiveFields(Map<FieldInfo, Object> origFields) {
        Map<FieldInfo, Object> result = new HashMap<FieldInfo, Object>();
        for (Entry<FieldInfo, Object> fInfo : origFields.entrySet()) {
            if (FieldType.isPrimitive(fInfo.getKey().getFieldType())) {
                result.put(fInfo.getKey(), fInfo.getValue());
            }
        }
        return result;
    }

    Map<FieldInfo, Object> getUninitialisedFields(Map<FieldKey, ID> uninitialisedFields, Map<FieldInfo, Object> state) {
        Map<FieldInfo, Object> result = new HashMap<FieldInfo, Object>();
        for (FieldKey fKey : uninitialisedFields.keySet()) {
            FieldInfo fi = new FieldInfo(fKey.getName(), fKey.getClazz(), FieldType.OBJECT);
            if (!state.containsKey(fi)) throw new JEMMInternalException("State missing field:" + fKey);
            result.put(fi, state.get(fi));
        }
        return result;
    }

    private void mapFields(Map<FieldInfo, Object> origFields, Object obj) throws ClassNotFoundException {
        Entity entity = new Entity(obj);
        for (Entry<FieldInfo, Object> fInfo : origFields.entrySet()) {
            FieldInfo key = fInfo.getKey();
            Object value = fInfo.getValue();
            FieldKey fkey = new FieldKey(key.getClassName(), key.getFieldName());
            Attribute a = entity.getAttribute(fkey);
            if (a instanceof PrimitiveAttribute) {
                ((PrimitiveAttribute) a).set(value);
            } else if (a instanceof EntityAttribute) {
                ((EntityAttribute) a).set((ID) value);
            } else {
                throw new RuntimeException("Needs refactoring - programming hole for types in FieldMapper");
            }
        }
    }
}
