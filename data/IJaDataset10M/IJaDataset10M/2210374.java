package com.orientechnologies.jdo.types;

import com.orientechnologies.jdo.oDynaClassLoader;
import com.orientechnologies.jdo.oOID;
import com.orientechnologies.jdo.oPersistenceManager;
import com.orientechnologies.jdo.oUtility;
import com.orientechnologies.jdo.objecthandler.oObjectHandler;
import com.orientechnologies.jdo.trasform.oCIP;
import com.orientechnologies.jdo.trasform.oTrasformUtil;
import javax.jdo.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Map;

/**
 * <p>Express an association key/value. This class is often used
 * in Map collections to contains entries. If the value is
 * a reference (oOID object), then on first access (get() method)
 * the references will be resolved in the real object.</p>
 * <p/>
 * <p>This behaviour avoid the loading of concrete objects in cache, but
 * only real required by application on the first use.</p>
 */
public class d_Association implements Map.Entry {

    public d_Association() {
        key = null;
        value = null;
    }

    public d_Association(Object iKey, Object iValue) {
        key = iKey;
        value = iValue;
    }

    /**
     * Returns the key
     *
     * @return key associated
     */
    public Object getKey() {
        return key;
    }

    /**
     * Returns the value without resolving it in case of relationship
     *
     * @return raw value associated
     */
    public Object getValue() {
        return value;
    }

    public Object getValue(boolean iTreatAsRelationship) {
        if (iTreatAsRelationship && manager != null) {
            oOID oid = (oOID) value;
            if (oid.isValid()) value = manager.getObjectById(oid, true);
            manager = null;
        }
        return value;
    }

    /**
     * Assign the value
     *
     * @param iValue value to assign
     * @return old value if any
     */
    public Object setValue(Object iValue) {
        Object oldValue = value;
        value = iValue;
        return oldValue;
    }

    /**
     * Serialize object from memory to stream.
     */
    public oBinary toStream(oPersistenceManager iManager, String iURL, boolean iRelationship) {
        oBinary stream = new oBinary();
        try {
            DataOutputStream buffer = new DataOutputStream(stream);
            oBinary tmpStream = new oBinary();
            DataOutputStream tmpBuffer = new DataOutputStream(tmpStream);
            buffer.writeShort(oUtility.getIdFromClass(iManager, key.getClass().getName()));
            tmpStream.reset();
            oCIP.typeToStream(iManager, iURL, key, tmpBuffer, key.getClass(), false, iRelationship);
            buffer.writeInt(tmpStream.size());
            stream.append(tmpStream.toByteArray(), tmpStream.size());
            int typeToStore = oTrasformUtil.getTypeToStore(iManager, value, iRelationship);
            Object valueToStore = oTrasformUtil.getValueToStore(iManager, typeToStore, value);
            buffer.writeShort(typeToStore);
            tmpStream.reset();
            oCIP.typeToStream(iManager, iURL, valueToStore, tmpBuffer, valueToStore.getClass(), false, iRelationship);
            buffer.writeInt(tmpStream.size());
            stream.append(tmpStream.toByteArray(), tmpStream.size());
        } catch (Exception e) {
            oUtility.throwJDOUserException("Error on serializing d_Association object.", e);
        }
        return stream;
    }

    /**
     * Deserialize object from stream to memory.
     */
    public void fromStream(oPersistenceManager iManager, oObjectHandler iHandler, String iURL, DataInputStream iBuffer, int iTotal, boolean iRelationship) {
        try {
            int classId = iBuffer.readShort();
            String className = oUtility.getClassFromId(iManager, classId);
            Class embClass = oDynaClassLoader.getInstance().loadClass(className);
            if (embClass == null) throw new JDOFatalInternalException("[d_Association] Unknown class " + className + " for key");
            int size = iBuffer.readInt();
            if (size == 0) throw new JDOFatalInternalException("[d_Association] key element is 0 size");
            key = oCIP.typeFromStream(iManager, iHandler, iURL, iBuffer, embClass, size, null, true, iRelationship);
            if (key instanceof com.orientechnologies.jdo.oOID) {
                oOID oid = (oOID) key;
                if (oid.isValid()) key = iManager.getObjectById(oid, true);
            }
            classId = iBuffer.readShort();
            className = oUtility.getClassFromId(iManager, classId);
            embClass = oDynaClassLoader.getInstance().loadClass(className);
            if (embClass == null) throw new JDOFatalInternalException("[d_Association] Unknown class " + className + " for value");
            size = iBuffer.readInt();
            value = oCIP.typeFromStream(iManager, iHandler, iURL, iBuffer, embClass, size, null, true, iRelationship);
            if (value instanceof com.orientechnologies.jdo.oOID) {
                manager = iManager;
            }
        } catch (Exception e) {
            oUtility.throwJDOUserException("Error on deserializing d_Association object.", e);
        }
    }

    private Object key;

    private Object value;

    private transient oPersistenceManager manager = null;
}
