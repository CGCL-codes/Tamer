package com.googlecode.openmpis.util;

import java.io.Reader;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * The SqlMapConfig class is a convenience class that instantiates a long-lived,
 * thread safe service object called SqlMapClient.
 * 
 * @author  <a href="mailto:rvbabilonia@gmail.com">Rey Vincent Babilonia</a>
 * @version 1.0
 */
public class SqlMapConfig {

    /**
     * The SQL map
     */
    private static final SqlMapClient sqlMap;

    /**
     * Creates the SQL map.
     */
    static {
        try {
            String resource = "com/googlecode/openmpis/persistence/ibatis/sqlmapconfig.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing SqlMapConfig class. Cause: " + e);
        }
    }

    /**
     * Returns the SQL map.
     * 
     * @return      the SQL map
     */
    public static SqlMapClient getSqlMapInstance() {
        return sqlMap;
    }
}
