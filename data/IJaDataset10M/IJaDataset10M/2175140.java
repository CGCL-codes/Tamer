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
 * extended all references should be to DBJournalVolume 
 */
public abstract class BaseDBJournalVolume extends BaseObject implements Retrievable {

    /** the value for the journal_volume_id field */
    private NumberKey journal_volume_id;

    /** the value for the volume field */
    private int volume;

    /** the value for the year field */
    private int year;

    /** the value for the journal_id field */
    private NumberKey journal_id;

    /**
     * Get the JournalVolumeId
     * @return NumberKey
     */
    public NumberKey getJournalVolumeId() {
        return journal_volume_id;
    }

    /**
     * Set the value of JournalVolumeId
     */
    public void setJournalVolumeId(NumberKey v) throws Exception {
        if (collDBArticles != null) {
            for (int i = 0; i < collDBArticles.size(); i++) {
                ((DBArticle) collDBArticles.get(i)).setPublishedInJournal(v);
            }
        }
        if (!ObjectUtils.equals(this.journal_volume_id, v)) {
            if (this.journal_volume_id == null) {
                this.journal_volume_id = new NumberKey(v);
            } else {
                this.journal_volume_id.setValue(v);
            }
            setModified(true);
        }
    }

    /**
    * Set the value of JournalVolumeId as a string.
    */
    public void setJournalVolumeId(String v) throws Exception {
        setJournalVolumeId(new NumberKey(v));
    }

    /**
     * Get the Volume
     * @return int
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Set the value of Volume
     */
    public void setVolume(int v) {
        if (this.volume != v) {
            this.volume = v;
            setModified(true);
        }
    }

    /**
     * Get the Year
     * @return int
     */
    public int getYear() {
        return year;
    }

    /**
     * Set the value of Year
     */
    public void setYear(int v) {
        if (this.year != v) {
            this.year = v;
            setModified(true);
        }
    }

    /**
     * Get the JournalId
     * @return NumberKey
     */
    public NumberKey getJournalId() {
        return journal_id;
    }

    /**
     * Set the value of JournalId
     */
    public void setJournalId(NumberKey v) throws Exception {
        aDBJournal = null;
        if (!ObjectUtils.equals(this.journal_id, v)) {
            if (this.journal_id == null) {
                this.journal_id = new NumberKey(v);
            } else {
                this.journal_id.setValue(v);
            }
            setModified(true);
        }
    }

    /**
    * Set the value of JournalId as a string.
    */
    public void setJournalId(String v) throws Exception {
        setJournalId(new NumberKey(v));
    }

    private DBJournal aDBJournal;

    /**
     * Declares an association between this object and a DBJournal object
     *
     * @param DBJournal v
     */
    public void setDBJournal(DBJournal v) throws Exception {
        setJournalId(v.getJournalId());
        aDBJournal = v;
    }

    public DBJournal getDBJournal() throws Exception {
        if (aDBJournal == null && (!ObjectUtils.equals(this.journal_id, null))) {
            aDBJournal = DBJournalPeer.retrieveByPK(this.journal_id);
        }
        return aDBJournal;
    }

    /**
     * Provides convenient way to set a relationship based on a 
     * ObjectKey.  e.g. 
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setDBJournalKey(ObjectKey key) throws Exception {
        setJournalId((NumberKey) key);
    }

    /**
     * Collection to store aggregation of collDBArticles
     */
    protected Vector collDBArticles;

    protected void initDBArticles() {
        if (collDBArticles == null) collDBArticles = new Vector();
    }

    /**
     * Method called to associate a DBArticle object to this object
     * through the DBArticle foreign key attribute
     *
     * @param DBArticle l
     */
    public void addDBArticle(DBArticle l) throws Exception {
        getDBArticles().add(l);
        l.setDBJournalVolume((DBJournalVolume) this);
    }

