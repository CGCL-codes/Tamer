package org.identifylife.key.editor.gwt.client.modules.taxonomy.gui.tree;

import java.util.ArrayList;
import java.util.List;
import org.identifylife.key.editor.gwt.client.EditorConstants;
import org.identifylife.key.editor.gwt.client.gui.tree.TaxonTree;
import org.identifylife.key.editor.gwt.client.gui.tree.TaxonTreeNode;
import org.identifylife.key.editor.gwt.client.modules.taxonomy.TaxonManager;
import org.identifylife.key.editor.gwt.shared.model.Taxon;
import org.identifylife.key.editor.gwt.shared.model.TaxonRef;
import org.identifylife.key.editor.gwt.shared.model.TaxonSet;
import org.identifylife.key.editor.gwt.shared.service.impl.JsonTaxonParser;
import org.identifylife.key.editor.gwt.shared.utils.TaxonUtils;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;

/**
 * @author dbarnier
 *
 */
public class TaxonTreeGrid extends TreeGrid implements FolderDropHandler, NodeClickHandler {

    private TaxonManager manager;

    private TaxonTree tree;

    public TaxonTreeGrid(TaxonManager manager) {
        this.manager = manager;
        this.tree = manager.getTaxonTree();
        setShowEdges(true);
        setAnimateFolders(false);
        setShowHeader(false);
        setCanReorderRecords(false);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setData(tree);
        setFolderIcon(null);
        setNodeIcon(null);
        addFolderDropHandler(this);
        addNodeClickHandler(this);
    }

    @Override
    public void onFolderDrop(FolderDropEvent event) {
        Log.debug("onFolderDrop(): event: " + event);
        String parentId = null;
        for (TreeNode node : event.getNodes()) {
            if (!(node instanceof TaxonTreeNode)) {
                event.cancel();
            }
            TaxonTreeNode treeNode = (TaxonTreeNode) node;
            String srcParentId = getParentIdFromSrcNode(treeNode);
            if (parentId == null) {
                parentId = srcParentId;
            } else if (!parentId.equals(srcParentId)) {
                event.cancel();
            }
            if (event.isCancelled()) {
                Log.debug("onFolderDrop(): event cancelled...");
                return;
            }
        }
        List<TaxonTreeNode> treeNodes = new ArrayList<TaxonTreeNode>();
        for (TreeNode node : event.getNodes()) {
            TaxonTreeNode treeNode = (TaxonTreeNode) node;
            if (manager.getTaxonStore().hasTaxonMappedToTaxonId(treeNode.getItemId())) {
                continue;
            }
            treeNodes.add(treeNode);
        }
        if (treeNodes.size() == 0) {
            event.cancel();
            return;
        }
        addTaxonNodes(treeNodes);
        event.cancel();
    }

    @Override
    public void onNodeClick(NodeClickEvent event) {
        TreeNode treeNode = event.getNode();
        if (treeNode instanceof TaxonTreeNode) {
            manager.setSelectedNode((TaxonTreeNode) treeNode);
        } else {
            manager.setSelectedNode(null);
        }
    }

    public void addTaxonNodes(List<TaxonTreeNode> treeNodes) {
        TaxonTreeNode rootNode = tree.getRoot();
        int childCount = tree.getChildCount(rootNode);
        if (childCount == 0) {
            for (TaxonTreeNode treeNode : treeNodes) {
                Taxon newTaxon = cloneTaxon(treeNode.getTaxon());
                manager.getTaxonStore().addTaxon(rootNode.getItemId(), newTaxon);
            }
        } else {
            addTaxonNodesToTreeNode(rootNode, treeNodes);
        }
    }

    public void removeTaxonNodes(List<TaxonTreeNode> treeNodes) {
        for (TaxonTreeNode treeNode : treeNodes) {
            TaxonSet ts = manager.getTaxonStore().getTaxonSet(treeNode.getItemId());
            if (ts != null) {
                Log.debug("removeTaxonNodes(): removing taxonSet: " + ts);
                manager.getTaxonStore().removeTaxonSet(ts.getParentId(), true);
            }
            Taxon t = manager.getTaxonStore().getTaxon(treeNode.getItemId());
            if (t != null) {
                Log.debug("removeTaxonNodes(): removing taxon: " + t);
                manager.getTaxonStore().removeTaxon(t);
            }
        }
    }

