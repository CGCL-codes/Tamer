package org.dmp.chillout.cpd.database.hibernate.base;

import org.dmp.chillout.cpd.database.hibernate.dao.TlicenseinfoDAO;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseTlicenseinfoDAO extends org.dmp.chillout.cpd.database.hibernate.dao._RootDAO {

    public BaseTlicenseinfoDAO() {
    }

    public BaseTlicenseinfoDAO(Session session) {
        super(session);
    }

    public static TlicenseinfoDAO instance;

    /**
	 * Return a singleton of the DAO
	 */
    public static TlicenseinfoDAO getInstance() {
        if (null == instance) instance = new org.dmp.chillout.cpd.database.hibernate.dao.TlicenseinfoDAO();
        return instance;
    }

    public Class getReferenceClass() {
        return org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo.class;
    }

    public Order getDefaultOrder() {
        return null;
    }

    /**
	 * Cast the object as a org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo
	 */
    public org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo cast(Object object) {
        return (org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo) object;
    }

    public org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo get(java.lang.String key) {
        return (org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo) get(getReferenceClass(), key);
    }

    public org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo get(java.lang.String key, Session s) {
        return (org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo) get(getReferenceClass(), key, s);
    }

    public org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo load(java.lang.String key) {
        return (org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo) load(getReferenceClass(), key);
    }

    public org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo load(java.lang.String key, Session s) {
        return (org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo) load(getReferenceClass(), key, s);
    }

    public org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo loadInitialize(java.lang.String key, Session s) {
        org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo obj = load(key, s);
        if (!Hibernate.isInitialized(obj)) {
            Hibernate.initialize(obj);
        }
        return obj;
    }

    /**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
    public java.util.List<org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo> findAll() {
        return super.findAll();
    }

    /**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
    public java.util.List<org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo> findAll(Order defaultOrder) {
        return super.findAll(defaultOrder);
    }

    /**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
    public java.util.List<org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo> findAll(Session s, Order defaultOrder) {
        return super.findAll(s, defaultOrder);
    }

    /**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param tlicenseinfo a transient instance of a persistent class 
	 * @return the class identifier
	 */
    public java.lang.String save(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo) {
        return (java.lang.String) super.save(tlicenseinfo);
    }

    /**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param tlicenseinfo a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
    public java.lang.String save(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo, Session s) {
        return (java.lang.String) save((Object) tlicenseinfo, s);
    }

    /**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param tlicenseinfo a transient instance containing new or updated state 
	 */
    public void saveOrUpdate(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo) {
        saveOrUpdate((Object) tlicenseinfo);
    }

    /**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param tlicenseinfo a transient instance containing new or updated state.
	 * @param s the Session.
	 */
    public void saveOrUpdate(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo, Session s) {
        saveOrUpdate((Object) tlicenseinfo, s);
    }

    /**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param tlicenseinfo a transient instance containing updated state
	 */
    public void update(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo) {
        update((Object) tlicenseinfo);
    }

    /**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param tlicenseinfo a transient instance containing updated state
	 * @param the Session
	 */
    public void update(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo, Session s) {
        update((Object) tlicenseinfo, s);
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
    public void delete(java.lang.String id) {
        delete((Object) load(id));
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param id the instance ID to be removed
	 * @param s the Session
	 */
    public void delete(java.lang.String id, Session s) {
        delete((Object) load(id, s), s);
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param tlicenseinfo the instance to be removed
	 */
    public void delete(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo) {
        delete((Object) tlicenseinfo);
    }

    /**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param tlicenseinfo the instance to be removed
	 * @param s the Session
	 */
    public void delete(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo, Session s) {
        delete((Object) tlicenseinfo, s);
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
    public void refresh(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo, Session s) {
        refresh((Object) tlicenseinfo, s);
    }
}
