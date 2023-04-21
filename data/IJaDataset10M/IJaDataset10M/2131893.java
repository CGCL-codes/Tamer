package org.slasoi.entit.messaging;

import java.util.UUID;
import org.slasoi.common.messaging.MessagingException;
import org.slasoi.common.messaging.Setting;
import org.slasoi.common.messaging.Settings;
import org.slasoi.common.messaging.pubsub.PubSubFactory;
import org.slasoi.common.messaging.pubsub.PubSubManager;

public class MessagingSingleton {

    private static final MessagingSingleton INSTANCE = new MessagingSingleton();

    private MessagingSingleton() {
    }

    public static MessagingSingleton getInstance() {
        return INSTANCE;
    }

    private static final String pubsub = "xmpp";

    private static final String pubsubResource = "b4-" + UUID.randomUUID().toString();

    private PubSubManager pubSubManager;

    public void connect(String username, String password, String host, String port, String service, String pubSubService) throws MessagingException {
        Settings settings = new Settings();
        settings.setSetting(Setting.pubsub, pubsub);
        settings.setSetting(Setting.xmpp_host, host);
        settings.setSetting(Setting.xmpp_port, port);
        settings.setSetting(Setting.xmpp_username, username);
        settings.setSetting(Setting.xmpp_password, password);
        settings.setSetting(Setting.xmpp_service, service);
        settings.setSetting(Setting.xmpp_pubsubservice, pubSubService);
        settings.setSetting(Setting.xmpp_resource, pubsubResource);
        pubSubManager = PubSubFactory.createPubSubManager(settings);
    }

    public PubSubManager getPubSubManager() {
        return pubSubManager;
    }
}
