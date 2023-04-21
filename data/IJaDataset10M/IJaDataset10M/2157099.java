package net.sourceforge.processdash.ev.ui.chart;

import net.sourceforge.processdash.ev.EVMetrics;
import net.sourceforge.processdash.ev.EVMetricsRollup;

public class ConfidenceIntervalCompletionDateChartData extends ConfidenceIntervalChartData {

    private EVMetrics evMetrics;

    public ConfidenceIntervalCompletionDateChartData(ChartEventAdapter eventAdapter, EVMetrics evMetrics) {
        super(eventAdapter, 0, getMaxChartDate());
        this.evMetrics = evMetrics;
    }

    public void recalc() {
        clearSeries();
        maybeAddSeries(evMetrics.getDateConfidenceInterval(), "Forecast");
        if (evMetrics instanceof EVMetricsRollup) {
            EVMetricsRollup rollupMetrics = (EVMetricsRollup) evMetrics;
            maybeAddSeries(rollupMetrics.getOptimizedDateConfidenceInterval(), "Optimized_Forecast");
        }
    }

    private static final long HOUR_MILLIS = 60L * 60L * 1000L;

    private static final long DAY_MILLIS = 24L * HOUR_MILLIS;

    private static final long YEAR_MILLIS = 365L * DAY_MILLIS;

    private static final long MAX_CHART_WIDTH = 50L * YEAR_MILLIS;

    static long getMaxChartDate() {
        return System.currentTimeMillis() + MAX_CHART_WIDTH;
    }
}
