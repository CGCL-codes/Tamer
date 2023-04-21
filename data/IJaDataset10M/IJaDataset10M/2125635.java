package net;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import org.eclipse.jface.preference.PreferenceStore;

public class Solid {

    static Connection con;

    static Statement stmt;

    public boolean init() {
        boolean failure = false;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            final PreferenceStore preferenceStore = merlin.MerlinMain.preferenceStore;
            try {
                preferenceStore.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sCon = "jdbc:mysql://" + preferenceStore.getString("traceability");
            con = DriverManager.getConnection(sCon, preferenceStore.getString("TRACEUSER"), preferenceStore.getString("TRACEPASS"));
            stmt = con.createStatement();
        } catch (Exception e) {
            failure = true;
            e.printStackTrace();
        }
        return failure;
    }

    public void createRequirementLink(String reqName, String requirementId) {
        String updateString = "INSERT INTO RequirementLinks (reqid,requirement) values(" + "'" + requirementId + "','" + reqName + "')";
        try {
            stmt.executeUpdate(updateString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getLinkId(String reqId) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT linkid FROM requirementlinks WHERE reqid ='" + reqId + "'");
            if (rs.next()) return rs.getInt("linkid"); else return 0;
        } catch (Exception exp) {
            exp.printStackTrace();
            return 0;
        }
    }

    public void addTaskLinks(int linkId, String taskName, String taskId, String path) {
        String updateString;
        updateString = "INSERT INTO tasks (linkid,taskid,name,path)" + " values (" + linkId + ",'" + taskId + "','" + taskName + "','" + path + "')";
        try {
            stmt.executeUpdate(updateString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addLinks(String table, int linkId, String name, String id, String path) {
        String updateString;
        updateString = "INSERT INTO " + table + " (linkid,id,name,path)" + " values (" + linkId + ",'" + id + "','" + name + "','" + path + "')";
        try {
            stmt.executeUpdate(updateString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addTaskLinks(int linkId, String taskName, String taskId) {
        String updateString;
        updateString = "INSERT INTO tasks (linkid,taskid,name)" + " values (" + linkId + ",'" + taskId + "','" + taskName + "')";
        try {
            stmt.executeUpdate(updateString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addTaskLinks(int linkId, String filename) {
        String updateString;
        updateString = "INSERT INTO tasks (linkid,name)" + " values (" + linkId + ",'" + filename + "')";
        try {
            stmt.executeUpdate(updateString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addFileLinks(int linkId, String filename, String filepath) {
        String updateString;
        updateString = "INSERT INTO codes (linkid,name,path)" + " values (" + linkId + ",'" + filename + "','" + filepath + "')";
        try {
            stmt.executeUpdate(updateString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addTestLinks(int linkId, String testId, String testName) {
        String updateString;
        updateString = "INSERT INTO tests (linkid,id,name)" + " values (" + linkId + ",'" + testId + "','" + testName + "')";
        try {
            stmt.executeUpdate(updateString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeTaskLinks(int linkId, String taskName) {
        try {
            stmt.executeUpdate("delete from tasks where linkid='" + linkId + "' and " + "name ='" + taskName + "'");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void removeFileLinks(int linkId, String filename) {
        try {
            stmt.executeUpdate("delete from codes where linkid='" + linkId + "' and " + "name ='" + filename + "'");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void removeTestLinks(int linkId, String testname) {
        try {
            stmt.executeUpdate("delete from tests where linkid='" + linkId + "' and " + "name ='" + testname + "'");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public String[] getFiles(String linkId) {
        String tiedot[] = null;
        int size = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM codes " + "WHERE linkid =" + linkId);
            while (rs.next()) size++;
            tiedot = new String[size];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                tiedot[i] = rs.getString("filename");
                i++;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return tiedot;
    }

    public String[] getFiles(int linkId) {
        String tiedot[] = null;
        int size = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM codes " + "WHERE linkid =" + linkId);
            while (rs.next()) size++;
            tiedot = new String[size];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                tiedot[i] = rs.getString("filename");
                i++;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return tiedot;
    }

    public String[][] getInfo(int linkId, String table) {
        String tiedot[][] = null;
        int size = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM " + table + " " + "WHERE linkid =" + linkId);
            while (rs.next()) size++;
            tiedot = new String[3][size];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                tiedot[0][i] = rs.getString("id");
                tiedot[1][i] = rs.getString("name");
                tiedot[2][i] = rs.getString("path");
                i++;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return tiedot;
    }

    public String[][] getTaskInfo(int linkId) {
        String tiedot[][] = null;
        int size = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM tasks " + "WHERE linkid =" + linkId);
            while (rs.next()) size++;
            tiedot = new String[2][size];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                tiedot[0][i] = rs.getString("name");
                tiedot[1][i] = rs.getString("taskid");
                i++;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return tiedot;
    }

    public String[][] getTaskInfo2(int linkId) {
        String tiedot[][] = null;
        int size = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM tasks " + "WHERE linkid =" + linkId);
            while (rs.next()) size++;
            tiedot = new String[2][size];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                tiedot[0][i] = rs.getString("path");
                tiedot[1][i] = rs.getString("taskid");
                i++;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return tiedot;
    }

    public String[][] getFileInfo(int linkId) {
        String tiedot[][] = null;
        int size = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM codes " + "WHERE linkid =" + linkId);
            while (rs.next()) size++;
            tiedot = new String[2][size];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                tiedot[0][i] = rs.getString("name");
                tiedot[1][i] = rs.getString("path");
                i++;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return tiedot;
    }

    public boolean hasLink(String reqId) {
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM requirementlinks where reqid='" + reqId + "'");
            if (rs.next()) return true; else return false;
        } catch (Exception exp) {
            exp.printStackTrace();
            return false;
        }
    }

    public Vector countLinks(String reqId) {
        Vector palautus = new Vector();
        int tmpInt = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM requirementlinks where reqid='" + reqId + "'");
            if (rs.next()) {
                int linkid = rs.getInt("linkid");
                rs = stmt.executeQuery("SELECT * FROM tasks where linkid='" + linkid + "'");
                while (rs.next()) tmpInt++;
                palautus.add(Integer.toString(tmpInt));
                tmpInt = 0;
                rs = stmt.executeQuery("SELECT * FROM codes where linkid='" + linkid + "'");
                while (rs.next()) tmpInt++;
                palautus.add(Integer.toString(tmpInt));
                tmpInt = 0;
                rs = stmt.executeQuery("SELECT * FROM tests where linkid='" + linkid + "'");
                while (rs.next()) tmpInt++;
                palautus.add(Integer.toString(tmpInt));
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return palautus;
    }
}
