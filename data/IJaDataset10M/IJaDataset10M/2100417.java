package org.objectstyle.cayenne.conn;

import org.objectstyle.cayenne.unit.CayenneTestCase;
import org.objectstyle.cayenne.util.Util;

public class DataSourceInfoTst extends CayenneTestCase {

    private DataSourceInfo dsi;

    public void setUp() throws java.lang.Exception {
        dsi = new DataSourceInfo();
        dsi.setUserName("a");
        dsi.setPassword("b");
        dsi.setMinConnections(1);
        dsi.setMaxConnections(2);
        dsi.setJdbcDriver("b");
        dsi.setDataSourceUrl("c");
        dsi.setAdapterClassName("d");
    }

    public void testDefaultValues() throws java.lang.Exception {
        DataSourceInfo localDsi = new DataSourceInfo();
        assertEquals(1, localDsi.getMinConnections());
        assertTrue(localDsi.getMinConnections() <= localDsi.getMaxConnections());
    }

    public void testClone() throws java.lang.Exception {
        DataSourceInfo dsiClone = dsi.cloneInfo();
        assertEquals(dsi, dsiClone);
        assertTrue(dsi != dsiClone);
    }

    public void testSerialize() throws java.lang.Exception {
        DataSourceInfo dsiUnserialized = (DataSourceInfo) Util.cloneViaSerialization(dsi);
        assertEquals(dsi, dsiUnserialized);
        assertTrue(dsi != dsiUnserialized);
    }
}
