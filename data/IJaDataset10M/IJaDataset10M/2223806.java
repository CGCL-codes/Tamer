package org.identifylife.key.player.gwt.client.gxt.tree;

import org.identifylife.key.player.gwt.client.PlayerManager;
import org.identifylife.key.player.gwt.client.PlayerStates;
import com.allen_sauer.gwt.log.client.Log;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;
import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;

/**
 * @author dbarnier
 *
 */
public class TaxonTreeGrid extends TreeGrid {

    public TaxonTreeGrid(PlayerManager playManager) {
        setAnimateFolders(false);
        setShowHeader(false);
        setContextMenu(new TaxonTreeMenu(playManager));
        setData(playManager.getTaxonTree());
        addNodeClickHandler(new NodeClickHandler() {

            @Override
            public void onNodeClick(NodeClickEvent event) {
                Log.debug("onNodeClick: " + event);
                setSelectedTreeNode((TaxonTreeNode) event.getNode());
            }
        });
        addNodeContextClickHandler(new NodeContextClickHandler() {

            @Override
            public void onNodeContextClick(NodeContextClickEvent event) {
                Log.debug("onNodeContextClick: " + event);
                setSelectedTreeNode((TaxonTreeNode) event.getNode());
            }
        });
        this.setFolderIcon(null);
        this.setNodeIcon(null);
    }

    private void setSelectedTreeNode(TaxonTreeNode treeNode) {
        Log.debug("setSelectedNode: " + treeNode);
        PlayerManager.getInstance().getContextManager().setValue(PlayerStates.SELECTED_NODE, treeNode);
        PlayerManager.getInstance().getContextManager().setValue(PlayerStates.SELECTED_TAXON, treeNode.getTaxon());
    }
}
