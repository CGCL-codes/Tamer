package org.mitre.mrald.util;

import javax.sql.rowset.*;

/**
 *  This class is the factory for JDBC 3 RowSet objects. This needs to be abstracted as no concrete classes are provided
 *  by Sun in the Java specification - only interfaces. The methods on this class use reflection to identify
 *
 * @author    jchoyt
 */
public class RowSetFactory {

    private static Class jdbcRowSetClass = null;

    private static Class joinRowSetClass = null;

    private static Class webRowSetClass = null;

    /**
     *  Creates a new instance of a class which implements JdbcRowSet
     *
     * @return                     a JdbcRowSet implementation
     * @exception  MraldException  Throws when it can't find an implementation
     */
    public static JdbcRowSet createJdbcRowSet() throws MraldException {
        try {
            if (jdbcRowSetClass == null) {
                init();
            }
            return (JdbcRowSet) jdbcRowSetClass.newInstance();
        } catch (InstantiationException wfe) {
            throw new MraldException(wfe);
        } catch (IllegalAccessException iae) {
            throw new MraldException(iae);
        }
    }

    /**
     *  Creates a new instance of a class which implements JoinRowSet
     *
     * @return                     a JdbcRowSet implementation
     * @exception  MraldException  Throws when it can't find an implementation
     */
    public static JoinRowSet createJoinRowSet() throws MraldException {
        try {
            if (joinRowSetClass == null) {
                init();
            }
            return (JoinRowSet) joinRowSetClass.newInstance();
        } catch (InstantiationException wfe) {
            throw new MraldException(wfe);
        } catch (IllegalAccessException iae) {
            throw new MraldException(iae);
        }
    }

    /**
     *  Creates a new instance of a class which implements WebRowSet
     *
     * @return                     a JdbcRowSet implementation
     * @exception  MraldException  Throws when it can't find an implementation
     */
    public static WebRowSet createWebRowSet() throws MraldException {
        try {
            if (webRowSetClass == null) {
                init();
            }
            return (WebRowSet) webRowSetClass.newInstance();
        } catch (InstantiationException wfe) {
            throw new MraldException(wfe);
        } catch (IllegalAccessException iae) {
            throw new MraldException(iae);
        }
    }

    /**  Iniitalizes the source classes.  This way we only have to do this once per MRALD run. */
    private static void init() throws MraldException {
        try {
            jdbcRowSetClass = Class.forName(Config.getProperty("JdbcRowSet"));
            joinRowSetClass = Class.forName(Config.getProperty("JoinRowSet"));
            webRowSetClass = Class.forName(Config.getProperty("WebRowSet"));
        } catch (ClassNotFoundException cne) {
            MraldOutFile.appendToFile("**WARNING** JdbcRowSet, JoinRowSet, and/or WebRowSet are not configured properly in standard.properties.  Please fix before you try to use any of the cross DB join facilities or XML output.");
            throw new MraldException(cne);
        }
    }
}
