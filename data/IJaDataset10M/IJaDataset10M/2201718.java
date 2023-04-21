package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import beans.MessageBean;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class MessageControl {

    private static ResultSet rs;

    private static Connection conn;

    public static Vector<MessageBean> getInboxMessage(String to, String type) {
        Vector<MessageBean> inboxVector = new Vector<MessageBean>();
        try {
            conn = (Connection) DatabaseConnection.connection();
            String sqltext = "SELECT * FROM `message` WHERE `to` = ? AND `type` = ? AND `receiverdelete` = 0" + " ORDER BY `message`.`date` DESC ";
            PreparedStatement pstmt1 = (PreparedStatement) conn.prepareStatement(sqltext);
            pstmt1.setString(1, to);
            pstmt1.setString(2, type);
            rs = pstmt1.executeQuery();
            while (rs.next()) {
                MessageBean message = new MessageBean();
                message.setTo(rs.getString("to"));
                message.setFrom(rs.getString("from"));
                message.setDate(rs.getString("date"));
                message.setSubject(rs.getString("subject"));
                message.setMessageText(rs.getString("messageText"));
                message.setType(rs.getString("type"));
                message.setIsReceiverRead(Integer.parseInt(rs.getString("isReceiverRead")));
                inboxVector.add(message);
            }
            rs.close();
            pstmt1.close();
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inboxVector;
    }

    /**
	 * Get all the new messages that user haven't read yet
	 */
    public static Vector<MessageBean> getNewInboxMessage(String to, String type) {
        conn = (Connection) DatabaseConnection.connection();
        Vector<MessageBean> inboxVector = new Vector<MessageBean>();
        try {
            String sqltext = "SELECT * FROM `message` WHERE `to` = ? AND `type` = ? AND `receiverdelete` = 0" + " AND `isReceiverRead` = 0" + " ORDER BY `message`.`date` DESC ";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sqltext);
            pstmt.setString(1, to);
            pstmt.setString(2, type);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MessageBean message = new MessageBean();
                message.setTo(rs.getString("to"));
                message.setFrom(rs.getString("from"));
                message.setDate(rs.getString("date"));
                message.setSubject(rs.getString("subject"));
                message.setMessageText(rs.getString("messageText"));
                message.setType(rs.getString("type"));
                message.setIsReceiverRead(Integer.parseInt(rs.getString("isReceiverRead")));
                inboxVector.add(message);
            }
            rs.close();
            pstmt.close();
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inboxVector;
    }

    /**
	 * get all user sent messages 
	 */
    public static Vector<MessageBean> getSentfMessage(String from, String type) {
        conn = (Connection) DatabaseConnection.connection();
        Vector<MessageBean> sentboxVector = new Vector<MessageBean>();
        try {
            String sqltext = "SELECT * FROM `message` WHERE `from` = ? AND `type` = ? AND `senderdelete` = 0" + " ORDER BY `message`.`date` DESC ";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sqltext);
            pstmt.setString(1, from);
            pstmt.setString(2, type);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MessageBean message = new MessageBean();
                message.setTo(rs.getString("to"));
                message.setFrom(rs.getString("from"));
                message.setDate(rs.getString("date"));
                message.setSubject(rs.getString("subject"));
                message.setMessageText(rs.getString("messageText"));
                message.setType(rs.getString("type"));
                sentboxVector.add(message);
            }
            rs.close();
            pstmt.close();
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sentboxVector;
    }

    /**
	 * get message detail 
	 */
    public static MessageBean getMessageDetail(String to, String from, String type, String date) {
        conn = (Connection) DatabaseConnection.connection();
        MessageBean message = new MessageBean();
        try {
            String sqltext = "SELECT * FROM `message` WHERE `date` = ? AND `to` = ? AND `from` = ? AND `type` = ?";
            String updateString = "UPDATE `message` SET `isReceiverRead` = 1 WHERE `date` = ? AND `to` = ?" + " AND `from` = ? AND `type` = ?";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sqltext);
            pstmt.setString(1, date);
            pstmt.setString(2, to);
            pstmt.setString(3, from);
            pstmt.setString(4, type);
            PreparedStatement pstmt2 = (PreparedStatement) conn.prepareStatement(updateString);
            pstmt2.setString(1, date);
            pstmt2.setString(2, to);
            pstmt2.setString(3, from);
            pstmt2.setString(4, type);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                message.setTo(to);
                message.setFrom(from);
                message.setDate(date);
                message.setSubject(rs.getString("subject"));
                message.setMessageText(rs.getString("messageText"));
                message.setType(type);
                message.setNetworkId(Integer.parseInt(rs.getString("networkId")));
            }
            pstmt2.executeUpdate();
            rs.close();
            pstmt.close();
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
	 * Create new message
	 */
    public static boolean createMessage(MessageBean message) {
        conn = (Connection) DatabaseConnection.connection();
        String insertString = "";
        Boolean messageCreated = false;
        String to = message.getTo();
        String from = message.getFrom();
        String subject = message.getSubject();
        String messageText = message.getMessageText();
        String type = message.getType();
        int networkId = message.getNetworkId();
        try {
            if (type.equalsIgnoreCase("Message")) {
                insertString = "INSERT INTO `message` (`to`, `from`, `date`, " + "`subject`, `messagetext`, `type`," + " `senderdelete`, `receiverdelete`)" + " VALUES( ?,?,SYSDATE(),?,?,?, 0, 0)";
                PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertString);
                pstmt.setString(1, to);
                pstmt.setString(2, from);
                pstmt.setString(3, subject);
                pstmt.setString(4, messageText);
                pstmt.setString(5, type);
                pstmt.executeUpdate();
                pstmt.close();
            } else if (type.equalsIgnoreCase("RejectMessage")) {
                insertString = "INSERT INTO `message` (`to`, `from`, `date`, " + "`subject`, `messagetext`, `type`," + " `senderdelete`, `receiverdelete`)" + " VALUES( ?,?,SYSDATE(),?,?,'MessageBean', 1, 0)";
                PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertString);
                pstmt.setString(1, to);
                pstmt.setString(2, from);
                pstmt.setString(3, subject);
                pstmt.setString(4, messageText);
                pstmt.executeUpdate();
                pstmt.close();
            } else if (type.equalsIgnoreCase("Invitation")) {
                insertString = "INSERT INTO `message` (`to`, `from`, `date`, " + "`subject`, `messagetext`, `type`," + " `senderdelete`, `receiverdelete`)" + " VALUES( ?,?,SYSDATE(),?,?,?, 1, 0)";
                PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertString);
                pstmt.setString(1, to);
                pstmt.setString(2, from);
                pstmt.setString(3, subject);
                pstmt.setString(4, messageText);
                pstmt.setString(5, type);
                pstmt.executeUpdate();
                pstmt.close();
            } else if (type.equalsIgnoreCase("NetworkInvitation")) {
                insertString = "INSERT INTO `message` (`to`, `from`, `date`, " + "`subject`, `messagetext`, `type`," + " `senderdelete`, `receiverdelete`, `networkId`)" + " VALUES( ?,?,SYSDATE(),?,?, '" + "Invitation" + "', 1, 0,?)";
                PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertString);
                pstmt.setString(1, to);
                pstmt.setString(2, from);
                pstmt.setString(3, subject);
                pstmt.setString(4, messageText);
                pstmt.setInt(5, networkId);
                pstmt.executeUpdate();
                pstmt.close();
            } else if (type.equalsIgnoreCase("bulletin")) {
                insertString = "INSERT INTO `message` (`to`, `from`, `date`, " + "`subject`, `messagetext`, `type`," + " `senderdelete`, `receiverdelete`)" + " VALUES(?,?,SYSDATE(),?,?,?, 1, 0)";
                PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertString);
                pstmt.setString(1, from);
                pstmt.setString(2, from);
                pstmt.setString(3, subject);
                pstmt.setString(4, messageText);
                pstmt.setString(5, type);
                pstmt.executeUpdate();
                pstmt.close();
            }
            DatabaseConnection.closeConnection();
            messageCreated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageCreated;
    }

    public static boolean senderDeleteMessage(MessageBean message) {
        conn = (Connection) DatabaseConnection.connection();
        Boolean senderMessageDeleted = false;
        String to = message.getTo();
        String from = message.getFrom();
        String date = message.getDate();
        String type = message.getType();
        try {
            String updateString = "UPDATE `message` SET `senderdelete` = 1 WHERE (`from` = ? " + " AND `to` = ? AND `date` = ? AND `type` = ?)";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(updateString);
            pstmt.setString(1, from);
            pstmt.setString(2, to);
            pstmt.setString(3, date);
            pstmt.setString(4, type);
            pstmt.executeUpdate();
            pstmt.close();
            String deleteString = "DELETE FROM `message` WHERE (`from` = ?" + " AND `to` = ? AND `date` = ? AND `type` = ? AND `senderdelete` = 1" + " AND `receiverdelete` = 1 )";
            PreparedStatement pstmt2 = (PreparedStatement) conn.prepareStatement(deleteString);
            pstmt2.setString(1, from);
            pstmt2.setString(2, to);
            pstmt2.setString(3, date);
            pstmt2.setString(4, type);
            pstmt2.executeUpdate();
            pstmt.close();
            DatabaseConnection.closeConnection();
            senderMessageDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return senderMessageDeleted;
    }

    public static boolean receiverDeleteMessage(MessageBean message) {
        conn = (Connection) DatabaseConnection.connection();
        Boolean receiverMessageDeleted = false;
        String to = message.getTo();
        String from = message.getFrom();
        String date = message.getDate();
        String type = message.getType();
        try {
            String updateString = "UPDATE `message` SET `receiverdelete` = 1 WHERE (`from` = ?" + " AND `to` =? AND `date` = ? AND `type` = ? AND `networkId` = -1)";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(updateString);
            pstmt.setString(1, from);
            pstmt.setString(2, to);
            pstmt.setString(3, date);
            pstmt.setString(4, type);
            pstmt.executeUpdate();
            pstmt.close();
            String deleteString = "DELETE FROM `message` WHERE (`from`= ? AND `to` = ? AND `date` = ? AND `type` = ?" + " AND `senderdelete` = 1" + " AND `receiverdelete` = 1 " + " AND `networkId` = -1)";
            PreparedStatement pstmt2 = (PreparedStatement) conn.prepareStatement(deleteString);
            pstmt2.setString(1, from);
            pstmt2.setString(2, to);
            pstmt2.setString(3, date);
            pstmt2.setString(4, type);
            pstmt2.executeUpdate();
            pstmt.close();
            DatabaseConnection.closeConnection();
            receiverMessageDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receiverMessageDeleted;
    }

    public static boolean deleteNetworkInvitaion(MessageBean message) {
        conn = (Connection) DatabaseConnection.connection();
        Boolean networkInvitaionDeleted = false;
        String to = message.getTo();
        String from = message.getFrom();
        String date = message.getDate();
        String type = message.getType();
        int networkId = message.getNetworkId();
        try {
            String deleteString = "DELETE FROM `message` WHERE (`from` = ?" + " AND `to` = ? AND `date` = ? AND `type` = ? AND `networkId` = ?)";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(deleteString);
            pstmt.setString(1, from);
            pstmt.setString(2, to);
            pstmt.setString(3, date);
            pstmt.setString(4, type);
            pstmt.setInt(5, networkId);
            pstmt.executeUpdate();
            pstmt.close();
            DatabaseConnection.closeConnection();
            networkInvitaionDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return networkInvitaionDeleted;
    }

    /**
	 * check if friend invitation already exists
	 */
    public static boolean checkFriendInvitationExistance(String to, String from) {
        conn = (Connection) DatabaseConnection.connection();
        boolean invitationExists = false;
        try {
            String sqltext = "SELECT * FROM `message` WHERE `to` = ? AND `type` = 'Invitation' " + "AND `from` = ? AND `networkId` = -1";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sqltext);
            pstmt.setString(1, to);
            pstmt.setString(2, from);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                invitationExists = true;
            }
            rs.close();
            pstmt.close();
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invitationExists;
    }

    /**
	 * check if network invitation already exists
	 */
    public static boolean checkNetworkInvitationExistance(String to, String from, int networkId) {
        conn = (Connection) DatabaseConnection.connection();
        boolean invitationExists = false;
        try {
            String sqltext = "SELECT * FROM `message` WHERE `to` = ? AND `type` = 'Invitation' " + "AND `from` = ? AND `networkId`=?";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sqltext);
            pstmt.setString(1, to);
            pstmt.setString(2, from);
            pstmt.setInt(3, networkId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                invitationExists = true;
            }
            rs.close();
            pstmt.close();
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invitationExists;
    }
}
