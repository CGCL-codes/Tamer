package be.fedict.eid.applet.tests.javacc;

import java.util.HashMap;
import java.util.Map;

public class State {

    public State() {
        this.running = true;
        this.instructionPointer = 0;
        this.variables = new HashMap<String, Integer>();
    }

    private boolean running;

    private int instructionPointer;

    private Map<String, Integer> variables;

    public boolean isRunning() {
        return this.running;
    }

    public int getInstructionPointer() {
        return this.instructionPointer;
    }

    public void increaseInstructionPointer() {
        this.instructionPointer++;
    }

    public void stop() {
        this.running = false;
    }

    public void store(String variableName, int value) {
        this.variables.put(variableName, value);
    }

    public int getVariable(String variableName) {
        return this.variables.get(variableName);
    }
}
