package org.light.portlets.chat.service;

import java.util.List;
import org.light.portal.core.service.BaseService;
import org.light.portlets.chat.model.Chatting;
import org.light.portlets.chat.model.ChattingRecord;
import org.light.portlets.chat.model.ChattingUser;

/**
 * 
 * @author Jianmin Liu
 **/
public interface ChatService extends BaseService {

    public List<ChattingUser> getChattingUsersById(long chattingId);

    public ChattingUser getChattingUsersById(long chattingId, long userId);

    public List<ChattingRecord> getChattingRecordsById(long chattingId);

    public List<ChattingRecord> getChattingRecordsById(long chattingId, int maxRow);

    public Chatting getChattingByUser(long userId);

    public Chatting getChattingById(long chattingId);

    public void leaveChattingByUser(long userId);

    public List<Chatting> getChattingByOrgId(long orgId);

    public boolean deleteChatRoom(long id);
}
