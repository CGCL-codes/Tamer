package it.unibo.deis.collaudo.p2p.application;

import it.unibo.deis.interaction.p2p.P2pDiscoveryService;
import java.net.InetSocketAddress;
import org.apache.log4j.Logger;

public class AskTesting {

    private static InetSocketAddress isa = null;

    protected static Logger logger = Logger.getLogger(AskTesting.class);

    public static void main(String[] args) {
        logger.info("Pre Discovering");
        isa = P2pDiscoveryService.getInstance().askForService("TestService");
        logger.info("Service discovered form > " + isa.toString());
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
