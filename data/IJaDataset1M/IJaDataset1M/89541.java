package org.apache.hadoop.metrics.spi;

import org.apache.hadoop.metrics.ContextFactory;
import org.apache.hadoop.metrics.MetricsException;

/**
 * A null context which has a thread calling 
 * periodically when monitoring is started. This keeps the data sampled 
 * correctly.
 * In all other respects, this is like the NULL context: No data is emitted.
 * This is suitable for Monitoring systems like JMX which reads the metrics
 *  when someone reads the data from JMX.
 * 
 * The default impl of start and stop monitoring:
 *  is the AbstractMetricsContext is good enough.
 * 
 */
public class NullContextWithUpdateThread extends AbstractMetricsContext {

    private static final String PERIOD_PROPERTY = "period";

    /** Creates a new instance of NullContextWithUpdateThread */
    public NullContextWithUpdateThread() {
    }

    public void init(String contextName, ContextFactory factory) {
        super.init(contextName, factory);
        String periodStr = getAttribute(PERIOD_PROPERTY);
        if (periodStr != null) {
            int period = 0;
            try {
                period = Integer.parseInt(periodStr);
            } catch (NumberFormatException nfe) {
            }
            if (period <= 0) {
                throw new MetricsException("Invalid period: " + periodStr);
            }
            setPeriod(period);
        }
    }

    /**
   * Do-nothing version of emitRecord
   */
    protected void emitRecord(String contextName, String recordName, OutputRecord outRec) {
    }

    /**
   * Do-nothing version of update
   */
    protected void update(MetricsRecordImpl record) {
    }

    /**
   * Do-nothing version of remove
   */
    protected void remove(MetricsRecordImpl record) {
    }
}
