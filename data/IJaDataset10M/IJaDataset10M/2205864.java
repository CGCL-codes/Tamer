package org.opennms.opennmsd;

public interface ProcessManagementListener {

    public String onInit();

    public String onStop();
}
