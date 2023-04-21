package org.sodeja.silan.gui;

import org.sodeja.silan.VirtualMachine;
import org.sodeja.swing.context.DefaultApplicationContext;

public class SilanContext extends DefaultApplicationContext {

    private final VirtualMachine vm;

    public SilanContext() {
        this.vm = new VirtualMachine();
    }

    public VirtualMachine getVm() {
        return vm;
    }
}
