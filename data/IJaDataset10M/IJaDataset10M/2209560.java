package net.sourceforge.nrl.parser.ast.constraints.impl;

import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.Token;
import net.sourceforge.nrl.parser.ast.INRLAstVisitor;
import net.sourceforge.nrl.parser.ast.constraints.IConcatenatedReport;
import net.sourceforge.nrl.parser.ast.constraints.IExpression;
import net.sourceforge.nrl.parser.ast.impl.Antlr3NRLBaseAst;

public class ConcatenatedReportImpl extends Antlr3NRLBaseAst implements IConcatenatedReport {

    private List<IExpression> expressions = null;

    public ConcatenatedReportImpl(Token token) {
        super(token);
    }

    @Override
    public void accept(INRLAstVisitor visitor) {
        if (visitor.visitBefore(this)) {
            for (IExpression expr : getExpressions()) {
                expr.accept(visitor);
            }
        }
        visitor.visitAfter(this);
    }

    public String dump(int indent) {
        String result = doIndent(indent) + "concatenated report" + NEWLINE;
        for (IExpression expr : getExpressions()) {
            result = result + expr.dump(indent + 1);
        }
        return result;
    }

    public List<IExpression> getExpressions() {
        if (expressions != null) {
            return expressions;
        }
        expressions = new ArrayList<IExpression>();
        List<?> children = getChildren();
        for (Object child : children) {
            expressions.add((IExpression) child);
        }
        return expressions;
    }
}
