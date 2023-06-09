package org.perfmon4j.java.management;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mockito.Mockito;
import org.perfmon4j.SQLTest;
import org.perfmon4j.SnapShotData;
import org.perfmon4j.instrument.snapshot.Delta;
import org.perfmon4j.instrument.snapshot.SnapShotGenerator;
import org.perfmon4j.instrument.snapshot.SnapShotGenerator.Bundle;
import org.perfmon4j.java.management.GarbageCollectorSnapShot.GarbageCollectorData;
import org.perfmon4j.java.management.GarbageCollectorSnapShot.SQLWriter;
import org.perfmon4j.util.JDBCHelper;

public class GarbageCollectorSnapShotTest extends SQLTest {

    public static final String TEST_ALL_TEST_TYPE = "UNIT";

    final String DERBY_CREATE_1 = "CREATE TABLE p4j.P4JGarbageCollection(\r\n" + "	InstanceName VARCHAR(200) NOT NULL,\r\n" + "	StartTime TIMESTAMP NOT NULL,\r\n" + "	EndTime TIMESTAMP NOT NULL,\r\n" + "	Duration INT NOT NULL,\r\n" + "	NumCollections INT NOT NULL,\r\n" + "	CollectionMillis INT NOT NULL,\r\n" + "	NumCollectionsPerMinute DECIMAL(9,2) NOT NULL,\r\n" + "	CollectionMillisPerMinute DECIMAL(9,2) NOT NULL\r\n" + ")\r\n";

    final String DERBY_DROP_1 = "DROP TABLE p4j.P4JGarbageCollection";

    private Connection conn;

    protected void setUp() throws Exception {
        super.setUp();
        conn = appender.getConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(DERBY_CREATE_1);
        } finally {
            JDBCHelper.closeNoThrow(stmt);
        }
    }

    protected void tearDown() throws Exception {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(DERBY_DROP_1);
        } finally {
            JDBCHelper.closeNoThrow(stmt);
        }
        super.tearDown();
    }

    public GarbageCollectorSnapShotTest(String name) {
        super(name);
    }

    public void testGetGarbageCollectors() throws Exception {
        GarbageCollectorMXBean gcBeans[] = GarbageCollectorSnapShot.getAllGarbageCollectors();
        assertNotNull(gcBeans);
        assertTrue("should have at least 1 garbage collector", gcBeans.length >= 1);
        GarbageCollectorMXBean bean = GarbageCollectorSnapShot.getGarbageCollector("SHOULD NOT EXIST");
        assertNull("Should not exist", bean);
        String gcName = gcBeans[0].getName();
        bean = GarbageCollectorSnapShot.getGarbageCollector(gcName);
        assertNotNull("Should exist", bean);
        assertEquals("gcName should match", gcName, bean.getName());
    }

    public void testValidateDataInterface() throws Exception {
        Bundle bundle = SnapShotGenerator.generateBundle(GarbageCollectorSnapShot.class);
        SnapShotData data = bundle.newSnapShotData();
        assertTrue("Should implement interface", data instanceof GarbageCollectorSnapShot.GarbageCollectorData);
        GarbageCollectorSnapShot.GarbageCollectorData d = (GarbageCollectorSnapShot.GarbageCollectorData) data;
        Method methods[] = GarbageCollectorSnapShot.GarbageCollectorData.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            try {
                methods[i].invoke(d, new Object[] {});
            } catch (InvocationTargetException it) {
                fail("Invalid method on data interface.  method name: " + methods[i].getName());
            }
        }
    }

    public void testGarbageCollectionInfoToSQL() throws Exception {
        SQLWriter writer = new GarbageCollectorSnapShot.SQLWriter();
        GarbageCollectorData data = Mockito.mock(GarbageCollectorData.class);
        long start = System.currentTimeMillis();
        long end = start + 60000;
        Mockito.when(data.getStartTime()).thenReturn(new Long(start));
        Mockito.when(data.getStartTime()).thenReturn(new Long(end));
        Mockito.when(data.getInstanceName()).thenReturn("Mark/Sweep");
        Mockito.when(data.getCollectionCount()).thenReturn(new Delta(1, 5, 60000));
        Mockito.when(data.getCollectionTime()).thenReturn(new Delta(100, 1100, 60000));
        writer.writeToSQL(conn, "p4j", data);
        final String VALIDATE_SQL = "SELECT " + " COUNT(*) " + " FROM p4j.P4JGarbageCollection\r\n" + " WHERE InstanceName='Mark/Sweep'\r \n" + " AND StartTime=?\r\n" + " AND EndTime=?\r\n" + " AND Duration=?\r\n" + " AND NumCollections=?\r\n" + " AND CollectionMillis=?\r\n" + " AND NumCollectionsPerMinute=?\r\n" + " AND CollectionMillisPerMinute=?\r\n";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(VALIDATE_SQL);
            stmt.setTimestamp(1, new Timestamp(data.getStartTime()));
            stmt.setTimestamp(2, new Timestamp(data.getEndTime()));
            stmt.setLong(3, data.getDuration());
            stmt.setLong(4, data.getCollectionCount().getDelta());
            stmt.setLong(5, data.getCollectionTime().getDelta());
            stmt.setDouble(6, data.getCollectionCount().getDeltaPerMinute());
            stmt.setDouble(7, data.getCollectionTime().getDeltaPerMinute());
            long resultCount = JDBCHelper.getQueryCount(stmt);
            assertEquals("Should have inserted row", 1, resultCount);
        } finally {
            JDBCHelper.closeNoThrow(stmt);
        }
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getLogger(GarbageCollectorSnapShotTest.class.getPackage().getName()).setLevel(Level.DEBUG);
        String[] testCaseName = { GarbageCollectorSnapShotTest.class.getName() };
        TestRunner.main(testCaseName);
    }

    public static junit.framework.Test suite() {
        String testType = System.getProperty("UNIT");
        TestSuite newSuite = new TestSuite();
        if (testType != null || newSuite == null || (newSuite.countTestCases() < 1)) {
            newSuite = new TestSuite(GarbageCollectorSnapShotTest.class);
        }
        return (newSuite);
    }
}
