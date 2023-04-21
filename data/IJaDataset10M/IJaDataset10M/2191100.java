package net.sf.peervibes.protocols.floodbroadcast;

import net.sf.peervibes.protocols.p2p.events.BroadcastSendableEvent;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.protocols.common.RegisterSocketEvent;
import net.sf.peervibes.protocols.membership.events.GetPeersEvent;
import net.sf.peervibes.protocols.p2p.events.P2PInitEvent;
import net.sf.appia.core.events.channel.PeriodicTimer;

public class FloodBroadcastLayer extends Layer {

    public FloodBroadcastLayer() {
        evRequire = new Class[] { RegisterSocketEvent.class };
        evAccept = new Class[] { BroadcastSendableEvent.class, PeriodicTimer.class, GetPeersEvent.class, ChannelInit.class, ChannelClose.class, P2PInitEvent.class };
        evProvide = new Class[] { PeriodicTimer.class, GetPeersEvent.class, BroadcastSendableEvent.class };
    }

    @Override
    public Session createSession() {
        return new FloodBroadcastSession(this);
    }
}
