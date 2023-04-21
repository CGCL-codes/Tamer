package example.nanoc;

/**
 * @author Franz-Josef Elmer
 */
public abstract class BinaryOperation implements Operation {

    private final Operation leftOperand;

    private final Operation rightOperand;

    public BinaryOperation(Operation leftOperand, Operation rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Object execute(Block block) {
        return operation(leftOperand.execute(block), rightOperand.execute(block));
    }

    protected abstract Object operation(Object leftValue, Object rightValue);
}
