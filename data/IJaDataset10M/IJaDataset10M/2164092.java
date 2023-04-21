package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.StatisticsReportDao;
import org.opennms.netmgt.model.StatisticsReport;

/**
 * DAO implementation for accessing StatisticsReport objects.
 * 
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @see StatisticsReport
 */
public class StatisticsReportDaoHibernate extends AbstractDaoHibernate<StatisticsReport, Integer> implements StatisticsReportDao {

    public StatisticsReportDaoHibernate() {
        super(StatisticsReport.class);
    }
}
