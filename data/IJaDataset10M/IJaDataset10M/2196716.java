package de.schlund.pfixcore.scriptedflow.compiler;

import de.schlund.pfixcore.scriptedflow.vm.Instruction;
import de.schlund.pfixcore.scriptedflow.vm.JumpCondFalseInstruction;
import de.schlund.pfixcore.scriptedflow.vm.NopInstruction;

/**
 * Allows to bind the execution of one or more statements to a condition  
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class IfStatement extends AbstractStatement {

    private String condition;

    private Statement child;

    private Instruction jumpInstr = null;

    private Instruction finalInstr = new NopInstruction();

    public IfStatement(Statement parent) {
        super(parent);
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setChild(Statement child) {
        this.child = child;
    }

    public Instruction[] getInstructions() {
        if (jumpInstr == null) {
            jumpInstr = new JumpCondFalseInstruction(condition, finalInstr);
        }
        Instruction[] temp = child.getInstructions();
        Instruction[] temp2 = new Instruction[temp.length + 2];
        temp2[0] = jumpInstr;
        temp2[temp2.length - 1] = finalInstr;
        for (int i = 0; i < temp.length; i++) {
            temp2[i + 1] = temp[i];
        }
        return temp2;
    }
}
