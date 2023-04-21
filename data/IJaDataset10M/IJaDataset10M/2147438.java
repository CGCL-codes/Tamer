package uk.org.ogsadai.resource.dataresource.jdbc;

import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.dataresource.DataResourceUseException;

/**
 * A problem was encountered when using an JDBC connection.
 * <p>
 * Associated with error code: 
 * <code>uk.org.ogsadai.JDBC_CONNECTION_USE_ERROR</code>.
 * 
 * @author The OGSA-DAI Project Team
 */
public class JDBCConnectionUseException extends DataResourceUseException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007.";

    /**
     * Constructor.
     *
     * @param connection
     *     Connection URI.
     * @param e
     *     Exception that caused the problem.
     */
    public JDBCConnectionUseException(String connection, Throwable e) {
        super(ErrorID.JDBC_CONNECTION_USE_ERROR, new Object[] { connection });
        super.initCause(e);
    }
}
