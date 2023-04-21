package com.att.echarts;

import java.lang.reflect.Constructor;

/**
   Subclass of MachineConstructor for variable submachines
   i.e. submachines declared with 'variable(..., ...)' keyword.
 */
public final class VariableMachineConstructor extends MachineConstructor {

    public VariableMachineConstructor(final MachineContextConstructor contextConstructor) {
        super(contextConstructor);
    }

    /**
	   Since variable machine identity is determined at run-time
	   (rather than at compile-time like other machines), we use
	   reflection to create a machine instance.
	 */
    public final Machine newInstance(final MachineContext machineContext) throws Exception {
        final Class machineContextClass = machineContext.getClass();
        final String machineContextClassName = machineContext.getClass().getName();
        final String machineClassName = machineContextClassName.substring(0, machineContextClassName.lastIndexOf("$"));
        final Class machineClass = Class.forName(machineClassName);
        final Constructor machineConstructor = machineClass.getConstructor(new Class[] { machineContextClass });
        machineConstructor.setAccessible(true);
        return (Machine) machineConstructor.newInstance(new Object[] { machineContext });
    }
}
