package org.myjerry.evenstar.core;

import java.util.Date;
import javax.jdo.annotations.Persistent;

public class UserStamped {

    @Persistent
    private Date lastUpdated;

    @Persistent
    private long lastUpdateUser;

    /**
	 * @return the lastUpdated
	 */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
	 * @param lastUpdated the lastUpdated to set
	 */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
	 * @return the lastUpdateUser
	 */
    public long getLastUpdateUser() {
        return lastUpdateUser;
    }

    /**
	 * @param lastUpdateUser the lastUpdateUser to set
	 */
    public void setLastUpdateUser(long lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
}
