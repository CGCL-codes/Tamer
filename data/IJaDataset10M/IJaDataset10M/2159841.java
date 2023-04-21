package org.homemotion.events.impl;

import java.io.File;
import java.io.IOException;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.log4j.Logger;
import org.homemotion.events.AbstractEvent;

@Singleton
public final class ActiveMQForwarder {

    private Destination destination;

    private BrokerService broker;

    private int port = 61616;

    private static Logger LOGGER = Logger.getLogger(ActiveMQForwarder.class);

    public void init(String target) {
        LOGGER.info("Creating destination topic: " + target + "...");
        this.destination = createTopicDestination(target);
    }

    public boolean onEvent(@Observes AbstractEvent event) {
        return true;
    }

    private Destination createTopicDestination(String target) {
        try {
            Destination destination = broker.getDestination(ActiveMQDestination.createDestination(target, ActiveMQDestination.TOPIC_TYPE));
            destination.start();
            return destination;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private BrokerService createBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setBrokerName("Homemotion");
        broker.setEnableStatistics(true);
        broker.setUseJmx(true);
        KahaDBPersistenceAdapter persistenceAdapter = new KahaDBPersistenceAdapter();
        persistenceAdapter.setJournalMaxFileLength(32 * 1024 * 1024);
        persistenceAdapter.setDirectory(new File("messageStore"));
        broker.setPersistenceAdapter(persistenceAdapter);
        broker.addConnector("tcp://localhost:" + port);
        SimpleAuthenticationPlugin authPlugin = new SimpleAuthenticationPlugin();
        authPlugin.setAnonymousAccessAllowed(true);
        broker.setPlugins(new BrokerPlugin[] { authPlugin });
        return broker;
    }

    public void start() {
        if (broker == null) {
            LOGGER.debug("Creating/starting ActiveMQ Broker...");
            try {
                broker = createBroker();
                LOGGER.debug("ActiveMQ Broker created.");
                broker.start();
                LOGGER.debug("Waiting for ActiveMQ Broker to start...");
                broker.waitUntilStarted();
                LOGGER.info("ActiveMQ Broker started.");
            } catch (Exception e) {
                LOGGER.fatal("Error starting ActiveMQ Broker:", e);
            }
        }
    }

    @PreDestroy
    public void stop() {
        LOGGER.debug("Stopping ActiveMQ Broker...");
        if (broker != null) {
            try {
                broker.stop();
                LOGGER.debug("Waiting for ActiveMQ Broker to stop...");
                broker.waitUntilStopped();
                LOGGER.info("ActiveMQ Broker stopped.");
            } catch (Exception e) {
                LOGGER.error("Error stopping ActiveMQ Broker:", e);
            }
        }
    }

    @Produces
    public BrokerService getBroker() {
        return this.broker;
    }

    public String[] getTargets() throws Exception {
        ActiveMQDestination[] destinations = this.broker.getDestinations();
        String[] result = new String[destinations.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = destinations[i].getQualifiedName();
        }
        return result;
    }

    public void deleteAllMessages() throws IOException {
        this.broker.deleteAllMessages();
    }
}
