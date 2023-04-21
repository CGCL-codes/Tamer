package org.jikesrvm.compilers.opt.lir2mir;

import org.jikesrvm.compilers.opt.driver.CompilerPhase;
import org.jikesrvm.compilers.opt.ir.BasicBlock;
import org.jikesrvm.compilers.opt.ir.BasicBlockEnumeration;
import org.jikesrvm.compilers.opt.ir.IR;
import org.jikesrvm.compilers.opt.ir.Instruction;

/**
 * Splits a large basic block into smaller ones with size <=
 * OptOptions.L2M_MAX_BLOCK_SIZE
 */
public final class SplitBasicBlock extends CompilerPhase {

    public String getName() {
        return "SplitBasicBlock";
    }

    public CompilerPhase newExecution(IR ir) {
        return this;
    }

    public void perform(IR ir) {
        for (BasicBlockEnumeration e = ir.getBasicBlocks(); e.hasMoreElements(); ) {
            BasicBlock bb = e.nextElement();
            if (!bb.isEmpty()) {
                while (bb != null) {
                    bb = splitEachBlock(bb, ir);
                }
            }
        }
    }

    /**
   * Splits basic block
   *
   * @return null if no splitting is done, returns the second block if splitting is done.
   */
    BasicBlock splitEachBlock(BasicBlock bb, IR ir) {
        int instCount = ir.options.L2M_MAX_BLOCK_SIZE;
        for (Instruction inst = bb.firstInstruction(); inst != bb.lastInstruction(); inst = inst.nextInstructionInCodeOrder()) {
            if ((--instCount) <= 0) {
                if (inst.isBranch()) {
                    return null;
                }
                return bb.splitNodeWithLinksAt(inst, ir);
            }
        }
        return null;
    }
}
