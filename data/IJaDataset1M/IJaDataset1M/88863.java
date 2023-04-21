package jparse.expr;

import antlr.Token;
import jparse.JavaTokenTypes;
import jparse.Type;
import jparse.VarList;

/**
 * An AST node that represents a unary arithmetic expression
 *
 * @version $Revision: 1.1.1.1 $, $Date: 2006/10/22 16:11:44 $
 * @author Jerry James
 */
public final class UnaryArithAST extends ExpressionAST implements JavaTokenTypes {

    /**
     * The expression on which to perform this unary operation
     */
    private ExpressionAST operand;

    /**
     * Create a new unary arithmetic expression AST
     *
     * @param token the token represented by this AST node
     */
    public UnaryArithAST(final Token token) {
        super(token);
    }

    public void parseComplete() {
        if (getType() == MINUS) context.negative = !context.negative;
        operand = (ExpressionAST) getFirstChild();
        operand.parseComplete();
    }

    protected Type computeType() {
        final int tokType = getType();
        final Type opType = operand.retrieveType();
        return tokType != INC && tokType != DEC && (opType == Type.byteType || opType == Type.shortType || opType == Type.charType) ? Type.intType : opType;
    }

    protected Type[] computeExceptions() {
        return operand.getExceptionTypes();
    }

    protected Object computeValue() {
        final int operator = getType();
        if (operator == INC || operator == DEC || operator == POST_INC || operator == POST_DEC) return nonconstant;
        final Object subval = operand.getValue();
        if (subval == nonconstant) return nonconstant;
        if (operator == PLUS || operator == MINUS) return subval;
        final Number num = (Number) subval;
        return (retrieveType() == Type.intType) ? (Object) new Integer(~num.intValue()) : (Object) new Long(~num.longValue());
    }

    public VarList getVarList() {
        return operand.getVarList();
    }

    /**
     * Get the operand of this operator
     *
     * @return the operand
     */
    public ExpressionAST getOperand() {
        return operand;
    }
}
