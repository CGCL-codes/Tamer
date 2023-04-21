package org.jmlspecs.jml6.core.javacontract.parser;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jmlspecs.jml6.core.ast.JmlAssignableClause;
import org.jmlspecs.jml6.core.ast.JmlClause;
import org.jmlspecs.jml6.core.checkers.IProblemReporter;

/**
 * @see org.jmlspecs.javacontract.JC#assignable(Object...)
 * @author DSRG
 */
public class AssignableClauseParser extends ClauseParser {

    protected AssignableClauseParser() {
        super(new String[] { ASSIGNABLE });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<JmlClause> parse(MethodInvocation methodInvocation, boolean isRedundant, IProblemReporter problemReporter) throws InsufficientBindingsException {
        List<Expression> assignablesList = methodInvocation.arguments();
        Expression[] assignables = new Expression[assignablesList.size()];
        assignablesList.toArray(assignables);
        for (Expression expr : assignablesList) {
            if (hasInvalidJcClause(expr)) problemReporter.reportError("Contract within an assignable clause.", expr);
        }
        JmlClause clause = new JmlAssignableClause(isRedundant, assignables);
        List<JmlClause> clauses = new ArrayList<JmlClause>();
        clauses.add(clause);
        return clauses;
    }
}
