package org.xebra.scp.db.persist;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.xebra.scp.db.exception.KeyNotFoundException;
import org.xebra.scp.db.exception.PersistException;
import org.xebra.scp.db.model.HxTiObject;
import org.xebra.scp.db.model.Series;
import org.xebra.scp.db.model.Study;
import org.xebra.scp.db.model.query.HxTiQuery;
import org.xebra.scp.db.peer.AEPeer;
import org.xebra.scp.db.sql.SqlClientContainer;
import org.xebra.scp.db.sql.XMLMethodMap;
import org.xebra.scp.db.sql.event.DatabaseAccessEvent;
import org.xebra.scp.db.sql.event.DatabaseAccessListener;

/**
 * Adds methods which simplify the development of subclasses.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.3 $
 */
abstract class AbstractLoader<T extends HxTiObject> implements Loader<T> {

    private static final EventListenerList EVENT_LIST = new EventListenerList();

    public void addDatabaseAccessListener(DatabaseAccessListener listener) {
        EVENT_LIST.add(DatabaseAccessListener.class, listener);
    }

    public void removeDatabaseAccessListener(DatabaseAccessListener listener) {
        EVENT_LIST.remove(DatabaseAccessListener.class, listener);
    }

    /**
     * Used to test for the presence of an object.
     *
     * @param objectType The object type.
     * @param uid The UID of the object.
     * @return Returns true if the object is present, false otherwise.
     * 
     * @throws PersistException
     */
    boolean test(Class<T> objectType, String uid) throws PersistException {
        LOG.trace("Checking for presence of " + objectType.getSimpleName() + " with UID: " + uid);
        DatabaseAccessEvent event = new DatabaseAccessEvent(this, objectType);
        event.setUidAccessed(uid);
        event.setObjectAccessType(objectType);
        XMLMethodMap methodMap = XMLMethodMap.getInstance();
        String xmlMethodId = null;
        boolean test = false;
        try {
            xmlMethodId = methodMap.getXMLMethodId(objectType, XMLMethodMap.TEST);
            LOG.trace("XML Method ID: " + xmlMethodId);
            Integer i = (Integer) SqlClientContainer.sql().queryForObject(xmlMethodId, uid);
            if (i != null && i.intValue() > 0) {
                LOG.trace("Object found");
                test = true;
            }
        } catch (SQLException exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".test(" + objectType.getName() + "," + uid + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".test(" + xmlMethodId + "," + uid + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByUIDEvent(event);
            throw pExc;
        } catch (Throwable exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".test(" + objectType.getName() + "," + uid + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".test(" + xmlMethodId + "," + uid + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByUIDEvent(event);
            throw pExc;
        }
        event.setMessage(DatabaseAccessEvent.ACTION_SUCCESSFULL);
        fireLoadByUIDEvent(event);
        return test;
    }

