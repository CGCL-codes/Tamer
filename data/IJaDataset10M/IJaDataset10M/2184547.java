package data.dao;

import gui.common.msg.SysMessage;
import java.sql.SQLException;
import java.util.List;
import com.ibatis.sqlmap.client.SqlMapException;
import data.Count;
import data.Forum;
import data.Message;

public interface MessageDao {

    public SysMessage createNewMessage(Message msg) throws SQLException;

    public SysMessage deleteMessage(Message msg) throws SQLException;

    public SysMessage updateMessage(Message msg) throws SqlMapException;

    public List<Message> getMessageList(Forum forum) throws SQLException;

    public Count getMessageCount(Forum forum) throws SQLException;

    public List<Message> getMesaList(Forum forum, int start, int end) throws SQLException;

    public Message getMessageById(int id) throws SQLException;
}
