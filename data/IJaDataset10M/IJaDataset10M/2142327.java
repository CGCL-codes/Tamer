package jelb.messaging;

import jelb.common.ActorList;
import jelb.common.Mob;
import jelb.netio.Message;
import jelb.netio.Protocol;

/**
 * Actor HP decreases 
 */
public class GetActorDamage implements IMessage {

    private int actorId;

    private int value;

    public byte getType() {
        return Protocol.GET_ACTOR_DAMAGE;
    }

    public void init(Message rawMessage) {
        this.actorId = rawMessage.getUint16(0).toInt();
        this.value = rawMessage.getUint16(2).toInt();
    }

    public int getValue() {
        return this.value;
    }

    public Mob getMob(ActorList actors) {
        return actors.getMob(this.actorId);
    }
}
