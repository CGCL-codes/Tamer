package org.deved.antlride.internal.core.model.ast.criteria;

import org.deved.antlride.core.model.IModelElement;
import org.deved.antlride.core.model.IRule;
import org.deved.antlride.core.model.ast.criteria.IModelElementCriteria;

public class LexerRuleCriteria implements IModelElementCriteria {

    public boolean accept(IModelElement element) {
        IRule rule = element.getAdapter(IRule.class);
        return rule == null ? false : rule.isLexerRule();
    }
}
