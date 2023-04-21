package org.kumenya.ide.psi.api;

import com.intellij.psi.PsiElement;
import org.kumenya.ide.psi.impl.CQLLabeledExpressionImpl;
import org.kumenya.ide.psi.impl.CQLRangeVarImpl;
import java.util.List;

/**
 * todo: rename CQLSelect
 */
public interface CQLSelectStatement extends CQLExpression {

    List<CQLRangeVarImpl> getRangeVars();

    List<CQLLabeledExpressionImpl> getProjection();
}
