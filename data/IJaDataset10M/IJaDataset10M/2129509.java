package com.sf.plctest.s7emulator.core;

/**
 * Kmmert sich um Baustein-Ende-Instruktionen. Eine Liste aller Operationen
 * findet sich unter {@link Instr#opcodeList}.
 * 
 * @author <a href="mailto:lst@tzi.de">Lars Stru</a>
 */
class InstrProgBlockEnd extends Instr {

    /** Siehe {@link Instr#execute}. */
    public boolean execute() {
        cpu.erstabfrage = false;
        if (!opcode.equals("BEB") || cpu.vke) {
            cpu.os = false;
            cpu.leaveBlock();
        } else cpu.vke = true;
        return true;
    }

    /** Siehe {@link Instr#parseParam}. */
    protected boolean parseParam(AWLParser parser) {
        return parser.parseParam(cpu, symtab, labelmap, Param.D_ANY, Param.T_ANY) == null;
    }
}
