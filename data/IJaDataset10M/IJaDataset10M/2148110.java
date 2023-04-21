package ces.platform.bbs.option.mysql.tableupgrader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import ces.platform.infoplat.utils.Function;
import ces.platform.bbs.option.mysql.DbUpgrader;
import ces.coral.lang.StringUtil;

public class V10ToV20BookmarkUpgrader extends DbTableUpgrader {

    private static Logger log = Logger.getLogger(V10ToV20BookmarkUpgrader.class);

    private static final String TABLE_NAME = "bookmark";

    private static final String[] FROM_INDEX_COLUMNS = { "id", "username" };

    private static final String SQL_FROM_LOAD = "SELECT b.url, b.addtime, u.UserID" + " FROM bookmark b" + " LEFT JOIN user u ON b.username = u.UserName" + " ORDER BY b.id ASC";

    private static final String SQL_TO_STORE = "INSERT INTO " + DbUpgrader.V20_DB_PREFIX_MODULE + "BOOKMARK" + " (I_USERID, I_THREADID, C_ADDTIME)" + " VALUES (?, ?, ?)";

    public String getTableName() {
        return TABLE_NAME;
    }

    public int upgrade() {
        int count = 0;
        Connection fromConn = DbUpgrader.getV10Connection();
        Connection toConn = DbUpgrader.getV20Connection();
        PreparedStatement fromPstmt = null;
        PreparedStatement toPstmt = null;
        ResultSet rs = null;
        try {
            fromPstmt = fromConn.prepareStatement(SQL_FROM_LOAD);
            toPstmt = toConn.prepareStatement(SQL_TO_STORE);
            rs = fromPstmt.executeQuery();
            while (rs.next()) {
                String url = rs.getString(1);
                String date = rs.getString(2);
                int userID = rs.getInt(3);
                date = StringUtil.replaceAll(StringUtil.replaceAll(StringUtil.replaceAll(date, "-", ""), ":", ""), " ", "") + "000";
                int threadID = 0;
                url = Function.stringToArray(url, "\\?")[1];
                String[] params = Function.stringToArray(url, "&");
                for (int i = 0; i < params.length; i++) {
                    if (params[i].startsWith("rootID")) {
                        try {
                            threadID = Integer.parseInt(Function.stringToArray(params[i], "=")[1]);
                        } catch (Exception e) {
                        }
                    }
                }
                if (threadID == 0) {
                    continue;
                }
                try {
                    toPstmt.clearParameters();
                    toPstmt.setInt(1, userID);
                    toPstmt.setInt(2, threadID);
                    toPstmt.setString(3, date);
                    toPstmt.executeUpdate();
                    count++;
                } catch (Exception e) {
                    log.error(e);
                }
            }
            rs.close();
        } catch (SQLException e) {
            log.error(e);
            e.printStackTrace();
        } finally {
            DbUpgrader.closeDB(rs, fromPstmt, null, fromConn);
            DbUpgrader.closeDB(null, toPstmt, null, toConn);
        }
        return count;
    }

    protected String[] getNeedIndexColumns() {
        return FROM_INDEX_COLUMNS;
    }

    protected Logger getLogger() {
        return log;
    }
}
