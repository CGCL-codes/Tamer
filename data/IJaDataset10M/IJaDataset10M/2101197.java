package com.j2js;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * This class is used to convert Java Serializable object to javascript. It
 * follows JSON. only public and non transiant properties of an object will be
 * converted to javascript object properties. e.g: <code>
 * <pre>
 * public class User{
 *    public int id = 10;
 *    private String name=&quot;venkoba&quot;;
 *    public transient address = &quot;Bangalore&quot;;
 * }
 * JSUtil.convert(new user()) =&gt; {&quot;id&quot;:10}
 * </pre>
 * </code>
 * <h6>(since name is private and address is transient, these will not be added
 * to response )</h6>
 * 
 * <pre>
 * It has the below advantages:
 * 
 *  1. supports all the datatypes available in java
 *  2. support for java.util.Map
 *  3. support for java.util.Collection classes like List,ArrayList
 *  4. supports java Arrays,Enum
 * </pre>
 * 
 * @author vdagudu
 * @since JDK 5.0
 * @version 1.0
 * 
 */
public class JSUtil {

    /**
	 * This method is used to convert serializable java object into javascript.
	 * 
	 * @param object
	 * @return javascript string
	 */
    public static String convert(Object object) {
        return convertToJS(object).toString();
    }

    private static StringBuffer convertToJS(Object o) {
        StringBuffer retVal = new StringBuffer();
        try {
            if (o == null) {
                retVal.append("null");
            } else if (o instanceof Map) {
                retVal.append(convertToJS((Map) o));
            } else if (o instanceof Collection) {
                retVal.append(convertToJS((Collection) o));
            } else if (o instanceof String) {
                retVal.append("\"").append(URLEncoder.encode(o.toString(), "UTF-8")).append("\"");
            } else if (o instanceof Number) {
                retVal.append(o);
            } else if (o instanceof Character) {
                retVal.append("\"").append(URLEncoder.encode(o.toString(), "UTF-8")).append("\"");
            } else if (o instanceof Boolean) {
                Boolean b = (Boolean) o;
                retVal.append(b.booleanValue());
            } else if (o.getClass().isArray()) {
                retVal.append(convertArrayToJS(o));
            } else if (o.getClass().isEnum()) {
                System.out.println("Still under development");
            } else if (o instanceof Serializable) {
                retVal.append("{");
                Class clazz = o.getClass();
                int fieldCount = 0;
                System.out.println(clazz);
                while (clazz != null && !clazz.equals(Object.class)) {
                    System.out.println(clazz);
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        try {
                            field.setAccessible(true);
                            retVal.append(getFieldJS(field, o));
                            fieldCount++;
                        } catch (IllegalAccessException exception) {
                        }
                    }
                    clazz = clazz.getSuperclass();
                }
                if (fieldCount > 0) {
                    retVal = new StringBuffer(retVal.substring(0, retVal.length() - 2));
                } else {
                    retVal.append("\"").append(o.toString()).append("\"");
                }
                retVal.append("}");
            }
        } catch (StackOverflowError e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return retVal;
    }

    private static StringBuffer getFieldJS(Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
        StringBuffer retVal = new StringBuffer();
        StringBuffer value = new StringBuffer();
        Object fieldObject = field.get(object);
        int modifier = field.getModifiers();
        if (!Modifier.isTransient(modifier)) {
            value = convertToJS(fieldObject);
            retVal.append("\"").append(field.getName()).append("\" : ").append(value).append(", ");
        }
        return retVal;
    }

    private static StringBuffer convertToJS(Map m) {
        StringBuffer retVal = new StringBuffer();
        Set<Map.Entry> s = m.entrySet();
        retVal.append("{");
        for (Map.Entry entry : s) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            retVal.append("\"");
            retVal.append(key).append("\" : ").append(convertToJS(value)).append(", ");
        }
        if (s.size() > 0) {
            retVal = new StringBuffer(retVal.substring(0, retVal.length() - 2));
        }
        retVal.append("}");
        return retVal;
    }

    private static StringBuffer convertArrayToJS(Object array) {
        StringBuffer retVal = new StringBuffer();
        retVal.append("[");
        for (int i = 0; i < Array.getLength(array); i++) {
            retVal.append(convertToJS(Array.get(array, i)));
            retVal.append(", ");
        }
        if (Array.getLength(array) > 0) {
            retVal = new StringBuffer(retVal.substring(0, retVal.length() - 2));
        }
        retVal.append("]");
        return retVal;
    }

    private static StringBuffer convertToJS(Collection c) {
        StringBuffer retVal = new StringBuffer();
        retVal.append("[");
        Iterator i = c.iterator();
        if (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Collection) {
                retVal.append(convertToJS(o));
            } else {
                retVal.append(convertToJS(o));
            }
        }
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Collection) {
                retVal.append(", ");
                retVal.append(convertToJS(o));
            } else {
                retVal.append(", ");
                retVal.append(convertToJS(o));
            }
        }
        retVal.append("]");
        return retVal;
    }
}
