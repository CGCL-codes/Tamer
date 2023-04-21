package example.nanoc;

/**
 * @author Franz-Josef Elmer
 */
public abstract class CompareOperation extends BinaryOperation {

    public CompareOperation(Operation leftOperand, Operation rightOperand) {
        super(leftOperand, rightOperand);
    }

    protected Object operation(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            int left = ((Number) leftValue).intValue();
            int right = ((Number) rightValue).intValue();
            return new Integer(compare(left, right) ? 1 : 0);
        } else if (leftValue instanceof String && rightValue instanceof String) {
            int c = ((String) leftValue).compareTo((String) rightValue);
            return new Integer(compare(c, 0) ? 1 : 0);
        } else {
            throw new IllegalArgumentException("Incompatible operands: " + leftValue + ", " + rightValue);
        }
    }

    protected abstract boolean compare(int left, int right);
}
