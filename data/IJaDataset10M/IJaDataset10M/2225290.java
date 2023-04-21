package com.sheng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.sheng.datasource.Pool;
import com.sheng.po.User;

public class InsertUserDaoImpl implements InsertUserDAO {

    public void saveuser(User u) {
        Connection conn = null;
        PreparedStatement pm = null;
        try {
            conn = Pool.getConnection();
            conn.setAutoCommit(false);
            pm = conn.prepareStatement("insert into user(userid,username,passwd,existstate,management)values(?,?,?,?,?)");
            pm.setString(1, u.getUserid());
            pm.setString(2, u.getUsername());
            pm.setString(3, u.getPasswd());
            pm.setInt(4, u.getExiststate());
            pm.setInt(5, u.getManagement());
            pm.execute();
            conn.commit();
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ep) {
                ep.printStackTrace();
            }
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(pm);
            Pool.close(conn);
        }
    }

    public int updateuser(User u) {
        int i = 0;
        Connection conn = null;
        PreparedStatement pm = null;
        try {
            conn = Pool.getConnection();
            conn.setAutoCommit(false);
            pm = conn.prepareStatement("update user set username=?,passwd=?,existstate=?,management=? where userid=?");
            pm.setString(1, u.getUsername());
            pm.setString(2, u.getPasswd());
            pm.setInt(3, u.getExiststate());
            pm.setInt(4, u.getManagement());
            pm.setString(5, u.getUserid());
            i = pm.executeUpdate();
            conn.commit();
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(pm);
            Pool.close(conn);
        }
        return i;
    }
}
