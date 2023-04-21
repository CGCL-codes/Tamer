package org.sulweb.infureports;

import org.sulweb.infumon.common.session.DBRowsProvider;
import java.util.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import org.sulweb.infumon.common.db.DBSearch;
import org.sulweb.infumon.common.db.EventPointDetailRow;
import org.sulweb.infumon.common.db.EventPointRow;
import org.sulweb.infumon.common.db.SearchResults;
import org.sulweb.infumon.common.db.SessionRow;

/**
 * <p>Title: InfuGraph</p>
 * <p>Description: Frontend for Infumon database log display</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Elaborazione Dati Pinerolo srl</p>
 * @author Lucio Crusca
 * @version 1.0
 */
public class LogTable<T extends EventPointDetailRow> implements DBRowsProvider<T> {

    protected List<T> details;

    private String pumpAddress;

    private boolean avoidUpdate;

    private static Date endOfTimes;

    private Connection conn;

    public LogTable(List<T> forced) {
        details = forced;
        avoidUpdate = true;
    }

    public LogTable(String pumpAddress) {
        this.pumpAddress = pumpAddress;
        details = new LinkedList<T>();
        if (endOfTimes == null) endOfTimes = new GregorianCalendar(2036, 1, 1).getTime();
    }

    public final synchronized void update(Connection conn) throws SQLException {
        this.conn = conn;
        if (avoidUpdate) return;
        DBSearch srch = new DBSearch(conn);
        srch.setOpenSessionFilter(getPumpAddress());
        srch.setDetailedResults(false);
        if (details.size() > 0) {
            T lastRow = details.get(details.size() - 1);
            srch.setTimeLimits(new Date(lastRow.getEventPointObj().getTime() + 1), endOfTimes);
        }
        SearchResults res = srch.getResults();
        List<SessionRow> sl = res.getSessions();
        assert (sl.size() <= 1);
        if (sl.size() == 0) return;
        List<EventPointRow> rows = sl.get(0).getEventPoints();
        synchronized (details) {
            for (EventPointRow epr : rows) details.add((T) epr.getDetail());
        }
    }

    public final ArrayList<T> getSortedRecords() {
        ArrayList<T> result;
        synchronized (details) {
            result = new ArrayList<T>(details.size());
            result.addAll(details);
            details.clear();
            Collections.sort(result);
        }
        return result;
    }

    public boolean equals(Object o) {
        LogTable l = (LogTable) o;
        return getPumpAddress().equals(l.getPumpAddress());
    }

    public Connection getConnection() {
        return conn;
    }

    public String getPumpAddress() {
        return pumpAddress;
    }
}
