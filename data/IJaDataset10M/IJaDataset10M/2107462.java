package org.kumenya.ide.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * 
 */
public class CQLLogicalAndExpressionImpl extends CQLLogicalExpressionImpl {

    public CQLLogicalAndExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public String toString() {
        return "Logical AND expression";
    }
}
