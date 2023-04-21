package eu.mpower.framework.sensor.fsa.derby;

import eu.mpower.framework.sensor.fsa.types.Value;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is an integrated database used to make persistent data needed for a 
 * well performance
 * @author Sixto Franco Martinez
 * @version 0.1
 */
public class DerbyDB {

    private String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    private String dbName = "FSADB";

    private String connectionURL = "jdbc:derby:" + dbName + ";create=true";

    private Connection conn = null;

    private Statement s;

    private PreparedStatement ps;

    private String insertDevice = "insert into DEVICE(ID_SENSOR, ID_DEVICE,MAX_HISTORY) values (?,?,?)";

    private String insertLog = "insert into DEVICE_LOG(ID_SENSOR,TYPE_LOG, VALUE,TIME) values (?,?,?,?)";

    private String insertType = "insert into TYPES(NAME,DESCRIPTION) values (?,?)";

    private String NValues = "select count(TYPE_LOG) from DEVICE_LOG where ID_SENSOR= ? and TYPE_LOG = ? ";

    private String MaxHistory = "select MAX_HISTORY from DEVICE where ID_SENSOR = ?";

    private String NValuesQuery = "select VALUE,TIME from DEVICE_LOG where ID_SENSOR= ? and TYPE_LOG = ? order by TIME desc";

    private String TimeValues = "select TIME from DEVICE_LOG where ID_SENSOR= ? and TYPE_LOG = ? ";

    private String delete = "delete from DEVICE_LOG where ID_SENSOR= ? and TYPE_LOG = ?  and  TIME = ?";

    private ResultSet myList;

    private String createTableDevices = "CREATE TABLE DEVICE " + "(ID_SENSOR VARCHAR(25) NOT NULL, " + " ID_DEVICE VARCHAR(25) NOT NULL, " + " MAX_HISTORY INTEGER NOT NULL," + " PRIMARY KEY(ID_SENSOR))";

    private String createTableLogs = "CREATE TABLE DEVICE_LOG  " + "(ID_SENSOR VARCHAR(25) NOT NULL, " + " TYPE_LOG VARCHAR(25) NOT NULL, " + " VALUE VARCHAR(25) NOT NULL, " + " TIME VARCHAR(50) NOT NULL, " + " Constraint sensor FOREIGN KEY(ID_SENSOR)" + " references DEVICE(ID_SENSOR)," + " Constraint log FOREIGN KEY(TYPE_LOG)" + " references TYPES(NAME), " + " PRIMARY KEY(ID_SENSOR,TYPE_LOG,TIME))";

    private String createTableTypes = "CREATE TABLE TYPES  " + "(NAME VARCHAR(25) NOT NULL, " + " DESCRIPTION VARCHAR(100)," + " PRIMARY KEY(NAME))";

    /**
     * The constructor for class DerbyDB
     */
    public DerbyDB() {
        try {
            Class.forName(driver);
            System.out.println(driver + " loaded. ");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Connected to database " + dbName);
            s = conn.createStatement();
            if (!Chk4Table(conn)) {
                System.out.println(" . . . . creating tables");
                s.execute(createTableDevices);
                s.execute(createTableTypes);
                s.execute(createTableLogs);
            }
        } catch (SQLException se) {
            System.err.print("SQLexception: ");
            System.err.println(se.getMessage());
        }
    }

    /**
     * This method is used for checking whether tables are created in database or not
     * @param conTst
     * @throws SQLException
     */
    private boolean Chk4Table(Connection conTst) throws SQLException {
        boolean chk = true;
        boolean doCreate = false;
        try {
            Statement s = conTst.createStatement();
            s.execute("update DEVICE set ID_DEVICE = 'TEST ENTRY' where 1=3");
        } catch (SQLException sqle) {
            String theError = (sqle).getSQLState();
            if (theError.equals("42X05")) {
                return false;
            } else if (theError.equals("42X14") || theError.equals("42821")) {
                System.out.println("Chk4Table: Incorrect table definition. Drop table VALUE_LIST");
                throw sqle;
            } else {
                System.out.println("Chk4Table: Unhandled SQLException");
                throw sqle;
            }
        }
        return true;
    }

