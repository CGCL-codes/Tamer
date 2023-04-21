package org.signserver.server.statistics.nonpersistent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.signserver.common.NonPersistentStatisticsConstants;
import org.signserver.common.StatisticsConstants;
import org.signserver.server.statistics.StatisticsEntry;

/**
 * Class in charge on maintaining statistics for all events
 * that have happened the last second.
 *
 * @author Philip Vendil 28 apr 2008
 * @version $Id: SecondStatisticsCollector.java 1825 2011-08-10 10:21:18Z netmackan $
 */
public class SecondStatisticsCollector extends BaseFIFOStatisticsCollector {

    private transient Logger log = Logger.getLogger(this.getClass());

    @Override
    protected Date genCurrentStartPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    protected Date genCurrentEndPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public List<StatisticsEntry> fetchStatistics(String type, Date startTime, Date endTime) {
        List<StatisticsEntry> retval;
        if (type.equals(StatisticsConstants.QUERYTYPE_ALL) || type.equals(NonPersistentStatisticsConstants.QUERYTYPE_SECOND)) {
            retval = fetchStatistics(startTime, endTime);
        } else {
            retval = new ArrayList<StatisticsEntry>();
        }
        return retval;
    }

    @Override
    public long getExpireTime() {
        return getExpireTime(NonPersistentStatisticsConstants.SECONDSTATISTICS_EXPIRETIME, NonPersistentStatisticsConstants.DEFAULT_SECONDSTATISTICS_EXPIRETIME, log);
    }
}
