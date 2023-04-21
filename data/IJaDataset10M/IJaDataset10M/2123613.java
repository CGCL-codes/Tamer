package org.mili.test;

import java.util.*;

/**
 * This test factory provides methods to instantiate object for test only.
 *
 * @author Michael Lieshoff
 */
public class DbInfoFactory {

    /**
     * Creates new database info properties.
     *
     * @return the database info properties.
     */
    static Properties create() {
        Properties p = new Properties();
        p.setProperty("driver", "org.hsqldb.jdbcDriver");
        p.setProperty("url", "jdbc:hsqldb:file:" + TestUtils.DB_FOLDER + "/" + "xyz");
        p.setProperty("user", "sa");
        p.setProperty("password", "");
        return p;
    }

    /**
     * Creates new defect database info properties.
     *
     * @return the defect database info properties.
     */
    static Properties createDefect() {
        Properties p = new Properties();
        p.setProperty("driver", "a.b.C");
        p.setProperty("url", "jdbc:hsqldb:file:" + TestUtils.DB_FOLDER + "/" + "xyz");
        p.setProperty("user", "sa");
        p.setProperty("password", "");
        return p;
    }
}
