package org.apache.roller.business.jdo;

import java.util.Collection;
import java.util.List;
import org.apache.roller.RollerException;
import org.apache.roller.business.AutoPingManagerImpl;
import org.apache.roller.business.PersistenceStrategy;
import org.apache.roller.pojos.AutoPingData;
import org.apache.roller.pojos.PingTargetData;
import org.apache.roller.pojos.WeblogEntryData;
import org.apache.roller.pojos.WebsiteData;

/**
 * @author Dave Johnson
 */
public class JDOAutoPingManagerImpl extends AutoPingManagerImpl {

    public JDOAutoPingManagerImpl(PersistenceStrategy persistenceStrategy) {
        super(persistenceStrategy);
    }

    public void removeAutoPing(PingTargetData pingTarget, WebsiteData website) throws RollerException {
    }

    public void removeAllAutoPings() throws RollerException {
    }

    public List getAutoPingsByWebsite(WebsiteData website) throws RollerException {
        return null;
    }

    public List getAutoPingsByTarget(PingTargetData pingTarget) throws RollerException {
        return null;
    }

    public List getCategoryRestrictions(AutoPingData autoPing) throws RollerException {
        return null;
    }

    public void setCategoryRestrictions(AutoPingData autoPing, Collection newCategories) {
    }

    public List getApplicableAutoPings(WeblogEntryData changedWeblogEntry) throws RollerException {
        return null;
    }
}
