package uk.co.badgersinfoil.flight.recorder;

import uk.co.badgersinfoil.flight.HttpClientRequest;
import uk.co.badgersinfoil.flight.Host;
import uk.co.badgersinfoil.flight.HeaderHandler;

public class RequestHostHandler implements HeaderHandler {

    private AgentRecorder logger;

    public RequestHostHandler(AgentRecorder l) {
        logger = l;
    }

    public void handle(HttpClientRequest request, String header, String value) {
        synchronized (logger) {
            Host host = logger.getCurrentHost();
            if (host == null || !host.getName().equalsIgnoreCase(value)) {
                logger.record("agent.setHost('" + value + "');");
                logger.setCurrentHostName(value);
            }
        }
    }
}
