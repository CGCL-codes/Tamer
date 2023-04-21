package net.sourceforge.dbsa.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import net.sourceforge.dbsa.structure.DBFRepository;
import net.sourceforge.dbsa.structure.DBFormat;
import net.sourceforge.dbsa.structure.DBStorage;
import net.sourceforge.dbsa.structure.DBDifference.DBEntityDifference;
import net.sourceforge.dbsa.structure.DBFormat.Schema;

public class DBTrack {

    private static Connection testConnection = null;

    private static DBStorage testStorage;

    private static DBFRepository testTracker;

    private static void connect() throws Exception {
        try {
            Class.forName(System.getProperties().getProperty("dbsa.driver"));
        } catch (ClassNotFoundException e) {
        }
        testConnection = DriverManager.getConnection(System.getProperties().getProperty("dbtrack.url"), System.getProperties().getProperty("dbtrack.user"), System.getProperties().getProperty("dbtrack.password"));
        testStorage = new DBStorage(testConnection, System.getProperties().getProperty("dbtrack.repository.schema"), System.getProperties().getProperty("dbtrack.repository.table"));
        testTracker = new DBFRepository(testStorage);
    }

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        System.getProperties().load(Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties"));
        System.getProperties().load(DBTrack.class.getResourceAsStream("dbt.properties"));
        connect();
        final String sSchemaName = System.getProperties().getProperty("dbtrack.schema");
        Schema sc = DBFormat.readFromDB(System.getProperties().getProperty("dbtrack.catalog"), sSchemaName, testConnection);
        long x = testTracker.addDif(sc);
        if (x > 0) {
            for (DBEntityDifference dif : testTracker.getVersionChanges(sSchemaName, x)) {
                System.out.println(dif.getDescription());
            }
        }
        testTracker.printHistory(sSchemaName);
    }
}
