package svc.core.cpu.impl;

import svc.core.Bit;
import svc.core.BitSequence;
import svc.core.cpu.CPU;
import svc.core.memory.Address;
import svc.util.TextUtils;

/**
 * The conditional branch instruction.
 * 
 * @author Allen Charlton
 */
public class BR extends AbstractInstruction {

    public static final BitSequence OPCODE = new BitSequence("0000");

    public static final String[] ASSEMBLER_NAMES = { "BR", "BRn", "BRz", "BRp", "BRnz", "BRzp", "BRnp", "BRnzp" };

    public BitSequence getOpcode() {
        return OPCODE;
    }

    public void execute(CPU cpu) {
        checkSyntax();
        Bit n = instructionSequence.getBitAt(11);
        Bit z = instructionSequence.getBitAt(10);
        Bit p = instructionSequence.getBitAt(9);
        if ((n.isSet() && cpu.getFlag("SF").getBitAt(0).isSet()) || (z.isSet() && cpu.getFlag("ZF").getBitAt(0).isSet()) || (p.isSet() && cpu.getFlag("SF").getBitAt(0).isSet())) {
            cpu.setPCValue(new Address(cpu.getPCValue().toBitSequence().subSequence(15, 9).concat(instructionSequence.subSequence(8))));
        }
    }

    public int getPrivilegeLevel() {
        return 0;
    }

    public void parse(String assembler, CPU cpu) {
        String[] elements = TextUtils.tokenizeAssemblerStatement(assembler);
        if (elements != null && elements.length == 2 && hasOpcodeName(elements[0])) {
            String signStr = elements[0].substring(2);
            if (signStr.equals("")) {
                signStr = "nzp";
            }
            if (signStr.contains("n")) {
                instructionSequence.setBitAt(11);
            }
            if (signStr.contains("z")) {
                instructionSequence.setBitAt(10);
            }
            if (signStr.contains("p")) {
                instructionSequence.setBitAt(9);
            }
            try {
                int offset = Integer.decode(elements[1]).intValue();
                BitSequence offsetBS = BitSequence.convertFromDecimal(offset, false);
                if (offsetBS.getBitLength() > 9) {
                    reportSyntaxError("Too large immediate operand: " + offset);
                } else {
                    offsetBS.unsignedExtend(9);
                    setOpcode();
                    instructionSequence.replaceSequence(offsetBS, 8, 0);
                }
            } catch (NumberFormatException e) {
                reportSyntaxError("Bad page offset");
            }
        } else {
            reportSyntaxError("Invalid statement");
        }
    }

    public String[] getOpcodeNames() {
        return ASSEMBLER_NAMES;
    }
}
