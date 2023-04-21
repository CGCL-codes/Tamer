package compiler.containers.operators;

import compiler.containers.operands.Operand;

public class AdditionSign extends AdditionOperator {

    public AdditionSign() {
        m_OperIndex = 2;
        m_Priority = 1;
    }

    /**
	 * Makes the code for the addition operation.
	 * @param Operand p_Opnd1. The first operand.
	 * @param Operand p_Opnd2. The second operand.
	 * @param Operand toOpnd. The operand where to store the result.
	 * @return String. The code for the operation.
	 */
    public String makeCode(Operand p_Opnd1, Operand p_Opnd2, Operand toOpnd) {
        return +ADD + " " + p_Opnd1.makeCode() + " " + p_Opnd2.makeCode() + " " + toOpnd.makeCode();
    }
}
