package mecca.portal.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import mecca.db.Db;
import mecca.db.DbException;
import mecca.portal.element.PageTheme;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class UserPage {

    public static String getCSS(String usrlogin) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            sql = "SELECT css_name FROM user_css WHERE user_login = '" + usrlogin + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getString("css_name"); else return "default.css";
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static String getTheme(String usrlogin) throws DbException {
        return getCSS(usrlogin);
    }

    public static Vector getPageThemeList() throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            sql = "SELECT css_name, css_title FROM page_css";
            ResultSet rs = stmt.executeQuery(sql);
            Vector v = new Vector();
            while (rs.next()) {
                String css_name = rs.getString("css_name");
                String css_title = rs.getString("css_title");
                v.addElement(new PageTheme(css_name, css_title));
            }
            return v;
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static void saveTheme(String usrlogin, String css) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            boolean found = false;
            {
                sql = "SELECT user_login FROM user_css WHERE user_login = '" + usrlogin + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (found) sql = "UPDATE user_css SET css_name = '" + css + "' WHERE user_login = '" + usrlogin + "'"; else sql = "INSERT into user_css (user_login, css_name) VALUES ('" + usrlogin + "', '" + css + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static String getUrlForHtmlContainer(String module) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            sql = "SELECT html_url FROM module_htmlcontainer WHERE module_id = '" + module + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getString("html_url"); else return "";
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static String getUrlForRSSContainer(String module) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            sql = "SELECT rss_source FROM rss_module WHERE module_id = '" + module + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getString("rss_source"); else return "";
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static Hashtable getUrlAndXslForXMLContainer(String module) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            Hashtable h = new Hashtable();
            sql = "SELECT xml, xsl FROM xml_module WHERE module_id = '" + module + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                h.put("xml", rs.getString("xml"));
                h.put("xsl", rs.getString("xsl"));
            }
            return h;
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static Hashtable getValuesForAttributable(String module) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            Hashtable h = new Hashtable();
            sql = "SELECT attribute_name, attribute_value  FROM attr_module_data WHERE module_id = '" + module + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                h.put(rs.getString("attribute_name"), rs.getString("attribute_value"));
            }
            return h;
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static String getDisplayType(String usrlogin, String tabid) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            String display_type = "";
            if (tabid != null && !"".equals(tabid)) sql = "SELECT display_type FROM tab WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' "; else sql = "SELECT display_type FROM tab WHERE user_login = '" + usrlogin + "' ORDER BY sequence";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) display_type = rs.getString("display_type");
            if (display_type == null || "".equals("display_type")) display_type = "left_navigation";
            return display_type;
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static String getDisplayType(String usrlogin) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            String display_type = "";
            sql = "SELECT display_type FROM tab WHERE user_login = '" + usrlogin + "' ORDER BY sequence";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) display_type = rs.getString("display_type");
            if (display_type == null || "".equals("display_type")) display_type = "left_navigation";
            return display_type;
        } catch (SQLException ex) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static String[] getDisplayTypes() {
        return new String[] { "left_navigation", "single_column", "two_columns", "three_columns", "narrow_wide" };
    }

    public static Vector getDisplayTypeVector() {
        String[] s = getDisplayTypes();
        Vector v = new Vector();
        for (int i = 0; i < s.length; i++) v.addElement(s[i]);
        return v;
    }
}
