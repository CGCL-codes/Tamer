package com.explosion.expfmodules.rdbmsconn.dbom.utils;

import java.util.List;
import com.explosion.datastream.exql.AbstractDB;
import com.explosion.datastream.exql.EnvironmentHelper;
import com.explosion.expfmodules.rdbmsconn.dbom.DBEntity;
import com.explosion.expfmodules.rdbmsconn.dbom.utils.MetadataUtils;

/**
 * @author Stephen Cowx
 * Created on 10-Jul-2004
 */
public class MetadataUtilsTest extends AbstractDB {

    private EnvironmentHelper helper = new EnvironmentHelper();

    /**
     * @param arg0
     * @throws Exception
     */
    public MetadataUtilsTest(String arg0) throws Exception {
        super(arg0);
    }

    /**
    * Tests the getBestIdentifier method
    * @throws Exception
    */
    public void testGetDerivedIdentityColumns() throws Exception {
        MetadataUtils utils = new MetadataUtils();
        DBEntity entity = helper.getDBEntity("BASICTABLE", schema, catalog, conn);
        List list = entity.getDerivedIdentityColumns();
        assertEquals(1, list.size());
    }

    public void testGetFullEntityName() throws Exception {
        MetadataUtils utils = new MetadataUtils();
        DBEntity entity = helper.getDBEntity("BASICTABLE", schema, catalog, conn);
        String columnName = utils.getFullEntityName(entity, conn.getMetaData());
        assertEquals(columnName, schema + ".BASICTABLE");
    }

    public void testGetFullColumnName() throws Exception {
        MetadataUtils utils = new MetadataUtils();
        String columnName = utils.getFullColumnName("COL_ONE", null, conn.getMetaData());
        assertEquals(columnName, "COL_ONE");
    }
}
