package org.opennms.netmgt.actiond;

import java.lang.reflect.UndeclaredThrowableException;
import org.opennms.core.queue.FifoQueue;
import org.opennms.core.queue.FifoQueueImpl;
import org.opennms.netmgt.config.ActiondConfigFactory;
import org.opennms.netmgt.daemon.AbstractServiceDaemon;

/**
 * This class is used to represent the auto action execution service. When an
 * event is received by this service that has one of either a notification,
 * trouble ticket, or auto action then a process is launched to execute the
 * appropriate commands.
 * 
 * @author <a href="mailto:mike@opennms.org">Mike Davidson </a>
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 * @author <a href="http://www.opennms.org/">OpenNMS.org </a>
 * 
 */
public final class Actiond extends AbstractServiceDaemon {

    /**
     * The singleton instance.
     */
    private static final Actiond m_singleton = new Actiond();

    /**
     * The execution launcher and reaper
     */
    private Executor m_executor;

    /**
     * The broadcast event receiver.
     */
    private BroadcastEventProcessor m_eventReader;

    private ActiondConfigFactory m_actiondConfig;

    /**
     * Constructs a new Action execution daemon.
     */
    private Actiond() {
        super("OpenNMS.Actiond");
        m_executor = null;
        m_eventReader = null;
    }

    protected void onInit() {
        FifoQueue<String> execQ = new FifoQueueImpl<String>();
        try {
            m_eventReader = new BroadcastEventProcessor(execQ);
        } catch (Exception ex) {
            log().error("Failed to setup event reader", ex);
            throw new UndeclaredThrowableException(ex);
        }
        m_executor = new Executor(execQ, m_actiondConfig.getMaxProcessTime(), m_actiondConfig.getMaxOutstandingActions());
    }

    protected void onStart() {
        if (m_executor == null) {
            init();
        }
        m_executor.start();
    }

    protected void onStop() {
        try {
            if (m_executor != null) {
                m_executor.stop();
            }
        } catch (Exception e) {
        }
        if (m_eventReader != null) {
            m_eventReader.close();
        }
        m_eventReader = null;
        m_executor = null;
        m_actiondConfig = null;
    }

    protected void onPause() {
        m_executor.pause();
    }

    protected void onResume() {
        m_executor.resume();
    }

    /**
     * Returns the singular instance of the actiond daemon. There can be only
     * one instance of this service per virtual machine.
     */
    public static Actiond getInstance() {
        return m_singleton;
    }

    public BroadcastEventProcessor getEventReader() {
        return m_eventReader;
    }

    public void setEventReader(BroadcastEventProcessor eventReader) {
        m_eventReader = eventReader;
    }

    public Executor getExecutor() {
        return m_executor;
    }

    public void setExecutor(Executor executor) {
        m_executor = executor;
    }

    public ActiondConfigFactory getActiondConfig() {
        return m_actiondConfig;
    }

    public void setActiondConfig(ActiondConfigFactory actiondConfig) {
        m_actiondConfig = actiondConfig;
    }
}
