package com.newisys.dv;

import com.newisys.eventsim.PulseEvent;
import com.newisys.verilog.EdgeSet;
import com.newisys.verilog.VerilogSimTime;

/**
 * Base class for events corresponding to a signal edge.
 * 
 * @author Trevor Robinson
 */
abstract class EdgeEvent extends PulseEvent implements EdgeListener {

    protected final EdgeMonitor monitor;

    protected final EdgeSet edges;

    protected final int bit;

    public EdgeEvent(String baseName, EdgeMonitor monitor, EdgeSet edges, int bit) {
        super(getEventName(baseName, monitor, edges, bit));
        this.monitor = monitor;
        this.edges = edges;
        this.bit = bit;
    }

    private static String getEventName(String baseName, EdgeMonitor monitor, EdgeSet edges, int bit) {
        return baseName + "{" + edges + " " + monitor.getSignalName() + ":" + bit + "}";
    }

    @Override
    protected void preWait() {
        monitor.addListener(this);
    }

    @Override
    protected void postWait() {
        monitor.removeListener(this);
    }

    public EdgeSet getEdges() {
        return edges;
    }

    public int getBit() {
        return bit;
    }

    public void notifyEdge(VerilogSimTime time, EdgeSet edge) {
        if (Debug.enabled) {
            Debug.out.println(this + ": " + edge + " @ " + time);
        }
    }
}
