package com.privilege.manager;

import com.privilege.entity.Message;

public interface MessagePersistanceManager {

    public void storeMessage(String message);

    public Message loadMessage(long id);
}
