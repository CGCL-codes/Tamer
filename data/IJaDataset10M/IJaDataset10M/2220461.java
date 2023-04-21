package pspdash;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import DistLib.uniform;

public class EVScheduleConfidenceIntervals {

    public interface Randomizable {

        public void randomize(uniform u);
    }

    private static final int BOOTSTRAP_SIZE = 1000;

    EVSchedule schedule;

    List randomObjects;

    EVMetrics metrics;

    MonteCarloConfidenceInterval cost, date, optimizedDate;

    public EVScheduleConfidenceIntervals(EVSchedule sched, List randomObjects) {
        this.schedule = sched;
        this.randomObjects = randomObjects;
        this.metrics = sched.getMetrics();
        cost = new MonteCarloConfidenceInterval();
        date = new MonteCarloConfidenceInterval();
        if (metrics instanceof EVMetricsRollup) optimizedDate = new MonteCarloConfidenceInterval();
        runSimulation();
    }

    public ConfidenceInterval getCostInterval() {
        return cost;
    }

    public ConfidenceInterval getForecastDateInterval() {
        return date;
    }

    public ConfidenceInterval getOptimizedForecastDateInterval() {
        return optimizedDate;
    }

    private static final boolean USE_RATIO = true;

    private void runSimulation() {
        long start = System.currentTimeMillis();
        uniform random = new uniform();
        int sampleCount = Settings.getInt("ev.simulationSize", BOOTSTRAP_SIZE);
        if (USE_RATIO) {
            double factor = Math.exp(0.75 * Math.log(randomObjects.size()));
            sampleCount = (int) (sampleCount / factor);
            if (sampleCount < 100) sampleCount = 100;
        }
        for (int i = 0; i < sampleCount; i++) runOneTest(random);
        cost.samplesDone();
        date.samplesDone();
        if (optimizedDate != null) optimizedDate.samplesDone();
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("schedule simulation took " + elapsed + " ms.");
        if (optimizedDate != null) date.debug = true;
    }

    private void runOneTest(uniform random) {
        randomizeAll(random);
        double forecastCost = metrics.independentForecastCost();
        cost.addSample(forecastCost - metrics.actualTime);
        date.addSample(getTime(metrics.independentForecastDate()));
        if (optimizedDate != null) {
            Date optDate = schedule.getHypotheticalDate(forecastCost, true);
            optimizedDate.addSample(getTime(optDate));
        }
    }

    private void randomizeAll(uniform random) {
        Iterator i = randomObjects.iterator();
        while (i.hasNext()) ((Randomizable) i.next()).randomize(random);
    }

    private double getTime(Date d) {
        return (d == null ? EVSchedule.NEVER.getTime() : d.getTime());
    }
}
