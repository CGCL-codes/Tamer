package sf2.vm.test;

import sf2.vm.VMException;
import sf2.vm.VMImage;
import sf2.vm.VMM;
import sf2.vm.VMSnapShot;
import sf2.vm.VirtualMachine;
import sf2.vm.impl.libvirt.LibVirtImage;
import sf2.vm.impl.libvirt.LibVirtVMM;
import sf2.vm.impl.libvirt.LibVirtVirtualMachine;
import sf2.vm.impl.xen.XenImage;
import sf2.vm.impl.xen.XenVMM;
import sf2.vm.impl.xen.XenVirtualMachine;

public final class LibVirtTest {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: LibVirtTest name baseimage");
            System.exit(1);
        }
        try {
            VMM vmm = new LibVirtVMM();
            vmm.start();
            VMImage base = new LibVirtImage();
            base.load(args[1]);
            VirtualMachine vm = new LibVirtVirtualMachine();
            vm.start(vmm, args[0], base, null);
            VMSnapShot snap = vm.takeFullSnap();
            vm.halt();
            vm.restore(vmm, args[0], base, snap);
            vm.halt();
            vmm.shutdown();
        } catch (VMException e) {
            e.printStackTrace();
        }
    }
}
