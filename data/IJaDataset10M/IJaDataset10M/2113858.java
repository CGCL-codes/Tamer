package com.amd.javalabs.tools.camodel;

import com.amd.javalabs.tools.commons.memory.Block;
import com.amd.javalabs.tools.disasm.InstructionGraph;

/**
 * A class to represent a CompiledOrNativeMethodModel.
 * 
 * @author gfrost
 * 
 */
@SuppressWarnings("serial")
public interface CompiledOrDynamicCodeMethodModel {

    public Block getBlock();

    public InstructionGraph getInstructionGraph();
}
