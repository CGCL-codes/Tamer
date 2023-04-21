package com.mycila.jms.tool;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class LightBroker {

    private final BrokerService broker;

    /**
     * Create a light JMS broker
     *
     * @param brokerName   The broker name
     * @param discoveryURI The rendez-vous address to use, in example: rendezvous://lightbrokers
     */
    public LightBroker(String brokerName, String discoveryURI) {
        try {
            final TransportConnector transportConnector = new TransportConnector();
            transportConnector.setUri(new URI("tcp://0.0.0.0:0"));
            transportConnector.setDiscoveryUri(new URI(discoveryURI));
            broker = new BrokerService();
            broker.setPersistent(false);
            broker.setUseJmx(false);
            broker.getManagementContext().setFindTigerMbeanServer(true);
            broker.setEnableStatistics(false);
            broker.setSupportFailOver(true);
            broker.setUseShutdownHook(true);
            broker.setBrokerName(brokerName);
            broker.addConnector(transportConnector);
            broker.addNetworkConnector(discoveryURI);
            broker.setPopulateJMSXUserID(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public BrokerService getBroker() {
        return broker;
    }

    public void start() {
        if (!broker.isStarted()) {
            final CountDownLatch started = new CountDownLatch(1);
            new Thread("broker-thread") {

                @Override
                public void run() {
                    try {
                        broker.start();
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    while (!broker.isStarted()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ignored) {
                            break;
                        }
                    }
                    started.countDown();
                }
            }.start();
            try {
                started.await();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void stop() {
        if (broker.isStarted()) {
            try {
                broker.stop();
            } catch (Exception ignored) {
            }
        }
    }
}
