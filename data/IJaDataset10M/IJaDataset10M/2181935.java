package com.incendiaryblue.user;

import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.storage.IntegerCaselessStringKey;
import com.incendiaryblue.storage.StorageManager;
import java.util.*;
import java.io.*;

/**
 *	@author $Author: jon $ - h.soran@syzygy.net
 *	@version $Revision: 1.3 $ - $Date: 2001/12/06 16:47:17 $  
 *  	This class is to represent customised user properties. It has a concatanated key
 *		which consists of userID and property name. This class is used and managed by User 
 *		class only. The primary key is the conc Key in the database.
 *
 */
public class UserProperty extends UserBusinessObject implements UserConstants {

    /** Strorage manager of this class */
    private static final StorageManager storageManager = (StorageManager) AppConfig.getComponent(StorageManager.class, "UserProperty");

    public static final String QUERY_BY_USER = "property_by_user";

    private String szValue;

    /** 
	 * Gets the user property object by its concatenated key. 
	 * @return required user property object by its ids.
	 */
    public static UserProperty getUserProperty(User oUser, String szPropName) {
        Integer oUserID = (Integer) oUser.getPrimaryKey();
        IntegerCaselessStringKey oConKey = new IntegerCaselessStringKey(oUserID, szPropName);
        return (UserProperty) storageManager.getObject(oConKey);
    }

    /** @return list of properties of required user by its id */
    public static List getProperties(User oUser) {
        return storageManager.getObjectList(QUERY_BY_USER, oUser.getPrimaryKey());
    }

    public void delete() {
        storageManager.delete(this);
    }

    public void store() {
        storageManager.store(this);
    }

    /**
	 * Create a new property for a user.
	 */
    public UserProperty(User oUser, String szName, String szValue) {
        super(true, new java.util.Date());
        setPrimaryKey(new IntegerCaselessStringKey((Integer) oUser.getPrimaryKey(), szName));
        this.szValue = szValue;
    }

    /**
	 * Recreated a user property that has been retreived from the database.
	 */
    public UserProperty(Integer oUserID, String szName, String szValue, boolean active, Date dChanged) {
        super(active, dChanged);
        setPrimaryKey(new IntegerCaselessStringKey(oUserID, szName));
        this.szValue = szValue;
    }

    /** Gets the value of the property. */
    public String getValue() {
        return this.szValue;
    }

    /** Sets the value of the property */
    public void setValue(String szValue) {
        this.szValue = szValue;
        setChanged();
    }

    /** Get the id of the user this property is related to. */
    public Integer getUserId() {
        return ((IntegerCaselessStringKey) getPrimaryKey()).getFirst();
    }

    /** Get the name of the property. */
    public String getName() {
        return ((IntegerCaselessStringKey) getPrimaryKey()).getSecond();
    }

    /** @return a string version of this object  */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UserProperty: ");
        buffer.append("user id == ");
        buffer.append(getUserId());
        buffer.append(", name == ");
        buffer.append(getName());
        buffer.append(", value == ");
        buffer.append(getValue());
        return buffer.toString();
    }

    /**
	 * Delete all the properties related to a particular user.  Called from User.delete().
	 */
    public static void deleteByUser(User u) {
        storageManager.delete(QUERY_BY_USER, u.getPrimaryKey());
    }
}
