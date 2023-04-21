package org.motiv.tests.store;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.apache.log4j.BasicConfigurator;
import org.motiv.core.Element;
import org.motiv.core.Cache;
import org.motiv.config.CacheConfiguration;
import org.motiv.tests.config.CacheConfigurationFactory;
import org.motiv.store.DiskStore;
import org.motiv.tests.policy.ElementFactory;
import org.motiv.tests.objects.TestObject;

/**
 * A cache disk store tests.
 * @author Pavlov Dm
 */
public class DiskStoreTest {

    /** DiskStore instance*/
    private static DiskStore diskStore;

    /** Cache instance*/
    private static Cache cache;

    @BeforeClass
    public static void setUpBeforeAll() {
        if (diskStore == null) {
            BasicConfigurator.configure();
            CacheConfiguration config = CacheConfigurationFactory.create();
            cache = new Cache(config);
            diskStore = new DiskStore(cache);
        }
    }

    @Test
    public void check_getDataFileName_method() {
        final String EXPECTED_NAME = CacheConfigurationFactory.EXPECTED_NAME + ".data";
        assertEquals("Check data file name", EXPECTED_NAME, diskStore.getDataFileName());
    }

    @Test
    public void check_getIndexFileName_method() {
        final String EXPECTED_NAME = CacheConfigurationFactory.EXPECTED_NAME + ".index";
        assertEquals("Check index file name", EXPECTED_NAME, diskStore.getIndexFileName());
    }

    @Test
    public void check_put_method() {
        final long EXPECTED_OBJECT_ID = 2L;
        final String EXPECTED_OBJECT_NAME = "second";
        diskStore.put(ElementFactory.create(1L, "first"));
        diskStore.put(ElementFactory.create(3L, "third"));
        diskStore.put(ElementFactory.create(2L, "second"));
        Element element = diskStore.get(EXPECTED_OBJECT_ID);
        TestObject obj = (TestObject) element.getObjectValue();
        assertEquals("Check putted element id", EXPECTED_OBJECT_ID, obj.getId());
        assertEquals("Check putted element name", EXPECTED_OBJECT_NAME, obj.getName());
    }

    @Test
    public void check_remove_method() {
        diskStore.put(ElementFactory.create(1L, "first"));
        diskStore.put(ElementFactory.create(3L, "third"));
        diskStore.put(ElementFactory.create(2L, "second"));
        diskStore.remove(2L);
        Element element = diskStore.get(2L);
        assertNull("Check removed element", element);
    }

    @Test
    public void check_containsKey_method() {
        final long EXPECTED_OBJECT_KEY = 3L;
        diskStore.put(ElementFactory.create(1L, "first"));
        diskStore.put(ElementFactory.create(3L, "third"));
        diskStore.put(ElementFactory.create(2L, "second"));
        assertTrue("Check element key", diskStore.containsKey(EXPECTED_OBJECT_KEY));
    }

    @After
    public void tearDown() {
        if (diskStore != null) {
            diskStore.removeAll();
        }
    }

    @AfterClass
    public static void tearDownAfterAll() {
        if (diskStore != null) {
            diskStore.dispose();
            diskStore = null;
        }
        if (cache != null) {
            cache.dispose();
            cache = null;
        }
    }
}
