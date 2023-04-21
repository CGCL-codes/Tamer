package com.business.photo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.beans.photo.PhotoType;
import com.sql.DBFactory;

public class PhotoTypeBus {

    private Connection conn = null;

    private Statement stmt = null;

    private ResultSet rs = null;

    private static final Log log = LogFactory.getLog(PhotoTypeBus.class);

    public void add(PhotoType pt) {
        String typeName = pt.getPhototypeName();
        String typeMemo = pt.getPhototypeMemo();
        String username = pt.getUsername();
        String sql = "insert into photo_type(phototype_name,username,phototype_memo)values('";
        sql += typeName + "','" + username + "','" + typeMemo + "')";
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error("the insert phototype exception:" + e.getMessage());
            log.error("the insert phototype sql is: " + sql);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                log.error(sqle.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List getAllTypes(String username) {
        List list = new ArrayList();
        String sql = "select * from photo_type where username='" + username + "' order by phototype_id";
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PhotoType pt = new PhotoType();
                pt.setPhototypeId(rs.getString("phototype_id"));
                pt.setPhototypeMemo(rs.getString("phototype_memo"));
                pt.setPhototypeName(rs.getString("phototype_name"));
                pt.setUsername(rs.getString("username"));
                list.add(pt);
            }
        } catch (Exception e) {
            log.error("the select phototype exception:" + e.getMessage());
            log.error("the select phototype sql is: " + sql);
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                log.error(sqle.toString());
            }
        }
        return list;
    }

    public PhotoType getPhotoTypeById(int id) {
        PhotoType pt = new PhotoType();
        String sql = "select * from photo_type where phototype_id=" + id;
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pt.setPhototypeId(rs.getString("phototype_id"));
                pt.setPhototypeMemo(rs.getString("phototype_memo"));
                pt.setPhototypeName(rs.getString("phototype_name"));
                pt.setUsername(rs.getString("username"));
            }
        } catch (Exception e) {
            log.error("the select photoinfo exception:" + e.getMessage());
            log.error("the select photoinfo sql is: " + sql);
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                log.error(sqle.toString());
            }
        }
        return pt;
    }

    public void del(String typeId) {
        String sql = "delete from photo_type where phototype_id=" + typeId;
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error("the delete phototype exception:" + e.getMessage());
            log.error("the delete phototype sql is: " + sql);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                log.error(sqle.toString());
            }
        }
    }

    public void update(PhotoType pt, String typeId) {
        String typeName = pt.getPhototypeName();
        String typeMemo = pt.getPhototypeMemo();
        String username = pt.getUsername();
        String sql = "update photo_type set phototype_name='" + typeName + "',username='" + username + "',phototype_memo='" + typeMemo + "'where phototype_id=" + typeId;
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error("the update phototype exception:" + e.getMessage());
            log.error("the update phototype sql is: " + sql);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                log.error(sqle.toString());
            }
        }
    }
}
