package tm.clc.ast;

import java.util.*;
import tm.interfaces.SourceCoords;
import tm.virtualMachine.AbruptCompletionStatus;
import tm.virtualMachine.VMState;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */
public class StatJump extends StatementNode {

    Object tag;

    public StatJump(SourceCoords coords, int varDepth, Object tag) {
        super("jump", coords, varDepth);
        this.tag = tag;
    }

    public void step(VMState vms) {
        AbruptCompletionStatus acs = new JumpCompletionStatus(tag);
        vms.abruptCompletion(acs);
    }

    public void beVisited(StatementNodeVisitor visitor) {
        visitor.visit(this);
    }

    public String toString(Hashtable h) {
        return "    (" + h.get(this) + ") StatJump line=" + getCoords() + " depth=" + getVarDepth() + " tag=" + tag + "\n" + "\n";
    }
}
