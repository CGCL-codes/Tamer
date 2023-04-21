package org.hibernate.collection;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.jcvi.vics.model.common.ParameterVOMapUserType;
import org.jcvi.vics.model.user_data.FastaFileNode;
import org.jcvi.vics.model.user_data.FileNode;
import org.jcvi.vics.model.user_data.blast.BlastDatabaseFileNode;
import org.jcvi.vics.model.user_data.blast.BlastResultFileNode;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * This class is responsible for removing any references contained in a model object
 * that are not consumable by GWT.  It's based on HibernateUtils.java we found on
 * http://www.jboss.com/index.html?module=bb&op=viewtopic&t=89436&postdays=0&postorder=asc&start=10
 * <p/>
 * Unfortunately it needs to reside in org.hibernate.collection because it needs access to protected
 * member variables in that package.
 *
 * @author Tareq Nabeel
 */
public class GWTEntityCleaner {

    private static Logger logger = Logger.getLogger(GWTEntityCleaner.class);

    private static Logger hibernateLogger = Logger.getLogger("org.hibernate");

    private static Level existingHibernateLevel = hibernateLogger.getLevel();

    /**
     * This method removes any references contained in obj that are not consumable by GWT.
     * It is expected that the Hibernate session is open when this method is called.
     * It is also expected that obj has been evicted from the session at this point if obj
     * is not java.util.Collection
     *
     * @param obj The entity to be cleaned or the collection of entities
     */
    public static void clean(Object obj) {
        cleanObject(obj, new ArrayList<Integer>(), null);
    }

    /**
     * This method evicts the object from the session before cleaning it up.
     *
     * @param obj
     * @param session
     */
    public static void evictAndClean(Object obj, Session session) {
        if (obj instanceof Collection) {
            for (Object o : (Collection) obj) {
                evictAndClean(o, session);
            }
        } else {
            cleanObject(obj, new ArrayList<Integer>(), session);
        }
    }

    /**
     * Checks if the object contains other objects that might need cleaning
     *
     * @param obj
     */
    private static boolean objectNeedsCleanup(Object obj) {
        Class objClass = obj.getClass();
        return classNeedsCleanup(objClass);
    }

