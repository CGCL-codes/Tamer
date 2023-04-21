package org.yaoqiang.bpmn.model.elements.activities;

import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.core.common.Expression;
import org.yaoqiang.bpmn.model.elements.core.common.FormalExpression;
import org.yaoqiang.bpmn.model.elements.core.foundation.BaseElement;

/**
 * ResourceParameterBinding
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class ResourceParameterBinding extends BaseElement {

    public ResourceParameterBinding(ResourceParameterBindings parent) {
        super(parent, "resourceParameterBinding");
    }

    protected void fillStructure() {
        XMLAttribute attrParameterRef = new XMLAttribute(this, "parameterRef");
        FormalExpression refExpression = new FormalExpression(this);
        super.fillStructure();
        add(attrParameterRef);
        add(refExpression);
    }

    public ResourceParameterBindings getParent() {
        return (ResourceParameterBindings) parent;
    }

    public final String getParameterRef() {
        return get("parameterRef").toValue();
    }

    public final void setParameterRef(String parameterRef) {
        set("parameterRef", parameterRef);
    }

    public final Expression getExpressionElement() {
        return (Expression) get("formalExpression");
    }
}
