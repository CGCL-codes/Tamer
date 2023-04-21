package com.leemba.monitor.server.dao.reports;

import com.leemba.monitor.server.dao.Query;
import com.leemba.monitor.server.objects.state.Event;
import java.util.List;

/**
 *
 * @author mrjohnson
 */
public class EventRecentReport {

    public List<Event> getReport() throws ReportException {
        return getReport(100);
    }

    public List<Event> getReport(int limit) throws ReportException {
        Query query = null;
        try {
            query = new Query();
            return query.prepScript("event/recentreport.sql", new Query.SqlName("limit", limit)).list(Event.class);
        } catch (Throwable e) {
            throw new ReportException("Report failed", e);
        } finally {
            if (query != null) query.close();
        }
    }
}
