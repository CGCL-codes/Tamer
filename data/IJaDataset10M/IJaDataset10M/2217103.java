package net.sourceforge.jspcemulator.client.emulator.pci;

import net.sourceforge.jspcemulator.client.emulator.HardwareComponent;
import net.sourceforge.jspcemulator.client.emulator.motherboard.*;

/**
 * Emulation of an Intel i440FX PCI Host Bridge.
 * <p>
 * The host bridge is the PCI device that provides the processor with access to
 * the PCI bus and the rest if its devices.
 * @author Chris Dennis
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */
public class PCIHostBridge extends AbstractPCIDevice implements IOPortCapable {

    private PCIBus attachedBus;

    private int configRegister;

    /**
     * Constructs the (singleton) host bridge for a pci bus.
     */
    public PCIHostBridge() {
        ioportRegistered = false;
        assignDeviceFunctionNumber(0);
        putConfigWord(PCI_CONFIG_VENDOR_ID, (short) 0x8086);
        putConfigWord(PCI_CONFIG_DEVICE_ID, (short) 0x1237);
        putConfigByte(PCI_CONFIG_REVISION, (byte) 0x02);
        putConfigWord(PCI_CONFIG_CLASS_DEVICE, (short) 0x0600);
        putConfigByte(PCI_CONFIG_HEADER, (byte) 0x00);
    }

    public boolean autoAssignDeviceFunctionNumber() {
        return false;
    }

    public void deassignDeviceFunctionNumber() {
    }

    public IORegion[] getIORegions() {
        return null;
    }

    public IORegion getIORegion(int index) {
        return null;
    }

    public int[] ioPortsRequested() {
        return new int[] { 0xcf8, 0xcf9, 0xcfa, 0xcfb, 0xcfc, 0xcfd, 0xcfe, 0xcff };
    }

    public void ioPortWriteByte(int address, int data) {
        switch(address) {
            case 0xcfc:
            case 0xcfd:
            case 0xcfe:
            case 0xcff:
                if ((configRegister & (1 << 31)) != 0) attachedBus.writePCIDataByte(configRegister | (address & 0x3), (byte) data);
                break;
            default:
        }
    }

    public void ioPortWriteWord(int address, int data) {
        switch(address) {
            case 0xcfc:
            case 0xcfd:
            case 0xcfe:
            case 0xcff:
                if ((configRegister & (1 << 31)) != 0) attachedBus.writePCIDataWord(configRegister | (address & 0x3), (short) data);
                break;
            default:
        }
    }

    public void ioPortWriteLong(int address, int data) {
        switch(address) {
            case 0xcf8:
            case 0xcf9:
            case 0xcfa:
            case 0xcfb:
                configRegister = data;
                break;
            case 0xcfc:
            case 0xcfd:
            case 0xcfe:
            case 0xcff:
                if ((configRegister & (1 << 31)) != 0) attachedBus.writePCIDataLong(configRegister | (address & 0x3), data);
                break;
            default:
        }
    }

    public int ioPortReadByte(int address) {
        switch(address) {
            case 0xcfc:
            case 0xcfd:
            case 0xcfe:
            case 0xcff:
                if ((configRegister & (1 << 31)) == 0) return 0xff; else return 0xff & attachedBus.readPCIDataByte(configRegister | (address & 0x3));
            default:
                return 0xff;
        }
    }

    public int ioPortReadWord(int address) {
        switch(address) {
            case 0xcfc:
            case 0xcfd:
            case 0xcfe:
            case 0xcff:
                if ((configRegister & (1 << 31)) == 0) return 0xffff; else return 0xffff & attachedBus.readPCIDataWord(configRegister | (address & 0x3));
            default:
                return 0xffff;
        }
    }

    public int ioPortReadLong(int address) {
        switch(address) {
            case 0xcf8:
            case 0xcf9:
            case 0xcfa:
            case 0xcfb:
                return configRegister;
            case 0xcfc:
            case 0xcfd:
            case 0xcfe:
            case 0xcff:
                if ((configRegister & (1 << 31)) == 0) return 0xffffffff; else return attachedBus.readPCIDataLong(configRegister | (address & 0x3));
            default:
                return 0xffffffff;
        }
    }

    private boolean ioportRegistered;

    private boolean pciRegistered;

    public boolean initialised() {
        return ioportRegistered && pciRegistered;
    }

    public void reset() {
        attachedBus = null;
        pciRegistered = false;
        ioportRegistered = false;
        assignDeviceFunctionNumber(0);
        putConfigWord(PCI_CONFIG_VENDOR_ID, (short) 0x8086);
        putConfigWord(PCI_CONFIG_DEVICE_ID, (short) 0x1237);
        putConfigByte(PCI_CONFIG_REVISION, (byte) 0x02);
        putConfigWord(PCI_CONFIG_CLASS_DEVICE, (short) 0x0600);
        putConfigByte(PCI_CONFIG_HEADER, (byte) 0x00);
    }

    public void acceptComponent(HardwareComponent component) {
        if ((component instanceof PCIBus) && component.initialised() && !pciRegistered) {
            attachedBus = (PCIBus) component;
            pciRegistered = attachedBus.registerDevice(this);
        }
        if ((component instanceof IOPortHandler) && component.initialised()) {
            ((IOPortHandler) component).registerIOPortCapable(this);
            ioportRegistered = true;
        }
    }

    public boolean updated() {
        return ioportRegistered && pciRegistered;
    }

    public void updateComponent(HardwareComponent component) {
        if ((component instanceof PCIBus) && component.updated() && !pciRegistered) {
            pciRegistered = attachedBus.registerDevice(this);
        }
        if ((component instanceof IOPortHandler) && component.updated()) {
            ((IOPortHandler) component).registerIOPortCapable(this);
            ioportRegistered = true;
        }
    }

    public String toString() {
        return "Intel i440FX PCI-Host Bridge";
    }
}
