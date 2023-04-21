package org.deved.antlride.core.model.ast.criteria;

import org.deved.antlride.core.model.IModelElement;

class ModelElementSourcePositionCriteria implements IModelElementCriteria {

    private int sourcePosition;

    public ModelElementSourcePositionCriteria(int sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public boolean accept(IModelElement element) {
        return element.sourceStart() <= sourcePosition;
    }
}
