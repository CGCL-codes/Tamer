package org.highcon.pddpd.plugins.processors;

import java.util.Hashtable;
import org.highcon.pddpd.lib.ConfigurationException;
import org.highcon.pddpd.lib.ProcessorSkeleton;

public class StringProcessor extends ProcessorSkeleton {

    protected void cleanup() {
    }

    protected Object process(Object in) {
        if (in == null) {
            return null;
        }
        return in.toString();
    }

    protected void initialize() {
    }

    public Hashtable getConfig() {
        return new Hashtable();
    }

    public Hashtable getSchema() {
        return new Hashtable();
    }

    public Object getValue(String name) {
        return null;
    }

    public void setConfig(Hashtable config) throws ConfigurationException {
    }

    public String getConfigFilename() {
        return null;
    }
}
