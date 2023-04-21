package backend.core.persistent;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class DbEnv {

    private Environment myEnv;

    private DatabaseConfig myDbConfig;

    private Database coDb;

    private Database relDb;

    private Database cvsDb;

    private Database ccsDb;

    private Database attrDb;

    private Database unitDb;

    private Database etsDb;

    private Database rtsDb;

    private Database setDb;

    private Database relEviDb;

    private Database relTypeSetDb;

    private Hashtable<String, Database> coNamesDbs;

    private Hashtable<String, Database> coAccsDbs;

    private Hashtable<String, Database> coGDSDbs;

    private Hashtable<String, Database> relGDSDbs;

    public DbEnv() {
        coNamesDbs = new Hashtable<String, Database>();
        coAccsDbs = new Hashtable<String, Database>();
        coGDSDbs = new Hashtable<String, Database>();
        relGDSDbs = new Hashtable<String, Database>();
    }

    public void setup(File envHome, boolean readOnly) throws DatabaseException {
        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        myDbConfig = new DatabaseConfig();
        DatabaseConfig dupDbConfig = new DatabaseConfig();
        myEnvConfig.setReadOnly(readOnly);
        myDbConfig.setReadOnly(readOnly);
        dupDbConfig.setReadOnly(readOnly);
        myEnvConfig.setAllowCreate(!readOnly);
        myDbConfig.setAllowCreate(!readOnly);
        dupDbConfig.setAllowCreate(!readOnly);
        myEnvConfig.setTransactional(!readOnly);
        myDbConfig.setTransactional(!readOnly);
        dupDbConfig.setTransactional(!readOnly);
        myEnv = new Environment(envHome, myEnvConfig);
        coDb = myEnv.openDatabase(null, "ConceptDB", myDbConfig);
        relDb = myEnv.openDatabase(null, "RelationDB", myDbConfig);
        cvsDb = myEnv.openDatabase(null, "CVsDB", myDbConfig);
        ccsDb = myEnv.openDatabase(null, "CCsDB", myDbConfig);
        attrDb = myEnv.openDatabase(null, "AttrDB", myDbConfig);
        unitDb = myEnv.openDatabase(null, "UnitDB", myDbConfig);
        etsDb = myEnv.openDatabase(null, "ETsDB", myDbConfig);
        rtsDb = myEnv.openDatabase(null, "RTsDB", myDbConfig);
        setDb = myEnv.openDatabase(null, "SetDB", myDbConfig);
        dupDbConfig.setSortedDuplicates(true);
        relEviDb = myEnv.openDatabase(null, "RelEviDB", dupDbConfig);
        relTypeSetDb = myEnv.openDatabase(null, "RelTypeSetDB", dupDbConfig);
    }

    public Environment getEnv() {
        return myEnv;
    }

    public Database getCoDB() {
        return coDb;
    }

    public Database getRelDB() {
        return relDb;
    }

    public Database getCVsDB() {
        return cvsDb;
    }

    public Database getCCsDB() {
        return ccsDb;
    }

    public Database getAttrDB() {
        return attrDb;
    }

    public Database getUnitDB() {
        return unitDb;
    }

    public Database getETsDB() {
        return etsDb;
    }

    public Database getRTsDB() {
        return rtsDb;
    }

    public Database getSetDB() {
        return setDb;
    }

    public Database getRelEviDB() {
        return relEviDb;
    }

    public Database getRelTypeSetDB() {
        return relTypeSetDb;
    }

    public Database getCoNamesDB(String id) {
        if (coNamesDbs.get(id) == null) {
            try {
                Database db = myEnv.openDatabase(null, "CoNamesFor" + id, myDbConfig);
                coNamesDbs.put(id, db);
            } catch (DatabaseException de) {
                de.printStackTrace();
            }
        }
        return coNamesDbs.get(id);
    }

    public Database getCoAccsDB(String id) {
        if (coAccsDbs.get(id) == null) {
            try {
                Database db = myEnv.openDatabase(null, "CoAccsFor" + id, myDbConfig);
                coAccsDbs.put(id, db);
            } catch (DatabaseException de) {
                de.printStackTrace();
            }
        }
        return coAccsDbs.get(id);
    }

    public Database getCoGDSDB(String id) {
        if (coGDSDbs.get(id) == null) {
            try {
                Database db = myEnv.openDatabase(null, "CoGDSFor" + id, myDbConfig);
                coGDSDbs.put(id, db);
            } catch (DatabaseException de) {
                de.printStackTrace();
            }
        }
        return coGDSDbs.get(id);
    }

    public Database getRelGDSDB(String id) {
        if (relGDSDbs.get(id) == null) {
            try {
                Database db = myEnv.openDatabase(null, "RelGDSFor" + id, myDbConfig);
                relGDSDbs.put(id, db);
            } catch (DatabaseException de) {
                de.printStackTrace();
            }
        }
        return relGDSDbs.get(id);
    }

    public void close() {
        if (myEnv != null) {
            try {
                coDb.close();
                relDb.close();
                cvsDb.close();
                ccsDb.close();
                attrDb.close();
                unitDb.close();
                etsDb.close();
                rtsDb.close();
                setDb.close();
                relEviDb.close();
                relTypeSetDb.close();
                Iterator<Database> it = coNamesDbs.values().iterator();
                while (it.hasNext()) {
                    it.next().close();
                }
                it = coAccsDbs.values().iterator();
                while (it.hasNext()) {
                    it.next().close();
                }
                it = coGDSDbs.values().iterator();
                while (it.hasNext()) {
                    it.next().close();
                }
                it = relGDSDbs.values().iterator();
                while (it.hasNext()) {
                    it.next().close();
                }
                myEnv.close();
            } catch (DatabaseException dbe) {
                System.err.println("Error closing DbEnv: " + dbe.toString());
            }
        }
    }
}
