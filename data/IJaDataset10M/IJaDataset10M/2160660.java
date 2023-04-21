package org.jmlspecs.jml6.core.javacontract.parser;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jmlspecs.jml6.core.ast.JmlClause;
import org.jmlspecs.jml6.core.ast.JmlEnsuresClause;
import org.jmlspecs.jml6.core.checkers.IProblemReporter;

/**
 * Parses {@link org.jmlspecs.javacontract.JC#ensures}
 * @see org.jmlspecs.javacontract.JC#ensures(boolean)
 * @author DSRG
 */
public class EnsuresClauseParser extends ClauseParser {

    protected EnsuresClauseParser() {
        super(new String[] { ENSURES }, new String[] { OLD, RESULT, ALL });
    }

    @Override
    public List<JmlClause> parse(MethodInvocation methodInvocation, boolean isRedundant, IProblemReporter problemReporter) throws InsufficientBindingsException {
        List<JmlClause> clauses = new ArrayList<JmlClause>();
        Expression expression = (Expression) methodInvocation.arguments().get(0);
        if (hasInvalidJcClause(expression)) problemReporter.reportError("Contract within an ensures clause.", expression);
        clauses.add(new JmlEnsuresClause(isRedundant, expression));
        return clauses;
    }
}
