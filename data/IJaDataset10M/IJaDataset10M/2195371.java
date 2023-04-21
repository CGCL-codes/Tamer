package org.chires.model.history.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.modeler.Registry;
import javax.management.j2ee.statistics.Stats;
import java.net.URL;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Sep 12, 2004
 * @version $Revision: 1.1.1.1 $($Author: ghinkl $ / $Date: 2004/11/12 03:47:36 $)
 */
public class JDBCModel implements JDBCModelMBean {

    protected JDBCStats stats = new JDBCStats();

    static int queryIndex;

    private static final JDBCModel INSTANCE = new JDBCModel();

    private Log log = LogFactory.getLog(getClass());

    private JDBCModel() {
        log.info("Building Chires JDBC Statistics Model.");
        try {
            URL url = this.getClass().getResource("/org/chires/model/history/jdbc/mbeans-descriptors.xml");
            Registry registry = Registry.getRegistry(this, null);
            registry.loadMetadata(url);
            registry.registerComponent(this, "Chires:type=JDBCModel,name=JDBCModel", "org.chires.model.history.jdbc.JDBCModel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JDBCModel getInstance() {
        return INSTANCE;
    }

    public boolean isStatisticsProvider() {
        return true;
    }

    public Stats getStats() {
        return stats;
    }

    public void resetStats() {
        this.stats = new JDBCStats();
    }

    public JDBCQueryStats getStatsForQuery(String query) {
        JDBCQueryStats stat = stats.getQueryStats(query);
        if (stat == null) {
            stat = new JDBCQueryStats();
            stat.setStatement(query);
            stat.setQueryId(queryIndex++);
            stats.putStatistic(query, stat);
        }
        return stat;
    }
}
