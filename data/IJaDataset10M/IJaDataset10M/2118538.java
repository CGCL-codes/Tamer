package net.mikaboshi.jdbc;

import static net.mikaboshi.validator.SimpleValidator.validatePattern;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import net.mikaboshi.property.PropertyFileLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JDBC/データベースのユーティリティクラス。
 *
 * @author Takuma Umezawa
 */
public final class DbUtils {

    private DbUtils() {
    }

    private static Log logger = LogFactory.getLog(DbUtils.class);

    /**
	 * <p>
	 * プロパティファイルからConnectionを生成する。
	 * </p><p>
	 * プロパティファイルの仕様：
	 * <ul>
	 *   <li>jdbc.driver = ドライバクラス名
	 *   <li>jdbc.url = DBのURL
	 *   <li>jdbc.user = DBのユーザ名
	 *   <li>jdbc.password = DBのパスワード
	 * </ul>
	 * </p>
	 * @param path プロパティファイルのパス
	 * @return 接続したConnectionオブジェクト
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public static Connection getConnection(String path) throws SQLException, IOException, ClassNotFoundException {
        return getConnection(path, "jdbc.driver", "jdbc.url", "jdbc.user", "jdbc.password");
    }

    /**
	 * プロパティファイルからConnectionを生成する。
	 * 
	 * @param path プロパティファイルのパス
	 * @param driverKey プロパティファイル中のドライバクラス名のキー
	 * @param urlKey プロパティファイル中のURLのキー
	 * @param userKey プロパティファイル中のユーザ名のキー
	 * @param passwordKey プロパティファイル中のパスワードのキー
	 * @return 接続したConnectionオブジェクト
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public static Connection getConnection(String path, String driverKey, String urlKey, String userKey, String passwordKey) throws SQLException, IOException, ClassNotFoundException {
        Properties properties = PropertyFileLoader.loadPropertyFile(new File(path));
        String driverName = properties.getProperty(driverKey);
        String url = properties.getProperty(urlKey);
        String user = properties.getProperty(userKey);
        String password = properties.getProperty(passwordKey);
        Class.forName(driverName);
        return DriverManager.getConnection(url, user, password);
    }

    /**
	 * ResultSetをクローズする。
	 * SQLExceptionが発生した場合は、ログに出力する。
	 * @param rs
	 */
    public static void closeQuietly(ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        } catch (SQLException e) {
            logger.error("ResultSetのクローズに失敗しました。", e);
        }
    }

    /**
	 * Statementをクローズする。
	 * SQLExceptionが発生した場合は、ログに出力する。
	 * 
	 * @param stmt
	 */
    public static void closeQuietly(Statement stmt) {
        if (stmt == null) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            logger.error("Statementのクローズに失敗しました。", e);
        }
    }

    /**
	 * PreparedStatementをクローズする。
	 * SQLExceptionが発生した場合は、ログに出力する。
	 * 
	 * @param pstmt
	 */
    public static void closeQuietly(PreparedStatement pstmt) {
        if (pstmt == null) {
            return;
        }
        try {
            pstmt.close();
        } catch (SQLException e) {
            logger.error("PreparedStatementのクローズに失敗しました。", e);
        }
    }

    /**
	 * Connectionをクローズする。
	 * SQLExceptionが発生した場合は、ログに出力する。
	 * @param conn
	 */
    public static void closeQuietly(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            if (conn.isClosed()) {
                return;
            }
            conn.close();
        } catch (SQLException e) {
            logger.error("Connectionのクローズに失敗しました。", e);
        }
    }

    /**
	 * Connectionをロールバックする。
	 * SQLExceptionが発生した場合は、ログに出力する。
	 * @param conn
	 */
    public static void rollbackQuietly(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            if (conn.isClosed()) {
                return;
            }
            conn.rollback();
        } catch (SQLException e) {
            logger.error("ロールバックに失敗しました。", e);
        }
    }

    /**
	 * テーブル名に不正な文字が含まれていないかチェックする。
	 * 
	 * @param tableName
	 * @throws IllegalArgumentException
	 */
    public static void validateTableName(String tableName) {
        validatePattern(tableName, "^[a-zA-Z0-9\\._]{1,30}$", "tableName", IllegalArgumentException.class);
    }
}
