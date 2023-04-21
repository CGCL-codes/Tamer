package org.caleigo.core;

import org.caleigo.security.*;

/**
 * @author Klas Ehnrot
 *
 * Dynamically adds the user properties to 
 */
public class UserProxyData implements IProxyData {

    private String propertyName;

    /**
     * Parses the incoming value and converts it into a UserProxyData.
     * Expecting a string formatted as : 
     * #USER.propertyName   where #USER. is the static directive and
     * propertyName is the name of the property to lookup in UserConfig.
     *    
     * @param value the data
     * @return UserProxyData
     */
    public static UserProxyData parse(String value) {
        if (value.startsWith("#USER.") && value.indexOf('#', 1) != -1) {
            String propertyName = value.substring(6, value.indexOf('#', 1)).trim();
            return new UserProxyData(propertyName);
        }
        return null;
    }

    /**
       * Used to convert an object to a Date Object, either the java.util.Date object or 
       * a DateProxyData object.
       * 
       * @param data The data representing a Date
       * @return Object The created data object
       */
    public static Object convertToStringObject(Object data) {
        if (data instanceof UserProxyData) return data; else if (data != null) {
            data = data.toString();
            UserProxyData userProxyData = UserProxyData.parse((String) data);
            if (userProxyData != null) {
                return userProxyData;
            }
            return data;
        } else return null;
    }

    public UserProxyData(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Implementation of the IProxy interface getData
     * Goes through UserInfo and lookups the propertyName
     * 
     * @see org.caleigo.core.IProxyData#getData()
     */
    public Object getData() {
        UserInfo userInfo = null;
        try {
            userInfo = UserInfo.getCurrentUserInfo();
        } catch (LoginCanceledException e) {
            return "#USER." + propertyName + "#";
        } catch (IllegalStateException e) {
            return "#USER." + propertyName + "#";
        }
        if (propertyName.toLowerCase().equals("userid")) {
            return userInfo.getUserID();
        }
        Object data = userInfo.getProperty(this.propertyName);
        if (data != null) {
            return data.toString();
        }
        data = userInfo.getProperty("entity");
        if (data != null) {
            IEntity entity = (IEntity) data;
            data = entity.getData(this.propertyName);
            if (data != null) return data.toString();
        }
        return "#USER." + propertyName + "#";
    }

    /** 
     * Implementation of the IProxy Interface getDataType
     * 
     * @return Returns a DataType.STRING
     * @see org.caleigo.core.IProxyData#getDataType()
     */
    public DataType getDataType() {
        return DataType.STRING;
    }
}
