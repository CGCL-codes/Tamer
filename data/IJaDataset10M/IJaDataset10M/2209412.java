package ssg.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author ssg
 */
public class CloneHelper {

    /**
     * Explicitly invokes clone() method or, if not cloneable,
     * tries to create object instanceand copy its fields.
     *
     * Requirements to object:
     * - implement Cloneable as public method 'clone()'
     *  OR
     * - provide public parameterless constructor and get/set methods for protected/private fields.
     *
     * @param instance
     * @return
     * @throws CloneNotSupportedException
     */
    public static Object reflectiveClone(Object instance) throws CloneNotSupportedException {
        if (instance == null) {
            return null;
        }
        Object o = null;
        try {
            Method m = instance.getClass().getMethod("clone");
            m.setAccessible(true);
            o = m.invoke(instance);
        } catch (Throwable th) {
            try {
                o = createObject(instance.getClass(), (Object[]) null);
                tryCopyFields(instance, o, (String[]) null);
            } catch (Throwable th2) {
                throw new CloneNotSupportedException("Failed to clone via reflection: " + th.getMessage() + "; " + th2.getMessage());
            }
        }
        return o;
    }

    /**
     * Tries to assign name to object by invoking setName(String) method.
     * If failed - no problem, just ignore
     * @param instance
     * @param name
     */
    public static void tryToSetName(Object o, String name) {
        if (o == null) {
            return;
        }
        try {
            Method m = o.getClass().getMethod("setName", new Class[] { String.class });
            if (m != null) {
                m.invoke(o, (Object[]) new String[] { name });
            }
        } catch (Throwable th) {
            try {
                o.getClass().getField("name").set(o, name);
            } catch (Throwable th2) {
            }
        }
    }

    /**
     * Tries to assign name to object by invoking setName(String) method.
     * If failed - no problem, just ignore
     * @param instance
     * @param name
     */
    public static String tryToGetName(Object o) {
        if (o == null) {
            return null;
        }
        try {
            Method m = o.getClass().getMethod("getName", (Class[]) null);
            if (m != null) {
                return (String) m.invoke(o, (Object[]) null);
            }
        } catch (Throwable th) {
            try {
                return (String) o.getClass().getField("name").get(o);
            } catch (Throwable th2) {
            }
        }
        return null;
    }

    /**
     * Tries to assign UID to object by invoking setUID(UID) method.
     * If failed - no problem, just ignore
     * @param instance
     * @param name
     */
    public static boolean tryToSetUID(Object o, UID uid) {
        if (o == null) {
            return false;
        }
        try {
            Method m = o.getClass().getMethod("setUID", new Class[] { UID.class });
            if (m != null) {
                m.invoke(o, (Object[]) new Object[] { uid });
                return true;
            }
        } catch (Throwable th) {
            try {
                o.getClass().getField("uid").set(o, uid);
            } catch (Throwable th2) {
            }
        }
        return false;
    }

    /**
     * Tries to retrieve UID from object by invoking getUID() method.
     * If failed - no problem, just ignore and return null
     * @param instance
     * @param name
     */
    public static UID tryToGetUID(Object o) {
        if (o == null) {
            return null;
        }
        try {
            Method m = o.getClass().getMethod("getUID", (Class[]) null);
            if (m != null) {
                UID uid = (UID) m.invoke(o, (Object[]) null);
                return uid;
            }
        } catch (Throwable th) {
            try {
                return (UID) o.getClass().getField("uid").get(o);
            } catch (Throwable th2) {
            }
        }
        return null;
    }

    /**
     * Tries to invoke for provided object named method with single parameter.
     * If failed - no problem, just ignore
     * @param instance
     * @param name
     */
    public static void tryToInvoke(Object o, String methodName, Object value) {
        if (o == null) {
            return;
        }
        try {
            Method[] ms = o.getClass().getMethods();
            Method m = null;
            for (Method mm : ms) {
                if (mm.getName().equals(methodName)) {
                    Class[] ps = mm.getParameterTypes();
                    if (ps != null && ps.length == 1) {
                        if (value == null || ps[0].isAssignableFrom(value.getClass())) {
                            m = mm;
                            break;
                        }
                    }
                }
            }
            if (m != null) {
                m.invoke(o, new Object[] { value });
            }
        } catch (Throwable th) {
        }
    }

