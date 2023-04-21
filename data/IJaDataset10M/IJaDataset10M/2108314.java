package atg.metier.dao.jdo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import atg.metier.dao.jdbc.exception.ATGDaoBaseIndisponibleException;
import atg.service.constante.AtgConstantes;
import atg.service.log.AtgLogManager;
import atg.util.service.ATGBasicClass;

/**
 * @author Administrateur
 */
public class ATGDaoJdoPoolDataSource extends ATGBasicClass {

    /**
   * R�f�rence du pool par d�faut
   */
    public static final String CSTE_DEFAULT_REFERENCE = "Reference par defaut";

    /**
   * Instances uniques (pour une r�f�rence donn�e) du pool de connections
   */
    private static java.util.Hashtable instances = new java.util.Hashtable();

    /**
   * nombre de connections maximum cr�es sur la base de donn�es
   */
    private static final int MAX_CONNECTION = 10;

    /**
   * nombre de connections automatiquement cr�es au d�marrage du pool
   */
    private static final int START_CONNECTION = 1;

    /**
   * Delai d'attente pour l'obtention d'une connection, exemple 3000 = 3
   * secondes
   */
    private static final int MAX_WAIT = 3000;

    /**
   * delai au dela duquel une connexion est consideree comme perdue, exemple
   * 60000 = 60 secondes
   */
    private static final int MAX_CTRL_TIME = 60000;

    /**
   * liste des connections cr��es mais non utilis�es
   */
    private java.util.Vector freeConnections = null;

    /**
   * nombre de connections en cours (demand�es mais pas encore lib�r�es)
   */
    private int checkedOut = 0;

    /**
   * r�f�rence du pool de donn�es
   */
    protected String reference = "";

    /**
   * ensemble des connections creees sur la base de donnees
   */
    protected java.util.Hashtable ctrlConnexion = null;

    protected static java.util.logging.Logger logger_ = null;

    private PersistenceManagerFactory pmf = null;

    /**
   * Ecriture des logs
   */
    protected java.util.logging.Logger getLogger() {
        if (logger_ == null) logger_ = AtgLogManager.getLog(AtgConstantes.ATG_LOG_CATEGORY_METIER_DAO_JDO);
        return logger_;
    }

    private ATGDaoJdoPoolDataSource(String reference) {
        String properties = atg.service.constante.AtgConstantesWF.getValue("PATH_JDO_FILE");
        Properties props = new Properties();
        String nomFichier = properties + reference + "_JDO.properties";
        try {
            props.load(new FileInputStream(nomFichier));
            pmf = JDOHelper.getPersistenceManagerFactory(props);
        } catch (FileNotFoundException e) {
            this.logSevere("Immpossible de trouver le fichier de properties pour le serveur JDO : " + nomFichier);
        } catch (IOException e) {
            this.logSevere("Erreur d'entr�e/sortie dans le fichier de properties : " + nomFichier);
        }
    }

    /**
   * Renvoie une connection au serveur de persistance.
   * 
   * @return PersistenceManager Ref�rence vers le gestionnaire de persistance
   *         Jdo
   */
    public PersistenceManager getPersistenceManager() throws ATGDaoBaseIndisponibleException {
        try {
            PersistenceManager conn = getPersistenceManager(MAX_WAIT);
            return conn;
        } catch (ATGDaoBaseIndisponibleException exception) {
            logSevere("Impossible de fournir une connection correcte : " + exception.getCause() + " / " + exception.getMessage());
            throw new ATGDaoBaseIndisponibleException();
        }
    }

    /**
   * Renvoie une connection � la base de donn�es. Attend au maximum 'timeout'
   * millisecondes l'obtention de la connection.
   * 
   * @param timeout
   *          Temps d'attente maximum de la connection
   * @throws java.sql.SQLException
   *           Exception SQL
   * @return java.sql.Connection Connection � la base de donn�es
   */
    protected synchronized PersistenceManager getPersistenceManager(long timeout) throws ATGDaoBaseIndisponibleException {
        long startTime = System.currentTimeMillis();
        long remaining = timeout;
        PersistenceManager conn = null;
        while ((conn = getPooledPersistenceManager()) == null) {
            try {
                logFinest("Attente d'une connection (" + timeout + ").");
                wait(timeout);
            } catch (InterruptedException exception) {
            }
            remaining = timeout - (System.currentTimeMillis() - startTime);
            if (remaining <= 0) {
                throw new ATGDaoBaseIndisponibleException("getPersistenceManager() time-out");
            }
        }
        if (!isConnectionOk(conn)) {
            return getPersistenceManager(remaining);
        }
        checkedOut++;
        return conn;
    }

