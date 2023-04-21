package com.hyper9.jwbem;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIUnsignedByte;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;

/**
 * An SWbemObject class.
 * 
 * @author akutz
 * @see "http://msdn.microsoft.com/en-us/library/aa393741(VS.85).aspx"
 */
public class SWbemObject extends SWbemDispatchObject implements SWbemSetItem {

    /**
     * This object's SWbemObjectPath.
     */
    private SWbemObjectPath objectPath;

    private SWbemPropertySet propSet;

    private SWbemMethodSet methSet;

    /**
     * Initializes a new instance of the SWbemObject class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public SWbemObject(IJIDispatch objectDispatcher, SWbemServices service) {
        super(objectDispatcher, service);
    }

    /**
     * Gets this object's SWbemObjectPath.
     * 
     * @return This object's SWbemObjectPath.
     * @throws JIException When an error occurs.
     */
    public SWbemObjectPath getObjectPath() throws JIException {
        if (this.objectPath != null) {
            return this.objectPath;
        }
        JIVariant variant = super.objectDispatcher.get("Path_");
        IJIComObject co = variant.getObjectAsComObject();
        IJIDispatch dispatch = (IJIDispatch) JIObjectFactory.narrowObject(co);
        this.objectPath = new SWbemObjectPath(dispatch, super.service);
        return this.objectPath;
    }

    /**
     * Gets the textual representation of the object (MOF syntax).
     * 
     * @return The textual representation of the object (MOF syntax).
     * @throws JIException
     */
    public String getObjectText() throws JIException {
        Object[] inParams = new Object[] { new Integer(0) };
        JIVariant[] results = super.objectDispatcher.callMethodA("GetObjectText_", inParams);
        return results[0].getObjectAsString().getString();
    }

    /**
     * Gets an SWbemPropertySet object that is the collection of properties for
     * this object.
     * 
     * @return A SWbemPropertySet object that is the collection of properties
     *         for this object.
     * @throws JIException When an error occurs.
     */
    public SWbemPropertySet getProperties() throws JIException {
        if (this.propSet != null) {
            return propSet;
        }
        JIVariant variant = super.objectDispatcher.get("Properties_");
        IJIComObject co = variant.getObjectAsComObject();
        IJIDispatch dispatch = (IJIDispatch) JIObjectFactory.narrowObject(co);
        this.propSet = new SWbemPropertySet(dispatch, this.service, SWbemProperty.class);
        return this.propSet;
    }

    /**
     * Gets an SWbemMethodSet object that is the collection of methods for this
     * object.
     * 
     * @return An SWbemMethodSet object that is the collection of methods for
     *         this object.
     * @throws JIException When an error occurs.
     */
    public SWbemMethodSet getMethods() throws JIException {
        if (this.methSet != null) {
            return methSet;
        }
        JIVariant variant = super.objectDispatcher.get("Methods_");
        IJIComObject co = variant.getObjectAsComObject();
        IJIDispatch dispatch = (IJIDispatch) JIObjectFactory.narrowObject(co);
        this.methSet = new SWbemMethodSet(dispatch, this.service, SWbemMethod.class);
        return this.methSet;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Object> T getProperty(String propertyName, Class<T> clazz) throws UnsupportedOperationException {
        try {
            SWbemProperty prop = getProperties().getItem(propertyName);
            if (prop == null) {
                return null;
            }
            if (clazz.isArray()) {
                final Class compType = clazz.getComponentType();
                Object pv = prop.getValue();
                if (pv.equals(0)) {
                    return (T) Array.newInstance(compType, 0);
                }
                final JIArray jiarr = (JIArray) pv;
                final JIVariant[] jivarr = (JIVariant[]) jiarr.getArrayInstance();
                final T arr = (T) Array.newInstance(compType, jivarr.length);
                for (int x = 0; x < jivarr.length; ++x) {
                    if (compType == String.class) {
                        Array.set(arr, x, jivarr[x].getObjectAsString2());
                    } else if (compType == Short.class || compType == short.class) {
                        Array.set(arr, x, jivarr[x].getObjectAsShort());
                    } else if (compType == Integer.class || compType == int.class) {
                        Array.set(arr, x, jivarr[x].getObjectAsInt());
                    } else if (compType == Long.class || compType == long.class) {
                        Array.set(arr, x, jivarr[x].getObjectAsLong());
                    }
                }
                return (T) arr;
            }
            if (clazz == String.class) {
                return (T) prop.toString();
            }
            if (clazz == Short.class || clazz == short.class) {
                return (T) new Short(((JIUnsignedByte) prop.getValue()).getValue().shortValue());
            }
            if (clazz == Integer.class || clazz == int.class) {
                return (T) prop.getValueAsInteger();
            }
            if (clazz == Long.class || clazz == long.class) {
                return (T) prop.getValueAsLong();
            }
            if (clazz == Boolean.class) {
                return (T) prop.getValueAsBoolean();
            }
            if (clazz == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dateString = prop.toString().substring(0, 8);
                return (T) sdf.parse(dateString);
            }
        } catch (Throwable e) {
            JISystem.getLogger().log(java.util.logging.Level.SEVERE, "Error getting property value", e);
        }
        throw new UnsupportedOperationException("Invalid class type.");
    }
}
