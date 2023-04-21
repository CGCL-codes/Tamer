package symore.test.sentinel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import junit.framework.TestCase;
import symore.events.Event;
import symore.events.SerializableEvent;
import symore.sentinel.SentinelManager;
import symore.sentinel.SentinelNode;
import symore.sql.jdbcimpl.Context;
import symore.sql.jdbcimpl.SymoreDataSource;
import symore.sql.jdbcimpl.Transaction;
import symore.timestamps.GroupManagerException;
import symore.timestamps.GroupManagerFactoryLoader;
import symore.timestamps.TimestampException;
import symore.timestamps.TimestampFactoryLoader;
import symore.timestamps.interfaces.IGroupManager;
import symore.util.PropertiesManager;
import symore.util.uuid.BasicUUID;
import symore.util.uuid.BasicUUIDFactory;
import symore.util.uuid.UUID;
import symore.util.uuid.UUIDFactory;

/**
 * 
 * @author Frank Bregulla, Manuel Scholz
 *
 */
public class SentinelManagerWithDBTest extends TestCase {

    private SymoreDataSource ds;

    private SentinelManager sm;

    private IGroupManager gm;

    private Properties properties;

    public SentinelManagerWithDBTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        properties = new PropertiesManager().getProperties();
        properties.setProperty("Delta", "2");
        try {
            TimestampFactoryLoader oTSLoader = new TimestampFactoryLoader(properties);
            oTSLoader.initialize();
            GroupManagerFactoryLoader oGMLoader = new GroupManagerFactoryLoader(properties);
            oGMLoader.initialize();
            gm = oGMLoader.getGroupManagerFactory().getNewGroupManager(properties, null, oTSLoader.getTimestampFactory());
        } catch (TimestampException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        sm = new SentinelManager(null, properties, gm, null);
    }

    public void testGetSentinelNodesBiggerThanMax() {
        try {
            UUIDFactory uuidFac = new BasicUUIDFactory();
            UUID taId = uuidFac.createUUID();
            Properties properties = new PropertiesManager().getProperties();
            TimestampFactoryLoader oTSLoader = new TimestampFactoryLoader(properties);
            oTSLoader.initialize();
            GroupManagerFactoryLoader oGMLoader = new GroupManagerFactoryLoader(properties);
            oGMLoader.initialize();
            IGroupManager gm = oGMLoader.getGroupManagerFactory().getNewGroupManager(properties, null, oTSLoader.getTimestampFactory());
            Context context = new Context(sm, gm, null, null, oTSLoader.getTimestampFactory(), null);
            Transaction ta = new Transaction(taId, uuidFac.createUUID(), context);
            ta.setOriginatingSite(new BasicUUID(0, 0, 0));
            ta.setTimestamp(gm.getTimestampFactory().getNewTimestamp(gm));
            sm.addNewTransaction(ta);
            SentinelNode lastSN = (SentinelNode) sm.getSentinelNodesInTimestampOrder().last();
            SentinelNode firstSN = (SentinelNode) sm.getSentinelNodesInTimestampOrder().first();
            System.out.println(sm.getSentinelNodesInTimestampOrder().size());
            for (Iterator sn = sm.getSentinelNodesInTimestampOrder().iterator(); sn.hasNext(); ) System.out.println("Timestamp: " + ((SentinelNode) sn.next()).getTimestamp().getTime());
            Event afterLastSN = sm.getSentinelNodeBiggerThanMax();
            assertTrue(lastSN != null);
            assertTrue(afterLastSN != null);
            assertNotNull(lastSN.getOriginatingSite());
            assertNotNull(afterLastSN.getOriginatingSite());
            assertTrue(firstSN.equals(sm.getDummyNode()));
            assertFalse(firstSN.equals(lastSN));
            assertTrue(firstSN.compareTo(lastSN) < 0);
            System.out.println("afterLastSN timestamp: " + afterLastSN.getTimestamp().getTime());
            System.out.println("lastSN timestamp: " + lastSN.getTimestamp().getTime());
            System.out.println("firstSN timestamp: " + firstSN.getTimestamp().getTime());
            System.out.println("firstSN.getTimestamp().compareTo(lastSN.getTimestamp()): " + firstSN.getTimestamp().compareTo(lastSN.getTimestamp()));
            assertTrue(firstSN.getTimestamp().compareTo(lastSN.getTimestamp()) < 0);
            assertTrue(firstSN.compareTo(lastSN) < 0);
            assertTrue(lastSN.compareTo(firstSN) > 0);
            assertTrue(afterLastSN.compareTo(lastSN) > 0);
            assertTrue(lastSN.compareTo(afterLastSN) < 0);
            assertTrue(firstSN.compareTo(afterLastSN) < 0);
        } catch (TimestampException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (GroupManagerException e) {
            e.getMessage();
            assertTrue(false);
        }
    }

    private void insertMoreDataIntoDatabase() throws SQLException {
        String insert1 = "INSERT INTO test VALUES (default, 123,'abcd')";
        String insert2 = "INSERT INTO test VALUES (DEFAult, 456, 'defg')";
        Connection conn = ds.getConnection();
        Statement stmt;
        stmt = conn.createStatement();
        stmt.executeUpdate(insert1);
        stmt.executeUpdate(insert2);
        conn.commit();
        conn.close();
    }

    private void newDatabase() throws SQLException {
        String create = "CREATE TABLE test (rowId CHAR(36) primary key, x int, y varchar(128) colsentinel, sentinel row)";
        System.out.println("opening db-connection");
        Connection conn = ds.getConnection();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE test");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stmt = conn.createStatement();
        stmt.executeUpdate(create);
        conn.commit();
    }

    private void insertDataIntoDB() throws SQLException {
        String create = "CREATE TABLE test (rowId CHAR(36) primary key, x int, y varchar(128) colsentinel, sentinel row)";
        String insert1 = "INSERT INTO test VALUES (default, 42, 'galaxy')";
        String insert2 = "INSERT INTO test (x,y) VALUES ((SELECT x FROM test WHERE y = 'galaxy')+2, 'star')";
        String update1 = "UPDATE test SET y='23' WHERE y = 'galaxy'";
        newDatabase();
        System.out.println("opening db-connection");
        Connection conn = ds.getConnection();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(insert1);
            conn.commit();
            stmt = conn.createStatement();
            stmt.executeUpdate(insert2);
            stmt.executeUpdate(update1);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Vector readTransactionsFromDisk() throws Exception {
        Vector events = new Vector();
        Thread.sleep(1000);
        try {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream("disseminate"));
            try {
                events.add((SerializableEvent) oos.readObject());
                events.add((SerializableEvent) oos.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw e;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Reason: " + e.getMessage());
            throw e;
        }
        return events;
    }

    private void showTables() throws SQLException {
        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM app.test");
        while (rs.next()) {
            System.out.println(rs.getString(1) + ", " + rs.getInt(2) + ", " + rs.getString(3));
        }
        conn.close();
    }
}
