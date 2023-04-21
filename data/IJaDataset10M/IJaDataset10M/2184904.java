package com.cti.product.test;

import java.util.*;
import junit.framework.*;
import org.jlf.dataMap.*;
import org.jlf.dataMap.jdbcMap.*;
import com.cti.product.*;

/**
 * This class tests the DBSchema object delete method from the product
 * package.
 *
 * @version: $Revision: 1.1 $
 * @author:  Todd Lauinger
 *
 * @see com.cti.product.DBSchema
 */
public class TestDBSchemaDelete extends TestCase {

    /**
     * Default constructor so the parameterized constructor can
     * be passed through to superclass.
     */
    public TestDBSchemaDelete(String name) {
        super(name);
    }

    /**
     * Test the create method.
     */
    public void testDelete() {
        DBSchema dbSchema = new DBSchema();
        JDBCDataMapper dataMapper = null;
        try {
            dataMapper = (JDBCDataMapper) dbSchema.getDefaultDataMapper();
            dbSchema.cleanupAfterWrite(dataMapper);
            dbSchema.deleteOnWrite();
            dbSchema.write(dataMapper);
            dataMapper.commitWrites();
        } finally {
            if (dataMapper != null) dataMapper.close();
        }
    }
}
