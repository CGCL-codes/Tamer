package net.sf.imca.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import net.sf.imca.model.ImcaBO;

/**
 * Utilities for common reflection tasks.
 *
 * @author dougculnane
 */
public class ReflectionUtil {

    public static final Object[] NO_PARAMS = new Object[] {};

    public static final String ENTITY = "Entity";

    private static final String GET = "get";

    private static final String SET = "set";

    private static final String ID = "Id";

    private static final String CLASS = "Class";

    @SuppressWarnings("unchecked")
    public static void replaceDBObject(Object dbObject, Object newObject) throws Exception {
        Method[] methods = newObject.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().startsWith(GET)) {
                String methodName = methods[i].getName().substring(3);
                if (!(ID.equals(methodName) || CLASS.equals(methodName))) {
                    Class dataType = methods[i].getReturnType();
                    Object value = methods[i].invoke(newObject, NO_PARAMS);
                    if (value != null) {
                        Method dbObjectMethod = dbObject.getClass().getMethod(SET + methodName, dataType);
                        dbObjectMethod.invoke(dbObject, value);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void mergeDBObject(Object dbObject, Object newObject) throws Exception {
        Method[] methods = newObject.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().startsWith(GET)) {
                String setterName = methods[i].getName().substring(3);
                if (!ID.equals(setterName)) {
                    Class dataType = methods[i].getReturnType();
                    Object value = methods[i].invoke(newObject, NO_PARAMS);
                    if (value != null) {
                        if (!"".equals(value.toString())) {
                            Method dbObjectMethod = dbObject.getClass().getMethod(SET + setterName, dataType);
                            dbObjectMethod.invoke((new Object[] { dataType }), value);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Object getObject(String className) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class clazz = ReflectionUtil.class.getClassLoader().loadClass(className);
        Constructor constructor = clazz.getConstructor();
        Object object = constructor.newInstance();
        return object;
    }

    public static String[] getSetMethodNames(Object entity) {
        return getMethodNamesStartingWith(entity, SET);
    }

    public static String[] getGetMethodNames(Object entity) {
        return getMethodNamesStartingWith(entity, GET);
    }

    private static String[] getMethodNamesStartingWith(Object entity, String startingWith) {
        Method[] methods = entity.getClass().getMethods();
        Vector<String> methodVec = new Vector<String>();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().startsWith(startingWith)) {
                methodVec.add(methods[i].getName());
            }
        }
        String[] methodNames = new String[methodVec.size()];
        for (int i = 0; i < methodNames.length; i++) {
            methodNames[i] = methodVec.get(i);
        }
        return methodNames;
    }

    @SuppressWarnings("unchecked")
    public static void setEntityValuesFromParameterMap(Object entity, Hashtable<String, String> parameterMap) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, ParseException {
        String[] setMethodNames = ReflectionUtil.getSetMethodNames(entity);
        for (int i = 0; i < setMethodNames.length; i++) {
            EditDataService service = new EditDataService();
            String value = parameterMap.get(setMethodNames[i].substring(3));
            if (value != null) {
                if ((SET + ID).equals(setMethodNames[i])) {
                    if (value != null && "".equals(value)) {
                        Method setMedthod = entity.getClass().getMethod(SET + ID, int.class);
                        setMedthod.invoke(entity, new Long(value).longValue());
                    }
                } else {
                    Method getMethod = entity.getClass().getMethod(GET + setMethodNames[i].substring(3));
                    Class dataType = getMethod.getReturnType();
                    Method setMethod = entity.getClass().getMethod(setMethodNames[i], dataType);
                    if ("java.lang.String".equals(dataType.getName())) {
                        setMethod.invoke(entity, value);
                    } else if ("boolean".equals(dataType.getName())) {
                        Boolean bol = new Boolean(value);
                        setMethod.invoke(entity, bol.booleanValue());
                    } else if ("int".equals(dataType.getName())) {
                        Integer id = new Integer(value);
                        setMethod.invoke(entity, id.intValue());
                    } else if ("double".equals(dataType.getName())) {
                        Double d = new Double(value);
                        setMethod.invoke(entity, d.doubleValue());
                    } else if ("java.util.Date".equals(dataType.getName())) {
                        Date date = ImcaBO.parseDate(value);
                        setMethod.invoke(entity, date);
                    } else if (dataType.getName().endsWith(ENTITY)) {
                        if (value.length() != 0) {
                            Object childEntity = service.findEntity(dataType, new Long(value.trim()).longValue());
                            if (childEntity != null) {
                                setMethod.invoke(entity, childEntity);
                            }
                        }
                    } else if ("java.util.Collection".equals(dataType.getName())) {
                        String temp = setMethod.getGenericParameterTypes()[0].toString();
                        String entityClassName = temp.substring(temp.indexOf("<") + 1, temp.indexOf(">"));
                        String[] values = value.split(",");
                        Collection<Object> children = new Vector<Object>();
                        for (int j = 0; j < values.length; j++) {
                            if (values[j].trim().length() > 0) {
                                Object childEntity = service.findEntity(getObject(entityClassName).getClass(), new Long(values[j].trim()).longValue());
                                children.add(childEntity);
                            }
                        }
                        setMethod.invoke(entity, children);
                    }
                }
            }
        }
    }

    public static Vector<Object> getChildEntities(Object dbObject) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String[] getMethodNames = ReflectionUtil.getGetMethodNames(dbObject);
        Vector<Object> entities = new Vector<Object>();
        for (int i = 0; i < getMethodNames.length; i++) {
            if (!(GET + CLASS).equals(getMethodNames[i])) {
                Method method = dbObject.getClass().getMethod(getMethodNames[i]);
                if (method.getReturnType().getName().endsWith(ENTITY)) {
                    entities.add(method.invoke(dbObject, NO_PARAMS));
                }
            }
        }
        return entities;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Class> getClasses(String packageName) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<Class>();
        java.io.File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = '/' + packageName.replace('.', '/');
            java.net.URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new java.io.File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + files[i].substring(0, files[i].length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
        }
        return classes;
    }

    public static String getEntityId(Object entity) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getIdMethod = entity.getClass().getMethod("getId");
        getIdMethod.invoke(entity, NO_PARAMS);
        return getIdMethod.invoke(entity, NO_PARAMS).toString();
    }
}
