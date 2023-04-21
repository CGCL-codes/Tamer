package com.wangyu001.logic;

import org.sysolar.sun.mvc.support.Pager;
import com.wangyu001.entity.UserBbsMsg;
import com.wangyu001.entity.UserBbsReply;

public interface UserBbsMsgLogic {

    public Long create(UserBbsMsg userBbsMsg) throws Exception;

    public void remove(Long userBbsMsgId) throws Exception;

    public Long createReply(UserBbsReply userBbsReply) throws Exception;

    /**
     * 
     * @param userId 留言列表的主人的ID
     * @param currentUserId 当前用户的ID,用于查询当前用户的悄悄话
     * @param msgType消息类型1:只取公开的留言;2:取所有留言,包括悄悄话
     * @param pager
     * @return
     * @throws Exception
     */
    public Pager<UserBbsMsg> fetchUserBbsMsgList(Long userId, Long currentUserId, int msgType, Pager<UserBbsMsg> pager) throws Exception;
}
