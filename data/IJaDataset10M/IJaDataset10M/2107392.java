package net.sf.katta.node.monitor;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.sf.katta.protocol.ConnectedComponent;
import net.sf.katta.protocol.IAddRemoveListener;
import net.sf.katta.protocol.InteractionProtocol;
import net.sf.katta.util.ZkConfiguration.PathDef;
import org.I0Itec.zkclient.IZkDataListener;
import org.apache.log4j.Logger;

public class MetricLogger implements IZkDataListener, ConnectedComponent {

    public enum OutputType {

        Log4J, SysOut
    }

    private static final Logger LOG = Logger.getLogger(MetricLogger.class);

    private OutputType _outputType;

    private ReentrantLock _lock;

    protected final InteractionProtocol _protocol;

    private long _loggedRecords = 0;

    public MetricLogger(OutputType outputType, InteractionProtocol protocol) {
        _protocol = protocol;
        _outputType = outputType;
        _protocol.registerComponent(this);
        List<String> children = _protocol.registerChildListener(this, PathDef.NODE_METRICS, new IAddRemoveListener() {

            @Override
            public void removed(String name) {
                unsubscribeDataUpdates(name);
            }

            @Override
            public void added(String name) {
                MetricsRecord metric = _protocol.getMetric(name);
                logMetric(metric);
                subscribeDataUpdates(name);
            }
        });
        for (String node : children) {
            subscribeDataUpdates(node);
        }
        _lock = new ReentrantLock();
        _lock.lock();
    }

    protected void subscribeDataUpdates(String nodeName) {
        _protocol.registerDataListener(this, PathDef.NODE_METRICS, nodeName, this);
    }

    protected void unsubscribeDataUpdates(String nodeName) {
        _protocol.unregisterDataChanges(this, PathDef.NODE_METRICS, nodeName);
    }

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        MetricsRecord metrics = (MetricsRecord) data;
        logMetric(metrics);
    }

    protected void logMetric(MetricsRecord metrics) {
        switch(_outputType) {
            case Log4J:
                LOG.info(metrics);
                break;
            case SysOut:
                System.out.println(metrics.toString());
                break;
            default:
                throw new IllegalStateException("output type " + _outputType + " not supported");
        }
        _loggedRecords++;
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
    }

    public void join() throws InterruptedException {
        synchronized (_lock) {
            _lock.wait();
        }
    }

    public long getLoggedRecords() {
        return _loggedRecords;
    }

    @Override
    public void disconnect() {
        _lock.unlock();
    }

    @Override
    public void reconnect() {
    }
}
