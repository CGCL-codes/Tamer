package org.skeegenin.searKit;

public class CollapsibleSearchNodeCompleter<TSearchNode extends ICollapsibleSearchNode<?, TSearchNode>> implements ISearchNodeCompleter<TSearchNode> {

    @Override
    public void completeNode(TSearchNode node, ISearchQueue<TSearchNode> searchQueue) {
        TSearchNode parentNode = node.getParentNode();
        while (parentNode != null) {
            parentNode.setChildCompleted(node);
            if (parentNode.isComplete()) {
                node = parentNode;
                parentNode = node.getParentNode();
            } else {
                if (false == parentNode.hasActiveChildren()) {
                    searchQueue.addNode(parentNode);
                }
                break;
            }
        }
    }
}
