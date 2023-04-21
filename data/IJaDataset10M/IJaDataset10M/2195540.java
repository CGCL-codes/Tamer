package amplitude.persistence.hibernate.base;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import amplitude.persistence.hibernate.dao.iface.GenreDAO;
import org.hibernate.criterion.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseGenreDAO extends amplitude.persistence.hibernate.dao._RootDAO {

    public BaseGenreDAO() {
    }

    public BaseGenreDAO(Session session) {
        super(session);
    }

    public static GenreDAO instance;

    /**
	 * Return a singleton of the DAO
	 */
    public static GenreDAO getInstance() {
        if (null == instance) instance = new amplitude.persistence.hibernate.dao.GenreDAO();
        return instance;
    }

    public Class getReferenceClass() {
        return amplitude.persistence.hibernate.Genre.class;
    }

    public Order getDefaultOrder() {
        return null;
    }

    /**
	 * Cast the object as a amplitude.persistence.hibernate.Genre
	 */
    public amplitude.persistence.hibernate.Genre cast(Object object) {
        return (amplitude.persistence.hibernate.Genre) object;
    }

    public amplitude.persistence.hibernate.Genre get(java.lang.Integer key) {
        return (amplitude.persistence.hibernate.Genre) get(getReferenceClass(), key);
    }

    public amplitude.persistence.hibernate.Genre get(java.lang.Integer key, Session s) {
        return (amplitude.persistence.hibernate.Genre) get(getReferenceClass(), key, s);
    }

    public amplitude.persistence.hibernate.Genre load(java.lang.Integer key) {
        return (amplitude.persistence.hibernate.Genre) load(getReferenceClass(), key);
    }

    public amplitude.persistence.hibernate.Genre load(java.lang.Integer key, Session s) {
        return (amplitude.persistence.hibernate.Genre) load(getReferenceClass(), key, s);
    }

    public amplitude.persistence.hibernate.Genre loadInitialize(java.lang.Integer key, Session s) {
        amplitude.persistence.hibernate.Genre obj = load(key, s);
        if (!Hibernate.isInitialized(obj)) {
            Hibernate.initialize(obj);
        }
        return obj;
    }

    /**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
    public java.util.List<amplitude.persistence.hibernate.Genre> findAll() {
        return super.findAll();
    }

    /**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
    public java.util.List<amplitude.persistence.hibernate.Genre> findAll(Order defaultOrder) {
        return super.findAll(defaultOrder);
    }

    /**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
    public java.util.List<amplitude.persistence.hibernate.Genre> findAll(Session s, Order defaultOrder) {
        return super.findAll(s, defaultOrder);
    }

    /**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param genre a transient instance of a persistent class 
	 * @return the class identifier
	 */
    public java.lang.Integer save(amplitude.persistence.hibernate.Genre genre) {
        return (java.lang.Integer) super.save(genre);
    }

    /**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param genre a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
    public java.lang.Integer save(amplitude.persistence.hibernate.Genre genre, Session s) {
        return (java.lang.Integer) save((Object) genre, s);
    }

    /**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param genre a transient instance containing new or updated state 
	 */
    public void saveOrUpdate(amplitude.persistence.hibernate.Genre genre) {
        saveOrUpdate((Object) genre);
    }

    /**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param genre a transient instance containing new or updated state.
	 * @param s the Session.
	 */
    public void saveOrUpdate(amplitude.persistence.hibernate.Genre genre, Session s) {
        saveOrUpdate((Object) genre, s);
    }

    /**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param genre a transient instance containing updated state
	 */
    public void update(amplitude.persistence.hibernate.Genre genre) {
        update((Object) genre);
    }

    /**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param genre a transient instance containing updated state
	 * @param the Session
	 */
    public void update(amplitude.persistence.hibernate.Genre genre, Session s) {
        update((Object) genre, s);
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
    public void delete(java.lang.Integer id) {
        delete((Object) load(id));
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param id the instance ID to be removed
	 * @param s the Session
	 */
    public void delete(java.lang.Integer id, Session s) {
        delete((Object) load(id, s), s);
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param genre the instance to be removed
	 */
    public void delete(amplitude.persistence.hibernate.Genre genre) {
        delete((Object) genre);
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param genre the instance to be removed
	 * @param s the Session
	 */
    public void delete(amplitude.persistence.hibernate.Genre genre, Session s) {
        delete((Object) genre, s);
    }

    /**
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 * For example 
	 * <ul> 
	 * <li>where a database trigger alters the object state upon insert or update</li>
	 * <li>after executing direct SQL (eg. a mass update) in the same session</li>
	 * <li>after inserting a Blob or Clob</li>
	 * </ul>
	 */
    public void refresh(amplitude.persistence.hibernate.Genre genre, Session s) {
        refresh((Object) genre, s);
    }
}
