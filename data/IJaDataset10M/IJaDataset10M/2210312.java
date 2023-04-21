package net.sf.gilead.core.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.sf.beanlib.hibernate.UnEnhancer;
import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.core.serialization.SerializableId;
import net.sf.gilead.exception.ComponentTypeException;
import net.sf.gilead.exception.NotPersistentObjectException;
import net.sf.gilead.exception.TransientObjectException;
import net.sf.gilead.pojo.base.IUserType;
import net.sf.gilead.util.IntrospectionHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.AbstractComponentType;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

/**
 * Persistent helper for Hibernate JPA implementation
 * Centralizes the SessionFactory and add some needed methods.
 * Not really a singleton, since there can be as many HibernateUtil instance as different sessionFactories
 * @author BMARCHESSON
 */
public class HibernateJpaUtil implements IPersistenceUtil {

    /**
	 * Proxy id
	 */
    private static final String ID = "id";

    /**
	 * Persistent collection class name
	 */
    private static final String CLASS_NAME = "class";

    /**
	 * Persistent collection role
	 */
    private static final String ROLE = "role";

    /**
	 * Persistent collection PK ids
	 */
    private static final String KEY = "key";

    /**
	 * Persistent collection ids list
	 */
    private static final String ID_LIST = "idList";

    /**
	 * Persistent map values list
	 */
    private static final String VALUE_LIST = "valueList";

    /**
	 * Log channel
	 */
    private static Log _log = LogFactory.getLog(HibernateJpaUtil.class);

    /**
	 * The pseudo unique instance of the singleton
	 */
    private static HibernateJpaUtil _instance = null;

    /**
	 * The Hibernate session factory
	 */
    private SessionFactoryImpl _sessionFactory;

    /**
	 * The persistance map, with persistance status of all classes
	 * including persistent component classes
	 */
    private Map<String, Boolean> _persistenceMap;

    /**
	 * The current opened session
	 */
    private ThreadLocal<Session> _session;

    /**
	 * @return the unique instance of the singleton
	 */
    public static HibernateJpaUtil getInstance() {
        if (_instance == null) {
            _instance = new HibernateJpaUtil();
        }
        return _instance;
    }

    /**
	 * @return the hibernate session Factory
	 */
    public SessionFactory getSessionFactory() {
        return _sessionFactory;
    }

    /**
	 * Sets the JPA entity manager factory
	 * @param entityManagerFactory
	 */
    public void setEntityManagerFactory(Object entityManagerFactory) {
        if (entityManagerFactory instanceof HibernateEntityManagerFactory == false) {
            entityManagerFactory = searchHibernateImplementation(entityManagerFactory);
            if (entityManagerFactory == null) {
                throw new IllegalArgumentException("Cannot find Hibernate entity manager factory implementation !");
            }
        }
        _sessionFactory = (SessionFactoryImpl) ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();
    }

    /**
	 * Default constructor
	 */
    public HibernateJpaUtil() {
        _session = new ThreadLocal<Session>();
        _persistenceMap = new HashMap<String, Boolean>();
        _persistenceMap.put(Byte.class.getName(), false);
        _persistenceMap.put(Short.class.getName(), false);
        _persistenceMap.put(Integer.class.getName(), false);
        _persistenceMap.put(Long.class.getName(), false);
        _persistenceMap.put(Float.class.getName(), false);
        _persistenceMap.put(Double.class.getName(), false);
        _persistenceMap.put(Boolean.class.getName(), false);
        _persistenceMap.put(String.class.getName(), false);
    }

    public Serializable getId(Object pojo) {
        return getId(pojo, getPersistentClass(pojo));
    }

