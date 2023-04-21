package com.leemba.monitor.server.dao.reports;

import com.leemba.monitor.server.dao.*;
import com.leemba.monitor.server.objects.reports.MetricMeta;
import com.leemba.monitor.server.objects.reports.Plot;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author mrjohnson
 */
public class DurationMetaReport {

    private static final transient Logger log = Logger.getLogger(DurationMetaReport.class);

    public Plot getReport(Plot plot, Date start, Date end) throws ReportException {
        Query query = null;
        try {
            query = new Query().prepScript("reports/duration/duration.meta.sql", new Query.SqlName("serviceId", String.valueOf(plot.getServiceId()))).set(start).set(end);
            List<MetricMeta> items = query.list(MetricMeta.class);
            for (final MetricMeta item : items) plot.addMeta(item);
            return plot;
        } catch (Throwable t) {
            throw new ReportException(t);
        } finally {
            if (query != null) query.close();
        }
    }
}
