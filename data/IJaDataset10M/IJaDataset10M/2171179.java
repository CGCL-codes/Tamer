package org.openrdf.sail.rdbms.exceptions;

import java.sql.SQLException;
import org.openrdf.query.QueryEvaluationException;

/**
 * Wraps a {@link SQLException} or {@link RdbmsException}.
 * 
 * @author James Leigh
 * 
 */
public class RdbmsQueryEvaluationException extends QueryEvaluationException {

    private static final long serialVersionUID = 8026425066876739868L;

    public RdbmsQueryEvaluationException(SQLException e) {
        super(e);
    }

    public RdbmsQueryEvaluationException(RdbmsException e) {
        super(e);
    }

    public RdbmsQueryEvaluationException(String msg, SQLException e) {
        super(msg, e);
    }
}
