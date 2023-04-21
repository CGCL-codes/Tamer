package com.topcoder.jdbc.resultset;

import java.sql.SQLException;
import junit.framework.TestCase;

/**
 * <p>
 * Tests OperationAfterClosedSQLException class.
 * </p>
 * 
 * @author csbird
 * @version 1.0
 */
public class OperationAfterClosedSQLExceptionTest extends TestCase {

    /**
	 * Tests constructor with error message and the properties can be retrieved correctly later.
	 * 
	 */
    public void testOperationAfterClosedSQLException() {
        SQLException e = new OperationAfterClosedSQLException("message");
        assertNotNull("Create instance failed.", e);
        assertTrue("The error message mismatches.", e.getMessage().indexOf("message") >= 0);
        assertEquals("The sqlstate mismatches.", "HYC00", e.getSQLState());
    }
}
