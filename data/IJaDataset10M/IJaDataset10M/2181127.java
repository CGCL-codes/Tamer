package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of interface "PullMonitor"
 *	@author JacORB IDL compiler 
 */
public interface PullMonitorOperations {

    void start_monitoring(org.omg.CORBA.FT.PullMonitorable target, int granularity, long monitoring_interval, long monitoring_timeout);

    void update_monitoring(org.omg.CORBA.FT.PullMonitorable target, int granularity, long monitoring_interval, long monitoring_timeout);

    void stop_monitoring(org.omg.CORBA.FT.PullMonitorable target);
}
