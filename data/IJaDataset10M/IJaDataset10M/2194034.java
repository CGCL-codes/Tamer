package net.vicms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.vicms.dto.Users;
import net.vicms.util.DBUtil;

/**
 *
 * @author kien
 */
public class UsersDAO {

    private static Logger logger = Logger.getLogger(UsersDAO.class.getName());

    private final String createSQL = "INSERT INTO Users(user_id, fullname, email, password, role,dateupdate)VALUES (?,?,?,?,?,?)";

    private final String retrievalSQL1 = "SELECT user_id, fullname, email, password, role,dateupdate FROM Users WHERE user_id = ?";

    private final String retrievalSecurity = "SELECT user_id, fullname, email, password, role,dateupdate FROM Users WHERE email = ? AND password = ?";

    private final String retrievalSQL2 = "SELECT * FROM Users";

    private final String updateSQL = "UPDATE Users SET fullname= ?,email=?, password=?, role=?,dateupdate=? WHERE user_id = ?";

    private final String updateSQL1 = "UPDATE Users SET fullname= ? WHERE user_id = ?";

    private final String updateSQL2 = "UPDATE Users SET email=?  WHERE user_id = ?";

    private final String updateSQL3 = "UPDATE Users SET password=? WHERE user_id = ?";

    private final String updateSQL4 = "UPDATE Users SET role=? WHERE user_id = ?";

    private final String deleteSQL = "DELETE  FROM Users WHERE user_id = ?";

    /**
     *
     * @param obj
     * @return
     */
    public Users create(Users obj) {
        Connection con = null;
        try {
            PreparedStatement ptmt = null;
            con = DBUtil.getConnection();
            ptmt = con.prepareStatement(createSQL);
            ptmt.setString(1, obj.getId());
            ptmt.setString(2, obj.getFullname());
            ptmt.setString(3, obj.getEmail());
            ptmt.setString(4, obj.getPassword());
            ptmt.setString(5, obj.getRole());
            ptmt.setDate(6, obj.getDateupdate());
            ptmt.executeUpdate();
            ptmt.close();
            return obj;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     * 
     * @param email
     * @param pass
     * @return User object
     */
    public Users retrievalByEmailPass(String email, String pass) {
        Connection con = null;
        try {
            PreparedStatement ptmt = null;
            con = DBUtil.getConnection();
            Users obj = null;
            ptmt = con.prepareStatement(retrievalSecurity);
            ptmt.setString(1, email);
            ptmt.setString(2, pass);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                obj = new Users();
                obj.setId(rs.getString(1));
                obj.setFullname(rs.getString(2));
                obj.setEmail(rs.getString(3));
                obj.setPassword(rs.getString(4));
                obj.setRole(rs.getString(5));
                obj.setDateupdate(rs.getDate(6));
            }
            rs.close();
            ptmt.close();
            return obj;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     *
     * @param user_id
     * @return
     */
    public Users findById(String user_id) {
        Connection con = null;
        try {
            PreparedStatement ptmt = null;
            Users obj = null;
            con = DBUtil.getConnection();
            ptmt = con.prepareStatement(retrievalSQL1);
            ptmt.setString(1, user_id);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                obj = new Users();
                obj.setId(rs.getString(1));
                obj.setFullname(rs.getString(2));
                obj.setEmail(rs.getString(3));
                obj.setPassword(rs.getString(4));
                obj.setRole(rs.getString(5));
                obj.setDateupdate(rs.getDate(6));
            }
            rs.close();
            ptmt.close();
            return obj;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     *
     * @return
     */
    public Collection<Users> findAll() {
        Connection con = null;
        try {
            Statement stmt = null;
            Users obj = null;
            Collection<Users> users = new ArrayList<Users>();
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(retrievalSQL2);
            while (rs.next()) {
                obj = new Users();
                obj.setId(rs.getString(1));
                obj.setFullname(rs.getString(2));
                obj.setEmail(rs.getString(3));
                obj.setPassword(rs.getString(4));
                obj.setRole(rs.getString(5));
                obj.setDateupdate(rs.getDate(6));
                users.add(obj);
            }
            rs.close();
            stmt.close();
            return users;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public Users update(Users obj) {
        Connection con = null;
        try {
            PreparedStatement ptmt = null;
            con = DBUtil.getConnection();
            ptmt = con.prepareStatement(updateSQL);
            ptmt.setString(1, obj.getFullname());
            ptmt.setString(2, obj.getEmail());
            ptmt.setString(3, obj.getPassword());
            ptmt.setString(4, obj.getRole());
            ptmt.setDate(5, obj.getDateupdate());
            ptmt.setString(6, obj.getId());
            ptmt.executeUpdate();
            ptmt.close();
            return obj;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     *
     * @param fullname
     * @param user_id
     * @return
     */
    public boolean updateFullname(String fullname, String user_id) {
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            PreparedStatement ps = null;
            ps = con.prepareStatement(updateSQL1);
            ps.setString(1, fullname);
            ps.setString(2, user_id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     * 
     * @param email
     * @param user_id
     * @return
     */
    public boolean updateEmail(String email, String user_id) {
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            PreparedStatement ps = null;
            ps = con.prepareStatement(updateSQL2);
            ps.setString(1, email);
            ps.setString(2, user_id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     * 
     * @param pass
     * @param user_id
     * @return
     */
    public boolean updatePassWord(String newpass, String user_id) {
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            PreparedStatement ps = null;
            ps = con.prepareStatement(updateSQL3);
            ps.setString(1, newpass);
            ps.setString(2, user_id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     * 
     * @param role
     * @param user_id
     * @return
     */
    public boolean updateRole(String role, String user_id) {
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            PreparedStatement ps = null;
            ps = con.prepareStatement(updateSQL4);
            ps.setString(1, role);
            ps.setString(2, user_id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     *
     * @param obj
     */
    public boolean delete(Users obj) {
        Connection con = null;
        try {
            PreparedStatement ptmt = null;
            con = DBUtil.getConnection();
            ptmt = con.prepareStatement(deleteSQL);
            ptmt.setString(1, obj.getId());
            ptmt.executeUpdate();
            ptmt.close();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    /**
     *
     * @param id
     */
    public boolean delete(String id) {
        Connection con = null;
        try {
            PreparedStatement ptmt = null;
            con = DBUtil.getConnection();
            ptmt = con.prepareStatement(deleteSQL);
            ptmt.setString(1, id);
            ptmt.executeUpdate();
            ptmt.close();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DBUtil.closeConnection(con);
        }
    }
}
