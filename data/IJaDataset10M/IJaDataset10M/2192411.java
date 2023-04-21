package org.mitre.rt.server.database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.mitre.rt.common.dto.ApplicationInfo;
import org.mitre.rt.server.database.DatabaseManager;
import org.mitre.rt.server.exceptions.DatabaseException;

public class ApplicationInfoDAO {

    private static final Logger logger = Logger.getLogger(ApplicationInfoDAO.class.getPackage().getName());

    private static ApplicationInfoDAO instance = null;

    private ApplicationInfoDAO() {
    }

    /**
     * Singleton class has an instance method to allow access to the single instance.
     * @return
     */
    public static ApplicationInfoDAO instance() {
        if (instance == null) instance = new ApplicationInfoDAO();
        return instance;
    }

    /**
     * Return the list of all applications on the server.
     * @return
     */
    public List<ApplicationInfo> getAllApplications(Connection conn) throws SQLException {
        List<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
        Statement s = conn.createStatement();
        String sql = "SELECT id, name, abbr, version FROM Application";
        ResultSet rs = s.executeQuery(sql);
        while (rs.next()) {
            ApplicationInfo appInfo = new ApplicationInfo();
            appInfo.setId(rs.getString("id"));
            appInfo.setAbbr(rs.getString("abbr"));
            appInfo.setName(rs.getString("name"));
            appInfo.setVersion(rs.getInt("version"));
            applications.add(appInfo);
        }
        s.close();
        return applications;
    }
}
