package jhomenet.server.db;

import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import jhomenet.commons.Library;
import jhomenet.commons.AbstractLibraryDependency;
import jhomenet.commons.Application;
import jhomenet.commons.JHomenetException;
import jhomenet.commons.event.EventCategory;
import jhomenet.commons.event.EventLogger;
import jhomenet.commons.persistence.PersistenceException;
import jhomenet.server.ServerDatabaseVersion;
import jhomenet.server.persistence.DatabaseVersionPersistenceFacade;
import jhomenet.server.persistence.hibernate.HibernateUtil;

/**
 * TODO: Class description.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public final class DbUtil {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(DbUtil.class.getName());

    /**
	 * 
	 */
    private static final String dbVersionString = "0.1.9";

    /**
	 * 
	 */
    private static ServerDatabaseVersion dbVersion;

    /**
	 * 
	 */
    private static final String requiredVersionString = "0.1.9+";

    /**
	 * 
	 */
    private static final Library.LibraryDependency databaseDependency = AbstractLibraryDependency.valueOf(requiredVersionString);

    static {
        dbVersion = ServerDatabaseVersion.valueOf(dbVersionString);
    }

    /**
	 * 
	 */
    private DbUtil() {
    }

    /**
	 * Initialize the database.
	 * 
	 * @throws JHomenetException
	 */
    public static final void initDb(Application server) throws JHomenetException {
        logger.debug("Performing database initialization...");
        try {
            if (HibernateUtil.createDatabase()) {
                HibernateUtil.buildDatabase(dbVersion);
            } else {
                ServerDatabaseVersion databaseVersion = DatabaseVersionPersistenceFacade.getInstance().getDatabaseVersion();
                if (databaseVersion == null || !databaseDependency.checkDependency(databaseVersion.getDatabaseVersion())) {
                    logger.info("Incompatible database version!");
                    EventLogger.addNewWarningEvent(DbUtil.class.getName(), "Incompatible database version", EventCategory.SYSTEM);
                    int confirm = JOptionPane.showConfirmDialog(null, "<html>The database version is incompatible. Do you want to exit the<br>application and backup the database before proceeding?</html>", "Database incompatibility", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        server.forceApplicationShutdown();
                    } else {
                        HibernateUtil.buildDatabase(dbVersion);
                    }
                } else {
                    logger.debug("Compatible database version.");
                }
            }
        } catch (PersistenceException pe) {
            logger.error("Error while initializing database: " + pe.getMessage(), pe);
            throw new JHomenetException(pe);
        }
        logger.debug("Database creation execution complete");
    }
}
