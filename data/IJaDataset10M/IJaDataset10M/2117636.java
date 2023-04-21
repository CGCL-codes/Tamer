package openjmx;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Hashtable;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.ReflectionException;
import javax.management.ObjectInstance;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.MBeanRegistration;
import javax.management.RuntimeOperationsException;
import javax.management.MalformedObjectNameException;
import javax.management.MBeanServerNotification;
import javax.management.MBeanServerDelegate;
import javax.management.NotificationListener;
import javax.management.NotificationFilter;
import javax.management.NotificationBroadcaster;
import javax.management.ListenerNotFoundException;
import javax.management.loading.DefaultLoaderRepository;
import javax.management.Attribute;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanInfo;
import javax.management.IntrospectionException;
import javax.management.AttributeList;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.BadStringOperationException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadAttributeValueExpException;
import javax.management.InvalidApplicationException;
import openjmx.util.ClassLoaderObjectInputStream;

/**
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1.8 $
 */
public class MBeanServerImpl implements MBeanServer {

    public static ObjectName DELEGATE_OBJECT_NAME;

    private static ObjectName DEFAULT_LOADER_REPOSITORY;

    static {
        try {
            DELEGATE_OBJECT_NAME = new ReservedObjectName("JMImplementation:type=MBeanServerDelegate");
            DEFAULT_LOADER_REPOSITORY = new ReservedObjectName("JMImplementation:type=DefaultLoaderRepository");
        } catch (MalformedObjectNameException ignored) {
            throw new ImplementationException(ignored.toString());
        }
    }

    public static final String ID_ATTRIBUTE = "MBeanServerId";

    private static final String[] EMPTY_PARAMS_STRING = new String[0];

    static final Class[] EMPTY_PARAMS = new Class[0];

    static final Object[] EMPTY_ARGS = new Object[0];

    private Map m_registeredMBeans = new HashMap();

    private MBeanServerDelegate m_delegate = new MBeanServerDelegate();

    private long m_notifications;

    private String m_defaultDomain;

    private MBeanIntrospector m_introspector;

