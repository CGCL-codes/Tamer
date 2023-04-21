package org.wsmo.factory;

import org.omwg.ontology.LogicalExpression;

public interface LogicalExpressionFactory {

    /**
     * Creates a LogicalExpression object from string.
     * @param expr the string representation of the logical expression
     * @return The newly created LogicalExpression object.
     */
    LogicalExpression createLogicalExpression(String expr);
}
