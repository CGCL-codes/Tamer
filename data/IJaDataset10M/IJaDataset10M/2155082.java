/*
 * Created on 15-Nov-2004 by Peter A. Pilgrim
 */
package com.xenonsoft.bridgetown.aop.transaction.jdbc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * Connection mode enumeration is a concept of the login style
 * borrowed from Expresso Framework. 
 *
 * @author Peter Pilgrim, 15-Nov-2004 19:13:59
 * @version $Id: ConnectionModeEnum.java,v 1.2 2005/02/23 01:27:28 peter_pilgrim Exp $
 */
public class ConnectionModeEnum extends ValuedEnum {


    /**
     * Connect to the database using the standard JDBC API
     * <p>
     * Create the connection with a call to 
     * <code>DriverManager.getConnection(dbURL, login, password)</code>
     *  where login and password are the supplied login name and password
     */
    public final static ConnectionModeEnum      STANDARD = new ConnectionModeEnum("STANDARD", 1);
    
    /**
     * Create the connection with a call to 
     * <code>DriverManager.getConnection(dbURL + "?user=" + login + ";password=" + password)</code>
     *  where login and password are the supplied login and password
     */
    public final static ConnectionModeEnum      DBURL_EMBEDDED_SEMIS = new ConnectionModeEnum("DBURL_EMBEDDED1", 2);
    
    /**
     * Create the connection by setting the login and password into a 
     * <code>java.util.Properties</code> object and 
     * calling <code>DriverManager.getConnection(dbURL, props)</code> 
     * where props is the created properties object
     */
    public final static ConnectionModeEnum      PROPERTIES_EMBEDDED = new ConnectionModeEnum("PROPERTIES_EMBEDDED", 3);
    
    /**
     * Create the connection with a string like 
     * <code>DriverManager.getConnection(dbURL + "?user=" + newLogin + "&password=" + newPassword)</code>
     */
    public final static ConnectionModeEnum      DBURL_EMBEDDED_AMPS = new ConnectionModeEnum("DBURL_EMBEDDED_AMPS", 4);
    

    /**
     * Private Constructor
     * @param arg0
     * @param arg1
     */
    private ConnectionModeEnum(String arg0, int arg1) {
        super(arg0, arg1);
    }

    /**
     * Returns the enumeration that matches the text value 
     * @param textValue
     * @return the enumeration
     */
    public static ConnectionModeEnum getEnum(String textValue) {
        return (ConnectionModeEnum) getEnum(ConnectionModeEnum.class, textValue);
    }

    /**
     * Convert an SQL transaction isolation type into an enumerated type.
     * @param transIso the transaction isolation
     * @return
     */
    public static ConnectionModeEnum getEnum(int transIso ) {
        return (ConnectionModeEnum) getEnum(ConnectionModeEnum.class, transIso );
    }

    /**
     * Return a map collection of registered enumerations for this class type
     * @return the map collection
     */
    public static Map getEnumMap() {
        return getEnumMap(ConnectionModeEnum.class);
    }

    /**
     * Return a list collection of registered enumerations for this class type
     * @return the list collection
     */
    public static List getEnumList() {
        return getEnumList(ConnectionModeEnum.class);
    }

    /**
     * Return an iterator that can traverse the collection of registered 
     * enumerations
     * @return the iterator
     */
    public static Iterator iterator() {
        return iterator(ConnectionModeEnum.class);
    }
}
