package org.sourceforge.jemm.database.memory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.sourceforge.jemm.MemoryStore;
import org.sourceforge.jemm.Session;
import org.sourceforge.jemm.database.collections.AbstractDatabaseSetTest;
import org.sourceforge.jemm.database.components.GCMode;

public class MemDbSetTest extends AbstractDatabaseSetTest {

    @BeforeClass
    public static void initialiseSystem() {
        Session.setStore(new MemoryStore(false, GCMode.MANUAL));
    }

    @AfterClass
    public static void shutdownSystem() {
        Session.shutdown();
    }
}
