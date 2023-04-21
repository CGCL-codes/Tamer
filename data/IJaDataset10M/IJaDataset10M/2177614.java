package org.nakedobjects.object.reflect.internal;

import org.nakedobjects.object.Aggregated;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecificationException;
import org.nakedobjects.object.Persistable;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.defaults.InternalNakedObject;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.ActionPeer;
import org.nakedobjects.object.reflect.FieldPeer;
import org.nakedobjects.object.reflect.ObjectTitle;
import org.nakedobjects.object.reflect.OneToManyAssociation;
import org.nakedobjects.object.reflect.OneToOneAssociation;
import org.nakedobjects.object.reflect.ReflectionException;
import org.nakedobjects.object.reflect.Reflector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.log4j.Category;

public class InternalReflector implements Reflector {

    private static final String ABOUT_FIELD_DEFAULT = "aboutFieldDefault";

    private static final String ABOUT_PREFIX = "about";

    private static final String DERIVE_PREFIX = "derive";

    private static final String GET_PREFIX = "get";

    private static final Category LOG = Category.getInstance(InternalReflector.class);

    private static final String SET_PREFIX = "set";

    /**
     * Invokes, by reflection, the Order method prefixed by the specified type
     * name. The returned string is tokenized - broken on the commas - and
     * returned in the array.
     */
    private static String[] readSortOrder(Class aClass, String type) {
        try {
            Method method = aClass.getMethod(type + "Order", new Class[0]);
            if (Modifier.isStatic(method.getModifiers())) {
                String s = (String) method.invoke(null, new Object[0]);
                if (s.trim().length() > 0) {
                    java.util.StringTokenizer st = new java.util.StringTokenizer(s, ",");
                    String[] a = new String[st.countTokens()];
                    int element = 0;
                    while (st.hasMoreTokens()) {
                        a[element] = st.nextToken().trim();
                        element++;
                    }
                    return a;
                } else {
                    return null;
                }
            } else {
                LOG.warn("method " + aClass.getName() + "." + type + "Order() must be decared as static");
            }
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        }
        return null;
    }

