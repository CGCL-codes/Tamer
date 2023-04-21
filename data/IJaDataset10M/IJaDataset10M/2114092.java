package org.hkupp.db.accessors;

import be.proteomics.util.db.interfaces.Deleteable;
import be.proteomics.util.db.interfaces.Persistable;
import be.proteomics.util.db.interfaces.Retrievable;
import be.proteomics.util.db.interfaces.Updateable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

/**
 * This class is a generated accessor for the Spectrum table.
 *
 * @author DBAccessor generator class (Lennart Martens).
 */
public class SpectrumTableAccessor implements Deleteable, Retrievable, Updateable, Persistable {

    /**
	 * This variable tracks changes to the object.
	 */
    protected boolean iUpdated = false;

    /**
	 * This variable can hold generated primary key columns.
	 */
    protected Object[] iKeys = null;

    /**
	 * This variable represents the contents for the 'spectrumid' column.
	 */
    protected long iSpectrumid = Long.MIN_VALUE;

    /**
	 * This variable represents the contents for the 'l_locationid' column.
	 */
    protected long iL_locationid = Long.MIN_VALUE;

    /**
	 * This variable represents the contents for the 'l_instrumentid' column.
	 */
    protected long iL_instrumentid = Long.MIN_VALUE;

    /**
	 * This variable represents the contents for the 'filename' column.
	 */
    protected String iFilename = null;

    /**
	 * This variable represents the contents for the 'spectrumfile' column.
	 */
    protected byte[] iSpectrumfile = null;

    /**
	 * This variable represents the contents for the 'creationdate' column.
	 */
    protected java.sql.Timestamp iCreationdate = null;

    /**
	 * This variable represents the contents for the 'modificationdate' column.
	 */
    protected java.sql.Timestamp iModificationdate = null;

    /**
	 * This variable represents the contents for the 'username' column.
	 */
    protected String iUsername = null;

    /**
	 * This variable represents the key for the 'spectrumid' column.
	 */
    public static final String SPECTRUMID = "SPECTRUMID";

    /**
	 * This variable represents the key for the 'l_locationid' column.
	 */
    public static final String L_LOCATIONID = "L_LOCATIONID";

    /**
	 * This variable represents the key for the 'l_instrumentid' column.
	 */
    public static final String L_INSTRUMENTID = "L_INSTRUMENTID";

    /**
	 * This variable represents the key for the 'filename' column.
	 */
    public static final String FILENAME = "FILENAME";

    /**
	 * This variable represents the key for the 'spectrumfile' column.
	 */
    public static final String SPECTRUMFILE = "SPECTRUMFILE";

    /**
	 * This variable represents the key for the 'creationdate' column.
	 */
    public static final String CREATIONDATE = "CREATIONDATE";

    /**
	 * This variable represents the key for the 'modificationdate' column.
	 */
    public static final String MODIFICATIONDATE = "MODIFICATIONDATE";

    /**
	 * This variable represents the key for the 'username' column.
	 */
    public static final String USERNAME = "USERNAME";

    /**
	 * Default constructor.
	 */
    public SpectrumTableAccessor() {
    }

    /**
	 * This constructor allows the creation of the 'SpectrumTableAccessor' object based on a set of values in the HashMap.
	 *
	 * @param	aParams	HashMap with the parameters to initialize this object with.
	 *		<i>Please use only constants defined on this class as keys in the HashMap!</i>
	 */
    public SpectrumTableAccessor(HashMap aParams) {
        if (aParams.containsKey(SPECTRUMID)) {
            this.iSpectrumid = ((Long) aParams.get(SPECTRUMID)).longValue();
        }
        if (aParams.containsKey(L_LOCATIONID)) {
            this.iL_locationid = ((Long) aParams.get(L_LOCATIONID)).longValue();
        }
        if (aParams.containsKey(L_INSTRUMENTID)) {
            this.iL_instrumentid = ((Long) aParams.get(L_INSTRUMENTID)).longValue();
        }
        if (aParams.containsKey(FILENAME)) {
            this.iFilename = (String) aParams.get(FILENAME);
        }
        if (aParams.containsKey(SPECTRUMFILE)) {
            this.iSpectrumfile = (byte[]) aParams.get(SPECTRUMFILE);
        }
        if (aParams.containsKey(CREATIONDATE)) {
            this.iCreationdate = (java.sql.Timestamp) aParams.get(CREATIONDATE);
        }
        if (aParams.containsKey(MODIFICATIONDATE)) {
            this.iModificationdate = (java.sql.Timestamp) aParams.get(MODIFICATIONDATE);
        }
        if (aParams.containsKey(USERNAME)) {
            this.iUsername = (String) aParams.get(USERNAME);
        }
        this.iUpdated = true;
    }

