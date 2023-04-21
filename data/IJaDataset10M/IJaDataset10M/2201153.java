package pcgen.base.formula.baseops;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import pcgen.base.formula.core.AbstractBinaryOperation;
import pcgen.base.formula.core.NodeEvaluator;
import pcgen.base.formula.core.OperationUtilities;
import pcgen.base.formula.parse.SimpleNode;

/**
 * @author Thomas Parker (thpr@users.sourceforge.net)
 * 
 * Performs a Logical OR (||) operation between the child SimpleNodes. If either
 * child SimpleNode is non-Zero, then this SimpleNode will resolve to 1.0.
 * Otherwise, it will resolve to 0.0
 */
public final class OrOperation extends AbstractBinaryOperation {

    /**
	 * Creates a new OrOperation
	 */
    public OrOperation() {
        super();
    }

    public int visitBytecode(NodeEvaluator ev, MethodVisitor mv, SimpleNode node, int loc) {
        if (node.jjtGetNumChildren() != 2) {
            throw new IllegalArgumentException("OrOperation expects a JJTOR SimpleNode to contain 2 children, has: " + node.jjtGetNumChildren());
        }
        Label nonZeroLabel = new Label();
        Label escapeLabel = new Label();
        int lhsm = OperationUtilities.compareToZero(ev, mv, loc, (SimpleNode) node.jjtGetChild(0));
        mv.visitJumpInsn(Opcodes.IFNE, nonZeroLabel);
        int rhsm = OperationUtilities.compareToZero(ev, mv, loc, (SimpleNode) node.jjtGetChild(1));
        mv.visitJumpInsn(Opcodes.IFNE, nonZeroLabel);
        mv.visitInsn(Opcodes.DCONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, escapeLabel);
        mv.visitLabel(nonZeroLabel);
        mv.visitInsn(Opcodes.DCONST_1);
        mv.visitLabel(escapeLabel);
        return Math.max(lhsm, rhsm);
    }

    public String getSign() {
        return "||";
    }
}
