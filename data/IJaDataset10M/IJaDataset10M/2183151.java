package org.xactor.test.txtimer.interfaces;

/**
 * Local interface for test/txtimer/TimerSession.
 */
public interface TimerSessionLocal extends javax.ejb.EJBLocalObject {

    public void createTimer(long duration, long periode, java.io.Serializable info);

    public void cancelFirstTimer();

    /**
    * This is not allowed on the remote interface.
    */
    public java.lang.Object createTimerReturnHandle(long duration);

    /**
    * This is not allowed on the remote interface.
    */
    public java.lang.String passTimerHandle(java.lang.Object handle);

    public void resetCallCount();

    public int getCallCount();

    public int getGlobalCallCount();

    public java.util.List getTimers();

    public java.security.Principal getEjbTimeoutCaller();
}
