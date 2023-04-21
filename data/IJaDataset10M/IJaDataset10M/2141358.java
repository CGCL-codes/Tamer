package com.business.music;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.beans.music.MusicType;
import com.sql.DBFactory;

public class MusicTypeBus {

    private Connection conn = null;

    private Statement stmt = null;

    private ResultSet rs = null;

    private static final Log log = LogFactory.getLog(MusicTypeBus.class);

    public void add(MusicType mt) {
        String typeName = mt.getMusictypeName();
        String typeMemo = mt.getMusictypeMemo();
        String username = mt.getUsername();
        String sql = "insert into music_type(musictype_name,username,musictype_memo)values('";
        sql += typeName + "','" + username + "','" + typeMemo + "')";
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error("the insert musictype excemtion:" + e.getMessage());
            log.error("the insert musictype sql is: " + sql);
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
        String sql = "select * from music_type where username='" + username + "' order by musictype_id";
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                MusicType mt = new MusicType();
                mt.setMusictypeId(rs.getString("musictype_id"));
                mt.setMusictypeMemo(rs.getString("musictype_memo"));
                mt.setMusictypeName(rs.getString("musictype_name"));
                mt.setUsername(rs.getString("username"));
                list.add(mt);
            }
        } catch (Exception e) {
            log.error("the select musictype exception:" + e.getMessage());
            log.error("the select musictype sql is: " + sql);
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

    public MusicType getMusicTypeById(int id) {
        MusicType mt = new MusicType();
        String sql = "select * from music_type where musictype_id=" + id;
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                mt.setMusictypeId(rs.getString("musictype_id"));
                mt.setMusictypeMemo(rs.getString("musictype_memo"));
                mt.setMusictypeName(rs.getString("musictype_name"));
                mt.setUsername(rs.getString("username"));
            }
        } catch (Exception e) {
            log.error("the select musicinfo excemtion:" + e.getMessage());
            log.error("the select musicinfo sql is: " + sql);
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                log.error(sqle.toString());
            }
        }
        return mt;
    }

    public void del(String typeId) {
        String sql = "delete from music_type where musictype_id=" + typeId;
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error("the delete musictype exception:" + e.getMessage());
            log.error("the delete musictype sql is: " + sql);
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException sqle) {
                log.error(sqle.toString());
            }
        }
    }

    public void update(MusicType mt, String typeId) {
        String typeName = mt.getMusictypeName();
        String typeMemo = mt.getMusictypeMemo();
        String username = mt.getUsername();
        String sql = "update music_type set musictype_name='" + typeName + "',username='" + username + "',musictype_memo='" + typeMemo + "'where musictype_id=" + typeId;
        try {
            conn = DBFactory.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error("the update musictype exception:" + e.getMessage());
            log.error("the update musictype sql is: " + sql);
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
