package jelb.messaging;

import jelb.netio.Message;
import jelb.netio.Protocol;

public class KillAllActors implements IMessage {

    @Override
    public byte getType() {
        return Protocol.KILL_ALL_ACTORS;
    }

    @Override
    public void init(Message rawMessage) {
    }
}
