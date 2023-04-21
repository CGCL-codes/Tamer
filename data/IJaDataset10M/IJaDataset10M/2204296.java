package org.grailrtls.tutorials.world_model;

import org.grailrtls.libworldmodel.client.ClientWorldModelInterface;
import org.grailrtls.libworldmodel.client.listeners.DataListener;
import org.grailrtls.libworldmodel.client.protocol.messages.AbstractRequestMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.AttributeAliasMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.DataResponseMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.OriginAliasMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.OriginPreferenceMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.StreamRequestMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.URISearchResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleStreamingClient implements DataListener {

    private static final Logger log = LoggerFactory.getLogger(SimpleStreamingClient.class);

    private final ClientWorldModelInterface worldModel = new ClientWorldModelInterface();

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsageInfo();
            return;
        }
        int port = 7013;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            log.error("Unable to format {} into a valid port number. Defaulting to 7013.", args[1]);
        }
        SimpleStreamingClient client = new SimpleStreamingClient(args[0], port, args.length > 2 ? args[2] : null);
        client.connect();
    }

    public SimpleStreamingClient(String worldModelHost, int worldModelPort, String searchUriRegex) {
        this.worldModel.setHost(worldModelHost);
        this.worldModel.setPort(worldModelPort);
        if (searchUriRegex != null) {
            this.worldModel.registerSearchRequest(searchUriRegex);
        }
        this.worldModel.setStayConnected(true);
        this.worldModel.setDisconnectOnException(true);
        this.worldModel.addDataListener(this);
    }

    public void connect() {
        this.worldModel.doConnectionSetup();
    }

    public static void printUsageInfo() {
        StringBuffer sb = new StringBuffer("Usage: <World Model IP> <World Model Port> [<Search URI RegEx>]\n");
        System.err.println(sb.toString());
    }

    @Override
    public void requestCompleted(ClientWorldModelInterface worldModel, AbstractRequestMessage message, int ticketNumber) {
        log.info("Request {} completed ticket {} from " + worldModel.toString(), message, Integer.valueOf(ticketNumber));
    }

    @Override
    public void dataResponseReceived(ClientWorldModelInterface worldModel, DataResponseMessage message) {
        log.info("{} sent {}", worldModel, message);
    }

    @Override
    public void uriSearchResponseReceived(ClientWorldModelInterface worldModel, URISearchResponseMessage message) {
        log.info("{} sent {}", worldModel, message);
        long now = System.currentTimeMillis();
        if (message.getMatchingUris() != null) {
            for (String uri : message.getMatchingUris()) {
                StreamRequestMessage request = new StreamRequestMessage();
                request.setBeginTimestamp(now);
                request.setUpdateInterval(0l);
                request.setQueryURI(uri);
                request.setQueryAttributes(new String[] { ".*" });
                worldModel.sendMessage(request);
            }
        }
    }

    @Override
    public void attributeAliasesReceived(ClientWorldModelInterface clientWorldModelInterface, AttributeAliasMessage message) {
        log.info("Received {} from {}", message, clientWorldModelInterface);
    }

    @Override
    public void originAliasesReceived(ClientWorldModelInterface clientWorldModelInterface, OriginAliasMessage message) {
        log.info("Received {} from {}.", message, clientWorldModelInterface);
    }

    @Override
    public void originPreferenceSent(ClientWorldModelInterface worldModel, OriginPreferenceMessage message) {
    }
}
