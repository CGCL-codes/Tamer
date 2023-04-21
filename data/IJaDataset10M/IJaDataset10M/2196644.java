package org.piax.trans;

import java.util.List;
import org.piax.trans.common.PeerId;
import org.piax.trans.common.PeerLocator;
import org.piax.trans.common.ServiceInfo;
import org.piax.trans.stat.TrafficInfo;

public interface PeerManager {

    public List<Peer> listPeers();

    public List<Peer> listSortedPeers();

    public void addPeer(Peer peer);

    public Peer getPeerByLocator(PeerLocator locator);

    public Peer getPeerCreateByLocator(PeerLocator locator);

    public Peer getPeer(PeerId peerId);

    public Peer getPeerCreate(PeerId id);

    public Peer getPeerCreate(ServiceInfo info);

    public Peer setIdAndLocatorMapping(PeerId id, PeerLocator locator);

    public void setLocatorStatus(PeerLocator locator, int status);

    public void putSend(int msgSize, PeerLocator to);

    public void putReceive(int msgSize, PeerLocator from);

    public TrafficInfo getTraffic();
}
