package de.iritgo.openmetix.comm.chat.action;

import de.iritgo.openmetix.comm.chat.chatter.ChatClientManager;
import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import java.io.IOException;

public class ChatCloseAction extends FrameworkAction {

    private int channel;

    public ChatCloseAction() {
    }

    public ChatCloseAction(long userUniqueId, int channel) {
        super(userUniqueId);
        this.channel = channel;
    }

    public String getTypeId() {
        return "action.chatclose";
    }

    public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException {
        channel = stream.readInt();
    }

    public void writeObject(FrameworkOutputStream stream) throws IOException {
        stream.writeInt(channel);
    }

    public void perform() {
        ChatClientManager chatManager = (ChatClientManager) Engine.instance().getManagerRegistry().getManager("chat.client");
        chatManager.leaveChannel(new Integer(channel), userUniqueId, "");
    }
}
