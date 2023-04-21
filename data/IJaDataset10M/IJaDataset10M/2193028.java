package dsb.bar.flowmeter.client.mgmt;

import org.apache.log4j.Logger;
import dsb.bar.flowmeter.client.Connector;
import dsb.bar.flowmeter.server.api.DiesUpdateCallbackManagerService;
import dsb.bar.tks.support.logging.Log4J;

public class StartDiesService {

    public static void main(String[] args) {
        Log4J.configure();
        Logger logger = Logger.getLogger(StartDiesService.class);
        logger.info("Started.");
        logger.info("Connecting to server ...");
        Connector connector = new Connector();
        DiesUpdateCallbackManagerService svc = connector.getDiesUpdateCallbackManagerService();
        logger.info("Starting Dies liter measurement service ...");
        svc.start();
        logger.info("Finished.");
    }
}
