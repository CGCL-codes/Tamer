package org.archive.crawler.frontier;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlHost;
import org.archive.crawler.framework.CrawlController;

/**
 * Uses the target IPs as basis for queue-assignment,
 * distributing them over a fixed number of sub-queues.
 * 
 * @author Christian Kohlschuetter
 */
public class BucketQueueAssignmentPolicy extends QueueAssignmentPolicy {

    private static final int DEFAULT_NOIP_BITMASK = 1023;

    private static final int DEFAULT_QUEUES_HOSTS_MODULO = 1021;

    public String getClassKey(final CrawlController controller, final CandidateURI curi) {
        CrawlHost host;
        try {
            host = controller.getServerCache().getHostFor(curi.getUURI().getReferencedHost());
        } catch (URIException e) {
            e.printStackTrace();
            host = null;
        }
        if (host == null) {
            return "NO-HOST";
        } else if (host.getIP() == null) {
            return "NO-IP-".concat(Integer.toString(Math.abs(host.getHostName().hashCode()) & DEFAULT_NOIP_BITMASK));
        } else {
            return Integer.toString(Math.abs(host.getIP().hashCode()) % DEFAULT_QUEUES_HOSTS_MODULO);
        }
    }

    public int maximumNumberOfKeys() {
        return DEFAULT_NOIP_BITMASK + DEFAULT_QUEUES_HOSTS_MODULO + 2;
    }
}
