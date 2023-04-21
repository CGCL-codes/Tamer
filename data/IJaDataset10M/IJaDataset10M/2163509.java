package com.entelience.probe.chat;

import java.util.Date;

public class Conversation {

    public String internalName;

    public Date startDate;

    public Date endDate;

    public Integer conversationId;

    public String toString() {
        return internalName + ", " + startDate + ", " + endDate;
    }
}
