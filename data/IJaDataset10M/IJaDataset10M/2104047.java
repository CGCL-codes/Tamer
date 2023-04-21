package org.ujac.util.exi.type;

import org.ujac.util.exi.BaseExpressionOperation;
import org.ujac.util.exi.ExpressionContext;
import org.ujac.util.exi.ExpressionException;
import org.ujac.util.exi.ExpressionInterpreter;
import org.ujac.util.exi.ExpressionOperation;
import org.ujac.util.exi.ExpressionTuple;
import org.ujac.util.exi.NoOperandException;
import org.ujac.util.exi.Operand;
import org.ujac.util.exi.OperandException;
import org.ujac.util.exi.OperandNotSupportedException;

/**
 * Name:IntArrayType<br>
 * Description: An expression type handler for float arrays.
 * 
 * @author lauerc
 */
public class FloatArrayType extends BaseType {

    /**
   * The 'isEmpty' operation for arrays.
   */
    public class IsEmptyOperation extends BaseExpressionOperation {

        /**
     * Evaluates the given values.
     * @param expr The expression tuple to process. 
     * @param ctx The expression context.
     * @return The result of the tuple evaluation.
     * @exception ExpressionException If the evaluation failed.
     */
        public Object evaluate(ExpressionTuple expr, ExpressionContext ctx) throws ExpressionException {
            Operand operand = expr.getOperand();
            if (operand != null) {
                throw new OperandNotSupportedException("No operand supported for operation: " + expr.getOperation() + " on object " + expr.getObject() + "!");
            }
            float[] array = (float[]) (expr.getObject().getValue());
            return new Boolean(array.length == 0);
        }

        /**
     * Gets a description for the operation.
     * @return The item's description.
     */
        public String getDescription() {
            return "Tells whether the array is empty or not.";
        }
    }

    /**
   * The 'notEmpty' operation for arrays.
   */
    public class NotEmptyOperation extends BaseExpressionOperation {

        /**
     * Evaluates the given values.
     * @param expr The expression tuple to process. 
     * @param ctx The expression context.
     * @return The result of the tuple evaluation.
     * @exception ExpressionException If the evaluation failed.
     */
        public Object evaluate(ExpressionTuple expr, ExpressionContext ctx) throws ExpressionException {
            Operand operand = expr.getOperand();
            if (operand != null) {
                throw new OperandNotSupportedException("No operand supported for operation: " + expr.getOperation() + " on object " + expr.getObject() + "!");
            }
            float[] array = (float[]) (expr.getObject().getValue());
            return new Boolean(array.length != 0);
        }

        /**
     * Gets a description for the operation.
     * @return The item's description.
     */
        public String getDescription() {
            return "Checks if the array is not empty.";
        }
    }

    /**
   * The 'size' operation for arrays.
   */
    public class SizeOperation extends BaseExpressionOperation {

        /**
     * Evaluates the given values.
     * @param expr The expression tuple to process. 
     * @param ctx The expression context.
     * @return The result of the tuple evaluation.
     * @exception ExpressionException If the evaluation failed.
     */
        public Object evaluate(ExpressionTuple expr, ExpressionContext ctx) throws ExpressionException {
            Operand operand = expr.getOperand();
            if (operand != null) {
                throw new OperandNotSupportedException("No operand supported for operation: " + expr.getOperation() + " on object " + expr.getObject() + "!");
            }
            float[] array = (float[]) (expr.getObject().getValue());
            return new Integer(array.length);
        }

        /**
     * Gets a description for the operation.
     * @return The item's description.
     */
        public String getDescription() {
            return "Determines the size of the array.";
        }
    }

    /**
   * The 'get' operation for arrays.
   */
    public class GetOperation extends BaseExpressionOperation {

        /**
     * Evaluates the given values.
     * @param expr The expression tuple to process. 
     * @param ctx The expression context.
     * @return The result of the tuple evaluation.
     * @exception ExpressionException If the evaluation failed.
     */
        public Object evaluate(ExpressionTuple expr, ExpressionContext ctx) throws ExpressionException {
            Operand operand = expr.getOperand();
            if (operand == null) {
                throw new NoOperandException("No operand given for operation: " + expr.getOperation() + " on object " + expr.getObject() + "!");
            }
            int operandValue = interpreter.evalIntOperand(operand, ctx, false);
            float[] array = (float[]) (expr.getObject().getValue());
            try {
                return new Float(array[operandValue]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new OperandException("Invalid index " + operandValue + " for array at expression '" + expr.getCode() + "'.");
            }
        }

        /**
     * Gets a description for the operation.
     * @return The item's description.
     */
        public String getDescription() {
            return "Gets an element from the array.";
        }
    }

    /**
   * Constructs a ObjectArrayType instance with specific attributes.
   * @param interpreter The expression interpreter.
   */
    public FloatArrayType(ExpressionInterpreter interpreter) {
        super(interpreter);
        ExpressionOperation op = new IsEmptyOperation();
        addOperation("isEmpty", op);
        op = new NotEmptyOperation();
        addOperation("notEmpty", op);
        op = new SizeOperation();
        addOperation("size", op);
        op = new GetOperation();
        addOperation(".", op);
        addOperation("[]", op);
        addOperation("get", op);
    }

    /**
   * @see org.ujac.util.exi.ExpressionType#getType()
   */
    public Class getType() {
        return float[].class;
    }
}