    /**
     * Tries to return field value for object.
     * If failed - tries to return it for upcased field name.
     *
     * @param o
     * @param fieldName
     * @return
     */
    public static Object tryToGetField(Object o, String fieldName, Object defaultValue) {
        Field f = null;
        try {
            if (o instanceof Class) {
                f = ((Class) o).getField(fieldName);
            } else {
                f = o.getClass().getField(fieldName);
            }
            return f.get(o);
        } catch (Throwable th) {
            try {
                if (o instanceof Class) {
                    f = ((Class) o).getField(fieldName.toUpperCase());
                } else {
                    f = o.getClass().getField(fieldName.toUpperCase());
                }
                return f.get(o);
            } catch (Throwable th2) {
            }
        }
        return defaultValue;
    }

    /**
     * Creates collection (list) and add to it all items from source.
     * If null is passed then returns null.
     *
     * @param c
     * @return
     */
    public static List cloneCollection(Collection c, boolean cloneItems) throws CloneNotSupportedException {
        if (c == null) {
            return null;
        }
        List l = new LinkedList();
        if (cloneItems) {
            for (Object o : c) {
                if (o instanceof Cloneable) {
                    l.add(reflectiveClone(o));
                } else {
                    l.add(o);
                }
            }
        } else {
            l.addAll(c);
        }
        return l;
    }

    /**
     * Creates map (hash map) and add to it all items from source.
     * If null is passed then returns null.
     *
     * @param c
     * @return
     */
    public static Map cloneMap(Map c, boolean cloneItems) throws CloneNotSupportedException {
        if (c == null) {
            return null;
        }
        Map l = new HashMap();
        if (cloneItems) {
            for (Entry es : (Set<Entry>) c.entrySet()) {
                Object key = es.getKey();
                if (key instanceof Cloneable) {
                    key = reflectiveClone(key);
                }
                Object value = es.getValue();
                if (value instanceof Cloneable) {
                    value = reflectiveClone(value);
                }
                l.put(key, value);
            }
        } else {
            l.putAll(c);
        }
        return l;
    }

    /**
     * Creates properties instance and add to it all items from source.
     * If null is passed then returns null.
     *
     * @param c
     * @return
     */
    public static Properties cloneProperties(Properties c) {
        if (c == null) {
            return null;
        } else {
            return (Properties) c.clone();
        }
    }

    /**
     * Copies fields found in source to target either.
     * If fieldName is present then only named fields are copied.
     *
     * @param source
     * @param target
     * @param fieldNames
     * @return
     */
    public static int tryCopyFields(Object source, Object target, String[] fieldNames) {
        Map<String, Object> readers = getReaders(source, fieldNames);
        Map<String, Object> writers = getWriters(target, fieldNames);
        return tryCopyFields(source, target, readers, writers);
    }

    public static int tryCopyFields(Object source, Object target, Map<String, Object> readers, Map<String, Object> writers) {
        int res = 0;
        if (source == null || target == null || readers == null || writers == null || readers.size() == 0 || writers.size() == 0) {
            return res;
        }
        for (String name : readers.keySet()) {
            Object reader = readers.get(name);
            Object writer = writers.get(name);
            if (writer == null) {
                continue;
            }
            try {
                Object value = (reader instanceof Method) ? ((Method) reader).invoke(source, (Object[]) null) : ((Field) reader).get(source);
                if (writer instanceof Method) {
                    ((Method) writer).invoke(target, new Object[] { value });
                } else {
                    ((Field) writer).set(target, value);
                }
                res++;
            } catch (Throwable th) {
            }
        }
        return res;
    }

    public static Map<String, Object> getReaders(Object obj, String[] namesOnly) {
        if (obj != null) {
            return getReaders(obj.getClass(), namesOnly);
        } else {
            return null;
        }
    }

    public static Map<String, Object> getReaders(Class c, String[] namesOnly) {
        Map<String, Object> res = new HashMap<String, Object>();
        Map<String, Method> getters;
        Map<String, Field> fields;
        if (c == null) {
            return res;
        }
        Method[] ms = c.getMethods();
        getters = new HashMap<String, Method>();
        fields = new HashMap<String, Field>();
        for (Method m : ms) {
            String mn = m.getName();
            Class[] mps = m.getParameterTypes();
            if (mn.startsWith("get") && (mps == null || mps.length == 0)) {
                mn = mn.substring(3);
                m.setAccessible(true);
                getters.put(mn, m);
            }
        }
        for (Field f : c.getFields()) {
            if (!Collection.class.isAssignableFrom(f.getType())) {
                String fn = f.getName();
                if (getters.get(fn) == null) {
                    f.setAccessible(true);
                    fields.put(fn, f);
                }
            }
        }
        if (namesOnly != null) {
            for (int i = 0; i < namesOnly.length; i++) {
                if (getters.containsKey(namesOnly[i])) {
                    res.put(namesOnly[i], getters.get(namesOnly[i]));
                } else if (fields.containsKey(namesOnly[i])) {
                    res.put(namesOnly[i], fields.get(namesOnly[i]));
                }
            }
        } else {
            res.putAll(getters);
            res.putAll(fields);
        }
        return res;
    }

