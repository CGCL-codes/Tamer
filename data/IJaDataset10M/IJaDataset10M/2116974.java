package backend.parser.kegg2.util;

import java.io.File;
import java.io.IOException;
import backend.core.AbstractONDEXGraph;
import backend.core.persistent.berkeley.BerkeleyEnv;
import backend.core.security.Session;
import backend.tools.DirUtils;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

public class BerkleyLocalEnvironment {

    private Environment env;

    private EntityStore store;

    private File f;

    public BerkleyLocalEnvironment(Session s, AbstractONDEXGraph og) {
        String ondexDir = System.getProperties().getProperty("ondex.dir");
        f = new File(ondexDir + File.separator + "dbs" + File.separator + "KEGG_TEMP_STORE_" + og.getName(s) + System.currentTimeMillis());
        try {
            DirUtils.deleteTree(f);
            if (!f.mkdirs()) {
                System.err.println("No permission to write to directory " + f.getAbsolutePath());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            envConfig.setTransactional(true);
            envConfig.setCacheSize(BerkeleyEnv.DEFAULT_CACHSIZE);
            env = new Environment(f, envConfig);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        try {
            StoreConfig storeConfig = new StoreConfig();
            storeConfig.setAllowCreate(true);
            storeConfig.setTransactional(false);
            storeConfig.setDeferredWrite(true);
            store = new EntityStore(env, "keggStore", storeConfig);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public Environment getEnv() {
        return env;
    }

    public EntityStore getStore() {
        return store;
    }

    @Override
    public void finalize() {
        try {
            store.close();
            env.close();
            try {
                DirUtils.deleteTree(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
