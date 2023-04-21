package runtimeconfig.components;

import flex.messaging.MessageDestination;
import flex.messaging.config.ConfigMap;
import flex.messaging.config.NetworkSettings;
import flex.messaging.config.ServerSettings;
import flex.messaging.config.ThrottleSettings;
import flex.messaging.services.AbstractBootstrapService;
import flex.messaging.services.MessageService;
import flex.messaging.services.messaging.adapters.JMSAdapter;
import flex.messaging.services.messaging.adapters.JMSSettings;

public class RuntimeJMSDestinationNoJNDIDestName extends AbstractBootstrapService {

    public void initialize(String id, ConfigMap properties) {
        MessageService service = (MessageService) getMessageBroker().getService("message-service");
        String dest = "JMSDestNoJNDIDestName_startup";
        createDestination(dest, service);
    }

    private MessageDestination createDestination(String id, MessageService messageService) {
        MessageDestination msgDest;
        msgDest = (MessageDestination) messageService.createDestination(id);
        NetworkSettings ns = new NetworkSettings();
        ns.setSubscriptionTimeoutMinutes(0);
        ThrottleSettings ts = new ThrottleSettings();
        ts.setIncomingClientFrequency(0);
        ts.setInboundPolicy(ThrottleSettings.Policy.ERROR);
        ts.setOutgoingClientFrequency(0);
        ts.setOutboundPolicy(ThrottleSettings.Policy.IGNORE);
        ns.setThrottleSettings(ts);
        msgDest.setNetworkSettings(ns);
        ServerSettings ss = new ServerSettings();
        ss.setDurable(false);
        msgDest.setServerSettings(ss);
        msgDest.addChannel("qa-polling-amf");
        JMSAdapter adapter = new JMSAdapter();
        adapter.setId("jms");
        JMSSettings js = new JMSSettings();
        js.setDestinationType("Topic");
        js.setMessageType("javax.jms.TextMessage");
        js.setConnectionFactory("java:comp/env/jms/flex/TopicConnectionFactory");
        js.setDeliveryMode("NON_PERSISTENT");
        js.setMessagePriority(javax.jms.Message.DEFAULT_PRIORITY);
        js.setAcknowledgeMode("AUTO_ACKNOWLEDGE");
        adapter.setJMSSettings(js);
        adapter.getJMSSettings();
        adapter.setDestination(msgDest);
        return msgDest;
    }

    public void start() {
        MessageService service = (MessageService) getMessageBroker().getService("message-service");
        String id = "JMSDestNoJNDIDestName_runtime";
        MessageDestination destination = createDestination(id, service);
        destination.start();
    }

    public void stop() {
    }
}
