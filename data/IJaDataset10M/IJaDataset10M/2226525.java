package com.jdbwc.core;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * java.sql.Savepoint implementation.
 *
 * @author Tim Gall
 * @version 2010-04-23
 */
public class WCSavepoint implements Savepoint {

    private int savepointId;

    private final String savepointName;

    /**
	 *
	 */
    protected WCSavepoint() {
        generateId();
        savepointName = generateName();
    }

    /**
	 * @param name Name of the savepoint
	 * @throws SQLException
	 */
    protected WCSavepoint(String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new SQLException("Savepoint name must contain a value.");
        }
        generateId();
        savepointName = name;
    }

    /**
	 * @see java.sql.Savepoint#getSavepointId()
	 */
    @Override
    public int getSavepointId() throws SQLException {
        return savepointId;
    }

    /**
	 * @see java.sql.Savepoint#getSavepointName()
	 */
    @Override
    public String getSavepointName() throws SQLException {
        return savepointName;
    }

    private String generateName() {
        return "SP_" + savepointId;
    }

    private void generateId() {
        savepointId = (int) java.lang.System.currentTimeMillis();
    }
}
