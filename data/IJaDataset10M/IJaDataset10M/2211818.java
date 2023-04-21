package ast.stm;

import semantica.Semantica;
import compiler.table.MySymbol;
import ast.exp.Expression;

public class AssignStatement extends Statement {

    private MySymbol variableId;

    private Expression expression;

    public AssignStatement(MySymbol variableId, Expression expression) {
        super();
        this.variableId = variableId;
        this.expression = expression;
    }

    public MySymbol getVariableId() {
        return variableId;
    }

    public void setVariableId(MySymbol variableId) {
        this.variableId = variableId;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "AssignStatement [expression=" + expression + ", variableId=" + variableId + "]";
    }

    public void semantica(Semantica s) {
        s.visit(this);
    }
}
