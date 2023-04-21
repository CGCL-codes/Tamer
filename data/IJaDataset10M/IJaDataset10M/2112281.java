package org.formaria.data.sql;

import java.sql.Connection;
import org.formaria.pool.PoolManager;

/**
 * <p>A data connection associated with a named set of connection parameters</p>
 * <p>Copyright: Copyright (c) 2003<br>
 * License: see license.txt</p>
 * $Revision: 2.3 $
 * License: see license.txt
 */
public class NamedConnectionObject extends ConnectionObject {

    private String connectionName;

    /**
   * Construct a new named connection
   * @param name the connection name
   * @param conn the connection
   * @param pool the pool manager for this connections
   */
    public NamedConnectionObject(String name, Connection conn, PoolManager pool) {
        super(conn, pool);
        connectionName = name;
    }

    /**
   * Get the name of this connection object
   * @return the connection name
   */
    public String getName() {
        return connectionName;
    }
}
