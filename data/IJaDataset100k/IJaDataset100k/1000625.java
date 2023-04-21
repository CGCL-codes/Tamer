package org.jikesrvm.compilers.opt;

import org.jikesrvm.compilers.opt.ir.OPT_BasicBlock;
import org.jikesrvm.compilers.opt.ir.OPT_BasicBlockEnumeration;
import org.jikesrvm.compilers.opt.ir.OPT_IR;
import org.jikesrvm.compilers.opt.ir.OPT_Instruction;

/**
 * Splits a large basic block into smaller ones with size <= MAX_NUM_INSTRUCTIONS.
 */
public final class OPT_SplitBasicBlock extends OPT_CompilerPhase {

    private static final int MAX_NUM_INSTRUCTIONS = 300;

    public String getName() {
        return "SplitBasicBlock";
    }

    public OPT_CompilerPhase newExecution(OPT_IR ir) {
        return this;
    }

    public void perform(OPT_IR ir) {
        for (OPT_BasicBlockEnumeration e = ir.getBasicBlocks(); e.hasMoreElements(); ) {
            OPT_BasicBlock bb = e.nextElement();
            if (!bb.isEmpty()) {
                while (bb != null) {
                    bb = splitEachBlock(bb, ir);
                }
            }
        }
    }

    OPT_BasicBlock splitEachBlock(OPT_BasicBlock bb, OPT_IR ir) {
        int instCount = MAX_NUM_INSTRUCTIONS;
        for (OPT_Instruction inst = bb.firstInstruction(); inst != bb.lastInstruction(); inst = inst.nextInstructionInCodeOrder()) {
            if ((--instCount) == 0) {
                if (inst.isBranch()) {
                    return null;
                }
                if (inst.isMove()) {
                    OPT_Instruction next = inst.nextInstructionInCodeOrder();
                    if (next != bb.lastInstruction() && next.isImplicitLoad()) {
                        inst = next;
                    }
                }
                return bb.splitNodeWithLinksAt(inst, ir);
            }
        }
        return null;
    }
}
