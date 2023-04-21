package ca.qc.adinfo.rouge.user.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import ca.qc.adinfo.rouge.server.DBManager;
import ca.qc.adinfo.rouge.user.User;

public class UserDb {

    private static Logger log = Logger.getLogger(UserDb.class);

    public static User createUser(DBManager dbManager, String username, String passwordHash, String firstName, String lastName, String email) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;
        sql = "INSERT INTO rouge_users (`username`, `password`, `firstname`, `lastname`, `email`, `created`) VALUES (?, ?, ?, ?, ?, NOW())";
        try {
            connection = dbManager.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            int ret = stmt.executeUpdate();
            if (ret > 0) {
                rs = stmt.getGeneratedKeys();
                rs.next();
                return new User(rs.getLong(1), username, passwordHash, firstName, lastName, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error(stmt);
            log.error(e);
            return null;
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(connection);
        }
    }

    public static boolean isEmailInUse(DBManager dbManager, String email) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;
        sql = "SELECT COUNT(`id`) AS cnt FROM rouge_users WHERE `email` = ?";
        try {
            connection = dbManager.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return (rs.getInt("cnt") > 0);
            } else {
                return false;
            }
        } catch (SQLException e) {
            log.error(stmt);
            log.error(e);
            return false;
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(connection);
        }
    }

    public static boolean isUsernameInUse(DBManager dbManager, String username) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;
        sql = "SELECT COUNT(`id`) AS cnt FROM rouge_users WHERE `username` = ?";
        try {
            connection = dbManager.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return (rs.getInt("cnt") > 0);
            } else {
                return false;
            }
        } catch (SQLException e) {
            log.error(stmt);
            log.error(e);
            return false;
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(connection);
        }
    }

    public static User getUser(DBManager dbManager, String username) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;
        sql = "SELECT `id`, `password`, `firstname`, `lastname`, `email` FROM rouge_users WHERE `username` = ?";
        try {
            connection = dbManager.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), username, rs.getString("password"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error(stmt);
            log.error(e);
            return null;
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(connection);
        }
    }

    public static User getUser(DBManager dbManager, long id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;
        sql = "SELECT `username`, `password`, `firstname`, `lastname`, `email` FROM rouge_users WHERE `id` = ?";
        try {
            connection = dbManager.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(id, rs.getString("username"), rs.getString("password"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error(stmt);
            log.error(e);
            return null;
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(connection);
        }
    }
}
