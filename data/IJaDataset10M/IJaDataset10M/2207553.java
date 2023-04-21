package ru.rsdn.rsdnreader.System.mapper.database;

import java.sql.*;
import java.util.*;
import ru.rsdn.Janus.UserRole;
import ru.rsdn.rsdnreader.System.domain.*;
import ru.rsdn.rsdnreader.System.mapper.*;

/**
 * Класс содержит информацию о сообщении в форуме.
 * User: serb Date: 01.05.2006 Time: 11:43:57
 */
public class MessageMapper extends AbstractMapper implements MessageFinder {

    private static Map<Integer, Message> messageList = new HashMap<Integer, Message>();

    /**
     * @param rs
     * @return
     * @throws java.sql.SQLException
     */
    private Message getMessageFromResultSet(ResultSet rs) throws SQLException {
        Calendar lastModerated = Calendar.getInstance();
        lastModerated.clear();
        lastModerated.setTimeInMillis(rs.getTimestamp("lastModerated").getTime());
        Calendar messageDate = Calendar.getInstance();
        messageDate.clear();
        messageDate.setTimeInMillis(rs.getTimestamp("messageDate").getTime());
        Integer id = rs.getInt("id");
        if (rs.wasNull()) id = null;
        Integer articleId = rs.getInt("articleId");
        if (rs.wasNull()) articleId = null;
        Integer forumId = rs.getInt("forumId");
        if (rs.wasNull()) forumId = null;
        String messageText = rs.getString("message");
        String messageName = rs.getString("messageName");
        Integer parentId = rs.getInt("parentId");
        if (rs.wasNull()) parentId = null;
        String subject = rs.getString("subject");
        Integer topicId = rs.getInt("topicId");
        if (rs.wasNull()) topicId = null;
        Integer userId = rs.getInt("userId");
        if (rs.wasNull()) userId = null;
        String userNick = rs.getString("userNick");
        UserRole userRole = null;
        String userTitle = rs.getString("userTitle");
        Integer userTitleColor = rs.getInt("userTitleColor");
        if (rs.wasNull()) userTitleColor = null;
        Message message = new Message(id, articleId, forumId, lastModerated, messageText, messageDate, messageName, parentId, subject, topicId, userId, userNick, userRole, userTitle, userTitleColor);
        message.setChange(Boolean.FALSE);
        getMessageList().put(message.getId(), message);
        return message;
    }

    protected String getSelectSQL() {
        return " select id,articleId,forumId,lastModerated,message,messageDate,messageName," + " parentId,subject,topicId,userId,userNick,userRole,userTitle,userTitleColor" + " from messages where id = ?";
    }