    /**
     * Checks if the class of object needs cleaning
     *
     * @param objClass
     */
    private static boolean classNeedsCleanup(Class objClass) {
        if (objClass.isPrimitive()) {
            return false;
        }
        if (objClass.getName().startsWith("java.lang") || objClass.getName().startsWith("javax.")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * If the specified object's identity hash code is not in the specified
     * collection of visited hash codes, this method removes objects not understood
     * by GWT e.g. hibernate proxies and java.sql.Timestamp
     *
     * @param obj            The object to clean; could be entity or collection of entities
     * @param visitedObjects The collection of objects that have already been cleaned
     */
    private static Object cleanObject(Object obj, Collection<Integer> visitedObjects, Session session) {
        if (obj == null) {
            return null;
        }
        if (session != null && session.isOpen()) session.evict(obj);
        if (visitedObjects.contains(System.identityHashCode(obj))) {
            return obj;
        } else {
            visitedObjects.add(System.identityHashCode(obj));
        }
        if ((obj instanceof PersistentCollection)) {
            obj = getInnerCollection((PersistentCollection) obj);
            if (obj == null) {
                return null;
            }
        }
        if (!objectNeedsCleanup(obj)) {
            return obj;
        }
        if (obj instanceof Collection) {
            for (Object o : (Collection) obj) {
                cleanObject(o, visitedObjects, session);
            }
        } else {
            cleanEntityProperties(obj, visitedObjects, session);
        }
        return obj;
    }

    /**
     * This method cleans the properties of the entity passed in
     *
     * @param entity
     * @param visitedObjects
     */
    private static void cleanEntityProperties(Object entity, Collection<Integer> visitedObjects, Session session) {
        cleanEntityOfLazyObjects(entity);
        Map objectProperties = getEntityProperties(entity);
        for (Object member : objectProperties.entrySet()) {
            if (session != null && session.isOpen()) session.evict(member);
            Map.Entry property = (Map.Entry) member;
            String propertyKey = property.getKey().toString();
            Object propertyValue = property.getValue();
            cleanEntityPropertyOfGWTIgnorantObjects(propertyKey, propertyValue, entity, visitedObjects, session);
        }
    }

    /**
     * Needs to be called before PropertyUtils.describe(entity) which could call getters
     * that in turn inencounter lazy
     *
     * @param entity
     */
    private static void cleanEntityOfLazyObjects(Object entity) {
        PropertyDescriptor[] propDescriptors = PropertyUtils.getPropertyDescriptors(entity);
        hibernateLogger.setLevel(Level.FATAL);
        for (PropertyDescriptor propDescriptor : propDescriptors) {
            if (propDescriptor.getReadMethod() != null) {
                try {
                    propDescriptor.getReadMethod().invoke(entity);
                } catch (Exception e) {
                    try {
                        propDescriptor.setReadMethod(null);
                    } catch (Exception e1) {
                    }
                }
            }
        }
        hibernateLogger.setLevel(existingHibernateLevel);
    }

    /**
     * This method is the 2nd half of this class's heart.  Replace any instance variables that
     * are not understood by GWT
     *
     * @param propertyKey
     * @param entity
     * @param visitedObjects
     */
    private static void cleanEntityPropertyOfGWTIgnorantObjects(String propertyKey, Object propertyValue, Object entity, Collection<Integer> visitedObjects, Session session) {
        if (propertyValue instanceof HibernateProxy) {
            setProperty(entity, propertyKey, null);
        } else if (propertyValue instanceof Timestamp) {
            setProperty(entity, propertyKey, new Date(((Timestamp) propertyValue).getTime()));
        } else if (propertyValue instanceof java.sql.Date) {
            setProperty(entity, propertyKey, new Date(((java.sql.Date) propertyValue).getTime()));
        } else if ((propertyValue instanceof ParameterVOMapUserType) || (propertyValue instanceof BlastDatabaseFileNode) || (propertyValue instanceof BlastResultFileNode) || (propertyValue instanceof FastaFileNode) || (propertyValue instanceof FileNode)) {
            setProperty(entity, propertyKey, null);
        } else {
            setProperty(entity, propertyKey, cleanObject(propertyValue, visitedObjects, session));
        }
    }

    private static void setProperty(Object entity, String propertyKey, Object newValue) {
        try {
            if (entity != null && PropertyUtils.isWriteable(entity, propertyKey)) {
                PropertyUtils.setProperty(entity, propertyKey, newValue);
            }
        } catch (Exception e) {
            logger.error("Caught Exception in setProperty for entity=" + entity.getClass().getName() + " propertyKey=" + propertyKey, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * If obj contains Hibernate proxies that wrap lazy objects, Hibernate session needs to be
     * open when this method is called
     *
     * @param obj
     * @return map of object properties
     */
    private static Map getEntityProperties(Object obj) {
        try {
            return PropertyUtils.describe(obj);
        } catch (IllegalAccessException e) {
            logger.error("Caught IllegalAccessException in getObjectProperties():", e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("obj=" + obj);
            if (obj != null) logger.error("obj.class=" + obj.getClass());
            logger.error("Caught InvocationTargetException in getObjectProperties():", e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            logger.error("Caught NoSuchMethodException in getObjectProperties():", e);
            throw new RuntimeException(e);
        } catch (org.hibernate.LazyInitializationException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Because this class is in package org.hibernate.collection this method
     * allows us to access the inner protected fields of the hibernate collections.
     *
     * @param obj
     * @return The inner collection object of the PersistentCollection parameter
     */
    private static Object getInnerCollection(PersistentCollection obj) {
        if (obj instanceof PersistentBag) {
            return ((PersistentBag) obj).bag;
        } else if (obj instanceof PersistentList) {
            return ((PersistentList) obj).list;
        } else if (obj instanceof PersistentSet) {
            return ((PersistentSet) obj).set;
        } else if (obj instanceof PersistentMap) {
            return ((PersistentMap) obj).map;
        } else {
            return null;
        }
    }
}
