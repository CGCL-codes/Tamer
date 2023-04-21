package org.jspar.partition;

import java.util.ArrayList;
import java.util.List;
import org.jspar.model.Module;

public class Cluster {

    private List<Module> modules;

    private int connections;

    public Cluster(Module module, int connections) {
        this.connections = connections;
        modules = new ArrayList<Module>();
        modules.add(module);
    }

    public int size() {
        return modules.size();
    }

    public int connections() {
        return connections;
    }

    public void mergeWith(Cluster other, int c) {
        connections = c;
        modules.addAll(other.modules);
    }

    public List<Module> modules() {
        return modules;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('{');
        for (int i = 0; i < modules.size(); ++i) {
            if (i != 0) buffer.append(' ');
            Module module = (Module) modules.get(i);
            buffer.append(module.name());
        }
        buffer.append('}');
        return buffer.toString();
    }
}
