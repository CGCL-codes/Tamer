package com.atolsystems.HwModeling;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sebastien.riou
 */
public class Net {

    String name;

    Set<InstancePort> connections;

    public Net(String name, Set<InstancePort> connections) {
        this.name = name;
        this.connections = connections;
    }

    public Net(String name) {
        this.name = name;
        this.connections = new HashSet<InstancePort>();
    }

    public void connect(InstancePort port) {
    }

    public void unconnect(InstancePort port) {
    }

    public Set<InstancePort> getConnections() {
        return connections;
    }

    public InstancePort getDriver() {
        VerilogInstance out = null;
        return null;
    }

    public VerilogInstance getDriverInstance() {
        InstancePort p = this.getDriver();
        if (null == p) return null;
        return p.getInstance();
    }

    public void setConnections(Set<InstancePort> connections) {
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Net{" + "name=" + name + '}';
    }
}
