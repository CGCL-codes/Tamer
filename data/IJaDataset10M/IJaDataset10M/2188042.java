package com.ufnasoft.dms.server.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;
import org.apache.log4j.Logger;

public class Message extends Database {

    Logger logger1 = Logger.getLogger(Message.class);

    public String replyMessage(String username, String key, int taskid, int documentid, int messageto, String subject, String message) {
        String rvalue = "no";
        int userid = getUserIdWithUsernameKey(username, key);
        try {
            con = getConnection();
            if (con != null) {
                String filedatetime = getCurrentDateTime();
                stmt = con.createStatement();
                sql = "INSERT INTO usdms_messages (documentid, subject, message, messagefrom, messageto, messagedatetime) VALUES (" + documentid + ",'" + subject + "', '" + message + "', " + userid + "," + messageto + ",'" + filedatetime + "')";
                stmt.executeUpdate(sql);
                rvalue = "yes";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rvalue;
    }

    /**
	 * 
	 * @param username
	 * @param key
	 * @param messagetype
	 * @param messageid
	 * @return
	 */
    public String deleteMessageById(String username, String key, String messagetype, int messageid) {
        String rvalue = "no";
        int userid = getUserIdWithUsernameKey(username, key);
        try {
            con = getConnection();
            if (con != null) {
                stmt = con.createStatement();
                if (messagetype.equalsIgnoreCase("inbox")) sql = "DELETE FROM usdms_messages WHERE messageTo = " + userid + "  AND messageid = " + messageid; else sql = "DELETE FROM usdms_messages WHERE messageFrom = " + userid + "  AND messageid = " + messageid;
                stmt.executeUpdate(sql);
                rvalue = "yes";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rvalue;
    }

    public StringBuffer getMessageById(String username, String key, String messagetype, int messageid) {
        StringBuffer sb = new StringBuffer();
        int userid = getUserIdWithUsernameKey(username, key);
        try {
            con = getConnection();
            if (con != null) {
                stmt = con.createStatement();
                if (messagetype.equalsIgnoreCase("inbox")) {
                    sql = "SELECT m.messageid,  m.subject, m.message, u.firstname, u.lastname, m.messagedatetime, d.documentid, d.title, d.filename,p.project  FROM usdms_messages m  INNER JOIN usdms_users u ON m.messageFrom = u.userid INNER JOIN usdms_documents d ON m.documentid = d.documentid INNER JOIN usdms_projects p ON d.projectid= p.projectid WHERE m.messageTo = " + userid + " AND messageid =" + messageid;
                } else {
                    sql = "SELECT m.messageid,  m.subject, m.message ,u.firstname, u.lastname, m.messagedatetime, d.documentid, d.title, d.filename, p.project  FROM usdms_messages m  INNER JOIN usdms_users u ON m.messageTo = u.userid INNER JOIN usdms_documents d ON m.documentid = d.documentid INNER JOIN usdms_projects p ON d.projectid= p.projectid WHERE m.messageFrom = " + userid + " AND messageid =" + messageid;
                }
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    sb.append("<msg>");
                    sb.append("<messagetype>");
                    sb.append(messagetype);
                    sb.append("</messagetype>");
                    sb.append("<messageid>");
                    sb.append(rs.getInt("messageid"));
                    sb.append("</messageid>");
                    sb.append("<subject>");
                    sb.append(rs.getString("subject"));
                    sb.append("</subject>");
                    sb.append("<firstname>");
                    sb.append(rs.getString("firstname"));
                    sb.append("</firstname>");
                    sb.append("<lastname>");
                    sb.append(rs.getString("lastname"));
                    sb.append("</lastname>");
                    String messagedatetime = rs.getString("messagedatetime").substring(19);
                    sb.append("<messagedatetime>");
                    sb.append(messagedatetime);
                    sb.append("</messagedatetime>");
                    sb.append("<message>");
                    sb.append(rs.getString("message"));
                    sb.append("</message>");
                    sb.append("<documentid>");
                    sb.append(rs.getString("documentid"));
                    sb.append("</documentid>");
                    sb.append("<title>");
                    sb.append(rs.getString("title"));
                    sb.append("</title>");
                    sb.append("<filename>");
                    sb.append(rs.getString("filename"));
                    sb.append("</filename>");
                    sb.append("<project>");
                    sb.append(rs.getString("project"));
                    sb.append("</project>");
                    sb.append("</msg>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    public StringBuffer getMessages(String username, String key, String messagetype, String orderby) {
        StringBuffer sb = new StringBuffer();
        int userid = getUserIdWithUsernameKey(username, key);
        try {
            con = getConnection();
            if (con != null) {
                stmt = con.createStatement();
                if (messagetype.equalsIgnoreCase("inbox")) {
                    sql = "SELECT m.messageid, m.documentid,  m.subject, m.messageFrom, m.messageTo, u.firstname, u.lastname, m.messagedatetime FROM usdms_messages m  INNER JOIN usdms_users u ON m.messageFrom = u.userid WHERE m.messageTo = " + userid + " ORDER BY  " + orderby;
                } else {
                    sql = "SELECT m.messageid, m.documentid, m.subject,m.messageFrom, m.messageTo, u.firstname, u.lastname, m.messagedatetime FROM usdms_messages m  INNER JOIN usdms_users u ON m.messageTo = u.userid WHERE m.messageFrom = " + userid + " ORDER BY  " + orderby;
                }
                rs = stmt.executeQuery(sql);
                String messagedatetime = "";
                while (rs.next()) {
                    sb.append("<message>");
                    sb.append("<messageid>");
                    sb.append(rs.getInt("messageid"));
                    sb.append("</messageid>");
                    sb.append("<documentid>");
                    sb.append(rs.getInt("documentid"));
                    sb.append("</documentid>");
                    sb.append("<messagefrom>");
                    sb.append(rs.getInt("messagefrom"));
                    sb.append("</messagefrom>");
                    sb.append("<messageto>");
                    sb.append(rs.getInt("messageto"));
                    sb.append("</messageto>");
                    sb.append("<subject>");
                    sb.append(rs.getString("subject"));
                    sb.append("</subject>");
                    sb.append("<firstname>");
                    sb.append(rs.getString("firstname"));
                    sb.append("</firstname>");
                    sb.append("<lastname>");
                    sb.append(rs.getString("lastname"));
                    sb.append("</lastname>");
                    sb.append("<messagedatetime>");
                    sb.append(rs.getString("messagedatetime").substring(0, 19));
                    sb.append("</messagedatetime>");
                    sb.append("</message>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
}
