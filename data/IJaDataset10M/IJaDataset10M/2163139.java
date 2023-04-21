package emulator.hardware.nmos6502.operands;

import emulator.EmulatorException;
import emulator.hardware.HwByte;
import emulator.hardware.HwWord;
import emulator.hardware.debug.BusWatchException;
import emulator.hardware.memory.UnmappedMemoryException;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Operand;

public abstract class ByteOperand implements Operand {

    private Cpu6502 cpu;

    private HwWord address;

    void setAddress(HwWord address) {
        this.address = address;
    }

    HwWord getAddress() {
        return address;
    }

    @Override
    public void init(Cpu6502 cpu) throws UnmappedMemoryException, BusWatchException {
        this.cpu = cpu;
    }

    @Override
    public HwByte getByte() throws UnmappedMemoryException, BusWatchException {
        return new HwByte(cpu.readByte((int) address.getNumber()));
    }

    @Override
    public HwWord getWord() throws OperandException {
        throw new OperandException("getWord() on byte operand called", this);
    }

    @Override
    public void setByte(HwByte data) throws BusWatchException, UnmappedMemoryException {
        cpu.writeByte((int) address.getNumber(), (int) data.getNumber());
    }

    @Override
    public void setWord(HwWord data) throws OperandException {
        throw new OperandException("setWord() on byte operand called", this);
    }

    @Override
    public String getString() throws EmulatorException {
        String result = "?";
        try {
            result = "$" + getByte();
        } catch (UnmappedMemoryException e) {
        }
        return result;
    }
}
