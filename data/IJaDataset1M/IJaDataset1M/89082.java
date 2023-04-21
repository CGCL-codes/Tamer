package org.nexopenframework.management.monitor.support;

import java.util.List;
import org.nexopenframework.management.monitor.jmx.CounterMonitorMBean;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @see org.nexopenframework.management.monitor.jmx.CounterMonitorMBean
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0.m2
 */
public interface CpuMonitorMBean extends CounterMonitorMBean<Double> {

    String getMonitorKey();

    long getPid();

    double getCpuUsage();

    String getCpuUsageWithFormat();

    String getWorkingDirectory();

    List<String> getProcessArgs();

    long getCpuCriticalProblems();

    long getCpuWarningProblems();

    double getCriticalThreshold();

    void setCriticalThreshold(final double criticalThreshold);
}
