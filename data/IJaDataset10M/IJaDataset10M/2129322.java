package dataExporters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JFrame;
import collection.Collection;
import junit.framework.TestCase;
import database.CreateTestDatabase;
import database.Database;
import database.InfoWarehouse;
import gui.ProgressBarWrapper;

/**
 * Tests MSAnalyze export as implemented in MSAnalyzeDataSetExporter
 * We don't really need to test the db parts since that's already tested in
 * DatabaseText.  We only test the file stuff, really.
 * @author jtbigwoo
 */
public class MSAnalyzeDataSetExporterTest extends TestCase {

    MSAnalyzeDataSetExporter exporter;

    InfoWarehouse db;

    File parFile;

    File setFile;

    public MSAnalyzeDataSetExporterTest(String s) {
        super(s);
    }

    /**
	 * @see TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        new CreateTestDatabase();
        db = (Database) Database.getDatabase("TestDB");
        if (!db.openConnection("TestDB")) {
            throw new Exception("Couldn't open DB con");
        }
        JFrame mf = new JFrame();
        final ProgressBarWrapper progressBar = new ProgressBarWrapper(mf, "Exporting to MS-Analyze", 100);
        exporter = new MSAnalyzeDataSetExporter(mf, db, progressBar);
    }

    public void testNormalExport() {
        try {
            boolean result;
            Collection coll = db.getCollection(2);
            parFile = File.createTempFile("test", ".par");
            result = exporter.exportToPar(coll, parFile.getPath(), null);
            assertTrue("Failure during exportToPar in a normal export", result);
            assertTrue("Export did not write par file", parFile.exists());
            BufferedReader reader = new BufferedReader(new FileReader(parFile));
            assertEquals("First line of .par file is wrong", "ATOFMS data set parameters", reader.readLine());
            String fileName = (new File(parFile.getName().replaceAll("\\.par$", ""))).getName();
            assertEquals("Second line of .par file did not match name of par file", fileName, reader.readLine());
            assertEquals("Third line of .par file had the wrong date", "09/02/2003 17:30:38", reader.readLine());
            assertEquals("Fourth line of .par file didn't match collection comment", coll.getComment(), reader.readLine());
            reader.close();
            setFile = new File(parFile.getPath().replaceAll("\\.par$", ".set"));
            assertTrue("Export did not write set file", setFile.exists());
            reader = new BufferedReader(new FileReader(setFile));
            assertEquals("1,..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\, 1, 65535, 1.000000, 09/02/2003 17:30:38", reader.readLine());
            parFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Caught Exception in testNormalExport");
        }
    }

    /**
	 * This is a test for bug 1951538.  If the export routine blew up and left
	 * some temp tables, it wouldn't work a subsequent time.
	 */
    public void testFailureBeforeSuccess() {
        try {
            boolean result;
            Connection con = db.getCon();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE #ParticlesToExport (AtomID INT " + "PRIMARY KEY, Filename TEXT, [Time] DATETIME, [Size] FLOAT, " + "LaserPower FLOAT, NumPeaks INT, TotalPosIntegral INT, " + "TotalNegIntegral INT)\n");
            Collection coll = db.getCollection(2);
            parFile = File.createTempFile("test", ".par");
            result = exporter.exportToPar(coll, parFile.getPath(), null);
            assertTrue("Failure during exportToPar in a normal export", result);
            assertTrue("Export did not write par file", parFile.exists());
            setFile = new File(parFile.getPath().replaceAll("\\.par$", ".set"));
            assertTrue("Export did not write set file", setFile.exists());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Caught Exception in testNormalExport");
        }
    }

    public void tearDown() {
        db.closeConnection();
        if (parFile != null) parFile.delete();
        if (setFile != null) setFile.delete();
    }
}
