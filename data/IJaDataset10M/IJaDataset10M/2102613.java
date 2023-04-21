package org.jpc.emulator.motherboard;

import org.jpc.emulator.*;
import org.jpc.emulator.processor.Processor;
import java.io.*;
import org.javanile.wrapper.java.util.logging.*;

/**
 * Emulation of an 8259 Programmable Interrupt Controller.
 * @see <a href="http://www.ee.hacettepe.edu.tr/~alkar/ELE414/8259.pdf">82C59A - Datasheet</a>
 * @author Chris Dennis
 */
public class InterruptController extends AbstractHardwareComponent implements IOPortCapable {

    private static final Logger LOGGING = Logger.getLogger(InterruptController.class.getName());

    private InterruptControllerElement master;

    private InterruptControllerElement slave;

    private Processor connectedCPU;

    /**
     * Constructs a <code>InterruptController</code> which will attach itself to
     * a <code>Processor</code> instance during the configuration stage.
     */
    public InterruptController() {
        ioportRegistered = false;
        master = new InterruptControllerElement(true);
        slave = new InterruptControllerElement(false);
    }

    public void saveState(DataOutput output) throws IOException {
        master.saveState(output);
        slave.saveState(output);
    }

    public void loadState(DataInput input) throws IOException {
        ioportRegistered = false;
        master.loadState(input);
        slave.loadState(input);
    }

    private void updateIRQ() {
        int slaveIRQ, masterIRQ;
        slaveIRQ = slave.getIRQ();
        if (slaveIRQ >= 0) {
            master.setIRQ(2, 1);
            master.setIRQ(2, 0);
        }
        masterIRQ = master.getIRQ();
        if (masterIRQ >= 0) {
            connectedCPU.raiseInterrupt();
        }
    }

    /**
     * Set interrupt number <code>irqNumber</code> to level <code>level</code>.
     * @param irqNumber interrupt channel number.
     * @param level requested level.
     */
    public void setIRQ(int irqNumber, int level) {
        switch(irqNumber >>> 3) {
            case 0:
                master.setIRQ(irqNumber & 7, level);
                this.updateIRQ();
                break;
            case 1:
                slave.setIRQ(irqNumber & 7, level);
                this.updateIRQ();
                break;
            default:
        }
    }

    /**
     * Return the highest priority interrupt request currently awaiting service
     * on this interrupt controller.  This is called by the processor emulation
     * once its <code>raiseInterrupt</code> method has been called to get the
     * correct interrupt vector value.
     * @return highest priority interrupt vector.
     */
    public int cpuGetInterrupt() {
        int masterIRQ, slaveIRQ;
        masterIRQ = master.getIRQ();
        if (masterIRQ >= 0) {
            master.intAck(masterIRQ);
            if (masterIRQ == 2) {
                slaveIRQ = slave.getIRQ();
                if (slaveIRQ >= 0) {
                    slave.intAck(slaveIRQ);
                } else {
                    slaveIRQ = 7;
                }
                this.updateIRQ();
                return slave.irqBase + slaveIRQ;
            } else {
                this.updateIRQ();
                return master.irqBase + masterIRQ;
            }
        } else {
            masterIRQ = 7;
            this.updateIRQ();
            return master.irqBase + masterIRQ;
        }
    }

    private class InterruptControllerElement implements Hibernatable {

        private int lastInterruptRequestRegister;

        private int interruptRequestRegister;

        private int interruptMaskRegister;

        private int interruptServiceRegister;

        private int priorityAdd;

        private int irqBase;

        private boolean readRegisterSelect;

        private boolean poll;

        private boolean specialMask;

        private int initState;

        private boolean fourByteInit;

        private int elcr;

        private int elcrMask;

        private boolean specialFullyNestedMode;

        private boolean autoEOI;

        private boolean rotateOnAutoEOI;

        private int[] ioPorts;

        public InterruptControllerElement(boolean master) {
            if (master == true) {
                ioPorts = new int[] { 0x20, 0x21, 0x4d0 };
                elcrMask = 0xf8;
            } else {
                ioPorts = new int[] { 0xa0, 0xa1, 0x4d1 };
                elcrMask = 0xde;
            }
        }

        public void saveState(DataOutput output) throws IOException {
            output.writeInt(lastInterruptRequestRegister);
            output.writeInt(interruptRequestRegister);
            output.writeInt(interruptMaskRegister);
            output.writeInt(interruptServiceRegister);
            output.writeInt(priorityAdd);
            output.writeInt(irqBase);
            output.writeBoolean(readRegisterSelect);
            output.writeBoolean(poll);
            output.writeBoolean(specialMask);
            output.writeInt(initState);
            output.writeBoolean(autoEOI);
            output.writeBoolean(rotateOnAutoEOI);
            output.writeBoolean(specialFullyNestedMode);
            output.writeBoolean(fourByteInit);
            output.writeInt(elcr);
            output.writeInt(elcrMask);
            output.writeInt(ioPorts.length);
            for (int i = 0; i < ioPorts.length; i++) {
                int port = ioPorts[i];
                output.writeInt(port);
            }
        }

