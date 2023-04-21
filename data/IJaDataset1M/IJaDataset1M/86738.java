package net.sf.pamvi.ch1_automating_vi_administration;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.*;
import org.apache.log4j.*;

/**
 * This class demonstrates how to export events that occur on a VI server to
 * a Syslog server, Windows event log server, or STDOUT.
 * @author Schley Andrew Kutz &lt;sakutz@gmail.com&gt;
 */
public class ExportingEvents {

    /**
	 * Makes this class uninstantiable.
	 */
    private ExportingEvents() {
    }

    /**
	 * A dictionary of property filters that keep track of running tasks. The
	 * dictionary is keyed on a TaskInfo object's key value.
	 */
    private static java.util.Hashtable<String, PropertyFilter> m_pfilters = new java.util.Hashtable<String, PropertyFilter>();

    /**
	 * A lock used to synchronize access to the property filters collection.
	 */
    private static java.util.concurrent.locks.ReentrantLock m_lock_pfilters = new java.util.concurrent.locks.ReentrantLock();

    /**
	 * The logger used to record events and tasks for this class.
	 */
    private static Logger m_logger = Logger.getLogger(ExportingEvents.class);

    /**
	 * The ServiceInstance this object uses.
	 */
    private static ServiceInstance m_si;

    /**
	 * The address of the syslog server.
	 */
    private static String m_syslog_server = null;

    /**
	 * The address of the windows server.
	 */
    private static String m_windows_server = null;

    /**
	 * True to monitor events and tasks initiated by the account logged into 
	 * the ServiceInstance; otherwise false. Setting this parameter to false 
	 * may reduce the amount of events and tasks that are squirted out of this 
	 * process.
	 */
    private static boolean m_mself = true;

    /**
	 * As long as the value is set to true the wait for updates thread will
	 * continue to run.
	 */
    private static boolean m_wfu = true;

    /**
	 * Logs the message to the log appenders.
	 * @param message The message to log.
	 */
    public static void logMessage(String message) {
        String msg = "";
        try {
            msg = String.format("%1$tb %1$te %1$tT %2$s monet: %3$s", java.util.Calendar.getInstance(), java.net.InetAddress.getLocalHost().getHostName(), message);
        } catch (java.net.UnknownHostException e) {
        }
        if (m_syslog_server != null || m_windows_server != null) {
            org.apache.log4j.PatternLayout layout = new org.apache.log4j.PatternLayout("%m%n");
            if (m_syslog_server != null) {
                org.apache.log4j.net.SyslogAppender o_syslog = new org.apache.log4j.net.SyslogAppender(layout, m_syslog_server, org.apache.log4j.net.SyslogAppender.LOG_LOCAL7);
                o_syslog.setThreshold(Level.DEBUG);
                m_logger.addAppender(o_syslog);
                m_logger.debug("monet: " + message);
                o_syslog.close();
                m_logger.removeAppender(o_syslog);
            } else if (m_windows_server != null) {
                org.apache.log4j.nt.NTEventLogAppender o_eventlog = new org.apache.log4j.nt.NTEventLogAppender(m_windows_server, "monet", layout);
                o_eventlog.setThreshold(Level.DEBUG);
                m_logger.addAppender(o_eventlog);
                m_logger.debug("monet: " + msg);
                o_eventlog.close();
                m_logger.removeAppender(o_eventlog);
            }
        }
        m_logger.info(msg);
    }

    /**
	 * Logs the given event to the log appenders.
	 * @param event The event to log.
	 */
    public static void logMessage(Event event) {
        String msg = String.format("TYPE=event; CHAINID=%1$s; KEY=%2$s; USER=%3$s; DESCRIPTION=%4$s", event.getChainId(), event.getKey(), event.getUserName(), event.getFullFormattedMessage());
        boolean su = java.util.regex.Pattern.matches(".*" + m_si.getSessionManager().getCurrentSession().getUserName() + ".*", event.getUserName());
        if (!su) {
            logMessage(msg);
        }
        if ((su && !java.util.regex.Pattern.matches(".*(logged in|out).*", event.getFullFormattedMessage())) || (su && m_mself)) {
            logMessage(msg);
        }
    }

    /**
	 * Logs the given task information to the log appenders.
	 * @param taskInfo The task information to log.
	 */
    public static void logMessage(TaskInfo taskInfo) {
        String username = "system";
        if (taskInfo.getReason().getClass().getName().equalsIgnoreCase("TaskReasonAlarm")) username = ((TaskReasonAlarm) (taskInfo.getReason())).getEntityName();
        if (taskInfo.getReason().getClass().getName().equalsIgnoreCase("TaskReasonSchedule")) username = ((TaskReasonSchedule) (taskInfo.getReason())).getName();
        if (taskInfo.getReason().getClass().getName().equalsIgnoreCase("TaskReasonUser")) username = ((TaskReasonUser) (taskInfo.getReason())).getUserName();
        int progress = taskInfo.getState() == TaskInfoState.running ? taskInfo.getProgress() : taskInfo.getState() == TaskInfoState.queued ? 0 : 100;
        String msg = String.format("TYPE=task; CHAINID=%1$s; KEY=%2$s; USER=%3$s; " + "DESCRIPTION=%4$s; PROGRESS=%5$s", taskInfo.getEventChainId(), taskInfo.getKey(), username, taskInfo.getName(), progress);
        logMessage(msg);
    }

