package net.taylor.workflow.jmx.timer;

/**
 * This mbean reinitializes timers at startup.
 * 
 * @author jgilbert
 * @version $Id: TimerManagerMBean.java,v 1.1 2005/09/05 00:19:22 jgilbert01 Exp $
 * 
 */
public interface TimerManagerMBean {

    /**
	 * The start lifecycle operation
	 */
    public void start() throws Exception;

    /**
	 * The stop lifecycle operation
	 */
    public void stop() throws Exception;
}
