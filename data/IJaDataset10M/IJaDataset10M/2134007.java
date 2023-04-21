package com.amd.javalabs.tools.disasm;

import com.amd.javalabs.tools.disasm.instructions.X64InstructionSet;

/**
 * An X64 Disassembler wrapped around the X64 instruction set
 */
public class X64Disassembler extends Disassembler {

    public X64Disassembler() {
        super(X64InstructionSet.getInstance());
    }
}
