package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNodeCollection;
import org.monet.bpi.BPIBehaviorNodeCollection;
import org.monet.bpi.BPIExpression;
import org.monet.bpi.BPINode;
import org.monet.bpi.BPIReference;
import org.monet.bpi.BPISchema;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.model.Node;

public abstract class BPIBaseNodeCollectionImpl<Schema extends BPISchema<?>, OperationEnum extends Enum<?>> extends BPIBaseNodeImpl<Schema> implements BPIBaseNodeCollection<Schema>, BPIBehaviorNodeCollection<OperationEnum> {

    public Iterable<BPIReference> get(BPIExpression where) {
        return new BPICollectionIterableImpl(this.node, where);
    }

    @SuppressWarnings("rawtypes")
    protected <T extends BPINode<?, ?>> T addNode(String name) {
        String code = BusinessUnit.getInstance().getBusinessModel().getDictionary().getTaskDefinition(name).getCode();
        Node node = componentPersistence.addNode(code, this.node);
        T bpiNode = this.bpiClassLocator.getDefinitionInstance(name);
        ((BPINodeImpl) bpiNode).injectNode(node);
        return (T) bpiNode;
    }

    @Override
    public void remove(String nodeId) {
        if (this.componentPersistence.loadNode(nodeId).getParentId().equals(this.node.getId())) this.componentPersistence.deleteNode(nodeId); else throw new RuntimeException("This collection doesn't contains the node " + nodeId);
    }

    @Override
    public <T extends org.monet.bpi.BPINode<?, ?>> void remove(T node) {
        if (node.getParentId().equals(this.node.getId())) this.componentPersistence.deleteNode(node.getId()); else throw new RuntimeException("This collection doesn't contains the node " + node.getId());
    }

    ;
}
