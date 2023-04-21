package infrastructureAPI.algorithms.monitorTools;

import gossipServices.aggregation.AggregationService;
import gossipServices.aggregation.SingleValueDouble;

/**
 * Description: 
 *
 */
public class MonitorInstancesThread extends Thread implements MonitoringService {

    protected static final int MULTIPLIER = 4;

    protected long INTERVAL_TIMER;

    protected AggregationService aggregationService;

    protected SingleValueDouble aggregationValue;

    protected double lastAggValue;

    protected boolean hasToStop = false;

    public MonitorInstancesThread() {
    }

    public MonitorInstancesThread(long interval, AggregationService aggregation) {
        this.INTERVAL_TIMER = interval;
        this.aggregationService = aggregation;
        this.setName("MonitorInstancesThread");
        this.aggregationValue = aggregationService.getActualValue();
        lastAggValue = aggregationValue.getValue();
    }

    public void run() {
        double totalNodes, aggValue;
        while (!hasToStop) {
            try {
                sleep(INTERVAL_TIMER);
                synchronized (aggregationValue) {
                    aggValue = aggregationValue.getValue();
                }
                if (aggValue > MULTIPLIER * lastAggValue) {
                    synchronized (aggregationService.getLastStableValue()) {
                        aggValue = aggregationService.getLastStableValue().getValue();
                    }
                }
                totalNodes = Math.round(1 / aggValue);
                lastAggValue = aggValue;
                System.out.println("Number of total nodes: " + totalNodes);
            } catch (InterruptedException e) {
                if (hasToStop) System.out.println("Stopped " + this.getName()); else e.printStackTrace();
            }
        }
    }

    @Override
    public Class<?> getServiceClass() {
        return MonitoringService.class;
    }

    @Override
    public void startMonitor() {
        this.start();
    }

    @Override
    public void stopMonitor() {
        hasToStop = true;
        this.interrupt();
    }
}
