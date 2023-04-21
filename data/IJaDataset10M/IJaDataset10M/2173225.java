package pl.wcislo.sbql4j.lang.parser.expression;

public abstract class BinaryExpression extends Expression {

    public Expression ex1;

    public Expression ex2;

    public BinaryExpression(int position, int priority, Expression ex1, Expression ex2) {
        super(position, priority);
        this.ex1 = ex1;
        this.ex2 = ex2;
        setParentExpression(ex1);
        setParentExpression(ex2);
    }

    public BinaryExpression(int priority, Expression ex1, Expression ex2) {
        this(-1, priority, ex1, ex2);
    }

    @Override
    public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
        if (oldExpr.equals(ex1)) {
            ex1 = newExpr;
            setParentExpression(newExpr);
        } else if (oldExpr.equals(ex2)) {
            ex2 = newExpr;
            setParentExpression(newExpr);
        }
    }
}
