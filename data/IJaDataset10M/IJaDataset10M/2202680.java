package com.habitsoft.kiyaa.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;
import com.habitsoft.kiyaa.util.DictionaryConstants;

/**
 * A class you can use to serialize objects using the RPC algorithms, but
 * so they can be embedded into the HTML page in a <script> ... </script>
 * block and loaded using DictionaryConstants.
 * 
 * @author dobes
 */
public class DictionaryConstantsWriter {

    /**
     * Convert an object into a serialized string which could be used to deserialize it later inside
     * the code for DictionaryConstants.
     * 
     * @param object
     *            Object to serialize
     * @param clazz
     *            Target class expected by the deserializer (pass object.getClass() if you don't
     *            have anything better)
     * @param inScriptTag Whether any close tags should be escaped because this is in a <script> ... </script> block instead of its own js file.
     * @return A string representing the encoded form of the object; this is a javascript/json expression.
     * @throws SerializationException
     *             If there is a problem during serialization
     */
    public static String serializeObject(Object object, Class<?> clazz, boolean inScriptTag) throws SerializationException {
        ServerSerializationStreamWriter stream = new ServerSerializationStreamWriter(new SerializationPolicy() {

            @Override
            public void validateSerialize(Class<?> clazz) throws SerializationException {
            }

            @Override
            public void validateDeserialize(Class<?> clazz) throws SerializationException {
                throw new SerializationException("Should only be used for serialization!");
            }

            @Override
            public boolean shouldSerializeFields(Class<?> clazz) {
                return clazz.getSuperclass() != null;
            }

            @Override
            public boolean shouldDeserializeFields(Class<?> clazz) {
                return false;
            }
        });
        stream.prepareToWrite();
        stream.serializeValue(object, clazz);
        return stream.toString();
    }

    /**
     * 
     * @param dictionaryName Javascript variable name
     * @param values Dictionary to serialize
     * @param clazz Target class expected by the deserializer (pass object.getClass() if you don't
     *            have anything better)
     * @param inScriptTag Whether any close tags should be escaped because this is in a <script> ... </script> block instead of its own js file.
     * @return A string representation of the given dictionary which can be deserialized by DictionaryConstants
     */
    public static String serializeMap(String dictionaryName, Map<String, Object> values, Class<? extends DictionaryConstants> clazz, boolean inScriptTag) throws SerializationException, SecurityException, NoSuchMethodException {
        StringBuffer sb = new StringBuffer();
        if (dictionaryName != null) sb.append(dictionaryName).append("=");
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (first) first = false; else sb.append(",\n");
            String key = entry.getKey();
            Object value = entry.getValue();
            Method method = clazz.getMethod(key);
            Class<?> returnType = method.getReturnType();
            appendValue(sb, key, value, returnType, inScriptTag);
        }
        sb.append("};");
        return sb.toString();
    }

    /**
     * Server-side serialization of the constants to a string.
     * 
     * @param <T>
     * @param dictionaryName Javascript variable name
     * @param values Dictionary to serialize
     * @param clazz Target class expected by the deserializer (pass object.getClass() if you don't
     *            have anything better)
     * @param inScriptTag Whether any close tags should be escaped because this is in a <script> ... </script> block instead of its own js file.
     * @return A string representation of the given dictionary which can be deserialized by DictionaryConstants
     */
    public static <T extends DictionaryConstants> String serializeConstants(String dictionaryName, T values, Class<T> clazz, boolean inScriptTag) throws SerializationException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        StringBuffer sb = new StringBuffer();
        if (dictionaryName != null) sb.append(dictionaryName).append("=");
        sb.append('{');
        boolean first = true;
        for (Method method : clazz.getMethods()) {
            if (first) first = false; else sb.append(",\n");
            String key = method.getName();
            Object value = method.invoke(values);
            Class<?> returnType = method.getReturnType();
            appendValue(sb, key, value, returnType, inScriptTag);
        }
        sb.append('}');
        return sb.toString();
    }

    private static void appendValue(StringBuffer sb, String key, Object value, Class<?> returnType, boolean inScriptTag) throws SerializationException {
        sb.append('\'').append(escapeJavaScriptString(key)).append('\'').append(':');
        if (returnType.equals(boolean.class) || returnType.equals(int.class) || returnType.equals(double.class) || returnType.equals(float.class)) {
            sb.append(StringEscapeUtils.escapeJavaScript(String.valueOf(value)));
        } else if (returnType.equals(String.class)) {
            sb.append('\'').append(escapeJavaScriptString(String.valueOf(value))).append('\'');
        } else {
            sb.append(serializeObject(value, returnType, inScriptTag));
        }
    }

    /**
     * Escape a javascript string, assuming that it's being enclosed in single quotes. Any </script>
     * inside the string is broken up by sticking a '+' in the middle, effectively preventing naive
     * javascript tag parsers from seeing </script> and allowing that value to pass through
     * (theoretically).
     */
    private static Object escapeJavaScriptString(String s) {
        s = StringEscapeUtils.escapeJavaScript(s);
        s = s.replace("</", "<\\/");
        return s;
    }
}
