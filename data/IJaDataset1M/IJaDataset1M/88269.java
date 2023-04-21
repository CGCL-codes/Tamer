package org.openscience.nmrshiftdb.om;

import java.util.List;
import java.util.Vector;
import org.apache.turbine.om.BaseObject;
import org.apache.turbine.om.NumberKey;
import org.apache.turbine.om.ObjectKey;
import org.apache.turbine.om.Persistent;
import org.apache.turbine.om.Retrievable;
import org.apache.turbine.om.peer.BasePeer;
import org.apache.turbine.util.ObjectUtils;
import org.apache.turbine.util.db.Criteria;
import org.apache.turbine.util.db.pool.DBConnection;

/** 
 * You should not use this class directly.  It should not even be
 * extended all references should be to DBPublisher 
 */
public abstract class BaseDBPublisher extends BaseObject implements Retrievable {

    /** the value for the publisher_id field */
    private NumberKey publisher_id;

    /** the value for the name field */
    private String name;

    /** the value for the place field */
    private String place;

    /**
     * Get the PublisherId
     * @return NumberKey
     */
    public NumberKey getPublisherId() {
        return publisher_id;
    }

    /**
     * Set the value of PublisherId
     */
    public void setPublisherId(NumberKey v) throws Exception {
        if (collDBBookDBPublishers != null) {
            for (int i = 0; i < collDBBookDBPublishers.size(); i++) {
                ((DBBookDBPublisher) collDBBookDBPublishers.get(i)).setPublisherId(v);
            }
        }
        if (!ObjectUtils.equals(this.publisher_id, v)) {
            if (this.publisher_id == null) {
                this.publisher_id = new NumberKey(v);
            } else {
                this.publisher_id.setValue(v);
            }
            setModified(true);
        }
    }

    /**
    * Set the value of PublisherId as a string.
    */
    public void setPublisherId(String v) throws Exception {
        setPublisherId(new NumberKey(v));
    }

    /**
     * Get the Name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of Name
     */
    public void setName(String v) {
        if (!ObjectUtils.equals(this.name, v)) {
            this.name = v;
            setModified(true);
        }
    }

    /**
     * Get the Place
     * @return String
     */
    public String getPlace() {
        return place;
    }

    /**
     * Set the value of Place
     */
    public void setPlace(String v) {
        if (!ObjectUtils.equals(this.place, v)) {
            this.place = v;
            setModified(true);
        }
    }

    /**
     * Collection to store aggregation of collDBBookDBPublishers
     */
    protected Vector collDBBookDBPublishers;

    protected void initDBBookDBPublishers() {
        if (collDBBookDBPublishers == null) collDBBookDBPublishers = new Vector();
    }

    /**
     * Method called to associate a DBBookDBPublisher object to this object
     * through the DBBookDBPublisher foreign key attribute
     *
     * @param DBBookDBPublisher l
     */
    public void addDBBookDBPublisher(DBBookDBPublisher l) throws Exception {
        getDBBookDBPublishers().add(l);
        l.setDBPublisher((DBPublisher) this);
    }

