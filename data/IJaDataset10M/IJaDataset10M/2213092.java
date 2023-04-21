package com.avaje.ebeaninternal.server.lib.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Methods for the ExtendedPreparedStatement.
 *
 * This interfaced is used to compose the ExtendedPreparedStatement and for the decorator to easily cache
 * these specific methods and fast {@link MethodHandler} lookup.
 */
public interface ExtendedPreparedStatementMethods {

    public PreparedStatement getDelegate();

    /**
	 * Return the key used to cache this on the Connection.
	 */
    public String getCacheKey();

    /**
	 * Return the SQL used to create this PreparedStatement.
	 */
    public String getSql();

    /**
	 * Fully close the underlying PreparedStatement. After this we can no longer
	 * reuse the PreparedStatement.
	 */
    public void closeDestroy() throws SQLException;
}
