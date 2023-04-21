package ring.test.game;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.exist.xmldb.DatabaseInstanceManager;
import ring.main.RingModule;

public class ExistTest implements RingModule {

    public static void main(String args[]) throws Exception {
        System.setProperty("exist.initdb", "true");
        System.setProperty("exist.home", "/etc/ringmud/");
        Class cl = Class.forName("org.exist.xmldb.DatabaseImpl");
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);
        Collection col = DatabaseManager.getCollection("xmldb:exist:///db", "admin", "");
        String resources[] = col.listResources();
        System.out.println("Resources:");
        for (int i = 0; i < resources.length; i++) {
            System.out.println(resources[i]);
        }
        DatabaseInstanceManager manager = (DatabaseInstanceManager) col.getService("DatabaseInstanceManager", "1.0");
        manager.shutdown();
    }

    public void start(String[] args) {
        try {
            main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
    }
}
