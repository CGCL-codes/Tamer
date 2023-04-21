package annone.engine.local.builder.nodes;

public class IfNode extends StatementNode {

    private ExpressionNode condition;

    private StatementNode then;

    private StatementNode _else;

    public IfNode() {
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
        setChildren(condition);
    }

    public StatementNode getThen() {
        return then;
    }

    public void setThen(StatementNode then) {
        this.then = then;
        setChildren(then);
    }

    public StatementNode getElse() {
        return _else;
    }

    public void setElse(StatementNode _else) {
        this._else = _else;
        setChildren(_else);
    }
}