    /**
     * This method is used for inser new data into database
     * 
     * @param id_sensor ID from sensor which is inserting data
     * @param type Type of data that is being inserted
     * @param value Value of data
     * @param time Time when data has been collected
     */
    public void insertLog(String id_sensor, String type, String value, String time) {
        try {
            ps = conn.prepareStatement(NValues);
            ps.setString(1, id_sensor);
            ps.setString(2, type);
            myList = ps.executeQuery();
            myList.next();
            int n = myList.getInt(1);
            myList.close();
            ps = conn.prepareStatement(MaxHistory);
            ps.setString(1, id_sensor);
            myList = ps.executeQuery();
            myList.next();
            int Max_History = myList.getInt(1);
            myList.close();
            if (n < Max_History) {
                ps = conn.prepareStatement(insertLog);
                ps.setString(1, id_sensor);
                ps.setString(2, type);
                ps.setString(3, value);
                ps.setString(4, time);
                ps.executeUpdate();
            } else {
                ps = conn.prepareStatement(TimeValues);
                ps.setString(1, id_sensor);
                ps.setString(2, type);
                myList = ps.executeQuery();
                Date dateold = new Date(System.currentTimeMillis());
                Date dateaux = new Date(System.currentTimeMillis());
                String Delete_Date = "";
                while (myList.next()) {
                    dateaux.setTime(new Long(myList.getString(1)).longValue());
                    if (dateold.after(dateaux)) {
                        dateold = dateaux;
                        Delete_Date = myList.getString(1);
                    }
                }
                myList.close();
                ps = conn.prepareStatement(delete);
                ps.setString(1, id_sensor);
                ps.setString(2, type);
                ps.setString(3, Delete_Date);
                ps.executeUpdate();
                ps = conn.prepareStatement(insertLog);
                ps.setString(1, id_sensor);
                ps.setString(2, type);
                ps.setString(3, value);
                ps.setString(4, time);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.err.print("SQLexception INSERT LOG: ");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * This method insert a new device into the database.
     * 
     * @param ID_Sensor
     * @param ID_Device
     * @param max
     */
    public void insertDevice(String ID_Sensor, String ID_Device, int max) {
        try {
            ps = conn.prepareStatement(insertDevice);
            ps.setString(1, ID_Sensor);
            ps.setString(2, ID_Device);
            ps.setInt(3, max);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.print("SQLexception INSERT DEVICE: ");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * This method insert a new type of sensor's value.
     * @param name The name of this type.
     * @param description The description of the new type.
     * 
     */
    public void insertType(String name, String description) {
        try {
            ps = conn.prepareStatement(insertType);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.print("SQLexception INSERT TYPE: ");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * This method is used for fetching information from database
     * 
     * @param id_sensor ID of sensor which information want to be consulted
     * @param type Type of data of sensor to be consulted
     * @param n Number of values to be consulted
     * @return A list with result of the consult to the database
     */
    public List QueryLog(String id_sensor, String type, int n) {
        try {
            List list = new ArrayList<Value>();
            Value val;
            Date date;
            ps = conn.prepareStatement(NValues);
            ps.setString(1, id_sensor);
            ps.setString(2, type);
            myList = ps.executeQuery();
            myList.next();
            int nvalues = myList.getInt(1);
            myList.close();
            if (nvalues < n) {
                ps = conn.prepareStatement(NValuesQuery);
                ps.setString(1, id_sensor);
                ps.setString(2, type);
                myList = ps.executeQuery();
                while (myList.next()) {
                    val = new Value();
                    val.setVal(myList.getString(1));
                    date = new Date(Long.valueOf(myList.getString(2)));
                    val.setTime(date.toString());
                    list.add(val);
                }
                myList.close();
            } else {
                ps = conn.prepareStatement(NValuesQuery);
                ps.setString(1, id_sensor);
                ps.setString(2, type);
                myList = ps.executeQuery();
                int i = 0;
                while (myList.next() && i < n) {
                    val = new Value();
                    val.setVal(myList.getString(1));
                    date = new Date(Long.valueOf(myList.getString(2)));
                    val.setTime(date.toString());
                    list.add(val);
                    i++;
                }
                myList.close();
            }
            return list;
        } catch (SQLException ex) {
            System.err.print("SQLexception Query: ");
            System.err.println(ex.getMessage());
            return null;
        }
    }

    /**
     * This method is used to check if a type of data already exist into the database
     * 
     * @param type Type of data to be checked
     * @return True if already exist, false otherwise
     */
    public boolean existType(String type) {
        try {
            ps = conn.prepareStatement("Select count(NAME) from TYPES where NAME like ?");
            ps.setString(1, type);
            myList = ps.executeQuery();
            myList.next();
            if (myList.getInt(1) == 1) {
                myList.close();
                return true;
            } else {
                myList.close();
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DerbyDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * This method is used to check if a device already exist into the database
     * 
     * @param idsensor Device to be checked
     * @return True if already exist, false otherwise
     */
    public boolean existDevice(String idsensor) {
        try {
            ps = conn.prepareStatement("Select count(ID_SENSOR) from DEVICE where ID_SENSOR like ?");
            ps.setString(1, idsensor);
            myList = ps.executeQuery();
            myList.next();
            if (myList.getInt(1) == 1) {
                myList.close();
                return true;
            } else {
                myList.close();
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DerbyDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * This method is used to get a list of all devices declared into the database
     *
     * @return List of all devices inserted into the database
     */
    public List TDevices() {
        try {
            List l = new ArrayList();
            ps = conn.prepareStatement("select * from DEVICE ");
            myList = ps.executeQuery();
            while (myList.next()) {
                l.add("issensor: " + myList.getString(1) + "iddevice: " + myList.getString(2) + "maxHistory: " + myList.getInt(3));
            }
            return l;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * This method is used to get a list of all types of data declared into the database
     *
     * @return List of all types of data inserted into the database
     */
    public List TTypes() {
        try {
            List l = new ArrayList();
            ps = conn.prepareStatement("select * from TYPES ");
            myList = ps.executeQuery();
            while (myList.next()) {
                l.add("name: " + myList.getString(1) + "descripvio: " + myList.getString(2));
            }
            return l;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**a
     * This method is used to get a list of data declared into the database
     *
     * @return List of all data inserted into the database
     */
    public List TDevicesLog() {
        try {
            List l = new ArrayList();
            ps = conn.prepareStatement("select * from DEVICE_LOG ");
            myList = ps.executeQuery();
            while (myList.next()) {
                l.add("issensor: " + myList.getString(1) + "type: " + myList.getString(2) + "valor: " + myList.getString(3) + "time: " + myList.getString(4));
            }
            return l;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
