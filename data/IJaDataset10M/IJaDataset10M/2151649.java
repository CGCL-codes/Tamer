package ro.gateway.aida.usr.messaging;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ro.gateway.aida.db.DBPersistenceManager;
import ro.gateway.aida.db.PersistenceToken;

/**
 *
 * @author Mihai Postelnicu<p>
 *
 *  e-mail (mihai@ro-gateway.org)<br>
 * (c) 2003 by eRomania Gateway<p>
 */
public class AlertDB extends DBPersistenceManager {

    private AlertDB(PersistenceToken token) {
        super(token);
    }

    public static AlertDB getManager(PersistenceToken token) {
        return new AlertDB(token);
    }

    public long add(Alert a) throws SQLException {
        if (getByIds(a.getTo(), a.getEventId()) != null) return -1;
        EventDB logdb = EventDB.getManager(token);
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(INSERT_ALERT);
            ps.setLong(1, a.getEventId());
            ps.setInt(2, a.getPriority());
            ps.setString(3, bool2Str(a.isByEmail()));
            ps.setInt(4, a.getAlertType());
            ps.setString(5, bool2Str(a.isDeleted()));
            ps.setDate(6, a.getRead());
            ps.setLong(7, a.getTo());
            ps.executeUpdate();
            ps.close();
            return a.getEventId();
        } finally {
            con.close();
        }
    }

    public Alert getByIds(long user_id, long event_id) throws SQLException {
        Connection con = getConnection();
        EventDB logdb = EventDB.getManager(token);
        Event l = logdb.getById(event_id);
        Alert a = null;
        try {
            PreparedStatement ps = con.prepareStatement(GET_ALERT);
            ps.setLong(1, user_id);
            ps.setLong(2, event_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a = new Alert(l);
                a.getParams().put("user_id", new Long(user_id));
                a.getParams().put("event_id", new Long(event_id));
                a.getParams().put("id", new Long(l.getSourceObjId()));
                a.setPriority(rs.getInt(1));
                a.setByEmail(str2Bool(rs.getString(2)));
                a.setAlertType(rs.getInt(3));
                a.setDeleted(str2Bool(rs.getString(4)));
                a.setRead(rs.getDate(5));
                a.setTo(user_id);
            }
            rs.close();
            ps.close();
            return a;
        } finally {
            con.close();
        }
    }

    public void markRead(long user_id, long event_id) throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(MARK_READ);
            ps.setLong(2, event_id);
            ps.setLong(3, user_id);
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.executeUpdate();
            ps.close();
        } finally {
            con.close();
        }
    }

    public void markDeleted(long user_id, long event_id) throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(MARK_DELETED);
            ps.setLong(1, event_id);
            ps.setLong(2, user_id);
            ps.executeUpdate();
            ps.close();
        } finally {
            con.close();
        }
    }

    public boolean update(Alert a) throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(UPDATE_ALERT);
            ps.setDate(1, a.getRead());
            ps.setLong(2, a.getTo());
            ps.setLong(3, a.getEventId());
            ps.executeUpdate();
            ps.close();
            return true;
        } finally {
            con.close();
        }
    }

    public ArrayList getForUser(long user_id) throws SQLException {
        Connection con = getConnection();
        ArrayList v = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement(GET_FOR_USER_ALERT);
            ps.setLong(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Alert a = new Alert();
                a.setPriority(rs.getInt(1));
                a.setByEmail(str2Bool(rs.getString(2)));
                a.setAlertType(rs.getInt(3));
                a.setDeleted(str2Bool(rs.getString(4)));
                a.setRead(rs.getDate(5));
                a.setEventId(rs.getInt(6));
                a.setSourceObjId(rs.getInt(7));
                a.setSourceUserId(rs.getInt(8));
                a.setSourceType(rs.getInt(9));
                a.setEventType(rs.getInt(10));
                a.setSent(rs.getDate(11));
                a.getParams().put("user_id", new Long(user_id));
                a.getParams().put("event_id", new Long(a.getEventId()));
                a.getParams().put("id", new Long(a.getSourceObjId()));
                v.add(a);
            }
            rs.close();
            ps.close();
            return v;
        } finally {
            con.close();
        }
    }

    public static final String MARK_DELETED = "UPDATE alerts SET deleted='Y' WHERE event_id=? AND user_id=?";

    public static final String MARK_READ = "UPDATE alerts SET read=? WHERE event_id=? AND user_id=?";

    public static final String GET_ALERT = "SELECT priority,by_email,alert_type,deleted,read" + " FROM alerts WHERE user_id=? AND event_id=?";

    public static final String GET_FOR_USER_ALERT = "SELECT al.priority,al.by_email,al.alert_type,al.deleted,al.read,al.event_id," + "l.source_obj_id,l.source_user_id,l.source_type,l.event_type,l.sent FROM alerts al, events l WHERE" + " l.id=al.event_id AND al.deleted='N' AND al.user_id=?";

    public static final String INSERT_ALERT = "INSERT INTO alerts " + "(event_id,priority,by_email,alert_type,deleted,read,user_id) VALUES (?,?,?,?,?,?,?)";

    public static final String UPDATE_ALERT = "UPDATE alerts SET read=? WHERE user_id=? AND event_id=?";
}
