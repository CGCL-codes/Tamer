package net.spamcomplaint.database;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import net.spamcomplaint.App;
import net.spamcomplaint.mail.MimeMessageParser;
import net.spamcomplaint.util.StringUtils;

public class MessageUniqueness {

    private Connection conn = null;

    private String databaseLocation;

    MessageDigest md;

    private MessageUniqueness(String databaseLocation) throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        this.databaseLocation = databaseLocation;
        md = MessageDigest.getInstance("SHA1");
    }

    private static final void log(String s) {
        App.log.info("MessageUniqueness: " + s);
    }

    public Connection connection() throws SQLException {
        if (conn == null) {
            String url = "jdbc:derby:" + databaseLocation + ";create=true";
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(true);
        }
        return conn;
    }

    public void close() throws SQLException {
        try {
            if (conn != null) conn.close();
        } finally {
            conn = null;
        }
    }

    /**
     * Get The Hash value as a Hex-String. Calling this method resets the object's state to initial state.
     * @return String representing the Hashvalue in hexadecimal format.
     */
    public String digout() {
        byte[] digest = md.digest();
        if (digest != null) return StringUtils.hexEncode(digest); else return null;
    }

    int msgCount = 0;

    Map nameMap = new HashMap();

    Map digestMap = new HashMap();

    void updateDate(int junkId, Date msgDate) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE SC_Email_Junk SET Msg_Date = ? WHERE Junk_Id = ?");
        try {
            if (msgDate == null) msgDate = new Date();
            ps.setTimestamp(1, new java.sql.Timestamp(msgDate.getTime()));
            ps.setInt(2, junkId);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    void updateDigout(int junkId, String digout) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE SC_Email_Junk SET Unique_SHA1 = ? WHERE Junk_Id = ?");
        try {
            ps.setString(1, digout);
            ps.setInt(2, junkId);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private String processMessage(int junkId, String msg) throws MessagingException, SQLException {
        msgCount++;
        MimeMessage spamMsg = new MimeMessage(null, new ByteArrayInputStream(msg.getBytes()));
        String str = null;
        StringBuffer headers = new StringBuffer();
        Enumeration e = spamMsg.getAllHeaders();
        md.reset();
        while (e.hasMoreElements()) {
            Header h = (Header) e.nextElement();
            String name = h.getName().trim().toLowerCase();
            String value = h.getValue();
            Integer i = (Integer) nameMap.get(name);
            if (i == null) i = new Integer(1); else i = new Integer(i.intValue() + 1);
            nameMap.put(name, i);
            headers.append(name + ':' + value + '\n');
            if (name.equals("date")) str = value;
            if (name.equals("received") || name.equals("from") || name.equals("to")) md.update(value.getBytes());
        }
        String digout = digout();
        if (digestMap.get(digout) != null) {
            log(junkId + ", " + digout + " !!!digest is not unique: " + digout);
            log(headers.toString());
        }
        digestMap.put(digout, Boolean.TRUE);
        if (str == null) {
            log(junkId + ", missing");
        } else log(junkId + ", message date text string " + str);
        Date msgDate = MimeMessageParser.getDate(spamMsg);
        log(junkId + ", " + msgDate);
        updateDate(junkId, msgDate);
        return digout;
    }

    void stats() {
        log("total = " + msgCount);
    }

    private void runAllJunkMail() throws SQLException, MessagingException, IOException {
        connection();
        try {
            Statement st = conn.createStatement();
            try {
                ResultSet rs = st.executeQuery("SELECT Junk_Id, Msg, Msg_Date FROM SC_Email_Junk");
                try {
                    long time = System.currentTimeMillis();
                    while (rs.next()) {
                        try {
                            int junkId = rs.getInt(1);
                            String msg = rs.getString(2);
                            log(junkId + ", database Msg_Date = " + rs.getTimestamp(3));
                            processMessage(junkId, msg);
                        } catch (Exception e) {
                            App.log.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                    log("milli seconds elapsed: " + ((System.currentTimeMillis() - time)));
                    stats();
                } finally {
                    rs.close();
                }
            } finally {
                st.close();
            }
        } finally {
            close();
        }
    }

    public static final void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("\n  Usage: path-to-derby-db");
            System.exit(1);
        }
        MessageUniqueness m = new MessageUniqueness(args[0]);
        m.runAllJunkMail();
    }
}