    /**
     * The criteria used to select the current contents of collDBArticles
     */
    private Criteria lastDBArticlesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of 
     * getDBArticles(new Criteria())
     */
    public Vector getDBArticles() throws Exception {
        if (collDBArticles == null) {
            collDBArticles = getDBArticles(new Criteria(10));
        }
        return collDBArticles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection. 
     * Otherwise if this DBJournalVolume has previously
     * been saved, it will retrieve related DBArticles from storage.
     * If this DBJournalVolume is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object. 
     */
    public Vector getDBArticles(Criteria criteria) throws Exception {
        if (collDBArticles == null) {
            if (isNew()) {
                collDBArticles = new Vector();
            } else {
                criteria.add(DBArticlePeer.PUBLISHED_IN_JOURNAL, getJournalVolumeId());
                collDBArticles = DBArticlePeer.doSelect(criteria);
            }
        } else {
            if (!isNew()) {
                criteria.add(DBArticlePeer.PUBLISHED_IN_JOURNAL, getJournalVolumeId());
                if (!lastDBArticlesCriteria.equals(criteria)) {
                    collDBArticles = DBArticlePeer.doSelect(criteria);
                }
            }
        }
        lastDBArticlesCriteria = criteria;
        return collDBArticles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection. 
     * Otherwise if this DBJournalVolume is new, it will return
     * an empty collection; or if this DBJournalVolume has previously
     * been saved, it will retrieve related DBArticles from storage.
     */
    protected Vector getDBArticlesJoinDBLiteratureRelatedByLiteratureId(Criteria criteria) throws Exception {
        if (collDBArticles == null) {
            if (isNew()) {
                collDBArticles = new Vector();
            } else {
                criteria.add(DBArticlePeer.PUBLISHED_IN_JOURNAL, getJournalVolumeId());
                collDBArticles = DBArticlePeer.doSelectJoinDBLiteratureRelatedByLiteratureId(criteria);
            }
        } else {
            boolean newCriteria = true;
            criteria.add(DBArticlePeer.PUBLISHED_IN_JOURNAL, getJournalVolumeId());
            if (!lastDBArticlesCriteria.equals(criteria)) {
                collDBArticles = DBArticlePeer.doSelectJoinDBLiteratureRelatedByLiteratureId(criteria);
            }
        }
        lastDBArticlesCriteria = criteria;
        return collDBArticles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection. 
     * Otherwise if this DBJournalVolume is new, it will return
     * an empty collection; or if this DBJournalVolume has previously
     * been saved, it will retrieve related DBArticles from storage.
     */
    protected Vector getDBArticlesJoinDBLiteratureRelatedByPublishedInBook(Criteria criteria) throws Exception {
        if (collDBArticles == null) {
            if (isNew()) {
                collDBArticles = new Vector();
            } else {
                criteria.add(DBArticlePeer.PUBLISHED_IN_JOURNAL, getJournalVolumeId());
                collDBArticles = DBArticlePeer.doSelectJoinDBLiteratureRelatedByPublishedInBook(criteria);
            }
        } else {
            boolean newCriteria = true;
            criteria.add(DBArticlePeer.PUBLISHED_IN_JOURNAL, getJournalVolumeId());
            if (!lastDBArticlesCriteria.equals(criteria)) {
                collDBArticles = DBArticlePeer.doSelectJoinDBLiteratureRelatedByPublishedInBook(criteria);
            }
        }
        lastDBArticlesCriteria = criteria;
        return collDBArticles;
    }

    private static Vector fieldNames_ = null;

    /**
     * Generate a list of field names.
     */
    public static synchronized List getFieldNames() {
        if (fieldNames_ == null) {
            fieldNames_ = new Vector();
            fieldNames_.add("JournalVolumeId");
            fieldNames_.add("Volume");
            fieldNames_.add("Year");
            fieldNames_.add("JournalId");
        }
        return fieldNames_;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.
     */
    public Object getByName(String name) {
        if (name.equals("JournalVolumeId")) {
            return getJournalVolumeId();
        }
        if (name.equals("Volume")) {
            return new Integer(getVolume());
        }
        if (name.equals("Year")) {
            return new Integer(getYear());
        }
        if (name.equals("JournalId")) {
            return getJournalId();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     */
    public Object getByPeerName(String name) {
        if (name.equals(DBJournalVolumePeer.JOURNAL_VOLUME_ID)) {
            return getJournalVolumeId();
        }
        if (name.equals(DBJournalVolumePeer.VOLUME)) {
            return new Integer(getVolume());
        }
        if (name.equals(DBJournalVolumePeer.YEAR)) {
            return new Integer(getYear());
        }
        if (name.equals(DBJournalVolumePeer.JOURNAL_ID)) {
            return getJournalId();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     */
    public Object getByPosition(int pos) {
        if (pos == 0) {
            return getJournalVolumeId();
        }
        if (pos == 1) {
            return new Integer(getVolume());
        }
        if (pos == 2) {
            return new Integer(getYear());
        }
        if (pos == 3) {
            return getJournalId();
        }
        return null;
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     */
    public void save() throws Exception {
        save(DBJournalVolumePeer.getMapBuilder().getDatabaseMap().getName());
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
                    DBJournalVolumePeer.doInsert((DBJournalVolume) this, dbCon);
                    setNew(false);
                } else {
                    DBJournalVolumePeer.doUpdate((DBJournalVolume) this, dbCon);
                }
            }
            if (collDBArticles != null) {
                for (int i = 0; i < collDBArticles.size(); i++) {
                    ((DBArticle) collDBArticles.get(i)).save(dbCon);
                }
            }
            alreadyInSave = false;
        }
    }

    /** 
     * Set the PrimaryKey using ObjectKey.
     *
     * @param ObjectKey journal_volume_id
     */
    public void setPrimaryKey(ObjectKey journal_volume_id) throws Exception {
        setJournalVolumeId((NumberKey) journal_volume_id);
    }

    /** 
     * Set the PrimaryKey using a String.
     */
    public void setPrimaryKey(String key) throws Exception {
        setJournalVolumeId(new NumberKey(key));
    }

    /** 
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey() {
        return getJournalVolumeId();
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
    public DBJournalVolume copy() throws Exception {
        DBJournalVolume copyObj = new DBJournalVolume();
        copyObj.setJournalVolumeId(journal_volume_id);
        copyObj.setVolume(volume);
        copyObj.setYear(year);
        copyObj.setJournalId(journal_id);
        List v = copyObj.getDBArticles();
        for (int i = 0; i < v.size(); i++) {
            ((Persistent) v.get(i)).setNew(true);
        }
        copyObj.setJournalVolumeId((NumberKey) null);
        return copyObj;
    }
}
