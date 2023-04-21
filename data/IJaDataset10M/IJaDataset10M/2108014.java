package org.apache.myfaces.trinidadinternal.util;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.TooManyListenersException;
import java.util.Vector;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.util.ClassLoaderUtils;

/**
 * The Introspector class provides a standard way for tools to learn about
 * the properties, events, and methods supported by a target Java Bean.
 * <p>
 * For each of those three kinds of information, the Introspector will
 * separately analyze the bean's class and superclasses looking for
 * either explicit or implicit information and use that information to
 * build a BeanInfo object that comprehensively describes the target bean.
 * <p>
 * For each class "Foo", explicit information may be available if there exists
 * a corresponding "FooBeanInfo" class that provides a non-null value when
 * queried for the information.   We first look for the BeanInfo class by
 * taking the full package-qualified name of the target bean class and
 * appending "BeanInfo" to form a new class name.  If this fails, then
 * we take the final classname component of this name, and look for that
 * class in each of the packages specified in the BeanInfo package search
 * path.
 * <p>
 * Thus for a class such as "sun.xyz.OurButton" we would first look for a
 * BeanInfo class called "sun.xyz.OurButtonBeanInfo" and if that failed we'd
 * look in each package in the BeanInfo search path for an OurButtonBeanInfo
 * class.  With the default search path, this would mean looking for
 * "sun.beans.infos.OurButtonBeanInfo".
 * <p>
 * If a class provides explicit BeanInfo about itself then we add that to
 * the BeanInfo information we obtained from analyzing any derived classes,
 * but we regard the explicit information as being definitive for the current
 * class and its base classes, and do not proceed any further up the superclass
 * chain.
 * <p>
 * If we don't find explicit BeanInfo on a class, we use low-level
 * reflection to study the methods of the class and apply standard design
 * patterns to identify property accessors, event sources, or public
 * methods.  We then proceed to analyze the class's superclass and add
 * in the information from it (and possibly on up the superclass chain).
 * <p>
 * This class differs from the standard Introspector in the following ways:
   <OL>
     <LI>Supports "has" as a prefix for boolean getters.
     <LI>Is smarter about not accidentally creating IndexedPropertyDescriptors.
     <LI>It handles SecurityManagers that don't allow access to all of the
         declared methods of a class gracefully.
     <LI>It caches only the method objects that it cares about.  Saving memory.
     <LI>It caches BeanInfos in more cases and makes fewer unnecessary copies
         when returning BeanInfos.
   </OL>
 * <p>
 * @since EWT 3.0
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/util/JavaIntrospector.java#0 $) $Date: 10-nov-2005.18:49:10 $
 */
public class JavaIntrospector {

    public static final int USE_ALL_BEANINFO = 1;

    public static final int IGNORE_IMMEDIATE_BEANINFO = 2;

    public static final int IGNORE_ALL_BEANINFO = 3;

    /**
   * Introspect on a Java bean and learn about all its properties, exposed
   * methods, and events.
   *
   * @param beanClass  The bean class to be analyzed.
   * @return  A BeanInfo object describing the target bean.
   * @exception IntrospectionException if an exception occurs during
   *              introspection.
   */
    public static BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException {
        GenericBeanInfo bi = (GenericBeanInfo) _sBeanInfoCache.get(beanClass);
        if (bi == null) {
            JavaIntrospector introspector = new JavaIntrospector(beanClass, null, USE_ALL_BEANINFO);
            bi = introspector._getBeanInfo();
            _sBeanInfoCache.put(beanClass, bi);
        }
        return new GenericBeanInfo(bi);
    }

    /**
   * Introspect on a Java bean and learn about all its properties, exposed
   * methods, and events, subnject to some comtrol flags.
   *
   * @param beanClass  The bean class to be analyzed.
   * @param flags  Flags to control the introspection.
   *     If flags == USE_ALL_BEANINFO then we use all of the BeanInfo
   *         classes we can discover.
   *     If flags == IGNORE_IMMEDIATE_BEANINFO then we ignore any
   *           BeanInfo associated with the specified beanClass.
   *     If flags == IGNORE_ALL_BEANINFO then we ignore all BeanInfo
   *           associated with the specified beanClass or any of its
   *         parent classes.
   * @return  A BeanInfo object describing the target bean.
   * @exception IntrospectionException if an exception occurs during
   *              introspection.
   */
    public static BeanInfo getBeanInfo(Class<?> beanClass, int flags) throws IntrospectionException {
        if (flags == USE_ALL_BEANINFO) {
            return getBeanInfo(beanClass);
        } else {
            return new JavaIntrospector(beanClass, null, flags)._getBeanInfo();
        }
    }

    /**
   * Introspect on a Java bean and learn all about its properties, exposed
   * methods, below a given "stop" point.
   *
   * @param bean The bean class to be analyzed.
   * @param stopClass The baseclass at which to stop the analysis.  Any
   *    methods/properties/events in the stopClass or in its baseclasses
   *    will be ignored in the analysis.
   * @exception IntrospectionException if an exception occurs during
   *              introspection.
   */
    public static BeanInfo getBeanInfo(Class<?> beanClass, Class<?> stopClass) throws IntrospectionException {
        if (stopClass == null) {
            return getBeanInfo(beanClass);
        } else {
            return new JavaIntrospector(beanClass, stopClass, USE_ALL_BEANINFO)._getBeanInfo();
        }
    }