    /**
	 * Starts exporting events.
	 * @param si The ServiceInstance this object uses.
	 * @param syslogServer The address of the syslog server to export events
	 *   to. Specify null to disable export to syslog.
	 * @param windowsServer The address of the Windows server to export events
	 *   to. Specify null to disable export to Windows.
	 * @param monitorSelf True to monitor events and tasks initiated by
	 *   the account logged into the ServiceInstance; otherwise false. Setting
	 *   this parameter to false may reduce the amount of events and tasks that
	 *   are squirted out of this process.
	 * @throws Exception When there is an error.
	 */
    public static void exportEvents(final ServiceInstance si, final String syslogServer, final String windowsServer, final boolean monitorSelf) throws Exception {
        m_si = si;
        m_syslog_server = syslogServer;
        m_windows_server = windowsServer;
        m_mself = monitorSelf;
        java.util.Properties props = new java.util.Properties();
        props.put("log4j.rootCategory", "DEBUG, CONSOLE");
        props.put("log4j.logger.org.apache.axis", "OFF");
        props.put("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
        props.put("log4j.appender.CONSOLE.Threshold", "INFO");
        props.put("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
        props.put("log4j.appender.CONSOLE.layout.ConversionPattern", "%m%n");
        PropertyConfigurator.configure(props);
        Thread twfu = new Thread(new WaitForUpdates());
        twfu.start();
    }

    /**
	 * A thread class that is used to wait for updates.
	 */
    private static class WaitForUpdates implements Runnable {

        public void run() {
            final PropertySpec pspec_tskmgr = new PropertySpec();
            pspec_tskmgr.setAll(false);
            pspec_tskmgr.setType("TaskManager");
            pspec_tskmgr.setPathSet(new String[] { "recentTask" });
            final ObjectSpec ospec_tskmgr = new ObjectSpec();
            ospec_tskmgr.setObj(m_si.getTaskManager().getMOR());
            final PropertySpec pspec_evtmgr = new PropertySpec();
            pspec_evtmgr.setAll(false);
            pspec_evtmgr.setType("EventManager");
            pspec_evtmgr.setPathSet(new String[] { "latestEvent" });
            final ObjectSpec ospec_evtmgr = new ObjectSpec();
            ospec_evtmgr.setObj(m_si.getEventManager().getMOR());
            final PropertyFilterSpec pfspec = new PropertyFilterSpec();
            pfspec.setObjectSet(new ObjectSpec[] { ospec_tskmgr, ospec_evtmgr });
            pfspec.setPropSet(new PropertySpec[] { pspec_tskmgr, pspec_evtmgr });
            try {
                m_si.getPropertyCollector().createFilter(pfspec, true);
            } catch (Exception e) {
            }
            String upver = "";
            while (m_wfu) {
                try {
                    upver = waitForUpdates(upver);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }

        /**
		 * Waits for updates.
		 * @param upver The update version.
		 * @return The new update version.
		 */
        private String waitForUpdates(String upver) throws Exception {
            final UpdateSet us = m_si.getPropertyCollector().waitForUpdates(upver);
            upver = us.getVersion();
            if (us.getFilterSet() == null) return upver;
            for (final PropertyFilterUpdate pfu : us.getFilterSet()) {
                final PropertyChange pc = pfu.getObjectSet(0).getChangeSet(0);
                if (pc.getName().startsWith("recentTask")) {
                    ManagedObjectReference[] tasks = pc.getVal().getClass().isArray() ? (ManagedObjectReference[]) pc.getVal() : new ManagedObjectReference[] { (ManagedObjectReference) pc.getVal() };
                    for (final ManagedObjectReference tmoref : tasks) {
                        final Task t = (Task) MorUtil.createExactManagedObject(m_si.getServerConnection(), tmoref);
                        m_lock_pfilters.lock();
                        if (!m_pfilters.containsKey(t.getTaskInfo().getKey())) {
                            logMessage(t.getTaskInfo());
                            final PropertySpec pspec_task = new PropertySpec();
                            pspec_task.setAll(false);
                            pspec_task.setType("Task");
                            pspec_task.setPathSet(new String[] { "info" });
                            final ObjectSpec ospec_task = new ObjectSpec();
                            ospec_task.setObj(tmoref);
                            final PropertyFilterSpec pfspec_task = new PropertyFilterSpec();
                            pfspec_task.setObjectSet(new ObjectSpec[] { ospec_task });
                            pfspec_task.setPropSet(new PropertySpec[] { pspec_task });
                            m_pfilters.put(t.getTaskInfo().getKey(), m_si.getPropertyCollector().createFilter(pfspec_task, false));
                        }
                        m_lock_pfilters.unlock();
                    }
                } else if (pc.getName().startsWith("latestEvent")) {
                    final Event evt = (Event) pc.getVal();
                    logMessage(evt);
                } else if (pc.getName().startsWith("info")) {
                    TaskInfo[] tis = pc.getVal().getClass().isArray() ? (TaskInfo[]) pc.getVal() : new TaskInfo[] { (TaskInfo) pc.getVal() };
                    for (final TaskInfo ti : tis) {
                        if (ti.getState() == TaskInfoState.error || ti.getState() == TaskInfoState.success) {
                            logMessage(ti);
                            m_lock_pfilters.lock();
                            if (m_pfilters.containsKey(ti.getKey())) {
                                m_si.getServerConnection().getVimService().destroyPropertyFilter(m_pfilters.get(ti.getKey()).getMOR());
                                m_pfilters.remove(ti.getKey());
                            }
                        }
                    }
                }
            }
            return (upver);
        }
    }
}
