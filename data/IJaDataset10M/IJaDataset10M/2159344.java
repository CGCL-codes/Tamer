package net.sf.katta.node.monitor;

import static org.junit.Assert.assertEquals;
import net.sf.katta.AbstractZkTest;
import net.sf.katta.node.monitor.MetricLogger.OutputType;
import net.sf.katta.protocol.InteractionProtocol;
import org.junit.Test;

public class MetricLoggerTest extends AbstractZkTest {

    @Test
    public void testLogMetric() throws Exception {
        InteractionProtocol protocol = _zk.createInteractionProtocol();
        MetricLogger metricLogger = new MetricLogger(OutputType.Log4J, protocol);
        protocol.setMetric("node1", new MetricsRecord("node1"));
        Thread.sleep(500);
        protocol.setMetric("node1", new MetricsRecord("node1"));
        Thread.sleep(500);
        assertEquals(2, metricLogger.getLoggedRecords());
        protocol.unregisterComponent(metricLogger);
        protocol.disconnect();
    }
}
