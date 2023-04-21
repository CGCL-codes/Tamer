package org.mobicents.tools.sip.balancer;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public interface BalancerRunnerMBean {

    void start(String configurationFile);

    void stop();

    /**
	 * Sets interval between runs of task that removes nodes that expired.
	 * @param value
	 */
    void setNodeExpirationTaskInterval(long value);

    long getNodeExpirationTaskInterval();

    /**
	 * Sets value which indicates when node has expired. if node.timeStamp+nodeExpiration<System.currentTimeMilis than node has expired and on next
	 * run of nodeExpirationTask will be removed.
	 * @param value
	 */
    void setNodeExpiration(long value);

    long getNodeExpiration();

    long getNumberOfRequestsProcessed();

    long getNumberOfResponsesProcessed();

    Map<String, AtomicLong> getNumberOfRequestsProcessedByMethod();

    Map<String, AtomicLong> getNumberOfResponsesProcessedByStatusCode();

    long getRequestsProcessedByMethod(String method);

    long getResponsesProcessedByStatusCode(String statusCode);

    List<SIPNode> getNodes();

    String[] getNodeList();

    String getProperty(String key);

    void setProperty(String key, String value);

    Properties getProperties();
}