    public MBeanServerImpl(String defaultDomain) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("createMBeanServer"));
        }
        if (defaultDomain == null) {
            defaultDomain = "";
        }
        m_defaultDomain = defaultDomain;
        m_introspector = new MBeanIntrospector();
        try {
            registerMBean(m_delegate, DELEGATE_OBJECT_NAME);
        } catch (InstanceAlreadyExistsException ignored) {
            throw new ImplementationException(ignored.toString());
        } catch (MBeanRegistrationException ignored) {
            throw new ImplementationException(ignored.toString());
        } catch (NotCompliantMBeanException ignored) {
            throw new ImplementationException(ignored.toString());
        }
    }

    public String getDefaultDomain() {
        return m_defaultDomain;
    }

    public boolean isRegistered(ObjectName objectName) {
        try {
            return getRegisteredMBean(objectName) != null;
        } catch (InstanceNotFoundException ignored) {
        }
        return false;
    }

    public Integer getMBeanCount() {
        synchronized (m_registeredMBeans) {
            return new Integer(m_registeredMBeans.size());
        }
    }

    public MBeanInfo getMBeanInfo(ObjectName objectName) throws InstanceNotFoundException, IntrospectionException, ReflectionException {
        Object mbean = getRegisteredMBean(objectName);
        try {
            return m_introspector.getMBeanInfo(mbean);
        } catch (NotCompliantMBeanException x) {
            throw new ImplementationException("Already registered MBean is not compliant");
        }
    }

    public ObjectInstance getObjectInstance(ObjectName objectName) throws InstanceNotFoundException {
        Object mbean = getRegisteredMBean(objectName);
        String className = null;
        try {
            className = m_introspector.getClassName(mbean);
        } catch (NotCompliantMBeanException x) {
            throw new ImplementationException("Already registered MBean is not compliant");
        }
        return new ObjectInstance(objectName, className);
    }

    public boolean isInstanceOf(ObjectName objectName, String className) throws InstanceNotFoundException {
        if (className == null || className.trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid class name"));
        }
        Object mbean = getRegisteredMBean(objectName);
        Class cls = null;
        try {
            cls = findClass(null, className);
        } catch (ReflectionException x) {
            throw new ImplementationException("Cannot find class " + className);
        }
        return cls.isInstance(mbean);
    }

    public Object invoke(ObjectName objectName, String methodName, Object[] args, String[] parameters) throws InstanceNotFoundException, MBeanException, ReflectionException {
        if (methodName == null || methodName.trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid operation name"));
        }
        if (args == null) {
            args = EMPTY_ARGS;
        }
        if (parameters == null) {
            parameters = EMPTY_PARAMS_STRING;
        }
        Object mbean = getRegisteredMBean(objectName);
        return m_introspector.invoke(mbean, methodName, args, parameters);
    }

    public void addNotificationListener(ObjectName observed, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException {
        Object mbean = getRegisteredMBean(listener);
        if (mbean instanceof NotificationListener) {
            addNotificationListener(observed, (NotificationListener) mbean, filter, handback);
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("MBean " + listener + " does not implement NotificationListener"));
        }
    }

    public void addNotificationListener(ObjectName observed, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException {
        if (listener == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("NotificationListener cannot be null"));
        }
        Object mbean = getRegisteredMBean(observed);
        if (mbean instanceof NotificationBroadcaster) {
            NotificationBroadcaster broadcaster = (NotificationBroadcaster) mbean;
            broadcaster.addNotificationListener(listener, filter, handback);
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("MBean " + observed + " does not implement NotificationBroadcaster"));
        }
    }

    public void removeNotificationListener(ObjectName observed, ObjectName listener) throws InstanceNotFoundException, ListenerNotFoundException {
        Object mbean = getRegisteredMBean(listener);
        if (mbean instanceof NotificationListener) {
            removeNotificationListener(observed, (NotificationListener) mbean);
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("MBean " + listener + " does not implement NotificationListener"));
        }
    }

    public void removeNotificationListener(ObjectName observed, NotificationListener listener) throws InstanceNotFoundException, ListenerNotFoundException {
        if (listener == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("NotificationListener cannot be null"));
        }
        Object mbean = getRegisteredMBean(observed);
        if (mbean instanceof NotificationBroadcaster) {
            NotificationBroadcaster broadcaster = (NotificationBroadcaster) mbean;
            broadcaster.removeNotificationListener(listener);
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("MBean " + observed + " does not implement NotificationBroadcaster"));
        }
    }

    /**
	 * Not present in the specification, but needed.
	 */
    public void removeNotificationListener(ObjectName observed, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException {
        Object mbean = getRegisteredMBean(listener);
        if (mbean instanceof NotificationListener) {
            removeNotificationListener(observed, (NotificationListener) mbean, filter, handback);
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("MBean " + listener + " does not implement NotificationListener"));
        }
    }

    /**
	 * Not present in the specification, but needed.
	 */
    public void removeNotificationListener(ObjectName observed, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException {
        if (listener == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("NotificationListener cannot be null"));
        }
        Object mbean = getRegisteredMBean(observed);
        if (mbean instanceof NotificationBroadcaster) {
            NotificationBroadcaster broadcaster = (NotificationBroadcaster) mbean;
            broadcaster.removeNotificationListener(listener, filter, handback);
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("MBean " + observed + " does not implement NotificationBroadcaster"));
        }
    }

    public Object instantiate(String className) throws ReflectionException, MBeanException {
        try {
            return instantiate(className, DEFAULT_LOADER_REPOSITORY, EMPTY_ARGS, EMPTY_PARAMS_STRING);
        } catch (InstanceNotFoundException x) {
            throw new ImplementationException(x.toString());
        }
    }

    public Object instantiate(String className, ObjectName loaderName) throws InstanceNotFoundException, ReflectionException, MBeanException {
        return instantiate(className, loaderName, EMPTY_ARGS, EMPTY_PARAMS_STRING);
    }

    public Object instantiate(String className, Object[] args, String[] parameters) throws ReflectionException, MBeanException {
        try {
            return instantiate(className, DEFAULT_LOADER_REPOSITORY, args, parameters);
        } catch (InstanceNotFoundException x) {
            throw new ImplementationException(x.toString());
        }
    }

    public Object instantiate(String className, ObjectName loaderName, Object[] args, String[] parameters) throws InstanceNotFoundException, ReflectionException, MBeanException {
        if (className == null || className.trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Class name cannot be null or empty"));
        }
        if (loaderName != null && loaderName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName for the ClassLoader cannot be a pattern ObjectName"));
        }
        if (args == null) {
            args = EMPTY_ARGS;
        }
        if (parameters == null) {
            parameters = EMPTY_PARAMS_STRING;
        }
        Class cls = findClass(loaderName, className);
        Class[] params = m_introspector.convertMethodParameters(cls.getClassLoader(), parameters);
        Object mbean = null;
        try {
            Constructor ctor = cls.getConstructor(params);
            mbean = ctor.newInstance(args);
        } catch (NoSuchMethodException x) {
            throw new ReflectionException(x);
        } catch (InstantiationException x) {
            throw new ReflectionException(x);
        } catch (IllegalAccessException x) {
            throw new ReflectionException(x);
        } catch (IllegalArgumentException x) {
            throw new RuntimeOperationsException(x);
        } catch (InvocationTargetException x) {
            Throwable t = x.getTargetException();
            if (t instanceof Error) {
                throw new RuntimeErrorException((Error) t);
            } else if (t instanceof RuntimeException) {
                throw new RuntimeMBeanException((RuntimeException) t);
            } else {
                throw new MBeanException((Exception) t);
            }
        }
        return mbean;
    }

    public ObjectInstance createMBean(String className, ObjectName objectName) throws ReflectionException, MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        try {
            return createMBean(className, objectName, DEFAULT_LOADER_REPOSITORY, EMPTY_ARGS, EMPTY_PARAMS_STRING);
        } catch (InstanceNotFoundException x) {
            throw new ImplementationException(x.toString());
        }
    }

    public ObjectInstance createMBean(String className, ObjectName objectName, ObjectName loaderName) throws InstanceNotFoundException, ReflectionException, MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        return createMBean(className, objectName, loaderName, EMPTY_ARGS, EMPTY_PARAMS_STRING);
    }

    public ObjectInstance createMBean(String className, ObjectName objectName, Object[] args, String[] parameters) throws ReflectionException, MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        try {
            return createMBean(className, objectName, DEFAULT_LOADER_REPOSITORY, args, parameters);
        } catch (InstanceNotFoundException x) {
            throw new ImplementationException(x.toString());
        }
    }

    public ObjectInstance createMBean(String className, ObjectName objectName, ObjectName loaderName, Object[] args, String[] parameters) throws InstanceNotFoundException, ReflectionException, MBeanException, InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        if (objectName != null && objectName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName for the MBean cannot be a pattern ObjectName"));
        }
        Object mbean = instantiate(className, loaderName, args, parameters);
        return registerMBean(mbean, objectName);
    }

    public ObjectInstance registerMBean(Object mbean, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        if (mbean == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("MBean cannot be null"));
        }
        m_introspector.testFullCompliance(mbean);
        boolean invokeRegistration = mbean instanceof MBeanRegistration;
        Class cls = mbean.getClass();
        synchronized (m_registeredMBeans) {
            if (invokeRegistration) {
                ObjectName objName = invokeRegistration((MBeanRegistration) mbean, cls, "preRegister", objectName, false);
                if (objName != null) {
                    objectName = objName;
                }
            }
            try {
                register(mbean, objectName);
                if (invokeRegistration) {
                    invokeRegistration((MBeanRegistration) mbean, cls, "postRegister", objectName, true);
                }
            } catch (Exception x) {
                if (invokeRegistration) {
                    try {
                        invokeRegistration((MBeanRegistration) mbean, cls, "postRegister", objectName, false);
                    } catch (MBeanRegistrationException ignored) {
                    }
                }
                throw new MBeanRegistrationException(x);
            }
            if (mbean instanceof ClassLoader) {
                DefaultLoaderRepository.addClassLoader((ClassLoader) mbean);
            }
            ObjectInstance instance = null;
            try {
                instance = getObjectInstance(objectName);
                return instance;
            } catch (InstanceNotFoundException x) {
                throw new ImplementationException("Cannot find just registered instance");
            }
        }
    }

    public void unregisterMBean(ObjectName objectName) throws InstanceNotFoundException, MBeanRegistrationException {
        if (DELEGATE_OBJECT_NAME.equals(objectName)) {
            throw new RuntimeOperationsException(new IllegalArgumentException(objectName + " cannot be removed"));
        }
        Object mbean = getRegisteredMBean(objectName);
        boolean invokeRegistration = mbean instanceof MBeanRegistration;
        if (invokeRegistration) {
            invokeRegistration((MBeanRegistration) mbean, mbean.getClass(), "preDeregister", null, false);
        }
        unregister(objectName);
        if (invokeRegistration) {
            invokeRegistration((MBeanRegistration) mbean, mbean.getClass(), "postDeregister", null, false);
        }
        if (mbean instanceof ClassLoader) {
            DefaultLoaderRepository.removeClassLoader((ClassLoader) mbean);
        }
    }

    public Object getAttribute(ObjectName objectName, String attribute) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
        if (attribute == null || attribute.trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid attribute"));
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("getAttribute"));
        }
        Object mbean = getRegisteredMBean(objectName);
        return m_introspector.getAttributeValue(mbean, attribute);
    }

    public void setAttribute(ObjectName objectName, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null || attribute.getName().trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid attribute"));
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("setAttribute"));
        }
        Object mbean = getRegisteredMBean(objectName);
        m_introspector.setAttribute(mbean, attribute);
    }

    public AttributeList getAttributes(ObjectName objectName, String[] attributes) throws InstanceNotFoundException, ReflectionException {
        Object mbean = getRegisteredMBean(objectName);
        return m_introspector.getAttributes(mbean, attributes);
    }

    public AttributeList setAttributes(ObjectName objectName, AttributeList attributes) throws InstanceNotFoundException, ReflectionException {
        Object mbean = getRegisteredMBean(objectName);
        return m_introspector.setAttributes(mbean, attributes);
    }

    public ObjectInputStream deserialize(String className, byte[] bytes) throws OperationsException, ReflectionException {
        if (className == null || className.trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid class name"));
        }
        try {
            Class cls = null;
            try {
                cls = findClass(null, className);
            } catch (ReflectionException x) {
                cls = findClass(DEFAULT_LOADER_REPOSITORY, className);
            }
            return deserialize(cls.getClassLoader(), bytes);
        } catch (InstanceNotFoundException x) {
            throw new ImplementationException("BUG: cannot find default classloaders");
        }
    }

    public ObjectInputStream deserialize(String className, ObjectName loaderName, byte[] bytes) throws InstanceNotFoundException, OperationsException, ReflectionException {
        if (className == null || className.trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid class name"));
        }
        Class cls = findClass(loaderName, className);
        return deserialize(cls.getClassLoader(), bytes);
    }

    public ObjectInputStream deserialize(ObjectName objectName, byte[] bytes) throws InstanceNotFoundException, OperationsException {
        Object mbean = getRegisteredMBean(objectName);
        return deserialize(mbean.getClass().getClassLoader(), bytes);
    }

    private ObjectInputStream deserialize(ClassLoader classLoader, byte[] bytes) throws OperationsException {
        if (bytes == null || bytes.length == 0) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid bytes"));
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            return new ClassLoaderObjectInputStream(bais, classLoader);
        } catch (IOException x) {
            throw new OperationsException(x.toString());
        }
    }

    public Set queryMBeans(ObjectName patternName, QueryExp filter) {
        Set scope = getMBeansByPattern(patternName);
        Set match = filterMBeans(scope, filter);
        Set set = new HashSet();
        for (Iterator i = match.iterator(); i.hasNext(); ) {
            ObjectName name = (ObjectName) i.next();
            try {
                Object mbean = getRegisteredMBean(name);
                String className = m_introspector.getClassName(mbean);
                ObjectInstance instance = new ObjectInstance(name, className);
                set.add(instance);
            } catch (InstanceNotFoundException ignored) {
                throw new ImplementationException("Already found MBean is not present anymore");
            } catch (NotCompliantMBeanException e) {
                throw new ImplementationException("Already registered MBean is not compliant");
            }
        }
        return set;
    }

    public Set queryNames(ObjectName patternName, QueryExp filter) {
        Set scope = getMBeansByPattern(patternName);
        Set match = filterMBeans(scope, filter);
        return match;
    }

    private void checkMBeanExistence(ObjectName objectName) throws InstanceAlreadyExistsException {
        synchronized (m_registeredMBeans) {
            if (m_registeredMBeans.get(objectName) != null) {
                throw new InstanceAlreadyExistsException("MBean " + objectName + " is already registered");
            }
        }
    }

    /**
	 * Loads the given class using the given ClassLoader MBean object name. <br>
	 * If <code>loaderName</code> is null this class' ClassLoader is used; if it's {@link #DEFAULT_LOADER_REPOSITORY} then
	 * the default loader repository is used; otherwise the given MBean is used
	 * @throws InstanceNotFoundException if the given non-null object name it's not one of the registered MBeans
	 * @throws ReflectionException if the class is not found
	 * @throws RuntimeOperationsException if the non-null object name it isn't a ClassLoader
	 */
    private Class findClass(ObjectName loaderName, String className) throws InstanceNotFoundException, ReflectionException {
        Class cls = null;
        try {
            if (DEFAULT_LOADER_REPOSITORY.equals(loaderName)) {
                cls = DefaultLoaderRepository.loadClass(className);
            } else {
                ClassLoader cl = null;
                if (loaderName == null) {
                    cl = getClass().getClassLoader();
                    if (cl == null) {
                        cl = Thread.currentThread().getContextClassLoader();
                    }
                } else {
                    Object mbean = getRegisteredMBean(loaderName);
                    if (!(mbean instanceof ClassLoader)) {
                        throw new RuntimeOperationsException(new IllegalArgumentException("MBean " + loaderName + " is not a ClassLoader"));
                    }
                    cl = (ClassLoader) mbean;
                }
                cls = cl.loadClass(className);
            }
            return cls;
        } catch (ClassNotFoundException x) {
            throw new ReflectionException(x);
        }
    }

    private ObjectName invokeRegistration(MBeanRegistration mbean, Class cls, String name, ObjectName objectName, boolean registered) throws MBeanRegistrationException {
        Class[] params = EMPTY_PARAMS;
        Object[] args = EMPTY_ARGS;
        if (name.equals("preRegister")) {
            params = new Class[] { MBeanServer.class, ObjectName.class };
            args = new Object[] { this, objectName };
        } else if (name.equals("postRegister")) {
            params = new Class[] { Boolean.class };
            args = new Object[] { new Boolean(registered) };
        }
        try {
            Method m = cls.getMethod(name, params);
            ObjectName objName = (ObjectName) m.invoke(mbean, args);
            return objName;
        } catch (NoSuchMethodException x) {
            throw new ImplementationException("MBeanRegistration method not found");
        } catch (IllegalAccessException x) {
            throw new ImplementationException(x.toString());
        } catch (IllegalArgumentException e) {
            throw new ImplementationException("Wrong arguments to MBeanRegistration method");
        } catch (InvocationTargetException x) {
            Throwable t = x.getTargetException();
            if (t instanceof MBeanRegistrationException) {
                throw (MBeanRegistrationException) t;
            } else if (t instanceof Error) {
                throw new RuntimeErrorException((Error) t);
            } else {
                throw new MBeanRegistrationException((Exception) t);
            }
        }
    }

    private void register(Object mbean, ObjectName objectName) throws InstanceAlreadyExistsException {
        if (objectName == null || objectName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName cannot be null or a pattern ObjectName"));
        }
        if (objectName.getDomain().equals("JMImplementation") && !(objectName instanceof ReservedObjectName)) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Domain 'JMImplementation' is reserved for the JMX Agent"));
        }
        synchronized (m_registeredMBeans) {
            checkMBeanExistence(objectName);
            m_registeredMBeans.put(objectName, mbean);
        }
        notify(objectName, MBeanServerNotification.REGISTRATION_NOTIFICATION);
    }

    private void unregister(ObjectName objectName) {
        if (objectName == null || objectName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName cannot be null or a pattern ObjectName"));
        }
        synchronized (m_registeredMBeans) {
            m_registeredMBeans.remove(objectName);
        }
        notify(objectName, MBeanServerNotification.UNREGISTRATION_NOTIFICATION);
    }

    private void notify(ObjectName objectName, String notificationType) {
        long sequenceNumber = 0;
        synchronized (MBeanServerImpl.class) {
            sequenceNumber = m_notifications;
            ++m_notifications;
        }
        m_delegate.sendNotification(new MBeanServerNotification(notificationType, DELEGATE_OBJECT_NAME, sequenceNumber, objectName));
    }

    private Object getRegisteredMBean(ObjectName objectName) throws InstanceNotFoundException {
        Object mbean = null;
        if (objectName != null) {
            synchronized (m_registeredMBeans) {
                mbean = m_registeredMBeans.get(objectName);
            }
        }
        if (mbean == null) {
            throw new InstanceNotFoundException("MbeanServer cannot find MBean with ObjectName " + objectName);
        }
        return mbean;
    }

    private Set getMBeansByPattern(ObjectName pattern) {
        if (pattern == null) {
            try {
                pattern = new ObjectName("*:*");
            } catch (MalformedObjectNameException ignored) {
            }
        }
        String patternDomain = pattern.getDomain();
        Hashtable patternProps = pattern.getKeyPropertyList();
        Set set = new HashSet();
        synchronized (m_registeredMBeans) {
            for (Iterator i = m_registeredMBeans.keySet().iterator(); i.hasNext(); ) {
                ObjectName name = (ObjectName) i.next();
                Hashtable props = name.getKeyPropertyList();
                String domain = name.getDomain();
                if (wildcardMatch(patternDomain, domain)) {
                    if (pattern.isPropertyPattern()) {
                        if (patternProps.size() == 1) {
                            set.add(name);
                        } else {
                            boolean found = true;
                            for (Iterator j = patternProps.entrySet().iterator(); j.hasNext(); ) {
                                Map.Entry entry = (Map.Entry) j.next();
                                Object patternKey = entry.getKey();
                                Object patternValue = entry.getValue();
                                if (patternKey.equals("*")) {
                                    continue;
                                }
                                if (!props.containsKey(patternKey)) {
                                    found = false;
                                    break;
                                } else {
                                    Object value = props.get(patternKey);
                                    if (value == null && patternValue == null) {
                                        continue;
                                    }
                                    if (value != null && value.equals(patternValue)) {
                                        continue;
                                    }
                                    found = false;
                                    break;
                                }
                            }
                            if (found) {
                                set.add(name);
                            }
                        }
                    } else {
                        if (props.entrySet().equals(patternProps.entrySet())) {
                            set.add(name);
                        }
                    }
                }
            }
        }
        return set;
    }

    private Set filterMBeans(Set scope, QueryExp filter) {
        if (filter == null) {
            return scope;
        }
        Set set = new HashSet();
        for (Iterator i = scope.iterator(); i.hasNext(); ) {
            ObjectName name = (ObjectName) i.next();
            filter.setMBeanServer(this);
            try {
                if (filter.apply(name)) {
                    set.add(name);
                }
            } catch (BadStringOperationException ignored) {
            } catch (BadBinaryOpValueExpException ignored) {
            } catch (BadAttributeValueExpException x) {
            } catch (InvalidApplicationException x) {
            }
        }
        return set;
    }

    public boolean wildcardMatch(String pattern, String string) {
        int stringLength = string.length();
        int stringIndex = 0;
        for (int patternIndex = 0; patternIndex < pattern.length(); ++patternIndex) {
            char c = pattern.charAt(patternIndex);
            if (c == '*') {
                while (stringIndex < stringLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1), string.substring(stringIndex))) {
                        return true;
                    }
                    ++stringIndex;
                }
            } else if (c == '?') {
                ++stringIndex;
                if (stringIndex > stringLength) {
                    return false;
                }
            } else {
                if (stringIndex >= stringLength || c != string.charAt(stringIndex)) {
                    return false;
                }
                ++stringIndex;
            }
        }
        return stringIndex == stringLength;
    }

    private static class ReservedObjectName extends ObjectName {

        private ReservedObjectName(String name) throws MalformedObjectNameException {
            super(name);
        }
    }
}
