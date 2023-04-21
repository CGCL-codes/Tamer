package it.negro.jajb;

import it.negro.jajb.introspecting.JAJBIntrospectorWrapper;
import it.negro.jajb.introspecting.JAJBIntrospectorWrapper.Property;
import it.negro.jajb.json.JSONArray;
import it.negro.jajb.json.JSONException;
import it.negro.jajb.json.JSONObject;
import it.negro.jajb.json.JSONTokener;
import it.negro.jajb.utility.Utility;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class SimpleJAJB {

    /**
	 * This if the default date formatter for parsing and formating date.
	 * You can also you your preferred date formatter by setting it.
	 */
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'ZZ");

    /**
	 * This method makes binding between passed json parameter and the specific java bean identified by
	 * passed clazz parameter.
	 * If your json string represents a JSONArray, you should pass into clazz parameter, the clazz of object (or primitive type)
	 * that are components of expected resulting array. If json string represents a "multi-array" (ex. int[][][]),
	 * you have to pass always the class of object in last nested level (ex. Integer.TYPE).
	 * In case of "multi-array", the only entity that can be null are the last nested level elements (for example: [null, [1,2,3]]
	 * is wrong); you must pass an empty array element (for example: [[],[1,null,3]]); obviously in case of multi primitive array, you
	 * totally loose the null concept.
	 * @param json the json string that represents the java bean that you want to obtain invoking this method.
	 * @param clazz the class type of expected java bean or collection.
	 * @return - an instance of java bean of passed type class.
	 * @throws JAJBUnMarshallException
	 */
    public Object unmarshall(String json, Class<?> clazz) throws JAJBUnMarshallException {
        JSONTokener tokener = new JSONTokener(json);
        try {
            if (json.startsWith("{")) return this.unmarshallSimpleObject(new JSONObject(tokener), clazz); else if (json.startsWith("[")) return this.unmarshallArray(new JSONArray(tokener), clazz);
        } catch (Exception e) {
            throw new JAJBUnMarshallException(e.getMessage(), e);
        }
        throw new JAJBUnMarshallException("Malformed JSONString: " + json + " !!!\n It must start with { or [");
    }

    /**
	 * This method serializes passed object parameter into a json string according to passed clazz parameter that represents
	 * the class type of passed object. If the object parameter is a collection o an array (but also a
	 * multi nested collection or multi nested array, for example Person[][][]) the clazz parameter should be the class type of the
	 * java bean at last nested level of passed array or collection.
	 * @param object the collection or array or java bean you want to serialize.
	 * @param clazz class type of passed object or class type of object in last nested level of array (or multi nested array)
	 * or of collection (or multi nested collection).
	 * @return - a String in JSON format that represents passed object parameter.
	 * @throws JAJBMarshallException
	 */
    public String marshall(Object object, Class<?> clazz) throws JAJBMarshallException {
        String result = null;
        if (object.getClass().isArray() || Collection.class.isAssignableFrom(object.getClass())) result = this.marshallArray(object, clazz).toString(); else result = this.marshallSimpleObject(object, clazz).toString();
        return result;
    }

    /**
	 * This method tries to make a instance of an object with passed clazz parameter.
	 * If the expected resulting object and all its attributes are pojos, the binding will works
	 * well, otherwise maybe data loss or errors.
	 * @param jsonObject the JSONObject that represents the expected object resulting by binding it.
	 * @param clazz the class type of the expected resulting object.
	 * @return an instance of an object built with the clazz class type and using passed jsonObject parameter and all data that contains.
	 * @throws JAJBUnMarshallException
	 */
    private Object unmarshallSimpleObject(JSONObject jsonObject, Class<?> clazz) throws JAJBUnMarshallException {
        Object result = null;
        try {
            result = clazz.newInstance();
            Object element = null;
            JAJBIntrospectorWrapper iw = new JAJBIntrospectorWrapper(clazz);
            for (Property property : iw.getProperties()) {
                if (jsonObject.get(property.getName()) instanceof JSONObject) element = this.unmarshallSimpleObject(jsonObject.getJSONObject(property.getName()), property.getType()); else if (jsonObject.get(property.getName()) instanceof JSONArray) {
                    Class<?> componentType = property.getType();
                    while (componentType.isArray() || Collection.class.isAssignableFrom(property.getType())) componentType = componentType.getComponentType();
                    element = this.unmarshallArray(jsonObject.getJSONArray(property.getName()), componentType);
                } else {
                    if (property.getType().equals(Date.class)) element = this.format.parse((String) jsonObject.get(property.getName())); else element = jsonObject.get(property.getName());
                }
                iw.invokeSetter(result, property.getName(), element);
            }
        } catch (Exception e) {
            throw new JAJBUnMarshallException(e.getMessage(), e);
        }
        return result;
    }

    /**
	 * This method tries to make a instance of an array with passed clazz parameter.
	 * The clazz parameter represents the expected resulting array component type.
	 * I you expect a multi nested array the clazz parameter must be the class type
	 * of the last nested level object (or primitive type) of the expected resulting multi nested array. 
	 * If the last nested level object in the array and all its attributes are pojos, the binding will works
	 * well, otherwise maybe data loss or errors.
	 * @param jsonArray the JSONArray that represents the expected java array and contains all data to use
	 * to build it.
	 * @param clazz the last nested level object class type.
	 * @return an instance of array built using passed jsonArray parameter and all data that contains.
	 * @throws JAJBUnMarshallException
	 */
    private Object unmarshallArray(JSONArray jsonArray, Class<?> clazz) throws JAJBUnMarshallException {
        if (clazz.isPrimitive()) return this.unmarshallPrimitiveArray(jsonArray, clazz);
        ArrayList<Object> arrayList = null;
        Class<?> componentType = clazz;
        try {
            Object element = null;
            arrayList = new ArrayList<Object>();
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.get(i) instanceof JSONArray) element = this.unmarshallArray(jsonArray.getJSONArray(i), clazz); else if (jsonArray.get(i) instanceof JSONObject) element = this.unmarshallSimpleObject(jsonArray.getJSONObject(i), clazz); else element = jsonArray.get(i);
                if (element == JSONObject.NULL) {
                    arrayList.add(null);
                    componentType = clazz;
                } else {
                    arrayList.add(element);
                    componentType = element.getClass();
                }
            }
        } catch (JSONException e) {
            throw new JAJBUnMarshallException(e.getMessage(), e);
        }
        return arrayList.toArray((Object[]) Array.newInstance(componentType, arrayList.size()));
    }

    /**
	 * This is a specialized method for creation of array with primitive component type.
	 * It can create multi nested array (for example int[][][]), but anyway the clazz parameter must
	 * be the type class of last nested level component (for example in case of creation of an int[][][] clazz must be Integer.TYPE).
	 * @param jsonArray the JSONArray that represents expected array and contains all data involved in transformation.
	 * @param clazz class type of last nested level component of expected array.
	 * @return an array with primitive component or a multi nested array in witch the last nested level component is a primitive type.
	 * @throws JAJBUnMarshallException
	 */
    private Object unmarshallPrimitiveArray(JSONArray jsonArray, Class<?> clazz) throws JAJBUnMarshallException {
        ArrayList<Object> list = null;
        Class<?> componentType = null;
        try {
            componentType = clazz;
            list = new ArrayList<Object>();
            Object element = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.get(i) instanceof JSONArray) {
                    element = this.unmarshallPrimitiveArray(jsonArray.getJSONArray(i), clazz);
                    list.add(element);
                    componentType = element.getClass();
                } else {
                    if (clazz.equals(Byte.TYPE)) {
                        element = new byte[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((byte[]) element)[j] = (byte) jsonArray.getInt(j);
                        return element;
                    }
                    if (clazz.equals(Short.TYPE)) {
                        element = new short[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((short[]) element)[j] = (short) jsonArray.getInt(j);
                        return element;
                    }
                    if (clazz.equals(Integer.TYPE)) {
                        element = new int[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((int[]) element)[j] = jsonArray.getInt(j);
                        return element;
                    }
                    if (clazz.equals(Long.TYPE)) {
                        element = new long[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((long[]) element)[j] = (long) jsonArray.getInt(j);
                        return element;
                    }
                    if (clazz.equals(Float.TYPE)) {
                        element = new float[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((float[]) element)[j] = (float) jsonArray.getDouble(i);
                        return element;
                    }
                    if (clazz.equals(Double.TYPE)) {
                        element = new double[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((double[]) element)[j] = jsonArray.getDouble(i);
                        return element;
                    }
                    if (clazz.equals(Character.TYPE)) {
                        element = new char[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((char[]) element)[j] = (jsonArray.getString(j) != null && jsonArray.getString(j).length() > 0) ? jsonArray.getString(j).charAt(0) : ((char[]) element)[j];
                        return element;
                    }
                    if (clazz.equals(Boolean.TYPE)) {
                        element = new boolean[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); j++) ((boolean[]) element)[j] = jsonArray.getBoolean(j);
                        return element;
                    }
                }
            }
        } catch (Exception e) {
            throw new JAJBUnMarshallException(e.getMessage(), e);
        }
        return list.toArray((Object[]) Array.newInstance(componentType, list.size()));
    }

    /**
	 * This method tries to build a JSONObject with the passed parameter object and its class type represented
	 * by passed clazz parameter. If object parameter and all its attributes are pojos and you don't need
	 * mapping features (like inheritance, conversions, factories, etc.) it works well. Otherwise could be
	 * data loss or errors.
	 * @param object the object to convert in JSONObject.
	 * @param clazz the class type of object parameter.
	 * @return a JSONObject that represents the passed object parameter.
	 * @throws JAJBMarshallException
	 */
    private JSONObject marshallSimpleObject(Object object, Class<?> clazz) throws JAJBMarshallException {
        JAJBIntrospectorWrapper iw = new JAJBIntrospectorWrapper(clazz);
        JSONObject jsonObject = new JSONObject();
        for (Property property : iw.getProperties()) {
            try {
                if (Utility.isWrapper(property.getType())) jsonObject.put(property.getName(), iw.invokeGetter(object, property.getName())); else if (property.getType().equals(Date.class)) jsonObject.put(property.getName(), format.format((Date) iw.invokeGetter(object, property.getName()))); else if (property.getType().isArray() || Collection.class.isAssignableFrom(property.getType())) jsonObject.put(property.getName(), this.marshallArray(iw.invokeGetter(object, property.getName()), property.getType())); else jsonObject.put(property.getName(), this.marshallSimpleObject(iw.invokeGetter(object, property.getName()), property.getType()));
            } catch (Exception e) {
                throw new JAJBMarshallException(e.getMessage(), e);
            }
        }
        return jsonObject;
    }

    /**
	 * This method tries to build a JSONArray with the passed parameter object that should be an array.
	 * If the components of the array represented by passed object parameter and all their attributes are pojos
	 * and you don't need mapping features (like inheritance, conversions, factories, etc.) it works well.
	 * @param object the array to transform in JSONArray
	 * @param clazz
	 * @return
	 * @throws JAJBMarshallException
	 */
    private JSONArray marshallArray(Object object, Class<?> clazz) throws JAJBMarshallException {
        Class<?> componentType = clazz;
        while (componentType.isArray()) componentType = componentType.getComponentType();
        if (componentType.isPrimitive()) return this.marshallPrimitiveArray(object);
        JSONArray jsonArray = new JSONArray();
        try {
            Object element = null;
            for (int i = 0; i < ((Object[]) object).length; i++) {
                element = ((Object[]) object)[i];
                if (element == null) jsonArray.put(i, JSONObject.NULL); else if (element.getClass().isArray()) jsonArray.put(i, this.marshallArray(element, clazz)); else if (Utility.isWrapper(element.getClass())) jsonArray.put(i, (element)); else if (element.getClass().equals(Date.class)) jsonArray.put(i, this.format.format((Date) element)); else jsonArray.put(i, this.marshallSimpleObject(element, clazz));
            }
        } catch (Exception e) {
            throw new JAJBMarshallException(e.getMessage(), e);
        }
        return jsonArray;
    }

    /**
	 * This method builds a JSONArray composed by primitive types using passed object parameter or if object
	 * is a multi nested array (for example int[][][]) builds a multi nested JSONArray that contains primitive types
	 * in its last nested level. If you don't need mapping features (like conversions, factories, etc.)
	 * this works well.
	 * @param object the primitive type's array (or multi nested primitive type's array) to transform into a JSONArray
	 * (or multi nested JSONArray).
	 * @return a JSONArray that represents the primitive type's passed array into object parameter.
	 * @throws JAJBMarshallException
	 */
    private JSONArray marshallPrimitiveArray(Object object) throws JAJBMarshallException {
        JSONArray jsonArray = new JSONArray();
        JSONArray element = new JSONArray();
        try {
            if (object.getClass().isArray() && !(object.getClass().getComponentType().isPrimitive())) {
                for (int i = 0; i < ((Object[]) object).length; i++) {
                    element = (JSONArray) this.marshallPrimitiveArray(((Object[]) object)[i]);
                    jsonArray.put(i, element);
                }
            } else {
                if (object.getClass().getComponentType().equals(Byte.TYPE)) {
                    for (int i = 0; i < ((byte[]) object).length; i++) element.put(i, ((byte[]) object)[i]);
                    return element;
                }
                if (object.getClass().getComponentType().equals(Short.TYPE)) {
                    for (int i = 0; i < ((short[]) object).length; i++) element.put(i, ((short[]) object)[i]);
                    return element;
                }
                if (object.getClass().getComponentType().equals(Integer.TYPE)) {
                    for (int i = 0; i < ((int[]) object).length; i++) element.put(i, ((int[]) object)[i]);
                    return element;
                }
                if (object.getClass().getComponentType().equals(Long.TYPE)) {
                    for (int i = 0; i < ((long[]) object).length; i++) element.put(i, ((long[]) object)[i]);
                    return element;
                }
                if (object.getClass().getComponentType().equals(Float.TYPE)) {
                    for (int i = 0; i < ((float[]) object).length; i++) element.put(i, ((float[]) object)[i]);
                    return element;
                }
                if (object.getClass().getComponentType().equals(Double.TYPE)) {
                    for (int i = 0; i < ((double[]) object).length; i++) element.put(i, ((double[]) object)[i]);
                    return element;
                }
                if (object.getClass().getComponentType().equals(Character.TYPE)) {
                    for (int i = 0; i < ((char[]) object).length; i++) element.put(i, ((char[]) object)[i]);
                    return element;
                }
                if (object.getClass().getComponentType().equals(Boolean.TYPE)) {
                    for (int i = 0; i < ((boolean[]) object).length; i++) element.put(i, ((boolean[]) object)[i]);
                    return element;
                }
            }
        } catch (Exception e) {
            throw new JAJBMarshallException(e.getMessage(), e);
        }
        return jsonArray;
    }

    /**
	 * This method allow you to set your preferred pattern for Date conversions.
	 * The default pattern is
	 * 
	 * yyyy-MM-dd'T'HH:mm:ss'Z'ZZ
	 * 
	 * @param pattern the pattern string to use for Date conversions.
	 */
    public void setDateFormatPattern(String dateFormatPattern) {
        this.format = new SimpleDateFormat(dateFormatPattern);
    }
}
