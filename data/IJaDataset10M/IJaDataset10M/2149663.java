package emulator.hardware.nmos6502.operands;

import emulator.assembler.SymbolTable;
import emulator.hardware.HwByte;
import emulator.hardware.HwWord;
import emulator.hardware.debug.BusWatchException;
import emulator.hardware.memory.UnmappedMemoryException;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Operand;
import emulator.support.ImmediateAddressOperandEval;
import emulator.support.OperandEval;
import emulator.util.MemoryPtr;

public class ImmediateAddress implements Operand {

    protected HwWord addr;

    @Override
    public HwByte getByte() throws OperandException {
        throw new OperandException("getWord() on immediate address called", this);
    }

    @Override
    public HwWord getWord() {
        return addr;
    }

    @Override
    public void init(Cpu6502 cpu) throws UnmappedMemoryException, BusWatchException {
        addr = new HwWord(cpu.fetchProgramWord());
    }

    @Override
    public void setByte(HwByte data) throws OperandException {
        throw new OperandException("getByte() on immediate address called", this);
    }

    @Override
    public void setWord(HwWord data) throws OperandException {
        throw new OperandException("setWord() on immediate address called", this);
    }

    @Override
    public String getString() {
        return "$" + addr;
    }

    @Override
    public int getByteCount() {
        return 2;
    }

    @Override
    public String getString(MemoryPtr mem_ref, SymbolTable symbols) {
        return symbols.createJumpLabel(mem_ref.getWordOp());
    }

    @Override
    public int getExtraCycles() {
        return 1;
    }

    @Override
    public OperandEval getEvaluator(MemoryPtr mem_ref) {
        return new ImmediateAddressOperandEval(mem_ref);
    }
}
