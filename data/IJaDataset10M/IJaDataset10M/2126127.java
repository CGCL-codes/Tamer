package com.wwg.cms.service;

import java.util.Collection;
import com.wwg.cms.bo.*;

/**
 * 
 * @author wwl
 */
public interface MessageService extends java.io.Serializable {

    /**
	 *获取所有
	 */
    public Collection getMessageList();

    /**
	 * 添加
	 * @param message 
	 * @return
	 */
    public Message addMessage(Message message);

    /**
	 * 修改
	 * @param message 
	 * @return
	 */
    public Message updateMessage(Message message);

    /**
	 * 删除
	 * @param message 
	 * @return
	 */
    public Message deleteMessage(Message message);

    /**
     *  获取 by id
     *  @param id 编号
     * @return
	 */
    public Message getMessageById(Long id);
}
