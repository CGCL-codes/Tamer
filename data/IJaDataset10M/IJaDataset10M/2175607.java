package com.jedi;

import com.tss.util.DbConn;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Message extends BaseObj {

    public Message() {
    }

    public Message(String id) {
        this.listId = id;
    }

    public void delete() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            conn.setAutoCommit(false);
            if (getMsgType() == 0) {
                sql = "delete from t_message_board where reply_id = ?";
                conn.prepare(sql);
                conn.setString(1, getId());
                conn.executeUpdate();
                conn.commit();
                return;
            }
            sql = "delete from t_message_board where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.executeUpdate();
            sql = "update t_message_board set reply_num = reply_num - 1 where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getReplyId());
            conn.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception e) {
                ex.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }

    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            if (getId().trim().equals("")) setId(KeyGen.nextID(""));
            if (getMsgType() == 0) setReplyId(getId());
            String sql = "insert into t_message_board (" + " list_id,msg_title,msg_info,author_id,author_name," + " msg_type,reply_id,reply_num,issue_time" + " ) values (?,?,?,?,?,?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.setString(2, getMsgTitle());
            conn.setString(3, getMsgInfo());
            conn.setString(4, getAuthorId());
            conn.setString(5, getAuthorName());
            conn.setInt(6, getMsgType());
            conn.setString(7, getReplyId());
            conn.setInt(8, getReplyNum());
            conn.setString(9, getIssueTime());
            conn.executeUpdate();
            if (getMsgType() == 1) {
                sql = "update t_message_board set reply_num = reply_num + 1 where list_id = ?";
                conn.prepare(sql);
                conn.setString(1, getReplyId());
                conn.executeUpdate();
            }
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception e) {
                ex.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }

    private void setId(String listId) {
        this.listId = listId;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public String getId() {
        return listId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public String getMsgInfo() {
        return msgInfo;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getReplyId() {
        return replyId;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public String getIssueTime() {
        return issueTime;
    }

    private String listId = "";

    private String msgTitle = "";

    private String msgInfo = "";

    private String authorId = "";

    private String authorName = "";

    private int msgType = 0;

    private String replyId = "";

    private int replyNum = 0;

    private String issueTime = "";
}
