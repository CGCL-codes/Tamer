package compiler.containers.operators;

import compiler.containers.operands.Operand;

public class DivisionSign extends MultiplicationOperator {

    public DivisionSign() {
        m_OperIndex = 3;
        m_Priority = 2;
    }

    /**
	 * Makes the code for the divition operation.
	 * @param Operand p_Opnd1. The first operand.
	 * @param Operand p_Opnd2. The second operand.
	 * @param Operand toOpnd. The operand where to store the result.
	 * @return String. The code for the operation.
	 */
    public String makeCode(Operand p_Opnd1, Operand p_Opnd2, Operand toOpnd) {
        return +DIV + " " + p_Opnd2.makeCode() + " " + p_Opnd1.makeCode() + " " + toOpnd.makeCode();
    }
}
