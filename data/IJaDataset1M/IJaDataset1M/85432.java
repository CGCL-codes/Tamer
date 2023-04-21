package edu.ucdavis.genomics.metabolomics.binbase.util.io.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Savepoint;
import net.sf.mzmine.datastructures.RawDataAtNode;
import net.sf.mzmine.datastructures.Scan;

/**
 * class to store netcdf data in the internal database for faster access
 * 
 * @author wohlgemuth
 * 
 */
public class StoreNetCDFFile {

    /**
	 * gives us a connection
	 * 
	 * @return
	 * @throws Exception
	 */
    public Connection obtainConnection() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:Bellerophon", "sa", "");
        return conn;
    }

    /**
	 * creates the schema
	 * 
	 * @throws Exception
	 */
    public void createSchema() throws Exception {
        Connection conn = obtainConnection();
        conn.setAutoCommit(false);
        Savepoint save = conn.setSavepoint();
        try {
            conn.commit();
            PreparedStatement cleanup = conn.prepareStatement("DROP ALL OBJECTS");
            PreparedStatement createSampleTable = conn.prepareStatement("CREATE TABLE SAMPLES (name VARCHAR(255), name_id INTEGER AUTO_INCREMENT(0,1) PRIMARY KEY)");
            PreparedStatement createTimeTable = conn.prepareStatement("CREATE TABLE RETENTION_TIMES (time double, time_id INTEGER AUTO_INCREMENT(0,1) PRIMARY KEY,name_id INTEGER)");
            PreparedStatement createIonTable = conn.prepareStatement("CREATE TABLE IONS (ion_id INTEGER AUTO_INCREMENT(0,1) PRIMARY KEY,time_id INTEGER,ion integer, intensity double)");
            PreparedStatement createIonTraceTable = conn.prepareStatement("CREATE TABLE ION_TRACE (ion_id INTEGER AUTO_INCREMENT(0,1) PRIMARY KEY,time_id INTEGER,ion integer, intensity double)");
            cleanup.execute();
            createTimeTable.execute();
            createSampleTable.execute();
            createIonTraceTable.execute();
            createIonTable.execute();
            cleanup.close();
            createTimeTable.close();
            createSampleTable.close();
            createIonTraceTable.close();
            createIonTable.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback(save);
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    /**
	 * stores a netcdf file in the database
	 * 
	 * @param file
	 * @return returns the internal identifier
	 * @throws Exception
	 */
    public String store(File file) throws Exception {
        RawDataAtNode node = new RawDataAtNode(0, file);
        node.setWorkingCopy(file);
        node.preLoad();
        node.initializeScanBrowser(0, node.getNumberOfScans());
        Connection conn = obtainConnection();
        PreparedStatement insertSpectra = conn.prepareStatement("INSERT INTO IONS(ION,INTENSITY) VALUES(?,?)");
        for (int i = 0; i < node.getNumberOfScans(); i++) {
            Scan s = node.getNextScan();
            double time = node.getScanTime(s.getScanNumber());
            double[] mz = s.getMZValues();
            double[] intensity = s.getIntensityValues();
            for (int x = 0; x < mz.length; x++) {
                if (intensity[x] > 0) {
                    insertSpectra.setInt(1, (int) mz[x]);
                    insertSpectra.setDouble(2, intensity[x]);
                    insertSpectra.execute();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        StoreNetCDFFile store = new StoreNetCDFFile();
        store.createSchema();
        store.store(new File("/home/wohlgemuth/060808bsdsa63_1.cdf"));
    }
}