    /**
	 * Loads all the objects of a certain type.
	 * 
	 * @param objectType The type of object to load.
	 * 
	 * @return Returns a list of all the objects in the database for 
	 *         the correct object type.
	 * 
	 * @throws PersistException Thrown if there is a sql error.
	 */
    List<T> loadAll(Class<T> objectType) throws PersistException {
        LOG.trace("Loading all objects of type: " + objectType.getSimpleName());
        DatabaseAccessEvent event = new DatabaseAccessEvent(this, objectType);
        XMLMethodMap methodMap = XMLMethodMap.getInstance();
        String xmlMethodId = null;
        List<T> list = null;
        try {
            xmlMethodId = methodMap.getXMLMethodId(objectType, XMLMethodMap.LIST_ALL);
            LOG.trace("XML Method ID: " + xmlMethodId);
            list = SqlClientContainer.sql().queryForList(xmlMethodId, null);
        } catch (SQLException exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadAll(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadAll(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadAllEvent(event);
            throw pExc;
        } catch (Throwable exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadAll(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadAll(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadAllEvent(event);
            throw pExc;
        }
        if (list == null || list.size() == 0) {
            event.setMessage(DatabaseAccessEvent.EMPTY_LIST);
            list = new ArrayList<T>();
        } else {
            event.setMessage(DatabaseAccessEvent.ACTION_SUCCESSFULL);
        }
        fireLoadAllEvent(event);
        return list;
    }

    /**
	 * Loads a list of objects using a query object.
	 * 
	 * @param objectType The type of object to load.
	 * @param query The object query.
	 * 
	 * @return Returns a list of objects in the database based upon
	 *         a given query.
	 * 
	 * @throws PersistException Thrown if there is a sql error.
	 */
    List<T> loadByQuery(Class<T> objectType, HxTiQuery<T> query) throws PersistException {
        LOG.trace("Loading objects of type " + objectType.getSimpleName() + " by query");
        DatabaseAccessEvent event = new DatabaseAccessEvent(this, objectType);
        event.setQuery(query);
        XMLMethodMap methodMap = XMLMethodMap.getInstance();
        String xmlMethodId = null;
        List<T> list = null;
        try {
            SqlClientContainer.sql().hashCode();
            xmlMethodId = methodMap.getXMLMethodId(objectType, XMLMethodMap.LIST_BY_QUERY);
            LOG.trace("XML Method ID: " + xmlMethodId);
            list = SqlClientContainer.sql().queryForList(xmlMethodId, query);
        } catch (SQLException exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByQuery(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByQuery(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByQueryEvent(event);
            throw pExc;
        } catch (Throwable exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByQuery(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByQuery(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByQueryEvent(event);
            throw pExc;
        }
        if (list == null || list.size() == 0) {
            event.setMessage(DatabaseAccessEvent.EMPTY_LIST);
            list = new ArrayList<T>();
        } else {
            event.setMessage(DatabaseAccessEvent.ACTION_SUCCESSFULL);
        }
        fireLoadByQueryEvent(event);
        return list;
    }

    /**
	 * Loads the peer of a specified object.
	 * 
	 * @param objectType The object type.
	 * @param uid The UID of the object.
	 * 
	 * @return Returns the specified broker.
	 * 
	 * @throws PersistException Throws if there is a sql error.
	 */
    AEPeer loadPeer(Class<T> objectType, String uid) throws PersistException {
        LOG.trace("Loading object peer");
        DatabaseAccessEvent event = new DatabaseAccessEvent(this, objectType);
        event.setUidAccessed("UID: " + uid);
        AEPeer broker = null;
        String method = null;
        try {
            Object item = null;
            if (objectType.getName().equals(Study.OBJECT_TYPE)) {
                method = "peerByStudyUID";
            } else if (objectType.getName().equals(Series.OBJECT_TYPE)) {
                method = "peerBySeriesUID";
            } else {
                method = "peerByInstanceUID";
            }
            item = SqlClientContainer.sql().queryForObject(method, uid);
            if (item != null && item instanceof AEPeer) {
                broker = (AEPeer) item;
            }
        } catch (SQLException exc) {
            PersistException pExc = null;
            if (method == null) pExc = new PersistException(this.getClass().getName() + ".loadBroker(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadBroker(" + method + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByUIDEvent(event);
            throw pExc;
        } catch (Throwable exc) {
            PersistException pExc = null;
            if (method == null) pExc = new PersistException(this.getClass().getName() + ".loadPeer(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadPeer(" + method + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByUIDEvent(event);
            throw pExc;
        }
        event.setMessage(DatabaseAccessEvent.ACTION_SUCCESSFULL);
        fireLoadByUIDEvent(event);
        return broker;
    }

    /**
	 * This method loads an object by its UID.
	 * 
	 * @param objectType The type of object to load.
	 * @param uid The object's UID.
	 * 
	 * @return Returns the object with the given type and UID.
	 * 
	 * @throws PersistException Thrown if there is a sql error.
	 * @throws KeyNotFoundException Thrown if the object cannot be found.
	 */
    T loadByUID(Class<T> objectType, String uid) throws PersistException, KeyNotFoundException {
        LOG.trace("Loading " + objectType.getSimpleName() + " by " + uid);
        DatabaseAccessEvent event = new DatabaseAccessEvent(this, objectType);
        event.setUidAccessed("UID: " + uid);
        XMLMethodMap methodMap = XMLMethodMap.getInstance();
        String xmlMethodId = null;
        T item = null;
        try {
            xmlMethodId = methodMap.getXMLMethodId(objectType, XMLMethodMap.GET_BY_UID);
            LOG.trace("XML Method ID: " + xmlMethodId);
            if (xmlMethodId == null) throw new PersistException(this.getClass().getName() + ".loadByUID(" + objectType.getName() + "): Could not find an xml method");
            Object object = SqlClientContainer.sql().queryForObject(xmlMethodId, uid);
            if (object == null) throw new KeyNotFoundException(this.getClass().getName() + ".loadByUID(" + objectType.getName() + "): Could not find an object of this type with the UID - " + uid);
            item = (T) object;
        } catch (KeyNotFoundException exc) {
            event.setMessage(DatabaseAccessEvent.NOT_FOUND);
            event.setException(exc);
            fireLoadByUIDEvent(event);
            throw exc;
        } catch (SQLException exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByUID(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByUID(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByUIDEvent(event);
            throw pExc;
        } catch (Throwable exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByUID(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByUID(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByUIDEvent(event);
            throw pExc;
        }
        event.setMessage(DatabaseAccessEvent.ACTION_SUCCESSFULL);
        fireLoadByUIDEvent(event);
        return item;
    }

    /**
	 * This method loads an object by its child's UID.
	 * 
	 * @param objectType The type of object to load.
	 * @param uid The object's child UID.
	 * 
	 * @return Returns the object with the given type and 
	 * and child's UID.
	 * 
	 * @throws PersistException Thrown if there is a sql error.
	 * @throws KeyNotFoundException Thrown if the object can't be found.
	 */
    T loadByChildUID(Class<T> objectType, String uid) throws PersistException, KeyNotFoundException {
        LOG.trace("Loading " + objectType.getSimpleName() + " by child uid: " + uid);
        DatabaseAccessEvent event = new DatabaseAccessEvent(this, objectType);
        event.setUidAccessed("CHILD UID: " + uid);
        XMLMethodMap methodMap = XMLMethodMap.getInstance();
        String xmlMethodId = null;
        T item = null;
        try {
            xmlMethodId = methodMap.getXMLMethodId(objectType, XMLMethodMap.GET_BY_CHILD_UID);
            LOG.trace("XML Method ID: " + xmlMethodId);
            Object object = SqlClientContainer.sql().queryForObject(xmlMethodId, uid);
            if (object == null) throw new KeyNotFoundException(this.getClass().getName() + ".loadByChildUID(" + objectType.getName() + "): Could not find an object of this type with the child object UID - " + uid);
            item = (T) object;
        } catch (KeyNotFoundException exc) {
            event.setMessage(DatabaseAccessEvent.NOT_FOUND);
            event.setException(exc);
            fireLoadByChildUIDEvent(event);
            throw exc;
        } catch (SQLException exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByChildUID(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByChildUID(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByChildUIDEvent(event);
            throw pExc;
        } catch (Throwable exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByChildUID(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByChildUID(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByChildUIDEvent(event);
            throw pExc;
        }
        event.setMessage(DatabaseAccessEvent.ACTION_SUCCESSFULL);
        fireLoadByChildUIDEvent(event);
        return item;
    }

    /**
	 * Loads a list of objects by the parent object UID.
	 * 
	 * @param objectType The type of object to load.
	 * @param uid The object's parent's UID.
	 * 
	 * @return Returns the list of objects associated with the parent UID.
	 * 
	 * @throws PersistException Thrown if there is a sql error.
	 */
    List<T> loadByParentUID(Class<T> objectType, String uid) throws PersistException {
        LOG.trace("Loading children of " + objectType.getSimpleName() + " by " + uid);
        DatabaseAccessEvent event = new DatabaseAccessEvent(this, objectType);
        event.setUidAccessed("PARENT UID: " + uid);
        XMLMethodMap methodMap = XMLMethodMap.getInstance();
        String xmlMethodId = null;
        List<T> list = null;
        try {
            xmlMethodId = methodMap.getXMLMethodId(objectType, XMLMethodMap.LIST_BY_PARENT_UID);
            LOG.trace("XML Method ID: " + xmlMethodId);
            list = SqlClientContainer.sql().queryForList(xmlMethodId, uid);
        } catch (SQLException exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByParentUID(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByParentUID(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByParentUIDEvent(event);
            throw pExc;
        } catch (Throwable exc) {
            PersistException pExc = null;
            if (xmlMethodId == null) pExc = new PersistException(this.getClass().getName() + ".loadByParentUID(" + objectType.getName() + "): " + exc.getMessage(), exc);
            pExc = new PersistException(this.getClass().getName() + ".loadByParentUID(" + xmlMethodId + "): " + exc.getMessage(), exc);
            event.setMessage(DatabaseAccessEvent.PERSIST_ERR);
            event.setException(pExc);
            fireLoadByParentUIDEvent(event);
            throw pExc;
        }
        if (list == null || list.size() == 0) {
            event.setMessage(DatabaseAccessEvent.EMPTY_LIST);
            list = new ArrayList<T>();
        } else {
            event.setMessage(DatabaseAccessEvent.ACTION_SUCCESSFULL);
        }
        fireLoadByParentUIDEvent(event);
        return list;
    }

    void fireLoadAllEvent(DatabaseAccessEvent event) {
        Object[] listeners = EVENT_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i].equals(DatabaseAccessListener.class)) {
                DatabaseAccessListener listener = (DatabaseAccessListener) listeners[i + 1];
                listener.databaseReadAttempt(event);
                listener.loadAllAttempt(event);
                if (!event.isSuccessfull()) {
                    listener.persistErrorThrown(event);
                }
            }
        }
    }

    void fireLoadByUIDEvent(DatabaseAccessEvent event) {
        Object[] listeners = EVENT_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i].equals(DatabaseAccessListener.class)) {
                DatabaseAccessListener listener = (DatabaseAccessListener) listeners[i + 1];
                listener.databaseReadAttempt(event);
                listener.loadByUIDAttempt(event);
                if (!event.isSuccessfull()) {
                    listener.persistErrorThrown(event);
                }
            }
        }
    }

    void fireLoadByParentUIDEvent(DatabaseAccessEvent event) {
        Object[] listeners = EVENT_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i].equals(DatabaseAccessListener.class)) {
                DatabaseAccessListener listener = (DatabaseAccessListener) listeners[i + 1];
                listener.databaseReadAttempt(event);
                listener.loadByParentUIDAttempt(event);
                if (!event.isSuccessfull()) {
                    listener.persistErrorThrown(event);
                }
            }
        }
    }

    void fireLoadByChildUIDEvent(DatabaseAccessEvent event) {
        Object[] listeners = EVENT_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i].equals(DatabaseAccessListener.class)) {
                DatabaseAccessListener listener = (DatabaseAccessListener) listeners[i + 1];
                listener.databaseReadAttempt(event);
                listener.loadByChildUIDAttempt(event);
                if (!event.isSuccessfull()) {
                    listener.persistErrorThrown(event);
                }
            }
        }
    }

    void fireLoadByQueryEvent(DatabaseAccessEvent event) {
        Object[] listeners = EVENT_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i].equals(DatabaseAccessListener.class)) {
                DatabaseAccessListener listener = (DatabaseAccessListener) listeners[i + 1];
                listener.databaseReadAttempt(event);
                listener.loadByQueryAttempt(event);
                if (!event.isSuccessfull()) {
                    listener.persistErrorThrown(event);
                }
            }
        }
    }
}
