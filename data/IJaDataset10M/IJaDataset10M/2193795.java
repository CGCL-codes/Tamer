package org.identifylife.key.editor.gwt.client.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.identifylife.key.editor.gwt.client.EditorActions;
import org.identifylife.key.editor.gwt.client.EditorConstants;
import org.identifylife.key.editor.gwt.client.EditorContexts;
import org.identifylife.key.editor.gwt.client.EditorManager;
import org.identifylife.key.editor.gwt.client.context.Context;
import org.identifylife.key.editor.gwt.client.context.ContextChangeListener;
import org.identifylife.key.editor.gwt.client.gui.grid.TreeGridPanel;
import org.identifylife.key.editor.gwt.client.gui.tree.FeatureTree;
import org.identifylife.key.editor.gwt.client.gui.tree.FeatureTreeGrid;
import org.identifylife.key.editor.gwt.client.gui.tree.FeatureTreeNode;
import org.identifylife.key.editor.gwt.client.gui.tree.TaxonTree;
import org.identifylife.key.editor.gwt.client.gui.tree.TaxonTreeGrid;
import org.identifylife.key.editor.gwt.client.gui.tree.TaxonTreeNode;
import org.identifylife.key.editor.gwt.client.rpc.TaskCallback;
import org.identifylife.key.editor.gwt.client.tasks.LoadStateScoresTask;
import org.identifylife.key.editor.gwt.client.tasks.TaskRunner;
import org.identifylife.key.editor.gwt.shared.lang.ModelMap;
import org.identifylife.key.editor.gwt.shared.model.ItemType;
import org.identifylife.key.editor.gwt.shared.model.Taxa;
import org.identifylife.key.editor.gwt.shared.model.Taxon;
import org.identifylife.key.editor.gwt.shared.model.TaxonSet;
import org.identifylife.key.editor.gwt.shared.score.Score;
import org.identifylife.key.editor.gwt.shared.score.ScoreValue;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.KeyPressEvent;
import com.smartgwt.client.widgets.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClosedEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;

/**
 * TaxaScorePanel
 * 
 * mode: score taxa for states
 * 
 * @author dbarnier
 *
 */
public class TaxaScorePanel extends ScorePanel implements ContextChangeListener {

    private FeatureTree featureTree;

    private TaxonTree absentTree;

    private TaxonTree presentTree;

    private TaxonTree uncertainTree;

    private TaxonTree unscoredTree;

    private AbsentTreeGrid absentTreeGrid;

    private PresentTreeGrid presentTreeGrid;

    private UncertainTreeGrid uncertainTreeGrid;

    private UnscoredTreeGrid unscoredTreeGrid;

    private Map<String, Set<String>> expandMap = new HashMap<String, Set<String>>();

    private String stateId;

    private boolean active;

    protected static final boolean INHERIT_SCORES = true;

    public TaxaScorePanel() {
        super(ItemType.STATE);
        initModel();
        initLayout();
        registerListeners();
    }

    private void initModel() {
        featureTree = EditorManager.getInstance().getEditorKey().getMsFeatureTree();
        absentTree = new TaxonTree("absent");
        presentTree = new TaxonTree("present");
        uncertainTree = new TaxonTree("uncertain");
        unscoredTree = new TaxonTree("unscored");
        expandMap.put(absentTree.getTreeId(), new HashSet<String>());
        expandMap.put(presentTree.getTreeId(), new HashSet<String>());
        expandMap.put(uncertainTree.getTreeId(), new HashSet<String>());
        expandMap.put(unscoredTree.getTreeId(), new HashSet<String>());
    }

    private void initLayout() {
        FeatureTreeGrid featureGrid = new FeatureTreeGrid(featureTree);
        featureGrid.setSelectionType(SelectionStyle.SINGLE);
        featureGrid.setWidth100();
        featureGrid.setHeight100();
        absentTreeGrid = new AbsentTreeGrid(absentTree);
        absentTreeGrid.setHeight100();
        absentTreeGrid.setWidth100();
        presentTreeGrid = new PresentTreeGrid(presentTree);
        presentTreeGrid.setHeight100();
        presentTreeGrid.setWidth100();
        uncertainTreeGrid = new UncertainTreeGrid(uncertainTree);
        uncertainTreeGrid.setHeight100();
        uncertainTreeGrid.setWidth100();
        unscoredTreeGrid = new UnscoredTreeGrid(unscoredTree);
        unscoredTreeGrid.setHeight100();
        unscoredTreeGrid.setWidth100();
        unscoredTreeGrid.setBodyBackgroundColor("#DEDEDE");
        unscoredTreeGrid.setAlternateRecordStyles(false);
        HLayout scoreLayout = new HLayout();
        scoreLayout.setMargin(5);
        scoreLayout.setMembersMargin(5);
        scoreLayout.setWidth100();
        scoreLayout.setHeight100();
        scoreLayout.addMember(new TreeGridPanel(unscoredTreeGrid, "Unscored"));
        scoreLayout.addMember(new PresentTreePanel(presentTreeGrid, "Yes", "images/Present_20.png"));
        scoreLayout.addMember(new TreeGridPanel(absentTreeGrid, "No", "images/Absent_20.png"));
        scoreLayout.addMember(new TreeGridPanel(uncertainTreeGrid, "Unsure", "images/Uncertain_20.png"));
        FeatureGridPanel featurePanel = new FeatureGridPanel(featureGrid, "Features");
        featurePanel.setMargin(5);
        VLayout mainLayout = new VLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight100();
        mainLayout.addMember(featurePanel);
        mainLayout.addMember(scoreLayout);
        setPadding(5);
        setWidth100();
        setHeight100();
        addMember(mainLayout);
    }

