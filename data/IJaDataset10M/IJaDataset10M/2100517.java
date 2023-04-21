package org.hsqldb.lib.java;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.lang.reflect.Method;

/**
 * Handles the differences between JDK 1.1.x and 1.2.x and above
 * JDBC 2 methods can now be called from JDK 1.1.x - see javadoc comments
 * for org.hsqldb.jdbcXXXX classes.<p>
 * HSQLDB should no longer be compiled with JDK 1.1.x. In verison 1.7.2 and
 * above, an HSQLDB jar compiled with JDK 1.2 or 1.3 can be used with
 * JRE 1.1.X<p>
 *
 * @author fredt@users
 * @version 1.7.2
 */
public class javaSystem {

    private static Method setLogMethod = null;

    static {
        try {
            setLogMethod = java.sql.DriverManager.class.getMethod("setLogWriter", new Class[] { PrintWriter.class });
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }
    }

    public static void setLogToSystem(boolean value) {
        if (setLogMethod == null) {
            PrintStream newOutStream = (value) ? System.out : null;
            DriverManager.setLogStream(newOutStream);
        } else {
            try {
                PrintWriter newPrintWriter = (value) ? new PrintWriter(System.out) : null;
                setLogMethod.invoke(null, new Object[] { newPrintWriter });
            } catch (java.lang.reflect.InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
        }
    }
}
