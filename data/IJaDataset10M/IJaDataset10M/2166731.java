package org.tzi.use.gen.assl.statics;

import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.uml.ocl.type.CollectionType;

/**
 * @see org.tzi.use.gen.assl.statics
 * @author  Joern Bohling
 */
public class GInstrTry_Seq extends GValueInstruction {

    private GValueInstruction fSequenceInstr;

    public GInstrTry_Seq(GValueInstruction instr) {
        fSequenceInstr = instr;
    }

    public GValueInstruction sequenceInstr() {
        return fSequenceInstr;
    }

    public Type type() {
        return ((CollectionType) fSequenceInstr.type()).elemType();
    }

    public String toString() {
        return "Try(" + fSequenceInstr + ")";
    }
}