    public static Map<String, Object> getWriters(Object obj, String[] namesOnly) {
        if (obj != null) {
            return getWriters(obj.getClass(), namesOnly);
        } else {
            return null;
        }
    }

    public static Map<String, Object> getWriters(Class c, String[] namesOnly) {
        Map<String, Object> res = new HashMap<String, Object>();
        Map<String, Method> setters;
        Map<String, Field> fields;
        if (c == null) {
            return res;
        }
        Method[] ms = c.getMethods();
        setters = new HashMap<String, Method>();
        fields = new HashMap<String, Field>();
        for (Method m : ms) {
            String mn = m.getName();
            Class[] mps = m.getParameterTypes();
            if (mn.startsWith("set") && mps != null && mps.length == 1 && !Collection.class.isAssignableFrom(mps[0])) {
                mn = mn.substring(3);
                m.setAccessible(true);
                setters.put(mn, m);
            }
        }
        for (Field f : c.getFields()) {
            if (!Collection.class.isAssignableFrom(f.getType())) {
                String fn = f.getName();
                if (setters.get(fn) == null) {
                    f.setAccessible(true);
                    fields.put(fn, f);
                }
            }
        }
        if (namesOnly != null) {
            for (int i = 0; i < namesOnly.length; i++) {
                if (setters.containsKey(namesOnly[i])) {
                    res.put(namesOnly[i], setters.get(namesOnly[i]));
                } else if (fields.containsKey(namesOnly[i])) {
                    res.put(namesOnly[i], fields.get(namesOnly[i]));
                }
            }
        } else {
            res.putAll(setters);
            res.putAll(fields);
        }
        return res;
    }

    /**
     * Performs objects comparison based on reader values.
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean reflectiveEquals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        if (!obj1.getClass().equals(obj2.getClass())) {
            return false;
        }
        try {
            Map<String, Object> readers = getReaders(obj1, null);
            for (String key : readers.keySet()) {
                Object reader = readers.get(key);
                Object v1 = null;
                Object v2 = null;
                if (reader instanceof Method) {
                    v1 = ((Method) reader).invoke(obj1, (Object[]) null);
                    v2 = ((Method) reader).invoke(obj2, (Object[]) null);
                } else {
                    v1 = ((Field) reader).get(obj1);
                    v2 = ((Field) reader).get(obj2);
                }
                if (v1 == null && v2 == null) {
                    continue;
                } else if (v1 == null || v2 == null) {
                    return false;
                } else if (!v1.equals(v2)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves from object set of values via provided readers.
     *
     * @param obj
     * @param readers
     * @return
     * @throws ApplicationException
     */
    public static Map<String, Object> getValues(Object obj, Map<String, Object> readers) throws ApplicationException {
        Map<String, Object> res = new HashMap<String, Object>();
        if (obj == null || readers == null || readers.size() == 0) {
            return res;
        }
        try {
            for (String key : readers.keySet()) {
                Object r = readers.get(key);
                Object value = (r instanceof Method) ? ((Method) r).invoke(obj, (Object[]) null) : ((Field) r).get(obj);
                res.put(key, value);
            }
        } catch (Throwable th) {
            throw new ApplicationException("Failed to retrieve value from object with provided readers.", th);
        }
        return res;
    }

    public static Map<String, Object> getValues(Object obj) throws ApplicationException {
        return getValues(obj, getReaders(obj, null));
    }

    /**
     * Assigns to object values via provided writers.
     * 
     * @param obj
     * @param writers
     * @param values
     * @throws ApplicationException
     */
    public static void setValues(Object obj, Map<String, Object> writers, Map<String, Object> values) throws ApplicationException {
        if (obj == null || writers == null || writers.size() == 0 || values == null || values.size() == 0) {
            return;
        }
        try {
            for (String key : writers.keySet()) {
                Object w = writers.get(key);
                if (values.containsKey(key)) {
                    if (w instanceof Method) {
                        ((Method) w).invoke(obj, new Object[] { values.get(key) });
                    } else {
                        ((Field) w).set(obj, values.get(key));
                    }
                } else {
                }
            }
        } catch (Throwable th) {
            throw new ApplicationException("Failed to assign value from object with provided writers.", th);
        }
    }

