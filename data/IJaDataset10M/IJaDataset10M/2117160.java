package org.riverock.dbrevision.test.db;

import org.riverock.dbrevision.offline.StartupApplication;
import org.riverock.dbrevision.offline.DbRevisionConfig;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.factory.ORAconnect;
import org.riverock.dbrevision.db.factory.MYSQLconnect;
import org.riverock.dbrevision.system.DbStructureExport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * User: SMaslyukov
 * Date: 22.02.2007
 * Time: 17:17:22
 */
public class ExportDbTest {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
        String username = "root";
        String password = "qqq";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, username, password);
        StartupApplication.init();
        System.out.println("DebugDir: " + DbRevisionConfig.getGenericDebugDir());
        DatabaseAdapter db = new MYSQLconnect(conn);
        FileOutputStream fileOutputStream = new FileOutputStream(DbRevisionConfig.getGenericDebugDir() + "webmill-schema.xml");
        DbStructureExport.export(db, fileOutputStream, true);
        fileOutputStream.flush();
        fileOutputStream.close();
        fileOutputStream = null;
    }
}