    /**
   * Utility method to take a string and convert it to normal Java variable
   * name capitalization.  This normally means converting the first
   * character from upper case to lower case, but in the (unusual) special
   * case when there is more than one character and both the first and
   * second characters are upper case, we leave it alone.
   * <p>
   * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays
   * as "URL".
   *
   * @param  name The string to be decapitalized.
   * @return  The decapitalized version of the string.
   */
    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if ((name.length() > 1) && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
   * @return  The array of package names that will be searched in
   *        order to find BeanInfo classes.
   * <p>     This is initially set to {"sun.beans.infos"}.
   */
    public static String[] getBeanInfoSearchPath() {
        if (_sSearchPath == null) return null;
        return _sSearchPath.clone();
    }

    /**
   * Change the list of package names that will be used for
   *        finding BeanInfo classes.
   * @param path  Array of package names.
   */
    public static void setBeanInfoSearchPath(String path[]) {
        _sSearchPath = path == null ? null : path.clone();
    }

    /**
   * Flush all of the Introspector's internal caches.  This method is
   * not normally required.  It is normally only needed by advanced
   * tools that update existing "Class" objects in-place and need
   * to make the Introspector re-analyze existing Class objects.
   */
    public static void flushCaches() {
        _sBeanInfoCache.clear();
        _sPublicMethodCache.clear();
    }

    /**
   * Flush the Introspector's internal cached information for a given class.
   * This method is not normally required.  It is normally only needed
   * by advanced tools that update existing "Class" objects in-place
   * and need to make the Introspector re-analyze an existing Class object.
   *
   * Note that only the direct state associated with the target Class
   * object is flushed.  We do not flush state for other Class objects
   * with the same name, nor do we flush state for any related Class
   * objects (such as subclasses), even though their state may include
   * information indirectly obtained from the target Class object.
   *
   * @param targetClass  Class object to be flushed.
   */
    public static void flushFromCaches(Class<?> targetClass) {
        _sBeanInfoCache.remove(targetClass);
        _sPublicMethodCache.remove(targetClass);
    }

    private JavaIntrospector(Class<?> beanClass, Class<?> stopClass, int flags) throws IntrospectionException {
        _beanClass = beanClass;
        if (stopClass != null) {
            boolean isSuper = false;
            for (Class<?> c = beanClass.getSuperclass(); c != null; c = c.getSuperclass()) {
                if (c == stopClass) {
                    isSuper = true;
                }
            }
            if (!isSuper) {
                throw new IntrospectionException(_LOG.getMessage("NOT_SUPERCLASS_OF", new Object[] { stopClass.getName(), beanClass.getName() }));
            }
        }
        if (flags == USE_ALL_BEANINFO) {
            _informant = _findInformant(beanClass);
        }
        Class<?> superClass = beanClass.getSuperclass();
        if (superClass != stopClass) {
            int newFlags = flags;
            if (newFlags == IGNORE_IMMEDIATE_BEANINFO) {
                newFlags = USE_ALL_BEANINFO;
            }
            if ((stopClass == null) && (newFlags == USE_ALL_BEANINFO)) {
                _superBeanInfo = _sBeanInfoCache.get(superClass);
                if (_superBeanInfo == null) {
                    JavaIntrospector ins = new JavaIntrospector(superClass, null, USE_ALL_BEANINFO);
                    _superBeanInfo = ins._getBeanInfo();
                    _sBeanInfoCache.put(superClass, _superBeanInfo);
                }
            } else {
                JavaIntrospector ins = new JavaIntrospector(superClass, stopClass, newFlags);
                _superBeanInfo = ins._getBeanInfo();
            }
        }
        if (_informant != null) {
            _additionalBeanInfo = _informant.getAdditionalBeanInfo();
        }
        if (_additionalBeanInfo == null) {
            _additionalBeanInfo = new BeanInfo[0];
        }
    }

    private GenericBeanInfo _getBeanInfo() throws IntrospectionException {
        return new GenericBeanInfo(this, _informant);
    }

    /**
   * Find any bean info information that the creator of the bean may
   * have left around for us
   */
    private BeanInfo _findInformant(Class<?> beanClass) {
        String name = beanClass.getName() + "BeanInfo";
        try {
            return (BeanInfo) _instantiate(beanClass, name);
        } catch (Exception ex) {
            ;
        }
        try {
            if (_isSubclass(beanClass, BeanInfo.class)) {
                return (BeanInfo) beanClass.newInstance();
            }
        } catch (Exception ex) {
            ;
        }
        while (name.indexOf('.') > 0) {
            name = name.substring(name.indexOf('.') + 1);
        }
        for (int i = 0; i < _sSearchPath.length; i++) {
            try {
                String fullName = _sSearchPath[i] + "." + name;
                return (BeanInfo) _instantiate(beanClass, fullName);
            } catch (Exception ex) {
                ;
            }
        }
        return null;
    }

    /**
   * @return An array of PropertyDescriptors describing the editable
   * properties supported by the target bean.
   */
    PropertyDescriptor[] __getTargetPropertyInfo() throws IntrospectionException {
        Hashtable<String, PropertyDescriptor> properties = new Hashtable<String, PropertyDescriptor>();
        PropertyDescriptor[] explicit = null;
        if (_informant != null) {
            explicit = _informant.getPropertyDescriptors();
            int ix = _informant.getDefaultPropertyIndex();
            if ((ix >= 0) && (ix < explicit.length)) {
                _defaultPropertyName = explicit[ix].getName();
            }
        }
        if ((explicit == null) && (_superBeanInfo != null)) {
            PropertyDescriptor supers[] = _superBeanInfo.getPropertyDescriptors();
            for (int i = 0; i < supers.length; i++) {
                _addProperty(properties, supers[i]);
            }
            int ix = _superBeanInfo.getDefaultPropertyIndex();
            if ((ix >= 0) && (ix < supers.length)) {
                _defaultPropertyName = supers[ix].getName();
            }
        }
        for (int i = 0; i < _additionalBeanInfo.length; i++) {
            PropertyDescriptor additional[] = _additionalBeanInfo[i].getPropertyDescriptors();
            if (additional != null) {
                for (int j = 0; j < additional.length; j++) {
                    _addProperty(properties, additional[j]);
                }
            }
        }
        if (explicit != null) {
            for (int i = 0; i < explicit.length; i++) {
                _addProperty(properties, explicit[i]);
            }
        } else {
            Method methodList[] = _getPublicMethods(_beanClass);
            for (int i = 0; i < methodList.length; i++) {
                Method method = methodList[i];
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                String name = method.getName();
                Class<?>[] argTypes = method.getParameterTypes();
                Class<?> resultType = method.getReturnType();
                int argCount = argTypes.length;
                PropertyDescriptor pd = null;
                try {
                    if (argCount == 0) {
                        if (name.startsWith(_READ_PREFIX)) {
                            pd = new PropertyDescriptor(decapitalize(name.substring(_READ_PREFIX.length())), method, null);
                        } else if (resultType == boolean.class) {
                            for (int j = 0; j < _BOOLEAN_READ_PREFIXES.length; j++) {
                                String currPrefix = _BOOLEAN_READ_PREFIXES[j];
                                if (name.startsWith(currPrefix)) {
                                    pd = new PropertyDescriptor(decapitalize(name.substring(currPrefix.length())), method, null);
                                }
                            }
                        }
                    } else if (argCount == 1) {
                        if ((argTypes[0] == int.class) && name.startsWith(_READ_PREFIX)) {
                            pd = new IndexedPropertyDescriptor(decapitalize(name.substring(_READ_PREFIX.length())), null, null, method, null);
                        } else if ((resultType == void.class) && name.startsWith(_WRITE_PREFIX)) {
                            pd = new PropertyDescriptor(decapitalize(name.substring(_WRITE_PREFIX.length())), null, method);
                            if (_throwsException(method, PropertyVetoException.class)) {
                                pd.setConstrained(true);
                            }
                        }
                    } else if (argCount == 2) {
                        if ((argTypes[0] == int.class) && name.startsWith(_WRITE_PREFIX)) {
                            pd = new IndexedPropertyDescriptor(decapitalize(name.substring(_WRITE_PREFIX.length())), null, null, null, method);
                            if (_throwsException(method, PropertyVetoException.class)) {
                                pd.setConstrained(true);
                            }
                        }
                    }
                } catch (IntrospectionException ex) {
                    pd = null;
                }
                if (pd != null) {
                    if (_propertyChangeSource) {
                        pd.setBound(true);
                    }
                    _addProperty(properties, pd);
                }
            }
        }
        Enumeration<PropertyDescriptor> elements = properties.elements();
        while (elements.hasMoreElements()) {
            Object currElement = elements.nextElement();
            if (currElement instanceof IndexedPropertyDescriptor) {
                IndexedPropertyDescriptor indexedElement = (IndexedPropertyDescriptor) currElement;
                if (indexedElement.getIndexedReadMethod() == null) {
                    Method writeMethod = indexedElement.getIndexedWriteMethod();
                    if ((writeMethod != null) && (writeMethod.getParameterTypes()[1] == int.class)) {
                        properties.remove(indexedElement.getName());
                    }
                }
            }
        }
        PropertyDescriptor result[] = new PropertyDescriptor[properties.size()];
        elements = properties.elements();
        for (int i = 0; i < result.length; i++) {
            result[i] = elements.nextElement();
            if ((_defaultPropertyName != null) && _defaultPropertyName.equals(result[i].getName())) {
                _defaultPropertyIndex = i;
            }
        }
        return result;
    }

    /**
   * Add a property to our list of properties, merging the property if there
   * is a conflict.
   */
    private void _addProperty(Hashtable<String, PropertyDescriptor> properties, PropertyDescriptor newDescriptor) {
        PropertyDescriptor putDescriptor = newDescriptor;
        String name = newDescriptor.getName();
        PropertyDescriptor oldDescriptor = properties.get(name);
        if (oldDescriptor != null) {
            Class<?> oldDescriptorType = oldDescriptor.getPropertyType();
            Class<?> newDescriptorType = newDescriptor.getPropertyType();
            if (!((oldDescriptorType != newDescriptorType) && (oldDescriptorType != null) && (newDescriptorType != null))) {
                if ((oldDescriptor instanceof IndexedPropertyDescriptor) || (newDescriptor instanceof IndexedPropertyDescriptor)) {
                    putDescriptor = _createMergedIndexedDescriptor(oldDescriptor, newDescriptor);
                    if (putDescriptor == null) {
                        putDescriptor = (!(oldDescriptor instanceof IndexedPropertyDescriptor)) ? oldDescriptor : newDescriptor;
                    }
                } else {
                    putDescriptor = _createMergedPropertyDescriptor(oldDescriptor, newDescriptor);
                }
            }
        }
        properties.put(name, putDescriptor);
    }

    /**
   * @return An array of EventSetDescriptors describing the kinds of
   * events fired by the target bean.
   */
    EventSetDescriptor[] __getTargetEventInfo() throws IntrospectionException {
        Hashtable<String, EventSetDescriptor> events = new Hashtable<String, EventSetDescriptor>();
        EventSetDescriptor[] explicit = null;
        if (_informant != null) {
            explicit = _informant.getEventSetDescriptors();
            int ix = _informant.getDefaultEventIndex();
            if ((ix >= 0) && (ix < explicit.length)) {
                _defaultEventName = explicit[ix].getName();
            }
        }
        if ((explicit == null) && (_superBeanInfo != null)) {
            EventSetDescriptor supers[] = _superBeanInfo.getEventSetDescriptors();
            for (int i = 0; i < supers.length; i++) {
                _addEvent(events, supers[i]);
            }
            int ix = _superBeanInfo.getDefaultEventIndex();
            if ((ix >= 0) && (ix < supers.length)) {
                _defaultEventName = supers[ix].getName();
            }
        }
        for (int i = 0; i < _additionalBeanInfo.length; i++) {
            EventSetDescriptor additional[] = _additionalBeanInfo[i].getEventSetDescriptors();
            if (additional != null) {
                for (int j = 0; j < additional.length; j++) {
                    _addEvent(events, additional[j]);
                }
            }
        }
        if (explicit != null) {
            for (int i = 0; i < explicit.length; i++) {
                _addEvent(events, explicit[i]);
            }
        } else {
            Method methodList[] = _getPublicMethods(_beanClass);
            Hashtable<String, Method> adds = new Hashtable<String, Method>();
            Hashtable<String, Method> removes = new Hashtable<String, Method>();
            for (int i = 0; i < methodList.length; i++) {
                Method method = methodList[i];
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                String name = method.getName();
                Class<?>[] argTypes = method.getParameterTypes();
                Class<?> resultType = method.getReturnType();
                if (name.startsWith("add") && (argTypes.length == 1) && (resultType == Void.TYPE)) {
                    String compound = name.substring(3) + ":" + argTypes[0];
                    adds.put(compound, method);
                } else if (name.startsWith("remove") && (argTypes.length == 1) && (resultType == Void.TYPE)) {
                    String compound = name.substring(6) + ":" + argTypes[0];
                    removes.put(compound, method);
                }
            }
            Enumeration<String> keys = adds.keys();
            while (keys.hasMoreElements()) {
                String compound = keys.nextElement();
                if (removes.get(compound) == null) {
                    continue;
                }
                if (compound.indexOf("Listener:") <= 0) {
                    continue;
                }
                String listenerName = compound.substring(0, compound.indexOf(':'));
                String eventName = decapitalize(listenerName.substring(0, listenerName.length() - 8));
                Method addMethod = adds.get(compound);
                Method removeMethod = removes.get(compound);
                Class<?> argType = addMethod.getParameterTypes()[0];
                if (!JavaIntrospector._isSubclass(argType, _EVENT_LISTENER_TYPE)) {
                    continue;
                }
                Method allMethods[] = argType.getMethods();
                int count = 0;
                for (int i = 0; i < allMethods.length; i++) {
                    if (_isEventHandler(allMethods[i])) {
                        count++;
                    } else {
                        allMethods[i] = null;
                    }
                }
                Method methods[] = new Method[count];
                int j = 0;
                for (int i = 0; i < allMethods.length; i++) {
                    if (allMethods[i] != null) {
                        methods[j++] = allMethods[i];
                    }
                }
                EventSetDescriptor esd = new EventSetDescriptor(eventName, argType, methods, addMethod, removeMethod);
                if (_throwsException(addMethod, TooManyListenersException.class)) {
                    esd.setUnicast(true);
                }
                _addEvent(events, esd);
            }
        }
        EventSetDescriptor[] result = new EventSetDescriptor[events.size()];
        Enumeration<EventSetDescriptor> elements = events.elements();
        for (int i = 0; i < result.length; i++) {
            result[i] = elements.nextElement();
            if ((_defaultEventName != null) && _defaultEventName.equals(result[i].getName())) {
                _defaultEventIndex = i;
            }
        }
        return result;
    }

    private void _addEvent(Hashtable<String, EventSetDescriptor> events, EventSetDescriptor descriptor) {
        String key = descriptor.getName() + descriptor.getListenerType();
        if (descriptor.getName().equals("propertyChange")) {
            _propertyChangeSource = true;
        }
        EventSetDescriptor oldDescriptor = events.get(key);
        if (oldDescriptor == null) {
            events.put(key, descriptor);
            return;
        }
        EventSetDescriptor composite = _createMergedEventSetDescriptor(oldDescriptor, descriptor);
        events.put(key, composite);
    }

    /**
   * @return An array of MethodDescriptors describing the public
   * methods supported by the target bean.
   */
    MethodDescriptor[] __getTargetMethodInfo() throws IntrospectionException {
        MethodDescriptor[] explicit = null;
        Hashtable<String, MethodDescriptor> methods = new Hashtable<String, MethodDescriptor>();
        if (_informant != null) {
            explicit = _informant.getMethodDescriptors();
        }
        if ((explicit == null) && (_superBeanInfo != null)) {
            MethodDescriptor supers[] = _superBeanInfo.getMethodDescriptors();
            for (int i = 0; i < supers.length; i++) {
                _addMethod(methods, supers[i]);
            }
        }
        for (int i = 0; i < _additionalBeanInfo.length; i++) {
            MethodDescriptor additional[] = _additionalBeanInfo[i].getMethodDescriptors();
            if (additional != null) {
                for (int j = 0; j < additional.length; j++) {
                    _addMethod(methods, additional[j]);
                }
            }
        }
        if (explicit != null) {
            for (int i = 0; i < explicit.length; i++) {
                _addMethod(methods, explicit[i]);
            }
        } else {
            Method methodList[] = _getPublicMethods(_beanClass);
            for (int i = 0; i < methodList.length; i++) {
                _addMethod(methods, new MethodDescriptor(methodList[i]));
            }
        }
        MethodDescriptor result[] = new MethodDescriptor[methods.size()];
        Enumeration<MethodDescriptor> elements = methods.elements();
        for (int i = 0; i < result.length; i++) {
            result[i] = elements.nextElement();
        }
        return result;
    }

    private void _addMethod(Hashtable<String, MethodDescriptor> methods, MethodDescriptor descriptor) {
        String name = descriptor.getMethod().getName();
        MethodDescriptor old = methods.get(name);
        if (old == null) {
            methods.put(name, descriptor);
            return;
        }
        Class p1[] = descriptor.getMethod().getParameterTypes();
        Class p2[] = old.getMethod().getParameterTypes();
        boolean match = false;
        if (p1.length == p2.length) {
            match = true;
            for (int i = 0; i < p1.length; i++) {
                if (p1[i] != p2[i]) {
                    match = false;
                    break;
                }
            }
        }
        if (match) {
            MethodDescriptor composite = _createMergedMethodDescriptor(old, descriptor);
            methods.put(name, composite);
            return;
        }
        String longKey = _makeQualifiedMethodName(descriptor);
        old = methods.get(longKey);
        if (old == null) {
            methods.put(longKey, descriptor);
            return;
        }
        MethodDescriptor composite = _createMergedMethodDescriptor(old, descriptor);
        methods.put(longKey, composite);
    }

    private String _makeQualifiedMethodName(MethodDescriptor descriptor) {
        Method m = descriptor.getMethod();
        StringBuffer sb = new StringBuffer();
        sb.append(m.getName());
        sb.append("=");
        Class params[] = m.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            sb.append(":");
            sb.append(params[i].getName());
        }
        return sb.toString();
    }