    private void addTaxonNodesToTreeNode(TaxonTreeNode rootNode, List<TaxonTreeNode> treeNodes) {
        final TaxonTreeNode destNode = (TaxonTreeNode) tree.getChildAt(rootNode, 0);
        final TaxonTreeNode srcNode = treeNodes.get(0);
        if (nodeIsSibling(destNode, srcNode)) {
            Log.info("SIBLING - : " + destNode);
            String parentId = rootNode.getItemId();
            for (TaxonTreeNode treeNode : treeNodes) {
                Taxon t = cloneTaxon(treeNode.getTaxon());
                Log.info("adding taxon: " + t + " to: " + parentId);
                manager.getTaxonStore().addTaxon(parentId, t);
            }
            return;
        }
        List<TaxonRef> srcParents = getParentTaxaFromSrcTree(srcNode);
        for (TreeNode node : tree.getChildren(rootNode)) {
            TaxonTreeNode childNode = (TaxonTreeNode) node;
            if (nodeIsAncestor(srcParents, childNode)) {
                Log.info("ANCESTOR - : " + childNode);
                for (int i = 0; i < srcParents.size(); i++) {
                    if (compareTaxon(srcParents.get(i), childNode.getTaxon())) {
                        int j = i - 1;
                        while (j >= 0) {
                            TaxonRef nodeRef = srcParents.get(j);
                            if (!manager.getTaxonStore().hasTaxonMappedToTaxonId(nodeRef.getRef())) {
                                Taxon t = new Taxon();
                                t.setUuid(manager.getNextTaxonId());
                                t.setName(nodeRef.getName());
                                t.setMappedTo(new TaxonRef(nodeRef.getRef(), nodeRef.getName()));
                                TaxonRef parentRef = srcParents.get(j + 1);
                                String parentId = getTaxonIdFromSrcTaxonRef(parentRef);
                                Log.info("adding taxon: " + t + " to: " + parentId);
                                manager.getTaxonStore().addTaxon(parentId, t);
                            }
                            j--;
                        }
                        break;
                    }
                }
                TaxonRef parentRef = srcParents.get(0);
                String parentId = getTaxonIdFromSrcTaxonRef(parentRef);
                for (TaxonTreeNode treeNode : treeNodes) {
                    Taxon t = cloneTaxon(treeNode.getTaxon());
                    Log.info("adding taxon: " + t + " to: " + parentId);
                    manager.getTaxonStore().addTaxon(parentId, t);
                }
                return;
            }
        }
        if (treeNodes.size() > 1) {
            return;
        }
        String destNodeId = (destNode.getTaxon().getMappedTo() != null) ? destNode.getTaxon().getMappedTo().getRef() : destNode.getItemId();
        manager.getTaxonService().getAncestorsByTaxonId(destNodeId, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String response) {
                List<TaxonRef> parents = new JsonTaxonParser().parseTaxonRefsFromResponse(response);
                if (nodeIsAncestor(parents, srcNode)) {
                    for (int i = 0; i < parents.size(); i++) {
                        if (compareTaxon(parents.get(i), srcNode.getTaxon())) {
                            TaxonSet rs = manager.getTaxonStore().getTaxonSet(EditorConstants.ROOT_ID);
                            TaxonSet ts = new TaxonSet();
                            ts.addTaxa(rs.getTaxa());
                            for (Taxon t : ts.getTaxa()) {
                                manager.getTaxonStore().removeTaxon(t);
                            }
                            tree.removeAll();
                            String parentId = EditorConstants.ROOT_ID;
                            for (int j = i; j >= 0; j--) {
                                TaxonRef nodeRef = parents.get(j);
                                Taxon t = new Taxon();
                                t.setUuid(manager.getNextTaxonId());
                                t.setName(nodeRef.getName());
                                t.setMappedTo(new TaxonRef(nodeRef.getRef(), nodeRef.getName()));
                                manager.getTaxonStore().addTaxon(parentId, t);
                                parentId = t.getUuid();
                            }
                            ts.setParent(new Taxon(parentId));
                            manager.getTaxonStore().addTaxonSet(ts);
                            for (Taxon t : ts.getTaxa()) {
                                manager.getTaxonStore().reloadTaxon(t.getUuid());
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    private String getTaxonIdFromSrcTaxonRef(TaxonRef taxonRef) {
        Taxon taxon = manager.getTaxonStore().getTaxonMappedToTaxonId(taxonRef.getRef());
        if (taxon != null) {
            return taxon.getUuid();
        }
        return null;
    }

    private boolean nodeIsParent(List<TaxonRef> parentList, TaxonTreeNode treeNode) {
        if (parentList != null) {
            String parentId = parentList.get(0).getRef();
            String srcParentId = getParentIdFromSrcNode(treeNode);
            return parentId.equals(srcParentId);
        }
        return false;
    }

    private boolean nodeIsSibling(TaxonTreeNode destNode, TaxonTreeNode srcNode) {
        String destNodeId = destNode.getTaxon().getMappedTo() != null ? destNode.getTaxon().getMappedTo().getRef() : destNode.getTaxon().getUuid();
        String destParentId = getParentIdFromSrcNodeId(destNodeId);
        String srcParentId = getParentIdFromSrcNode(srcNode);
        Log.debug("nodeIsSibling(): destParentId: " + destParentId + ", srcParentId: " + srcParentId);
        if (srcParentId.equals(destParentId)) {
            return true;
        }
        return false;
    }

    private boolean nodeIsDescendent(List<TaxonRef> refList, TaxonTreeNode treeNode) {
        return nodeIsAncestor(refList, treeNode);
    }

    private boolean nodeIsAncestor(List<TaxonRef> refList, TaxonTreeNode treeNode) {
        for (TaxonRef ref : refList) {
            Taxon taxon = treeNode.getTaxon();
            if (compareTaxon(ref, taxon)) {
                return true;
            }
        }
        return false;
    }

    private boolean compareTaxon(TaxonRef taxonRef, Taxon taxon) {
        if (taxon.getMappedTo() != null) {
            if (taxon.getMappedTo().getRef().equals(taxonRef.getRef())) {
                return true;
            }
        } else {
            if (taxon.getUuid().equals(taxonRef.getRef())) {
                return true;
            }
        }
        return false;
    }

    private Taxon cloneTaxon(Taxon taxon) {
        Taxon copy = TaxonUtils.copyOf(taxon);
        copy.setUuid(manager.getNextTaxonId());
        copy.setMappedTo(new TaxonRef(taxon.getUuid(), taxon.getName()));
        copy.setLsid(null);
        copy.setLeaf(false);
        return copy;
    }

    private String getParentIdFromSrcNodeId(String srcNodeId) {
        TaxonTreeNode srcNode = (TaxonTreeNode) manager.getColTree().getTreeNode(srcNodeId);
        if (srcNode != null) {
            return getParentIdFromSrcNode(srcNode);
        }
        return null;
    }

    private String getParentIdFromSrcNode(TaxonTreeNode srcNode) {
        TaxonTreeNode parentNode = (TaxonTreeNode) manager.getColTree().getParent(srcNode);
        if (parentNode != null) {
            return parentNode.getItemId();
        }
        return EditorConstants.ROOT_ID;
    }

    private List<TaxonRef> getParentTaxaFromSrcTree(TaxonTreeNode srcNode) {
        List<TaxonRef> results = new ArrayList<TaxonRef>();
        TreeNode parentNode = manager.getColTree().getParent(srcNode);
        while (parentNode != null) {
            Taxon parentTaxon = ((TaxonTreeNode) parentNode).getTaxon();
            if (parentTaxon.getUuid().equals(EditorConstants.ROOT_ID)) {
                break;
            }
            results.add(new TaxonRef(parentTaxon.getUuid(), parentTaxon.getName()));
            parentNode = manager.getColTree().getParent(parentNode);
        }
        return results;
    }
}
