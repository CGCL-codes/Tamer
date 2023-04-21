package org.mindhaus.bdb.browser;

import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindhaus.bdb.browser.entities.MyEntity;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.EntityMetadata;
import com.sleepycat.persist.model.EntityModel;
import com.sleepycat.persist.model.PrimaryKeyMetadata;

public class BaseDPLTest {

    Logger log = Logger.getAnonymousLogger();

    protected File dbHome;

    protected Environment environment;

    protected EntityStore store;

    @BeforeTest()
    public void setUp() throws Exception {
        final EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setAllowCreate(true);
        envConfig.setTxnNoSync(true);
        envConfig.setTxnWriteNoSync(true);
        StoreConfig storeConfig = new StoreConfig();
        dbHome = new File("target/test/db");
        dbHome.mkdirs();
        this.environment = new Environment(this.dbHome, envConfig);
        storeConfig.setAllowCreate(true);
        storeConfig.setTransactional(true);
        store = new EntityStore(environment, "EntityStore", storeConfig);
    }

    @AfterTest
    public void cleanUp() throws Exception {
        store.close();
        environment.close();
    }

    @Test
    public void putEntities() throws Exception {
        PrimaryIndex<Long, MyEntity> entitiesById = store.getPrimaryIndex(Long.class, MyEntity.class);
        for (long i = 0; i < 10; i++) {
            entitiesById.put(new MyEntity("My new entity " + i));
        }
    }

    @Test(dependsOnMethods = "putEntities")
    public void showMetaData() throws Exception {
        EntityModel model = store.getModel();
        Set<String> classNames = model.getKnownClasses();
        for (String className : classNames) {
            showStoredEntities(model, className);
        }
    }

    protected void showStoredEntities(EntityModel model, String classname) throws DatabaseException, ClassNotFoundException {
        EntityMetadata metadata = model.getEntityMetadata(classname);
        if (metadata != null) {
            PrimaryKeyMetadata pkMeta = metadata.getPrimaryKey();
            String pkClassname = pkMeta.getClassName();
            PrimaryIndex entitiesById = store.getPrimaryIndex(Class.forName(pkClassname, true, Thread.currentThread().getContextClassLoader()), Class.forName(classname, true, Thread.currentThread().getContextClassLoader()));
            EntityCursor cursor = entitiesById.entities();
            try {
                for (Object entity : cursor) {
                    log.log(Level.INFO, entity.toString());
                }
            } finally {
                cursor.close();
            }
        }
    }
}
