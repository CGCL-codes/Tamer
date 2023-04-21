package org.apache.roller.business.jdo;

import java.util.List;
import org.apache.roller.RollerException;
import org.apache.roller.business.PersistenceStrategy;
import org.apache.roller.business.PingQueueManagerImpl;
import org.apache.roller.pojos.AutoPingData;
import org.apache.roller.pojos.PingTargetData;
import org.apache.roller.pojos.WebsiteData;

/**
 * @author Dave Johnson
 */
public class JDOPingQueueManagerImpl extends PingQueueManagerImpl {

    public JDOPingQueueManagerImpl(PersistenceStrategy persistenceStrategy) {
        super(persistenceStrategy);
    }

    public void addQueueEntry(AutoPingData autoPing) throws RollerException {
    }

    public void dropQueue() throws RollerException {
    }

    public List getAllQueueEntries() throws RollerException {
        return null;
    }

    public void removeQueueEntriesByPingTarget(PingTargetData pingTarget) throws RollerException {
    }

    public void removeQueueEntriesByWebsite(WebsiteData website) throws RollerException {
    }
}
