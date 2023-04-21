package org.macchiato.db.berkleydb;

import java.io.File;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * @author fdietz
 *  
 */
public class MyDBEnv {

    private static final int CACHE_SIZE = 20;

    private Environment myEnv;

    private Database tokenDb;

    private Database classCatalogDb;

    private StoredClassCatalog classCatalog;

    public MyDBEnv() {
    }

    public void setup(File envHome, boolean readOnly) throws DatabaseException {
        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        DatabaseConfig myDbConfig = new DatabaseConfig();
        myEnvConfig.setReadOnly(readOnly);
        myDbConfig.setReadOnly(readOnly);
        myEnvConfig.setAllowCreate(!readOnly);
        myDbConfig.setAllowCreate(!readOnly);
        myEnvConfig.setTransactional(!readOnly);
        myDbConfig.setTransactional(!readOnly);
        myEnvConfig.setCachePercent(CACHE_SIZE);
        myEnv = new Environment(envHome, myEnvConfig);
        tokenDb = myEnv.openDatabase(null, "TokenDB", myDbConfig);
        classCatalogDb = myEnv.openDatabase(null, "ClassCatalogDB", myDbConfig);
        classCatalog = new StoredClassCatalog(classCatalogDb);
    }

    public Environment getEnv() {
        return myEnv;
    }

    public Database getTokenDB() {
        return tokenDb;
    }

    public StoredClassCatalog getClassCatalog() {
        return classCatalog;
    }

    public void close() {
        if (myEnv != null) {
            try {
                tokenDb.close();
                classCatalogDb.close();
                myEnv.close();
            } catch (DatabaseException dbe) {
                System.err.println("Error closing MyDbEnv: " + dbe.toString());
                System.exit(-1);
            }
        }
    }
}
