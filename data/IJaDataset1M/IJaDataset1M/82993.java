package org.opennms.netmgt.vmmgr;

import java.util.Map;

public interface DaemonManager {

    public void start();

    public void stop();

    public void pause();

    public void resume();

    public Map<String, String> status();
}