    /**
	 * This constructor allows the creation of the 'SpectrumTableAccessor' object based on a resultset
	 * obtained by a 'select * from Spectrum' query.
	 *
	 * @param	aResultSet	ResultSet with the required columns to initialize this object with.
	 * @exception	SQLException	when the ResultSet could not be read.
	 */
    public SpectrumTableAccessor(ResultSet aResultSet) throws SQLException {
        this.iSpectrumid = aResultSet.getLong("spectrumid");
        this.iL_locationid = aResultSet.getLong("l_locationid");
        this.iL_instrumentid = aResultSet.getLong("l_instrumentid");
        this.iFilename = (String) aResultSet.getObject("filename");
        InputStream is4 = aResultSet.getBinaryStream("spectrumfile");
        Vector bytes4 = new Vector();
        int reading = -1;
        try {
            while ((reading = is4.read()) != -1) {
                bytes4.add(new Byte((byte) reading));
            }
            is4.close();
        } catch (IOException ioe) {
            bytes4 = new Vector();
        }
        reading = bytes4.size();
        this.iSpectrumfile = new byte[reading];
        for (int i = 0; i < reading; i++) {
            this.iSpectrumfile[i] = ((Byte) bytes4.get(i)).byteValue();
        }
        this.iCreationdate = (java.sql.Timestamp) aResultSet.getObject("creationdate");
        this.iModificationdate = (java.sql.Timestamp) aResultSet.getObject("modificationdate");
        this.iUsername = (String) aResultSet.getObject("username");
        this.iUpdated = true;
    }

    /**
	 * This method returns the value for the 'Spectrumid' column
	 * 
	 * @return	long	with the value for the Spectrumid column.
	 */
    public long getSpectrumid() {
        return this.iSpectrumid;
    }

    /**
	 * This method returns the value for the 'L_locationid' column
	 * 
	 * @return	long	with the value for the L_locationid column.
	 */
    public long getL_locationid() {
        return this.iL_locationid;
    }

    /**
	 * This method returns the value for the 'L_instrumentid' column
	 * 
	 * @return	long	with the value for the L_instrumentid column.
	 */
    public long getL_instrumentid() {
        return this.iL_instrumentid;
    }

    /**
	 * This method returns the value for the 'Filename' column
	 * 
	 * @return	String	with the value for the Filename column.
	 */
    public String getFilename() {
        return this.iFilename;
    }

    /**
	 * This method returns the value for the 'Spectrumfile' column
	 * 
	 * @return	byte[]	with the value for the Spectrumfile column.
	 */
    public byte[] getSpectrumfile() {
        return this.iSpectrumfile;
    }

    /**
	 * This method returns the value for the 'Creationdate' column
	 * 
	 * @return	java.sql.Timestamp	with the value for the Creationdate column.
	 */
    public java.sql.Timestamp getCreationdate() {
        return this.iCreationdate;
    }

    /**
	 * This method returns the value for the 'Modificationdate' column
	 * 
	 * @return	java.sql.Timestamp	with the value for the Modificationdate column.
	 */
    public java.sql.Timestamp getModificationdate() {
        return this.iModificationdate;
    }

    /**
	 * This method returns the value for the 'Username' column
	 * 
	 * @return	String	with the value for the Username column.
	 */
    public String getUsername() {
        return this.iUsername;
    }

    /**
	 * This method sets the value for the 'Spectrumid' column
	 * 
	 * @param	aSpectrumid	long with the value for the Spectrumid column.
	 */
    public void setSpectrumid(long aSpectrumid) {
        this.iSpectrumid = aSpectrumid;
        this.iUpdated = true;
    }

