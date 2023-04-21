package com.macro10.switchboard;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<ResponseMessage> messages = new ArrayList<ResponseMessage>();

    public List<ResponseMessage> getMessages() {
        return messages;
    }

    public void addMessage(ResponseMessage message) {
        messages.add(message);
    }

    public void clearMessages() {
        messages.clear();
    }
}
