package com.googlecode.psiprobe.beans.stats.providers;

import com.googlecode.psiprobe.model.stats.StatsCollection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.springframework.web.bind.ServletRequestUtils;

public class ConnectorSeriesProvider extends AbstractSeriesProvider {

    public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request) {
        String connectorName = ServletRequestUtils.getStringParameter(request, "cn", null);
        String statType = ServletRequestUtils.getStringParameter(request, "st", null);
        String series1Legend = ServletRequestUtils.getStringParameter(request, "sl", "");
        if (connectorName != null && statType != null) {
            List l = statsCollection.getStats("stat.connector." + connectorName + "." + statType);
            if (l != null) {
                dataset.addSeries(toSeries(series1Legend, l));
            }
        }
    }
}