    /**
	 * This method sets the value for the 'L_locationid' column
	 * 
	 * @param	aL_locationid	long with the value for the L_locationid column.
	 */
    public void setL_locationid(long aL_locationid) {
        this.iL_locationid = aL_locationid;
        this.iUpdated = true;
    }

    /**
	 * This method sets the value for the 'L_instrumentid' column
	 * 
	 * @param	aL_instrumentid	long with the value for the L_instrumentid column.
	 */
    public void setL_instrumentid(long aL_instrumentid) {
        this.iL_instrumentid = aL_instrumentid;
        this.iUpdated = true;
    }

    /**
	 * This method sets the value for the 'Filename' column
	 * 
	 * @param	aFilename	String with the value for the Filename column.
	 */
    public void setFilename(String aFilename) {
        this.iFilename = aFilename;
        this.iUpdated = true;
    }

    /**
	 * This method sets the value for the 'Spectrumfile' column
	 * 
	 * @param	aSpectrumfile	byte[] with the value for the Spectrumfile column.
	 */
    public void setSpectrumfile(byte[] aSpectrumfile) {
        this.iSpectrumfile = aSpectrumfile;
        this.iUpdated = true;
    }

    /**
	 * This method sets the value for the 'Creationdate' column
	 * 
	 * @param	aCreationdate	java.sql.Timestamp with the value for the Creationdate column.
	 */
    public void setCreationdate(java.sql.Timestamp aCreationdate) {
        this.iCreationdate = aCreationdate;
        this.iUpdated = true;
    }

    /**
	 * This method sets the value for the 'Modificationdate' column
	 * 
	 * @param	aModificationdate	java.sql.Timestamp with the value for the Modificationdate column.
	 */
    public void setModificationdate(java.sql.Timestamp aModificationdate) {
        this.iModificationdate = aModificationdate;
        this.iUpdated = true;
    }

    /**
	 * This method sets the value for the 'Username' column
	 * 
	 * @param	aUsername	String with the value for the Username column.
	 */
    public void setUsername(String aUsername) {
        this.iUsername = aUsername;
        this.iUpdated = true;
    }

    /**
	 * This method allows the caller to delete the data represented by this
	 * object in a persistent store.
	 *
	 * @param   aConn Connection to the persitent store.
	 */
    public int delete(Connection aConn) throws SQLException {
        PreparedStatement lStat = aConn.prepareStatement("DELETE FROM spectrum WHERE spectrumid = ?");
        lStat.setLong(1, iSpectrumid);
        int result = lStat.executeUpdate();
        lStat.close();
        return result;
    }

    /**
	 * This method allows the caller to read data for this
	 * object from a persistent store based on the specified keys.
	 *
	 * @param   aConn Connection to the persitent store.
	 */
    public void retrieve(Connection aConn, HashMap aKeys) throws SQLException {
        if (!aKeys.containsKey(SPECTRUMID)) {
            throw new IllegalArgumentException("Primary key field 'SPECTRUMID' is missing in HashMap!");
        } else {
            iSpectrumid = ((Long) aKeys.get(SPECTRUMID)).longValue();
        }
        PreparedStatement lStat = aConn.prepareStatement("SELECT * FROM spectrum WHERE spectrumid = ?");
        lStat.setLong(1, iSpectrumid);
        ResultSet lRS = lStat.executeQuery();
        int hits = 0;
        while (lRS.next()) {
            hits++;
            iSpectrumid = lRS.getLong("spectrumid");
            iL_locationid = lRS.getLong("l_locationid");
            iL_instrumentid = lRS.getLong("l_instrumentid");
            iFilename = (String) lRS.getObject("filename");
            InputStream is4 = lRS.getBinaryStream("spectrumfile");
            Vector bytes4 = new Vector();
            int reading = -1;
            try {
                while ((reading = is4.read()) != -1) {
                    bytes4.add(new Byte((byte) reading));
                }
                is4.close();
            } catch (IOException ioe) {
                bytes4 = new Vector();
            }
            reading = bytes4.size();
            iSpectrumfile = new byte[reading];
            for (int i = 0; i < reading; i++) {
                iSpectrumfile[i] = ((Byte) bytes4.get(i)).byteValue();
            }
            iCreationdate = (java.sql.Timestamp) lRS.getObject("creationdate");
            iModificationdate = (java.sql.Timestamp) lRS.getObject("modificationdate");
            iUsername = (String) lRS.getObject("username");
        }
        lRS.close();
        lStat.close();
        if (hits > 1) {
            throw new SQLException("More than one hit found for the specified primary keys in the 'spectrum' table! Object is initialized to last row returned.");
        } else if (hits == 0) {
            throw new SQLException("No hits found for the specified primary keys in the 'spectrum' table! Object is not initialized correctly!");
        }
    }

