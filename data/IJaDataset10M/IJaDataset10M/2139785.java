package egu.plugin.util.parse.externalReference.expression;

import egu.plugin.util.StackDeclaration;
import egu.plugin.util.parse.externalReference.ExternalReference;
import egu.plugin.util.parse.externalReference.IExternalReferenceRule;
import java.util.List;
import java.util.Set;
import org.eclipse.cdt.core.dom.ast.*;

public class RuleListExpression implements IExternalReferenceRule {

    @Override
    public Boolean Apply(IASTNode Node, StackDeclaration declDefined, Set<ExternalReference> externalRef, List<IASTNode> NotManaged) {
        ListExpressionRule Rules = new ListExpressionRule();
        if (!(Node instanceof IASTExpressionList)) {
            return false;
        }
        IASTExpressionList ExpressionList = (IASTExpressionList) Node;
        for (IASTExpression Expression : ExpressionList.getExpressions()) {
            Rules.Apply(Expression, declDefined, externalRef, NotManaged);
        }
        return true;
    }
}
