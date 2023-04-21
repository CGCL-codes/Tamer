package org.openscience.nmrshiftdb.om;

import java.util.List;
import java.util.Vector;
import org.apache.turbine.om.BaseObject;
import org.apache.turbine.om.ComboKey;
import org.apache.turbine.om.NumberKey;
import org.apache.turbine.om.ObjectKey;
import org.apache.turbine.om.Retrievable;
import org.apache.turbine.om.SimpleKey;
import org.apache.turbine.om.peer.BasePeer;
import org.apache.turbine.util.ObjectUtils;
import org.apache.turbine.util.db.pool.DBConnection;

/** 
 * You should not use this class directly.  It should not even be
 * extended all references should be to DBSpectrumDBLiterature 
 */
public abstract class BaseDBSpectrumDBLiterature extends BaseObject implements Retrievable {

    /** the value for the spectrum_id field */
    private NumberKey spectrum_id;

    /** the value for the literature_id field */
    private NumberKey literature_id;

    /**
     * Get the SpectrumId
     * @return NumberKey
     */
    public NumberKey getSpectrumId() {
        return spectrum_id;
    }

    /**
     * Set the value of SpectrumId
     */
    public void setSpectrumId(NumberKey v) throws Exception {
        aDBSpectrum = null;
        if (!ObjectUtils.equals(this.spectrum_id, v)) {
            if (this.spectrum_id == null) {
                this.spectrum_id = new NumberKey(v);
            } else {
                this.spectrum_id.setValue(v);
            }
            setModified(true);
        }
    }

    /**
    * Set the value of SpectrumId as a string.
    */
    public void setSpectrumId(String v) throws Exception {
        setSpectrumId(new NumberKey(v));
    }

    /**
     * Get the LiteratureId
     * @return NumberKey
     */
    public NumberKey getLiteratureId() {
        return literature_id;
    }

    /**
     * Set the value of LiteratureId
     */
    public void setLiteratureId(NumberKey v) throws Exception {
        aDBLiterature = null;
        if (!ObjectUtils.equals(this.literature_id, v)) {
            if (this.literature_id == null) {
                this.literature_id = new NumberKey(v);
            } else {
                this.literature_id.setValue(v);
            }
            setModified(true);
        }
    }

    /**
    * Set the value of LiteratureId as a string.
    */
    public void setLiteratureId(String v) throws Exception {
        setLiteratureId(new NumberKey(v));
    }

    private DBSpectrum aDBSpectrum;

    /**
     * Declares an association between this object and a DBSpectrum object
     *
     * @param DBSpectrum v
     */
    public void setDBSpectrum(DBSpectrum v) throws Exception {
        setSpectrumId(v.getSpectrumId());
        aDBSpectrum = v;
    }

    public DBSpectrum getDBSpectrum() throws Exception {
        if (aDBSpectrum == null && (!ObjectUtils.equals(this.spectrum_id, null))) {
            aDBSpectrum = DBSpectrumPeer.retrieveByPK(this.spectrum_id);
        }
        return aDBSpectrum;
    }

    /**
     * Provides convenient way to set a relationship based on a 
     * ObjectKey.  e.g. 
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setDBSpectrumKey(ObjectKey key) throws Exception {
        setSpectrumId((NumberKey) key);
    }

    private DBLiterature aDBLiterature;

    /**
     * Declares an association between this object and a DBLiterature object
     *
     * @param DBLiterature v
     */
    public void setDBLiterature(DBLiterature v) throws Exception {
        setLiteratureId(v.getLiteratureId());
        aDBLiterature = v;
    }

    public DBLiterature getDBLiterature() throws Exception {
        if (aDBLiterature == null && (!ObjectUtils.equals(this.literature_id, null))) {
            aDBLiterature = DBLiteraturePeer.retrieveByPK(this.literature_id);
        }
        return aDBLiterature;
    }

