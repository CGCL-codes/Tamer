package org.pprun.common.monitor;

/**
 * This is the interface for JMX Bean that monitors JMX gc memory or thread.
 *
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public interface JvmMonitorMXBean extends Statistic {

    /**
     *  the total amount of memory in the Java virtual machine, measured in bytes.
     */
    long getTotalMemory();

    /**
     * the amount of free memory in the Java Virtual Machine, measured in bytes.
     */
    long getFreeMemory();

    /**
     * the maximum amount of memory that the Java virtual machine will attempt to use, measured in bytes.
     */
    long getMaxMemory();

    /**
     * an estimate of the number of active threads in this thread group.
     */
    int getThreadsCount();
}