    private void registerListeners() {
        EditorManager.getInstance().getContextManager().registerListener(EditorContexts.SELECTED_NODE, this);
        EditorManager.getInstance().getContextManager().registerListener(EditorContexts.SELECTED_VIEW, this);
    }

    @Override
    public void contextChanged(Context context, Object value) {
        Log.debug("TaxonScorePanel.contextChanged(): " + context.getName() + ": " + value);
        if (EditorContexts.SELECTED_NODE.equals(context.getName()) && isActive()) {
            if (value instanceof FeatureTreeNode) {
                FeatureTreeNode fsItemNode = (FeatureTreeNode) value;
                if (fsItemNode.getItemType() != ItemType.STATE) {
                    setSelectedState(null);
                } else {
                    setSelectedState(fsItemNode.getItemId());
                }
            }
        } else if (EditorContexts.SELECTED_VIEW.equals(context.getName())) {
            boolean active = EditorConstants.TAXA_VIEW.equalsIgnoreCase(value.toString());
            if (isActive() && !active) {
                removeTreeData();
                if (!cacheScoreData && !sandbox) {
                    removeScoreData();
                }
            }
            setActive(active);
            if (isActive()) {
                setSelectedState(stateId);
            }
        }
    }

    private void setSelectedState(String stateId) {
        this.stateId = stateId;
        removeTreeData();
        if (stateId == null) {
            return;
        }
        List<Taxon> taxa = getOrderedTaxa(false);
        Taxa taxaToFetch = new Taxa();
        if (!getScoreMap().hasRow(stateId)) {
            taxaToFetch.addTaxa(taxa);
        } else {
            for (Taxon t : taxa) {
                if (!hasScore(stateId, t.getUuid())) {
                    taxaToFetch.addTaxon(t);
                }
            }
        }
        if (taxaToFetch.getTaxonCount() > 0) {
            ModelMap model = new ModelMap();
            model.put("stateId", stateId);
            model.put("taxa", taxaToFetch);
            TaskRunner runner = new TaskRunner(EditorActions.LOAD_STATE_SCORES);
            runner.setModel(model);
            runner.setCallback(new LoadStateScoresCallback());
            runner.execute(new LoadStateScoresTask());
        } else {
            updateTreeData(taxa);
        }
    }

    private void updateTreeData(List<Taxon> taxa) {
        for (Taxon taxon : taxa) {
            Score score = getScore(stateId, taxon.getUuid());
            switch(score.getValue()) {
                case ScoreValue.PRESENT:
                case ScoreValue.MISINT:
                case ScoreValue.RARE:
                    presentTreeGrid.addTaxonToTree(taxon);
                    break;
                case ScoreValue.ABSENT:
                    absentTreeGrid.addTaxonToTree(taxon);
                    break;
                case ScoreValue.UNCERTAIN:
                    uncertainTreeGrid.addTaxonToTree(taxon);
                    break;
                case ScoreValue.UNSCORED:
                default:
                    unscoredTreeGrid.addTaxonToTree(taxon);
            }
        }
        TaxonTreeNode rootTaxon = unscoredTree.getRoot();
        if (unscoredTree.getChildCount(rootTaxon) == 1) {
            TreeNode childNode = unscoredTree.getChildren(rootTaxon)[0];
            unscoredTree.openFolder(childNode);
        }
        restoreExpandSet(unscoredTree);
        restoreExpandSet(presentTree);
        restoreExpandSet(absentTree);
        restoreExpandSet(uncertainTree);
    }

    private void removeTaxonFromTaxonTree(TaxonTree tree, Taxon taxon) {
        if (tree == presentTree) {
            presentTreeGrid.removeTaxonFromTree(taxon);
        }
        if (tree == absentTree) {
            absentTreeGrid.removeTaxonFromTree(taxon);
        } else if (tree == uncertainTree) {
            uncertainTreeGrid.removeTaxonFromTree(taxon);
        } else if (tree == unscoredTree) {
            unscoredTreeGrid.removeTaxonFromTree(taxon);
        } else {
            Log.error("removeTaxonFromTaxonTree(): unknown tree: " + tree);
        }
    }

