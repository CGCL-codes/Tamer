package org.echarts.servlet.sip;

import org.echarts.TransitionMachine;
import org.echarts.monitor.MachineMonitorEvent;
import javax.servlet.sip.SipApplicationSession;

/**
   Abstract class that can be subclassed for the purposes of providing
   an interface to an ECharts for SIP Servlets machine from its
   external environment. An interface that subclasses this class has
   access to a machine's ECharts monitor by way of {@link
   JavaToEChartsMachine#putEvent(org.echarts.monitor.MachineMonitorEvent)
   putEvent()}. It also has the option of overriding this class's
   {@link JavaToEChartsMachine#init(TransitionMachine) init()} method
   to initialize the instance. This class also provides a factory
   method to create an interface instance and a convenience method to
   create a new feature box and provide it with a EChartsMachineToJava
   instance.
*/
public abstract class JavaToEChartsMachine extends JavaToSip {

    private static final String JAVA_TO_ECHARTS_MACHINE_ATTRIB = "JAVA_TO_ECHARTS_MACHINE";

    /**
	   Gets existing JavaToEChartsMachine instance for specified
	   appSession id, or creates a new one if no instance currently
	   exists. Instance class name is value of top-level machine name
	   with prefix 'JavaTo' e.g. if top-level machine name is
	   'org.echarts.servlet.sip.features.monitorControl.MonitorControlMachine'
	   then this method will return an instance of
	   'org.echarts.servlet.sip.features.monitorControl.JavaToMonitorControlMachine'.

	   @param sasId the SIP application session ID associated with the
	   JavaToEChartsMachine instance to be created
	   @return JavaToEChartsMachine instance, or null if app session no longer active
	 */
    @SuppressWarnings("unchecked")
    public static final <T extends JavaToEChartsMachine> T getInstance(final String uid) throws Exception {
        final SipApplicationSession appSession = EChartsSipServlet.getApplicationSession(uid);
        if (appSession != null) {
            return (T) getInstance(appSession);
        } else {
            return null;
        }
    }

    /**
	   Gets existing JavaToEChartsMachine instance for specified
	   appSession, or creates a new one if none currently
	   exists. Instance class name is value of top-level machine name
	   with prefix 'JavaTo' e.g. if top-level machine name is
	   'org.echarts.servlet.sip.features.monitorControl.MonitorControlMachine'
	   then this method will return an instance of
	   'org.echarts.servlet.sip.features.monitorControl.JavaToMonitorControlMachine'. If
	   appSession no longer active then returns null.

	   @param appSession the SIP application session associated with
	   the JavaToEChartsMachine instance to be created
	   @return JavaToEChartsMachine instance, or null if application session no longer active
	 */
    @SuppressWarnings("unchecked")
    public static final <T extends JavaToEChartsMachine> T getInstance(final SipApplicationSession appSession) throws Exception {
        boolean appSessionActive = true;
        T javaToEChartsMachine = null;
        try {
            javaToEChartsMachine = (T) appSession.getAttribute(JAVA_TO_ECHARTS_MACHINE_ATTRIB);
        } catch (IllegalStateException e) {
            appSessionActive = false;
        }
        if (appSessionActive && javaToEChartsMachine == null) {
            javaToEChartsMachine = (T) JavaToEChartsMachine.newInstance(appSession);
            if (javaToEChartsMachine != null) {
                try {
                    appSession.setAttribute(JAVA_TO_ECHARTS_MACHINE_ATTRIB, javaToEChartsMachine);
                } catch (IllegalStateException e) {
                    javaToEChartsMachine = null;
                }
            }
        }
        return javaToEChartsMachine;
    }

    /**
	   Create new JavaToEChartsMachine instance for specified
	   appSession. Creates an instance of class whose name is value of
	   top-level machine name with prefix 'JavaTo' e.g. if top-level
	   machine name is 'org.echarts.servlet.sip.machines.B2buaFSM'
	   then this method will create an instance of
	   'org.echarts.servlet.sip.machines.JavaToB2buaFSM'. Immediately
	   following the creation of this instance, its init() method is
	   called to give the programmer the opportunity to initialize its
	   fields.

	   @param appSession the SIP application session associated with
	   the JavaToEChartsMachine instance to be created
	   @return new JavaToEChartsMachine instance, or null if app
	   session no longer active
	 */
    @SuppressWarnings("unchecked")
    private static final <T extends JavaToEChartsMachine> T newInstance(final SipApplicationSession appSession) throws Exception {
        boolean appSessionActive = true;
        OpaqueFeatureBox box = null;
        T javaToEChartsMachine = null;
        try {
            box = (OpaqueFeatureBox) appSession.getAttribute(EChartsSipServlet.FEATURE_BOX);
        } catch (IllegalStateException e) {
            appSessionActive = false;
        }
        if (appSessionActive) {
            javaToEChartsMachine = (T) JavaToSip.newInstance(JavaToEChartsMachine.getJavaToSipClassName(box));
            javaToEChartsMachine.setBox(box.box);
            javaToEChartsMachine.setSipApplicationSessionId(appSession.getId());
            javaToEChartsMachine.init(box.box.getApplicationMachine());
        }
        return javaToEChartsMachine;
    }

    private static final String getJavaToSipClassName(final OpaqueFeatureBox box) {
        final String machineClassName = box.getMachineClassName();
        final String defaultJavaToSipClassName = machineClassName.substring(0, machineClassName.lastIndexOf('.') + 1) + "JavaTo" + machineClassName.substring(machineClassName.lastIndexOf('.') + 1);
        return defaultJavaToSipClassName;
    }

    private FeatureBox box;

    /**
	   Initializes this instance to use the {@link
	   #putEvent(MachineMonitorEvent) putEvent()} method if an
	   instance of this class is created via a direct call to a
	   constructor, rather than indeirectly via {@link
	   #getInstance(String) getInstance()}. There is no
	   need to use this method if this instance is created the usual
	   way via a call to {@link #getInstance(String)
	   getInstance()} since the initialization will occur
	   automatically.
	   @param box FeatureBox instance associated with this interface
	   instance.
	*/
    protected void setBox(final FeatureBox box) {
        this.box = box;
    }

    /**
	   Put an event to the machine's monitor.
	   @param event the event to put to the monitor
	 */
    protected void putEvent(final MachineMonitorEvent event) {
        if (box != null) box.getMonitor().putEvent(event);
    }

    /**
	   This method is called immediately after new
	   JavaToEChartsMachine instance is created. This method should be
	   overridden in order to initialize subclass fields.
	   @param machine application machine instance associated with
	   this instance's SIP application session
	 */
    protected void init(final TransitionMachine machine) throws Exception {
    }
}