    /**
     * The criteria used to select the current contents of collDBBookDBPublishers
     */
    private Criteria lastDBBookDBPublishersCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of 
     * getDBBookDBPublishers(new Criteria())
     */
    public Vector getDBBookDBPublishers() throws Exception {
        if (collDBBookDBPublishers == null) {
            collDBBookDBPublishers = getDBBookDBPublishers(new Criteria(10));
        }
        return collDBBookDBPublishers;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection. 
     * Otherwise if this DBPublisher has previously
     * been saved, it will retrieve related DBBookDBPublishers from storage.
     * If this DBPublisher is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object. 
     */
    public Vector getDBBookDBPublishers(Criteria criteria) throws Exception {
        if (collDBBookDBPublishers == null) {
            if (isNew()) {
                collDBBookDBPublishers = new Vector();
            } else {
                criteria.add(DBBookDBPublisherPeer.PUBLISHER_ID, getPublisherId());
                collDBBookDBPublishers = DBBookDBPublisherPeer.doSelect(criteria);
            }
        } else {
            if (!isNew()) {
                criteria.add(DBBookDBPublisherPeer.PUBLISHER_ID, getPublisherId());
                if (!lastDBBookDBPublishersCriteria.equals(criteria)) {
                    collDBBookDBPublishers = DBBookDBPublisherPeer.doSelect(criteria);
                }
            }
        }
        lastDBBookDBPublishersCriteria = criteria;
        return collDBBookDBPublishers;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection. 
     * Otherwise if this DBPublisher is new, it will return
     * an empty collection; or if this DBPublisher has previously
     * been saved, it will retrieve related DBBookDBPublishers from storage.
     */
    protected Vector getDBBookDBPublishersJoinDBLiterature(Criteria criteria) throws Exception {
        if (collDBBookDBPublishers == null) {
            if (isNew()) {
                collDBBookDBPublishers = new Vector();
            } else {
                criteria.add(DBBookDBPublisherPeer.PUBLISHER_ID, getPublisherId());
                collDBBookDBPublishers = DBBookDBPublisherPeer.doSelectJoinDBLiterature(criteria);
            }
        } else {
            boolean newCriteria = true;
            criteria.add(DBBookDBPublisherPeer.PUBLISHER_ID, getPublisherId());
            if (!lastDBBookDBPublishersCriteria.equals(criteria)) {
                collDBBookDBPublishers = DBBookDBPublisherPeer.doSelectJoinDBLiterature(criteria);
            }
        }
        lastDBBookDBPublishersCriteria = criteria;
        return collDBBookDBPublishers;
    }

    private static Vector fieldNames_ = null;

    /**
     * Generate a list of field names.
     */
    public static synchronized List getFieldNames() {
        if (fieldNames_ == null) {
            fieldNames_ = new Vector();
            fieldNames_.add("PublisherId");
            fieldNames_.add("Name");
            fieldNames_.add("Place");
        }
        return fieldNames_;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.
     */
    public Object getByName(String name) {
        if (name.equals("PublisherId")) {
            return getPublisherId();
        }
        if (name.equals("Name")) {
            return getName();
        }
        if (name.equals("Place")) {
            return getPlace();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     */
    public Object getByPeerName(String name) {
        if (name.equals(DBPublisherPeer.PUBLISHER_ID)) {
            return getPublisherId();
        }
        if (name.equals(DBPublisherPeer.NAME)) {
            return getName();
        }
        if (name.equals(DBPublisherPeer.PLACE)) {
            return getPlace();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     */
    public Object getByPosition(int pos) {
        if (pos == 0) {
            return getPublisherId();
        }
        if (pos == 1) {
            return getName();
        }
        if (pos == 2) {
            return getPlace();
        }
        return null;
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     */
    public void save() throws Exception {
        save(DBPublisherPeer.getMapBuilder().getDatabaseMap().getName());
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     */
    public void save(String dbName) throws Exception {
        DBConnection dbCon = null;
        try {
            dbCon = BasePeer.beginTransaction(dbName);
            save(dbCon);
        } catch (Exception e) {
            BasePeer.rollBackTransaction(dbCon);
            throw e;
        }
        BasePeer.commitTransaction(dbCon);
    }

    private boolean alreadyInSave = false;

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     */
    public void save(DBConnection dbCon) throws Exception {
        if (!alreadyInSave) {
            alreadyInSave = true;
            if (isModified()) {
                if (isNew()) {
                    DBPublisherPeer.doInsert((DBPublisher) this, dbCon);
                    setNew(false);
                } else {
                    DBPublisherPeer.doUpdate((DBPublisher) this, dbCon);
                }
            }
            if (collDBBookDBPublishers != null) {
                for (int i = 0; i < collDBBookDBPublishers.size(); i++) {
                    ((DBBookDBPublisher) collDBBookDBPublishers.get(i)).save(dbCon);
                }
            }
            alreadyInSave = false;
        }
    }

    /** 
     * Set the PrimaryKey using ObjectKey.
     *
     * @param ObjectKey publisher_id
     */
    public void setPrimaryKey(ObjectKey publisher_id) throws Exception {
        setPublisherId((NumberKey) publisher_id);
    }

    /** 
     * Set the PrimaryKey using a String.
     */
    public void setPrimaryKey(String key) throws Exception {
        setPublisherId(new NumberKey(key));
    }

    /** 
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey() {
        return getPublisherId();
    }

    /** 
     * get an id that differentiates this object from others
     * of its class.
     */
    public String getQueryKey() {
        if (getPrimaryKey() == null) {
            return "";
        } else {
            return getPrimaryKey().toString();
        }
    }

    /** 
     * set an id that differentiates this object from others
     * of its class.
     */
    public void setQueryKey(String key) throws Exception {
        setPrimaryKey(key);
    }

    /**
     * Makes a copy of this object.  
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public DBPublisher copy() throws Exception {
        DBPublisher copyObj = new DBPublisher();
        copyObj.setPublisherId(publisher_id);
        copyObj.setName(name);
        copyObj.setPlace(place);
        List v = copyObj.getDBBookDBPublishers();
        for (int i = 0; i < v.size(); i++) {
            ((Persistent) v.get(i)).setNew(true);
        }
        copyObj.setPublisherId((NumberKey) null);
        return copyObj;
    }
}
