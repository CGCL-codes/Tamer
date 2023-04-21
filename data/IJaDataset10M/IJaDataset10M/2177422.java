package com.jtse.unit_sim.processor.processor6502;

import com.jtse.unit_sim.processor.ProcessorException;

/**
 * @author timk
 */
final class OperationSBC extends OperationAccumulator {

    public OperationSBC(Processor6502 processor, Byte opcode) {
        super(processor, opcode);
    }

    public void execute() throws ProcessorException {
        Processor6502 processor = getProcessor();
        byte operandValue = getOperand().read();
        byte A = processor.getA();
        int borrow = processor.isC() ? 0 : 1;
        int newA = Util.uByteToInt(A) - Util.uByteToInt(operandValue) - borrow;
        boolean C = newA >= 0;
        processor.setC(C);
        processor.setV(C ^ ((((int) A & 0x7F) - ((int) operandValue & 0x7F) - borrow) >= 0));
        A = (byte) (newA & 0xFF);
        processor.setA(A);
        processor.setSZ(A);
    }
}
