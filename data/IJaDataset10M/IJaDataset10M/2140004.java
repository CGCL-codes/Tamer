package org.toobsframework.performance.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerformanceMetricGroup implements IPerformanceInfo {

    private static final String GROUP = "group";

    private Map<String, PerformanceMetric> metrics = new ConcurrentHashMap<String, PerformanceMetric>();

    private PerformanceMetric getPerformanceMetric(String label) {
        PerformanceMetric metric = metrics.get(label);
        if (metric == null) {
            metric = new PerformanceMetric();
            metrics.put(label, metric);
        }
        return metric;
    }

    public void reportTime(String name, double time) {
        PerformanceMetric metric = getPerformanceMetric(name);
        metric.reportTime(name, time);
    }

    public Map<String, PerformanceMetric> getMetrics() {
        return metrics;
    }

    public String getType() {
        return GROUP;
    }
}