    public Serializable getId(Object pojo, Class<?> hibernateClass) {
        if (_sessionFactory == null) {
            throw new NullPointerException("No Hibernate Session Factory defined !");
        }
        if (isPersistentClass(hibernateClass) == false) {
            if (_log.isDebugEnabled()) {
                _log.debug(hibernateClass + " is not persistent");
                dumpPersistenceMap();
            }
            throw new NotPersistentObjectException(pojo);
        }
        ClassMetadata hibernateMetadata = _sessionFactory.getClassMetadata(hibernateClass);
        if (hibernateMetadata == null) {
            throw new ComponentTypeException(pojo);
        }
        Serializable id = null;
        Class<?> pojoClass = getPersistentClass(pojo);
        if (hibernateClass.equals(pojoClass)) {
            if (pojo instanceof HibernateProxy) {
                id = ((HibernateProxy) pojo).getHibernateLazyInitializer().getIdentifier();
            } else {
                id = hibernateMetadata.getIdentifier(pojo, EntityMode.POJO);
            }
        } else {
            String property = hibernateMetadata.getIdentifierPropertyName();
            try {
                property = property.substring(0, 1).toUpperCase() + property.substring(1);
                String getter = "get" + property;
                Method method = pojoClass.getMethod(getter, (Class[]) null);
                if (method == null) {
                    throw new RuntimeException("Cannot find method " + getter + " for Class<?> " + pojoClass);
                }
                id = (Serializable) method.invoke(pojo, (Object[]) null);
            } catch (Exception ex) {
                throw new RuntimeException("Invocation exception ", ex);
            }
        }
        if (isUnsavedValue(id, hibernateClass)) {
            throw new TransientObjectException(pojo);
        }
        return id;
    }

    public boolean isPersistentPojo(Object pojo) {
        if (pojo == null) {
            return false;
        }
        try {
            getId(pojo);
            return true;
        } catch (TransientObjectException ex) {
            return false;
        } catch (NotPersistentObjectException ex) {
            return false;
        }
    }

    public boolean isPersistentClass(Class<?> clazz) {
        if (_sessionFactory == null) {
            throw new NullPointerException("No Hibernate Session Factory defined !");
        }
        clazz = getUnenhancedClass(clazz);
        synchronized (_persistenceMap) {
            Boolean persistent = _persistenceMap.get(clazz.getName());
            if (persistent != null) {
                return persistent.booleanValue();
            }
        }
        computePersistenceForClass(clazz);
        return _persistenceMap.get(clazz.getName()).booleanValue();
    }

