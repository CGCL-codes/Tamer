package com.metanology.mde.core.metaModel.datatypes;

public class ActionExpression extends Expression {

    public Object cloneObject(java.util.Map parameters, Object clone) {
        java.util.Map objMap = (java.util.Map) parameters.get("model.element");
        if (objMap.get(this) != null) return objMap.get(this);
        if (clone == null) clone = new ActionExpression();
        super.cloneObject(parameters, clone);
        return clone;
    }
}
