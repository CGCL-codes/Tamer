package net.sf.peervibes.protocols.p2p.events;

import java.util.UUID;
import net.sf.peervibes.utils.Peer;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.message.Message;

/**
 * Event used to broadcast messages in a P2P system.
 * <br>
 * In order to use this message the P2PBottomLayer should be in the stack. 
 * <br>
 * It uses a UUID filed to carry a (big) random identifier, that should be
 * used to avoid more that one delivery to the application layer.
 * <br>
 * OriginalSender is the node that generated the broadcast message, whereas
 * sender is the node that retransmitted the message for the last time (e.g. the last
 * hop of the message). Moreover, when the event is created for the first time it
 * makes sense that the originalSender and sender are set to the same Peer
 * identifier.
 * 
 * @version 0.1
 * @author Joao Leitao
 */
public class RouteSendableEvent extends SendableEvent implements Cloneable {

    private UUID msgID;

    private Peer originalSender;

    private Peer sender;

    private UUID destiny;

    public RouteSendableEvent() {
        super(new Message());
        this.msgID = UUID.randomUUID();
        this.originalSender = null;
        this.sender = null;
        this.destiny = null;
    }

    public RouteSendableEvent(Message msg) {
        super(msg);
        this.msgID = UUID.randomUUID();
        this.originalSender = null;
        this.sender = null;
        this.destiny = null;
    }

    public RouteSendableEvent(Channel channel, int dir, Session source) throws AppiaEventException {
        super(channel, dir, source, new Message());
        this.msgID = UUID.randomUUID();
        this.originalSender = null;
        this.sender = null;
        this.destiny = null;
    }

    public RouteSendableEvent(Channel channel, int dir, Session source, Message msg) throws AppiaEventException {
        super(channel, dir, source, msg);
        this.msgID = UUID.randomUUID();
        this.originalSender = null;
        this.sender = null;
        this.destiny = null;
    }

    public RouteSendableEvent(Channel channel, int dir, Session source, Peer creator) throws AppiaEventException {
        super(channel, dir, source, new Message());
        this.msgID = UUID.randomUUID();
        this.originalSender = creator;
        this.sender = creator;
        this.destiny = null;
    }

    public RouteSendableEvent(Channel channel, int dir, Session source, Message msg, Peer creator) throws AppiaEventException {
        super(channel, dir, source, msg);
        this.msgID = UUID.randomUUID();
        this.originalSender = creator;
        this.sender = creator;
        this.destiny = null;
    }

    public RouteSendableEvent(Channel channel, int dir, Session source, Peer creator, UUID destiny) throws AppiaEventException {
        super(channel, dir, source, new Message());
        this.msgID = UUID.randomUUID();
        this.originalSender = creator;
        this.sender = creator;
        this.destiny = destiny;
    }

    public RouteSendableEvent(Channel channel, int dir, Session source, Message msg, Peer creator, UUID destiny) throws AppiaEventException {
        super(channel, dir, source, msg);
        this.msgID = UUID.randomUUID();
        this.originalSender = creator;
        this.sender = creator;
        this.destiny = destiny;
    }

    public Object clone() throws CloneNotSupportedException {
        RouteSendableEvent bse = (RouteSendableEvent) super.clone();
        bse.msgID = this.msgID;
        bse.originalSender = this.originalSender;
        bse.sender = this.originalSender;
        return bse;
    }

    public void setSender(Peer sender) {
        this.sender = sender;
    }

    public void setOriginalSender(Peer sender) {
        this.originalSender = sender;
    }

    public void setMsgID(UUID id) {
        this.msgID = id;
    }

    public Peer getSender() {
        return this.sender;
    }

    public Peer getOriginalSender() {
        return this.originalSender;
    }

    public UUID getMsgID() {
        return this.msgID;
    }

    public void setDestiny(UUID destiny) {
        this.destiny = destiny;
    }

    public UUID getDestiny() {
        return this.destiny;
    }
}