    public Class<?> getUnenhancedClass(Class<?> clazz) {
        if (_sessionFactory == null) {
            throw new NullPointerException("No Hibernate Session Factory defined !");
        }
        if (isEnhanced(clazz)) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    public boolean isEnhanced(Class<?> clazz) {
        return (clazz != UnEnhancer.unenhanceClass(clazz));
    }

    public void openSession() {
        if (_sessionFactory == null) {
            throw new NullPointerException("No Hibernate Session Factory defined !");
        }
        Session session = _sessionFactory.openSession();
        _session.set(session);
    }

    public void closeCurrentSession() {
        Session session = _session.get();
        if (session != null) {
            session.close();
            _session.remove();
        }
    }

    public Object load(Serializable id, Class<?> persistentClass) {
        Session session = _session.get();
        if (session == null) {
            throw new NullPointerException("Cannot load : no session opened !");
        }
        persistentClass = getUnenhancedClass(persistentClass);
        return session.get(persistentClass, id);
    }

    public Map<String, Serializable> serializeEntityProxy(Object proxy) {
        if (proxy == null) {
            return null;
        }
        Map<String, Serializable> result = new HashMap<String, Serializable>();
        result.put(CLASS_NAME, getUnenhancedClass(proxy.getClass()).getName());
        result.put(ID, getId(proxy));
        return result;
    }

    /**
	 * Create a proxy for the argument class and id
	 */
    public Object createEntityProxy(Map<String, Serializable> proxyInformations) {
        Session session = _session.get();
        if (session == null) {
            throw new NullPointerException("Cannot load : no session opened !");
        }
        Serializable id = proxyInformations.get(ID);
        String className = (String) proxyInformations.get(CLASS_NAME);
        Class<?> persistentClass = null;
        try {
            persistentClass = Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (Exception e) {
            throw new RuntimeException("Cannot find persistent class : " + className, e);
        }
        return session.load(persistentClass, id);
    }

    public Map<String, Serializable> serializePersistentCollection(Object persistentCollection) {
        Map<String, Serializable> result = new HashMap<String, Serializable>();
        AbstractPersistentCollection collection = (AbstractPersistentCollection) persistentCollection;
        result.put(CLASS_NAME, collection.getClass().getName());
        result.put(ROLE, collection.getRole());
        result.put(KEY, collection.getKey());
        if (isInitialized(collection) == true) {
            if (collection instanceof Collection) {
                result.put(ID_LIST, createIdList((Collection) collection));
            } else if (collection instanceof Map) {
                Map map = (Map) collection;
                ArrayList<SerializableId> keyList = createIdList(map.keySet());
                if (keyList != null) {
                    result.put(ID_LIST, keyList);
                    ArrayList<SerializableId> valueList = createIdList(map.values());
                    if (keyList != null) {
                        result.put(VALUE_LIST, valueList);
                    }
                }
            } else {
                throw new RuntimeException("Unexpected Persistent collection : " + collection.getClass());
            }
        }
        return result;
    }

    /**
	 * Create a persistent collection
	 * @param proxyInformations serialized proxy informations 
	 * @param underlyingCollection the filled underlying collection
	 * @return
	 */
    public Object createPersistentCollection(Map<String, Serializable> proxyInformations, Object underlyingCollection) {
        Session session = _session.get();
        if (session == null) {
            throw new NullPointerException("Cannot load : no session opened !");
        }
        Collection<?> deletedItems = addDeletedItems(proxyInformations, underlyingCollection);
        String className = (String) proxyInformations.get(CLASS_NAME);
        PersistentCollection collection = null;
        if (PersistentBag.class.getName().equals(className)) {
            if (underlyingCollection == null) {
                collection = new PersistentBag((SessionImpl) session);
            } else {
                collection = new PersistentBag((SessionImpl) session, (Collection<?>) underlyingCollection);
            }
        } else if (PersistentList.class.getName().equals(className)) {
            if (underlyingCollection == null) {
                collection = new PersistentList((SessionImpl) session);
            } else {
                collection = new PersistentList((SessionImpl) session, (List<?>) underlyingCollection);
            }
        } else if (PersistentSet.class.getName().equals(className)) {
            if (underlyingCollection == null) {
                collection = new PersistentSet((SessionImpl) session);
            } else {
                collection = new PersistentSet((SessionImpl) session, (Set<?>) underlyingCollection);
            }
        } else if (PersistentMap.class.getName().equals(className)) {
            if (underlyingCollection == null) {
                collection = new PersistentMap((SessionImpl) session);
            } else {
                collection = new PersistentMap((SessionImpl) session, (Map<?, ?>) underlyingCollection);
            }
        } else {
            throw new RuntimeException("Unknown persistent collection class name : " + className);
        }
        String role = (String) proxyInformations.get(ROLE);
        Serializable snapshot = null;
        if (underlyingCollection != null) {
            CollectionPersister collectionPersister = _sessionFactory.getCollectionPersister(role);
            snapshot = collection.getSnapshot(collectionPersister);
        }
        collection.setSnapshot(proxyInformations.get(KEY), role, snapshot);
        if (deletedItems != null) {
            if (collection instanceof Collection) {
                ((Collection) collection).removeAll(deletedItems);
            } else if (collection instanceof Map) {
                for (Object key : deletedItems) {
                    ((Map) collection).remove(key);
                }
            }
        }
        return collection;
    }

    public boolean isPersistentCollection(Class<?> collectionClass) {
        return (PersistentCollection.class.isAssignableFrom(collectionClass));
    }

    public boolean isInitialized(Object proxy) {
        return Hibernate.isInitialized(proxy);
    }

    public void initialize(Object proxy) {
        Hibernate.initialize(proxy);
    }

    /**
	 * Compute embedded persistence (Component, UserType) for argument class
	 */
    private void computePersistenceForClass(Class<?> clazz) {
        synchronized (_persistenceMap) {
            if (_persistenceMap.get(clazz.getName()) != null) {
                return;
            }
        }
        ClassMetadata metadata = _sessionFactory.getClassMetadata(clazz);
        if (metadata == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (int index = 0; index < interfaces.length; index++) {
                    if (isPersistentClass(interfaces[index])) {
                        markClassAsPersistent(clazz, true);
                        return;
                    }
                }
            }
            markClassAsPersistent(clazz, false);
            return;
        }
        markClassAsPersistent(clazz, true);
        Type[] types = metadata.getPropertyTypes();
        for (int index = 0; index < types.length; index++) {
            Type type = types[index];
            if (_log.isDebugEnabled()) {
                _log.debug("Scanning type " + type.getName() + " from " + clazz);
            }
            computePersistentForType(type);
        }
    }

    /**
	 * Mark class as persistent or not
	 * @param clazz
	 * @param persistent
	 */
    private void markClassAsPersistent(Class<?> clazz, boolean persistent) {
        if (_log.isDebugEnabled()) {
            if (persistent) {
                _log.debug("Marking " + clazz + " as persistent");
            } else {
                _log.debug("Marking " + clazz + " as not persistent");
            }
        }
        synchronized (_persistenceMap) {
            String className = clazz.getName();
            if (_persistenceMap.get(className) == null) {
                _persistenceMap.put(className, persistent);
            } else {
                if (persistent != _persistenceMap.get(className).booleanValue()) {
                    throw new RuntimeException("Invalid persistence state for " + clazz);
                }
            }
        }
    }

    /**
	 * Compute persistent for Hibernate type
	 * @param type
	 */
    private void computePersistentForType(Type type) {
        if (_log.isDebugEnabled()) {
            _log.debug("Scanning type " + type.getName());
        }
        if (type.isComponentType()) {
            if (_log.isDebugEnabled()) {
                _log.debug("Type " + type.getName() + " is component type");
            }
            markClassAsPersistent(type.getReturnedClass(), true);
            Type[] subtypes = ((AbstractComponentType) type).getSubtypes();
            for (int index = 0; index < subtypes.length; index++) {
                computePersistentForType(subtypes[index]);
            }
        } else if (IUserType.class.isAssignableFrom(type.getReturnedClass())) {
            if (_log.isDebugEnabled()) {
                _log.debug("Type " + type.getName() + " is user type");
            }
            markClassAsPersistent(type.getReturnedClass(), true);
        } else if (type.isCollectionType()) {
            if (_log.isDebugEnabled()) {
                _log.debug("Type " + type.getName() + " is collection type");
            }
            computePersistentForType(((CollectionType) type).getElementType(_sessionFactory));
        } else if (type.isEntityType()) {
            if (_log.isDebugEnabled()) {
                _log.debug("Type " + type.getName() + " is entity type");
            }
            computePersistenceForClass(type.getReturnedClass());
        }
    }

    /**
	 * Debug method : dump persistence map for checking
	 */
    private void dumpPersistenceMap() {
        synchronized (_persistenceMap) {
            _log.debug("-- Start of persistence map --");
            for (Entry<String, Boolean> persistenceEntry : _persistenceMap.entrySet()) {
                _log.debug(persistenceEntry.getKey() + " persistence is " + persistenceEntry.getValue());
            }
            _log.debug("-- End of persistence map --");
        }
    }

    /**
	 * Seach the underlying Hibernate entity manager factory implementation.
	 * @param object
	 * @return the entity manager factory if found, null otherwise
	 */
    private static HibernateEntityManagerFactory searchHibernateImplementation(Object object) {
        if ((object == null) || (object.getClass().getName().startsWith("java."))) {
            return null;
        }
        Field[] fields = IntrospectionHelper.getRecursiveDeclaredFields(object.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value instanceof HibernateEntityManagerFactory) {
                    return (HibernateEntityManagerFactory) value;
                }
                value = searchHibernateImplementation(value);
                if (value != null) {
                    return (HibernateEntityManagerFactory) value;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
	 * Create a list of serializable ID for the argument collection
	 * @param collection
	 * @return
	 */
    private ArrayList<SerializableId> createIdList(Collection collection) {
        int size = collection.size();
        ArrayList<SerializableId> idList = new ArrayList<SerializableId>(size);
        Iterator<Object> iterator = ((Collection) collection).iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (isPersistentPojo(item)) {
                SerializableId id = new SerializableId();
                id.setId(getId(item));
                id.setClassName(item.getClass().getName());
                idList.add(id);
            }
        }
        if (idList.isEmpty()) {
            return null;
        } else {
            return idList;
        }
    }

    /**
	 * Compute deleted items for collection recreation
	 * @param collection
	 * @param idList
	 * @return
	 */
    private List<Object> getDeletedItemsForCollection(Collection collection, ArrayList<SerializableId> idList) {
        Session session = _session.get();
        if (session == null) {
            throw new NullPointerException("Cannot load : no session opened !");
        }
        ArrayList<Object> deletedItems = new ArrayList<Object>();
        for (SerializableId sid : idList) {
            boolean found = false;
            Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                try {
                    if (sid.getId().equals(getId(iterator.next()))) {
                        found = true;
                        break;
                    }
                } catch (TransientObjectException ex) {
                }
            }
            if (found == false) {
                try {
                    Class<?> itemClass = Thread.currentThread().getContextClassLoader().loadClass(sid.getClassName());
                    itemClass = UnEnhancer.unenhanceClass(itemClass);
                    Object proxy = session.load(itemClass, sid.getId());
                    deletedItems.add(proxy);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (deletedItems.isEmpty()) {
            return null;
        } else {
            return deletedItems;
        }
    }

    /**
	 * Add deleted items to the underlying collection, so the Hibernate PersistentCollection
	 * snapshot will take care of deleted items
	 * @param proxyInformations
	 * @param underlyingCollection
	 * @return the deleted items list
	 */
    private Collection<?> addDeletedItems(Map<String, Serializable> proxyInformations, Object underlyingCollection) {
        if (underlyingCollection instanceof Collection) {
            Collection<?> collection = (Collection<?>) underlyingCollection;
            ArrayList<SerializableId> idList = (ArrayList<SerializableId>) proxyInformations.get(ID_LIST);
            if (idList != null) {
                Collection deletedItemList = getDeletedItemsForCollection(collection, idList);
                if (deletedItemList != null) {
                    collection.addAll(deletedItemList);
                }
                return deletedItemList;
            }
        } else if (underlyingCollection instanceof Map) {
            Map map = (Map) underlyingCollection;
            ArrayList<SerializableId> idList = (ArrayList<SerializableId>) proxyInformations.get(ID_LIST);
            if (idList != null) {
                List deletedKeyList = getDeletedItemsForCollection(map.keySet(), idList);
                if (deletedKeyList != null) {
                    List deletedValueList = null;
                    ArrayList<SerializableId> valueList = (ArrayList<SerializableId>) proxyInformations.get(VALUE_LIST);
                    if (valueList != null) {
                        deletedValueList = getDeletedItemsForCollection(map.values(), idList);
                    }
                    int deleteCount = deletedKeyList.size();
                    for (int index = 0; index < deleteCount; index++) {
                        Object key = deletedKeyList.get(index);
                        Object value = null;
                        if ((deletedValueList != null) && (index < deletedValueList.size())) {
                            value = deletedValueList.get(index);
                        }
                        map.put(key, value);
                    }
                }
                return deletedKeyList;
            }
        }
        return null;
    }

    /**
	 * Check if the id equals the unsaved value or not
	 * @param entity
	 * @return
	 */
    private boolean isUnsavedValue(Serializable id, Class<?> persistentClass) {
        if (id == null) {
            return true;
        }
        return id.toString().equals("0");
    }

    /**
	 * Return the underlying persistent class
	 * @param pojo
	 * @return
	 */
    private Class<?> getPersistentClass(Object pojo) {
        if (pojo instanceof HibernateProxy) {
            return ((HibernateProxy) pojo).getHibernateLazyInitializer().getPersistentClass();
        } else {
            return pojo.getClass();
        }
    }
}