        public void loadState(DataInput input) throws IOException {
            lastInterruptRequestRegister = input.readInt();
            interruptRequestRegister = input.readInt();
            interruptMaskRegister = input.readInt();
            interruptServiceRegister = input.readInt();
            priorityAdd = input.readInt();
            irqBase = input.readInt();
            readRegisterSelect = input.readBoolean();
            poll = input.readBoolean();
            specialMask = input.readBoolean();
            initState = input.readInt();
            autoEOI = input.readBoolean();
            rotateOnAutoEOI = input.readBoolean();
            specialFullyNestedMode = input.readBoolean();
            fourByteInit = input.readBoolean();
            elcr = input.readInt();
            elcrMask = input.readInt();
            int len = input.readInt();
            ioPorts = new int[len];
            for (int i = 0; i < len; i++) ioPorts[i] = input.readInt();
        }

        public int[] ioPortsRequested() {
            return ioPorts;
        }

        public int ioPortRead(int address) {
            if (poll) {
                poll = false;
                return this.pollRead(address);
            }
            if ((address & 1) == 0) {
                if (readRegisterSelect) {
                    return interruptServiceRegister;
                }
                return interruptRequestRegister;
            }
            return interruptMaskRegister;
        }

        public int elcrRead() {
            return elcr;
        }

        public boolean ioPortWrite(int address, byte data) {
            int priority, command, irq;
            address &= 1;
            if (address == 0) {
                if (0 != (data & 0x10)) {
                    this.reset();
                    connectedCPU.clearInterrupt();
                    initState = 1;
                    fourByteInit = ((data & 1) != 0);
                    if (0 != (data & 0x02)) LOGGING.log(Level.INFO, "single mode not supported");
                    if (0 != (data & 0x08)) LOGGING.log(Level.INFO, "level sensitive irq not supported");
                } else if (0 != (data & 0x08)) {
                    if (0 != (data & 0x04)) poll = true;
                    if (0 != (data & 0x02)) readRegisterSelect = ((data & 0x01) != 0);
                    if (0 != (data & 0x40)) specialMask = (((data >>> 5) & 1) != 0);
                } else {
                    command = data >>> 5;
                    switch(command) {
                        case 0:
                        case 4:
                            rotateOnAutoEOI = ((command >>> 2) != 0);
                            break;
                        case 1:
                        case 5:
                            priority = this.getPriority(interruptServiceRegister);
                            if (priority != 8) {
                                irq = (priority + priorityAdd) & 7;
                                interruptServiceRegister &= ~(1 << irq);
                                if (command == 5) priorityAdd = (irq + 1) & 7;
                                return true;
                            }
                            break;
                        case 3:
                            irq = data & 7;
                            interruptServiceRegister &= ~(1 << irq);
                            return true;
                        case 6:
                            priorityAdd = (data + 1) & 7;
                            return true;
                        case 7:
                            irq = data & 7;
                            interruptServiceRegister &= ~(1 << irq);
                            priorityAdd = (irq + 1) & 7;
                            return true;
                        default:
                            break;
                    }
                }
            } else {
                switch(initState) {
                    case 0:
                        interruptMaskRegister = data;
                        return true;
                    case 1:
                        irqBase = data & 0xf8;
                        initState = 2;
                        break;
                    case 2:
                        if (fourByteInit) {
                            initState = 3;
                        } else {
                            initState = 0;
                        }
                        break;
                    case 3:
                        specialFullyNestedMode = (((data >>> 4) & 1) != 0);
                        autoEOI = (((data >>> 1) & 1) != 0);
                        initState = 0;
                        break;
                }
            }
            return false;
        }

        public void elcrWrite(int data) {
            elcr = data & elcrMask;
        }

        private int pollRead(int address) {
            int ret = this.getIRQ();
            if (ret < 0) {
                InterruptController.this.updateIRQ();
                return 0x07;
            }
            if (0 != (address >>> 7)) {
                InterruptController.this.masterPollCode();
            }
            interruptRequestRegister &= ~(1 << ret);
            interruptServiceRegister &= ~(1 << ret);
            if (0 != (address >>> 7) || ret != 2) InterruptController.this.updateIRQ();
            return ret;
        }

        public void setIRQ(int irqNumber, int level) {
            int mask;
            mask = (1 << irqNumber);
            if (0 != (elcr & mask)) {
                if (0 != level) {
                    interruptRequestRegister |= mask;
                    lastInterruptRequestRegister |= mask;
                } else {
                    interruptRequestRegister &= ~mask;
                    lastInterruptRequestRegister &= ~mask;
                }
            } else {
                if (0 != level) {
                    if ((lastInterruptRequestRegister & mask) == 0) {
                        interruptRequestRegister |= mask;
                    }
                    lastInterruptRequestRegister |= mask;
                } else {
                    lastInterruptRequestRegister &= ~mask;
                }
            }
        }

