package pcgen.base.formula.baseops;

import org.objectweb.asm.MethodVisitor;
import pcgen.base.formula.core.AbstractFormulaOperation;
import pcgen.base.formula.core.NodeEvaluator;
import pcgen.base.formula.parse.SimpleNode;

/**
 * Performs a mathematically associative operation between two items (such as
 * addition or multiplication). Note that the associative operation is defined
 * at the time of construction of the AssociativeOperation (along with the
 * String representation used to write out the persistent version of the
 * formula)
 * 
 * @author Thomas Parker (thpr@users.sourceforge.net)
 */
public class AssociativeOperation extends AbstractFormulaOperation {

    private final int inst;

    private final String sign;

    public AssociativeOperation(int instruction, String symbol) {
        inst = instruction;
        sign = symbol;
    }

    public int visitBytecode(NodeEvaluator ev, MethodVisitor mv, SimpleNode node, int loc) {
        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
        int max = ev.evaluate(mv, lhs, loc);
        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            SimpleNode rhs = (SimpleNode) node.jjtGetChild(i);
            int rhsm = ev.evaluate(mv, rhs, loc + 2);
            mv.visitInsn(inst);
            max = Math.max(max, rhsm);
        }
        return max;
    }

    public void visitToString(NodeEvaluator ev, MethodVisitor mv, SimpleNode node) {
        ev.stringify(mv, (SimpleNode) node.jjtGetChild(0));
        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            writeString(mv, sign);
            ev.stringify(mv, (SimpleNode) node.jjtGetChild(i));
        }
    }

    public String getSign() {
        return sign;
    }
}
