package com.ibm.celldt.simulator.internal;

import java.io.IOException;
import org.eclipse.core.runtime.Assert;
import com.ibm.celldt.utils.terminal.AbstractTerminalProvider;

public class ProcessTerminalProvider extends AbstractTerminalProvider {

    SimulatorControl control;

    ProcessTerminalProvider(SimulatorControl control) {
        this.control = control;
    }

    public void writeDataToTerminal(byte[] bytes, int length) {
        Assert.isNotNull(control.processOutputStream);
        try {
            control.processOutputStream.write(bytes, 0, length);
            control.processOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
