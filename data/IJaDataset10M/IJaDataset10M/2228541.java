package edumips64.core.is;

import edumips64.core.*;
import edumips64.utils.*;

/**This is the base class of Load store instructions
 *
 * @author Trubia Massimo, Russo Daniele
 */
public abstract class LDSTInstructions extends Instruction {

    protected static CPU cpu = CPU.getInstance();

    static final int RT_FIELD = 0;

    static final int OFFSET_FIELD = 1;

    static final int BASE_FIELD = 2;

    static final int LMD_REGISTER = 3;

    static final int OFFSET_PLUS_BASE = 4;

    static final int RT_FIELD_INIT = 11;

    static final int OFFSET_FIELD_INIT = 16;

    static final int BASE_FIELD_INIT = 6;

    static final int RT_FIELD_LENGTH = 5;

    static final int OFFSET_FIELD_LENGTH = 16;

    static final int BASE_FIELD_LENGTH = 5;

    String OPCODE_VALUE = "";

    public LDSTInstructions() {
        this.syntax = "%R,%L(%R)";
        this.paramCount = 3;
    }

    public void setOpcode(String opcode) {
    }

    public void IF() {
        Dinero din = Dinero.getInstance();
        try {
            din.IF(Converter.binToHex(Converter.intToBin(64, cpu.getLastPC().getValue())));
        } catch (IrregularStringOfBitsException e) {
            e.printStackTrace();
        }
    }

    public abstract void ID() throws RAWException, IrregularWriteOperationException, IrregularStringOfBitsException, TwosComplementSumException;

    public abstract void EX() throws IrregularStringOfBitsException, IntegerOverflowException;

    public abstract void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException, AddressErrorException, IrregularWriteOperationException;

    public abstract void WB() throws IrregularStringOfBitsException;

    public void pack() throws IrregularStringOfBitsException {
        repr.setBits(OPCODE_VALUE, 0);
        repr.setBits(Converter.intToBin(BASE_FIELD_LENGTH, params.get(BASE_FIELD)), BASE_FIELD_INIT);
        repr.setBits(Converter.intToBin(RT_FIELD_LENGTH, params.get(RT_FIELD)), RT_FIELD_INIT);
        repr.setBits(Converter.intToBin(OFFSET_FIELD_LENGTH, params.get(OFFSET_FIELD)), OFFSET_FIELD_INIT);
    }
}
