package examples.math;

import examples.math.cmd.*;

/**
 *
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public interface MathVisitor {

    /** String containing the CVS revision. Read out via reflection!*/
    static final String CVS_REVISION = "$Revision: 1.1 $";

    void visit(ValueOperand theOp);

    void visit(AddOperator theOp);

    void visit(MinusOperator theOp);

    void visit(DivideOperator theOp);

    void visit(MultiplyOperator theOp);
}
