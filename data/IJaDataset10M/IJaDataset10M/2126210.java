package de.iritgo.openmetix.comm.chat.action;

import de.iritgo.openmetix.comm.chat.chatter.ChatClientManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import java.io.IOException;

public class UserJoinAction extends FrameworkAction {

    private String userName;

    private String channel;

    public UserJoinAction() {
    }

    public UserJoinAction(String userName, long userUniqueId, String channel) {
        super(userUniqueId);
        this.userName = userName;
        this.channel = channel;
    }

    public String getTypeId() {
        return "action.userjoin";
    }

    public String getUserName() {
        return userName;
    }

    public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException {
        userName = stream.readUTF();
        channel = stream.readUTF();
    }

    public void writeObject(FrameworkOutputStream stream) throws IOException {
        stream.writeUTF(userName);
        stream.writeUTF(channel);
    }

    public void perform() {
        ChatClientManager chatManager = (ChatClientManager) Engine.instance().getManagerRegistry().getManager("chat.client");
        chatManager.joinChannel(channel, userUniqueId, userName);
    }
}