        private int getPriority(int mask) {
            if ((0xff & mask) == 0) {
                return 8;
            }
            int priority = 0;
            while ((mask & (1 << ((priority + priorityAdd) & 7))) == 0) {
                priority++;
            }
            return priority;
        }

        public int getIRQ() {
            int mask, currentPriority, priority;
            mask = interruptRequestRegister & ~interruptMaskRegister;
            priority = this.getPriority(mask);
            if (priority == 8) {
                return -1;
            }
            mask = interruptServiceRegister;
            if (specialFullyNestedMode && this.isMaster()) {
                mask &= ~(1 << 2);
            }
            currentPriority = this.getPriority(mask);
            if (priority < currentPriority) {
                return (priority + priorityAdd) & 7;
            } else {
                return -1;
            }
        }

        private void intAck(int irqNumber) {
            if (autoEOI) {
                if (rotateOnAutoEOI) priorityAdd = (irqNumber + 1) & 7;
            } else {
                interruptServiceRegister |= (1 << irqNumber);
            }
            if (0 == (elcr & (1 << irqNumber))) interruptRequestRegister &= ~(1 << irqNumber);
        }

        private boolean isMaster() {
            return InterruptController.this.master == this;
        }

        private void reset() {
            lastInterruptRequestRegister = 0x0;
            interruptRequestRegister = 0x0;
            interruptMaskRegister = 0x0;
            interruptServiceRegister = 0x0;
            priorityAdd = 0;
            irqBase = 0x0;
            readRegisterSelect = false;
            poll = false;
            specialMask = false;
            autoEOI = false;
            rotateOnAutoEOI = false;
            specialFullyNestedMode = false;
            initState = 0;
            fourByteInit = false;
            elcr = 0x0;
        }

        public String toString() {
            if (isMaster()) {
                return (InterruptController.this).toString() + ": [Master Element]";
            } else {
                return (InterruptController.this).toString() + ": [Slave  Element]";
            }
        }
    }

    public int[] ioPortsRequested() {
        int[] masterIOPorts = master.ioPortsRequested();
        int[] slaveIOPorts = slave.ioPortsRequested();
        int[] temp = new int[masterIOPorts.length + slaveIOPorts.length];
        System.arraycopy(masterIOPorts, 0, temp, 0, masterIOPorts.length);
        System.arraycopy(slaveIOPorts, 0, temp, masterIOPorts.length, slaveIOPorts.length);
        return temp;
    }

    public int ioPortReadByte(int address) {
        switch(address) {
            case 0x20:
            case 0x21:
                return 0xff & master.ioPortRead(address);
            case 0xa0:
            case 0xa1:
                return 0xff & slave.ioPortRead(address);
            case 0x4d0:
                return 0xff & master.elcrRead();
            case 0x4d1:
                return 0xff & slave.elcrRead();
            default:
        }
        return 0;
    }

    public int ioPortReadWord(int address) {
        return (0xff & ioPortReadByte(address)) | (0xff00 & (ioPortReadByte(address + 1) << 8));
    }

    public int ioPortReadLong(int address) {
        return (0xffff & ioPortReadWord(address)) | (0xffff0000 & (ioPortReadWord(address + 2) << 16));
    }

    public void ioPortWriteByte(int address, int data) {
        switch(address) {
            case 0x20:
            case 0x21:
                if (master.ioPortWrite(address, (byte) data)) this.updateIRQ();
                break;
            case 0xa0:
            case 0xa1:
                if (slave.ioPortWrite(address, (byte) data)) this.updateIRQ();
                break;
            case 0x4d0:
                master.elcrWrite(data);
                break;
            case 0x4d1:
                slave.elcrWrite(data);
                break;
            default:
        }
    }

    public void ioPortWriteWord(int address, int data) {
        this.ioPortWriteByte(address, data);
        this.ioPortWriteByte(address + 1, data >>> 8);
    }

    public void ioPortWriteLong(int address, int data) {
        this.ioPortWriteWord(address, data);
        this.ioPortWriteWord(address + 2, data >>> 16);
    }

    private void masterPollCode() {
        master.interruptServiceRegister &= ~(1 << 2);
        master.interruptRequestRegister &= ~(1 << 2);
    }

    private boolean ioportRegistered;

    public void reset() {
        master.reset();
        slave.reset();
        ioportRegistered = false;
        connectedCPU = null;
    }

    public boolean initialised() {
        return ((connectedCPU != null) && ioportRegistered);
    }

    public boolean updated() {
        return ioportRegistered;
    }

    public void updateComponent(HardwareComponent component) {
        if (component instanceof IOPortHandler) {
            ((IOPortHandler) component).registerIOPortCapable(this);
            ioportRegistered = true;
        }
    }

    public void acceptComponent(HardwareComponent component) {
        if (component instanceof Processor) connectedCPU = (Processor) component;
        if ((component instanceof IOPortHandler) && component.initialised()) {
            ((IOPortHandler) component).registerIOPortCapable(this);
            ioportRegistered = true;
        }
    }

    public String toString() {
        return "Intel i8259 Programmable Interrupt Controller";
    }
}
