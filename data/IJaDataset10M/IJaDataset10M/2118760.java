package net.sf.jode.flow;

import net.sf.jode.GlobalOptions;
import net.sf.jode.expr.*;

public class CreateForInitializer {

    /**
     * This combines an variable initializer into a for statement
     * @param forBlock the for block
     * @param last  the lastModified of the flow block.
     */
    public static boolean transform(LoopBlock forBlock, StructuredBlock last) {
        if (!(last.outer instanceof SequentialBlock)) return false;
        SequentialBlock sequBlock = (SequentialBlock) last.outer;
        if (!(sequBlock.subBlocks[0] instanceof InstructionBlock)) return false;
        InstructionBlock init = (InstructionBlock) sequBlock.subBlocks[0];
        if (!init.getInstruction().isVoid() || !(init.getInstruction() instanceof CombineableOperator) || !forBlock.conditionMatches((CombineableOperator) init.getInstruction())) return false;
        if (GlobalOptions.verboseLevel > 0) GlobalOptions.err.print('f');
        forBlock.setInit((InstructionBlock) sequBlock.subBlocks[0]);
        return true;
    }
}
