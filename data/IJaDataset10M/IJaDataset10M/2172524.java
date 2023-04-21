package com.iver.cit.gvsig.mdb.core;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.lang.reflect.Field;
import java.awt.geom.Rectangle2D;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.mdb.panels.MDBtoHSQLDB;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class for accessing a GeoMedia warehouse.
 * @author Stefano Orlando
 */
public class MDBWarehouse {

    private String osName;

    private File mdbFile;

    private String driver;

    private String tempHSQLDB;

    private Connection conAccessDBConnection;

    private String currentFeatureClass;

    private String[] currentFeatureClassColNames;

    private Class[] currentFeatureClassColJavaTypes;

    private int[] currentFeatureClassColSQLTypes;

    private String currentFeatureClassGeomField;

    private String currentFeatureClassSpatialIndexField;

    private int[] currentFeatureClassPKFields;

    private Object[][] currentFeatureClassAttrTable;

    private Object[] currentFeatureClassGeometry;

    private Object[] currentFeatureClassSpatialIndex;

    private int currentFeatureClassGeometryType;

    private byte currentFeatureClassCoordSystemType;

    private double currentFeatureClassHorStorageUnitX;

    private double currentFeatureClassHorStorageUnitY;

    private double currentFeatureClassVerStorageUnit;

    private double currentFeatureClassStorageCenterX;

    private double currentFeatureClassStorageCenterY;

    private double currentFeatureClassStorageCenterZ;

    private static Map<Integer, Class<?>> mapSqlTypes;

    private Map<String, String> mapGAliasTable;

    private Geometry geom;

    private Envelope env;

    private MDBGeometryAdapter geometryAdapter;

    private boolean firstGeometry = true;

    private double xMin, xMax, yMin, yMax;

    private static boolean fullScan = true;

