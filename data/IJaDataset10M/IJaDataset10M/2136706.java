package com.arsenal.rtcomm.message;

import com.arsenal.message.*;
import com.arsenal.client.Client;

public class AssignCommKeyMessage extends BaseMessage {

    public void execute() {
        Client.getInstance().getCommClient().assignKey((String) getPayload());
    }
}
