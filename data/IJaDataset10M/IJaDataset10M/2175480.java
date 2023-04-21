package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIBaseNodeContainer;
import org.monet.bpi.BPIBehaviorNodeContainer;
import org.monet.bpi.BPISchema;
import org.monet.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.kernel.components.ComponentPersistence;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.model.Node;

public abstract class BPIBaseNodeContainerImpl<Schema extends BPISchema<?>, OperationEnum extends Enum<?>> extends BPIBaseNodeImpl<Schema> implements BPIBaseNodeContainer<Schema>, BPIBehaviorNodeContainer<OperationEnum> {

    String getChildNodeId(String code) {
        if (code == null) return null;
        if (code.length() <= 0) return null;
        if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
        return this.node.getIndicatorValue(code + ".value");
    }

    protected BPIBaseNode<?> getChildNode(String name) {
        BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
        ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
        String code = BusinessUnit.getInstance().getBusinessModel().getDictionary().getNodeDefinition(name).getCode();
        Node node = componentPersistence.loadNode(this.getChildNodeId(code));
        BPIBaseNodeImpl<?> containedNode = bpiClassLocator.getDefinitionInstance(node.getDefinition().getName());
        containedNode.injectNode(node);
        return (BPIBaseNode<?>) containedNode;
    }
}
