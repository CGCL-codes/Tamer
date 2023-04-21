package orm;

import org.orm.*;
import org.orm.cfg.JDBCConnectionSetting;
import org.hibernate.*;
import java.util.Properties;

public class PINPASSPersistentManager extends PersistentManager {

    private static final String PROJECT_NAME = "PINPASS";

    private static PersistentManager _instance = null;

    private static SessionType _sessionType = SessionType.THREAD_BASE;

    private static int _timeToAlive = 60000;

    private static JDBCConnectionSetting _connectionSetting = null;

    private static Properties _extraProperties = null;

    private PINPASSPersistentManager() throws PersistentException {
        super(_connectionSetting, _sessionType, _timeToAlive, new String[] {}, _extraProperties);
        setFlushMode(FlushMode.AUTO);
    }

    public String getProjectName() {
        return PROJECT_NAME;
    }

    public static final synchronized PersistentManager instance() throws PersistentException {
        if (_instance == null) {
            _instance = new PINPASSPersistentManager();
        }
        return _instance;
    }

    public void disposePersistentManager() throws PersistentException {
        _instance = null;
        super.disposePersistentManager();
    }

    public static void setSessionType(SessionType sessionType) throws PersistentException {
        if (_instance != null) {
            throw new PersistentException("Cannot set session type after create PersistentManager instance");
        } else {
            _sessionType = sessionType;
        }
    }

    public static void setAppBaseSessionTimeToAlive(int timeInMs) throws PersistentException {
        if (_instance != null) {
            throw new PersistentException("Cannot set session time to alive after create PersistentManager instance");
        } else {
            _timeToAlive = timeInMs;
        }
    }

    public static void setJDBCConnectionSetting(JDBCConnectionSetting aConnectionSetting) throws PersistentException {
        if (_instance != null) {
            throw new PersistentException("Cannot set connection setting after create PersistentManager instance");
        } else {
            _connectionSetting = aConnectionSetting;
        }
    }

    public static void setHibernateProperties(Properties aProperties) throws PersistentException {
        if (_instance != null) {
            throw new PersistentException("Cannot set hibernate properties after create PersistentManager instance");
        } else {
            _extraProperties = aProperties;
        }
    }

    public static void saveJDBCConnectionSetting() {
        PersistentManager.saveJDBCConnectionSetting(PROJECT_NAME, _connectionSetting);
    }
}
