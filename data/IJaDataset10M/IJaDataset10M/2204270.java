package org.jul.warp.messaging;

public interface AgentListener {

    public void process(AgentAnnouncement message);

    public void process(DiscoveryRequest message);
}
