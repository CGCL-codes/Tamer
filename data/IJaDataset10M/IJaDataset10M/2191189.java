package expressions;

import OpenCOM.IUnknown;

/**
 *
 * @author Tinoo
 */
public class Multiplier extends BaseOperator {

    protected static final int operandCount = 2;

    public Multiplier(IUnknown binder) {
        super(binder);
    }

    public int GetResult() {
        if (m_operands.interfaceList.size() != operandCount) return 0;
        IExpression firstOp = m_operands.interfaceList.elementAt(0);
        IExpression secondOp = m_operands.interfaceList.elementAt(1);
        return firstOp.GetResult() * secondOp.GetResult();
    }
}
