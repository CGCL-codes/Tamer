package org.tmapiutils.query.tolog.memory;

import org.tmapiutils.query.tolog.TologResultsSet;
import org.tmapiutils.query.tolog.PreparedQuery;
import org.tmapiutils.query.tolog.TologProcessingException;
import org.tmapiutils.query.tolog.parser.TologQuery;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class PreparedQueryImpl implements PreparedQuery {

    private TologQuery m_query;

    public PreparedQueryImpl(TologQuery q) {
        m_query = q;
        rewrite();
    }

    /**
     * Rewrite the query for optimisation purposes
     */
    private void rewrite() {
    }

    public String getQueryString() {
        return m_query.toString();
    }

    public TologResultsSet execute() throws TologProcessingException {
        return m_query.execute();
    }

    public TologResultsSet execute(Object[] params) throws TologProcessingException {
        return m_query.execute(params);
    }
}
