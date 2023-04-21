package org.colimas.db.mapper;

import java.util.Collections;
import java.util.Map;
import org.colimas.db.DBException;
import org.colimas.services.logs.LogWeb;
import org.colimas.utils.PropertiesException;
import org.colimas.utils.ReadProperties;

/**
 * <h3>SQLQueries</h3>
 * <P>
 * Function:<BR />
 * Structure the sql sentence by given code.
 * the sql.properties must be copied into [installed]/resources/sql/sql.properties
 * @author zhao lei
 * @version 1.5
 * </P>
 * Modification History:
 * <PRE>
 * SEQ DATE        DEVELOPER      DESCRIPTION
 * --- ----------  -------------- -----------------------------
 * 001 2005/12/03  Zhao Lei       INIT
 * 002 2006/01/02  zhao lei       coding
 * 003 2006/01/13  zhao lei       deal with install path and property path
 * 004 2006/02/27  zhao lei       change the install path getter.
 * </PRE>
 */
public class SQLQueries {

    /**
	 * Test environment
	 */
    private static String installPath = null;

    private static String className = "org.colimas.db.mapper.SQLQueries";

    /**
	 * <p>
	 * Not instanciable
	 * </P>
	 */
    private SQLQueries() {
    }

    /**
	 * List of loaded queries
	 * </P>
	 */
    private static Map queries = null;

    /**
	 * <p>
	 * Gets a query
	 * </P>
	 * @param code Key used to retrieve the query
	 */
    public static String getQuery(String code) throws DBException {
        if (queries == null) {
            init();
        }
        String query = (String) queries.get(code);
        if (query != null) {
            return query;
        } else {
            throw new DBException("Cannot find SQL query for code=" + code);
        }
    }

    /**
	 * <p>
	 * replace the {0},...{n} by replace values.
	 * </P>
	 * @param value of replaced
	 * @param Value of repace
	 * @return replaced sentence
	 */
    public static String replaceArgs(String value, String[] replaceValue) {
        if (value == null || replaceValue == null) return value;
        StringBuffer sql = new StringBuffer();
        sql.append(value);
        for (int i = 0; i < replaceValue.length; i++) {
            StringBuffer key = new StringBuffer();
            key = key.append("{").append(i).append("}");
            int pos = sql.indexOf(key.toString());
            if (pos < 0) {
                return sql.toString();
            }
            int start = pos;
            int end = pos + key.length();
            sql = sql.replace(start, end, replaceValue[i]);
        }
        return sql.toString();
    }

    /**
	 * <p>
	 * Loads all queries
	 * </P>
	 * @throws DBxception
	 */
    private static synchronized void init() throws DBException {
        LogWeb log = new LogWeb(className);
        if (queries == null) {
            String path = System.getProperty("installedpath");
            if (path != null && path != "") {
                installPath = path;
                installPath = installPath + "/resources/sql/sql.properties";
            }
            installPath = SQLQueries.class.getResource("/resources/sql/sql.properties").toString();
            ReadProperties properties = new ReadProperties(installPath);
            try {
                queries = Collections.synchronizedMap(properties.getProperties());
                log.debug("init queries successfully");
            } catch (PropertiesException e) {
                log.error("Read sql.properties failed");
            }
        }
    }
}
