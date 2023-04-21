package net.entropysoft.jmx.plugin.jmx;

import net.entropysoft.dashboard.plugin.variables.AbstractVariable;
import net.entropysoft.dashboard.plugin.variables.VariablePath;

public class JmxAttribute extends AbstractVariable implements IJmxVariable {

    private String name;

    private VariablePath variablePath;

    public JmxAttribute(JmxBean jmxBean, String name) {
        this.variablePath = new VariablePath(jmxBean.getPath(), name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VariablePath getPath() {
        return variablePath;
    }
}
