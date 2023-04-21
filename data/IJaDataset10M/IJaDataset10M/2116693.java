package org.apache.roller.business.referrers;

import org.apache.roller.model.RollerFactory;

/**
 * Same as the ReferrerProcessingJob, except that we add a little logic that
 * tries to lookup incoming referrers from the ReferrerQueueManager.
 *
 * @author Allen Gilliland
 */
public class QueuedReferrerProcessingJob extends ReferrerProcessingJob {

    public QueuedReferrerProcessingJob() {
        super();
    }

    public void execute() {
        ReferrerQueueManager refQueue = RollerFactory.getRoller().getReferrerQueueManager();
        referrer = refQueue.dequeue();
        while (referrer != null) {
            super.execute();
            referrer = refQueue.dequeue();
        }
    }
}