    /**
     * Returns the short name of the fully qualified name (including the package
     * name) . e.g. for com.xyz.example.Customer returns Customer.
     */
    protected static String shortClassName(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf('.') + 1);
    }

    private Class cls;

    private Method defaultAboutFieldMethod;

    private Method methods[];

    public InternalReflector(String name) throws ReflectionException {
        LOG.info("introspecting " + name);
        Class cls;
        try {
            cls = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Could not load class " + name, e);
        }
        if (!InternalNakedObject.class.isAssignableFrom(cls) && !cls.getName().startsWith("java.") && !Exception.class.isAssignableFrom(cls)) {
            throw new NakedObjectSpecificationException("Class must be InternalNakedObject: " + cls.getName());
        }
        if (!Modifier.isPublic(cls.getModifiers())) {
            throw new NakedObjectSpecificationException("A NakedObject class must be marked as public.  Error in " + cls);
        }
        this.cls = cls;
        methods = cls.getMethods();
    }

    public ActionPeer[] actionPeers(boolean forClass) {
        LOG.debug("  looking for action methods");
        Method defaultAboutMethod = findMethod(forClass, "aboutActionDefault", null, new Class[] { InternalAbout.class });
        LOG.debug(defaultAboutMethod == null ? "  no default about method for actions" : defaultAboutMethod.toString());
        Vector validMethods = new Vector();
        Vector actions = new Vector();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i] == null) {
                continue;
            }
            Method method = methods[i];
            if (Modifier.isStatic(method.getModifiers()) != forClass) {
                continue;
            }
            String[] prefixes = { "action", "explorationAction", "debugAction" };
            int prefix = -1;
            for (int j = 0; j < prefixes.length; j++) {
                if (method.getName().startsWith(prefixes[j])) {
                    prefix = j;
                    break;
                }
            }
            if (prefix == -1) {
                continue;
            }
            Class[] params = method.getParameterTypes();
            validMethods.addElement(method);
            LOG.info("  identified action " + method);
            String methodName = method.getName();
            methods[i] = null;
            String name = methodName.substring(prefixes[prefix].length());
            Class[] longParams = new Class[params.length + 1];
            longParams[0] = InternalAbout.class;
            System.arraycopy(params, 0, longParams, 1, params.length);
            String aboutName = "about" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            Method aboutMethod = findMethod(forClass, aboutName, null, longParams);
            if (aboutMethod == null) {
                aboutMethod = defaultAboutMethod;
            } else {
                LOG.debug("  with about method " + aboutMethod);
            }
            Action.Type action;
            action = new Action.Type[] { Action.USER, Action.EXPLORATION, Action.DEBUG }[prefix];
            ActionPeer local = createAction(method, name, aboutMethod, action);
            actions.addElement(local);
        }
        return convertToArray(actions);
    }

    public String[] actionSortOrder() {
        LOG.debug("  looking  for action sort order");
        return readSortOrder(cls, "action");
    }

    public String[] classActionSortOrder() {
        LOG.debug("  looking  for class action sort order");
        return readSortOrder(cls, "classAction");
    }

    public Hint classHint() {
        LOG.debug("  looking  for class about");
        try {
            InternalAbout about = new InternalAbout();
            cls.getMethod(ABOUT_PREFIX + shortName(), new Class[] { InternalAbout.class }).invoke(null, new Object[] { about });
            return about;
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        }
        return null;
    }

    private String className() {
        return cls.getName();
    }

    public void clearDirty(NakedObject object) {
    }

    private ActionPeer[] convertToArray(Vector actions) {
        ActionPeer results[] = new ActionPeer[actions.size()];
        Enumeration actionEnumeration = actions.elements();
        int i = 0;
        while (actionEnumeration.hasMoreElements()) {
            results[i++] = (ActionPeer) actionEnumeration.nextElement();
        }
        return (ActionPeer[]) results;
    }

    ActionPeer createAction(Method method, String name, Method aboutMethod, Action.Type action) {
        return new InternalAction(name, action, method, aboutMethod);
    }

    private void derivedFields(Vector fields) {
        Vector v = findPrefixedMethods(OBJECT, DERIVE_PREFIX, null, 0);
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            Method method = (Method) e.nextElement();
            LOG.info("  identified derived value method " + method);
            String name = method.getName();
            Method aboutMethod = findMethod(OBJECT, ABOUT_PREFIX + name, null, new Class[] { InternalAbout.class });
            if (aboutMethod == null) {
                aboutMethod = defaultAboutFieldMethod;
            }
            InternalOneToOneAssociation association = new InternalOneToOneAssociation(name, method.getReturnType(), method, null, null, null, aboutMethod);
            fields.addElement(association);
        }
    }

    public void destroyed(NakedObject object) {
    }

    public FieldPeer[] fields() {
        LOG.debug("  looking  for fields");
        Vector elements = new Vector();
        defaultAboutFieldMethod = findMethod(OBJECT, ABOUT_FIELD_DEFAULT, null, new Class[] { InternalAbout.class });
        valueFields(elements, String.class);
        derivedFields(elements);
        oneToManyAssociationFields(elements);
        oneToOneAssociationFields(elements);
        FieldPeer[] results = new FieldPeer[elements.size()];
        elements.copyInto(results);
        return results;
    }

    public String[] fieldSortOrder() {
        return readSortOrder(cls, "field");
    }

    /**
     * Returns a specific public methods that: have the specified prefix; have
     * the specified return type, or void, if canBeVoid is true; and has the
     * specified number of parameters. If the returnType is specified as null
     * then the return type is ignored.
     * 
     * @param forClass
     * @param name
     * @param returnType
     * @param paramTypes
     *                       the set of parameters the method should have, if null then is
     *                       ignored
     * @return Method
     */
    private Method findMethod(boolean forClass, String name, Class returnType, Class[] paramTypes) {
        method: for (int i = 0; i < methods.length; i++) {
            if (methods[i] == null) {
                continue;
            }
            Method method = methods[i];
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(method.getModifiers()) != forClass) {
                continue;
            }
            if (!method.getName().equals(name)) {
                continue;
            }
            if (returnType != null && returnType != method.getReturnType()) {
                continue;
            }
            if (paramTypes != null) {
                if (paramTypes.length != method.getParameterTypes().length) {
                    continue;
                }
                for (int c = 0; c < paramTypes.length; c++) {
                    if ((paramTypes[c] != null) && (paramTypes[c] != method.getParameterTypes()[c])) {
                        continue method;
                    }
                }
            }
            methods[i] = null;
            return method;
        }
        return null;
    }

    /**
     * Returns a Vector of public methods that: have the specified prefix; have
     * the specified return type, or void, if canBeVoid is true; and has the
     * specified number of parameters. If the returnType is specified as null
     * then the return type is ignored.
     */
    private Vector findPrefixedMethods(boolean forClass, String prefix, Class returnType, boolean canBeVoid, int paramCount) {
        Vector validMethods = new Vector();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i] == null) {
                continue;
            }
            Method method = methods[i];
            if (Modifier.isStatic(method.getModifiers()) != forClass) {
                continue;
            }
            boolean goodPrefix = method.getName().startsWith(prefix);
            boolean goodCount = method.getParameterTypes().length == paramCount;
            Class type = method.getReturnType();
            boolean goodReturn = (returnType == null) || (canBeVoid && (type == void.class)) || returnType.isAssignableFrom(type);
            if (goodPrefix && goodCount && goodReturn) {
                validMethods.addElement(method);
                methods[i] = null;
            }
        }
        return validMethods;
    }

    private Vector findPrefixedMethods(boolean forClass, String prefix, Class returnType, int paramCount) {
        return findPrefixedMethods(forClass, prefix, returnType, false, paramCount);
    }

    public String fullName() {
        return cls.getName();
    }

    public Object getExtension(Class cls) {
        return null;
    }

    public Class[] getExtensions() {
        return new Class[0];
    }

    public String[] getInterfaces() {
        Class[] interfaces = cls.getInterfaces();
        Class[] nakedInterfaces = new Class[interfaces.length];
        int validInterfaces = 0;
        for (int i = 0; i < interfaces.length; i++) {
            nakedInterfaces[validInterfaces++] = interfaces[i];
        }
        String[] interfaceNames = new String[validInterfaces];
        for (int i = 0; i < validInterfaces; i++) {
            interfaceNames[i] = nakedInterfaces[i].getName();
        }
        return interfaceNames;
    }

    public String getSuperclass() {
        Class superclass = cls.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return superclass.getName();
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(cls.getModifiers());
    }

    public boolean isDirty(NakedObject object) {
        return false;
    }

    public boolean isLookup() {
        return false;
    }

    public boolean isObject() {
        return NakedObject.class.isAssignableFrom(cls);
    }

    public boolean isPartOf() {
        return Aggregated.class.isAssignableFrom(cls);
    }

    public Persistable persistable() {
        return Persistable.TRANSIENT;
    }

    public boolean isValue() {
        return String.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
    }

    public void markDirty(NakedObject object) {
    }

    String[] names(Vector methods) {
        String[] names = new String[methods.size()];
        Enumeration e = methods.elements();
        int i = 0;
        while (e.hasMoreElements()) {
            Method method = (Method) e.nextElement();
            names[i++] = method.getName();
        }
        return names;
    }

    /**
     * Returns the details about the basic accessor/mutator methods. Based on
     * each suitable get... method a vector of OneToManyAssociation objects are
     * returned.
     * 
     * @see OneToManyAssociation
     */
    private void oneToManyAssociationFields(Vector associations) {
        Vector v = findPrefixedMethods(OBJECT, GET_PREFIX, Vector.class, 0);
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            Method getMethod = (Method) e.nextElement();
            LOG.info("  identified 1-many association method " + getMethod);
            String name = getMethod.getName().substring(GET_PREFIX.length());
            Method aboutMethod = findMethod(OBJECT, ABOUT_PREFIX + name, null, new Class[] { InternalAbout.class, null, boolean.class });
            Class aboutType = (aboutMethod == null) ? null : aboutMethod.getParameterTypes()[1];
            if (aboutMethod == null) {
                aboutMethod = defaultAboutFieldMethod;
            }
            Method addMethod = findMethod(OBJECT, "addTo" + name, void.class, null);
            if (addMethod == null) {
                addMethod = findMethod(OBJECT, "add" + name, void.class, null);
            }
            if (addMethod == null) {
                addMethod = findMethod(OBJECT, "associate" + name, void.class, null);
            }
            Method removeMethod = findMethod(OBJECT, "removeFrom" + name, void.class, null);
            if (removeMethod == null) {
                removeMethod = findMethod(OBJECT, "remove" + name, void.class, null);
            }
            if (removeMethod == null) {
                removeMethod = findMethod(OBJECT, "dissociate" + name, void.class, null);
            }
            if (addMethod == null || removeMethod == null) {
                LOG.error("there must be both add and remove methods for " + name + " in " + className());
            }
            Class removeType = (removeMethod == null) ? null : removeMethod.getParameterTypes()[0];
            Class addType = (addMethod == null) ? null : addMethod.getParameterTypes()[0];
            Class elementType = (aboutType == null) ? null : aboutType;
            elementType = (addType == null) ? elementType : addType;
            elementType = (removeType == null) ? elementType : removeType;
            if (((aboutType != null) && (aboutType != elementType)) || ((addType != null) && (addType != elementType)) || ((removeType != null) && (removeType != elementType))) {
                LOG.error("the add/remove/associate/dissociate/about methods in " + className() + " must " + "all deal with same type of object.  There are at least two different " + "types");
            }
            associations.addElement(new InternalOneToManyAssociation(name, elementType, getMethod, addMethod, removeMethod, aboutMethod));
        }
    }

    /**
     * Returns a vector of Association fields for all the get methods that use
     * NakedObjects.
     * 
     * @throws ReflectionException
     * 
     * @see OneToOneAssociation
     */
    private void oneToOneAssociationFields(Vector associations) throws ReflectionException {
        Vector v = findPrefixedMethods(OBJECT, GET_PREFIX, Object.class, 0);
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            Method getMethod = (Method) e.nextElement();
            LOG.info("  identified 1-1 association method " + getMethod);
            if (getMethod.getName().equals("getNakedClass")) {
                continue;
            }
            String name = getMethod.getName().substring(GET_PREFIX.length());
            Class[] params = new Class[] { getMethod.getReturnType() };
            Method aboutMethod = findMethod(OBJECT, ABOUT_PREFIX + name, null, new Class[] { InternalAbout.class, getMethod.getReturnType() });
            if (aboutMethod == null) {
                aboutMethod = defaultAboutFieldMethod;
            }
            Method addMethod = findMethod(OBJECT, "associate" + name, void.class, params);
            if (addMethod == null) {
                addMethod = findMethod(OBJECT, "add" + name, void.class, params);
            }
            Method removeMethod = findMethod(OBJECT, "dissociate" + name, void.class, null);
            if (removeMethod == null) {
                removeMethod = findMethod(OBJECT, "remove" + name, void.class, null);
            }
            Method setMethod = findMethod(OBJECT, SET_PREFIX + name, void.class, params);
            if (setMethod == null) {
                continue;
            }
            LOG.info("one-to-one association " + name + " ->" + addMethod);
            InternalOneToOneAssociation association = new InternalOneToOneAssociation(name, getMethod.getReturnType(), getMethod, setMethod, addMethod, removeMethod, aboutMethod);
            associations.addElement(association);
        }
    }

    public String pluralName() {
        try {
            return (String) cls.getMethod("pluralName", new Class[0]).invoke(null, new Object[0]);
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        }
        return null;
    }

    public String shortName() {
        String name = cls.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public String singularName() {
        try {
            Method method = cls.getMethod("singularName", new Class[0]);
            return (String) method.invoke(null, new Object[0]);
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        }
        return null;
    }

    public ObjectTitle title() {
        Method titleMethod = findMethod(OBJECT, "titleString", String.class, null);
        if (titleMethod == null) {
            titleMethod = findMethod(OBJECT, "title", String.class, null);
        }
        if (titleMethod == null) {
            return new ObjectTitle() {

                public String title(NakedObject object) {
                    return object.getObject().toString();
                }
            };
        } else {
            return new InternalObjectTitle(titleMethod);
        }
    }

    private Vector valueFields(Vector fields, Class type) {
        Vector v = findPrefixedMethods(OBJECT, GET_PREFIX, type, 0);
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            Method getMethod = (Method) e.nextElement();
            Class returnType = getMethod.getReturnType();
            String name = getMethod.getName().substring(GET_PREFIX.length());
            Method aboutMethod = findMethod(OBJECT, ABOUT_PREFIX + name, null, new Class[] { InternalAbout.class, returnType });
            if (aboutMethod == null) {
                aboutMethod = defaultAboutFieldMethod;
            }
            Method setMethod = findMethod(OBJECT, SET_PREFIX + name, null, new Class[] { returnType });
            Class[] params = new Class[] { returnType };
            if ((findMethod(OBJECT, SET_PREFIX + name, void.class, params) != null)) {
                LOG.error("the method set" + name + " is not needed for the NakedValue class " + className());
            }
            if (findMethod(OBJECT, "associate" + name, void.class, params) != null) {
                LOG.error("the method associate" + name + " is not needed for the NakedValue class " + className());
            }
            LOG.info("  value " + name + " ->" + getMethod);
            InternalOneToOneAssociation association = new InternalOneToOneAssociation(name, getMethod.getReturnType(), getMethod, setMethod, null, null, aboutMethod);
            fields.addElement(association);
        }
        return fields;
    }
}
