package vqwiki;

import java.sql.*;
import java.util.Date;

public class DatabaseVersionManger implements VersionManager {

    protected static final String STATEMENT_VERSION_FIND = "SELECT * FROM TopicVersion WHERE name = ? ORDER BY versionat";

    protected static final String STATEMENT_VERSION_FIND_ONE = "SELECT * FROM TopicVersion WHERE name = ? AND versionAt = ?";

    protected static PreparedStatement versionFindStatement, versionFindStatementOne;

    protected static Connection conn;

    protected static DatabaseVersionManger instance;

    protected static org.apache.log4j.Category cat = org.apache.log4j.Category.getInstance(DatabaseVersionManger.class);

    private DatabaseVersionManger() throws Exception {
        if (conn != null) if (!conn.isClosed()) conn.close();
        Class.forName(Environment.getInstance().getDriver());
        conn = DriverManager.getConnection(Environment.getInstance().getUrl(), Environment.getInstance().getUserName(), Environment.getInstance().getPassword());
        versionFindStatement = conn.prepareStatement(STATEMENT_VERSION_FIND);
        versionFindStatementOne = conn.prepareStatement(STATEMENT_VERSION_FIND_ONE);
    }

    public static DatabaseVersionManger getInstance() throws Exception {
        if (instance == null) instance = new DatabaseVersionManger();
        return instance;
    }

    public Object lookupLastRevision(String topicName) throws Exception {
        return lookupRevision(topicName, 0);
    }

    public Object lookupRevision(String topicName, int version) throws Exception {
        if (version < 0) throw new Exception("version # must be >= 0");
        versionFindStatement.setString(1, topicName);
        ResultSet rs = versionFindStatement.executeQuery();
        for (int i = 0; i <= version; i++) {
            if (!rs.next()) return null;
        }
        Timestamp stamp = rs.getTimestamp("versionat");
        cat.debug("Revision #" + version + " @" + stamp);
        return stamp;
    }

    public String revisionContents(String topicName, Timestamp date) throws Exception {
        versionFindStatementOne.setString(1, topicName);
        versionFindStatementOne.setTimestamp(2, date);
        ResultSet rs = versionFindStatementOne.executeQuery();
        if (!rs.next()) return null;
        String contents = rs.getString("contents");
        cat.debug("Contents @" + date + ": " + contents);
        return contents;
    }

    public void saveCache() throws Exception {
    }

    public boolean loadCache() throws Exception {
        return false;
    }

    public void cacheMostRecentVersion(String topicName, String versionKey) {
    }

    public String diff(String topicName, int revision1, int revision2) throws Exception {
        cat.debug("Diff for version " + revision1 + " against version " + revision2);
        String contents1 = revisionContents(topicName, (Timestamp) lookupRevision(topicName, revision1));
        String contents2 = revisionContents(topicName, (Timestamp) lookupRevision(topicName, revision2));
        Diff diff = new Diff();
        cat.debug("Diffing: " + contents1 + " against: " + contents2);
        if (contents1 == null) return diff.getStringDiff("", contents2); else if (contents2 == null) return diff.getStringDiff("", contents1); else return diff.getStringDiff(contents1, contents2);
    }

    public Date lastRevisionDate(String topicName) throws Exception {
        Timestamp stamp = (Timestamp) lookupLastRevision(topicName);
        if (stamp == null) return null;
        return new Date(stamp.getTime());
    }
}
