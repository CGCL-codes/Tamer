package org.apache.tools.ant;

import org.apache.tools.ant.Task;

public class DummyTaskWithoutDefaultConstructor extends Task {

    public DummyTaskWithoutDefaultConstructor(int dummy) {
    }

    public void execute() {
    }
}
