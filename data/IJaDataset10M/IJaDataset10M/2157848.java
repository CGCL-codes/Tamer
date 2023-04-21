package org.archive.crawler.frontier;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.framework.CrawlController;
import org.archive.net.PublicSuffixes;

/**
 * Create a queueKey based on the SURT authority, reduced to the 
 * public-suffix-plus-one domain (topmost assignable domain). 
 * 
 * @author gojomo
 */
public class TopmostAssignedSurtQueueAssignmentPolicy extends SurtAuthorityQueueAssignmentPolicy {

    @Override
    public String getClassKey(CrawlController controller, CandidateURI cauri) {
        String candidate = super.getClassKey(controller, cauri);
        candidate = PublicSuffixes.reduceSurtToTopmostAssigned(candidate);
        return candidate;
    }
}
