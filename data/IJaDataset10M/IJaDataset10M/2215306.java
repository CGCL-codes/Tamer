package org.archive.crawler.event;

import org.archive.crawler.reporting.CrawlStatSnapshot;
import org.archive.crawler.reporting.StatisticsTracker;
import org.springframework.context.ApplicationEvent;

/**
 * ApplicationEvent published when the StatisticsTracker takes its
 * sample of various statistics. Other modules can observe this event
 * to perform periodic checks related to overall statistics. 
 * 
 * @contributor gojomo
 */
public class StatSnapshotEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    CrawlStatSnapshot snapshot;

    public StatSnapshotEvent(StatisticsTracker stats, CrawlStatSnapshot snapshot) {
        super(stats);
        this.snapshot = snapshot;
    }

    public CrawlStatSnapshot getSnapshot() {
        return snapshot;
    }
}
