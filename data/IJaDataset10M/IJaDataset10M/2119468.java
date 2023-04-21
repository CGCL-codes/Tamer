package com.volantis.xml.expression.impl;

import com.volantis.shared.recovery.RecoverableTransactionMap;
import com.volantis.shared.recovery.TransactionTracker;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.expression.RecoverableExpressionScope;
import com.volantis.xml.expression.InternalExpressionScope;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionScope;
import com.volantis.xml.expression.Variable;
import com.volantis.xml.expression.Value;

/**
 * This is a simple, generic implementation of the ExpressionScope interface.
 *
 * @see com.volantis.xml.expression.impl.SimpleExpressionFactory
 */
public class SimpleExpressionScope implements RecoverableExpressionScope, InternalExpressionScope {

    /**
     * The factory by which this scope was created.
     */
    protected ExpressionFactory factory;

    /**
     * The scope enclosing this scope, or null if this scope is top-level.
     */
    private ExpressionScope enclosingScope;

    /**
     * The variables defined within this scope.
     */
    private RecoverableTransactionMap variables;

    /**
     * A TransactionTracker to determine our transaction state.
     */
    private TransactionTracker tracker = new TransactionTracker();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory        the factory by which the scope was created
     * @param enclosingScope the scope enclosing this one
     */
    public SimpleExpressionScope(ExpressionFactory factory, ExpressionScope enclosingScope) {
        this.factory = factory;
        this.enclosingScope = enclosingScope;
        this.variables = new RecoverableTransactionMap();
    }

    public ExpressionScope getEnclosingScope() {
        return enclosingScope;
    }

    public Variable declareVariable(ExpandedName name, Value initialValue) {
        Variable variable = null;
        if (variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name.getLocalName() + " in namespace \"" + name.getNamespaceURI() + "\" already exists in this scope");
        } else {
            variable = factory.createVariable(name, initialValue);
            variables.put(name, variable);
        }
        return variable;
    }

    public Variable declareVariable(ExpandedName name) {
        return declareVariable(name, SimpleVariable.UNSET);
    }

    public Variable resolveVariable(ExpandedName name) {
        Variable variable = (Variable) variables.get(name);
        if ((variable == null) && (enclosingScope != null)) {
            variable = enclosingScope.resolveVariable(name);
        }
        return variable;
    }

    public void startTransaction() {
        tracker.startTransaction();
        variables.startTransaction();
    }

    public void commitTransaction() {
        tracker.commitTransaction();
        variables.commitTransaction();
    }

    public void rollbackTransaction() {
        tracker.rollbackTransaction();
        variables.rollbackTransaction();
    }
}