    int __getTargetDefaultEventIndex() {
        return _defaultEventIndex;
    }

    int __getTargetDefaultPropertyIndex() {
        return _defaultPropertyIndex;
    }

    BeanDescriptor __getTargetBeanDescriptor() throws IntrospectionException {
        if (_informant != null) {
            BeanDescriptor bd = _informant.getBeanDescriptor();
            if (bd != null) {
                return bd;
            }
        }
        return (new BeanDescriptor(_beanClass));
    }

    private boolean _isEventHandler(Method m) throws IntrospectionException {
        try {
            Class argTypes[] = m.getParameterTypes();
            if (argTypes.length != 1) {
                return false;
            }
            return (_isSubclass(argTypes[0], EventObject.class));
        } catch (Exception ex) {
            throw new IntrospectionException("Unexpected reflection exception: " + ex);
        }
    }

    /**
   * Return all of the public methods for the target class
   */
    private static synchronized Method[] _getPublicMethods(Class<?> targetClass) {
        Method[] declaredMethods = _sPublicMethodCache.get(targetClass);
        if (declaredMethods == null) {
            if (_sDeclaredAccessOK) {
                try {
                    declaredMethods = targetClass.getDeclaredMethods();
                } catch (SecurityException e) {
                    _sDeclaredAccessOK = false;
                }
            }
            if (declaredMethods == null) {
                try {
                    declaredMethods = targetClass.getMethods();
                } catch (SecurityException e) {
                    ;
                }
            }
            if (declaredMethods != null) {
                int numDeclaredMethods = declaredMethods.length;
                if (numDeclaredMethods > 0) {
                    Vector<Method> publicMethods = new Vector<Method>(numDeclaredMethods);
                    for (int i = 0; i < numDeclaredMethods; i++) {
                        Method currMethod = declaredMethods[i];
                        if (Modifier.isPublic(currMethod.getModifiers()) && (currMethod.getDeclaringClass() == targetClass)) {
                            publicMethods.addElement(currMethod);
                        }
                    }
                    declaredMethods = new Method[publicMethods.size()];
                    publicMethods.copyInto(declaredMethods);
                }
            }
            _sPublicMethodCache.put(targetClass, declaredMethods);
        }
        return declaredMethods;
    }

