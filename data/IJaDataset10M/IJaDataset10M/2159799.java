package net.sf.magicmap.core.model.node.impl;

import net.sf.magicmap.client.model.node.INodeModel;
import net.sf.magicmap.client.model.node.Node;
import net.sf.magicmap.client.interfaces.NodeModelListener;
import net.sf.magicmap.core.model.node.KeyGenerator;
import net.sf.magicmap.core.model.node.NodeFilter;
import net.sf.magicmap.core.model.node.NodeIndex;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * <p>
 * Class NodeIndexFactory ZUSAMMENFASSUNG
 * </p>
 * <p>
 * DETAILS
 * </p>
 *
 * @author Jan Friderici
 *         Date: 31.12.2007
 *         Time: 15:51:35
 */
public class NodeIndexFactory {

    private List<NodeIndex> indexList = new LinkedList<NodeIndex>();

    private final INodeModel nodeModel;

    public NodeIndexFactory(INodeModel nodeModel) {
        this.nodeModel = nodeModel;
    }

    /**
     * 
     * @param index
     * @return
     */
    public boolean removeIndex(NodeIndex index) {
        boolean removed = indexList.remove(index);
        if (removed) {
            nodeModel.removeNodeModelListener((NodeModelListener) index);
        }
        return removed;
    }

    /**
     * 
     * @param indexFunction
     * @param filter
     * @return
     */
    public <K> NodeIndex<K> createIndex(KeyGenerator<K> indexFunction, NodeFilter filter) {
        NodeIndex<K> index = new BaseNodeIndex<K>(new WeakHashMap<K, Node>(), filter, indexFunction);
        indexList.add(index);
        nodeModel.addNodeModelListener((NodeModelListener) index);
        return index;
    }
}
