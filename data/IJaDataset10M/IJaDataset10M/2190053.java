package jparse.expr;

import antlr.Token;
import jparse.JavaTokenTypes;
import jparse.Type;
import jparse.VarList;

/**
 * An AST node that represents a bitwise expression
 *
 * @version $Revision: 1.1.1.1 $, $Date: 2006/10/22 16:11:43 $
 * @author Jerry James
 */
public final class BitwiseAST extends ExpressionAST implements JavaTokenTypes {

    /**
     * The left expression
     */
    private ExpressionAST left;

    /**
     * The right expression
     */
    private ExpressionAST right;

    /**
     * Create a new bitwise expression AST
     *
     * @param token the token represented by this AST node
     */
    public BitwiseAST(final Token token) {
        super(token);
    }

    public void parseComplete() {
        left = (ExpressionAST) getFirstChild();
        right = (ExpressionAST) left.getNextSibling();
        left.parseComplete();
        right.parseComplete();
    }

    protected Type computeType() {
        final Type leftType = left.retrieveType();
        if (leftType == Type.booleanType) return leftType;
        final Type rightType = right.retrieveType();
        return Type.arithType(leftType, rightType);
    }

    protected Type[] computeExceptions() {
        return Type.mergeTypeLists(left.getExceptionTypes(), right.getExceptionTypes());
    }

    protected Object computeValue() {
        final Object leftObj = left.getValue();
        if (leftObj == nonconstant) return nonconstant;
        final Object rightObj = right.getValue();
        if (rightObj == nonconstant) return nonconstant;
        final Type myType = retrieveType();
        if (myType == Type.booleanType) {
            final boolean leftBool = ((Boolean) leftObj).booleanValue();
            final boolean rightBool = ((Boolean) rightObj).booleanValue();
            switch(getType()) {
                case BOR:
                    return new Boolean(leftBool | rightBool);
                case BXOR:
                    return new Boolean(leftBool ^ rightBool);
                case BAND:
                    return new Boolean(leftBool & rightBool);
            }
        } else if (myType == Type.longType) {
            final long leftLong = ((Number) leftObj).longValue();
            final long rightLong = ((Number) rightObj).longValue();
            switch(getType()) {
                case BOR:
                    return new Long(leftLong | rightLong);
                case BXOR:
                    return new Long(leftLong ^ rightLong);
                case BAND:
                    return new Long(leftLong & rightLong);
            }
        } else {
            final int leftInt = ((Number) leftObj).intValue();
            final int rightInt = ((Number) rightObj).intValue();
            switch(getType()) {
                case BOR:
                    return new Integer(leftInt | rightInt);
                case BXOR:
                    return new Integer(leftInt ^ rightInt);
                case BAND:
                    return new Integer(leftInt & rightInt);
            }
        }
        return nonconstant;
    }

    public VarList getVarList() {
        return new VarList(left.getVarList(), right.getVarList());
    }

    /**
     * Get the left-hand-side of this bitwise expression
     *
     * @return the lhs of the expression
     */
    public ExpressionAST getLeft() {
        return left;
    }

    /**
     * Get the right-hand-side of this bitwise expression
     *
     * @return the rhs of the expression
     */
    public ExpressionAST getRight() {
        return right;
    }
}
