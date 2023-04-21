package org.datanucleus.tests;

import java.sql.SQLException;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.jdo.spatial.SpatialHelper;
import org.datanucleus.tests.JDOPersistenceTestCase;

public class PgGeometrySpatialHelperTest extends JDOPersistenceTestCase {

    protected SpatialHelper helper;

    public PgGeometrySpatialHelperTest(String name) {
        super(name);
    }

    boolean runTestsForDatastore() {
        return (vendorID.equalsIgnoreCase("postgresql") || vendorID.equalsIgnoreCase("mysql"));
    }

    protected void setUp() throws Exception {
        helper = new SpatialHelper((JDOPersistenceManagerFactory) pmf);
        super.setUp();
    }

    public void testIsGeometryColumnBackedField() throws SQLException {
        if (!runTestsForDatastore()) {
            return;
        }
        assertTrue(helper.isGeometryColumnBackedField(org.jpox.samples.pggeometry.SampleGeometry.class, "geom"));
        assertFalse(helper.isGeometryColumnBackedField(org.jpox.samples.pggeometry.SampleGeometry.class, "name"));
    }

    public void testGetDimensionFromJdoMetadata() throws SQLException {
        if (!runTestsForDatastore()) {
            return;
        }
        Integer dim2 = new Integer(2);
        Integer dim3 = new Integer(3);
        assertEquals(dim2, helper.getDimensionFromJdoMetadata(org.jpox.samples.pggeometry.SampleGeometry.class, "geom"));
        assertEquals(dim3, helper.getDimensionFromJdoMetadata(org.jpox.samples.pggeometry.SampleGeometryCollection3D.class, "geom"));
    }

    public void testGetSridFromJdoMetadata() throws SQLException {
        if (!runTestsForDatastore()) {
            return;
        }
        Integer srid = new Integer(4326);
        assertEquals(srid, helper.getSridFromJdoMetadata(org.jpox.samples.pggeometry.SampleGeometry.class, "geom"));
    }
}
