package org.actioncenters.cometd.cache.user;

import org.actioncenters.core.contribution.data.IUser;
import org.actioncenters.core.spring.ApplicationContextHelper;
import org.actioncenters.core.usersecurity.IUserManagementService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

/**
 * @author dkjeldgaard
 *
 */
public class UserCacheEntryFactory implements CacheEntryFactory {

    /** Logger. */
    private static Logger log = Logger.getLogger(UserCacheEntryFactory.class.getName());

    /** The application context. */
    private static ApplicationContext ac = ApplicationContextHelper.getApplicationContext("actioncenters.xml");

    /** The user management service. */
    private static IUserManagementService userMgmtSvc = (IUserManagementService) ac.getBean("userManagementService");

    /**
     * {@inheritDoc}
     */
    @Override
    public Object createEntry(Object key) throws Exception {
        IUser returnValue = null;
        if (key instanceof String) {
            String userName = (String) key;
            returnValue = userMgmtSvc.getUserByUsername(userName);
            if (log.isDebugEnabled()) {
                log.debug("New Entry Created for user: " + key);
            }
        }
        return returnValue;
    }
}
