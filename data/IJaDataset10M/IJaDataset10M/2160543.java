package com.sheng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.sheng.datasource.Pool;
import com.sheng.po.Outwuliao;

public class LookpersonDaoImpl implements LookpersonDAO {

    public List<Outwuliao> getpersonout(String userid, Timestamp startdate, Timestamp enddate) {
        List<Outwuliao> ls = new ArrayList<Outwuliao>();
        Connection conn = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {
            conn = Pool.getConnection();
            pm = conn.prepareStatement("select * from delwuliao where outuserid=? and outdate between ? and ?");
            pm.setString(1, userid);
            pm.setTimestamp(2, startdate);
            pm.setTimestamp(3, enddate);
            rs = pm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Outwuliao ow = setproperty(rs);
                    ls.add(ow);
                }
            }
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        }
        return ls;
    }

    private Outwuliao setproperty(ResultSet rs) throws SQLException {
        Outwuliao ow = new Outwuliao();
        ow.setMaori(rs.getDouble("maori"));
        ow.setPurchaser(rs.getString("purchaser"));
        ow.setOutname(rs.getString("outname"));
        ow.setOutnum(rs.getInt("outnum"));
        ow.setOutprice(rs.getDouble("outprice"));
        ow.setOutuserid(rs.getString("outuserid"));
        ow.setOutdate(rs.getString("outdate").substring(0, 19));
        ow.setPid(rs.getString("pid"));
        return ow;
    }

    public List<Outwuliao> getpersonoutbypage(String userid, Timestamp startdate, Timestamp enddate, int pageSize, int pageNo) {
        List<Outwuliao> ls = new ArrayList<Outwuliao>();
        Connection conn = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {
            conn = Pool.getConnection();
            pm = conn.prepareStatement("select * from delwuliao where outuserid=? and outdate between ? and ? limit " + (pageSize * pageNo - pageSize) + "," + pageSize + "");
            pm.setString(1, userid);
            pm.setTimestamp(2, startdate);
            pm.setTimestamp(3, enddate);
            rs = pm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Outwuliao ow = setproperty(rs);
                    ls.add(ow);
                }
            }
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        }
        return ls;
    }

    public List<Double> getsumms(String userid, Timestamp startdate, Timestamp enddate) {
        List<Double> ls = new ArrayList<Double>();
        Connection conn = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {
            conn = Pool.getConnection();
            pm = conn.prepareStatement("select sum(outprice),sum(maori) from delwuliao where outuserid=? and outdate between ? and ?");
            pm.setString(1, userid);
            pm.setTimestamp(2, startdate);
            pm.setTimestamp(3, enddate);
            rs = pm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    ls.add(rs.getDouble(1));
                    ls.add(rs.getDouble(2));
                }
            }
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        }
        return ls;
    }

    public List<Integer> getsumpage(String userid, Timestamp startdate, Timestamp enddate, int pageSize) {
        List<Integer> ls = new ArrayList<Integer>();
        Connection conn = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {
            conn = Pool.getConnection();
            pm = conn.prepareStatement("select count(*) from delwuliao where outuserid=? and outdate between ? and ?");
            pm.setString(1, userid);
            pm.setTimestamp(2, startdate);
            pm.setTimestamp(3, enddate);
            rs = pm.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    ls.add(rs.getInt(1) / pageSize);
                    ls.add(rs.getInt(1) % pageSize);
                }
            }
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(rs);
            Pool.close(pm);
            Pool.close(conn);
        }
        return ls;
    }
}
