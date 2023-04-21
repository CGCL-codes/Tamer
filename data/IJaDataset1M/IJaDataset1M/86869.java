package ostf.test.data.expression;

public interface ScriptableExpression {

    public Object doExpression(Object[] args) throws ExpressionException;
}
