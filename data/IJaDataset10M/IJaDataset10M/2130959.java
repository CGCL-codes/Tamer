package uk.org.ogsadai.converters.resultset;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Problem arising when trying to convert a 
 * <code>java.sql.ResultSet</code> to another format.
 * <p>
 * Associated with error ID:
 * <code>uk.org.ogsadai.RESULTSET_HANDLER_ERROR</code>.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ResultSetHandlerException extends DAIException {

    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007.";

    /**
     * Constructor.
     * 
     * @param e
     *     Cause of the problem.
     */
    public ResultSetHandlerException(Throwable e) {
        super(ErrorID.RESULTSET_HANDLER_ERROR);
        super.initCause(e);
    }
}
