package project.fantalk.command;

import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class BroadcastHandler extends BaseCommand {

    public BroadcastHandler() {
        super("broadcast", "announce");
    }

    @Override
    public void doCommand(Message message, String content) {
        if (XMPPUtils.isAdmin(message.email)) {
            XMPPUtils.broadcast("[系统消息] " + content);
        }
    }

    public String documentation() {
        return null;
    }
}
