package de.schlund.pfixcore.scriptedflow.compiler;

import de.schlund.pfixcore.scriptedflow.vm.Instruction;

/**
 * High-level representation of a command that is used
 * within a scripted flow.
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public interface Statement {

    Statement getParentStatement();

    Instruction[] getInstructions();
}
