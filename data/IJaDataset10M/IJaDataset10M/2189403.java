package junit.iono;

import org.junit.*;
import org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import java.sql.*;
import java.util.*;
import dbaccess.util2.*;
import dbaccess.iono2.*;

/**
 * JUnit TestCase for Ursi class
 */
public class cdmpLoadTest30 {

    private static String DB = "ionodb_blank";

    static IonoRowList rows;

    static int Nrows = 0;

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(cdmpLoadTest30.class);
    }

    /**
    * setup before the class 
    * load the test file data into the database
    */
    @BeforeClass
    public static void setUp() {
        String args[] = { "--database=" + DB, "testfile2.CDMP" };
        CdmpLoad c = new CdmpLoad();
        boolean rc = c.initialize(args);
        if (!rc) System.exit(1);
        c.loop();
        DBProperties prop = new DBProperties();
        prop.setProperty("propPrefix", "Iono");
        prop.setProperty("database", DB);
        prop.getProperties();
        DBConnect connection = new DBConnect(prop, "Iono");
        connection.connect();
        rows = new IonoRowList(connection);
        try {
            Nrows = rows.get("LI050", 1952, 01);
        } catch (Exception e) {
            Assert.fail("Failed on getting loaded data; rows.get() exception:" + e);
        }
    }

    /**
    * teardown after the class
    * remove the test file data from the database
    */
    @AfterClass
    public static void tearDown() {
        String args[] = { "-d", "--database=" + DB, "testfile2.CDMP" };
        CdmpLoad c = new CdmpLoad();
        boolean rc = c.initialize(args);
        if (!rc) System.exit(1);
        c.loop();
    }

    @Test
    public void check00() {
        ArrayList<IonoRow> urows = rows.getUrsiRows("00");
        Assert.assertEquals(31, urows.size());
        IonoRow row;
        IonoObs obs;
        IonoObsList obsList;
        row = urows.get(0);
        Assert.assertEquals("LI050", row.getStn());
        Assert.assertEquals(new DateTime(1952, 1, 1), row.getObsdate());
        Assert.assertEquals("00", row.getUrsi());
        Assert.assertEquals("R", row.getRelease());
        Assert.assertEquals(0, row.getIonosondeID());
        Assert.assertEquals(1.0, row.getScaling(), .00001);
        Assert.assertEquals(0, row.getOffset());
        Assert.assertEquals(48, row.getNobs());
        obsList = row.getObsList();
        Assert.assertEquals(48, obsList.getTobs());
        Assert.assertEquals(48, obsList.getNobs());
        obs = obsList.get(0);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(3.1, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(1);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(2.9, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(2);
        Assert.assertEquals(1, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(3.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(47);
        Assert.assertEquals(23, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(1.8, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        row = urows.get(1);
        Assert.assertEquals("LI050", row.getStn());
        Assert.assertEquals(new DateTime(1952, 1, 2), row.getObsdate());
        Assert.assertEquals("00", row.getUrsi());
        Assert.assertEquals("R", row.getRelease());
        Assert.assertEquals(0, row.getIonosondeID());
        Assert.assertEquals(1.0, row.getScaling(), .00001);
        Assert.assertEquals(0, row.getOffset());
        Assert.assertEquals(45, row.getNobs());
        obsList = row.getObsList();
        Assert.assertEquals(45, obsList.getTobs());
        Assert.assertEquals(45, obsList.getNobs());
        obs = obsList.get(0);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(1.8, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(1);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(1.65, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(2);
        Assert.assertEquals(1, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(1.65, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(44);
        Assert.assertEquals(23, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(2.9, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        row = urows.get(30);
        Assert.assertEquals("LI050", row.getStn());
        Assert.assertEquals(new DateTime(1952, 1, 31), row.getObsdate());
        Assert.assertEquals("00", row.getUrsi());
        Assert.assertEquals("R", row.getRelease());
        Assert.assertEquals(0, row.getIonosondeID());
        Assert.assertEquals(1.0, row.getScaling(), .00001);
        Assert.assertEquals(0, row.getOffset());
        Assert.assertEquals(36, row.getNobs());
        obsList = row.getObsList();
        Assert.assertEquals(36, obsList.getTobs());
        Assert.assertEquals(36, obsList.getNobs());
        obs = obsList.get(0);
        Assert.assertEquals(4, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(1.4, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(1);
        Assert.assertEquals(4, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(1.6, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(2);
        Assert.assertEquals(5, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(1.5, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(35);
        Assert.assertEquals(22, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(2.7, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
    }

    @Test
    public void check34() {
        ArrayList<IonoRow> urows = rows.getUrsiRows("34");
        Assert.assertEquals(31, urows.size());
        IonoRow row;
        IonoObs obs;
        IonoObsList obsList;
        row = urows.get(0);
        Assert.assertEquals("LI050", row.getStn());
        Assert.assertEquals(new DateTime(1952, 1, 1), row.getObsdate());
        Assert.assertEquals("34", row.getUrsi());
        Assert.assertEquals("R", row.getRelease());
        Assert.assertEquals(0, row.getIonosondeID());
        Assert.assertEquals(1.0, row.getScaling(), .00001);
        Assert.assertEquals(0, row.getOffset());
        Assert.assertEquals(48, row.getNobs());
        obsList = row.getObsList();
        Assert.assertEquals(48, obsList.getTobs());
        Assert.assertEquals(48, obsList.getNobs());
        obs = obsList.get(0);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(1);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(47);
        Assert.assertEquals(23, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        row = urows.get(1);
        Assert.assertEquals("LI050", row.getStn());
        Assert.assertEquals(new DateTime(1952, 1, 2), row.getObsdate());
        Assert.assertEquals("34", row.getUrsi());
        Assert.assertEquals("R", row.getRelease());
        Assert.assertEquals(0, row.getIonosondeID());
        Assert.assertEquals(1.0, row.getScaling(), .00001);
        Assert.assertEquals(0, row.getOffset());
        Assert.assertEquals(48, row.getNobs());
        obsList = row.getObsList();
        Assert.assertEquals(48, obsList.getTobs());
        Assert.assertEquals(48, obsList.getNobs());
        obs = obsList.get(0);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(1);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(47);
        Assert.assertEquals(23, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        row = urows.get(30);
        Assert.assertEquals("LI050", row.getStn());
        Assert.assertEquals(new DateTime(1952, 1, 31), row.getObsdate());
        Assert.assertEquals("34", row.getUrsi());
        Assert.assertEquals("R", row.getRelease());
        Assert.assertEquals(0, row.getIonosondeID());
        Assert.assertEquals(1.0, row.getScaling(), .00001);
        Assert.assertEquals(0, row.getOffset());
        Assert.assertEquals(44, row.getNobs());
        obsList = row.getObsList();
        Assert.assertEquals(44, obsList.getTobs());
        Assert.assertEquals(44, obsList.getNobs());
        obs = obsList.get(0);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(1);
        Assert.assertEquals(0, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(2);
        Assert.assertEquals(1, obs.getHour());
        Assert.assertEquals(0, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
        obs = obsList.get(43);
        Assert.assertEquals(22, obs.getHour());
        Assert.assertEquals(30, obs.getMin());
        Assert.assertEquals(0, obs.getSec());
        Assert.assertEquals(100.0, obs.getData(), .0001);
        Assert.assertEquals(3, obs.getSourceID());
        Assert.assertEquals(0, obs.getQual());
    }
}
