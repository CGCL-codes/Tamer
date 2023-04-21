package net.sourceforge.pojosync.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.sourceforge.pojosync.Delta;
import net.sourceforge.pojosync.PojoSyncException;
import net.sourceforge.pojosync.client.Objects;
import net.sourceforge.pojosync.client.Synchronizer.IObjectIdentifier;

public class Classes {

    private static Classes instance;

    private Map<Class<?>, Class<?>> implClasses = new HashMap<Class<?>, Class<?>>();

    private Map<Class<?>, Class<? extends Access>> accessClasses = new HashMap<Class<?>, Class<? extends Access>>();

    private IObjectIdentifier objectIdentifier;

    @SuppressWarnings("unchecked")
    private Classes() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("classes.properties");
        if (in == null) {
            throw new PojoSyncException("classes.properties not in classpath");
        }
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        accessClasses = new HashMap<Class<?>, Class<? extends Access>>();
        for (Object key : properties.keySet()) {
            String value = properties.getProperty((String) key);
            try {
                Class<?> iface = Class.forName((String) key);
                Class<?> accessClass = Class.forName(value + "Access");
                Class<?> impl = Class.forName(value);
                if (!Access.class.isAssignableFrom(accessClass)) {
                    throw new PojoSyncException("invalid Access class: " + accessClass);
                }
                accessClasses.put(iface, (Class<? extends Access>) accessClass);
                implClasses.put(iface, impl);
            } catch (ClassNotFoundException e) {
                throw new PojoSyncException("Failed to instantiate class", e);
            }
        }
    }

    public static Classes getInstance() {
        if (instance == null) {
            instance = new Classes();
        }
        return instance;
    }

    public void setObjectIdentifier(IObjectIdentifier objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    public IObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    public Class<? extends Access> getAccessClass(Class<?> iface) {
        if (!iface.isInterface()) {
            throw new IllegalArgumentException("interface expected");
        }
        return accessClasses.get(iface);
    }

    public Class<?> getDelegateClass(Class<?> iface) {
        if (!iface.isInterface()) {
            throw new IllegalArgumentException("interface expected");
        }
        return implClasses.get(iface);
    }

    public Class<?> getSynchronizableInterface(Class<?> clazz) {
        Set<Class<?>> interfaces = accessClasses.keySet();
        for (Class<?> iface : interfaces) {
            if (iface.isAssignableFrom(clazz)) {
                return iface;
            }
        }
        return null;
    }

    public Access createAccess(IAccessCollection synchronizedObjects, Object delegate, Delta delta) {
        Class<?> iface = delta.getInterface();
        Class<?> impl = Classes.getInstance().getDelegateClass(iface);
        Class<? extends Access> accessClass = Classes.getInstance().getAccessClass(iface);
        try {
            Constructor<? extends Access> constructor = accessClass.getConstructor(IAccessCollection.class, impl, Delta.class);
            return constructor.newInstance(synchronizedObjects, delegate, delta.clone());
        } catch (Exception e) {
            throw new PojoSyncException("Failed to create access object", e);
        }
    }

    public Access createAccessForObject(IAccessCollection objects, Object object) {
        try {
            Class<?> iface = getSynchronizableInterface(object.getClass());
            Class<?> impl = Classes.getInstance().getDelegateClass(iface);
            Class<? extends Access> accessClass = Classes.getInstance().getAccessClass(iface);
            Constructor<? extends Access> constructor = accessClass.getConstructor(Objects.class, impl, Delta.class);
            Classes.getInstance().getObjectIdentifier().setId(object, objectIdentifier.getId(object));
            return constructor.newInstance(objects, object, new Delta(iface));
        } catch (Exception e) {
            return null;
        }
    }
}
