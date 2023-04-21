package org.yaoqiang.bpmn.model.elements.core.common;

import org.yaoqiang.bpmn.model.elements.XMLElement;
import org.yaoqiang.bpmn.model.elements.XMLFactory;

/**
 * Expressions
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class Expressions extends XMLFactory {

    protected String elementName = null;

    public Expressions(XMLElement parent, String name) {
        super(parent, name);
        this.elementName = name;
    }

    public XMLElement generateNewElement() {
        if (type.equals("formalExpression")) {
            if (elementName == null) {
                return new FormalExpression(this);
            } else {
                return new FormalExpression(this, elementName);
            }
        } else if (type.equals("expression")) {
            if (elementName == null) {
                return new Expression(this);
            } else {
                return new Expression(this, elementName);
            }
        }
        return null;
    }
}
