package server.mwmysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import common.CampaignData;
import server.campaign.CampaignMain;

public class PhpBBConnector {

    Connection con = null;

    private String userGroupTable = "";

    private String groupsTable = "";

    private String tablePrefix = CampaignMain.cm.getServer().getConfigParam("PHPBB_TABLE_PREFIX");

    private String bbVersion = "0";

    private String userTable = "";

    private int bbMajorVersion = Integer.parseInt(CampaignMain.cm.getServer().getConfigParam("PHPBB_MAJOR_VERSION"));

    private String bbUrl = "";

    public void close() {
        CampaignData.mwlog.dbLog("Attempting to close MySQL phpBB Connection");
        try {
            this.con.close();
        } catch (SQLException e) {
            CampaignData.mwlog.dbLog("SQL Exception in PhpBBConnector.close: " + e.getMessage());
            CampaignData.mwlog.errLog("SQL Exception in PhpBBConnector.close: ");
            CampaignData.mwlog.errLog(e);
        }
    }

    private boolean userExistsInForum(String name) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("SELECT COUNT(*) as numusers from " + userTable + " WHERE username = ?");
                    ps.setString(1, name);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("numusers") > 0) exists = true; else exists = false;
                    } else {
                        exists = false;
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQL Error in PhpBBConnector.userExistsInForum: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                    exists = false;
                } finally {
                    if (rs != null) try {
                        rs.close();
                    } catch (SQLException e) {
                    }
                    if (ps != null) try {
                        ps.close();
                    } catch (SQLException e) {
                    }
                }
                break;
        }
        return exists;
    }

    private boolean emailExistsInForum(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("SELECT COUNT(*) as numusers from " + userTable + " WHERE user_email = ?");
                    ps.setString(1, email);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("numusers") > 0) exists = true; else exists = false;
                    } else {
                        exists = false;
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.emailExistsInForum: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                    exists = false;
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
        return exists;
    }

    private boolean userExistsInForum(String name, String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("SELECT COUNT(*) as numusers from " + userTable + " WHERE username = ? AND user_email = ?");
                    ps.setString(1, name);
                    ps.setString(2, email);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("numusers") > 0) exists = true; else exists = false;
                    } else {
                        exists = false;
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQL Error in PhpBBConnector.userExistsInForum: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                    exists = false;
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
        return exists;
    }

    private boolean userExistsInForum(String name, String email, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("SELECT COUNT(*) as numusers from " + userTable + " WHERE username = ? AND user_email = ? AND user_password=MD5(?)");
                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, password);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("numusers") > 0) exists = true; else exists = false;
                    } else {
                        exists = false;
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.userExistsInForum(String, String, String): " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                    try {
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                    } catch (SQLException ex) {
                        CampaignData.mwlog.dbLog(ex);
                    }
                    exists = false;
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
        return exists;
    }

    public synchronized void deleteForumAccount(int forumID) {
        PreparedStatement ps = null;
        switch(bbMajorVersion) {
            case 3:
                ResultSet rs = null;
                Runtime runtime = Runtime.getRuntime();
                try {
                    String userName = "";
                    ps = con.prepareStatement("SELECT username from " + userTable + " WHERE user_id = ?");
                    rs = ps.executeQuery();
                    rs.next();
                    userName = rs.getString("username");
                    try {
                        rs.close();
                        ps.close();
                    } catch (SQLException e) {
                    }
                    if (userName != "") {
                        String fs = System.getProperty("file.separator");
                        String[] call = { "PHPBB3" + fs + "delUser.php", userName };
                        runtime.exec(call);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case 2:
                try {
                    ps = con.prepareStatement("DELETE from " + userGroupTable + " WHERE user_id = " + forumID);
                    ps.executeUpdate();
                    ps.executeUpdate("DELETE from " + userTable + " WHERE user_id = " + forumID);
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.deleteForumAccount: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                } finally {
                    if (ps != null) try {
                        ps.close();
                    } catch (SQLException e) {
                    }
                }
                break;
        }
    }

    public void addToHouseForum(int userID, int houseForumID) {
        if (userID < 1) {
            CampaignData.mwlog.dbLog("User ID < 1 in addToHouseForum, exiting");
            return;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        switch(bbMajorVersion) {
            case 3:
                Runtime runtime = Runtime.getRuntime();
                String fs = System.getProperty("file.separator");
                String[] call = { "PHPBB3" + fs + "addUserToGroup.php", Integer.toString(userID), Integer.toString(houseForumID) };
                try {
                    runtime.exec(call);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case 2:
                try {
                    ps = con.prepareStatement("SELECT count(*) as num from " + userGroupTable + " WHERE group_id = " + houseForumID + " AND user_id = " + userID);
                    rs = ps.executeQuery();
                    if (rs.next()) if (rs.getInt("num") > 0) removeFromHouseForum(userID, houseForumID);
                    rs.close();
                    ps.close();
                    ps = con.prepareStatement("INSERT into " + userGroupTable + " set group_id = " + houseForumID + ", user_id = " + userID + ", user_pending=0");
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.addToHouseForum: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
    }

    public void removeFromHouseForum(int userID, int forumID) {
        if (userID < 1) {
            CampaignData.mwlog.dbLog("User ID < 1 in removeFromHouseForum, exiting");
            return;
        }
        PreparedStatement ps = null;
        switch(bbMajorVersion) {
            case 3:
                Runtime runtime = Runtime.getRuntime();
                String fs = System.getProperty("file.separator");
                String[] call = { "PHPBB3" + fs + "removeUserFromGroup.php", Integer.toString(userID), Integer.toString(forumID) };
                try {
                    runtime.exec(call);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case 2:
                try {
                    ps = con.prepareStatement("DELETE from " + userGroupTable + " WHERE group_id = " + forumID + " AND user_id = " + userID);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.removeFromHouseForum: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
    }

    public int getHouseForumID(String houseForumName) {
        int forumID = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("SELECT group_id from " + groupsTable + " WHERE group_name = ?");
                    ps.setString(1, houseForumName);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        forumID = rs.getInt("group_id");
                    }
                    rs.close();
                    ps.close();
                    CampaignData.mwlog.dbLog("Searching for forumID for house " + houseForumName + ": " + forumID);
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.getHouseForumID: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
        return forumID;
    }

    public int getUserForumID(String userName, String userEmail) {
        int userID = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("SELECT user_id from " + userTable + " WHERE (username = ? AND user_email = ?)");
                    ps.setString(1, userName);
                    ps.setString(2, userEmail);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        userID = rs.getInt("user_id");
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.getUserForumID: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
        return userID;
    }

    public boolean addToForum(String name, String pass, String email) {
        boolean toReturn = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        switch(bbMajorVersion) {
            case 3:
                if (userExistsInForum(name)) {
                    if (userExistsInForum(name, email, pass)) {
                        addActivationKey(getUserForumID(name, email), name);
                        toReturn = this.sendEmailValidation(getUserForumID(name, email), email, getActivationKey(getUserForumID(name, email)));
                    } else if (userExistsInForum(name, email)) {
                        addActivationKey(getUserForumID(name, email), name);
                        toReturn = this.sendEmailValidation(getUserForumID(name, email), email, getActivationKey(getUserForumID(name, email)));
                    } else {
                        CampaignMain.cm.toUser("This name is already registered to a different email address.", name, true);
                        return false;
                    }
                } else if (emailExistsInForum(email)) {
                    CampaignMain.cm.toUser("This email address is already registered to another user.", name, true);
                    return false;
                } else {
                    Runtime runtime = Runtime.getRuntime();
                    String fs = System.getProperty("file.separator");
                    String[] call = { "PHPBB3" + fs + "createUser.php", name, pass, email };
                    try {
                        runtime.exec(call);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case 2:
                if (userExistsInForum(name)) {
                    if (userExistsInForum(name, email, pass)) {
                        addActivationKey(getUserForumID(name, email), name);
                        toReturn = this.sendEmailValidation(getUserForumID(name, email), email, getActivationKey(getUserForumID(name, email)));
                    } else if (userExistsInForum(name, email)) {
                        addActivationKey(getUserForumID(name, email), name);
                        toReturn = this.sendEmailValidation(getUserForumID(name, email), email, getActivationKey(getUserForumID(name, email)));
                    } else {
                        CampaignMain.cm.toUser("This name is already registered to a different email address.", name, true);
                        return false;
                    }
                } else if (emailExistsInForum(email)) {
                    CampaignMain.cm.toUser("This email address is already registered to another user.", name, true);
                    return false;
                } else {
                    StringBuffer sql = new StringBuffer();
                    try {
                        switch(bbMajorVersion) {
                            case 2:
                                if (bbVersion.equalsIgnoreCase(".0.22")) {
                                    sql.append("INSERT into " + userTable + " set ");
                                    sql.append("user_active = 0, ");
                                    sql.append("username = ?, ");
                                    sql.append("user_password = MD5(?), ");
                                    sql.append("user_session_time = 0, ");
                                    sql.append("user_session_page = 0, ");
                                    sql.append("user_lastvisit = 0, ");
                                    sql.append("user_regdate = ?, ");
                                    sql.append("user_email = ?, ");
                                    sql.append("user_id = ?");
                                    ps = con.prepareStatement(sql.toString());
                                    ps.setString(1, name);
                                    ps.setString(2, pass);
                                    ps.setLong(3, (int) (System.currentTimeMillis() / 1000));
                                    ps.setString(4, email);
                                    int userID = 0;
                                    Statement stmt = con.createStatement();
                                    rs = stmt.executeQuery("SELECT MAX(user_id) as total FROM " + userTable);
                                    if (rs.next()) {
                                        userID = rs.getInt("total") + 1;
                                    }
                                    if (userID == 0) {
                                        break;
                                    }
                                    ps.setInt(5, userID);
                                    ps.executeUpdate();
                                    stmt.close();
                                    ps.close();
                                    ps = con.prepareStatement("INSERT INTO " + groupsTable + " (group_name, group_description, group_single_user, group_moderator) VALUES ('', 'Personal User', 1, 0)", PreparedStatement.RETURN_GENERATED_KEYS);
                                    ps.executeUpdate();
                                    rs = ps.getGeneratedKeys();
                                    rs.next();
                                    int groupID = rs.getInt(1);
                                    ps.close();
                                    ps = con.prepareStatement("INSERT INTO " + userGroupTable + " (user_id, group_id, user_pending) VALUES (?, ?, 0)");
                                    ps.setInt(1, userID);
                                    ps.setInt(2, groupID);
                                    ps.executeUpdate();
                                    addActivationKey(userID, name);
                                    toReturn = sendEmailValidation(userID, email, getActivationKey(userID));
                                }
                                break;
                            default:
                                break;
                        }
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                    } catch (SQLException e) {
                        CampaignData.mwlog.dbLog("SQL Error in PhpBBConnector.addToForum: " + e.getMessage());
                        CampaignData.mwlog.dbLog(e);
                    } finally {
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (SQLException e) {
                            }
                        }
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (SQLException e) {
                            }
                        }
                    }
                }
                break;
        }
        if (toReturn) {
            StringBuilder text = new StringBuilder();
            text.append("Your forum account has been created.  You can log in to the forum at " + bbUrl + ".<br />");
            text.append("You will be receiving an email with an activation code.  You can validate your email address by using the ValidateEmail command.");
            CampaignMain.cm.toUser(text.toString(), name, true);
        }
        return toReturn;
    }

    private String getBBConfigVar(String varName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String ret = null;
        try {
            String sql = "SELECT config_value from " + tablePrefix + "config WHERE config_name = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, varName);
            rs = ps.executeQuery();
            if (rs.next()) {
                ret = rs.getString("config_value");
                rs.close();
                ps.close();
            }
        } catch (SQLException e) {
            CampaignData.mwlog.dbLog("SQL Error in PhpBBConnector.getBBConfigVar: " + e.getMessage());
            CampaignData.mwlog.dbLog(e);
            ret = null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        }
        return ret;
    }

    public void init() {
        this.bbVersion = getBBConfigVar("version");
        this.bbUrl = CampaignMain.cm.getServer().getConfigParam("PHPBB_URL");
        switch(bbMajorVersion) {
            case 3:
                this.groupsTable = tablePrefix + "groups";
                this.userGroupTable = tablePrefix + "user_group";
                this.userTable = tablePrefix + "users";
                CampaignData.mwlog.dbLog("Valid phpBB Version: 3");
                break;
            case 2:
                if (bbVersion.equalsIgnoreCase(".0.22")) {
                    this.groupsTable = tablePrefix + "groups";
                    this.userGroupTable = tablePrefix + "user_group";
                    this.userTable = tablePrefix + "users";
                    CampaignData.mwlog.dbLog("Valid phpBB Version: 2.0.22");
                } else {
                    CampaignData.mwlog.dbLog("Unsupported phpBB Version");
                    CampaignMain.cm.turnOffBBSynch();
                }
                break;
            default:
                CampaignData.mwlog.dbLog("Unsupported phpBB Version");
                CampaignMain.cm.turnOffBBSynch();
                break;
        }
    }

    public boolean sendEmailValidation(int userID, String emailAddress, String activationKey) {
        File file = new File("./data/activationemail.txt");
        if (!file.exists()) {
            CampaignData.mwlog.errLog("/data/activationemail.txt does not exist");
            return false;
        }
        String line = "";
        String subject = "";
        String mailFrom = "";
        StringBuilder body = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader dis = new BufferedReader(new InputStreamReader(fis));
            while (dis.ready()) {
                line = dis.readLine();
                if (line.startsWith("[SUBJECT]")) {
                    subject = line;
                    subject = subject.replace("[SUBJECT]", "");
                } else if (line.startsWith("[MAILFROM]")) {
                    mailFrom = line;
                    mailFrom = mailFrom.replace("[MAILFROM]", "");
                } else {
                    body.append(line + "\n");
                }
            }
            dis.close();
            fis.close();
        } catch (FileNotFoundException fnfe) {
            CampaignData.mwlog.errLog("FileNotFoundException in PhpBBConnector.sendActivationEmail: " + fnfe.getMessage());
            return false;
        } catch (IOException ioe) {
            CampaignData.mwlog.errLog("IOException in PhpBBConnector.sendActivationEmail: " + ioe.getMessage());
            return false;
        }
        String bodyString = body.toString().replaceAll("%USERACTKEY%", activationKey);
        Properties props = new Properties();
        String smtphost = null;
        if ((smtphost = CampaignMain.cm.getServer().getConfigParam("MAILHOST")) == null) {
            CampaignData.mwlog.errLog("MAILHOST not set in serverconfig");
            CampaignMain.cm.doSendModMail("NOTE", "MAILHOST not set in serverconfig.");
            return false;
        }
        String protocol = "smtp";
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", Boolean.toString(Boolean.parseBoolean(CampaignMain.cm.getServer().getConfigParam("MAILPASSREQUIRED"))));
        props.put("mail.smtp.host", smtphost);
        props.put("mail.from", mailFrom);
        Session session = Session.getInstance(props, null);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO, emailAddress);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(bodyString);
            if (Boolean.parseBoolean(props.get("mail.smtp.auth").toString())) {
                Transport trans = session.getTransport(protocol);
                trans.connect(CampaignMain.cm.getServer().getConfigParam("MAILUSER"), CampaignMain.cm.getServer().getConfigParam("MAILPASS"));
                trans.sendMessage(msg, msg.getAllRecipients());
            } else {
                Transport.send(msg);
            }
        } catch (MessagingException e) {
            CampaignData.mwlog.errLog("Email send failed:");
            CampaignData.mwlog.errLog(e);
            return false;
        }
        return true;
    }

    private String addActivationKey(int userID, String userName) {
        PreparedStatement ps = null;
        String toReturn = "";
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("UPDATE " + userTable + " SET user_actkey = MD5(?) WHERE user_id = ?");
                    ps.setString(1, userName + Long.toString(System.currentTimeMillis()));
                    ps.setInt(2, userID);
                    ps.executeUpdate();
                    toReturn = getActivationKey(userID);
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.addActivationKey: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                    try {
                        if (ps != null) ps.close();
                    } catch (SQLException ex) {
                        CampaignData.mwlog.dbLog(ex);
                    }
                }
                break;
        }
        return toReturn;
    }

    public String getActivationKey(int userID) {
        String toReturn = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("SELECT user_actkey from " + userTable + " WHERE user_id = ?");
                    ps.setInt(1, userID);
                    rs = ps.executeQuery();
                    if (rs.next()) toReturn = rs.getString("user_actkey");
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    try {
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                    } catch (SQLException ex) {
                    }
                }
                break;
        }
        return toReturn;
    }

    public void validateUser(int forumID) {
        PreparedStatement ps = null;
        switch(bbMajorVersion) {
            case 3:
                break;
            case 2:
                try {
                    ps = con.prepareStatement("UPDATE " + userTable + " SET user_active = 1 WHERE user_id = " + forumID);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.validateUser: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
    }

    public void changeForumName(String oldname, String newname) {
        PreparedStatement ps = null;
        switch(bbMajorVersion) {
            case 3:
            case 2:
                try {
                    ps = con.prepareStatement("UPDATE " + userTable + " SET username = ? WHERE username = ?");
                    ps.setString(1, newname);
                    ps.setString(2, oldname);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    CampaignData.mwlog.dbLog("SQLException in PhpBBConnector.changeForumName: " + e.getMessage());
                    CampaignData.mwlog.dbLog(e);
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                        }
                    }
                }
                break;
        }
    }

    public PhpBBConnector() {
        String url = "jdbc:mysql://" + CampaignMain.cm.getServer().getConfigParam("PHPBB_HOST") + "/" + CampaignMain.cm.getServer().getConfigParam("PHPBB_DB") + "?user=" + CampaignMain.cm.getServer().getConfigParam("PHPBB_USER") + "&password=" + CampaignMain.cm.getServer().getConfigParam("PHPBB_PASS");
        CampaignData.mwlog.dbLog("Attempting phpBB Connection");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            CampaignData.mwlog.dbLog("ClassNotFoundException: " + e.getMessage());
            CampaignData.mwlog.dbLog(e);
        }
        try {
            this.con = DriverManager.getConnection(url);
            if (con != null) CampaignData.mwlog.dbLog("phpBB Connection established");
        } catch (SQLException ex) {
            CampaignData.mwlog.dbLog("SQLException in PhpBBConnector: " + ex.getMessage());
            CampaignData.mwlog.dbLog(ex);
        }
    }
}
