package org.appspy.perf.servlet.provider;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.appspy.perf.data.ServletTimerData;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class TimerDataProvider extends AbstractDataProvider {

    public static final String START_TIME = "startTime";

    public static final String START_BLOCK_TIME = "startBlockTime";

    public static final String START_BLOCK_COUNT = "startBlockCount";

    public static final String START_CPU_TIME = "startCPUTime";

    public static final String START_USER_CPU_TIME = "startUserCPUTime";

    public static final String START_WAIT_TIME = "startWaitTime";

    public static final String START_WAIT_COUNT = "startWaitCount";

    public void afterRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext, Throwable throwable) {
        Long initialTime = (Long) getAttribute(servletTimerData, START_TIME);
        long timerDelay = System.currentTimeMillis() - initialTime;
        servletTimerData.setTimerDelay(timerDelay);
        long threadId = Thread.currentThread().getId();
        ThreadMXBean mxb = ManagementFactory.getThreadMXBean();
        if (mxb.isThreadCpuTimeEnabled()) {
            Long initialCpuTime = (Long) getAttribute(servletTimerData, START_CPU_TIME);
            if (initialCpuTime != null) {
                long cpuTime = mxb.getCurrentThreadCpuTime() - initialCpuTime;
                servletTimerData.setCPUTime(cpuTime);
            }
            Long initialUserCpuTime = (Long) getAttribute(servletTimerData, START_USER_CPU_TIME);
            if (initialUserCpuTime != null) {
                long userCpuTime = mxb.getCurrentThreadUserTime() - initialUserCpuTime;
                servletTimerData.setUserCPUTime(userCpuTime);
            }
        }
        ThreadInfo threadInfo = mxb.getThreadInfo(threadId);
        Long initialWaitCount = (Long) getAttribute(servletTimerData, START_WAIT_COUNT);
        if (initialWaitCount != null) {
            long waitCount = threadInfo.getWaitedCount() - initialWaitCount;
            servletTimerData.setWaitCount(waitCount);
        }
        Long initialBlockCount = (Long) getAttribute(servletTimerData, START_BLOCK_COUNT);
        if (initialBlockCount != null) {
            long blockCount = threadInfo.getBlockedCount() - initialBlockCount;
            servletTimerData.setBlockingCount(blockCount);
        }
        if (mxb.isThreadContentionMonitoringEnabled()) {
            Long initialWaitTime = (Long) getAttribute(servletTimerData, START_WAIT_TIME);
            if (initialWaitTime != null) {
                long waitTime = threadInfo.getWaitedTime() - initialWaitTime;
                servletTimerData.setWaitDelay(waitTime);
            }
            Long initialBlockTime = (Long) getAttribute(servletTimerData, START_BLOCK_TIME);
            if (initialBlockTime != null) {
                long blockTime = threadInfo.getBlockedTime() - initialBlockTime;
                servletTimerData.setBlockingDelay(blockTime);
            }
        }
    }

    public void beforeRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext) {
        servletTimerData.setCollectionDate(new Date());
        setAttribute(servletTimerData, START_TIME, System.currentTimeMillis());
        long threadId = Thread.currentThread().getId();
        ThreadMXBean mxb = ManagementFactory.getThreadMXBean();
        if (mxb.isThreadCpuTimeEnabled()) {
            long cpuTime = mxb.getCurrentThreadCpuTime();
            setAttribute(servletTimerData, START_CPU_TIME, cpuTime);
            long userCpuTime = mxb.getCurrentThreadUserTime();
            setAttribute(servletTimerData, START_USER_CPU_TIME, userCpuTime);
        }
        ThreadInfo threadInfo = mxb.getThreadInfo(threadId);
        long waitCount = threadInfo.getWaitedCount();
        setAttribute(servletTimerData, START_WAIT_COUNT, waitCount);
        long blockCount = threadInfo.getBlockedCount();
        setAttribute(servletTimerData, START_BLOCK_COUNT, blockCount);
        if (mxb.isThreadContentionMonitoringEnabled()) {
            long waitTime = threadInfo.getWaitedTime();
            setAttribute(servletTimerData, START_WAIT_TIME, waitTime);
            long blockTime = threadInfo.getBlockedTime();
            setAttribute(servletTimerData, START_BLOCK_TIME, blockTime);
        }
    }
}
