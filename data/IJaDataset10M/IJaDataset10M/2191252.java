package leeon.towthree;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogDBHelper {

    public static String DB_URL = "jdbc:oracle:thin:@localhost:1521:local";

    public static String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";

    public static String DB_USERNAME = "peddy";

    public static String DB_PASSWORD = "peddy";

    private static Log logger = LogFactory.getLog(LogDBHelper.class);

    public static void updateLog(List<LogObject> list) {
        Connection conn = null;
        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            conn.setAutoCommit(false);
            for (LogObject log : list) {
                updateOne(log, conn);
            }
        } catch (Exception e) {
            logger.error("error when update log", e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (SQLException e) {
                logger.error("error close connection", e);
            }
        }
    }

    public static void updateOne(LogObject log, Connection conn) {
        if (log == null || conn == null) return;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        try {
            String sql = "select address from ftp_server where address = ?";
            ps1 = conn.prepareStatement(sql);
            ps1.setString(1, log.getFtpAddress());
            rs1 = ps1.executeQuery();
            if (!rs1.next()) {
                sql = "insert into ftp_server (address, port, username, password, path) values (?, ?, ?, ?, ?)";
                ps2 = conn.prepareStatement(sql);
                ps2.setString(1, log.getFtpAddress());
                ps2.setString(2, log.getFtpPort());
                ps2.setString(3, log.getFtpUser());
                ps2.setString(4, log.getFtpPassword());
                ps2.setString(5, log.getFtpPath());
                ps2.execute();
            }
            sql = "select file_name from ftp_file where address = ? and file_name = ? and file_date = ?";
            ps3 = conn.prepareStatement(sql);
            ps3.setString(1, log.getFtpAddress());
            ps3.setString(2, log.getFileName());
            ps3.setDate(3, dealDate(log.getFileDate(), "yyyy年MM月dd日"));
            rs2 = ps3.executeQuery();
            if (!rs2.next()) {
                sql = "insert into ftp_file (address, file_name, push_date, file_date) values (?, ?, ?, ?)";
                ps4 = conn.prepareStatement(sql);
                ps4.setString(1, log.getFtpAddress());
                ps4.setString(2, log.getFileName());
                ps4.setDate(3, dealDate(log.getPushDate(), "yyyy-MM-dd HH:mm:ss"));
                ps4.setDate(4, dealDate(log.getFileDate(), "yyyy年MM月dd日"));
                ps4.execute();
            }
            conn.commit();
        } catch (Exception e) {
            logger.error("error when update one log:[" + log + "]", e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                logger.error("error when conn rollback", e);
            }
        } finally {
            try {
                if (rs1 != null) rs1.close();
                if (rs2 != null) rs2.close();
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (ps3 != null) ps3.close();
                if (ps4 != null) ps4.close();
            } catch (SQLException e) {
                logger.error("error when rs and ps closing", e);
            }
        }
    }

    private static Date dealDate(String d, String f) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        return new Date(sdf.parse(d).getTime());
    }
}
