package com.serotonin.json.convert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import com.serotonin.json.JsonContext;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.JsonSerializable;
import com.serotonin.json.type.JsonArray;
import com.serotonin.json.type.JsonBoolean;
import com.serotonin.json.type.JsonNull;
import com.serotonin.json.type.JsonNumber;
import com.serotonin.json.type.JsonObject;
import com.serotonin.json.type.JsonString;
import com.serotonin.json.type.JsonValue;
import com.serotonin.json.util.SerializableProperty;
import com.serotonin.json.util.TypeUtils;

/**
 * This class is the general purpose converter for objects for which no other converter has been explicitly provided.
 * Instance of this class are typically generated automatically by the JSON context, but can also be provided by client
 * code.
 * 
 * @author Matthew Lohbihler
 */
public class JsonPropertyConverter extends AbstractClassConverter {

    private final boolean jsonSerializable;

    private final List<SerializableProperty> properties;

    /**
     * Constructor.
     * 
     * @param jsonSerializable
     *            does the class implement JsonSerializable?
     * @param properties
     *            the list of serializable properties of the class.
     */
    public JsonPropertyConverter(boolean jsonSerializable, List<SerializableProperty> properties) {
        this.jsonSerializable = jsonSerializable;
        this.properties = properties;
    }

    @Override
    public void jsonWrite(JsonWriter writer, Object value) throws IOException, JsonException {
        ObjectWriter objectWriter = new ObjectWriter(writer);
        if (jsonSerializable) ((JsonSerializable) value).jsonWrite(objectWriter);
        if (properties != null) {
            for (SerializableProperty prop : properties) {
                if (!prop.include(writer.getIncludeHint())) continue;
                Method readMethod = prop.getReadMethod();
                if (readMethod == null) continue;
                String name = prop.getNameToUse();
                Object propertyValue;
                try {
                    propertyValue = readMethod.invoke(value);
                } catch (Exception e) {
                    throw new JsonException("Error reading '" + prop.getName() + "' from value " + value + " of class " + value.getClass(), e);
                }
                boolean ignore = false;
                if (prop.isSuppressDefaultValue()) {
                    if (propertyValue == null) ignore = true; else {
                        Class<?> propertyClass = readMethod.getReturnType();
                        if (propertyClass == Boolean.TYPE) ignore = ((Boolean) propertyValue) == false; else if (propertyClass == Double.TYPE) ignore = (Double) propertyValue == 0; else if (propertyClass == Long.TYPE) ignore = (Long) propertyValue == 0; else if (propertyClass == Float.TYPE) ignore = (Float) propertyValue == 0; else if (propertyClass == Integer.TYPE) ignore = (Integer) propertyValue == 0; else if (propertyClass == Short.TYPE) ignore = (Short) propertyValue == 0; else if (propertyClass == Byte.TYPE) ignore = (Byte) propertyValue == 0; else if (propertyClass == Character.TYPE) ignore = (Character) propertyValue == 0; else if (propertyClass == JsonNull.class) ignore = true; else if (propertyClass == JsonBoolean.class) ignore = ((JsonBoolean) propertyValue).getValue() == false; else if (propertyClass == JsonNumber.class) ignore = ((JsonNumber) propertyValue).getDoubleValue() == 0; else if (propertyClass == JsonString.class) ignore = ((JsonString) propertyValue).getValue() == null; else if (propertyClass == JsonArray.class) ignore = ((JsonArray) propertyValue).getElements() == null; else if (propertyClass == JsonObject.class) ignore = ((JsonObject) propertyValue).getProperties() == null;
                    }
                }
                if (!ignore) objectWriter.writeEntry(name, propertyValue);
            }
        }
        objectWriter.finish();
    }

    @Override
    protected Object newInstance(JsonContext context, JsonValue jsonValue, Type type) throws JsonException {
        return context.getNewInstance(TypeUtils.getRawClass(type), jsonValue);
    }

    @Override
    public void jsonRead(JsonReader reader, JsonValue jsonValue, Object obj, Type type) throws JsonException {
        JsonObject jsonObject = jsonValue.toJsonObject();
        if (jsonSerializable) ((JsonSerializable) obj).jsonRead(reader, jsonObject);
        if (properties != null) {
            for (SerializableProperty prop : properties) {
                if (!prop.include(reader.getIncludeHint())) continue;
                Method writeMethod = prop.getWriteMethod();
                if (writeMethod == null) continue;
                String name = prop.getNameToUse();
                JsonValue propJsonValue = jsonObject.getValue(name);
                if (propJsonValue == null) continue;
                Type propType = writeMethod.getGenericParameterTypes()[0];
                propType = TypeUtils.resolveTypeVariable(type, propType);
                Class<?> propClass = TypeUtils.getRawClass(propType);
                try {
                    Object propValue = reader.read(propType, propJsonValue);
                    if (propClass.isPrimitive() && propValue == null) {
                        if (propClass == Boolean.TYPE) propValue = false; else propValue = 0;
                    }
                    prop.getWriteMethod().invoke(obj, propValue);
                } catch (Exception e) {
                    throw new JsonException("JsonException writing property '" + prop.getName() + "' of class " + propClass.getName(), e);
                }
            }
        }
    }
}