    /**
	 * Creates a new MDBWarehouse object and opens the connection to the underlying database
	 * (Warehouse) using the JDBC-ODBC bridge for Windows or the MDBtoHSQLDB conversion class
	 * for Linux. Reads also the <code>GAliasTable </code> and caches it for subsequent accesses.
	 * @param file - the file's name (including the path) of the Warehouse
	 * @throws MDBException if a database access error occurs or the Warehouse format is different
	 * from the expected schema
	 * @author Stefano Orlando
	 */
    public MDBWarehouse(File file) throws MDBException {
        osName = System.getProperty("os.name");
        String databaseURL = null;
        tempHSQLDB = null;
        driver = null;
        if (osName.toLowerCase(Locale.ENGLISH).startsWith("windows")) {
            databaseURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + file.getAbsolutePath().replace('\\', '/').trim() + ";DriverID=22;READONLY=true}";
            driver = "sun.jdbc.odbc.JdbcOdbcDriver";
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new MDBException("JDBC-ODBC_bridge_not_found_error");
            }
            try {
                conAccessDBConnection = DriverManager.getConnection(databaseURL, "", "");
            } catch (SQLException ex) {
                if (ex.getSQLState().equalsIgnoreCase("S1000")) throw new MDBException("db_conn_error"); else throw new MDBException("unexpected_SQL_error", ex);
            }
        } else if (osName.toLowerCase(Locale.ENGLISH).startsWith("linux")) {
            File tempFile = null;
            driver = "org.hsqldb.jdbcDriver";
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                throw new MDBException("HSQLDB_engine_driver_not_found_error");
            }
            try {
                tempFile = File.createTempFile("temp", ".tmp");
                tempHSQLDB = tempFile.getName().substring(0, tempFile.getName().lastIndexOf('.'));
                tempHSQLDB = "\\tmp\\".replace('\\', '/').trim() + tempHSQLDB;
                tempFile.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                conAccessDBConnection = DriverManager.getConnection("jdbc:hsqldb:" + tempHSQLDB, "sa", "");
                conAccessDBConnection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new MDBException("unexpected_SQL_error", ex);
            }
            MDBtoHSQLDB process = new MDBtoHSQLDB(file, conAccessDBConnection);
            if (process.canStart()) {
                process.start();
                PluginServices.getMDIManager().addWindow(process);
            }
            if (process.getResult() == false) {
                try {
                    Statement st = conAccessDBConnection.createStatement();
                    st.execute("SHUTDOWN");
                    conAccessDBConnection.close();
                    throw new MDBException("aborted_MDBtoHSQLDB");
                } catch (SQLException ex) {
                    throw new MDBException("unexpected_SQL_error", ex);
                } finally {
                    File f1 = new File(tempHSQLDB + ".properties");
                    f1.delete();
                    f1 = null;
                    File f2 = new File(tempHSQLDB + ".script");
                    f2.delete();
                    f2 = null;
                    File f3 = new File(tempHSQLDB + ".data");
                    f3.delete();
                    f3 = null;
                    File f4 = new File(tempHSQLDB + ".backup");
                    f4.delete();
                    f4 = null;
                    File f5 = new File(tempHSQLDB + ".log");
                    f5.delete();
                    f5 = null;
                }
            }
        } else throw new MDBException("OS_not_supported_error");
        Statement s = null;
        ResultSet rs = null;
        try {
            s = conAccessDBConnection.createStatement();
            s.execute("SELECT TableType,TableName FROM GAliasTable");
            rs = s.getResultSet();
            if ((rs == null) || (rs.next() == false)) throw new MDBException("format_error");
            mapGAliasTable = new HashMap<String, String>();
            do {
                mapGAliasTable.put(rs.getString("TableType"), rs.getString("TableName"));
            } while (rs.next());
            if (mapGAliasTable.isEmpty() || mapGAliasTable.get("INGRFieldLookup") == null || mapGAliasTable.get("INGRGeometryProperties") == null || mapGAliasTable.get("INGRFeatures") == null || mapGAliasTable.get("GCoordSystemTable") == null) throw new MDBException("format_error");
            mdbFile = file;
            xMin = xMax = yMin = yMax = 0.0d;
            geometryAdapter = new MDBGeometryAdapter();
        } catch (SQLException ex) {
            close();
            if (ex.getSQLState().equalsIgnoreCase("S0002") || ex.getSQLState().equalsIgnoreCase("S0022") || ex.getSQLState().equalsIgnoreCase("07001")) throw new MDBException("format_error"); else throw new MDBException("unexpected_SQL_error", ex);
        } finally {
            try {
                if (s != null) s.close();
            } catch (SQLException ex) {
                throw new MDBException("unexpected_SQL_error", ex);
            }
        }
    }

    /**
	 * Returns the geometry type's name of a Feature Class, knowing its code.
	 * @see getGeometryType
	 * @param geometryType - the code of the geometry type
	 * @return "Line", "Area", "Compound", "Image", "Point", "Text", "None" or <code>null</code>
	 * if the geometry type is unknown
	 * @author Stefano Orlando
	 */
    private String decodeGeometryType(int geometryType) {
        switch(geometryType) {
            case 1:
                return "Line";
            case 2:
                return "Area";
            case 3:
                return "Compound";
            case 4:
                return "Image";
            case 10:
                return "Point";
            case 5:
                return "Text";
            case -1:
                return "None";
            default:
                return null;
        }
    }

    /**
	* Retrieves the geometry type's code of a Feature Class.
	* @param featureName - the Feature Class to query
	* @return the geometry type's code of the Feature Class (-1: None; 1: Line; 2: Area; 3: Compound; 4: Image; 5: Text; 10: Point) or 0 if the geometry type is unknown
	* @throws MDBException if a database access error occurs or the Warehouse format is different
	* from the expected schema
	* @author Stefano Orlando
	*/
    private int getGeometryType(String featureName) throws MDBException {
        int geometryType = 0;
        String tblFieldLookUp = mapGAliasTable.get("INGRFieldLookup").toString();
        String tblGeometryProperties = mapGAliasTable.get("INGRGeometryProperties").toString();
        Statement s1 = null;
        Statement s2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try {
            s1 = conAccessDBConnection.createStatement();
            s2 = conAccessDBConnection.createStatement();
            s1.execute("SELECT " + tblFieldLookUp + ".FeatureName," + tblGeometryProperties + ".GeometryType " + "FROM " + tblFieldLookUp + " INNER JOIN " + tblGeometryProperties + " " + "ON " + tblFieldLookUp + ".IndexID = " + tblGeometryProperties + ".IndexID " + "WHERE " + tblFieldLookUp + ".FeatureName = '" + featureName + "'");
            s2.execute("SELECT GeometryType,PrimaryGeometryFieldName " + "FROM " + mapGAliasTable.get("INGRFeatures").toString() + " " + "WHERE FeatureName = '" + featureName + "'");
            rs1 = s1.getResultSet();
            rs2 = s2.getResultSet();
            if (rs1 == null) throw new MDBException("format_error");
            if (rs1.next() == false) {
                if ((rs2 == null) || (rs2.next() == false)) throw new MDBException("format_error");
                geometryType = rs2.getInt("GeometryType");
                if (geometryType != -1) throw new MDBException("format_error");
            } else geometryType = rs1.getInt("GeometryType");
            return geometryType;
        } catch (SQLException ex) {
            if (ex.getSQLState().equalsIgnoreCase("S0002") || ex.getSQLState().equalsIgnoreCase("S0022") || ex.getSQLState().equalsIgnoreCase("07001")) throw new MDBException("format_error"); else throw new MDBException("unexpected_SQL_error", ex);
        } finally {
            try {
                if (s2 != null) s2.close();
                if (s1 != null) s1.close();
            } catch (SQLException ex) {
                throw new MDBException("unexpected_SQL_error", ex);
            }
        }
    }

    /**
	 * Closes the connection to the underlying database file of the Warehouse
	 * or the temporary HSQLDB database (also erasing it).
	 * @throws MDBException if a database access error occurs
	 * @author Stefano Orlando
	 */
    public void close() throws MDBException {
        try {
            if (conAccessDBConnection != null) if (conAccessDBConnection.isClosed()) return;
            if (driver.equalsIgnoreCase("org.hsqldb.jdbcDriver")) {
                Statement st = conAccessDBConnection.createStatement();
                st.execute("SHUTDOWN");
            }
            if (conAccessDBConnection != null) conAccessDBConnection.close();
        } catch (SQLException ex) {
            throw new MDBException("unexpected_SQL_error", ex);
        } finally {
            geom = null;
            env = null;
            if (driver.equalsIgnoreCase("org.hsqldb.jdbcDriver")) {
                File f1 = new File(tempHSQLDB + ".properties");
                f1.delete();
                f1 = null;
                File f2 = new File(tempHSQLDB + ".script");
                f2.delete();
                f2 = null;
                File f3 = new File(tempHSQLDB + ".data");
                f3.delete();
                f3 = null;
                File f4 = new File(tempHSQLDB + ".backup");
                f4.delete();
                f4 = null;
                File f5 = new File(tempHSQLDB + ".log");
                f5.delete();
                f5 = null;
            }
        }
    }

    /**
	 * Returns the name of the Warehouse (i.e. the name - without the path - of the underlying
	 * database file of the Warehouse).
	 * @return the Warehouse's name
	 * @author Stefano Orlando
	 */
    public String getName() {
        return mdbFile.getName();
    }

    /**
	 * Returns the database file of the Warehouse.
	 * @return the Warehouse's file
	 * @author Stefano Orlando
	 */
    public File getFile() {
        return mdbFile;
    }

    /**
	 * Retrieves the <code>GFeatures</code> table of the Warehouse.
	 * @return two-dimensional array of strings representing the <code>GFeatures</code> table of the
	 * Warehouse, using the following column order:
	 * <code>|FeatureName|GeometryType|PrimaryGeometryFieldName|FeatureDescription|</code>
	 * @throws MDBException if a database access error occurs or the Warehouse format is different
	 * from the expected schema
	 * @author Stefano Orlando
	 */
    public String[][] getFeatureClassTable() throws MDBException {
        Statement s1 = null;
        ResultSet rs1 = null;
        Statement s2 = null;
        ResultSet rs2 = null;
        try {
            s1 = conAccessDBConnection.createStatement();
            s2 = conAccessDBConnection.createStatement();
            s1.execute("SELECT COUNT(*) FROM " + mapGAliasTable.get("INGRFeatures").toString());
            s2.execute("SELECT FeatureName,GeometryType,PrimaryGeometryFieldName,FeatureDescription FROM " + mapGAliasTable.get("INGRFeatures").toString());
            rs1 = s1.getResultSet();
            if ((rs1 == null) || (rs1.next() == false)) throw new MDBException("format_error");
            int rowNumber = rs1.getInt(1);
            if (rowNumber == 0) {
                return null;
            }
            rs2 = s2.getResultSet();
            if ((rs2 == null) || (rs2.next() == false)) throw new MDBException("format_error");
            String[][] tabGFeatures = new String[rowNumber][4];
            int rowCount = 0;
            do {
                String featureName = rs2.getString("FeatureName");
                String primaryGeometryFieldName = rs2.getString("PrimaryGeometryFieldName");
                String sGeometryType = rs2.getString("GeometryType");
                int geometryType;
                try {
                    geometryType = Integer.parseInt(sGeometryType);
                } catch (NumberFormatException e) {
                    throw new MDBException("format_error");
                }
                String featureDescription = rs2.getString("FeatureDescription");
                tabGFeatures[rowCount][0] = featureName;
                if ((primaryGeometryFieldName == null) || (primaryGeometryFieldName.length() == 0)) {
                    if (geometryType == -1) {
                        tabGFeatures[rowCount][1] = decodeGeometryType(-1);
                    } else {
                        tabGFeatures[rowCount][1] = decodeGeometryType(getGeometryType(featureName));
                    }
                } else {
                    tabGFeatures[rowCount][1] = decodeGeometryType(geometryType);
                }
                tabGFeatures[rowCount][2] = featureDescription;
                tabGFeatures[rowCount][3] = null;
                rowCount++;
            } while (rs2.next() == true);
            return tabGFeatures;
        } catch (SQLException ex) {
            close();
            if (ex.getSQLState().equalsIgnoreCase("S0002") || ex.getSQLState().equalsIgnoreCase("S0022") || ex.getSQLState().equalsIgnoreCase("07001")) throw new MDBException("format_error"); else throw new MDBException("unexpected_SQL_error", ex);
        } finally {
            try {
                if (s1 != null) s1.close();
                if (s2 != null) s2.close();
            } catch (SQLException ex) {
                throw new MDBException("unexpected_SQL_error", ex);
            }
        }
    }

    /**
	 * Sets the current Feature Class to process. Must be called before other methods that require a current
	 * Feature Class to be set.
	 * @param featureClassName - the name of the Feature Class
	 * @throws MDBException if a database access error occurs or the Warehouse format is different
	 * from the expected schema
	 * @author Stefano Orlando
	 */
    public void setCurrentFeatureClass(String featureClassName) throws MDBException {
        currentFeatureClass = featureClassName;
        currentFeatureClassColNames = null;
        currentFeatureClassColJavaTypes = null;
        currentFeatureClassAttrTable = null;
        currentFeatureClassGeometry = null;
        currentFeatureClassGeometryType = 0;
        Statement s1 = null;
        Statement s2 = null;
        Statement s3 = null;
        Statement s4 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        try {
            s1 = conAccessDBConnection.createStatement();
            s2 = conAccessDBConnection.createStatement();
            s3 = conAccessDBConnection.createStatement();
            s4 = conAccessDBConnection.createStatement();
            currentFeatureClassGeometryType = getGeometryType(currentFeatureClass);
            String tblFieldLookUp = mapGAliasTable.get("INGRFieldLookup").toString();
            String tblGeometryProperties = mapGAliasTable.get("INGRGeometryProperties").toString();
            String tblGCoordSystem = mapGAliasTable.get("GCoordSystemTable").toString();
            if (currentFeatureClassGeometryType != -1) {
                s1.execute("SELECT FieldName FROM " + tblFieldLookUp + " INNER JOIN " + tblGeometryProperties + " " + "ON " + tblFieldLookUp + ".IndexID = " + tblGeometryProperties + ".IndexID " + "WHERE FeatureName = '" + currentFeatureClass + "'");
                rs1 = s1.getResultSet();
                if ((rs1 == null) || (rs1.next() == false)) throw new MDBException("format_error");
                currentFeatureClassGeomField = rs1.getString("FieldName");
            }
            s2.execute("SELECT COUNT(*) FROM " + currentFeatureClass);
            s3.execute("SELECT * FROM " + currentFeatureClass);
            rs2 = s2.getResultSet();
            if ((rs2 == null) || (rs2.next() == false)) throw new MDBException("format_error");
            int rowNumber = rs2.getInt(1);
            rs3 = s3.getResultSet();
            if (rs3 == null) throw new MDBException("format_error");
            ResultSetMetaData rsm3 = rs3.getMetaData();
            int colNumber = rsm3.getColumnCount();
            if (colNumber < 1 && currentFeatureClassGeometryType == -1) throw new MDBException("format_error");
            if (colNumber < 2 && currentFeatureClassGeometryType != -1) throw new MDBException("format_error");
            if (currentFeatureClassGeometryType != -1) {
                if (driver.equalsIgnoreCase("sun.jdbc.odbc.JdbcOdbcDriver")) {
                    s4.execute("SELECT " + tblGCoordSystem + ".BaseStorageType," + tblGCoordSystem + ".Stor2CompMatrix1," + tblGCoordSystem + ".Stor2CompMatrix6," + tblGCoordSystem + ".Stor2CompMatrix11," + tblGCoordSystem + ".Stor2CompMatrix13," + tblGCoordSystem + ".Stor2CompMatrix14," + tblGCoordSystem + ".Stor2CompMatrix15 " + "FROM (" + tblFieldLookUp + " INNER JOIN " + tblGeometryProperties + " " + "ON " + tblFieldLookUp + ".IndexID = " + tblGeometryProperties + ".IndexID) " + "INNER JOIN " + tblGCoordSystem + " " + "ON " + tblGeometryProperties + ".GCoordSystemGUID = " + tblGCoordSystem + ".CSGUID " + "WHERE " + tblFieldLookUp + ".FeatureName = '" + currentFeatureClass + "'");
                }
                if (driver.equalsIgnoreCase("org.hsqldb.jdbcDriver")) {
                    s4.execute("SELECT " + tblGCoordSystem + ".BaseStorageType," + tblGCoordSystem + ".Stor2CompMatrix1," + tblGCoordSystem + ".Stor2CompMatrix6," + tblGCoordSystem + ".Stor2CompMatrix11," + tblGCoordSystem + ".Stor2CompMatrix13," + tblGCoordSystem + ".Stor2CompMatrix14," + tblGCoordSystem + ".Stor2CompMatrix15 " + "FROM (SELECT " + tblGeometryProperties + ".GCoordSystemGUID AS one, " + tblFieldLookUp + ".FeatureName AS two FROM " + tblFieldLookUp + " INNER JOIN " + tblGeometryProperties + " " + "ON " + tblFieldLookUp + ".IndexID = " + tblGeometryProperties + ".IndexID) " + "INNER JOIN " + tblGCoordSystem + " " + "ON one = " + tblGCoordSystem + ".CSGUID " + "WHERE two = '" + currentFeatureClass + "'");
                }
                rs5 = s4.getResultSet();
                if ((rs5 == null) || (rs5.next() == false)) throw new MDBException("format_error");
            }
            ArrayList<String> currentFCColNames = new ArrayList<String>();
            ArrayList<Class<?>> currentFCColJavaTypes = new ArrayList<Class<?>>();
            ArrayList<Integer> currentFCColSQLTypes = new ArrayList<Integer>();
            int currentFeatureClassGeomFieldPos = 0;
            int currentFeatureClassSpatialIndexFieldPos = 0;
            currentFeatureClassSpatialIndexField = null;
            for (int i = 0; i < colNumber; i++) {
                String fieldName = rsm3.getColumnName(i + 1);
                if (fieldName.equalsIgnoreCase(currentFeatureClassGeomField)) {
                    currentFeatureClassGeomFieldPos = i + 1;
                    continue;
                }
                if (fieldName.equalsIgnoreCase(currentFeatureClassGeomField + "_sk")) {
                    currentFeatureClassSpatialIndexFieldPos = i + 1;
                    currentFeatureClassSpatialIndexField = fieldName;
                    continue;
                }
                currentFCColNames.add(fieldName);
                currentFCColJavaTypes.add(getJavaTypeClass(rsm3.getColumnType(i + 1)));
                currentFCColSQLTypes.add(rsm3.getColumnType(i + 1));
            }
            currentFeatureClassColNames = new String[currentFCColNames.size()];
            currentFeatureClassColJavaTypes = new Class[currentFCColJavaTypes.size()];
            currentFeatureClassColSQLTypes = new int[currentFCColSQLTypes.size()];
            int index = 0;
            for (String x : currentFCColNames) {
                currentFeatureClassColNames[index++] = x.toString();
            }
            index = 0;
            for (Class x : currentFCColJavaTypes) {
                currentFeatureClassColJavaTypes[index++] = x.getClass();
            }
            index = 0;
            for (Integer x : currentFCColSQLTypes) {
                currentFeatureClassColSQLTypes[index++] = x.intValue();
            }
            if (rowNumber != 0) {
                currentFeatureClassAttrTable = new Object[rowNumber][currentFeatureClassColNames.length];
                currentFeatureClassGeometry = new Object[rowNumber];
                currentFeatureClassSpatialIndex = new Object[rowNumber];
                while (rs3.next()) {
                    int j = 0;
                    for (int i = 0; i < colNumber; i++) {
                        if (currentFeatureClassGeomFieldPos == i + 1) {
                            currentFeatureClassGeometry[rs3.getRow() - 1] = rs3.getObject(i + 1);
                        } else if (currentFeatureClassSpatialIndexFieldPos == i + 1) {
                            currentFeatureClassSpatialIndex[rs3.getRow() - 1] = rs3.getObject(i + 1);
                        } else {
                            currentFeatureClassAttrTable[rs3.getRow() - 1][j] = rs3.getObject(i + 1);
                            j++;
                        }
                    }
                    if (currentFeatureClassGeomFieldPos == 0) {
                        if (currentFeatureClassGeomField != null) throw new MDBException("format_error");
                        currentFeatureClassGeometry[rs3.getRow() - 1] = null;
                    }
                    if (currentFeatureClassSpatialIndexFieldPos == 0) {
                        currentFeatureClassSpatialIndex[rs3.getRow() - 1] = null;
                    }
                }
            }
            currentFeatureClassPKFields = new int[currentFeatureClassColNames.length];
            DatabaseMetaData meta = conAccessDBConnection.getMetaData();
            rs4 = meta.getIndexInfo(null, null, currentFeatureClass, true, true);
            while (rs4.next()) {
                String indexName = rs4.getString(6);
                if (indexName != null) {
                    String keyColName = rs4.getString(9);
                    int keyColSeq = rs4.getInt(8);
                    for (int i = 0; i < currentFeatureClassColNames.length; i++) {
                        if (currentFeatureClassColNames[i].equalsIgnoreCase(keyColName)) currentFeatureClassPKFields[i] = keyColSeq;
                    }
                }
            }
            if (currentFeatureClassGeometryType != -1) {
                currentFeatureClassCoordSystemType = rs5.getByte("BaseStorageType");
                currentFeatureClassHorStorageUnitX = rs5.getDouble("Stor2CompMatrix1");
                currentFeatureClassHorStorageUnitY = rs5.getDouble("Stor2CompMatrix6");
                currentFeatureClassVerStorageUnit = rs5.getDouble("Stor2CompMatrix11");
                currentFeatureClassStorageCenterX = rs5.getDouble("Stor2CompMatrix13");
                currentFeatureClassStorageCenterY = rs5.getDouble("Stor2CompMatrix14");
                currentFeatureClassStorageCenterZ = rs5.getDouble("Stor2CompMatrix15");
                geometryAdapter.setCoordTransData(currentFeatureClassCoordSystemType, currentFeatureClassHorStorageUnitX, currentFeatureClassHorStorageUnitY, currentFeatureClassVerStorageUnit, currentFeatureClassStorageCenterX, currentFeatureClassStorageCenterY, currentFeatureClassStorageCenterZ);
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equalsIgnoreCase("S0002") || ex.getSQLState().equalsIgnoreCase("S0022") || ex.getSQLState().equalsIgnoreCase("07001")) throw new MDBException("format_error"); else throw new MDBException("unexpected_SQL_error", ex);
        } finally {
            try {
                if (s1 != null) s1.close();
                if (s2 != null) s2.close();
                if (s3 != null) s3.close();
                if (rs4 != null) rs4.close();
                if (s4 != null) s4.close();
            } catch (SQLException ex) {
                throw new MDBException("unexpected_SQL_error", ex);
            }
        }
    }

    /**
	 * Returns the current Feature Class name.
	 * @return the name of the current Feature Class
	 * @author Stefano Orlando
	 */
    public String getCurrentFeatureClassName() {
        return currentFeatureClass;
    }

    /**
	 * Retrieves a JTS Geometry (3D) from the current Feature Class.
	 * @param index - index of the geometry to retrieve
	 * @return a JTS Geometry (3D) (could be <code>null</code>)
	 * @throws MDBException if the geometry can't be retrieved (i.e. malformed, unknown or unsupported)
	 * @author Stefano Orlando
	 */
    public Geometry getCurrentFeatureClassGeometry(int index) throws MDBException {
        try {
            geom = null;
            geom = geometryAdapter.deSerialize((byte[]) currentFeatureClassGeometry[index]);
            if (geom == null) return null;
            env = geom.getEnvelopeInternal();
            if (firstGeometry == true) {
                xMin = env.getMinX();
                yMin = env.getMinY();
                xMax = env.getMaxX();
                yMax = env.getMaxY();
                firstGeometry = false;
            } else {
                double xMinEnv = env.getMinX();
                double yMinEnv = env.getMinY();
                double xMaxEnv = env.getMinX();
                double yMaxEnv = env.getMaxY();
                if (xMin > xMinEnv) xMin = xMinEnv;
                if (yMin > yMinEnv) yMin = yMinEnv;
                if (xMax < xMaxEnv) xMax = xMaxEnv;
                if (yMax < yMaxEnv) yMax = yMaxEnv;
            }
            return geom;
        } catch (MDBAdapterException ex) {
            throw new MDBException(ex.getMessage());
        }
    }

    /**
	 * Retrieves the bounding box (Envelope) of a geometry of the current Feature Class.
	 * @param index - index of the geometry to query
	 * @return a Rectangle2D representing the bounds of the geometry (could be <code>null</code>)
	 * @throws MDBException if the geometry to query can't be retrieved (i.e. malformed, unknown or unsupported) 
	 * @author Stefano Orlando
	 */
    public Rectangle2D getCurrentFeatureClassGeometryEnvelope(int index) throws MDBException {
        try {
            geom = geometryAdapter.deSerialize((byte[]) currentFeatureClassGeometry[index]);
            if (geom == null) return null;
            env = geom.getEnvelopeInternal();
            double width = env.getWidth();
            double height = env.getHeight();
            if (width == 0.0d) width = 0.2d;
            if (height == 0.0d) height = 0.2d;
            return new Rectangle2D.Double(env.getMinX(), env.getMinY(), width, height);
        } catch (MDBAdapterException ex) {
            throw new MDBException(ex.getMessage());
        }
    }

    /**
	 * Returns the type (JTS Geometry) of a geometry of the current Feature Class.
	 * @param index - index of the geometry to query
	 * @return a JTS Geometry type (Point, LineString, Polygon, GeometryCollection, MultiLineString, MultiPolygon) or <code>null</code>
	 * @throws MDBException if the geometry to query can't be retrieved (i.e. malformed, unknown or unsupported)
	 * @see MDBGeometryAdapter class
	 * @author Stefano Orlando
	 */
    public Class getCurrentFeatureClassGeometryType(int index) throws MDBException {
        try {
            geom = geometryAdapter.deSerialize((byte[]) currentFeatureClassGeometry[index]);
            if (geom == null) return null;
            return geom.getClass();
        } catch (MDBAdapterException ex) {
            throw new MDBException(ex.getMessage());
        }
    }

    /**
	 * Returns the spatial index associated to a geometry of the current Feature Class.
	 * @param index - index of the geometry to query
	 * @return a string representing the spatial index associated to the geometry (could
	 * be <code>null</code> if the spatial index is not present)
	 * @author Stefano Orlando
	 */
    public String getCurrentFeatureClassSpatialIndex(int index) {
        return currentFeatureClassSpatialIndex[index].toString();
    }

    /**
	 * Returns the data row associated to a geometry of the current Feature Class. The original data types must
	 * be retrieved using the <code>getCurrentFeatureClassColJavaTypes</code> or <code>getCurrentFeatureClassColSQLTypes</code>
	 * methods, as well as the name of the columns using the <code>getCurrentFeatureClassColNames</code> method.
	 * @param index - index of the geometry to query
	 * @return array of objects containing the data
	 * @author Stefano Orlando
	 */
    public Object[] getCurrentFeatureClassAttributes(int index) {
        return currentFeatureClassAttrTable[index];
    }

    /**
	 * Returns the columns' name of the data table of the current Feature Class.
	 * @return array of strings representing the columns' name of the data table
	 * @author Stefano Orlando
	 */
    public String[] getCurrentFeatureClassColNames() {
        return currentFeatureClassColNames;
    }

    /**
	 * Returns the name of the geometry field of the current Feature Class.
	 * @return the geometry field's name
	 * @author Stefano Orlando
	 */
    public String getCurrentFeatureClassGeomColName() {
        return currentFeatureClassGeomField;
    }

    /**
	 * Returns the sequence indexes of the primary key of the current Feature Class
	 * (1 for the first column of the primary key, 2 for the second column and so on;
	 * 0 if a column isn't part of the primary key). The indexes correspond with the
	 * column names returned by the <code>getCurrentFeatureClassColNames()</code> method.
	 * @return array of indexes of the primary key
	 * @author Stefano Orlando
	 */
    public int[] getCurrentFeatureClassPKColIndex() {
        return currentFeatureClassPKFields;
    }

    /**
	 * Returns the columns' types of the data table of the current Feature Class.
	 * @return array of Java types of the columns of the data table
	 * @author Stefano Orlando
	 */
    public Class[] getCurrentFeatureClassColJavaTypes() {
        return currentFeatureClassColJavaTypes;
    }

    /**
	 * Returns the columns' types of the data table of the current Feature Class.
	 * @return array of SQL types of the columns of the data table
	 * @author Stefano Orlando
	 */
    public int[] getCurrentFeatureClassColSQLTypes() {
        return currentFeatureClassColSQLTypes;
    }

    /**
	 * Returns the geometry type of the current Feature Class.
	 * @return the geometry type's code of the current Feature Class (-1: None; 1: Line; 2: Area;
	 * 3: Compound; 4: Image; 5: Text; 10: Point) or 0 if the geometry type is unknown
	 * @see getGeometryType
	 * @author Stefano Orlando
	 */
    public int getCurrentFeatureClassGeometryType() {
        return currentFeatureClassGeometryType;
    }

    /**
	 * Returns the number of records in the current Feature Class.
	 * @return the geometries' number (0 if the current Feature Class is empty)
	 * @author Stefano Orlando
	 */
    public int getCurrentFeatureClassGeometryCount() {
        if (currentFeatureClassAttrTable == null) return 0;
        return currentFeatureClassAttrTable.length;
    }

    /**
	 * Retrieves the extents of the current Feature Class as a <code>Rectangle2D</code>.
	 * If the private variable <code>fullScan</code> is set to <code>true</code> the
	 * entire Feature Class is scanned to get the extents; if <code>fullScan</code> is
	 * set to false, the extents of the current Feature Class are progressively built
	 * calling <code>getCurrentFeatureClassGeometry</code>, so this method must be
	 * called only when all the geometries have been read.
	 * @return a Rectangle2D representing the extents of the current Feature Class
	 * @throws MDBException if a geometry can't be retrieved (i.e. malformed, unknown
	 * or unsupported)
	 * @author Stefano Orlando
	 */
    public Rectangle2D getCurrentFeatureClassExtent() throws MDBException {
        if (fullScan == true) {
            firstGeometry = true;
            int numGeometry = getCurrentFeatureClassGeometryCount();
            for (int i = 0; i < numGeometry; i++) {
                getCurrentFeatureClassGeometry(i);
            }
            double width = xMax - xMin;
            double height = yMax - yMin;
            double xUpperLeft = xMin;
            double yUpperLeft = yMin;
            if (width == 0.0d) {
                width = 0.2d;
                xUpperLeft = xMin - 0.1d;
            }
            if (height == 0.0d) {
                height = 0.2d;
                yUpperLeft = yMin - 0.1d;
            }
            return new Rectangle2D.Double(xUpperLeft, yUpperLeft, width, height);
        } else {
            double width = xMax - xMin;
            double height = yMax - yMin;
            double xUpperLeft = xMin;
            double yUpperLeft = yMin;
            if (width == 0.0d) {
                width = 0.2d;
                xUpperLeft = xMin - 0.1d;
            }
            if (height == 0.0d) {
                height = 0.2d;
                yUpperLeft = yMin - 0.1d;
            }
            return new Rectangle2D.Double(xUpperLeft, yUpperLeft, width, height);
        }
    }

    /**
	 * Converts a generic SQL type in a Java type.
	 * @param sqlType - a constant representing a SQL type as defined in <code>java.sql.Types</code>.
	 * @return a Java type corresponding to SQL type supplied
	 * @author Stefano Orlando
	 */
    private static Class getJavaTypeClass(int sqlType) {
        if (mapSqlTypes == null) {
            mapSqlTypes = new HashMap<Integer, Class<?>>();
            Field[] fields = java.sql.Types.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                Integer fieldValue = java.sql.Types.NULL;
                try {
                    fieldValue = (Integer) fields[i].get(null);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                Class fieldType;
                switch(fieldValue.intValue()) {
                    case java.sql.Types.CHAR:
                    case java.sql.Types.VARCHAR:
                    case java.sql.Types.LONGVARCHAR:
                        fieldType = java.lang.String.class;
                        break;
                    case java.sql.Types.NUMERIC:
                    case java.sql.Types.DECIMAL:
                        fieldType = java.math.BigDecimal.class;
                        break;
                    case java.sql.Types.BIT:
                    case java.sql.Types.BOOLEAN:
                        fieldType = java.lang.Boolean.class;
                        break;
                    case java.sql.Types.TINYINT:
                        fieldType = java.lang.Byte.class;
                        break;
                    case java.sql.Types.SMALLINT:
                        fieldType = java.lang.Short.class;
                        break;
                    case java.sql.Types.INTEGER:
                        fieldType = java.lang.Integer.class;
                        break;
                    case java.sql.Types.BIGINT:
                        fieldType = java.lang.Long.class;
                        break;
                    case java.sql.Types.REAL:
                        fieldType = java.lang.Float.class;
                        break;
                    case java.sql.Types.DOUBLE:
                    case java.sql.Types.FLOAT:
                        fieldType = java.lang.Double.class;
                        break;
                    case java.sql.Types.BINARY:
                    case java.sql.Types.VARBINARY:
                    case java.sql.Types.LONGVARBINARY:
                        fieldType = java.lang.Byte[].class;
                        break;
                    case java.sql.Types.DATE:
                        fieldType = java.sql.Date.class;
                        break;
                    case java.sql.Types.TIME:
                        fieldType = java.sql.Time.class;
                        break;
                    case java.sql.Types.TIMESTAMP:
                        fieldType = java.sql.Timestamp.class;
                        break;
                    case java.sql.Types.CLOB:
                        fieldType = java.sql.Clob.class;
                        break;
                    case java.sql.Types.BLOB:
                        fieldType = java.sql.Blob.class;
                        break;
                    default:
                        continue;
                }
                mapSqlTypes.put(fieldValue, fieldType);
            }
        }
        return (Class) mapSqlTypes.get(new Integer(sqlType));
    }
}
