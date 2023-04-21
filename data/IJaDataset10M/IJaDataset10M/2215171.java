package cn.ac.ntarl.umt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;
import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.actions.user.DBListGroup;
import cn.ac.ntarl.umt.actions.user.DBListRelation;
import cn.ac.ntarl.umt.config.Config;
import cn.ac.ntarl.umt.database.DAOFactory;
import cn.ac.ntarl.umt.database.Database;

public class InitServlet extends HttpServlet {

    private static final long serialVersionUID = 7794320718625739056L;

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        String prefix = getServletContext().getRealPath("/");
        String config_dir = prefix + getInitParameter("config-dir");
        System.setProperty("webappHome", prefix);
        Config.reload();
        PropertyConfigurator.configure(config_dir + File.separator + "Log4j.prop");
        System.setProperty(Config.CONF_DIR, config_dir);
        Config config = Config.getInstance();
        String sqlfile = config_dir + File.separator + "umt.sql";
        String databasename = config.getStringProp("database", "duckling");
        String databaseip = config.getStringProp("database.ip", "localhost:3306");
        String user = config.getStringProp("Database.auth.username", "root");
        String password = config.getStringProp("Database.auth.password", "root");
        ;
        initDatabase(sqlfile, databasename, databaseip, user, password);
        DAOFactory.getInstance();
        GroupGraph groupgraph = GroupGraph.getInstance();
        if (groupgraph.getGroups() == null || groupgraph.getGroups().size() <= 0) {
            try {
                ArrayList groups = (ArrayList) Database.perform(new DBListGroup());
                groupgraph.setGroups(groups);
                ArrayList grouprelations = (ArrayList) Database.perform(new DBListRelation());
                groupgraph.setGroupRelations(grouprelations);
            } catch (CLBException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDatabase(String sqlfile, String databasename, String databaseip, String user, String password) {
        ifNotExistCreateDatabase(databasename, databaseip, user, password);
        initOriginalData(sqlfile, databasename, databaseip, user, password);
    }

    private boolean checkTable(String tablename, Connection conn) {
        boolean found = false;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from " + tablename);
            if (rs.next()) {
                found = true;
            }
        } catch (Throwable se) {
        }
        return found;
    }

    private void initOriginalData(String sqlfile, String databasename, String databaseip, String user, String password) {
        Connection conn = null;
        Statement stmt = null;
        SQLReader reader = null;
        try {
            String sql = null;
            String url = "jdbc:mysql://" + databaseip + "/" + databasename + "?useUnicode=true&characterEncoding=utf8";
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            if (checkTable("adminusers", conn)) {
                return;
            }
            reader = new SQLReader(new FileInputStream(sqlfile), "UTF-8");
            while ((sql = reader.next()) != null) {
                stmt.execute(sql);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void ifNotExistCreateDatabase(String databasename, String databaseip, String user, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            String url = "jdbc:mysql://" + databaseip + "/?useUnicode=true&characterEncoding=utf8";
            conn = DriverManager.getConnection(url, user, password);
            pstmt = conn.prepareStatement("show databases");
            ResultSet rs = pstmt.executeQuery();
            boolean exist = false;
            while (rs.next()) {
                if (databasename.equals(rs.getString(1))) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                pstmt = conn.prepareStatement("CREATE database IF NOT EXISTS " + databasename + " CHARACTER SET utf8");
                pstmt.execute();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
