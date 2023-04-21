package abstractminds.model.persistence.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Vector;
import abstractminds.common.entity.DiagramChangeBean;
import abstractminds.common.entity.TinyUserBean;
import abstractminds.common.entity.UserBean;
import abstractminds.model.persistence.UserDAO;

public class OracleUserDAO implements UserDAO {

    public void insertUser(UserBean user) throws SQLException {
        Connection conn = OracleDAOFactory.createConnection();
        String sql = "INSERT INTO users" + " VALUES(" + "'" + user.getId() + "'," + "'" + user.getPassword() + "'," + "'" + user.getFirstName() + "'," + "'" + user.getLastName() + "'," + "'" + user.getEmail() + "')";
        Statement stat = null;
        try {
            stat = conn.createStatement();
            stat.executeUpdate(sql);
        } finally {
            OracleDAOFactory.freeResources(stat, null);
        }
    }

    public Collection<UserBean> getAllUsers() throws SQLException {
        Connection conn = OracleDAOFactory.createConnection();
        String sql = "SELECT u.*, " + " NVL(g.diagram_count,0) diagram_count " + "FROM users u LEFT JOIN " + "(SELECT diagram_creator, " + "  COUNT(diagram_id) diagram_count " + " FROM diagrams " + " GROUP BY diagram_creator) g " + "ON u.user_id = g.diagram_creator " + "ORDER BY NVL(g.diagram_count,0) DESC";
        Vector<UserBean> v = new Vector<UserBean>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            UserBean userBean = null;
            while (rs.next()) {
                userBean = new UserBean(rs.getString("USER_ID"), null, rs.getString("USER_FIRSTNAME"), rs.getString("USER_LASTNAME"), rs.getString("USER_EMAIL"));
                userBean.setDiagramCount(rs.getInt("DIAGRAM_COUNT"));
                v.add(userBean);
            }
            return v;
        } finally {
            OracleDAOFactory.freeResources(stat, rs);
        }
    }

    public UserBean getUser(String id) throws SQLException {
        Connection conn = OracleDAOFactory.createConnection();
        Statement stat = conn.createStatement();
        String sql = "SELECT u.*, " + " NVL(g.diagram_count,0) diagram_count " + "FROM " + "(SELECT * " + " FROM users u " + " WHERE user_id='" + id + "') u " + "LEFT JOIN " + "(SELECT diagram_creator, " + "  COUNT(diagram_id) diagram_count " + " FROM diagrams " + " WHERE diagram_creator='" + id + "' " + " GROUP BY diagram_creator) g " + "ON u.user_id = g.diagram_creator";
        ResultSet rs = stat.executeQuery(sql);
        if (!rs.next()) return null;
        UserBean userBean = new UserBean(rs.getString("USER_ID"), null, rs.getString("USER_FIRSTNAME"), rs.getString("USER_LASTNAME"), rs.getString("USER_EMAIL"));
        userBean.setDiagramCount(rs.getInt("DIAGRAM_COUNT"));
        return userBean;
    }

    public boolean login(TinyUserBean login) throws SQLException {
        Connection conn = OracleDAOFactory.createConnection();
        String sql = "SELECT user_id, user_password FROM users WHERE user_id='" + login.getId() + "'";
        String id = null;
        String password = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            if (!rs.next()) return false;
            id = rs.getString("USER_ID");
            password = rs.getString("USER_PASSWORD");
        } finally {
            OracleDAOFactory.freeResources(stat, rs);
        }
        if (!login.getId().equals(id) || !login.getPassword().equals(password)) {
            return false;
        }
        return true;
    }
}
