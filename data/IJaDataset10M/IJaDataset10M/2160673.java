package edu.harvard.iq.safe.saasystem.etl.dao;

import edu.harvard.iq.safe.saasystem.etl.util.sql.DAOFactory;
import javax.sql.*;
import java.sql.*;
import java.util.logging.*;
import javax.naming.*;

/**
 *
 * @author Akio Sone
 */
public class MySQLDAOFactory extends DAOFactory {

    static Logger logger = Logger.getLogger(MySQLDAOFactory.class.getName());

    /**
     *
     * @return
     */
    public LockssBoxDAO getLockssBoxDAO() {
        return new MySQLLockssBoxDAO();
    }

    /**
     * 
     * @return
     */
    public ArchivalUnitStatusDAO getArchivalUnitStatusDAO() {
        return new MySQLArchivalUnitStatusDAO();
    }

    /**
     *
     * @return
     */
    public CrawlStatusDAO getCrawlStatusDAO() {
        return new MySQLCrawlStatusDAO();
    }

    /**
     *
     * @return
     */
    public PollsDAO getPollsDAO() {
        return new MySQLPollsDAO();
    }

    /**
     *
     * @return
     */
    public RepositorySpaceDAO getRepositorySpaceDAO() {
        return new MySQLRepositorySpaceDAO();
    }

    /**
     *
     * @return
     */
    public SuccessfulPollsDAO getSuccessfulPollsDAO() {
        return new MySQLSuccessfulPollsDAO();
    }

    /**
     *
     * @return
     */
    public AuOverviewDAO getAuOverviewDAO() {
        return new MySQLAuOverviewDAO();
    }

    /**
     *
     * @return
     */
    public SuccessfulReplicaIpDAO getSuccessfulReplicaIpDAO() {
        return new MySQLSuccessfulReplicaIpDAO();
    }

    private static DataSource getDataSource() {
        InitialContext ictx = null;
        DataSource ds = null;
        try {
            ictx = new InitialContext();
            ds = (DataSource) ictx.lookup("java:comp/env/jdbc/saasDS");
        } catch (NamingException ex) {
            logger.severe("JNDI NamingException occurred");
            if (ictx != null) {
                try {
                    ictx.close();
                } catch (NamingException e) {
                }
            }
        } catch (Exception e) {
        }
        return ds;
    }
}
