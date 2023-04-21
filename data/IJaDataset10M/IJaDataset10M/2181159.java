package com.google.appengine.datanucleus.jdo;

import java.util.Map;
import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import com.google.appengine.datanucleus.DatastoreManager;
import com.google.appengine.datanucleus.StorageVersion;
import com.google.appengine.datanucleus.Utils;

/**
 * @author Max Ross <max.ross@gmail.com>
 */
public class JDOStorageVersionTest extends JDOTestCase {

    public void testDefaultStorageVersion() {
        DatastoreManager storeMgr = (DatastoreManager) getExecutionContext().getStoreManager();
        assertEquals(StorageVersion.READ_OWNED_CHILD_KEYS_FROM_PARENTS, storeMgr.getStorageVersion());
    }

    public void testNonDefaultStorageVersion() {
        pm.close();
        pmf.close();
        Map<String, String> props = Utils.newHashMap();
        props.put(StorageVersion.STORAGE_VERSION_PROPERTY, StorageVersion.PARENTS_DO_NOT_REFER_TO_CHILDREN.name());
        pmf = JDOHelper.getPersistenceManagerFactory(props, getPersistenceManagerFactoryName().name());
        pm = pmf.getPersistenceManager();
        DatastoreManager storeMgr = (DatastoreManager) getExecutionContext().getStoreManager();
        assertEquals(StorageVersion.PARENTS_DO_NOT_REFER_TO_CHILDREN, storeMgr.getStorageVersion());
    }

    public void testUnknownStorageVersion() {
        pm.close();
        pmf.close();
        Map<String, String> props = Utils.newHashMap();
        props.put(StorageVersion.STORAGE_VERSION_PROPERTY, "does not exist");
        try {
            pmf = JDOHelper.getPersistenceManagerFactory(props, getPersistenceManagerFactoryName().name());
        } catch (JDOFatalUserException e) {
            assertTrue(e.getCause().getMessage().startsWith("'does not exist'"));
        }
    }
}
