package com.zuaari.chat;

import com.zuaari.messages.ChatRequest;
import com.zuaari.messages.ChatResponse;

public interface IChatService {

    ChatResponse doChat(ChatRequest chatReq);
}
