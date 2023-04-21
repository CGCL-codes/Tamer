package dao_service;

import i_dao.I_User_Dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import dao_pool.Pool;
import Protocol.contact.Contact;
import Protocol.contact.ContactGroup;
import Protocol.user.BasePersonInfo;
import Protocol.user.Login_Q;
import Protocol.user.Login_R;
import Protocol.user.User;

public class UserImp implements I_User_Dao {

    Pool m_Pool = Pool.getInstance();

    @Override
    public boolean insertUser(User user) {
        Connection conn = m_Pool.getConnection();
        Statement statement = null;
        try {
            int count = 0;
            int num = 0;
            statement = conn.createStatement();
            String n_sql = "SELECT user_number From user";
            ResultSet rs = statement.executeQuery(n_sql);
            while (rs.next()) {
                count++;
                if (rs.getString("user_number").equals(user.getUser_number())) {
                } else {
                    num++;
                }
            }
            if (count == num) {
                String sql = "insert  into user (user_number, password)  values ( '" + user.getUser_number() + "', '" + user.getUser_password() + "')";
                statement.execute(sql);
                return true;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_Pool.freeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        Connection conn = m_Pool.getConnection();
        try {
            Statement statement = null;
            int count = 0;
            int num = 0;
            statement = conn.createStatement();
            String n_sql = "SELECT user_id From user";
            ResultSet rs = statement.executeQuery(n_sql);
            while (rs.next()) {
                count++;
                if (rs.getInt("user_id") != user.getUser_id()) num++;
            }
            if (count == num + 1) {
                int new_id = user.getUser_id();
                String new_user_number = user.getUser_number();
                String new_password = user.getUser_password();
                SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String new_last_login_time = sFormat.format(user.getLast_login_time());
                String new_last_logout_time = sFormat.format(user.getLast_logout_time());
                String new_reg = sFormat.format(user.getRegister_time());
                String sql = "update user set user_number = ?,password=? where user_id=?";
                PreparedStatement prstmt = conn.prepareStatement(sql);
                prstmt.setString(1, new_user_number);
                prstmt.setString(2, new_password);
                prstmt.setLong(3, new_id);
                prstmt.executeUpdate();
                if (new_last_login_time != null) {
                    String sql1 = "update user set last_login_time=? where user_id=?";
                    PreparedStatement prstmt1 = conn.prepareStatement(sql1);
                    prstmt1.setString(1, new_last_login_time);
                    prstmt1.setLong(2, new_id);
                    prstmt1.executeUpdate();
                }
                if (new_last_logout_time != null) {
                    String sql2 = "update user set last_logout_time=? where user_id=?";
                    PreparedStatement prstmt2 = conn.prepareStatement(sql2);
                    prstmt2.setString(1, new_last_logout_time);
                    prstmt2.setLong(2, new_id);
                    prstmt2.executeUpdate();
                }
                if (new_reg != null) {
                    String sql3 = "update user set register_time=? where user_id=?";
                    PreparedStatement prstmt3 = conn.prepareStatement(sql3);
                    prstmt3.setString(1, new_reg);
                    prstmt3.setLong(2, new_id);
                    prstmt3.executeUpdate();
                }
                return true;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_Pool.freeConnection(conn);
        }
        return false;
    }

    @Override
    public User getUerByNumber(String user_number) {
        User retUser = null;
        Connection conn = m_Pool.getConnection();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String n_sql = "SELECT user_number FROM user";
            ResultSet n_rs = statement.executeQuery(n_sql);
            String sql = "select*from user where user_number='" + user_number + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                retUser = new User();
                retUser.setUser_id(rs.getInt("user_id"));
                retUser.setUser_number(rs.getString("user_number"));
                retUser.setUser_password(rs.getString("password"));
                if (rs.getDate("last_login_time") != null) retUser.setLast_login_time(rs.getDate("last_login_time"));
                if (rs.getDate("last_logout_time") != null) retUser.setLast_logout_time(rs.getDate("last_logout_time"));
                if (rs.getDate("register_time") != null) retUser.setRegister_time(rs.getDate("register_time"));
                rs.close();
                m_Pool.freeConnection(conn);
                return retUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteUser(int user_id) {
        Connection conn = m_Pool.getConnection();
        Statement statement;
        try {
            int count = 0;
            int num = 0;
            statement = conn.createStatement();
            String n_sql = "SELECT user_id From user";
            ResultSet rs = statement.executeQuery(n_sql);
            while (rs.next()) {
                count++;
                if (rs.getInt("user_id") != user_id) {
                    num++;
                }
            }
            if (count == num + 1) {
                statement = conn.createStatement();
                String sql = "DELETE from user where user_id='" + user_id + "'";
                statement.execute(sql);
                return true;
            }
            m_Pool.freeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getUserById(int user_id) {
        User retUser = null;
        Connection conn = m_Pool.getConnection();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String sql = "select*from user where user_number='" + user_id + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                retUser = new User();
                retUser.setUser_id(rs.getInt("user_id"));
                retUser.setUser_number(rs.getString("user_number"));
                retUser.setUser_password(rs.getString("password"));
                if (rs.getDate("last_login_time") != null) retUser.setLast_login_time(rs.getDate("last_login_time"));
                if (rs.getDate("last_logout_time") != null) {
                    retUser.setLast_logout_time(rs.getDate("last_logout_time"));
                }
                if (rs.getDate("register_time") != null) retUser.setRegister_time(rs.getDate("register_time"));
                rs.close();
                m_Pool.freeConnection(conn);
                return retUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteUsers(ArrayList<Integer> userIdList) {
        Connection conn = m_Pool.getConnection();
        Statement statement = null;
        try {
            for (int i = 0; i < userIdList.size(); i++) {
                int count = 0;
                int num = 0;
                statement = conn.createStatement();
                String n_sql = "SELECT user_id From user";
                ResultSet rs = statement.executeQuery(n_sql);
                while (rs.next()) {
                    count++;
                    if (rs.getInt("user_id") != userIdList.get(i)) {
                        num++;
                    }
                }
                if (count == num + 1) {
                    statement = conn.createStatement();
                    String sql = "DELETE from user where user_id='" + userIdList.get(i) + "'";
                    statement.execute(sql);
                }
            }
            m_Pool.freeConnection(conn);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertUsers(ArrayList<User> userList) {
        Connection conn = m_Pool.getConnection();
        Statement statement = null;
        try {
            for (int i = 0; i < userList.size(); i++) {
                int count = 0;
                int num = 0;
                statement = conn.createStatement();
                String n_sql = "SELECT user_number From user";
                ResultSet rs = statement.executeQuery(n_sql);
                while (rs.next()) {
                    count++;
                    if (rs.getString("user_number").equals(userList.get(i).getUser_number())) {
                    } else {
                        num++;
                    }
                }
                if (count == num) {
                    String sql = "insert  into user (user_number, password)  values ( '" + userList.get(i).getUser_number() + "', '" + userList.get(i).getUser_password() + "')";
                    statement.execute(sql);
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            m_Pool.freeConnection(conn);
        }
        return false;
    }
}
