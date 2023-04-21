package ocumed.persistenz.dao;

/**
 * @author rudi
 *
 */
public interface DAOUserTransaction {

    /**
     * commit/finish a hibernate transaction
     */
    public void commit();

    /**
     * rollback a hibernate transaction
     */
    public void rollback();

    /**
     * start a new hibernate transaction
     */
    public void start();
}
