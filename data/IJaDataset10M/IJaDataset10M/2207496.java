package net.sourceforge.mords.appt.store;

import com.tdcs.appt.obj.Appointment;
import net.sourceforge.mords.store.data.ConnectionPool;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author david
 */
public class ApptDb extends UnicastRemoteObject implements AppointmentServer {

    private ConnectionPool cp;

    private Properties props;

    private static Logger log;

    private static String clazz;

    /** Creates a new instance of ApptDb */
    public ApptDb() throws RemoteException {
        super();
        try {
            checkLogging();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLogging() throws Exception {
        if (log == null) {
            clazz = getClass().getName();
            String logFile = "ApptDb.log";
            FileHandler handler = new FileHandler(logFile, true);
            handler.setFormatter(new SimpleFormatter());
            log = Logger.getLogger("com.tdcs.appt.store.ApptDb");
        }
    }

    public void init(Properties props) throws Exception {
        this.props = props;
        cp = new ConnectionPool(props);
    }

    private Appointment makeAppt(ResultSet rs) throws SQLException {
        Appointment appt = new Appointment();
        appt.setId(rs.getInt("ID"));
        appt.setRecord(rs.getString("RECORD"));
        appt.setPhysician(rs.getString("PHYSICIAN"));
        appt.setRoom(rs.getString("ROOM"));
        appt.setComplaint(rs.getString("PRIMARY_COMPLAINT"));
        appt.setSecondaryNeed(rs.getString("SECONDARY_NEED"));
        appt.setStatus(rs.getString("STATUS"));
        appt.setContact(rs.getString("CONTACT"));
        appt.setFacility(rs.getString("FACILITY"));
        appt.setTimes(rs.getInt("YEAR"), rs.getInt("MONTH"), rs.getInt("DAY"), rs.getInt("START_HOUR"), rs.getInt("START_MINUTE"), rs.getInt("DURATION"));
        return appt;
    }

    public Map<Integer, String> listAppointments(Date day) throws RemoteException {
        String sql = "SELECT ID,RECORD FROM APPOINTMENTS WHERE YEAR = ? AND MONTH = ? AND DAY = ? " + "ORDER BY START_HOUR,START_MINUTE ASC";
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        try {
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            Calendar cal = Calendar.getInstance();
            cal.setTime(day);
            stmt.setInt(1, cal.get(Calendar.YEAR));
            stmt.setInt(2, cal.get(Calendar.MONTH));
            stmt.setInt(3, cal.get(Calendar.DAY_OF_MONTH));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(new Integer(rs.getInt("ID")), rs.getString("RECORD"));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem listing appointments for " + day.toString(), e);
        }
        return map;
    }

    public Collection<Appointment> getAppointments(Date day) throws RemoteException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE YEAR = ? AND MONTH = ? AND DAY = ? " + "ORDER BY START_HOUR,START_MINUTE ASC";
        Vector<Appointment> v = new Vector<Appointment>();
        try {
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            Calendar cal = Calendar.getInstance();
            cal.setTime(day);
            stmt.setInt(1, cal.get(Calendar.YEAR));
            stmt.setInt(2, cal.get(Calendar.MONTH));
            stmt.setInt(3, cal.get(Calendar.DAY_OF_MONTH));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                v.add(makeAppt(rs));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem loading appointments for " + day.toString(), e);
        }
        return v;
    }

    public Appointment getAppointment(int id) throws RemoteException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE ID = ?";
        Appointment appt = null;
        try {
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                appt = makeAppt(rs);
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem getting Appt #" + Integer.toString(id), e);
        }
        return appt;
    }

    public Appointment getAppointment(Integer id) throws RemoteException {
        return getAppointment(id.intValue());
    }

