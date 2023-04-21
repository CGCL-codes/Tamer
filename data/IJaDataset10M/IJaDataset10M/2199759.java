package org.openremote.modeler.server.lutron.importmodel;

import java.util.HashSet;
import java.util.Set;

public class Room {

    private String name;

    private Set<Output> outputs;

    private Set<ControlStation> inputs;

    public Room(String name) {
        super();
        this.name = name;
        this.outputs = new HashSet<Output>();
        this.inputs = new HashSet<ControlStation>();
    }

    public String getName() {
        return name;
    }

    public Set<Output> getOutputs() {
        return outputs;
    }

    public Set<ControlStation> getInputs() {
        return inputs;
    }

    public void addOutput(Output output) {
        outputs.add(output);
    }

    public void addInput(ControlStation input) {
        inputs.add(input);
    }
}
