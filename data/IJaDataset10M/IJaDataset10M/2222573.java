package runtimeconfig.components;

import flex.messaging.MessageDestination;
import flex.messaging.config.ConfigMap;
import flex.messaging.config.NetworkSettings;
import flex.messaging.config.ServerSettings;
import flex.messaging.services.AbstractBootstrapService;
import flex.messaging.services.MessageService;

public class RuntimeRTPushOverHttpDestinations extends AbstractBootstrapService {

    public void initialize(String id, ConfigMap properties) {
        MessageService service = (MessageService) broker.getService("message-service");
        String dest = "HTTPLongPollDestination";
        String channel = "data-http-long-poll";
        createDestination(dest, channel, service);
        dest = "HTTPWaitingPollRequestDestination";
        channel = "data-http-waiting-poll-requests";
        createDestination(dest, channel, service);
        dest = "HTTPPollSecureDestination";
        channel = "data-secure-http-polling";
        createDestination(dest, channel, service);
    }

    private MessageDestination createDestination(String id, String channel, MessageService messageService) {
        MessageDestination msgDest;
        msgDest = (MessageDestination) messageService.createDestination(id);
        NetworkSettings ns = new NetworkSettings();
        ns.setSubscriptionTimeoutMinutes(0);
        msgDest.setNetworkSettings(ns);
        ServerSettings ss = new ServerSettings();
        ss.setMessageTTL(0);
        ss.setDurable(false);
        msgDest.setServerSettings(ss);
        msgDest.addChannel(channel);
        return msgDest;
    }

    public void start() {
    }

    public void stop() {
    }
}
