package com.dukesoftware.utils.node.graph;

import java.util.HashSet;
import java.util.Iterator;
import com.dukesoftware.utils.common.CollectionUtils;

public class BasicGraphNode extends TGraphNode<BasicGraphNode> {

    private final HashSet<BasicGraphNode> nodes;

    private BasicGraphNode[] cahce = new BasicGraphNode[0];

    public BasicGraphNode(String key) {
        super(key);
        nodes = new HashSet<BasicGraphNode>();
    }

    protected final boolean add(BasicGraphNode node) {
        return nodes.add(node);
    }

    protected final boolean remove(BasicGraphNode node) {
        return nodes.remove(node);
    }

    protected final void clear() {
        nodes.clear();
    }

    public final boolean contains(BasicGraphNode node) {
        return nodes.contains(node);
    }

    public final int degree() {
        return nodes.size();
    }

    public final Iterator<BasicGraphNode> iterator() {
        return nodes.iterator();
    }

    public final void updateArrayCache() {
        cahce = new BasicGraphNode[nodes.size()];
        CollectionUtils.collectionToArrayFastest(nodes, cahce);
    }

    public BasicGraphNode[] arrayCache() {
        return cahce;
    }
}
