package org.nexopenframework.management.monitor.plugins.jdk6;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.management.monitor.plugins.PluginException;
import org.nexopenframework.management.monitor.plugins.Process;
import org.nexopenframework.management.monitor.plugins.ProcessManager;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Implementation of {@link ProcessManager} based in features of J2SE 6.0</p>
 * 
 * @see org.nexopenframework.management.monitor.plugins.ProcessManager
 * @see java.lang.management.OperatingSystemMXBean
 * @author Francesc Xavier Magdaleno
 * @version $Revision 3237,$Date 10/01/2009 11:53:18
 * @since 1.0.0.m3
 */
public class Jdk6ProcessManager implements ProcessManager {

    /**Logging facility based in Jakarta Commons Logging*/
    protected static final Log logger = LogFactory.getLog(Jdk6ProcessManager.class);

    /**Method related to <code>getSystemLoadAverage</code> available since J2SE 6.0*/
    private Method jdk6CpuLoadMtd = null;

    /**MXBean for dealing with OS in which the Java virtual machine is running*/
    private OperatingSystemMXBean os = null;

    public void clear() {
    }

    public void init() throws PluginException {
        try {
            jdk6CpuLoadMtd = OperatingSystemMXBean.class.getMethod("getSystemLoadAverage");
        } catch (final NoSuchMethodException e) {
            throw new PluginException("Not a J2SE 6.0 environment", e);
        }
        os = ManagementFactory.getOperatingSystemMXBean();
    }

    public void destroy() {
    }

    /**
	 * <p>Returns the system load average for the last minute. The system load average is the sum of the number 
	 * of runnable entities queued to the available processors  and the number of runnable entities running 
	 * on the available processors averaged over a period of time. The way in which the load average 
	 * is calculated is operating system specific but is typically a damped time-dependent average.
	 * If the load average is not available, a negative value is returned.
	 * This method is designed to provide a hint about the system load and may be queried frequently. 
	 * The load average may be unavailable on some platform where it is expensive to implement this method.</p>
	 * 
	 * <p><b>IMPORTANT NOTE</b>: It is independent of <code>pid</code> passed as argument</p>
	 */
    public double getCpuUsage(final long pid) throws PluginException {
        if (jdk6CpuLoadMtd == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("Not invoked init method. ProcessManager implements Lifecycle contract. " + "Invoke init method for avoid an ugly NPE.");
            }
            this.init();
        }
        try {
            final Double value = (Double) jdk6CpuLoadMtd.invoke(os);
            return value * 100;
        } catch (final InvocationTargetException e) {
            return -1;
        } catch (final Throwable e) {
            return -1;
        }
    }

    public String getCpuUsageWithFormat(final long pid) throws PluginException {
        return null;
    }

    /**
	 * <p>We return value provide from {@link RuntimeMXBean#getName()}. If some problems occurs, it returns <code>0</code>.</p>
	 * 
	 * @see java.lang.management.RuntimeMXBean#getName()
	 * @see org.nexopenframework.management.monitor.plugins.ProcessManager#getCurrentPid()
	 */
    public long getCurrentPid() {
        final RuntimeMXBean rmxb = ManagementFactory.getRuntimeMXBean();
        final String name = rmxb.getName();
        if (name.indexOf("@") > -1) {
            final String pid = name.substring(0, name.indexOf("@"));
            try {
                return Long.parseLong(pid);
            } catch (final NumberFormatException e) {
                logger.info("NumberFormatException with pid :: " + e.getMessage());
            }
        }
        return 0;
    }

    public String[] getProcessArgs(final long pid) throws PluginException {
        return new String[0];
    }

    /**
	 * <p>It returns a single {@link Process} with some information [current PID process]</p>
	 * 
	 * @see #getCurrentPid()
	 * @see org.nexopenframework.management.monitor.plugins.ProcessManager#getProcesses(java.lang.String)
	 */
    public List<Process> getProcesses(final String query) throws PluginException {
        final Process process = new Process(this.getCurrentPid(), query, null, "<NOT_PRESENT>", this);
        return Collections.singletonList(process);
    }
}
