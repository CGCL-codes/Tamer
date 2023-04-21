package com.rapidgwt.client.commands;

import com.google.gwt.user.client.ui.*;
import com.rapidgwt.client.*;
import com.rapidgwt.client.components.MessageBox;

public abstract class AbstractCommand {

    protected MessageBox m_messageBox;

    public AbstractCommand(MessageBox pMessageBox) {
        m_messageBox = pMessageBox;
    }

    public MessageBox getMessageBox() {
        return (m_messageBox);
    }

    public abstract boolean execute();
}
