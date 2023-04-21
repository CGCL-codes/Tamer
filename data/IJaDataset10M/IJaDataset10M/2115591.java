package net.wimpi.modbus.procimg;

import java.util.Vector;

/**
 * Class implementing a simple process image
 * to be able to run unit tests or handle
 * simple cases.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class SimpleProcessImage implements ProcessImageImplementation {

    protected Vector m_DigitalInputs;

    protected Vector m_DigitalOutputs;

    protected Vector m_InputRegisters;

    protected Vector m_Registers;

    protected boolean m_Locked = false;

    /**
   * Constructs a new <tt>SimpleProcessImage</tt> instance.
   */
    public SimpleProcessImage() {
        m_DigitalInputs = new Vector();
        m_DigitalOutputs = new Vector();
        m_InputRegisters = new Vector();
        m_Registers = new Vector();
    }

    public boolean isLocked() {
        return m_Locked;
    }

    public void setLocked(boolean locked) {
        m_Locked = locked;
    }

    public void addDigitalIn(DigitalIn di) {
        if (!isLocked()) {
            m_DigitalInputs.addElement(di);
        }
    }

    public void removeDigitalIn(DigitalIn di) {
        if (!isLocked()) {
            m_DigitalInputs.removeElement(di);
        }
    }

    public void setDigitalIn(int ref, DigitalIn di) throws IllegalAddressException {
        if (!isLocked()) {
            try {
                m_DigitalInputs.setElementAt(di, ref);
            } catch (IndexOutOfBoundsException ex) {
                throw new IllegalAddressException();
            }
        }
    }

    public DigitalIn getDigitalIn(int ref) throws IllegalAddressException {
        try {
            return (DigitalIn) m_DigitalInputs.elementAt(ref);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalAddressException();
        }
    }

    public int getDigitalInCount() {
        return m_DigitalInputs.size();
    }

    public DigitalIn[] getDigitalInRange(int ref, int count) {
        if (ref < 0 || ref + count > m_DigitalInputs.size()) {
            throw new IllegalAddressException();
        } else {
            DigitalIn[] dins = new DigitalIn[count];
            for (int i = 0; i < dins.length; i++) {
                dins[i] = getDigitalIn(ref + i);
            }
            return dins;
        }
    }

    public void addDigitalOut(DigitalOut _do) {
        if (!isLocked()) {
            m_DigitalOutputs.addElement(_do);
        }
    }

    public void removeDigitalOut(DigitalOut _do) {
        if (!isLocked()) {
            m_DigitalOutputs.removeElement(_do);
        }
    }

    public void setDigitalOut(int ref, DigitalOut _do) throws IllegalAddressException {
        if (!isLocked()) {
            try {
                m_DigitalOutputs.setElementAt(_do, ref);
            } catch (IndexOutOfBoundsException ex) {
                throw new IllegalAddressException();
            }
        }
    }

    public DigitalOut getDigitalOut(int ref) throws IllegalAddressException {
        try {
            return (DigitalOut) m_DigitalOutputs.elementAt(ref);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalAddressException();
        }
    }

    public int getDigitalOutCount() {
        return m_DigitalOutputs.size();
    }

    public DigitalOut[] getDigitalOutRange(int ref, int count) {
        if (ref < 0 || ref + count > m_DigitalOutputs.size()) {
            throw new IllegalAddressException();
        } else {
            DigitalOut[] douts = new DigitalOut[count];
            for (int i = 0; i < douts.length; i++) {
                douts[i] = getDigitalOut(ref + i);
            }
            return douts;
        }
    }

    public void addInputRegister(InputRegister reg) {
        if (!isLocked()) {
            m_InputRegisters.addElement(reg);
        }
    }

    public void removeInputRegister(InputRegister reg) {
        if (!isLocked()) {
            m_InputRegisters.removeElement(reg);
        }
    }

    public void setInputRegister(int ref, InputRegister reg) throws IllegalAddressException {
        if (!isLocked()) {
            try {
                m_InputRegisters.setElementAt(reg, ref);
            } catch (IndexOutOfBoundsException ex) {
                throw new IllegalAddressException();
            }
        }
    }

    public InputRegister getInputRegister(int ref) throws IllegalAddressException {
        try {
            return (InputRegister) m_InputRegisters.elementAt(ref);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalAddressException();
        }
    }

    public int getInputRegisterCount() {
        return m_InputRegisters.size();
    }

    public InputRegister[] getInputRegisterRange(int ref, int count) {
        if (ref < 0 || ref + count > m_InputRegisters.size()) {
            throw new IllegalAddressException();
        } else {
            InputRegister[] iregs = new InputRegister[count];
            for (int i = 0; i < iregs.length; i++) {
                iregs[i] = getInputRegister(ref + i);
            }
            return iregs;
        }
    }

    public void addRegister(Register reg) {
        if (!isLocked()) {
            m_Registers.addElement(reg);
        }
    }

    public void removeRegister(Register reg) {
        if (!isLocked()) {
            m_Registers.removeElement(reg);
        }
    }

    public void setRegister(int ref, Register reg) throws IllegalAddressException {
        if (!isLocked()) {
            try {
                m_Registers.setElementAt(reg, ref);
            } catch (IndexOutOfBoundsException ex) {
                throw new IllegalAddressException();
            }
        }
    }

    public Register getRegister(int ref) throws IllegalAddressException {
        try {
            return (Register) m_Registers.elementAt(ref);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalAddressException();
        }
    }

    public int getRegisterCount() {
        return m_Registers.size();
    }

    public Register[] getRegisterRange(int ref, int count) {
        if (ref < 0 || ref + count > m_Registers.size()) {
            throw new IllegalAddressException();
        } else {
            Register[] iregs = new Register[count];
            for (int i = 0; i < iregs.length; i++) {
                iregs[i] = getRegister(ref + i);
            }
            return iregs;
        }
    }
}
