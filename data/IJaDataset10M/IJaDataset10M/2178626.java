package fi.hiit.framework.network.monitor;

import java.util.List;
import fi.hiit.framework.network.monitor.NetMonitor.HipNetInterface;

public class NetMonitorEvent {

    public List<HipNetInterface> getAddrsList() {
        return __list;
    }

    public NetMonitorEvent(List<HipNetInterface> list) {
        __list = list;
    }

    private List<HipNetInterface> __list;
}
