package br.com.tarlis.datatransfer.database;

import br.com.tarlis.datatransfer.connection.FileTransferServlet;
import br.com.tarlis.datatransfer.exception.unchecked.ConnectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author tarlis
 */
public class Factory {

    private static Factory instance = null;

    private static String PU = "DataTransferPersistenceUnit";

    private EntityManagerFactory factory = null;

    private Factory() {
        try {
            factory = Persistence.createEntityManagerFactory(PU);
        } catch (Exception e) {
            ConnectionException ex = new ConnectionException("Exception on EntityManagerFactory creation. " + e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            Logger.getLogger(FileTransferServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public EntityManager getEntityManager() {
        try {
            return factory.createEntityManager();
        } catch (Exception e) {
            ConnectionException ex = new ConnectionException("Exception on EntityManager creation. " + e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            Logger.getLogger(FileTransferServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public static synchronized Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public static void setPU(String persistenceUnit) {
        PU = persistenceUnit;
    }

    public static void close() {
        if (instance.factory != null) {
            if (instance.factory.isOpen()) {
                instance.factory.close();
            }
        }
    }
}
