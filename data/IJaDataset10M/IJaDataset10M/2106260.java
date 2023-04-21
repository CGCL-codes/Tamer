package org.freem.love.client.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FolderBean implements IsSerializable {

    private String name;

    private int countMembers;

    private int countNewMessages;

    private int countAllMessages;

    public int getCountAllMessages() {
        return countAllMessages;
    }

    public void setCountAllMessages(int countAllMessages) {
        this.countAllMessages = countAllMessages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountMembers() {
        return countMembers;
    }

    public void setCountMembers(int countMembers) {
        this.countMembers = countMembers;
    }

    public int getCountNewMessages() {
        return countNewMessages;
    }

    public void setCountNewMessages(int countNewMessages) {
        this.countNewMessages = countNewMessages;
    }
}
