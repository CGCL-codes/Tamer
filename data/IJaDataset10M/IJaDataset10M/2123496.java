package com.commsen.stopwatch.jmx;

import com.commsen.stopwatch.Report;
import com.commsen.stopwatch.reports.StatisticalSummary;
import com.commsen.stopwatch.util.ReportComparator;

public interface StopwatchManagerMBean {

    public void start();

    public void stop();

    public void reset();

    public Report[] getReports(String group, String label, ReportComparator reportComparator);

    public Report[] getAllByGroupReports(ReportComparator reportComparator);

    public Report[] getAllByLabelReports(ReportComparator reportComparator);

    public long[] getLoadReports(String group, String label, int periodField, int numberOfPeriods);

    public StatisticalSummary getStatisticalSummary(String group, String label, int window);

    public String getReportsAsXML(String group, String label);

    public String getReportsAsString(String group, String label);

    public boolean isDebug();

    public void setDebug(boolean debug);

    public String getEngine();

    public void setEngine(String engine);

    public String getStorage();

    public void setStorage(String storage);

    public boolean isActive();

    public boolean isChanged();
}
