package com.ontotext.ordi.mapper.rdbms;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import com.ontotext.ordi.exception.ORDIRuntimeException;
import com.ontotext.ordi.mapper.model.PredicatePattern;
import com.ontotext.ordi.mapper.model.RDFResultSet;
import com.ontotext.ordi.mapper.processor.RDFResultManager;

public abstract class JdbcRDFResultSetBase implements RDFResultSet {

    protected PredicatePattern predicatePattern;

    protected PreparedStatement statement;

    protected ResultSet result;

    protected RDFResultManager manager;

    private static Logger logger = Logger.getLogger(JdbcRDFResultSetBase.class);

    /**
     * 
     * @param statement
     * @param predicatePattern
     * @param manager
     *            may be null if the
     */
    public JdbcRDFResultSetBase(PreparedStatement statement, PredicatePattern predicatePattern, RDFResultManager manager) {
        if (statement == null || predicatePattern == null) {
            throw new IllegalArgumentException();
        }
        this.predicatePattern = predicatePattern;
        this.statement = statement;
        if (manager != null) {
            this.manager = manager;
            this.manager.register(this);
        }
    }

    public void run() {
        RDFResultManager.threadCount++;
        try {
            statement.setFetchSize(10000);
            result = statement.executeQuery();
        } catch (SQLException e) {
            logger.error(String.format("Error while executing %s!", statement));
            logger.error(e);
        } finally {
            if (manager != null) manager.registerComplete(this);
            RDFResultManager.threadCount--;
        }
    }

    public void close() {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException e) {
            throw new ORDIRuntimeException("Error while closing!", e);
        }
    }

    public URI getNamedGraph() {
        return predicatePattern.getNamedGraph();
    }

    public URI getPredicate() {
        return predicatePattern.getPredicate();
    }

    public abstract Resource getSubject();

    public abstract Value getObject();

    public abstract URI[] getTriplesets();

    public abstract boolean next();
}
