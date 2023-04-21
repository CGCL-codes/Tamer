package management.impl;

import management.JmxStatisticsSingleton;
import management.Statistics;

/**
 * Provides statistic data for retrieval via JMX.
 * @author Peter Falkensteiner
 */
public class PdfConverterStatistics extends Statistics {

    /**
	 * Register MBean at JMX-Server.
	 */
    public PdfConverterStatistics() {
        JmxStatisticsSingleton.getInstance().registerStatisticsMBean(this);
    }
}
