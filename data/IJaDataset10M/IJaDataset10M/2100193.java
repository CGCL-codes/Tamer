package database.subdatabases;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import database.classdatabases.ReleaseClassDatabase;

/**
 * 
 * @author Mirage
 *
 * Class which is used to temporarily hold the stack 
 * of releases which need to be parsed by the parser
 * 
 */
public class ReleaseDatabase {

    public Database _Database = null;

    public Cursor _Cursor = null;

    public ReleaseClassDatabase _ClassDatabase = null;

    public ReleaseDatabase(Environment env) {
        DatabaseConfig DBCONF = new DatabaseConfig();
        DBCONF.setAllowCreate(true);
        DBCONF.setTemporary(true);
        _Database = env.openDatabase(null, "release", DBCONF);
        System.out.println("\t[Database] Temporary Release Database Opened!");
        _ClassDatabase = new ReleaseClassDatabase(env, _Database, false);
    }

    public void close() {
        if (_Cursor != null) {
            _Cursor.close();
            _Cursor = null;
        }
        _Database.close();
        _Database = null;
        System.out.println("\t[Database] Temporary Release Database Closed!");
        _ClassDatabase.close();
    }
}