    @Override
    protected void onSetScore(String stateId, String taxonId, Score score) {
    }

    private TaxonTreeGrid getTreeGrid(TaxonTree tree) {
        if ("present".equals(tree.getTreeId())) {
            return presentTreeGrid;
        } else if ("absent".equals(tree.getTreeId())) {
            return absentTreeGrid;
        } else if ("uncertain".equals(tree.getTreeId())) {
            return uncertainTreeGrid;
        }
        return unscoredTreeGrid;
    }

    private TaxonTree getTreeFromScore(byte scoreValue) {
        switch(scoreValue) {
            case ScoreValue.PRESENT:
                return presentTree;
            case ScoreValue.ABSENT:
                return absentTree;
            case ScoreValue.UNCERTAIN:
                return uncertainTree;
            case ScoreValue.UNSCORED:
            default:
                return unscoredTree;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private void removeTreeData() {
        Log.debug("TaxaScorePanel.removeTreeData(): removing tree data...");
        absentTree.removeAll();
        presentTree.removeAll();
        uncertainTree.removeAll();
        unscoredTree.removeAll();
    }

    private void removeScoreData() {
        Log.debug("TaxaScorePanel.removeScoreData(): removing score data...");
        getScoreMap().clear();
    }

    private void restoreExpandSet(TaxonTree taxonTree) {
        Iterator<String> nodeIdIter = expandMap.get(taxonTree.getTreeId()).iterator();
        while (nodeIdIter.hasNext()) {
            String nodeId = nodeIdIter.next();
            if (taxonTree.hasTreeNode(nodeId)) {
                taxonTree.expandNode(nodeId);
            }
        }
    }

    private void updateIcon(TaxonTree tree, Taxon taxon) {
        TaxonTreeNode treeNode = (TaxonTreeNode) tree.getTreeNode(taxon.getUuid());
        if (treeNode == null) {
            Log.error("null treeNode for taxon: " + taxon);
            return;
        }
        ((ScoreableTreeNode) treeNode).updateIcon();
        markForRedraw("update_icon");
    }

    public class PresentTreeGrid extends TaxonTreeGrid implements FolderDropHandler, FolderOpenedHandler, FolderClosedHandler {

        private byte scoreValue = ScoreValue.PRESENT;

        public PresentTreeGrid(TaxonTree taxonTree) {
            super(taxonTree);
            this.setCanAcceptDroppedRecords(true);
            this.setCanDragRecordsOut(true);
            addFolderDropHandler(this);
            addFolderOpenedHandler(this);
            addFolderClosedHandler(this);
        }

        @Override
        public void onFolderDrop(FolderDropEvent event) {
            String parentId = null;
            for (TreeNode node : event.getNodes()) {
                if (!(node instanceof TaxonTreeNode)) {
                    Log.debug("onFolderDrop(): node !instanceof TaxonTreeNode, cancel..");
                    event.cancel();
                } else {
                    TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                    if (parentId == null) {
                        parentId = getParentIdForTaxonId(taxonNode.getItemId());
                    } else {
                        if (!parentId.equals(getParentIdForTaxonId(taxonNode.getItemId()))) {
                            Log.debug("onFolderDrop(): nodes not from same parent, cancel..");
                            event.cancel();
                        }
                    }
                }
                if (event.isCancelled()) {
                    return;
                }
            }
            try {
                for (TreeNode node : event.getNodes()) {
                    TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                    Taxon taxon = (Taxon) taxonNode.getItem();
                    String taxonId = taxon.getUuid();
                    Score oldScore = TaxaScorePanel.this.getScore(stateId, taxonId);
                    setScore(stateId, taxonId, new Score(scoreValue));
                    addTaxonToTree(taxon);
                    removeTaxonFromTaxonTree(getTreeFromScore(oldScore.getValue()), taxon);
                }
                presentTree.expandNode(parentId);
            } finally {
                event.cancel();
            }
        }

        public byte getScoreValue() {
            return scoreValue;
        }

        public void setScoreValue(byte scoreValue) {
            this.scoreValue = scoreValue;
        }

        @Override
        public void onFolderOpened(FolderOpenedEvent event) {
            TaxonTreeNode taxonNode = (TaxonTreeNode) event.getNode();
            expandMap.get(getTreeId()).add(taxonNode.getNodeId());
        }

        @Override
        public void onFolderClosed(FolderClosedEvent event) {
            TaxonTreeNode taxonNode = (TaxonTreeNode) event.getNode();
            expandMap.get(getTreeId()).remove(taxonNode.getNodeId());
        }

        protected void addTaxonToTree(Taxon taxon) {
            if (!taxonTree.hasTreeNode(taxon.getUuid())) {
                TaxonTreeNode taxonNode = new PresentTreeNode(taxon);
                Log.debug("addTaxonToTree(): adding taxonNode: " + taxonNode);
                addTaxonNodeToTree(taxonNode);
            } else {
                updateIcon(taxonTree, taxon);
            }
        }

        private void addTaxonNodeToTree(TaxonTreeNode taxonNode) {
            String parentId = getParentIdForTaxonId(taxonNode.getItemId());
            if (parentId == null) {
                Log.error("null parentId for taxonNode: " + taxonNode);
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) taxonTree.getTreeNode(parentId);
            if (parentNode == null) {
                Taxon parent = getEditorKey().getTaxon(parentId);
                parentNode = new PresentTreeNode(parent);
                addTaxonNodeToTree(parentNode);
            }
            taxonTree.add(taxonNode, parentNode);
        }

        protected void removeTaxonFromTree(Taxon taxon) {
            Log.debug("removeTaxonFromTree(): taxon: " + taxon);
            if (!taxonTree.hasTreeNode(taxon.getUuid())) {
                Log.warn("removeTaxonFromTree(): expected node for taxon: " + taxon.getUuid());
                return;
            }
            TaxonTreeNode treeNode = (TaxonTreeNode) taxonTree.getTreeNode(taxon.getUuid());
            Score score = getScore(stateId, taxon.getUuid());
            if (score.isValueSet() && (score.getValue() == ScoreValue.PRESENT || score.getValue() == ScoreValue.MISINT || score.getValue() == ScoreValue.RARE)) {
                Log.warn("removeTaxonFromTree(): node scored for tree, can't remove..");
                return;
            }
            if (taxonTree.getChildCount(treeNode) > 0) {
                Log.warn("removeTaxonFromTree(): node has children, can't remove..");
                ((ScoreableTreeNode) treeNode).updateIcon();
                getTreeGrid(taxonTree).markForRedraw("update_icon");
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) taxonTree.getParent(treeNode);
            Log.debug("removeTaxonFromTree(): removing treeNode: " + treeNode);
            taxonTree.remove(treeNode);
            if (taxonTree.getChildCount(parentNode) == 0) {
                removeTaxonFromTree(parentNode.getTaxon());
            }
        }
    }

    public class AbsentTreeGrid extends TaxonTreeGrid implements FolderClosedHandler, FolderDropHandler, FolderOpenedHandler {

        public AbsentTreeGrid(TaxonTree taxonTree) {
            super(absentTree);
            this.setCanAcceptDroppedRecords(true);
            this.setCanDragRecordsOut(true);
            addFolderDropHandler(this);
            addFolderOpenedHandler(this);
            addFolderClosedHandler(this);
        }

        @Override
        public void onFolderDrop(FolderDropEvent event) {
            String parentId = null;
            for (TreeNode node : event.getNodes()) {
                if (!(node instanceof TaxonTreeNode)) {
                    Log.debug("onFolderDrop(): node !instanceof TaxonTreeNode, cancel..");
                    event.cancel();
                } else {
                    TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                    if (parentId == null) {
                        parentId = getParentIdForTaxonId(taxonNode.getItemId());
                    } else {
                        if (!parentId.equals(getParentIdForTaxonId(taxonNode.getItemId()))) {
                            Log.debug("onFolderDrop(): nodes not from same parent, cancel..");
                            event.cancel();
                        }
                    }
                }
                if (event.isCancelled()) {
                    return;
                }
            }
            try {
                startBatch();
                for (TreeNode node : event.getNodes()) {
                    TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                    Taxon taxon = (Taxon) taxonNode.getItem();
                    String taxonId = taxon.getUuid();
                    Score newScore = new Score(ScoreValue.ABSENT);
                    Score oldScore = TaxaScorePanel.this.getScore(stateId, taxonId);
                    setScore(stateId, taxonId, newScore);
                    addTaxonToTree(taxon);
                    if (INHERIT_SCORES) {
                        if (!taxon.isLeaf()) {
                            addDescendantTaxa(taxon);
                        }
                    }
                    removeTaxonFromTaxonTree(getTreeFromScore(oldScore.getValue()), taxon);
                }
                absentTree.expandNode(parentId);
            } finally {
                endBatch();
                event.cancel();
            }
        }

        @Override
        public void onFolderOpened(FolderOpenedEvent event) {
            TaxonTreeNode taxonNode = (TaxonTreeNode) event.getNode();
            expandMap.get(getTreeId()).add(taxonNode.getNodeId());
        }

        @Override
        public void onFolderClosed(FolderClosedEvent event) {
            TaxonTreeNode taxonNode = (TaxonTreeNode) event.getNode();
            expandMap.get(getTreeId()).remove(taxonNode.getNodeId());
        }

        protected void addTaxonToTree(Taxon taxon) {
            if (!taxonTree.hasTreeNode(taxon.getUuid())) {
                TaxonTreeNode taxonNode = new AbsentTreeNode(taxon);
                Log.debug("addTaxonToTree(): adding taxonNode: " + taxonNode);
                addTaxonNodeToTree(taxonNode);
            } else {
                updateIcon(taxonTree, taxon);
            }
        }

        private void addTaxonNodeToTree(TaxonTreeNode taxonNode) {
            String parentId = getParentIdForTaxonId(taxonNode.getItemId());
            if (parentId == null) {
                Log.error("null parentId for taxonNode: " + taxonNode);
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) taxonTree.getTreeNode(parentId);
            if (parentNode == null) {
                Taxon parent = getEditorKey().getTaxon(parentId);
                parentNode = new AbsentTreeNode(parent);
                addTaxonNodeToTree(parentNode);
            }
            taxonTree.add(taxonNode, parentNode);
        }

        protected void removeTaxonFromTree(Taxon taxon) {
            Log.debug("removeTaxonFromTree(): taxon: " + taxon);
            if (!absentTree.hasTreeNode(taxon.getUuid())) {
                Log.warn("removeTaxonFromTree(): expected node for taxon: " + taxon.getUuid());
                return;
            }
            TaxonTreeNode treeNode = (TaxonTreeNode) absentTree.getTreeNode(taxon.getUuid());
            Score score = getScore(stateId, taxon.getUuid());
            if (score.isValueSet() && score.getValue() == ScoreValue.ABSENT) {
                Log.warn("removeTaxonFromTree(): node scored for tree, can't remove..");
                return;
            }
            if (absentTree.getChildCount(treeNode) > 0) {
                Log.warn("removeTaxonFromTree(): node required for tree, removing icon..");
                ((AbsentTreeNode) treeNode).updateIcon();
                markForRedraw("icon_change");
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) absentTree.getParent(treeNode);
            Log.debug("removeNodeFromTaxonTree(): removing treeNode: " + treeNode);
            absentTree.remove(treeNode);
            if (absentTree.getChildCount(parentNode) == 0) {
                removeTaxonFromTree(parentNode.getTaxon());
            }
        }

        protected void addDescendantTaxa(Taxon taxon) {
            Log.debug("addDescendantTaxa(): taxon: " + taxon);
            TaxonSet ts = getEditorKey().getTaxonSet(taxon.getUuid());
            if (ts == null || ts.getTaxonCount() == 0) {
                return;
            }
            for (Taxon child : ts.getTaxa()) {
                String childId = child.getUuid();
                if (absentTree.hasTreeNode(childId)) {
                    continue;
                }
                Score oldScore = getScore(stateId, childId);
                Log.debug("addDescendantTaxa(): taxonId: " + childId + ", oldScore: " + oldScore);
                if (!oldScore.isValueSet()) {
                    Log.debug("addDescendantTaxa(): setting absent score");
                    setScore(stateId, childId, new Score(ScoreValue.ABSENT));
                } else if (oldScore.getValue() != ScoreValue.ABSENT) {
                    Log.warn("addDescendantTaxa(): clearing old score: " + oldScore.getValue());
                    setScore(stateId, childId, new Score(ScoreValue.ABSENT));
                }
                addTaxonToTree(child);
                if (!child.isLeaf()) {
                    addDescendantTaxa(child);
                }
                removeTaxonFromTaxonTree(getTreeFromScore(oldScore.getValue()), child);
            }
        }
    }

    public class UncertainTreeGrid extends TaxonTreeGrid implements FolderDropHandler, FolderOpenedHandler, FolderClosedHandler {

        public UncertainTreeGrid(TaxonTree taxonTree) {
            super(taxonTree);
            this.setCanAcceptDroppedRecords(true);
            this.setCanDragRecordsOut(true);
            this.addFolderDropHandler(this);
            this.addFolderOpenedHandler(this);
            this.addFolderClosedHandler(this);
        }

        @Override
        public void onFolderDrop(FolderDropEvent event) {
            String parentId = null;
            for (TreeNode node : event.getNodes()) {
                if (!(node instanceof TaxonTreeNode)) {
                    Log.debug("onFolderDrop(): node !instanceof TaxonTreeNode, cancel..");
                    event.cancel();
                } else {
                    TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                    if (parentId == null) {
                        parentId = getParentIdForTaxonId(taxonNode.getItemId());
                    } else {
                        if (!parentId.equals(getParentIdForTaxonId(taxonNode.getItemId()))) {
                            Log.debug("onFolderDrop(): nodes not from same parent, cancel..");
                            event.cancel();
                        }
                    }
                }
                if (event.isCancelled()) {
                    return;
                }
            }
            try {
                for (TreeNode node : event.getNodes()) {
                    TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                    Taxon taxon = (Taxon) taxonNode.getItem();
                    String taxonId = taxon.getUuid();
                    Score oldScore = TaxaScorePanel.this.getScore(stateId, taxonId);
                    setScore(stateId, taxonId, new Score(ScoreValue.UNCERTAIN));
                    addTaxonToTree(taxon);
                    removeTaxonFromTaxonTree(getTreeFromScore(oldScore.getValue()), taxon);
                }
                uncertainTree.expandNode(parentId);
            } finally {
                event.cancel();
            }
        }

        @Override
        public void onFolderOpened(FolderOpenedEvent event) {
            TaxonTreeNode taxonNode = (TaxonTreeNode) event.getNode();
            expandMap.get(getTreeId()).add(taxonNode.getNodeId());
        }

        @Override
        public void onFolderClosed(FolderClosedEvent event) {
            TaxonTreeNode taxonNode = (TaxonTreeNode) event.getNode();
            expandMap.get(getTreeId()).remove(taxonNode.getNodeId());
        }

        protected void addTaxonToTree(Taxon taxon) {
            if (!taxonTree.hasTreeNode(taxon.getUuid())) {
                TaxonTreeNode taxonNode = new UncertainTreeNode(taxon);
                Log.debug("addTaxonToTree(): adding taxonNode: " + taxonNode);
                addTaxonNodeToTree(taxonNode);
            } else {
                updateIcon(taxonTree, taxon);
            }
        }

        private void addTaxonNodeToTree(TaxonTreeNode taxonNode) {
            String parentId = getParentIdForTaxonId(taxonNode.getItemId());
            if (parentId == null) {
                Log.error("null parentId for taxonNode: " + taxonNode);
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) taxonTree.getTreeNode(parentId);
            if (parentNode == null) {
                Taxon parent = getEditorKey().getTaxon(parentId);
                parentNode = new UncertainTreeNode(parent);
                addTaxonNodeToTree(parentNode);
            }
            taxonTree.add(taxonNode, parentNode);
        }

        protected void removeTaxonFromTree(Taxon taxon) {
            Log.debug("removeTaxonFromTree(): taxon: " + taxon);
            if (!taxonTree.hasTreeNode(taxon.getUuid())) {
                Log.warn("removeTaxonFromTree(): expected node for taxon: " + taxon.getUuid());
                return;
            }
            TaxonTreeNode treeNode = (TaxonTreeNode) taxonTree.getTreeNode(taxon.getUuid());
            Score score = getScore(stateId, taxon.getUuid());
            if (score.isValueSet() && score.getValue() == ScoreValue.UNCERTAIN) {
                Log.warn("removeTaxonFromTree(): node scored for tree, can't remove..");
                return;
            }
            if (taxonTree.getChildCount(treeNode) > 0) {
                Log.warn("removeTaxonFromTree(): node has children, can't remove..");
                ((ScoreableTreeNode) treeNode).updateIcon();
                getTreeGrid(taxonTree).markForRedraw("update_icon");
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) taxonTree.getParent(treeNode);
            Log.debug("removeTaxonFromTree(): removing treeNode: " + treeNode);
            taxonTree.remove(treeNode);
            if (taxonTree.getChildCount(parentNode) == 0) {
                removeTaxonFromTree(parentNode.getTaxon());
            }
        }
    }

    public class UnscoredTreeGrid extends TaxonTreeGrid {

        public UnscoredTreeGrid(TaxonTree taxonTree) {
            super(taxonTree);
            this.setCanAcceptDroppedRecords(true);
            this.setCanDragRecordsOut(true);
            this.addKeyPressHandler(new KeyPressHandler() {

                @Override
                public void onKeyPress(KeyPressEvent event) {
                    TreeGrid destGrid = getTreeGridFromKeyEvent(event);
                    if (destGrid == null) {
                        return;
                    }
                    Log.debug("onKeyPress(): destGrid: " + destGrid);
                    destGrid.transferSelectedData(unscoredTreeGrid);
                }

                private TreeGrid getTreeGridFromKeyEvent(KeyPressEvent event) {
                    if (event.isCtrlKeyDown() || event.isAltKeyDown()) {
                        return null;
                    }
                    if ("y".equalsIgnoreCase(event.getKeyName())) {
                        return presentTreeGrid;
                    } else if ("n".equalsIgnoreCase(event.getKeyName())) {
                        return absentTreeGrid;
                    } else if ("?".equalsIgnoreCase(event.getKeyName()) || "/".equalsIgnoreCase(event.getKeyName()) || "u".equalsIgnoreCase(event.getKeyName())) {
                        return uncertainTreeGrid;
                    }
                    Log.debug("unexpected key: " + event.getKeyName());
                    return null;
                }
            });
            this.addFolderDropHandler(new FolderDropHandler() {

                @Override
                public void onFolderDrop(FolderDropEvent event) {
                    for (TreeNode node : event.getNodes()) {
                        String parentId = null;
                        if (!(node instanceof TaxonTreeNode)) {
                            Log.debug("onFolderDrop(): node !instanceof TaxonTreeNode, cancel..");
                            event.cancel();
                        } else {
                            TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                            if (parentId == null) {
                                parentId = getParentIdForTaxonId(taxonNode.getItemId());
                            } else {
                                if (!parentId.equals(getParentIdForTaxonId(taxonNode.getItemId()))) {
                                    Log.debug("onFolderDrop(): nodes not from same parent, cancel..");
                                    event.cancel();
                                }
                            }
                        }
                        if (event.isCancelled()) {
                            return;
                        }
                    }
                    try {
                        for (TreeNode node : event.getNodes()) {
                            TaxonTreeNode taxonNode = (TaxonTreeNode) node;
                            Taxon taxon = (Taxon) taxonNode.getItem();
                            String taxonId = taxon.getUuid();
                            Score oldScore = TaxaScorePanel.this.getScore(stateId, taxonId);
                            if (!oldScore.isValueSet()) {
                                continue;
                            } else {
                                setScore(stateId, taxonId, new Score(ScoreValue.UNSCORED));
                                addTaxonToTree(taxon);
                                removeTaxonFromTaxonTree(getTreeFromScore(oldScore.getValue()), taxon);
                            }
                        }
                    } finally {
                        event.cancel();
                    }
                }
            });
        }

        protected void addTaxonToTree(Taxon taxon) {
            if (!taxonTree.hasTreeNode(taxon.getUuid())) {
                TaxonTreeNode taxonNode = new TaxonTreeNode(taxon);
                Log.debug("addTaxonToTree(): adding taxonNode: " + taxonNode);
                addTaxonNodeToTree(taxonNode);
            }
        }

        private void addTaxonNodeToTree(TaxonTreeNode taxonNode) {
            String parentId = getParentIdForTaxonId(taxonNode.getItemId());
            if (parentId == null) {
                Log.error("null parentId for taxonNode: " + taxonNode);
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) taxonTree.getTreeNode(parentId);
            if (parentNode == null) {
                Taxon parent = getEditorKey().getTaxon(parentId);
                parentNode = new TaxonTreeNode(parent);
                addTaxonNodeToTree(parentNode);
            }
            taxonTree.add(taxonNode, parentNode);
        }

        protected void removeTaxonFromTree(Taxon taxon) {
            Log.debug("removeTaxonFromTree(): taxon: " + taxon);
            if (!taxonTree.hasTreeNode(taxon.getUuid())) {
                Log.warn("removeTaxonFromTree(): expected node for taxon: " + taxon.getUuid());
                return;
            }
            TaxonTreeNode treeNode = (TaxonTreeNode) taxonTree.getTreeNode(taxon.getUuid());
            if (taxonTree.getChildCount(treeNode) > 0) {
                Log.warn("removeTaxonFromTree(): node has children, can't remove..");
                return;
            }
            TaxonTreeNode parentNode = (TaxonTreeNode) taxonTree.getParent(treeNode);
            Log.debug("removeTaxonFromTree(): removing treeNode: " + treeNode);
            taxonTree.remove(treeNode);
            if (taxonTree.getChildCount(parentNode) == 0) {
                removeTaxonFromTree(parentNode.getTaxon());
            }
        }
    }

    public class FeatureGridPanel extends TreeGridPanel implements ContextChangeListener {

        FeatureTreeGrid treeGrid;

        public FeatureGridPanel(FeatureTreeGrid treeGrid, String title) {
            super(treeGrid, title);
            this.treeGrid = treeGrid;
            this.setShowResizeBar(true);
            EditorManager.getInstance().getContextManager().registerListener(EditorContexts.SELECTED_VIEW, this);
        }

        @Override
        public void contextChanged(Context context, Object value) {
            if (EditorContexts.SELECTED_VIEW.equals(context.getName())) {
                if (EditorConstants.TAXA_VIEW.equalsIgnoreCase(value.toString())) {
                    FeatureTreeNode treeNode = (FeatureTreeNode) treeGrid.getSelectedRecord();
                    treeGrid.setSelectedTreeNode(treeNode);
                }
            }
        }
    }

    private String getScoreButton(byte value) {
        return GWT.getModuleBaseURL() + "images/" + getScoreString(value) + "_button.png";
    }

    private String getScoreIcon(byte value) {
        switch(value) {
            case ScoreValue.ABSENT:
            case ScoreValue.UNCERTAIN:
                return GWT.getModuleBaseURL() + "images/" + getScoreString(value) + "_16_nb.png";
            default:
                return GWT.getModuleBaseURL() + "images/" + getScoreString(value) + "_16.png";
        }
    }

    private String getScoreString(byte value) {
        switch(value) {
            case ScoreValue.PRESENT:
                return "Present";
            case ScoreValue.MISINT:
                return "Misint";
            case ScoreValue.RARE:
                return "Rare";
            case ScoreValue.ABSENT:
                return "Absent";
            case ScoreValue.UNCERTAIN:
                return "Uncertain";
            case ScoreValue.UNSCORED:
            default:
                return "Unscored";
        }
    }

    public class PresentTreePanel extends TreeGridPanel implements NodeClickHandler {

        private PresentTreeGrid treeGrid;

        public PresentTreePanel(PresentTreeGrid treeGrid, String title, String iconUrl) {
            super(treeGrid, title, iconUrl);
            this.treeGrid = treeGrid;
            VLayout buttonLayout = new VLayout();
            buttonLayout.setMargin(3);
            buttonLayout.setAlign(VerticalAlignment.TOP);
            buttonLayout.setWidth(20);
            buttonLayout.setHeight100();
            buttonLayout.setMembersMargin(1);
            buttonLayout.addMember(new ScoreButton(ScoreValue.PRESENT));
            buttonLayout.addMember(new ScoreButton(ScoreValue.MISINT));
            buttonLayout.addMember(new ScoreButton(ScoreValue.RARE));
            mainLayout.addMember(buttonLayout);
            treeGrid.addNodeClickHandler(this);
        }

        @Override
        public void onNodeClick(NodeClickEvent event) {
            TreeNode leafNode = event.getNode();
            if (leafNode instanceof PresentTreeNode) {
                PresentTreeNode treeNode = (PresentTreeNode) leafNode;
                byte oldScore = getScore(stateId, treeNode.getItemId()).getValue();
                if (oldScore != getScoreValue()) {
                    setScore(stateId, treeNode.getItemId(), new Score(getScoreValue()));
                    treeNode.updateIcon();
                    treeGrid.markForRedraw("update_icon");
                }
            }
        }

        public byte getScoreValue() {
            return treeGrid.getScoreValue();
        }

        public void setScoreValue(byte scoreValue) {
            treeGrid.setScoreValue(scoreValue);
        }

        private class ScoreButton extends ImgButton implements ClickHandler {

            protected byte scoreValue;

            public ScoreButton(byte scoreValue) {
                this.scoreValue = scoreValue;
                setWidth(18);
                setHeight(18);
                setShowDown(false);
                setShowRollOver(false);
                setSrc(getScoreButton(scoreValue));
                setRadioGroup("scoreValue");
                setActionType(SelectionType.RADIO);
                addClickHandler(this);
                setSelected(getScoreValue() == scoreValue);
            }

            @Override
            public void onClick(ClickEvent event) {
                setScoreValue(scoreValue);
            }
        }
    }

    abstract class ScoreableTreeNode extends TaxonTreeNode {

        public ScoreableTreeNode(Taxon taxon) {
            super(taxon);
            updateIcon();
        }

        abstract void updateIcon();
    }

    public class PresentTreeNode extends ScoreableTreeNode {

        public PresentTreeNode(Taxon taxon) {
            super(taxon);
        }

        @Override
        public void updateIcon() {
            setIcon(getScoreIcon(getScore(stateId, getItemId()).getValue()));
        }
    }

    public class AbsentTreeNode extends ScoreableTreeNode {

        public AbsentTreeNode(Taxon taxon) {
            super(taxon);
        }

        @Override
        public void updateIcon() {
            if (getScore(stateId, getItemId()).getValue() == ScoreValue.ABSENT) {
                setIcon(getScoreIcon(ScoreValue.ABSENT));
            } else {
                setIcon(null);
            }
        }
    }

    public class UncertainTreeNode extends ScoreableTreeNode {

        public UncertainTreeNode(Taxon taxon) {
            super(taxon);
        }

        @Override
        public void updateIcon() {
            if (getScore(stateId, getItemId()).getValue() == ScoreValue.UNCERTAIN) {
                setIcon(getScoreIcon(ScoreValue.UNCERTAIN));
            } else {
                setIcon(null);
            }
        }
    }

    private class LoadStateScoresCallback implements TaskCallback {

        public void onSuccess(ModelMap model) {
            List<Taxon> taxa = getOrderedTaxa(false);
            updateTreeData(taxa);
        }

        public void onFailure(Throwable caught) {
        }
    }
}