    protected String getInsertSQL() {
        return " insert into " + " messages(id,articleId,forumId,lastModerated,message,messageDate,messageName," + " parentId,subject,topicId,userId,userNick,userRole,userTitle,userTitleColor)" + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    protected String getUpdateSQL() {
        return " update messages" + " set" + " articleId = ?," + " forumId = ?," + " lastModerated = ?," + " message = ?," + " messageDate = ?," + " messageName = ?," + " parentId = ?," + " subject = ?," + " topicId = ?," + " userId = ?," + " userNick = ?," + " userRole = ?," + " userTitle = ?," + " userTitleColor = ?" + " where id = ?";
    }

    protected String getDeleteSQL() {
        return "delete from messages where id = ?";
    }

    protected PreparedStatement getSelectStatement(DomainObject domainObject, PreparedStatement selectStatement) throws SQLException {
        Message message = (Message) domainObject;
        selectStatement.setInt(1, message.getId());
        return selectStatement;
    }

    protected PreparedStatement getInsertStatement(DomainObject domainObject, PreparedStatement insertStatement) throws SQLException {
        Message message = (Message) domainObject;
        insertStatement.setInt(1, message.getId());
        if (message.getArticleId() != null) {
            insertStatement.setInt(2, message.getArticleId());
        } else {
            insertStatement.setNull(2, Types.INTEGER);
        }
        if (message.getForum() != null) {
            insertStatement.setInt(3, message.getForum().getId());
        } else {
            insertStatement.setNull(3, Types.INTEGER);
        }
        if (message.getLastModerated() != null) {
            insertStatement.setTimestamp(4, new Timestamp(message.getLastModerated().getTimeInMillis()));
        } else {
            insertStatement.setNull(4, Types.TIMESTAMP);
        }
        if (message.getMessage() != null && message.getMessage().length() != 0) {
            insertStatement.setString(5, message.getMessage());
        } else {
            insertStatement.setNull(5, Types.VARCHAR);
        }
        if (message.getMessageDate() != null) {
            insertStatement.setTimestamp(6, new Timestamp(message.getMessageDate().getTimeInMillis()));
        } else {
            insertStatement.setNull(6, Types.TIMESTAMP);
        }
        if (message.getMessageName() != null && message.getMessageName().length() != 0) {
            insertStatement.setString(7, message.getMessageName());
        } else {
            insertStatement.setNull(7, Types.VARCHAR);
        }
        if (message.getParent() != null) {
            insertStatement.setInt(8, message.getParent().getId());
        } else {
            insertStatement.setNull(8, Types.INTEGER);
        }
        if (message.getSubject() != null && message.getSubject().length() != 0) {
            insertStatement.setString(9, message.getSubject());
        } else {
            insertStatement.setNull(9, Types.VARCHAR);
        }
        if (message.getTopic() != null) {
            insertStatement.setInt(10, message.getTopic().getId());
        } else {
            insertStatement.setNull(10, Types.INTEGER);
        }
        if (message.getUser() != null) {
            insertStatement.setInt(11, message.getUser().getId());
        } else {
            insertStatement.setNull(11, Types.INTEGER);
        }
        if (message.getUserNick() != null && message.getUserNick().length() != 0) {
            insertStatement.setString(12, message.getUserNick());
        } else {
            insertStatement.setNull(12, Types.VARCHAR);
        }
        if (message.getUserRole() != null && message.getUserRole().length() != 0) {
            insertStatement.setString(13, message.getUserRole());
        } else {
            insertStatement.setNull(13, Types.VARCHAR);
        }
        if (message.getUserTitle() != null && message.getUserTitle().length() != 0) {
            insertStatement.setString(14, message.getUserTitle());
        } else {
            insertStatement.setNull(14, Types.VARCHAR);
        }
        if (message.getUserTitleColor() != null) {
            insertStatement.setInt(15, message.getUserTitleColor());
        } else {
            insertStatement.setNull(15, Types.INTEGER);
        }
        return insertStatement;
    }

    protected PreparedStatement getUpdateStatement(DomainObject domainObject, PreparedStatement updateStatement) throws SQLException {
        Message message = (Message) domainObject;
        updateStatement.setInt(15, message.getId());
        if (message.getArticleId() != null) {
            updateStatement.setInt(1, message.getArticleId());
        } else {
            updateStatement.setNull(1, Types.INTEGER);
        }
        if (message.getForum() != null) {
            updateStatement.setInt(2, message.getForum().getId());
        } else {
            updateStatement.setNull(2, Types.INTEGER);
        }
        if (message.getLastModerated() != null) {
            updateStatement.setTimestamp(3, new Timestamp(message.getLastModerated().getTimeInMillis()));
        } else {
            updateStatement.setNull(3, Types.TIMESTAMP);
        }
        if (message.getMessage() != null && message.getMessage().length() != 0) {
            updateStatement.setString(4, message.getMessage());
        } else {
            updateStatement.setNull(4, Types.VARCHAR);
        }
        if (message.getMessageDate() != null) {
            updateStatement.setTimestamp(5, new Timestamp(message.getMessageDate().getTimeInMillis()));
        } else {
            updateStatement.setNull(5, Types.TIMESTAMP);
        }
        if (message.getMessageName() != null && message.getMessageName().length() != 0) {
            updateStatement.setString(6, message.getMessageName());
        } else {
            updateStatement.setNull(6, Types.VARCHAR);
        }
        if (message.getParent() != null) {
            updateStatement.setInt(7, message.getParent().getId());
        } else {
            updateStatement.setNull(7, Types.INTEGER);
        }
        if (message.getSubject() != null && message.getSubject().length() != 0) {
            updateStatement.setString(8, message.getSubject());
        } else {
            updateStatement.setNull(8, Types.VARCHAR);
        }
        if (message.getTopic() != null) {
            updateStatement.setInt(9, message.getTopic().getId());
        } else {
            updateStatement.setNull(9, Types.INTEGER);
        }
        if (message.getUser() != null) {
            updateStatement.setInt(10, message.getUser().getId());
        } else {
            updateStatement.setNull(10, Types.INTEGER);
        }
        if (message.getUserNick() != null && message.getUserNick().length() != 0) {
            updateStatement.setString(11, message.getUserNick());
        } else {
            updateStatement.setNull(11, Types.VARCHAR);
        }
        if (message.getUserRole() != null && message.getUserRole().length() != 0) {
            updateStatement.setString(12, message.getUserRole());
        } else {
            updateStatement.setNull(12, Types.VARCHAR);
        }
        if (message.getUserTitle() != null && message.getUserTitle().length() != 0) {
            updateStatement.setString(13, message.getUserTitle());
        } else {
            updateStatement.setNull(13, Types.VARCHAR);
        }
        if (message.getUserTitleColor() != null) {
            updateStatement.setInt(14, message.getUserTitleColor());
        } else {
            updateStatement.setNull(14, Types.INTEGER);
        }
        return updateStatement;
    }

    public Message getMessage(Integer id) {
        Message message = getMessageList().get(id);
        if (message != null) {
            return message;
        }
        String SQL = " select id,articleId,forumId,lastModerated,message,messageDate,messageName," + " parentId,subject,topicId,userId,userNick,userRole,userTitle,userTitleColor" + " from messages where id = " + id;
        try {
            ResultSet rs = doFind(SQL);
            if (rs.next()) {
                return getMessageFromResultSet(rs);
            } else {
                getMessageList().put(id, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Кеш для getSubMessage
     */
    private static Map<Integer, Integer> getSubMessageCache = new HashMap<Integer, Integer>();

    public ArrayList<Message> getSubMessage(Integer parentId) {
        ArrayList<Message> messages = new ArrayList<Message>();
        if (getSubMessageCache.containsValue(parentId)) {
            for (Message message : getMessageList().values()) {
                if (message != null && message.getParent() != null && message.getParent().getId().equals(parentId)) messages.add(message);
                if (message != null && message.getParent() == null && parentId == null) messages.add(message);
            }
            return messages;
        }
        getSubMessageCache.put(parentId, parentId);
        String SQL;
        if (parentId == null) SQL = " select id,articleId,forumId,lastModerated,message,messageDate,messageName," + " parentId,subject,topicId,userId,userNick,userRole,userTitle,userTitleColor" + " from messages where parentid is null "; else SQL = " select id,articleId,forumId,lastModerated,message,messageDate,messageName," + " parentId,subject,topicId,userId,userNick,userRole,userTitle,userTitleColor" + " from messages where parentid = " + parentId;
        ResultSet rs;
        try {
            rs = doFind(SQL);
            while (rs.next()) {
                messages.add(getMessageFromResultSet(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messages;
    }

    public void add(Message message) {
        message.setChange(Boolean.TRUE);
        getMessageList().put(message.getId(), message);
    }

    public void commit() throws SQLException {
        doCommit(getMessageList());
    }

    public void init() {
        getMessageList().clear();
        getSubMessageCache.clear();
        try {
            beginTransaction();
            stmt.execute("drop table messages");
            endTransaction();
        } catch (Exception e) {
            try {
                endTransaction();
            } catch (SQLException e1) {
            }
        }
        try {
            beginTransaction();
            stmt.execute("create table messages" + "(" + " id numeric NOT NULL ," + " articleId varchar(100)," + " forumId numeric," + " lastModerated timestamp," + " message text," + " messageDate timestamp," + " messageName varchar(200)," + " parentId numeric," + " subject varchar(100)," + " topicId numeric," + " userId numeric," + " userNick varchar(50)," + " userRole varchar(50)," + " userTitle varchar(200)," + " userTitleColor numeric," + " CONSTRAINT messages_pkey PRIMARY KEY (id))");
            stmt.execute("delete  from messages");
            endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Map<Integer, Message> getMessageList() {
        return messageList;
    }

    public static synchronized void setMessageList(Map<Integer, Message> aMessageList) {
        messageList = aMessageList;
    }
}
