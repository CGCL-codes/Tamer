package org.chires.model.history.jdbc;

import javax.management.j2ee.statistics.Stats;
import javax.management.j2ee.statistics.Statistic;
import javax.management.j2ee.statistics.TimeStatistic;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Serializable;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Sep 13, 2004
 * @version $Revision: 1.1.1.1 $($Author: ghinkl $ / $Date: 2004/11/12 03:47:36 $)
 */
public class JDBCStats implements Stats, Serializable {

    private Map queryStatsMap = new HashMap();

    public void putStatistic(String name, JDBCQueryStats queryStats) {
        queryStatsMap.put(name, queryStats);
    }

    public JDBCQueryStats[] getQueryStats() {
        System.out.println("Queries: " + queryStatsMap.size());
        return (JDBCQueryStats[]) queryStatsMap.values().toArray(new JDBCQueryStats[queryStatsMap.size()]);
    }

    public JDBCQueryStats getQueryStats(String query) {
        return (JDBCQueryStats) queryStatsMap.get(query);
    }

    public Statistic getStatistic(String s) {
        return null;
    }

    public String[] getStatisticNames() {
        return new String[0];
    }

    public Statistic[] getStatistics() {
        return new Statistic[0];
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getClass().getName());
        buf.append("\n\t[");
        for (int i = 0; i < getStatistics().length; i++) {
            Statistic statistic = getStatistics()[i];
            if (i > 0) buf.append(',');
            buf.append("{");
            buf.append(statistic.toString());
            buf.append("}\n");
        }
        return buf.toString();
    }
}
