package org.sonify.vm.event;

import org.sonify.vm.AbstractVirtualMachine;
import org.sonify.vm.ExceptionManager;
import org.sonify.vm.VirtualMachineEvent;
import org.sonify.vm.execution.ExecutionStep;

/**
 *
 * @author Andreas Stefik
 */
public class VirtualMachineErrorState extends VirtualMachineEvent {

    private ExceptionManager errorManager;

    public VirtualMachineErrorState(ExecutionStep step, AbstractVirtualMachine vm, boolean executeEvent, ExceptionManager manager) {
        super(step, vm, executeEvent);
        errorManager = manager;
    }

    @Override
    public boolean isErrorStateEvent() {
        return false;
    }

    public ExceptionManager getErrorManager() {
        return errorManager;
    }
}