    /**
	 * This method allows the caller to update the data represented by this
	 * object in a persistent store.
	 *
	 * @param   aConn Connection to the persitent store.
	 */
    public int update(Connection aConn) throws SQLException {
        if (!this.iUpdated) {
            return 0;
        }
        PreparedStatement lStat = aConn.prepareStatement("UPDATE spectrum SET spectrumid = ?, l_locationid = ?, l_instrumentid = ?, filename = ?, spectrumfile = ?, creationdate = ?, modificationdate = CURRENT_TIMESTAMP, username = ? WHERE spectrumid = ?");
        lStat.setLong(1, iSpectrumid);
        lStat.setLong(2, iL_locationid);
        lStat.setLong(3, iL_instrumentid);
        lStat.setObject(4, iFilename);
        ByteArrayInputStream bais4 = new ByteArrayInputStream(iSpectrumfile);
        lStat.setBinaryStream(5, bais4, iSpectrumfile.length);
        lStat.setObject(6, iCreationdate);
        lStat.setObject(7, iUsername);
        lStat.setLong(8, iSpectrumid);
        int result = lStat.executeUpdate();
        lStat.close();
        this.iUpdated = false;
        return result;
    }

    /**
	 * This method allows the caller to insert the data represented by this
	 * object in a persistent store.
	 *
	 * @param   aConn Connection to the persitent store.
	 */
    public int persist(Connection aConn) throws SQLException {
        PreparedStatement lStat = aConn.prepareStatement("INSERT INTO spectrum (spectrumid, l_locationid, l_instrumentid, filename, spectrumfile, creationdate, modificationdate, username) values(?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_USER)");
        if (iSpectrumid == Long.MIN_VALUE) {
            lStat.setNull(1, 4);
        } else {
            lStat.setLong(1, iSpectrumid);
        }
        if (iL_locationid == Long.MIN_VALUE) {
            lStat.setNull(2, 4);
        } else {
            lStat.setLong(2, iL_locationid);
        }
        if (iL_instrumentid == Long.MIN_VALUE) {
            lStat.setNull(3, 4);
        } else {
            lStat.setLong(3, iL_instrumentid);
        }
        if (iFilename == null) {
            lStat.setNull(4, -1);
        } else {
            lStat.setObject(4, iFilename);
        }
        if (iSpectrumfile == null) {
            lStat.setNull(5, -4);
        } else {
            ByteArrayInputStream bais4 = new ByteArrayInputStream(iSpectrumfile);
            lStat.setBinaryStream(5, bais4, iSpectrumfile.length);
        }
        int result = lStat.executeUpdate();
        ResultSet lrsKeys = lStat.getGeneratedKeys();
        ResultSetMetaData lrsmKeys = lrsKeys.getMetaData();
        int colCount = lrsmKeys.getColumnCount();
        iKeys = new Object[colCount];
        while (lrsKeys.next()) {
            for (int i = 0; i < iKeys.length; i++) {
                iKeys[i] = lrsKeys.getObject(i + 1);
            }
        }
        lrsKeys.close();
        lStat.close();
        this.iUpdated = false;
        return result;
    }

    /**
	 * This method will return the automatically generated key for the insert if 
	 * one was triggered, or 'null' otherwise.
	 *
	 * @return	Object[]	with the generated keys.
	 */
    public Object[] getGeneratedKeys() {
        return this.iKeys;
    }
}
