package dataImporters;

import junit.framework.TestCase;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import database.InfoWarehouse;
import database.Database;

public class TSImportTest extends TestCase {

    private String taskFile;

    private InfoWarehouse db;

    private static final int NUM_PARTICLES = 5000;

    private File tsFile;

    private File tsExcelFormatFile;

    private Date firstParticleTime;

    private Date firstExcelParticleTime;

    protected void setUp() throws Exception {
        super.setUp();
        new database.CreateTestDatabase();
        db = Database.getDatabase("TestDB");
        if (!db.openConnection("TestDB")) {
            throw new Exception("Couldn't open DB con");
        }
        tsFile = makeTestFile();
        tsExcelFormatFile = makeTestFileExcelDates();
        taskFile = makeTaskFile();
    }

    private File makeTestFile() throws IOException {
        File f = File.createTempFile("tsFile", ".csv");
        PrintWriter ts = new PrintWriter(f);
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat dForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ts.println("Time,Val");
        firstParticleTime = c.getTime();
        for (int i = 0; i < NUM_PARTICLES; i++) {
            ts.println(dForm.format(c.getTime()) + "," + i);
            c.add(Calendar.SECOND, 30);
        }
        ts.close();
        return f;
    }

    private File makeTestFileExcelDates() throws IOException {
        File f = File.createTempFile("tsExcelFile", ".csv");
        PrintWriter ts = new PrintWriter(f);
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat dForm = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        ts.println("Time,Excel");
        firstExcelParticleTime = c.getTime();
        for (int i = 0; i < NUM_PARTICLES; i++) {
            ts.println(dForm.format(c.getTime()) + "," + i);
            c.add(Calendar.SECOND, 30);
        }
        ts.close();
        return f;
    }

    private String makeTaskFile() throws IOException {
        File taskF = File.createTempFile("taskFile", ".task");
        PrintWriter task = new PrintWriter(taskF);
        task.println(tsFile.getName() + ",Time,Val");
        task.println(tsExcelFormatFile.getName() + ",Time,Excel");
        task.close();
        return taskF.getPath();
    }

    public void testRead() {
        TSImport imp = new TSImport(db, null, false);
        System.out.println("Inserting " + NUM_PARTICLES + " particles.");
        System.out.println("Started at " + new Date());
        try {
            if (!imp.readTaskFile(taskFile)) {
                fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        System.out.println("Ended at   " + new Date());
        try {
            int collectionID = 0;
            Connection con = db.getCon();
            PreparedStatement ps = con.prepareStatement("USE TestDB\n" + "SELECT tsdense.AtomID, tsdense.Time, tsdense.Value, mem.CollectionID, coll.Name\n" + "FROM TimeSeriesAtomInfoDense tsdense, \n" + "AtomMembership mem, \n" + "Collections coll \n" + "WHERE tsdense.Time = ? and " + "mem.AtomID = tsdense.AtomID and " + "coll.CollectionID = mem.CollectionID");
            SimpleDateFormat dForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, dForm.format(firstParticleTime));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals("Inserted wrong value for first particle", rs.getInt("Value"), 0);
                assertEquals("Inserted wrong collection name for first file", rs.getString("Name"), "Val");
                collectionID = rs.getInt("CollectionID");
            } else {
                fail("Did not insert the correct time value for first particle");
            }
            rs.close();
            rs = con.createStatement().executeQuery("USE TestDB \n" + "SELECT count(*) \n" + "FROM InternalAtomOrder " + "WHERE CollectionID = " + collectionID);
            if (rs.next()) {
                assertEquals("Inserted the wrong number of particles into InternalAtomOrder", 5000, rs.getInt(1));
            } else {
                fail("Did not insert any values into InternalAtomOrder");
            }
            ps.setString(1, dForm.format(firstExcelParticleTime));
            rs.close();
            rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals("Inserted wrong value for first excel particle", rs.getInt("Value"), 0);
            } else {
                fail("Did not insert the correct time value for first excel particle");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tearDown() {
        db.closeConnection();
        tsFile.delete();
        tsExcelFormatFile.delete();
        new File(taskFile).delete();
    }
}
