package org.eyewitness.hids.main;

/**
 *
 * @author vkorennoy
 */
public class HidsStarter {

    private StatisticsGatherer gatherer = new StatisticsGatherer();

    public void run() {
        gatherer.run();
    }

    public StatisticsGatherer getGatherer() {
        return gatherer;
    }

    public boolean isAnomalitiesDetected() {
        return false;
    }
}