    public static void setValues(Object obj, Map<String, Object> values) throws ApplicationException {
        setValues(obj, getWriters(obj, null), values);
    }

    public static Map<String, Class> toNameClassMap(Map<String, Object> items) {
        Map<String, Class> res = new HashMap<String, Class>();
        if (items == null || items.size() == 0) {
            return res;
        }
        for (String key : items.keySet()) {
            Object o = items.get(key);
            if (o instanceof Method) {
                Method m = (Method) o;
                if (m.getName().startsWith("get") || m.getName().startsWith("is")) {
                    res.put(key, m.getReturnType());
                } else if (m.getName().startsWith("set")) {
                    res.put(key, m.getParameterTypes()[0]);
                } else {
                    res.put(key, o.getClass());
                }
            } else if (o instanceof Field) {
                res.put(key, ((Field) o).getType());
            } else {
                res.put(key, o.getClass());
            }
        }
        return res;
    }

    public static Object createObject(Class c, Object[] params) throws ApplicationException {
        if (c == null) {
            return null;
        }
        try {
            Object obj = null;
            Class[] paramTypes = new Class[(params != null) ? params.length : 0];
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    paramTypes[i] = (params[i] != null) ? params[i].getClass() : null;
                }
            }
            Constructor[] ctors = c.getConstructors();
            Constructor ctor = null;
            for (Constructor ct : ctors) {
                Class[] cpTypes = ct.getParameterTypes();
                if (cpTypes.length == paramTypes.length) {
                    boolean matches = true;
                    for (int i = 0; i < cpTypes.length; i++) {
                        if (!(paramTypes[i] == null || cpTypes[i].isAssignableFrom(paramTypes[i]))) {
                            if (cpTypes[i].isPrimitive() && primitiveTypes.get(paramTypes[i]) != cpTypes[i]) {
                                matches = false;
                            }
                        }
                    }
                    if (matches) {
                        if (ctor == null) {
                            ctor = ct;
                        } else {
                            throw new ApplicationException("Failed to instantiate object due tu multiple matching constructors: " + ctor + "," + ct);
                        }
                    }
                }
            }
            if (ctor == null) {
                throw new ApplicationException("Failed to instantiate object: no matching constructor found.");
            }
            obj = ctor.newInstance(params);
            return obj;
        } catch (InvocationTargetException itex) {
            throw new ApplicationException("Failed to create instance for " + c.getName(), itex);
        } catch (InstantiationException iex) {
            throw new ApplicationException("Failed to create instance for " + c.getName(), iex);
        } catch (IllegalAccessException iaex) {
            throw new ApplicationException("Failed to create instance for " + c.getName(), iaex);
        }
    }

    public static Object stringToObject(String value, Class target) throws ApplicationException {
        return stringToObject(value, target, Locale.getDefault());
    }

    public static Object stringToObject(String value, Class target, Locale locale) throws ApplicationException {
        if (value == null || target == null) {
            return null;
        }
        try {
            Object val = null;
            Class c = primitive2Objective(target);
            if (c.equals(Byte.class)) {
                val = new Byte(Byte.parseByte(value));
            } else if (c.equals(Short.class)) {
                val = new Short(Short.parseShort(value));
            } else if (c.equals(Integer.class)) {
                val = new Integer(Integer.parseInt(value));
            } else if (c.equals(Long.class)) {
                val = new Long(Long.parseLong(value));
            } else if (c.equals(Float.class)) {
                val = new Float(Float.parseFloat(value));
            } else if (c.equals(Double.class)) {
                val = new Double(Double.parseDouble(value));
            } else if (c.equals(Date.class)) {
                val = new Date(value);
            }
            return val;
        } catch (Throwable th) {
            throw new ApplicationException("Failed to convert string['" + value + "'] to object[" + target.getName() + "]: " + th.getMessage(), th);
        }
    }

    public static Class primitive2Objective(Class primitive) {
        return (objectiveTypes.containsKey(primitive)) ? objectiveTypes.get(primitive) : primitive;
    }

    public static Map<Class, Class> primitiveTypes = new HashMap<Class, Class>() {

        {
            put(Byte.class, byte.class);
            put(Short.class, short.class);
            put(Integer.class, int.class);
            put(Long.class, long.class);
            put(Double.class, double.class);
            put(Float.class, float.class);
            put(Boolean.class, boolean.class);
        }
    };

    public static Map<Class, Class> objectiveTypes = new HashMap<Class, Class>() {

        {
            for (Entry<Class, Class> e : primitiveTypes.entrySet()) {
                put(e.getValue(), e.getKey());
            }
        }
    };
}
