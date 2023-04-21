package org.basegen.base.persistence.iterator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.basegen.base.exception.CommunicationException;
import org.basegen.base.business.iterator.AbstractBaseIterator;
import org.basegen.base.business.iterator.BaseIterator;
import org.basegen.base.business.iterator.BaseIteratorProcessor;
import org.basegen.base.business.iterator.BaseIteratorVerifier;
import org.basegen.base.business.iterator.IteratorBaseIterator;
import org.basegen.base.exception.BusinessLogicException;
import org.basegen.base.exception.RepositoryException;

/**
 * BaseIterator implementation using JDBCQuery. This class executes the JDBCQuery.
 */
public class JDBCQueryRunner extends AbstractBaseIterator {

    /**
     * Result set
     */
    private ResultSet rs = null;

    /**
     * Prepared statement
     */
    private PreparedStatement ps = null;

    /**
     * Holds value of property baseIteratorProcessorVerifier.
     */
    private JDBCQuery jdbcQuery;

    /**
     * Getter for property baseIteratorProcessorVerifier.
     * 
     * @return Value of property baseIteratorProcessorVerifier.
     */
    public JDBCQuery getJDBCQuery() {
        return this.jdbcQuery;
    }

    /**
     * Setter for property baseIteratorProcessorVerifier.
     * 
     * @param newJdbcQuery New value of property jdbcQuery.
     */
    public void setJDBCQuery(JDBCQuery newJdbcQuery) {
        this.jdbcQuery = newJdbcQuery;
    }

    /**
     * Sorts the baseIterator
     * 
     * @param iterator iterator
     * @return a sorted BaseIterator
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    private BaseIterator sort(BaseIterator iterator) throws CommunicationException, BusinessLogicException, RepositoryException {
        List list = new ArrayList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Collections.sort(list, (Comparator) getJDBCQuery());
        return new IteratorBaseIterator(list.iterator());
    }

    /**
     * Run jdbc query
     * @param connection connection
     * @return object
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public Object run(Connection connection) throws CommunicationException, BusinessLogicException, RepositoryException {
        try {
            ps = connection.prepareStatement(getJDBCQuery().getSql());
        } catch (SQLException error) {
            throw new RepositoryException(error);
        } catch (RuntimeException error) {
            throw new RepositoryException(error);
        }
        try {
            getJDBCQuery().setParameters(ps);
        } catch (SQLException error) {
            finalize(ps);
            throw new RepositoryException(error);
        } catch (RuntimeException error) {
            finalize(ps);
            throw new RepositoryException(error);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException error) {
            finalize(ps);
            throw new RepositoryException(error);
        } catch (RuntimeException error) {
            finalize(ps);
            throw new RepositoryException(error);
        }
        this.setBaseIteratorVerifiers(new BaseIteratorVerifier[] { getJDBCQuery() });
        this.setBaseIteratorProcessors(new BaseIteratorProcessor[] { getJDBCQuery() });
        JDBCBaseIterator jdbcBaseIterator = new JDBCBaseIterator();
        jdbcBaseIterator.setResultSet(rs);
        jdbcBaseIterator.setPreparedStatement(ps);
        jdbcBaseIterator.setBaseIterator(this);
        BaseIterator result = IteratorBaseIterator.getBaseIterator(jdbcBaseIterator);
        if (getJDBCQuery() instanceof Comparator) {
            result = sort(result);
        }
        return result;
    }

    /**
     * Informs if there is more beans
     * @return boolean
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public boolean hasNext() throws CommunicationException, BusinessLogicException, RepositoryException {
        try {
            boolean result = false;
            if ((rs != null)) {
                boolean next = true;
                boolean verify = false;
                while (next && !verify) {
                    next = rs.next();
                    if (next) {
                        verify = verify(rs);
                    }
                }
                result = next && verify;
            }
            return result;
        } catch (SQLException error) {
            throw new RepositoryException(error);
        }
    }

    /**
     * Returns the next bean
     * @return the type B
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public Object next() throws CommunicationException, BusinessLogicException, RepositoryException {
        return process(rs);
    }

    /**
     * Finalize the prepared statement
     * @param preparedStatement prepared statement
     * @throws RepositoryException repository exception
     */
    public void finalize(PreparedStatement preparedStatement) throws RepositoryException {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException error) {
                throw new RepositoryException(error);
            }
        }
    }

    /**
     * Run the jdbc query 
     * @param jdbcQuery jdbc query
     * @param connection connection
     * @return base iterator
     * @throws CommunicationException communication exception
     * @throws BusinessLogicException business logic exception
     * @throws RepositoryException repository exception
     */
    public static BaseIterator run(JDBCQuery jdbcQuery, Connection connection) throws CommunicationException, BusinessLogicException, RepositoryException {
        JDBCQueryRunner command = new JDBCQueryRunner();
        command.setJDBCQuery(jdbcQuery);
        return (BaseIterator) command.run(connection);
    }
}
