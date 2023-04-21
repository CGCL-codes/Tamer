package org.gwtoolbox.commons.js.client;

import com.google.gwt.core.client.JavaScriptObject;
import java.util.Map;

/**
 * @author Uri Boness
 */
public class JSUtils {

    private JSUtils() {
    }

    public static JSOBuilder jso() {
        return new JSOBuilder();
    }

    @SuppressWarnings("unchecked")
    public static JavaScriptObject fromMap(Map<String, Object> input) {
        JavaScriptObject obj = JavaScriptObject.createObject();
        for (String k : input.keySet()) {
            Object v = input.get(k);
            setValue(obj, k, v);
        }
        return obj;
    }

    public static void setValue(JavaScriptObject obj, String name, Object value) {
        if (value == null) {
            setNull(obj, name);
        } else if (value instanceof Integer) {
            setInt(obj, name, (Integer) value);
        } else if (value instanceof Boolean) {
            setBoolean(obj, name, (Boolean) value);
        } else if (value instanceof Short) {
            setShort(obj, name, (Short) value);
        } else if (value instanceof Double) {
            setDouble(obj, name, (Double) value);
        } else if (value instanceof Float) {
            setFloat(obj, name, (Float) value);
        } else if (value instanceof String) {
            setString(obj, name, (String) value);
        } else if (value instanceof JavaScriptObject) {
            setJSO(obj, name, (JavaScriptObject) value);
        } else if (value instanceof Enum) {
            setString(obj, name, ((Enum) value).name());
        } else {
            throw new IllegalArgumentException("Unsupported javascript value '" + value + "'");
        }
    }

    public static void setValue(JavaScriptObject array, int index, Object value) {
        if (value == null) {
            setNull(array, index);
        } else if (value instanceof Integer) {
            setInt(array, index, (Integer) value);
        } else if (value instanceof Boolean) {
            setBoolean(array, index, (Boolean) value);
        } else if (value instanceof Short) {
            setShort(array, index, (Short) value);
        } else if (value instanceof Double) {
            setDouble(array, index, (Double) value);
        } else if (value instanceof Float) {
            setFloat(array, index, (Float) value);
        } else if (value instanceof String) {
            setString(array, index, (String) value);
        } else if (value instanceof JavaScriptObject) {
            setJSO(array, index, (JavaScriptObject) value);
        } else if (value instanceof Enum) {
            setString(array, index, ((Enum) value).name());
        } else {
            throw new IllegalArgumentException("Unsupported javascript value '" + value + "'");
        }
    }

    public static native void setNull(JavaScriptObject object, String name);

    public static native void setBoolean(JavaScriptObject object, String name, boolean value);

    public static native void setString(JavaScriptObject object, String name, String value);

    public static native void setInt(JavaScriptObject object, String name, int value);

    public static native void setShort(JavaScriptObject object, String name, short value);

    public static native void setDouble(JavaScriptObject object, String name, double value);

    public static native void setFloat(JavaScriptObject object, String name, float value);

    public static native void setJSO(JavaScriptObject object, String name, JavaScriptObject value);

    public static native <T extends JavaScriptObject> T getJSO(JavaScriptObject object, String name);

    public static native void setNull(JavaScriptObject array, int index);

    public static native void setBoolean(JavaScriptObject array, int index, boolean value);

    public static native void setString(JavaScriptObject array, int index, String value);

    public static native void setInt(JavaScriptObject array, int index, int value);

    public static native void setShort(JavaScriptObject array, int index, short value);

    public static native void setDouble(JavaScriptObject array, int index, double value);

    public static native void setFloat(JavaScriptObject array, int index, float value);

    public static native void setJSO(JavaScriptObject array, int index, JavaScriptObject value);

    public static native <T extends JavaScriptObject> T getJSO(JavaScriptObject array, int index);

    public static native int getInt(JavaScriptObject object, String name);

    public static native String getString(JavaScriptObject object, String name);

    public static native String invokeAndGetString(JavaScriptObject object, String fnName);
}
