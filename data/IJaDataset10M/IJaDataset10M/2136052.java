package egu.plugin.util.parse.externalReference;

import egu.plugin.activator.EGUPluginActivator;
import egu.plugin.util.StackDeclaration;
import java.util.List;
import java.util.Set;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

/**
 * @author pachdjian
 *
 */
public class RuleCompoundStatement implements IExternalReferenceRule {

    @Override
    public Boolean Apply(IASTNode node, StackDeclaration declDefined, Set<ExternalReference> externalRef, List<IASTNode> notManaged) {
        if (!(node instanceof IASTCompoundStatement)) return false;
        IASTCompoundStatement CompoundStatement = (IASTCompoundStatement) node;
        ListStatementRule Rules = new ListStatementRule();
        for (IASTStatement Statement : CompoundStatement.getStatements()) {
            Rules.Apply(Statement, declDefined, externalRef, notManaged);
        }
        return true;
    }
}
