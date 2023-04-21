package ces.platform.bbs.mysql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.cnjsp.util.PropMan;
import org.cnjsp.util.PropertyManager;
import ces.platform.bbs.Cnjbb;
import ces.platform.bbs.ForumBoard;
import ces.platform.bbs.ForumConfig;
import ces.platform.bbs.ForumInstall;
import ces.platform.bbs.Group;
import ces.platform.bbs.exception.UnauthorizedException;
import ces.platform.bbs.option.Upgrader;
import ces.platform.bbs.option.mysql.DbUpgrader;
import ces.coral.lang.StringUtil;

public class DbForumInstall extends ForumInstall {

    private static Logger log = Logger.getLogger(DbForumInstall.class.getName());

    private static final String USER_PERMESSION = "INSERT INTO JB_USERPERM (I_BOARDID, I_USERID, I_PERM) VALUES (?, ?, ?)";

    private static final String GROUP_PERMESSION = "INSERT INTO JB_GROUPPERM (I_BOARDID, I_GROUPID, I_PERM) VALUES (?, ?, ?)";

    private static final String CREATE_GROUP = "INSERT INTO JB_GROUP (I_GROUPID, V_NAME, V_DESC) VALUES (?, ?, ?)";

    private static final String CREATE_GROUP_PERM = "INSERT INTO JB_GROUPPERM (I_BOARDID, I_GROUPID, I_PERM) VALUES (?, ?, ?)";

    private static final String ADD_TO_GROUP = "INSERT INTO JB_GROUPUSER (I_USERID, I_ADMIN, I_GROUPID) VALUES (?, ?, ?)";

    private static final String ADD_DEFAULT_COLOR = "INSERT INTO JB_COLOR" + " (V_TITLE, V_DESCRIPTION, V_PAGECOLOR, VPAGEBKCOLOR, V_BORDERCOLOR, V_TITLECOLOR, V_TITLEBKCOLOR, V_CONTENTCOLOR, V_CONTENTBKCOLOR, V_CONTENTBKCOLOR2, V_ALERTCOLOR, V_STICKYCOLOR, V_BESTCOLOR, I_COLORID)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";

    private static final String DEFAULT_PREFIX = "JB_";

    private static final String MYSQL_SQL_FILE = "/mysql.sql";

    private static final String SQLSERVER_SQL_FILE = "/sqlserver.sql";

    private static final String ORACLE_SQL_FILE = "/oracle.sql";

    private static final String[] GROUP_NAME = { Group.SYSTEM_ADMIN, Group.FORUM_ADMIN, Group.MODERATOR, Group.REGISTER_USER };

    private static final String[] GROUP_DESC = { "system admin", "forum admin", "moderator", "register user" };

    private static int systemGroupId = 0;

    private static int registerGroupId = 0;

    private static final boolean[][] GROUP_PERM = { { true, true, true, true, true, true, true, true }, { true, false, true, true, false, true, true, true }, { true, false, false, true, false, true, true, true }, { true, false, false, true, false, false, true, true } };

    private static final boolean[] GUEST_PERM = { true, false, false, false, false, false, false, false };

    public DbForumInstall() {
    }

