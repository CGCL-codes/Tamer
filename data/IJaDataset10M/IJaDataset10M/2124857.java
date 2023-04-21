package bexee.dao;

import bexee.core.ProcessContext;

/**
 * The <code>ProcessContextDAO</code> abstracts the underlying data access
 * implementation for <code>ProcessContext</code> objects to enable
 * transparent access to the data source.
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:09 $
 * @author Patric Fornasier
 * @author Pawel Kowalski
 */
public interface ProcessContextDAO {

    /**
     * Inserts a new <code>ProcessContext</code>.
     * 
     * @param ctx
     *            the <code>ProcessContext</code> to insert
     * @return the new id of the <code>ProcessContext</code>
     * @throws DAOException
     *             to indicate that there went something wrong
     */
    public String insert(ProcessContext ctx) throws DAOException;

    /**
     * Finds a <code>ProcessContext</code> given its key.
     * 
     * @param key
     *            the key of the <code>ProcessContext</code> to find
     * @return the <code>ProcessContext</code> or null if no
     *         <code>ProcessContext</code> matches the given key
     * @throws DAOException
     *             to indicate that there went something wrong
     */
    public ProcessContext find(String key) throws DAOException;

    /**
     * Deletes a <code>ProcessContext</code> given its key.
     * 
     * @param key
     *            the id of the <code>ProcessContext</code> to delete
     * @throws DAOException
     *             to indicate that there went something wrong
     */
    public void delete(String id) throws DAOException;

    /**
     * Updates an existing <code>ProcessContext</code>.
     * 
     * @param ctx
     *            the <code>ProcessContext</code> to update
     * @throws DAOException
     *             to indicate that there went something wrong
     */
    public void update(ProcessContext ctx) throws DAOException;
}
