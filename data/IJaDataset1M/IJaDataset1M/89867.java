package net.sf.peervibes.protocols.tman;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.protocols.common.RegisterSocketEvent;
import net.sf.peervibes.protocols.membership.events.GetPeersEvent;
import net.sf.peervibes.protocols.p2p.events.P2PInitEvent;
import net.sf.peervibes.protocols.tman.events.SwitchRankFunctionEvent;
import net.sf.peervibes.protocols.tman.events.UpdateViewEvent;
import net.sf.peervibes.protocols.tman.events.ViewReturnEvent;
import net.sf.peervibes.protocols.tman.timers.UpdateViewTimer;

public class TManLayer extends Layer {

    public TManLayer() {
        evRequire = new Class[] { RegisterSocketEvent.class };
        evAccept = new Class[] { ChannelInit.class, ChannelClose.class, UpdateViewEvent.class, ViewReturnEvent.class, GetPeersEvent.class, SwitchRankFunctionEvent.class, P2PInitEvent.class, UpdateViewTimer.class };
        evProvide = new Class[] { UpdateViewEvent.class, ViewReturnEvent.class, GetPeersEvent.class, UpdateViewTimer.class };
    }

    @Override
    public Session createSession() {
        return new TManSession(this);
    }
}
