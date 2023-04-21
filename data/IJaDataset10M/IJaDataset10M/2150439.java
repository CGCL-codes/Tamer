package org.jmlspecs.jml6.core.javacontract.parser;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.jmlspecs.javacontract.JC;
import org.jmlspecs.jml6.core.ast.JmlClause;
import org.jmlspecs.jml6.core.ast.JmlSpecCase;
import org.jmlspecs.jml6.core.ast.JmlSpecCaseBody;
import org.jmlspecs.jml6.core.checkers.IProblemReporter;

/**
 * Parses {@link org.jmlspecs.javacontract.JC#spec}
 * @see org.jmlspecs.javacontract.JC#spec
 * @author DSRG
 */
public class SpecCaseParser {

    Map<String, ClauseParser> clauseParsers = new HashMap<String, ClauseParser>();

    public SpecCaseParser(IParserProvider provider) {
        ClauseParser[] clauseParsersArray = { provider.getRequiresClauseParser(), provider.getEnsuresClauseParser(), provider.redundantClauseParser(), provider.getAssignableClauseParser(), provider.getModifiesClauseParser(), provider.getSignalsParser(), provider.getSignalsOnlyParser() };
        for (ClauseParser parser : clauseParsersArray) {
            for (String methodName : parser.getMethodNames()) {
                clauseParsers.put(methodName, parser);
            }
        }
    }

    /**
	 * 
	 * @param expressionStatement
	 * @return
	 * @throws InsufficientBindingsException
	 */
    @SuppressWarnings("unchecked")
    public JmlSpecCase parse(ExpressionStatement expressionStatement, IProblemReporter problemReporter) throws InsufficientBindingsException {
        Expression specExpression = expressionStatement.getExpression();
        if (specExpression.getNodeType() != ASTNode.METHOD_INVOCATION) throw new IllegalArgumentException("Expected MethodInvocation expression.");
        MethodInvocation methodInvocation = (MethodInvocation) specExpression;
        List<Expression> arguments = methodInvocation.arguments();
        Expression firstExpression = arguments.get(0);
        JC.Visibility modifier;
        int startIndex;
        if (firstExpression instanceof Name) {
            Name modifierNode = (Name) firstExpression;
            IVariableBinding varBinding = (IVariableBinding) modifierNode.resolveBinding();
            varBinding.getDeclaringClass();
            String name = varBinding.getName();
            modifier = JC.Visibility.valueOf(name);
            startIndex = 1;
        } else {
            modifier = getSpecCaseModifier(expressionStatement);
            startIndex = 0;
        }
        List<JmlClause> clauses = new ArrayList<JmlClause>();
        for (int i = startIndex; i < arguments.size(); i++) {
            Expression expression = arguments.get(i);
            if (!(expression instanceof MethodInvocation)) throw new IllegalArgumentException("Expect MethodInvocation expression" + "within spec.");
            MethodInvocation mi = (MethodInvocation) expression;
            clauses.addAll(parseClause(mi, false, problemReporter));
        }
        JmlClause[] clausesArray = clauses.toArray(new JmlClause[clauses.size()]);
        JmlSpecCaseBody body = new JmlSpecCaseBody(null, null, clausesArray);
        JmlSpecCase specCase = new JmlSpecCase(modifier, false, body);
        return specCase;
    }

    private List<JmlClause> parseClause(MethodInvocation mi, boolean isRedundant, IProblemReporter problemReporter) {
        try {
            String methodName = mi.getName().toString();
            ClauseParser parser = clauseParsers.get(methodName);
            if (parser == null) {
                problemReporter.reportError("Unexpect method invocation", mi);
            } else {
                return parser.parse(mi, isRedundant, problemReporter);
            }
        } catch (InsufficientBindingsException e) {
            problemReporter.reportError(e.getMessage(), e.getNode());
        }
        return new ArrayList<JmlClause>();
    }

    /**
	 * Parses the specCase modifier for a JC.spec ExpressionStatement.
	 * If a modifier is not specified, the method's visibility is used for
	 * visibility.
	 * @param expressionStatement
	 * @return
	 */
    private JC.Visibility getSpecCaseModifier(ExpressionStatement expressionStatement) {
        JC.Visibility modifier;
        modifier = null;
        ASTNode parent = expressionStatement.getParent();
        while (parent != null && !(parent instanceof MethodDeclaration)) {
            parent = parent.getParent();
        }
        if (parent != null && parent instanceof MethodDeclaration) {
            int mod = ((MethodDeclaration) parent).resolveBinding().getModifiers();
            if (Modifier.isPrivate(mod)) modifier = JC.PRIVATE; else if (Modifier.isPublic(mod)) modifier = JC.PUBLIC; else if (Modifier.isProtected(mod)) modifier = JC.PROTECTED; else modifier = JC.PACKAGE;
        }
        return modifier;
    }
}
