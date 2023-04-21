package org.archive.crawler.frontier;

import org.archive.crawler.datamodel.CrawlURI;

/**
 * CostAssignmentPolicy considering all URIs costless -- essentially
 * disabling budgetting features.
 * 
 * @author gojomo
 */
public class ZeroCostAssignmentPolicy extends CostAssignmentPolicy {

    public int costOf(CrawlURI curi) {
        return 0;
    }
}
