package com.newisys.langschema;

import java.util.List;
import java.util.Set;

/**
 * Base interface for expressions representing an operation.
 * 
 * @author Trevor Robinson
 */
public interface Operation extends Expression {

    /**
     * Returns a list of expressions that evaluate to the operands of the
     * operation.
     *
     * @return List of Expression
     */
    List<? extends Expression> getOperands();

    /**
     * Returns a collection of any language-specific modifiers for this
     * operation.
     *
     * @return Set of OperationModifier
     */
    Set<? extends OperationModifier> getModifiers();
}
