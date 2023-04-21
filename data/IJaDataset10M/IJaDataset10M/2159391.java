package org.sonify.vm.intermediate.execution;

import org.sonify.vm.execution.BlockScope;
import org.sonify.vm.execution.DataEnvironment;
import org.sonify.vm.execution.RuntimeScope;

/**
 * Enter a block opcode that adds a custom scope, used for if statements, etc.
 *
 * @author Melissa Stefik
 */
public class BeginScopeStep extends IntermediateStep {

    protected String blockName;

    @Override
    public void execute() {
        DataEnvironment de = vm.getDataEnvironment();
        BlockScope cs = new BlockScope();
        RuntimeScope localScope = de.getLocalScope();
        cs.setBlockName(blockName);
        cs.setParent(localScope);
        de.callStackPush(cs);
    }

    @Override
    public void unexecute() {
        DataEnvironment de = vm.getDataEnvironment();
        de.callStackUndo();
    }

    /**
     * set the static key for the check statement.
     *
     * @param name
     */
    public void setBlockName(String name) {
        blockName = name;
    }
}
