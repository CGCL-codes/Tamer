package com.wox.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FillField {

    /**
 * 	To fill the values in a map into an object
 * @param bObj		basic object,the values will be fill into it
 * @param paramMap	values are stored in this map
 */
    public static void fill(Object bObj, Map<String, String> paramMap) {
        HashMap<String, Object> parsedMap;
        parsedMap = parseParams(paramMap);
        fillValue(bObj, parsedMap);
    }

    @SuppressWarnings("unchecked")
    private static void fillValue(Object bObj, HashMap<String, Object> parsedMap) {
        Set<String> keyNames = parsedMap.keySet();
        for (String keyName : keyNames) {
            Object valueObj = parsedMap.get(keyName);
            if (valueObj instanceof Map<?, ?>) {
                HashMap<String, Object> subMap = (HashMap<String, Object>) valueObj;
                Class<?> objClass = bObj.getClass();
                try {
                    Field field = objClass.getDeclaredField(keyName);
                    Class<?> fieldClass = field.getType();
                    Object fieldObject = fieldClass.newInstance();
                    String setName = getSetName(keyName);
                    Method setMethod = objClass.getDeclaredMethod(setName, fieldClass);
                    setMethod.invoke(bObj, fieldObject);
                    fillValue(fieldObject, subMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                fillBasicValue(bObj, keyName, valueObj);
            }
        }
    }

    private static void fillBasicValue(Object bObj, String keyName, Object valueObj) {
        Class<?> objClass = bObj.getClass();
        Object basicObj = null;
        try {
            Field field = objClass.getDeclaredField(keyName);
            Class<?> fieldClass = field.getType();
            if (fieldClass.isArray()) {
                Class<?> componentClass = fieldClass.getComponentType();
                Object[] objects;
                if (valueObj.getClass().isArray()) {
                    String[] valueStrings = (String[]) valueObj;
                    objects = (Object[]) Array.newInstance(componentClass, valueStrings.length);
                    for (int i = 0; i < valueStrings.length; i++) {
                        Object o = getBasicValue(valueStrings[i], componentClass);
                        objects[i] = o;
                    }
                } else {
                    objects = (Object[]) Array.newInstance(componentClass, 1);
                    objects[0] = getBasicValue((String) valueObj, componentClass);
                }
                basicObj = objects;
            } else basicObj = getBasicValue((String) valueObj, fieldClass);
            String setName = getSetName(keyName);
            Method setMethod = objClass.getDeclaredMethod(setName, fieldClass);
            setMethod.invoke(bObj, basicObj);
        } catch (Exception e) {
            if (!(e instanceof NoSuchFieldException)) e.printStackTrace();
        }
    }

    /**
	 * Parse String value to basic value
	 *<P> Only Integer(int),Float(float),Character(char),Double(double),String supported,BigDecimal otherwise it will return null object
	 *</P>
	 * @param valueString
	 * @param componentClass
	 * @return
	 */
    private static Object getBasicValue(String valueString, Class<?> componentClass) {
        Object basicObj = null;
        if (componentClass.equals(Integer.class) || componentClass.equals(int.class)) {
            basicObj = Integer.parseInt(valueString);
        } else if (componentClass.equals(Float.class) || componentClass.equals(float.class)) {
            basicObj = Float.parseFloat(valueString);
        } else if (componentClass.equals(Character.class) || componentClass.equals(char.class)) {
            if (valueString.length() != 0) basicObj = valueString.charAt(0);
        } else if (componentClass.equals(Double.class) || componentClass.equals(double.class)) {
            basicObj = Double.parseDouble(valueString);
        } else if (componentClass.equals(Boolean.class) || componentClass.equals(boolean.class)) {
            basicObj = Boolean.parseBoolean(valueString);
        } else if (componentClass.equals(BigDecimal.class)) basicObj = new BigDecimal(valueString); else if (componentClass.equals(String.class)) {
            basicObj = valueString;
        }
        return basicObj;
    }

    /**
	 * To make the get function name 
	 * @param keyName
	 * @return
	 */
    public static String getSetName(String keyName) {
        return "set" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1);
    }

    /**
	 * 
	 * @param paramMap
	 * @return
	 */
    private static HashMap<String, Object> parseParams(Map<String, String> paramMap) {
        HashMap<String, Object> parsedMap = new HashMap<String, Object>();
        Set<String> paramNames = paramMap.keySet();
        for (String paramName : paramNames) {
            String[] splitNames = paramName.split("[\\.]");
            parseLeft1(parsedMap, paramMap.get(paramName), splitNames, 0);
        }
        return parsedMap;
    }

    @SuppressWarnings("unchecked")
    private static void parseLeft1(HashMap<String, Object> parsedMap, Object value, String[] splitNames, int i) {
        if (i < splitNames.length - 1) {
            Object subObj = parsedMap.get(splitNames[i]);
            if (subObj == null) {
                HashMap<String, Object> subParsedMap = new HashMap<String, Object>();
                parsedMap.put(splitNames[i], subParsedMap);
                parseLeft2(subParsedMap, value, splitNames, i + 1);
            } else {
                HashMap<String, Object> subParsedMap = (HashMap<String, Object>) subObj;
                parseLeft1(subParsedMap, value, splitNames, i + 1);
            }
        } else parsedMap.put(splitNames[i], value);
    }

    private static void parseLeft2(HashMap<String, Object> parsedMap, Object value, String[] splitNames, int i) {
        if (i < splitNames.length - 1) {
            HashMap<String, Object> subParsedMap = new HashMap<String, Object>();
            parsedMap.put(splitNames[i], subParsedMap);
            parseLeft2(subParsedMap, value, splitNames, i + 1);
        } else parsedMap.put(splitNames[i], value);
    }
}