    public boolean createDatabase() throws Exception {
        boolean b = false;
        Iterator iterator = getSQL().iterator();
        try {
            Connection conn = DbForumFactory.getConnection();
            while (iterator.hasNext()) {
                PreparedStatement pstmt = conn.prepareStatement((String) iterator.next());
                pstmt.executeUpdate();
                pstmt.close();
            }
            conn.close();
            b = true;
        } catch (SQLException e) {
            b = false;
            log.error(e);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return b;
    }

    private List getSQL() {
        PropertyManager pm = PropMan.getInstance(Cnjbb.PROPERTY_FILEANME);
        String prefix = pm.getProperty("database.table_prefix");
        prefix = (prefix != null) ? prefix.trim() : prefix;
        List list = null;
        String databaseType = pm.getProperty("database.type");
        if (databaseType.equals("mysql")) {
            list = getMysqlSQL(prefix);
        } else if (databaseType.equals("sqlserver")) {
            list = getSqlserverSQL(prefix);
        } else if (databaseType.equals("oracle")) {
            list = getOracleSQL(prefix);
        }
        return list;
    }

    private List getMysqlSQL(String prefix) {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer();
        InputStream fis = getClass().getResourceAsStream(MYSQL_SQL_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String temp = null;
        boolean comm = false;
        try {
            while ((temp = br.readLine()) != null) {
                if (temp.startsWith("/*")) {
                    if (!temp.endsWith("*/")) {
                        comm = true;
                    }
                } else if (temp.startsWith("#")) {
                } else if (comm) {
                    if (temp.endsWith("*/")) {
                        comm = false;
                    }
                } else {
                    sb.append(temp);
                    if (temp.endsWith(";")) {
                        list.add(sql(sb.toString(), prefix));
                        sb = new StringBuffer();
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
        return list;
    }

    private List getSqlserverSQL(String prefix) {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer();
        InputStream fis = getClass().getResourceAsStream(SQLSERVER_SQL_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String temp = null;
        boolean comm = false;
        try {
            while ((temp = br.readLine()) != null) {
                if (temp.startsWith("/*")) {
                    if (!temp.endsWith("*/")) {
                        comm = true;
                    }
                } else if (temp.startsWith("#")) {
                } else if (comm) {
                    if (temp.endsWith("*/")) {
                        comm = false;
                    }
                } else {
                    if (temp.endsWith("go")) {
                        list.add(sql(sb.toString(), prefix));
                        sb = new StringBuffer();
                    } else {
                        sb.append(temp);
                        sb.append("\n");
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
        return list;
    }

    private List getOracleSQL(String prefix) {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer();
        InputStream fis = getClass().getResourceAsStream(ORACLE_SQL_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String temp = null;
        boolean comm = false;
        try {
            while ((temp = br.readLine()) != null) {
                if (temp.startsWith("/*")) {
                    if (!temp.endsWith("*/")) {
                        comm = true;
                    }
                } else if (temp.startsWith("#")) {
                } else if (comm) {
                    if (temp.endsWith("*/")) {
                        comm = false;
                    }
                } else {
                    if (temp.endsWith("/")) {
                        list.add(sql(sb.toString(), prefix));
                        sb = new StringBuffer();
                    } else {
                        sb.append(temp);
                        sb.append("\n");
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
        return list;
    }

    public boolean initDatabase() throws Exception {
        boolean b = false;
        int[] groupID = new int[GROUP_NAME.length];
        Connection conn = null;
        PreparedStatement pstmt = null;
        PropertyManager pm = PropMan.getInstance(Cnjbb.PROPERTY_FILEANME);
        String prefix = pm.getProperty("database.table_prefix");
        prefix = (prefix != null) ? prefix.trim() : prefix;
        try {
            DbSequenceManager.reload();
            conn = DbForumFactory.getConnection();
            pstmt = conn.prepareStatement(this.sql(USER_PERMESSION, prefix));
            for (int i = 0; i < GUEST_PERM.length; i++) {
                if (GUEST_PERM[i]) {
                    pstmt.setInt(1, 0);
                    pstmt.setInt(2, -1);
                    pstmt.setInt(3, i);
                    pstmt.executeUpdate();
                    pstmt.clearParameters();
                }
            }
            pstmt.close();
            pstmt = conn.prepareStatement(this.sql(CREATE_GROUP, prefix));
            for (int i = 0; i < GROUP_NAME.length; i++) {
                groupID[i] = DbSequenceManager.nextID(DbSequenceManager.GROUP);
                if (GROUP_NAME[i].equals(Group.SYSTEM_ADMIN)) {
                    systemGroupId = groupID[i];
                } else if (GROUP_NAME[i].equals(Group.REGISTER_USER)) {
                    registerGroupId = groupID[i];
                }
                pstmt.setInt(1, groupID[i]);
                pstmt.setString(2, GROUP_NAME[i]);
                pstmt.setString(3, GROUP_DESC[i]);
                pstmt.executeUpdate();
                pstmt.clearParameters();
            }
            pstmt.close();
            pstmt = conn.prepareStatement(this.sql(CREATE_GROUP_PERM, prefix));
            for (int i = 0; i < GROUP_NAME.length; i++) {
                for (int j = 0; j < GROUP_PERM[i].length; j++) {
                    if (GROUP_PERM[i][j]) {
                        pstmt.setInt(1, ForumBoard.BOARD_HOME);
                        pstmt.setInt(2, groupID[i]);
                        pstmt.setInt(3, j);
                        pstmt.executeUpdate();
                        pstmt.clearParameters();
                    }
                }
            }
            pstmt.close();
            pstmt = conn.prepareStatement(this.sql(ADD_DEFAULT_COLOR, prefix));
            DbForumColor color = new DbForumColor();
            pstmt.setString(1, color.getTitle());
            pstmt.setString(2, color.getDescription());
            pstmt.setString(3, color.getPageColor());
            pstmt.setString(4, color.getPageBkColor());
            pstmt.setString(5, color.getBorderColor());
            pstmt.setString(6, color.getTitleColor());
            pstmt.setString(7, color.getTitleBkColor());
            pstmt.setString(8, color.getContentColor());
            pstmt.setString(9, color.getContentBkColor());
            pstmt.setString(10, color.getContentBkColor2());
            pstmt.setString(11, color.getAlertColor());
            pstmt.setString(12, color.getStickyColor());
            pstmt.setString(13, color.getMarrowColor());
            pstmt.executeUpdate();
            b = true;
        } catch (SQLException e) {
            log.error(e);
            e.printStackTrace();
        } finally {
            DbForumFactory.closeDB(null, pstmt, null, conn);
        }
        return b;
    }

    public boolean testConnection(String pool, String ds, String driver, String url, String username, String password) {
        Connection conn = null;
        boolean b = false;
        try {
            conn = DbForumFactory.getConnection(pool, ds, driver, url, username, password, false);
            b = (conn != null);
        } catch (Exception e) {
            log.error(e);
            b = false;
        } finally {
            DbForumFactory.closeDB(null, null, null, conn);
        }
        return b;
    }

    /**
     * @see ces.platform.bbs.ForumInstall#isInConvert()
     */
    public boolean isInConvert() {
        return false;
    }

    /**
     * @see ces.platform.bbs.ForumInstall#isOutConvert()
     */
    public boolean isOutConvert() {
        return false;
    }

    /**
     * @see ces.platform.bbs.ForumInstall#getForumConfig()
     */
    public ForumConfig getForumConfig() {
        return DbForumConfig.getInstance(null);
    }

    /**
     * @see ces.platform.bbs.ForumInstall#createAdministrator()
     */
    public boolean createAdministrator(String username, String password, String email, String question, String answer) {
        boolean b = true;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PropertyManager pm = PropMan.getInstance(Cnjbb.PROPERTY_FILEANME);
        String prefix = pm.getProperty("database.table_prefix");
        prefix = (prefix != null) ? prefix.trim() : prefix;
        try {
            DbUser user = new DbUser(null, username, password, email, question, answer);
            DbUserProps userProps = new DbUserProps(user);
            userProps.save();
            conn = DbForumFactory.getConnection();
            pstmt = conn.prepareStatement(sql(ADD_TO_GROUP, prefix));
            pstmt.setInt(1, user.getID());
            pstmt.setInt(2, 1);
            pstmt.setInt(3, systemGroupId);
            pstmt.addBatch();
            pstmt.setInt(3, registerGroupId);
            pstmt.addBatch();
            pstmt.executeBatch();
            DbForumConfig forumConfig = (DbForumConfig) getForumConfig();
            forumConfig.setUserCount(1);
            forumConfig.setlastRegisterUser(user);
        } catch (Exception e) {
            b = false;
            e.printStackTrace();
        } finally {
            DbForumFactory.closeDB(null, pstmt, null, conn);
        }
        return b;
    }

    private String sql(String str, String prefix) {
        if (prefix != null) {
            if (prefix.length() > 0 && !prefix.equals(DEFAULT_PREFIX)) {
                str = StringUtil.replaceAll(str, DEFAULT_PREFIX, prefix);
                ;
            }
        }
        return str;
    }

    public Upgrader getUpgrader(int type) throws UnauthorizedException {
        return DbUpgrader.getInstance(type);
    }

    public Upgrader getUpgrader() throws UnauthorizedException {
        return DbUpgrader.getInstance();
    }
}
