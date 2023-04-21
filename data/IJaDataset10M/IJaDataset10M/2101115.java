package org.hardtokenmgmt.server.services;

import java.text.ParseException;
import java.util.Date;
import org.apache.log4j.Logger;
import org.ejbca.core.model.services.BaseInterval;
import org.quartz.CronExpression;

/**
 * Interval that support CRON expressions to define execution intervals.
 * <p>
 * Supports the setting 'cronpattern' that should define valid crontab entry such as "* 1 * * *", etc.
 * <p> 
 * 
 * 
 * @author Philip Vendil 27 Jun 2010
 *
 * @version $Id$
 */
public class CronInterval extends BaseInterval {

    private static final Logger log = Logger.getLogger(CronInterval.class);

    public static final String SETTING_CRONPATTERN = "cronpattern";

    /**
	 * 
	 * 
	 * @see org.ejbca.core.model.services.IInterval#getTimeToExecution()
	 */
    @Override
    public long getTimeToExecution() {
        long retval = 0;
        String cronExpression = properties.getProperty(SETTING_CRONPATTERN);
        if (cronExpression != null) {
            try {
                CronExpression ce = new CronExpression(cronExpression);
                Date nextDate = ce.getNextValidTimeAfter(new Date());
                retval = (long) (nextDate.getTime() - System.currentTimeMillis());
                retval = retval / 1000;
            } catch (ParseException e) {
                log.error("Error in Service configuration, illegal CRON expression : " + cronExpression + " defined for service with name " + serviceName);
            }
        } else {
            log.error("Error in Service configuration, no cron pattern specified with the setting '" + SETTING_CRONPATTERN + "'.");
        }
        return retval;
    }
}
