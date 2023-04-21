package org.plantstreamer.treetable.node;

import java.util.Map.Entry;
import org.plantstreamer.panel.compositeitempanel.CompositeNodeItemElement;
import org.opcda2out.output.database.nodes.PersistentCompositeNodeInfo;

/**
 *
 * @author Joao Leal
 */
public class CompositeTreeTableNodeUtils {

    /**
     * Creates the nodes for the composite items
     *
     * @param itemDataFactory The item data factory to use to get the item data
     *                        for the items in the script
     * @return The composite item nodes
     */
    public static CompositeTreeTableNode[] generateNodes(final PersistentCompositeNodeInfo[] compositeInfo, ItemDataFactory itemDataFactory) {
        if (compositeInfo == null || compositeInfo.length == 0) {
            return new CompositeTreeTableNode[0];
        }
        CompositeTreeTableNode[] compNodes = new CompositeTreeTableNode[compositeInfo.length];
        for (int i = 0; i < compositeInfo.length; i++) {
            PersistentCompositeNodeInfo compInfo = compositeInfo[i];
            CompositeTreeTableNode compNode = new CompositeTreeTableNode(compInfo.name, compInfo.engineHandler, compInfo.dataType, compInfo.script);
            compNodes[i] = compNode;
            CompositeItemElementList elements = compNode.getItemElementList();
            for (Entry<String, String> e : compInfo.alias2id.entrySet()) {
                String id = e.getValue();
                ItemData itemData = itemDataFactory.getItemData(id);
                elements.addElement(new CompositeNodeItemElement(itemData, e.getKey()));
            }
        }
        return compNodes;
    }
}