    /**
     * Provides convenient way to set a relationship based on a 
     * ObjectKey.  e.g. 
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setDBLiteratureKey(ObjectKey key) throws Exception {
        setLiteratureId((NumberKey) key);
    }

    private static Vector fieldNames_ = null;

    /**
     * Generate a list of field names.
     */
    public static synchronized List getFieldNames() {
        if (fieldNames_ == null) {
            fieldNames_ = new Vector();
            fieldNames_.add("SpectrumId");
            fieldNames_.add("LiteratureId");
        }
        return fieldNames_;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.
     */
    public Object getByName(String name) {
        if (name.equals("SpectrumId")) {
            return getSpectrumId();
        }
        if (name.equals("LiteratureId")) {
            return getLiteratureId();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     */
    public Object getByPeerName(String name) {
        if (name.equals(DBSpectrumDBLiteraturePeer.SPECTRUM_ID)) {
            return getSpectrumId();
        }
        if (name.equals(DBSpectrumDBLiteraturePeer.LITERATURE_ID)) {
            return getLiteratureId();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     */
    public Object getByPosition(int pos) {
        if (pos == 0) {
            return getSpectrumId();
        }
        if (pos == 1) {
            return getLiteratureId();
        }
        return null;
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     */
    public void save() throws Exception {
        save(DBSpectrumDBLiteraturePeer.getMapBuilder().getDatabaseMap().getName());
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
                    DBSpectrumDBLiteraturePeer.doInsert((DBSpectrumDBLiterature) this, dbCon);
                    setNew(false);
                } else {
                    DBSpectrumDBLiteraturePeer.doUpdate((DBSpectrumDBLiterature) this, dbCon);
                }
            }
            alreadyInSave = false;
        }
    }

    private final SimpleKey[] pks = new SimpleKey[2];

    private final ComboKey comboPK = new ComboKey(pks);

    /** 
     * Set the PrimaryKey with an ObjectKey
     */
    public void setPrimaryKey(ObjectKey key) throws Exception {
        SimpleKey[] keys = (SimpleKey[]) key.getValue();
        setSpectrumId((NumberKey) keys[0]);
        setLiteratureId((NumberKey) keys[1]);
    }

    /** 
     * Set the PrimaryKey using SimpleKeys.
     *
     * @param NumberKey spectrum_id
     * @param NumberKey literature_id
     */
    public void setPrimaryKey(NumberKey spectrum_id, NumberKey literature_id) throws Exception {
        setSpectrumId(spectrum_id);
        setLiteratureId(literature_id);
    }

    /** 
     * Set the PrimaryKey with Strings.
     *
     * @param String spectrum_id
     * @param String literature_id
     */
    public void setPrimaryKey(String spectrum_id, String literature_id) throws Exception {
        setSpectrumId(new NumberKey(spectrum_id));
        setLiteratureId(new NumberKey(literature_id));
    }

    /** 
     * Set the PrimaryKey using a String.
     */
    public void setPrimaryKey(String key) throws Exception {
        int prevPos = 0;
        int colonPos = key.indexOf(':', prevPos);
        setSpectrumId(new NumberKey(key.substring(prevPos, colonPos)));
        prevPos = colonPos + 1;
        colonPos = key.indexOf(':', prevPos);
        setLiteratureId(new NumberKey(key.substring(prevPos, colonPos)));
        prevPos = colonPos + 1;
    }

    /** 
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey() {
        pks[0] = getSpectrumId();
        pks[1] = getLiteratureId();
        return comboPK;
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
    public DBSpectrumDBLiterature copy() throws Exception {
        DBSpectrumDBLiterature copyObj = new DBSpectrumDBLiterature();
        copyObj.setSpectrumId(spectrum_id);
        copyObj.setLiteratureId(literature_id);
        copyObj.setSpectrumId((NumberKey) null);
        copyObj.setLiteratureId((NumberKey) null);
        return copyObj;
    }
}
