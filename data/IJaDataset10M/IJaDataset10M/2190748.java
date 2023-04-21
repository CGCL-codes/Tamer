package ro.gateway.aida.obj.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ro.gateway.aida.db.DBPersistenceManager;
import ro.gateway.aida.db.PersistenceToken;
import ro.gateway.aida.obj.ContactInfo;

/**
 * Created by IntelliJ IDEA.
 * User: xblue
 * Date: Dec 17, 2003
 * Time: 8:46:55 PM
 * To change this template use Options | File Templates.
 */
public class ContactInfoPMan extends DBPersistenceManager {

    private ContactInfoPMan(PersistenceToken token) {
        super(token);
    }

    public static ContactInfoPMan getManager(PersistenceToken token) {
        return new ContactInfoPMan(token);
    }

    public ContactInfo getForActivity(long activity_id) throws SQLException {
        Connection con = db_token.getCFService().getConnection(db_token.getPool_name());
        ContactInfo result = null;
        try {
            PreparedStatement ps = con.prepareStatement(GET);
            ps.setLong(1, activity_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new ContactInfo();
                result.setActivity_id(activity_id);
                result.setcName(rs.getString(1));
                result.setcTitle(rs.getString(2));
                result.setcEmail(rs.getString(3));
                result.setCountry_iso3(rs.getString(4));
                result.setAddr1(rs.getString(5));
                result.setAddr2(rs.getString(6));
                result.setPostalZip(rs.getString(7));
                result.setcPhone(rs.getString(8));
                result.setcURL(rs.getString(9));
            }
            rs.close();
            ps.close();
        } catch (SQLException sqlEx) {
            throw sqlEx;
        } finally {
            con.close();
        }
        return result;
    }

    public void insert(ContactInfo contact) throws SQLException {
        Connection con = db_token.getCFService().getConnection(db_token.getPool_name());
        try {
            PreparedStatement ps = con.prepareStatement(INSERT);
            ps.setLong(1, contact.getActivity_id());
            ps.setString(2, contact.getcName());
            ps.setString(3, contact.getcTitle());
            ps.setString(4, contact.getcEmail());
            ps.setString(5, contact.getCountry_iso3());
            ps.setString(6, contact.getAddr1());
            ps.setString(7, contact.getAddr2());
            ps.setString(8, contact.getPostalZip());
            ps.setString(9, contact.getcPhone());
            ps.setString(10, contact.getcURL());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException sqlEx) {
            throw sqlEx;
        } finally {
            con.close();
        }
    }

    public void removeForActivity(long activity_id) throws SQLException {
        Connection con = db_token.getCFService().getConnection(db_token.getPool_name());
        try {
            PreparedStatement ps = con.prepareStatement(DELETE);
            ps.setLong(1, activity_id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException sqlEx) {
            throw sqlEx;
        } finally {
            con.close();
        }
    }

    public static String DELETE = "DELETE FROM contact_info WHERE activity_id=?";

    public static String INSERT = "INSERT INTO contact_info (activity_id, cname, ctitle, cemail," + "country, addr1, addr2, postalzip,cfax,curl) " + "VALUES (?,?,?,?,?,?,?,?,?,?)";

    public static String GET = "SELECT cname, ctitle, cemail," + "country, addr1, addr2, postalzip,cfax,curl " + "FROM contact_info WHERE activity_id=?";
}
