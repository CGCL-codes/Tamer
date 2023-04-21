package com.dustedpixels.jasmin.chips.z80.util;

import com.dustedpixels.jasmin.disassembler.Disassembler16x8Output;
import com.dustedpixels.jasmin.disassembler.implbase.Disassembler16x8ImplBase;
import com.dustedpixels.jasmin.memory.Memory16x8;

public class Z80Disassembler extends Disassembler16x8ImplBase {

    public int disassemble(Memory16x8 memory, short start, int length, Disassembler16x8Output output) {
        int disassembledBytes = 0;
        short address = start;
        while (length > 0) {
            byte opcode = memory.peek(address);
            if (opcode == 0xCB) {
            } else if (opcode == 0xDD) {
            } else if (opcode == 0xED) {
            } else if (opcode == 0xFD) {
            } else {
            }
        }
        return disassembledBytes;
    }
}
