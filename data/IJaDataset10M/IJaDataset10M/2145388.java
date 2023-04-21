package tm.clc.ast;

import java.util.Hashtable;
import tm.interfaces.SourceCoords;
import tm.virtualMachine.VMState;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */
public class StatPopRecovery extends StatementNode {

    StatementNodeLink nextLink = new StatementNodeLink();

    public StatPopRecovery(SourceCoords coords, int varDepth) {
        super("pop recovery", coords, varDepth);
    }

    public StatementNodeLink next() {
        return nextLink;
    }

    public void step(VMState vms) {
        trimVariables(varDepth, vms);
        vms.popRecoveryGroup();
        vms.top().map(this, null);
        vms.top().setSelected(nextLink.get());
    }

    public void beVisited(StatementNodeVisitor visitor) {
        visitor.visit(this);
        nextLink.beVisited(visitor);
    }

    public String toString(Hashtable h) {
        return "    (" + h.get(this) + ") StatPopRecovery line=" + getCoords() + " depth=" + getVarDepth() + "\n" + "      ---> " + formatLink(nextLink, h) + "\n";
    }
}