    @SuppressWarnings("unused")
    private static Method _internalFindMethod(Class<?> start, String methodName, int argCount) {
        for (Class<?> cl = start; cl != null; cl = cl.getSuperclass()) {
            Method methods[] = _getPublicMethods(cl);
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                int mods = method.getModifiers();
                if (Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
                    continue;
                }
                if (method.getName().equals(methodName) && (method.getParameterTypes().length == argCount)) {
                    return method;
                }
            }
        }
        Class ifcs[] = start.getInterfaces();
        for (int i = 0; i < ifcs.length; i++) {
            Method m = _internalFindMethod(ifcs[i], methodName, argCount);
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    /**
   * Return true if class a is either equivalent to class b, or
   * if class a is a subclass of class b, i.e. if a either "extends"
   * or "implements" b.
   * Note tht either or both "Class" objects may represent interfaces.
   */
    static boolean _isSubclass(Class<?> a, Class<?> b) {
        if (a == b) {
            return true;
        }
        if ((a == null) || (b == null)) {
            return false;
        }
        for (Class<?> x = a; x != null; x = x.getSuperclass()) {
            if (x == b) {
                return true;
            }
            if (b.isInterface()) {
                Class interfaces[] = x.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    if (_isSubclass(interfaces[i], b)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
   * Return true iff the given method throws the given exception.
   */
    private boolean _throwsException(Method method, Class<?> exception) {
        Class exs[] = method.getExceptionTypes();
        for (int i = 0; i < exs.length; i++) {
            if (exs[i] == exception) {
                return true;
            }
        }
        return false;
    }

    /**
   * Try to create an instance of a named class.  First try the classloader of
   * "sibling", then try the context loader followed by the system
   * classloader.
   */
    private static Object _instantiate(Class<?> sibling, String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ClassLoader cl = sibling.getClassLoader();
        if (cl != null) {
            try {
                Class<?> cls = cl.loadClass(className);
                return cls.newInstance();
            } catch (Exception ex) {
                ;
            }
        }
        Class<?> cls = ClassLoaderUtils.loadClass(className);
        return cls.newInstance();
    }

    /**
   * Merge information from two PropertyDescriptors into a third
   * IndexedPropertyDescriptor.
   * In the event of other conflicts, the second argument
   * <code>primaryDescriptor</code> is given priority over the first argument
   * <code>secondaryDescriptor</code>.
   * <p>
   * @param secondaryDescriptor  The lower priority PropertyDescriptor
   * @param primaryDescriptor  The higher priority PropertyDescriptor
   * @param mergedDescriptor The IndexedPropertyDescriptor to merge the
   *                         information into.
   */
    private static IndexedPropertyDescriptor _createMergedIndexedDescriptor(PropertyDescriptor secondaryDescriptor, PropertyDescriptor primaryDescriptor) {
        Method readMethod = _getMergedReadMethod(secondaryDescriptor, primaryDescriptor);
        Method writeMethod = _getMergedWriteMethod(secondaryDescriptor, primaryDescriptor);
        Method indexedReadMethod = null;
        Method indexedWriteMethod = null;
        if (secondaryDescriptor instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor iSecondaryDescriptor = (IndexedPropertyDescriptor) secondaryDescriptor;
            readMethod = iSecondaryDescriptor.getReadMethod();
            writeMethod = iSecondaryDescriptor.getWriteMethod();
            indexedReadMethod = iSecondaryDescriptor.getIndexedReadMethod();
            indexedWriteMethod = iSecondaryDescriptor.getIndexedWriteMethod();
        }
        if (primaryDescriptor instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor iPrimaryDescriptor = (IndexedPropertyDescriptor) primaryDescriptor;
            Method tempMethod = iPrimaryDescriptor.getIndexedReadMethod();
            if (tempMethod != null) {
                indexedReadMethod = tempMethod;
            }
            tempMethod = iPrimaryDescriptor.getIndexedWriteMethod();
            if (tempMethod != null) {
                indexedWriteMethod = tempMethod;
            }
        }
        try {
            IndexedPropertyDescriptor mergedIndexedDescriptor = new IndexedPropertyDescriptor(primaryDescriptor.getName(), readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
            _mergePropertyDescriptors(secondaryDescriptor, primaryDescriptor, mergedIndexedDescriptor);
            return mergedIndexedDescriptor;
        } catch (Exception e) {
            return null;
        }
    }

    private static PropertyDescriptor _createMergedPropertyDescriptor(PropertyDescriptor secondaryDescriptor, PropertyDescriptor primaryDescriptor) {
        try {
            PropertyDescriptor mergedDescriptor = new PropertyDescriptor(primaryDescriptor.getName(), _getMergedReadMethod(secondaryDescriptor, primaryDescriptor), _getMergedWriteMethod(secondaryDescriptor, primaryDescriptor));
            _mergePropertyDescriptors(secondaryDescriptor, primaryDescriptor, mergedDescriptor);
            return mergedDescriptor;
        } catch (Exception e) {
            return null;
        }
    }

    private static MethodDescriptor _createMergedMethodDescriptor(MethodDescriptor secondaryDescriptor, MethodDescriptor primaryDescriptor) {
        ParameterDescriptor[] parameterDescriptors = primaryDescriptor.getParameterDescriptors();
        if (parameterDescriptors == null) {
            parameterDescriptors = secondaryDescriptor.getParameterDescriptors();
        }
        MethodDescriptor mergedDescriptor = new MethodDescriptor(primaryDescriptor.getMethod(), parameterDescriptors);
        _mergeFeatureDescriptors(secondaryDescriptor, primaryDescriptor, mergedDescriptor);
        return mergedDescriptor;
    }

    static EventSetDescriptor __createMergedEventSetStub(EventSetDescriptor oldDescriptor, MethodDescriptor[] listenerDescriptors) {
        try {
            EventSetDescriptor stubDescriptor = new EventSetDescriptor(oldDescriptor.getName(), oldDescriptor.getListenerType(), listenerDescriptors, oldDescriptor.getAddListenerMethod(), oldDescriptor.getRemoveListenerMethod());
            stubDescriptor.setUnicast(oldDescriptor.isUnicast());
            return stubDescriptor;
        } catch (Exception e) {
            return null;
        }
    }

    private static EventSetDescriptor _createMergedEventSetDescriptor(EventSetDescriptor secondaryDescriptor, EventSetDescriptor primaryDescriptor) {
        MethodDescriptor[] listenerDescriptors = primaryDescriptor.getListenerMethodDescriptors();
        if (listenerDescriptors == null) {
            listenerDescriptors = secondaryDescriptor.getListenerMethodDescriptors();
        }
        EventSetDescriptor mergedDescriptor = __createMergedEventSetStub(primaryDescriptor, listenerDescriptors);
        mergedDescriptor.setInDefaultEventSet(primaryDescriptor.isInDefaultEventSet() && secondaryDescriptor.isInDefaultEventSet());
        _mergeFeatureDescriptors(secondaryDescriptor, primaryDescriptor, mergedDescriptor);
        return mergedDescriptor;
    }

    /**
   * Necessary because no support for PropertyDescriptor.setReadMethod()
   * until JDK 1.2.
   */
    private static Method _getMergedReadMethod(PropertyDescriptor secondaryDescriptor, PropertyDescriptor primaryDescriptor) {
        Method primaryReadMethod = primaryDescriptor.getReadMethod();
        Method secondaryReadMethod = secondaryDescriptor.getReadMethod();
        Method readMethod = primaryReadMethod;
        if (readMethod == null) {
            readMethod = secondaryReadMethod;
        } else {
            if ((secondaryReadMethod != null) && (secondaryReadMethod.getDeclaringClass() == primaryReadMethod.getDeclaringClass()) && (secondaryReadMethod.getReturnType() == boolean.class) && (primaryReadMethod.getReturnType() == boolean.class) && primaryReadMethod.getName().startsWith(_READ_PREFIX)) {
                for (int i = 0; i < _BOOLEAN_READ_PREFIXES.length; i++) {
                    String currPrefix = _BOOLEAN_READ_PREFIXES[i];
                    if (secondaryReadMethod.getName().startsWith(currPrefix)) {
                        readMethod = secondaryReadMethod;
                        break;
                    }
                }
            }
        }
        return readMethod;
    }

    /**
   * Necessary because no support for PropertyDescriptor.setWriteMethod()
   * until JDK 1.2.
   */
    private static Method _getMergedWriteMethod(PropertyDescriptor secondaryDescriptor, PropertyDescriptor primaryDescriptor) {
        Method writeMethod = primaryDescriptor.getWriteMethod();
        if (writeMethod == null) {
            writeMethod = secondaryDescriptor.getWriteMethod();
        }
        return writeMethod;
    }

    /**
   * Merge information from two PropertyDescriptors into a third
   * PropertyDescriptor.
   * In the event of other conflicts, the second argument
   * <code>primaryDescriptor</code> is given priority over the first argument
   * <code>secondaryDescriptor</code>.
   * <p>
   * @param secondaryDescriptor  The lower priority PropertyDescriptor
   * @param primaryDescriptor  The higher priority PropertyDescriptor
   * @param mergedDescriptor The PropertyDescriptor to merge the information
   *                         into.
   */
    private static void _mergePropertyDescriptors(PropertyDescriptor secondaryDescriptor, PropertyDescriptor primaryDescriptor, PropertyDescriptor mergedDescriptor) {
        _mergeFeatureDescriptors(secondaryDescriptor, primaryDescriptor, mergedDescriptor);
        Class<?> editorClass = primaryDescriptor.getPropertyEditorClass();
        if (editorClass == null) {
            editorClass = secondaryDescriptor.getPropertyEditorClass();
        }
        mergedDescriptor.setPropertyEditorClass(editorClass);
        mergedDescriptor.setBound(secondaryDescriptor.isBound() | primaryDescriptor.isBound());
        mergedDescriptor.setConstrained(secondaryDescriptor.isConstrained() | primaryDescriptor.isConstrained());
    }

    /**
   * Merge information from two FeatureDescriptors into a third
   * FeatureDescriptor.
   * The merged hidden and expert flags are formed by or-ing the values.
   * In the event of other conflicts, the second argument
   * <code>primaryDescriptor</code> is given priority over the first argument
   * <code>secondaryDescriptor</code>.
   * <p>
   * @param secondaryDescriptor  The lower priority FeatureDescriptor
   * @param primaryDescriptor  The higher priority FeatureDescriptor
   * @param mergedDescriptor The FeatureDescriptor to merge the information
   *                         into.
   */
    private static void _mergeFeatureDescriptors(FeatureDescriptor secondaryDescriptor, FeatureDescriptor primaryDescriptor, FeatureDescriptor mergedDescriptor) {
        mergedDescriptor.setExpert(secondaryDescriptor.isExpert() | primaryDescriptor.isExpert());
        mergedDescriptor.setHidden(secondaryDescriptor.isHidden() | primaryDescriptor.isHidden());
        mergedDescriptor.setName(primaryDescriptor.getName());
        String shortDescription = primaryDescriptor.getShortDescription();
        if (shortDescription == null) {
            shortDescription = primaryDescriptor.getShortDescription();
        }
        mergedDescriptor.setShortDescription(shortDescription);
        String displayName = primaryDescriptor.getDisplayName();
        if (displayName == null) {
            displayName = primaryDescriptor.getDisplayName();
        }
        mergedDescriptor.setDisplayName(displayName);
        __addFeatureValues(secondaryDescriptor, mergedDescriptor);
        __addFeatureValues(primaryDescriptor, mergedDescriptor);
    }

    /**
   * Add all of the attributes of one FeatureDescriptor to thos
   * of another, replacing any attributes that conflict.
   */
    static void __addFeatureValues(FeatureDescriptor addingDescriptor, FeatureDescriptor destinationDescriptor) {
        Enumeration<String> keys = addingDescriptor.attributeNames();
        if (keys != null) {
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                Object value = addingDescriptor.getValue(key);
                destinationDescriptor.setValue(key, value);
            }
        }
    }

    private static final Class<EventListener> _EVENT_LISTENER_TYPE = EventListener.class;

    private static final String _READ_PREFIX = "get";

    private static final String _WRITE_PREFIX = "set";

    private static final String[] _BOOLEAN_READ_PREFIXES = { "is", "has" };

    private boolean _propertyChangeSource = false;

    private Class<?> _beanClass;

    private BeanInfo _superBeanInfo;

    private BeanInfo _informant;

    private BeanInfo[] _additionalBeanInfo;

    private String _defaultEventName;

    private String _defaultPropertyName;

    private int _defaultEventIndex = -1;

    private int _defaultPropertyIndex = -1;

    private static Hashtable<Class<?>, BeanInfo> _sBeanInfoCache = new Hashtable<Class<?>, BeanInfo>();

    private static Hashtable<Class<?>, Method[]> _sPublicMethodCache = new Hashtable<Class<?>, Method[]>();

    private static boolean _sDeclaredAccessOK = true;

    private static String[] _sSearchPath = { "sun.beans.infos" };

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(JavaIntrospector.class);
}

/**
 * Package private implementation support class for Introspector's
 * internal use.
 */
class GenericBeanInfo extends SimpleBeanInfo {

    public GenericBeanInfo(JavaIntrospector introspector, BeanInfo targetBeanInfo) {
        _introspector = introspector;
        _targetBeanInfo = targetBeanInfo;
    }

    /**
   * Package-private dup constructor
   * This must isolate the new object from any changes to the old object.
   */
    GenericBeanInfo(GenericBeanInfo old) {
        _oldBeanInfo = old;
        _targetBeanInfo = old._targetBeanInfo;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        if (_properties == null) {
            if (_introspector != null) {
                try {
                    _properties = _introspector.__getTargetPropertyInfo();
                    _defaultProperty = _introspector.__getTargetDefaultPropertyIndex();
                } catch (IntrospectionException e) {
                    ;
                }
            } else {
                PropertyDescriptor[] oldProperties = _oldBeanInfo.getPropertyDescriptors();
                if (oldProperties != null) {
                    int len = oldProperties.length;
                    PropertyDescriptor[] newProperties = new PropertyDescriptor[len];
                    for (int i = 0; i < len; i++) {
                        PropertyDescriptor oldProperty = oldProperties[i];
                        if (oldProperty instanceof IndexedPropertyDescriptor) {
                            newProperties[i] = _cloneIndexedPropertyDescriptor((IndexedPropertyDescriptor) oldProperty);
                        } else {
                            newProperties[i] = _clonePropertyDescriptor(oldProperty);
                        }
                    }
                    _properties = newProperties;
                }
                _defaultProperty = _oldBeanInfo.getDefaultPropertyIndex();
            }
        }
        return _properties;
    }

    @Override
    public int getDefaultPropertyIndex() {
        if (_defaultProperty == _DEFAULT_VALUE) {
            getPropertyDescriptors();
        }
        return _defaultProperty;
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        if (_events == null) {
            if (_introspector != null) {
                try {
                    _events = _introspector.__getTargetEventInfo();
                    _defaultEvent = _introspector.__getTargetDefaultEventIndex();
                } catch (IntrospectionException e) {
                    ;
                }
            } else {
                EventSetDescriptor[] oldEventSet = _oldBeanInfo.getEventSetDescriptors();
                if (oldEventSet != null) {
                    int len = oldEventSet.length;
                    EventSetDescriptor[] newEventSet = new EventSetDescriptor[len];
                    for (int i = 0; i < len; i++) {
                        newEventSet[i] = _cloneEventSetDescriptor(oldEventSet[i]);
                    }
                    _events = newEventSet;
                }
                _defaultEvent = _oldBeanInfo.getDefaultEventIndex();
            }
        }
        return _events;
    }

    @Override
    public int getDefaultEventIndex() {
        if (_defaultEvent == _DEFAULT_VALUE) {
            getEventSetDescriptors();
        }
        return _defaultEvent;
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        if (_methods == null) {
            if (_introspector != null) {
                try {
                    _methods = _introspector.__getTargetMethodInfo();
                } catch (IntrospectionException e) {
                    ;
                }
            } else {
                MethodDescriptor[] oldMethods = _oldBeanInfo.getMethodDescriptors();
                if (oldMethods != null) {
                    int len = oldMethods.length;
                    MethodDescriptor[] newMethods = new MethodDescriptor[len];
                    for (int i = 0; i < len; i++) {
                        newMethods[i] = _cloneMethodDescriptor(oldMethods[i]);
                    }
                    _methods = newMethods;
                }
            }
        }
        return _methods;
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        if (_beanDescriptor == null) {
            if (_introspector != null) {
                try {
                    _beanDescriptor = _introspector.__getTargetBeanDescriptor();
                } catch (IntrospectionException e) {
                    ;
                }
            } else {
                _beanDescriptor = _cloneBeanDescriptor(_oldBeanInfo.getBeanDescriptor());
            }
        }
        return _beanDescriptor;
    }

    @Override
    public Image getIcon(int iconKind) {
        if (_targetBeanInfo != null) {
            return _targetBeanInfo.getIcon(iconKind);
        }
        return super.getIcon(iconKind);
    }

    private static void _copyPropertyDescriptor(PropertyDescriptor oldDescriptor, PropertyDescriptor newDescriptor) {
        newDescriptor.setBound(oldDescriptor.isBound());
        newDescriptor.setConstrained(oldDescriptor.isConstrained());
        newDescriptor.setPropertyEditorClass(oldDescriptor.getPropertyEditorClass());
        _copyFeatureDescriptor(oldDescriptor, newDescriptor);
    }

    private static void _copyFeatureDescriptor(FeatureDescriptor oldDescriptor, FeatureDescriptor newDescriptor) {
        newDescriptor.setName(oldDescriptor.getName());
        newDescriptor.setDisplayName(oldDescriptor.getDisplayName());
        newDescriptor.setExpert(oldDescriptor.isExpert());
        newDescriptor.setHidden(oldDescriptor.isHidden());
        newDescriptor.setShortDescription(oldDescriptor.getShortDescription());
        JavaIntrospector.__addFeatureValues(oldDescriptor, newDescriptor);
    }

    private static IndexedPropertyDescriptor _cloneIndexedPropertyDescriptor(IndexedPropertyDescriptor oldDescriptor) {
        try {
            IndexedPropertyDescriptor newDescriptor = new IndexedPropertyDescriptor(oldDescriptor.getName(), oldDescriptor.getReadMethod(), oldDescriptor.getWriteMethod(), oldDescriptor.getIndexedReadMethod(), oldDescriptor.getIndexedWriteMethod());
            _copyPropertyDescriptor(oldDescriptor, newDescriptor);
            return newDescriptor;
        } catch (Exception e) {
            _LOG.severe(e);
            return null;
        }
    }

    private static PropertyDescriptor _clonePropertyDescriptor(PropertyDescriptor oldDescriptor) {
        try {
            PropertyDescriptor newDescriptor = new PropertyDescriptor(oldDescriptor.getName(), oldDescriptor.getReadMethod(), oldDescriptor.getWriteMethod());
            _copyPropertyDescriptor(oldDescriptor, newDescriptor);
            return newDescriptor;
        } catch (Exception e) {
            _LOG.severe(e);
            return null;
        }
    }

    private static BeanDescriptor _cloneBeanDescriptor(BeanDescriptor oldDescriptor) {
        try {
            BeanDescriptor newDescriptor = new BeanDescriptor(oldDescriptor.getBeanClass(), oldDescriptor.getCustomizerClass());
            _copyFeatureDescriptor(oldDescriptor, newDescriptor);
            return newDescriptor;
        } catch (Exception e) {
            _LOG.severe(e);
            return null;
        }
    }

    private static MethodDescriptor _cloneMethodDescriptor(MethodDescriptor oldDescriptor) {
        try {
            MethodDescriptor newDescriptor = new MethodDescriptor(oldDescriptor.getMethod(), oldDescriptor.getParameterDescriptors());
            _copyFeatureDescriptor(oldDescriptor, newDescriptor);
            return newDescriptor;
        } catch (Exception e) {
            _LOG.severe(e);
            return null;
        }
    }

    private static EventSetDescriptor _cloneEventSetDescriptor(EventSetDescriptor oldDescriptor) {
        EventSetDescriptor newDescriptor = JavaIntrospector.__createMergedEventSetStub(oldDescriptor, oldDescriptor.getListenerMethodDescriptors());
        newDescriptor.setInDefaultEventSet(oldDescriptor.isInDefaultEventSet());
        return newDescriptor;
    }

    private static final int _DEFAULT_VALUE = -2;

    private JavaIntrospector _introspector;

    private GenericBeanInfo _oldBeanInfo;

    private BeanDescriptor _beanDescriptor;

    private EventSetDescriptor[] _events;

    private int _defaultEvent = _DEFAULT_VALUE;

    private PropertyDescriptor[] _properties;

    private int _defaultProperty = _DEFAULT_VALUE;

    private MethodDescriptor[] _methods;

    private BeanInfo _targetBeanInfo;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(GenericBeanInfo.class);
}
