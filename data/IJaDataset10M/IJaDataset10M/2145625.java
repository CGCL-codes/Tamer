package org.yaoqiang.bpmn.model.elements.core.foundation;

import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.XMLComplexElement;

/**
 * ExtensionDefinition
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class ExtensionDefinition extends XMLComplexElement {

    public ExtensionDefinition(ExtensionDefinitions parent) {
        super(parent, "extensionDefinition");
    }

    protected void fillStructure() {
        XMLAttribute attrName = new XMLAttribute(this, "name");
        ExtensionAttributeDefinitions refExtensionAttributeDefinitions = new ExtensionAttributeDefinitions(this);
        add(attrName);
        add(refExtensionAttributeDefinitions);
    }

    public ExtensionDefinitions getParent() {
        return (ExtensionDefinitions) parent;
    }
}
