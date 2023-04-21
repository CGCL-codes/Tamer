package sf2.vm.impl.emu;

import sf2.vm.VMException;
import sf2.vm.VMImage;
import sf2.vm.VMM;
import sf2.vm.VMNetwork;
import sf2.vm.VMResource;
import sf2.vm.VMSnapShot;
import sf2.vm.VirtualMachine;
import sf2.vm.VirtualMachineFactory;

public class EmuVirtualMachineFactory extends VirtualMachineFactory {

    public VMM createVMM() throws VMException {
        return new EmuVMM();
    }

    public VirtualMachine createVirtualMachine() throws VMException {
        return new EmuVirtualMachine();
    }

    public VMImage createVMImage() throws VMException {
        return new EmuVMImage();
    }

    public VMSnapShot createVMSnapShot() throws VMException {
        return new EmuVMSnapShot();
    }

    public VMResource createVMResource() throws VMException {
        return new EmuVMResouce();
    }

    public VMNetwork createVMNetwork() throws VMException {
        return new EmuVMNetwork();
    }
}
