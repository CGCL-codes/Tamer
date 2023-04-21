package org.p2pws.multicast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.p2pws.ServiceDescriptor;
import org.p2pws.loaddistribution.DistributedRequestProducer;
import org.p2pws.loaddistribution.request.P2PRequest;
import org.p2pws.loaddistribution.request.P2PRequestFactory;
import org.p2pws.loaddistribution.request.P2PRequestProducer;
import org.p2pws.loaddistribution.request.P2PServerRequest;
import org.p2pws.platform.P2pPlatform;

/**
 * @author panisson
 *
 */
public class MulticastRequestFactory implements P2PRequestFactory {

    private P2pPlatform platform;

    private String group = "225.6.7.8";

    private int port = 5678;

    private List<MulticastMessageReceiver> receivers = new ArrayList<MulticastMessageReceiver>();

    private List<MulticastMessageSender> senders = new ArrayList<MulticastMessageSender>();

    public MulticastRequestFactory(P2pPlatform platform, String group, int port) {
        this.group = group;
        this.port = port;
        this.platform = platform;
    }

    public P2PRequest createClientP2PRequest(ServiceDescriptor descriptor) {
        P2PRequest request = null;
        try {
            request = new MulticastClientRequest(group, port, descriptor);
        } catch (IOException e) {
            throw new RuntimeException("Error creating Multicast ClientRequest", e);
        }
        return request;
    }

    public P2PRequestProducer createP2RequestProducer(ServiceDescriptor descriptor) {
        MulticastMessageReceiver receiver = new MulticastMessageReceiver(group, port, descriptor);
        receivers.add(receiver);
        MulticastMessageSender messageSender = new MulticastMessageSender(group, port, descriptor);
        senders.add(messageSender);
        P2PRequestProducer server = new DistributedRequestProducer(messageSender, receiver.getMessageBuffer(), receiver.getSystemMessageBuffer(), this, platform.getPeerId(), descriptor);
        return server;
    }

    public P2PServerRequest createP2PServerRequest(ServiceDescriptor descriptor) {
        return new MulticastServerRequest(descriptor);
    }

    public void stop() {
        for (MulticastMessageReceiver receiver : receivers) {
            receiver.end();
        }
        for (MulticastMessageSender sender : senders) {
            sender.stop();
        }
    }
}
