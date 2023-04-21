package com.icesoft.net.messaging.expression;

public abstract class Operator extends Expression implements Operand {

    protected Operand leftOperand;

    protected Operand rightOperand;

    /**
     * <p>
     *   Constructs a new Operator object with the specified
     *   <code>leftOperand</code> and <code>rightOperand</code>.
     * </p>
     *
     * @param      leftOperand
     *                 the Operand that is to be to the left of the Operator.
     * @param      rightOperand
     *                 the Operand that is to be to the right of the Operator.
     * @throws     IllegalArgumentException
     *                 if one of the following occurs:
     *                 <ul>
     *                   <li>
     *                     the specified <code>leftOperand</code> is
     *                     <code>null</code>, or
     *                   </li>
     *                   <li>
     *                     the specified <code>rightOperand</code> is
     *                     <code>null</code>.
     *                   </li>
     *                 </ul>
     */
    protected Operator(final Operand leftOperand, final Operand rightOperand) throws IllegalArgumentException {
        if (leftOperand == null) {
            throw new IllegalArgumentException("leftOperand is null");
        }
        if (rightOperand == null) {
            throw new IllegalArgumentException("rightOperand is null");
        }
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * <p>
     *   Gets the Operand that is to the left of this Operator.
     * </p>
     *
     * @return     the left Operand.
     * @see        #getRightOperand()
     */
    public Operand getLeftOperand() {
        return leftOperand;
    }

    /**
     * <p>
     *   Gets the Operand that is to the right of this Operator.
     * </p>
     *
     * @return     the right Operand.
     * @see        #getLeftOperand()
     */
    public Operand getRightOperand() {
        return rightOperand;
    }
}
