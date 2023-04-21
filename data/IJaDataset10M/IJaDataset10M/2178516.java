package in.espirit.tracer.database.dao;

import in.espirit.tracer.database.connection.ConnectionFactory;
import in.espirit.tracer.database.connection.ConnectionPool;
import in.espirit.tracer.model.Link;
import in.espirit.tracer.util.DaoUtils;
import in.espirit.tracer.util.StringUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class LinkDao {

    private static Logger logger = Logger.getLogger(LinkDao.class.getName());

    public static Link getLink(String id) throws Exception {
        ConnectionPool pool = ConnectionFactory.getPool();
        Connection con = pool.getConnection();
        Statement st = null;
        ResultSet rs = null;
        Link result = new Link();
        String query = "SELECT f_id, f_name, f_desc, f_target, f_teamvisible, f_tags FROM t_link where f_id=" + id;
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                result.setId(rs.getString(1));
                result.setName(rs.getString(2));
                result.setDesc(rs.getString(3));
                result.setTarget(rs.getString(4));
                if (rs.getString(5) != null) {
                    result.setTeamVisible(Integer.parseInt(rs.getString(5)));
                }
                result.setTags(rs.getString(6));
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            logger.fatal("Getting Information Failed" + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (con != null) con.close();
        }
        return result;
    }

    public static ArrayList<Link> getLinks(String reportType, String loggedUser, String count, String tag) throws Exception {
        ConnectionPool pool = ConnectionFactory.getPool();
        Connection con = pool.getConnection();
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Link> result = new ArrayList<Link>();
        String query = "select f_id, f_name, f_target, f_desc, f_username, f_position, f_tags from t_link WHERE";
        if (reportType.equalsIgnoreCase("my")) {
            query += " f_userName='" + loggedUser + "'";
        } else {
            query += " f_teamvisible=1";
        }
        if (tag != null) {
            query += " AND f_tags like '%" + tag + "%' ";
        }
        query += " ORDER by f_position DESC";
        if (!count.equalsIgnoreCase("all")) {
            query += " LIMIT " + count;
        }
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Link a = new Link();
                a.setId(rs.getString(1));
                a.setName(rs.getString(2));
                a.setTarget(rs.getString(3));
                a.setDesc(rs.getString(4));
                a.setUserName(rs.getString(5));
                a.setPosition(Integer.parseInt(rs.getString(6)));
                result.add(a);
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            logger.fatal("Getting link List failed" + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (con != null) con.close();
        }
        return result;
    }

    public static void registerLink(Link link, String loggedUser) throws Exception {
        String query = "INSERT INTO t_link (f_name, f_desc, f_target, f_username, f_tags, f_teamvisible) VALUES('" + StringUtils.nullCheck(link.getName()) + "','" + StringUtils.nullCheck(link.getDesc()) + "','" + StringUtils.nullCheck(link.getTarget()) + "','" + loggedUser + "','" + StringUtils.nullCheck(link.getTags()) + "'," + link.getTeamVisible() + ")";
        DaoUtils.executeUpdate(query);
    }

    public static void editLink(Link link) throws Exception {
        String query = "Update t_link " + "SET  f_name='" + StringUtils.nullCheck(link.getName()) + "',  f_desc='" + StringUtils.nullCheck(link.getDesc()) + "',  f_target='" + StringUtils.nullCheck(link.getTarget()) + "',  f_teamvisible=" + link.getTeamVisible() + ",  f_tags='" + StringUtils.nullCheck(link.getTags()) + "' where  f_id=" + link.getId();
        DaoUtils.executeUpdate(query);
    }

    public static void deleteLink(Link link) throws Exception {
        String query = "Delete from t_link where f_id=" + link.getId();
        DaoUtils.executeUpdate(query);
    }

    public static boolean updatePosition(String changes) {
        String[] arrChanges = changes.split(",");
        String[] linkpos;
        String query = "";
        for (int i = 0; i < arrChanges.length; i++) {
            linkpos = arrChanges[i].split("N");
            query += "Update t_link set f_position=" + linkpos[1] + " WHERE f_id=" + linkpos[0] + ";";
        }
        try {
            return DaoUtils.executeUpdate(query);
        } catch (Exception e) {
            logger.fatal("Persisting changes for link failed with error - " + e.getMessage());
            return false;
        }
    }
}
