package magoffin.matt.ieat.task;

import magoffin.matt.ieat.dao.UserDao;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Task to remove users that have registered but not confirmed 
 * that registration after a period of time.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 26 $ $Date: 2009-05-03 19:35:54 -0400 (Sun, 03 May 2009) $
 */
public class RemoveStaleUserRegistrations extends QuartzJobBean {

    private static final Logger LOG = Logger.getLogger(RemoveStaleUserRegistrations.class);

    /** The UserDao object. */
    private UserDao userDao = null;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        verifyConfiguration();
        if (LOG.isInfoEnabled()) {
            LOG.info("Removing stale user registrations");
        }
        int numDeleted = userDao.removeStaleUserRegistrations();
        if (LOG.isInfoEnabled()) {
            if (numDeleted > 0) {
                LOG.info("Removed " + numDeleted + " users whose registration was stale.");
            } else {
                LOG.info("No stale user registrations were found.");
            }
        }
    }

    /**
	 * Verify the task configuration is valid.
	 * @throws RuntimeException if not configured properly
	 */
    public void verifyConfiguration() {
        if (userDao == null) {
            throw new RuntimeException("userDao not configured");
        }
    }

    /**
	 * @return the userDao
	 */
    public UserDao getUserDao() {
        return userDao;
    }

    /**
	 * @param userDao the userDao to set
	 */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