    public Collection<Appointment> getAppointments(String record) throws RemoteException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE RECORD = ? " + "ORDER BY YEAR,MONTH,DAY,START_HOUR,START_MINUTE DESC";
        Vector<Appointment> v = new Vector<Appointment>();
        try {
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, record);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                v.add(makeAppt(rs));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem loading appointments for " + record, e);
        }
        return v;
    }

    public void add(Appointment appt) throws RemoteException {
        String sql = "INSERT INTO APPOINTMENTS(" + "RECORD,PHYSICIAN,ROOM,PRIMARY_COMPLAINT,SECONDARY_NEED,STATUS," + "YEAR,MONTH,DAY,START_HOUR,START_MINUTE,DURATION,CONTACT,FACILITY) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(appt.getDateTime());
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, appt.getRecord());
            stmt.setString(2, appt.getPhysician());
            stmt.setString(3, appt.getRoom());
            stmt.setString(4, appt.getComplaint());
            stmt.setString(5, appt.getSecondaryNeed());
            stmt.setString(6, appt.getStatus());
            stmt.setInt(7, cal.get(Calendar.YEAR));
            stmt.setInt(8, cal.get(Calendar.MONTH));
            stmt.setInt(9, cal.get(Calendar.DAY_OF_MONTH));
            stmt.setInt(10, cal.get(Calendar.HOUR_OF_DAY));
            stmt.setInt(11, cal.get(Calendar.MINUTE));
            stmt.setInt(12, appt.getDuration());
            stmt.setString(13, appt.getContact());
            stmt.setString(14, appt.getFacility());
            stmt.executeUpdate();
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem adding Appointment", e);
        }
    }

    public void update(Appointment appt) throws RemoteException {
        String sql = "UPDATE APPOINTMENTS SET " + "RECORD = ?, " + "PHYSICIAN = ?, " + "ROOM = ?, " + "PRIMARY_COMPLAINT = ?, " + "SECONDARY_NEED = ?, " + "STATUS = ?, " + "YEAR = ?, " + "MONTH = ?, " + "DAY = ?, " + "START_HOUR = ?, " + "START_MINUTE = ?, " + "DURATION = ?, " + "CONTACT = ?, " + "FACILITY = ? " + "WHERE ID = ?";
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(appt.getDateTime());
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, appt.getRecord());
            stmt.setString(2, appt.getPhysician());
            stmt.setString(3, appt.getRoom());
            stmt.setString(4, appt.getComplaint());
            stmt.setString(5, appt.getSecondaryNeed());
            stmt.setString(6, appt.getStatus());
            stmt.setInt(7, cal.get(Calendar.YEAR));
            stmt.setInt(8, cal.get(Calendar.MONTH));
            stmt.setInt(9, cal.get(Calendar.DAY_OF_MONTH));
            stmt.setInt(10, cal.get(Calendar.HOUR_OF_DAY));
            stmt.setInt(11, cal.get(Calendar.MINUTE));
            stmt.setInt(12, appt.getDuration());
            stmt.setString(13, appt.getContact());
            stmt.setString(14, appt.getFacility());
            stmt.setInt(15, appt.getId());
            stmt.executeUpdate();
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem adding Appointment", e);
        }
    }

    public void delete(Appointment appt) throws RemoteException {
        String sql = "DELETE FROM APPOINTMENTS WHERE ID = ?";
        try {
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appt.getId());
            stmt.executeUpdate();
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem deleting appointment.", e);
        }
    }

    public Collection<Appointment> getAppointments(Date begin, Date end) throws RemoteException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE YEAR >= ? AND MONTH >= ? AND DAY >= ? " + "AND YEAR <= ? AND MONTH <= ? and DAY <= ? ORDER BY START_HOUR,START_MINUTE ASC";
        Vector<Appointment> v = new Vector<Appointment>();
        try {
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            Calendar cal = Calendar.getInstance();
            cal.setTime(begin);
            stmt.setInt(1, cal.get(Calendar.YEAR));
            stmt.setInt(2, cal.get(Calendar.MONTH));
            stmt.setInt(3, cal.get(Calendar.DAY_OF_MONTH));
            cal.setTime(end);
            stmt.setInt(4, cal.get(Calendar.YEAR));
            stmt.setInt(5, cal.get(Calendar.MONTH));
            stmt.setInt(6, cal.get(Calendar.DAY_OF_MONTH));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                v.add(makeAppt(rs));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem loading appointments for dates given", e);
        }
        return v;
    }

    public Collection<Appointment> getAppointments(Date begin, Date end, String status) throws RemoteException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE YEAR >= ? AND MONTH >= ? AND DAY >= ? " + "AND YEAR <= ? AND MONTH <= ? and DAY <= ? AND STATUS = ? ORDER BY START_HOUR,START_MINUTE ASC";
        Vector<Appointment> v = new Vector<Appointment>();
        try {
            Connection conn = cp.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            Calendar cal = Calendar.getInstance();
            cal.setTime(begin);
            stmt.setInt(1, cal.get(Calendar.YEAR));
            stmt.setInt(2, cal.get(Calendar.MONTH));
            stmt.setInt(3, cal.get(Calendar.DAY_OF_MONTH));
            cal.setTime(end);
            stmt.setInt(4, cal.get(Calendar.YEAR));
            stmt.setInt(5, cal.get(Calendar.MONTH));
            stmt.setInt(6, cal.get(Calendar.DAY_OF_MONTH));
            stmt.setString(7, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                v.add(makeAppt(rs));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem loading appointments for dates given", e);
        }
        return v;
    }

    public Collection<String> listPhysicians() throws RemoteException {
        String sql = "SELECT DISTINCT PHYSICIAN FROM APPOINTMENTS ORDER BY PHYSICIAN ASC";
        Vector<String> v = new Vector<String>();
        try {
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                v.add(rs.getString("PHYSICIAN"));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem listing.", e);
        }
        return v;
    }

    public Collection<String> listRooms() throws RemoteException {
        String sql = "SELECT DISTINCT ROOM FROM APPOINTMENTS ORDER BY ROOM ASC";
        Vector<String> v = new Vector<String>();
        try {
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                v.add(rs.getString("ROOM"));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem listing.", e);
        }
        return v;
    }

    public Collection<String> listStatuses() throws RemoteException {
        String sql = "SELECT DISTINCT STATUS FROM APPOINTMENTS ORDER BY STATUS ASC";
        Vector<String> v = new Vector<String>();
        try {
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                v.add(rs.getString("STATUS"));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem listing.", e);
        }
        return v;
    }

    public Collection<String> listFacilities() throws RemoteException {
        String sql = "SELECT DISTINCT FACILITY FROM APPOINTMENTS ORDER BY FACILITY ASC";
        Vector<String> v = new Vector<String>();
        try {
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                v.add(rs.getString("FACILITY"));
            }
            stmt.close();
            cp.releaseConnection(conn);
        } catch (Exception e) {
            throw new RemoteException("Problem listing facilities.", e);
        }
        return v;
    }
}
