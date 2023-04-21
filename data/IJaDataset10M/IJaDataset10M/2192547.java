package jelb.messaging;

import jelb.netio.Message;

public class RemoveActor implements IMessage {

    private int actorId;

    public byte getType() {
        return jelb.netio.Protocol.REMOVE_ACTOR;
    }

    public void init(Message rawMessage) {
        this.actorId = rawMessage.getUint16(0).toInt();
    }

    public int getActorId() {
        return this.actorId;
    }
}
