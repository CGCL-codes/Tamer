package com.spartancoder.accommodation.models;

import com.spartancoder.accommodation.beans.CollegeBean;
import com.spartancoder.accommodation.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author andrei
 */
public class College {

    private static Logger log = Logger.getLogger(College.class.getName());

    private static Database db = Database.getInstance();

    public static void create(CollegeBean cb) {
        try {
            Connection conn = db.getConnection();
            String query = "insert into `colleges`" + "(`university_id`, `name`, `address`, `available`)" + "value(?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, cb.getUniversityId());
            st.setString(2, cb.getName());
            st.setString(3, cb.getAddress());
            st.setBoolean(4, cb.isAvailable());
            st.execute();
        } catch (SQLException ex) {
            log.severe(ex.toString());
            throw new InternalError(ex.toString());
        }
    }

    public static void update(CollegeBean cb) {
        try {
            Connection conn = db.getConnection();
            String query = "update `colleges` set `university_id`=?, " + "`name`=?, `address`=?, `available`=? " + "where `college_id`=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, cb.getUniversityId());
            st.setString(2, cb.getName());
            st.setString(3, cb.getAddress());
            st.setBoolean(4, cb.isAvailable());
            st.setInt(5, cb.getCollegeId());
            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            log.severe(ex.toString());
            throw new InternalError(ex.toString());
        }
    }

    public static CollegeBean fetch(int collegeId) {
        try {
            Connection conn = db.getConnection();
            String query = "select * from `colleges` where `college_id`=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, collegeId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                CollegeBean cb = new CollegeBean();
                cb.fill(rs);
                return cb;
            }
            return null;
        } catch (SQLException ex) {
            log.severe(ex.toString());
            throw new InternalError(ex.toString());
        }
    }

    public static List<CollegeBean> fetchAll(int univId) {
        try {
            Connection conn = db.getConnection();
            String query = "select * from `colleges` where `university_id`=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, univId);
            ResultSet rs = st.executeQuery();
            List<CollegeBean> ret = new LinkedList<CollegeBean>();
            while (rs.next()) {
                CollegeBean cb = new CollegeBean();
                cb.fill(rs);
                ret.add(cb);
            }
            return ret;
        } catch (SQLException ex) {
            log.severe(ex.toString());
            throw new InternalError(ex.toString());
        }
    }

    public static void remove(int cId) {
        try {
            Connection conn = db.getConnection();
            String query = "delete from `colleges` where `college_id`=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, cId);
            st.execute();
        } catch (SQLException ex) {
            log.severe(ex.toString());
            throw new InternalError(ex.toString());
        }
    }
}
