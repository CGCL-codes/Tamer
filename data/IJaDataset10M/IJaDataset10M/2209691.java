package fluid.java.operator;

import fluid.ir.IRNode;

public interface Assignment extends StatementExpressionInterface {

    public IRNode getSource(IRNode node);

    public IRNode getTarget(IRNode node);
}
