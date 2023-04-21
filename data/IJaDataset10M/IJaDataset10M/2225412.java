package net.sourceforge.myjorganizer.jpa;

import java.sql.SQLException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * <p>JPAUtil class.</p>
 *
 * @author Davide Bellettini <dbellettini@users.sourceforge.net>
 * @version $Id: JPAUtil.java 111 2010-10-28 12:24:09Z dbellettini $
 */
public class JPAUtil {

    private static DatabaseLauncher databaseLauncher;

    static {
        databaseLauncher = new DummyDatabaseLauncher();
    }

    /**
     * <p>createEntityManagerFactory</p>
     *
     * @return a {@link javax.persistence.EntityManagerFactory} object.
     */
    public static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("myjorganizer");
    }

    /**
     * <p>startServers</p>
     *
     * @throws java.sql.SQLException if any.
     */
    public static void startServers() throws SQLException {
        databaseLauncher.start();
    }

    /**
     * <p>shutdownServers</p>
     */
    public static void shutdownServers() {
        databaseLauncher.stop();
    }
}
