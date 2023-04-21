package org.pittjug.jspbeans;

import java.util.List;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.pittjug.sql.pool.PoolManager;
import org.pittjug.email.automated.AutomatedEmail;
import org.pittjug.config.Site;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pittjug.sql.util.SqlHelper;
import org.pittjug.sql.util.TablePrimaryKeys;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
import javax.mail.MessagingException;

/**
 *Provides information for a Member of the site.  Is also used as the JSP Bean to
 * retreive information to add Members and to Edit their Profile.
 * @author Carl Trusiak
 * @author Khurram Merchant
 * @author Sudha Jagannath
 * @version 1.0
 * @isElementExists
 */
public class Member {

    private static Log log = LogFactory.getLog(Member.class);

    /**
     *  Connect to the database and return a valid user object
     *  @param username The username
     *  @param password The user password
     *  @return The user or null if the user is not valid
     */
    public static Member login(String loginName, String password) throws SQLException {
        return login(loginName, digestPassword(password));
    }

    /**
     *  Connect to the database and return a valid user object
     *  @param username The username
     *  @param password The user password
     *  @return The user or null if the user is not valid
     */
    public static Member login(String loginName, byte[] password) throws SQLException {
        Member m = null;
        if (loginName != null && password != null) {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rset = null;
            String sqlSelect = "select * from MEMBER where loginName=? AND Password=?";
            try {
                conn = PoolManager.getConnection();
                stmt = conn.prepareStatement(sqlSelect);
                stmt.setString(1, loginName);
                stmt.setBytes(2, password);
                rset = stmt.executeQuery();
                if (rset.next()) {
                    m = new Member();
                    m.populate(rset);
                    m.emailAddressList = EmailAddress.findFor(m.loginName, conn);
                }
            } catch (SQLException se) {
                if (log.isErrorEnabled()) log.error("Sql Error on Find Member", se);
                throw se;
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                        stmt = null;
                    }
                } catch (SQLException se) {
                }
                try {
                    if (rset != null) {
                        rset.close();
                        rset = null;
                    }
                } catch (SQLException se) {
                }
                try {
                    if (conn != null) {
                        PoolManager.freeConnection(conn);
                        conn = null;
                    }
                } catch (SQLException se) {
                }
            }
        }
        return m;
    }

    /**
     *  Populates this Member Object with data retrieved from the database.
     *  @param rset The ResultSet containing Member information
     *  @throws SQLException if on occurs during population.
     */
    public void populate(ResultSet rset) throws SQLException {
        this.loginName = rset.getString("loginName");
        this.memberName = rset.getString("memberName");
        this.emailAddressVisibleToOthers = (rset.getInt("emailAddressVisibleToOthers") == 1);
    }

    /**
     *  Validates this new Member and adds them to the Member Table.
     *  @throws ValidationException if one occurs
     */
    public void add() throws ValidationException, SQLException {
        validate(true);
        Connection conn = null;
        PreparedStatement stmt = null;
        String initPassword = generatePassword();
        try {
            conn = PoolManager.getConnection();
            conn.setAutoCommit(false);
            String sqlInsert = "Insert INTO member (LoginName, Password, MemberName, EmailAddressVisibleToOthers) VALUES (?, ?, ?, ?)";
            if (log.isInfoEnabled()) log.info(sqlInsert.toString());
            stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, loginName);
            stmt.setBytes(2, digestPassword(initPassword));
            stmt.setString(3, memberName);
            stmt.setInt(4, (emailAddressVisibleToOthers) ? 1 : 0);
            stmt.execute();
            EmailAddress.update(this, emailAddressList, conn);
            conn.commit();
            sendNewPasswordEmail(initPassword);
        } catch (SQLException se) {
            try {
                conn.rollback();
            } catch (SQLException ser) {
            }
            if (log.isErrorEnabled()) log.error("Sql Error on Update Member", se);
            throw se;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException se) {
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    PoolManager.freeConnection(conn);
                    conn = null;
                }
            } catch (SQLException se) {
            }
        }
    }

    /**
     *  Validates this Member and updates the Member Table.
     *  @throws ValidationException if one occurs
     */
    public void update() throws ValidationException, SQLException {
        validate(false);
        boolean success = false;
        Connection conn = null;
        Statement stmt = null;
        StringBuffer sqlUpdate = new StringBuffer("UPDATE Member set MemberName='");
        sqlUpdate.append(SqlHelper.escapeForSql(memberName));
        sqlUpdate.append("', EmailAddressVisibleToOthers=");
        sqlUpdate.append((emailAddressVisibleToOthers ? 1 : 0));
        sqlUpdate.append(" where LoginName = '");
        sqlUpdate.append(SqlHelper.escapeForSql(loginName));
        sqlUpdate.append("'");
        if (log.isInfoEnabled()) log.info(sqlUpdate.toString());
        try {
            conn = PoolManager.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            stmt.execute(sqlUpdate.toString());
            EmailAddress.update(this, emailAddressList, conn);
            conn.commit();
        } catch (SQLException se) {
            try {
                conn.rollback();
            } catch (SQLException ser) {
            }
            if (log.isErrorEnabled()) log.error("Sql Error on Update Member", se);
            throw se;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException se) {
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    PoolManager.freeConnection(conn);
                    conn = null;
                }
            } catch (SQLException se) {
            }
        }
    }

    /**
     *  Updates this Members password.
     *  @throws SQLException if one occurs
     */
    public void updatePassword(String newPassword) throws SQLException {
        updatePassword(digestPassword(newPassword));
    }

    /**
     *  Updates this Members password.
     *  @throws SQLException if one occurs
     */
    public void updatePassword(byte[] newPassword) throws SQLException {
        boolean success = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        String sqlUpdate = "UPDATE Member set password=? where LoginName =?";
        if (log.isInfoEnabled()) log.info(sqlUpdate.toString());
        try {
            conn = PoolManager.getConnection();
            stmt = conn.prepareStatement(sqlUpdate);
            stmt.setBytes(1, newPassword);
            stmt.setString(2, loginName);
            stmt.execute();
        } catch (SQLException se) {
            if (log.isErrorEnabled()) log.error("Sql Error on Update Members password", se);
            throw se;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException se) {
            }
            try {
                if (conn != null) {
                    PoolManager.freeConnection(conn);
                    conn = null;
                }
            } catch (SQLException se) {
            }
        }
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isEmailAddressVisibleToOthers() {
        return emailAddressVisibleToOthers;
    }

    public void setEmailAddressVisibleToOthers(boolean emailAddressVisibleToOthers) {
        this.emailAddressVisibleToOthers = emailAddressVisibleToOthers;
    }

    public List getSubscribedLists() {
        return subscribedLists;
    }

    public void setSubscribedLists(List subscribedLists) {
        this.subscribedLists = subscribedLists;
    }

    public List getOwnedLists() {
        return ownedLists;
    }

    public void setOwnedLists(List ownedLists) {
        this.ownedLists = ownedLists;
    }

    public List getModeratedLists() {
        return moderatedLists;
    }

    public void setModeratedLists(List moderatedLists) {
        this.moderatedLists = moderatedLists;
    }

    public List getEmailAddressList() {
        return emailAddressList;
    }

    public void setEmailAddressList(List emailAddressList) {
        this.emailAddressList = emailAddressList;
    }

    private void validateUnique(ValidationException ve) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        StringBuffer sqlSelect = new StringBuffer("select * from MEMBER where loginName='");
        sqlSelect.append(SqlHelper.escapeForSql(loginName));
        sqlSelect.append("'");
        if (log.isInfoEnabled()) log.info(sqlSelect.toString());
        try {
            conn = PoolManager.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sqlSelect.toString());
            if (rset.next()) {
                ve.addException("The Member Login Name : " + this.loginName + " Exists. If this is you and you have forgotten your password, use the password reminder to obtain it. Otherwise, chose a different Login Name");
            }
        } catch (SQLException se) {
            if (log.isErrorEnabled()) log.error("Sql Error on Validate Unique Member", se);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException se) {
            }
            try {
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
            } catch (SQLException se) {
            }
            try {
                if (conn != null) {
                    PoolManager.freeConnection(conn);
                    conn = null;
                }
            } catch (SQLException se) {
            }
        }
    }

    private void validate(boolean newMember) throws ValidationException {
        ValidationException ve = new ValidationException();
        if (this.loginName == null) {
            ve.addException("You must provide a Login Name");
        } else {
            if (newMember) validateUnique(ve);
            if (loginName.length() > 16) ve.addException("Login Name cannot exceed 16 characters");
        }
        if (this.emailAddressList == null || this.emailAddressList.size() == 0) {
            ve.addException("You must provide an Email Address");
        } else {
            EmailAddress.validateEmailAddressList(ve, this.getEmailAddressList(), loginName);
        }
        if (this.getMemberName() == null) {
            ve.addException("You must provide a Full name for display.");
        } else {
            if (this.memberName.length() > 127) ve.addException("Your Display name exceeds 127 characters, the system cannot use these.");
        }
        if (log.isDebugEnabled()) log.debug(ve.getMessage() + " : " + ve.hasAnExceptionOccured());
        if (ve.hasAnExceptionOccured()) throw ve;
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress != null) {
            if (this.emailAddressList == null) this.emailAddressList = new ArrayList();
            StringTokenizer st = new StringTokenizer(emailAddress, ",");
            while (st.hasMoreTokens()) {
                String add = st.nextToken();
                emailAddressList.add(new EmailAddress(add));
                log.debug(add);
            }
        }
    }

    /**
     * Returns the list of e-mail addresses as a comma-separated String list
     *
     * @return The list as a string
     */
    public String getEmailAddress() {
        return EmailAddress.commaSeperateList(this.emailAddressList);
    }

    public String generatePassword() {
        int i = loginName.hashCode();
        Random r = new Random(i);
        i = r.nextInt();
        return Integer.toHexString(i);
    }

    public static void main(String[] args) {
        System.out.println(digestPassword("catmanmecatmanme"));
        System.out.println(digestPassword("zzzzzzzz").length);
    }

    public boolean isInitPassword(String password) {
        return password.equals(generatePassword());
    }

    private void sendNewPasswordEmail(String initPassword) {
        try {
            Hashtable ht = new Hashtable();
            ht.put("site.name", "Pittjug ListServe Site");
            ht.put("member.name", this.memberName);
            ht.put("member.login", this.loginName);
            ht.put("member.password", initPassword);
            ht.put("site.login.url", "some.url");
            AutomatedEmail email = AutomatedEmail.getInstance("NewMember");
            email.setMessage(ht);
            ht = new Hashtable();
            ht.put("site.name", "Pittjug ListServe Site");
            email.setSubject(ht);
            email.send(this.getEmailAddress(), Site.getMailStoreAddress());
        } catch (MessagingException me) {
            log.error("Message not sent", me);
        }
    }

    /**
     * Digests the Password using MD5 Disgestion.
     */
    public static byte[] digestPassword(String password) {
        byte[] b = password.getBytes();
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            synchronized (digest) {
                digest.reset();
                b = digest.digest(password.getBytes());
            }
        } catch (java.security.NoSuchAlgorithmException nsa) {
        }
        return b;
    }

    private String memberName;

    private String loginName;

    private boolean emailAddressVisibleToOthers;

    private List subscribedLists;

    private List ownedLists;

    private List moderatedLists;

    private String emailAddress;

    private List emailAddressList;
}
