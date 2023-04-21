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
 * extended all references should be to DBBookmark 
 */
public abstract class BaseDBBookmark extends BaseObject implements Retrievable {

    /** the value for the molecule_id field */
    private NumberKey molecule_id;

    /** the value for the user_id field */
    private NumberKey user_id;

    /**
     * Get the MoleculeId
     * @return NumberKey
     */
    public NumberKey getMoleculeId() {
        return molecule_id;
    }

    /**
     * Set the value of MoleculeId
     */
    public void setMoleculeId(NumberKey v) throws Exception {
        aDBMolecule = null;
        if (!ObjectUtils.equals(this.molecule_id, v)) {
            if (this.molecule_id == null) {
                this.molecule_id = new NumberKey(v);
            } else {
                this.molecule_id.setValue(v);
            }
            setModified(true);
        }
    }

    /**
    * Set the value of MoleculeId as a string.
    */
    public void setMoleculeId(String v) throws Exception {
        setMoleculeId(new NumberKey(v));
    }

    /**
     * Get the UserId
     * @return NumberKey
     */
    public NumberKey getUserId() {
        return user_id;
    }

    /**
     * Set the value of UserId
     */
    public void setUserId(NumberKey v) throws Exception {
        aNmrshiftdbUser = null;
        if (!ObjectUtils.equals(this.user_id, v)) {
            if (this.user_id == null) {
                this.user_id = new NumberKey(v);
            } else {
                this.user_id.setValue(v);
            }
            setModified(true);
        }
    }

    /**
    * Set the value of UserId as a string.
    */
    public void setUserId(String v) throws Exception {
        setUserId(new NumberKey(v));
    }

    private DBMolecule aDBMolecule;

    /**
     * Declares an association between this object and a DBMolecule object
     *
     * @param DBMolecule v
     */
    public void setDBMolecule(DBMolecule v) throws Exception {
        setMoleculeId(v.getMoleculeId());
        aDBMolecule = v;
    }

    public DBMolecule getDBMolecule() throws Exception {
        if (aDBMolecule == null && (!ObjectUtils.equals(this.molecule_id, null))) {
            aDBMolecule = DBMoleculePeer.retrieveByPK(this.molecule_id);
        }
        return aDBMolecule;
    }

    /**
     * Provides convenient way to set a relationship based on a 
     * ObjectKey.  e.g. 
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setDBMoleculeKey(ObjectKey key) throws Exception {
        setMoleculeId((NumberKey) key);
    }

    private NmrshiftdbUser aNmrshiftdbUser;

    /**
     * Declares an association between this object and a NmrshiftdbUser object
     *
     * @param NmrshiftdbUser v
     */
    public void setNmrshiftdbUser(NmrshiftdbUser v) throws Exception {
        setUserId(v.getUserId());
        aNmrshiftdbUser = v;
    }

    public NmrshiftdbUser getNmrshiftdbUser() throws Exception {
        if (aNmrshiftdbUser == null && (!ObjectUtils.equals(this.user_id, null))) {
            aNmrshiftdbUser = NmrshiftdbUserPeer.retrieveByPK(this.user_id);
        }
        return aNmrshiftdbUser;
    }

    /**
     * Provides convenient way to set a relationship based on a 
     * ObjectKey.  e.g. 
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setNmrshiftdbUserKey(ObjectKey key) throws Exception {
        setUserId((NumberKey) key);
    }

    private static Vector fieldNames_ = null;

    /**
     * Generate a list of field names.
     */
    public static synchronized List getFieldNames() {
        if (fieldNames_ == null) {
            fieldNames_ = new Vector();
            fieldNames_.add("MoleculeId");
            fieldNames_.add("UserId");
        }
        return fieldNames_;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.
     */
    public Object getByName(String name) {
        if (name.equals("MoleculeId")) {
            return getMoleculeId();
        }
        if (name.equals("UserId")) {
            return getUserId();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     */
    public Object getByPeerName(String name) {
        if (name.equals(DBBookmarkPeer.MOLECULE_ID)) {
            return getMoleculeId();
        }
        if (name.equals(DBBookmarkPeer.USER_ID)) {
            return getUserId();
        }
        return null;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     */
    public Object getByPosition(int pos) {
        if (pos == 0) {
            return getMoleculeId();
        }
        if (pos == 1) {
            return getUserId();
        }
        return null;
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     */
    public void save() throws Exception {
        save(DBBookmarkPeer.getMapBuilder().getDatabaseMap().getName());
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
                    DBBookmarkPeer.doInsert((DBBookmark) this, dbCon);
                    setNew(false);
                } else {
                    DBBookmarkPeer.doUpdate((DBBookmark) this, dbCon);
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
        setMoleculeId((NumberKey) keys[0]);
        setUserId((NumberKey) keys[1]);
    }

    /** 
     * Set the PrimaryKey using SimpleKeys.
     *
     * @param NumberKey molecule_id
     * @param NumberKey user_id
     */
    public void setPrimaryKey(NumberKey molecule_id, NumberKey user_id) throws Exception {
        setMoleculeId(molecule_id);
        setUserId(user_id);
    }

    /** 
     * Set the PrimaryKey with Strings.
     *
     * @param String molecule_id
     * @param String user_id
     */
    public void setPrimaryKey(String molecule_id, String user_id) throws Exception {
        setMoleculeId(new NumberKey(molecule_id));
        setUserId(new NumberKey(user_id));
    }

    /** 
     * Set the PrimaryKey using a String.
     */
    public void setPrimaryKey(String key) throws Exception {
        int prevPos = 0;
        int colonPos = key.indexOf(':', prevPos);
        setMoleculeId(new NumberKey(key.substring(prevPos, colonPos)));
        prevPos = colonPos + 1;
        colonPos = key.indexOf(':', prevPos);
        setUserId(new NumberKey(key.substring(prevPos, colonPos)));
        prevPos = colonPos + 1;
    }

    /** 
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey() {
        pks[0] = getMoleculeId();
        pks[1] = getUserId();
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
    public DBBookmark copy() throws Exception {
        DBBookmark copyObj = new DBBookmark();
        copyObj.setMoleculeId(molecule_id);
        copyObj.setUserId(user_id);
        copyObj.setMoleculeId((NumberKey) null);
        copyObj.setUserId((NumberKey) null);
        return copyObj;
    }
}
