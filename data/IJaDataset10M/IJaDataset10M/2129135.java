package com.atosorigin.nl.brainfuck.compiler.v1;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.GOTO;
import com.atosorigin.nl.brainfuck.compiler.base.AbstractCompiler;
import com.atosorigin.nl.brainfuck.compiler.base.CompilerContext;

/**
 * @author a108600
 *
 */
public class LoopStartCompiler extends AbstractCompiler {

    /**
	 * 
	 */
    public LoopStartCompiler() {
        super();
    }

    @Override
    public void compile(CompilerContext ctx, Object instr) {
        GOTO jump = new GOTO(null);
        BranchHandle handle = ctx.append(jump);
        ctx.storeNextForBranch(handle);
    }

    @Override
    public int getMaxStack() {
        return 1;
    }
}
