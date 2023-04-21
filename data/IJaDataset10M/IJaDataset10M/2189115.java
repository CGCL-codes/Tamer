package net.sf.bbarena.ds;

import junit.framework.TestCase;

public class DataSourceFactoryTest extends TestCase {

    DataSourceFactory dsFactory = null;

    public void setUp() {
        dsFactory = new DataSourceFactory();
    }

    public void testLocalRosterDS() {
        RosterDS localRDS = dsFactory.getRosterDS("LRB5.0", true);
        assertTrue(localRDS != null);
        assertTrue(localRDS.getRosterNames().size() == 1);
        assertTrue(localRDS.getRosterNames().get(0).equals("humans"));
    }
}