    /**
   * Renvoie une connection.
   * 
   * @throws ATGDaoBaseIndisponibleException
   * @return PersistenceManager Connection au gestionnaire de persistance
   */
    protected PersistenceManager getPooledPersistenceManager() throws ATGDaoBaseIndisponibleException {
        PersistenceManager conn = null;
        logFinest("Une connection est demandee au pool.");
        if (freeConnections.size() > 0) {
            conn = (PersistenceManager) freeConnections.firstElement();
            freeConnections.removeElementAt(0);
        } else {
            if (checkedOut == MAX_CONNECTION) {
                logWarning("Attention, le nombre maximum de connexions creees sur le serveur de persitance est atteint : " + MAX_CONNECTION);
                ckeckCtrlConnexion();
            }
            if (checkedOut < MAX_CONNECTION) conn = newConnection();
        }
        return conn;
    }

    /**
   * V�rifie si toutes les connexions ont bien �t� rendues au pool.
   */
    public void ckeckCtrlConnexion() {
        java.util.Enumeration conns = ctrlConnexion.keys();
        do {
            Object conn = conns.nextElement();
            long delta = System.currentTimeMillis() - ((Long) (ctrlConnexion.get(conn))).longValue();
            if ((!freeConnections.contains(conn)) && (delta > MAX_CTRL_TIME)) {
                ctrlConnexion.remove(conn);
                logSevere("La connexion " + conn + " est consideree comme perdue : elle n'a pas ete rendue au pool.");
                checkedOut--;
                ((PersistenceManager) conn).close();
            }
        } while (conns.hasMoreElements());
    }

    /**
   * Cr�ation d'une nouvelle connection.
   * 
   * @throws ATGDaoBaseIndisponibleException
   * @return PersistenceManager Nouvelle connection � la base de donn�es
   */
    protected PersistenceManager newConnection() throws ATGDaoBaseIndisponibleException {
        this.logFinest("Demande d'une ref�rence sur le Serveur de persistance");
        PersistenceManager pm = (PersistenceManager) this.pmf.getPersistenceManager();
        if (pm != null) {
            logFinest("  getPersistenceManager()");
        } else {
            logSevere("Impossible de r�cup�rer une instance de persistance manager");
        }
        return pm;
    }

    /**
   * Contr�le si la connection specifi�e est valide.
   * 
   * @param conn
   *          Connection � tester
   * @return boolean Connection valide ou non
   */
    protected boolean isConnectionOk(PersistenceManager conn) {
        if (!conn.isClosed()) {
            conn.refreshAll();
            return true;
        } else return false;
    }

    /**
   * Renvoie l'instance unique du pool de connections pour la r�f�rence donn�e.
   * 
   * @param reference
   *          R�f�rence du pool de connection
   * @return StdPoolDataSource Instance d'un pool d'acc�s aux donn�es
   */
    public static ATGDaoJdoPoolDataSource getInstance(String reference) {
        if ((instances.get(reference) != null)) {
            return (ATGDaoJdoPoolDataSource) (instances.get(reference));
        } else {
            ATGDaoJdoPoolDataSource instance = new ATGDaoJdoPoolDataSource(reference);
            instance.initPool();
            instances.put(reference, instance);
            return instance;
        }
    }

    /**
   * Initialisation du pool. Appel� lors de l'instanciation.
   */
    protected void initPool() {
        freeConnections = new java.util.Vector();
        for (int i = 0; i < START_CONNECTION; i++) {
            try {
                freeConnections.addElement(newConnection());
            } catch (ATGDaoBaseIndisponibleException exception) {
                logSevere("Impossible d'initialiser le pool de Connection Jdo!" + exception.getCause() + " / " + exception.getMessage());
            }
        }
    }

    /**
   * Lib�re la connection sp�cifi�e.
   * @param conn Connection � lib�rer
   */
    public synchronized void release(PersistenceManager conn) {
        logFinest("Un persistance manager est liberee : " + (freeConnections.size() + 1) + " persistance manager sont libres.");
        freeConnections.addElement(conn);
        checkedOut--;
        notifyAll();
    }
}
