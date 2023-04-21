package com.c2b2.open286.container.portlet;

import com.c2b2.open286.container.runtime.Open286MBean;

public interface PortletRuntimeMBean extends Open286MBean {

    public long getInitialisationTime();

    public long getInvocationCount();

    public long getProcessActionCount();

    public long getProcessActionMaxTime();

    public long getProcessActionMinTime();

    public long getProcessActionAverageTime();

    public long getRenderCount();

    public long getRenderMaxTime();

    public long getRenderMinTime();

    public long getRenderAverageTime();

    public long getProcessEventCount();

    public long getProcessEventMaxTime();

    public long getProcessEventMinTime();

    public long getProcessEventAverageTime();

    public long getServeResourceCount();

    public long getServeResourceMaxTime();

    public long getServeResourceMinTime();

    public long getServeResourceAverageTime();

    public String getLogLevel();

    public void setLogLevel(String level) throws IllegalArgumentException;

    public String getName();

    public String getContextName();

    public String getStatus();

    public long getLastFailureTime();

    public long getNextInServiceDate();

    public String getLastFailureMessage();

    public String getLastFailureStack();
}
