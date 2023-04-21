package com.zuaari.server;

import com.zuaari.messages.Message;

public interface IConnection {

    void sendMessage(Message msg);
}
