package net.entropysoft.dashboard.plugin.samples.values;

import net.entropysoft.dashboard.plugin.variables.AbstractVariable;
import net.entropysoft.dashboard.plugin.variables.VariablePath;

public class MyVariable extends AbstractVariable {

    private VariablePath variablePath;

    public MyVariable(VariablePath variablePath) {
        this.variablePath = variablePath;
    }

    public VariablePath getPath() {
        return variablePath;
    }

    public String getName() {
        return variablePath.getLastPathElement();
    }
}
