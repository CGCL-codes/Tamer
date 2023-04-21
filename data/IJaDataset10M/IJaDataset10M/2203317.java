package net.sf.rcpforms.experimenting.rcp_base.widgets.tree;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sf.rcpforms.experimenting.java.base.CollectionUtil;
import net.sf.rcpforms.experimenting.java.base.ReflectionUtil;
import net.sf.rcpforms.experimenting.model.bean.IJavaBean;
import net.sf.rcpforms.experimenting.rcp_base.event.IRCPTreeInputChangedListener;
import net.sf.rcpforms.experimenting.rcp_base.event.RCPBaseInputChangedEvent;
import net.sf.rcpforms.experimenting.rcp_base.event.RCPTreeInputChangedEvent;
import net.sf.rcpforms.experimenting.rcp_base.tree.ITreeBuilder;
import net.sf.rcpforms.experimenting.rcp_base.tree.TreeNode2;
import net.sf.rcpforms.experimenting.rcp_base.tree.TreeNode3;
import net.sf.rcpforms.experimenting.rcp_base.widgets.table.ETableColumnResizePolicy;
import net.sf.rcpforms.experimenting.rcp_base.widgets.table.IRCPAdvancedViewerColumn;
import net.sf.rcpforms.experimenting.rcp_base.widgets.table.RCPAdvancedTable;
import net.sf.rcpforms.experimenting.rcp_base.widgets.table.RCPAdvancedViewer;
import net.sf.rcpforms.experimenting.rcp_base.widgets.table.helper.AdvancedTableUtil;
import net.sf.rcpforms.experimenting.rcp_base.widgets.tree.columns.AdvColumnConfigUsesLabelProvider;
import net.sf.rcpforms.experimenting.rcp_base.widgets.tree.columns.AdvColumnConfigUsesTreeNode2;
import net.sf.rcpforms.experimenting.rcp_base.widgets.tree.dnd.IRCPTreeModfiyHandler;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * See {@link RCPAdvancedTable}
 * 
 * <p>
 */
public class RCPAdvancedTree<ROW_TYPE extends TreeNode3> extends RCPAdvancedViewer<ROW_TYPE, RCPAdvancedTree<ROW_TYPE>> implements IRCPAdvancedTree<ROW_TYPE, RCPAdvancedTree<ROW_TYPE>> {

    public static final String PROP_AUTO_EXPAND_LEVEL = "autoExpandLevel";

    private ITreeBuilder<? extends TreeNode3> m_treeBuilder = null;

    private int m_autoExpandLevel = 0;

    private boolean m_firstColumnUsesTreeNodes;

    private ILabelProvider m_firstColumnProvider;

    private IRCPTreeModfiyHandler m_treeModifier;

    private final TreeUpdateAdapter m_treeUpdateAdapter = new TreeUpdateAdapter(this);

    public RCPAdvancedTree(final Class<ROW_TYPE> rowClass, final RCPAdvancedTreeColumn<ROW_TYPE>... columnConfigs) {
        super(rowClass, columnConfigs);
        m_firstColumnUsesTreeNodes = false;
        m_firstColumnProvider = null;
    }

    public RCPAdvancedTree(final Class<ROW_TYPE> rowClass, final int style, final RCPAdvancedTreeColumn<ROW_TYPE>... columnConfigs) {
        super(rowClass, style, columnConfigs);
        m_firstColumnUsesTreeNodes = false;
        m_firstColumnProvider = null;
    }

    public RCPAdvancedTree(final Class<ROW_TYPE> rowClass, final ILabelProvider firstColumnProvider, final RCPAdvancedTreeColumn<ROW_TYPE>... columnConfigs) {
        this(rowClass, columnConfigs);
        m_firstColumnProvider = firstColumnProvider;
    }

    public RCPAdvancedTree(final Class<ROW_TYPE> rowClass, final int style, final ILabelProvider firstColumnProvider, final RCPAdvancedTreeColumn<ROW_TYPE>... columnConfigs) {
        this(rowClass, style, columnConfigs);
        m_firstColumnProvider = firstColumnProvider;
    }

    public RCPAdvancedTree(final Class<ROW_TYPE> rowClass, final boolean firstColumnUsesTreeNodes, final RCPAdvancedTreeColumn<ROW_TYPE>... columnConfigs) {
        this(rowClass, columnConfigs);
        m_firstColumnUsesTreeNodes = firstColumnUsesTreeNodes;
    }

    public RCPAdvancedTree(final Class<ROW_TYPE> rowClass, final int style, final boolean firstColumnUsesTreeNodes, final RCPAdvancedTreeColumn<ROW_TYPE>... columnConfigs) {
        this(rowClass, style, reworkColumns(columnConfigs, firstColumnUsesTreeNodes, null));
        m_firstColumnUsesTreeNodes = firstColumnUsesTreeNodes;
    }

    private static RCPAdvancedTreeColumn[] reworkColumns(final RCPAdvancedTreeColumn[] columnConfigs, final boolean firstColumnUsesTreeNodes, final ILabelProvider firstColumnProvider) {
        if (firstColumnUsesTreeNodes || firstColumnProvider != null) {
            final RCPAdvancedTreeColumn[] columnConfigs2 = new RCPAdvancedTreeColumn[columnConfigs.length + 1];
            System.arraycopy(columnConfigs, 0, columnConfigs2, 1, columnConfigs.length);
            if (firstColumnUsesTreeNodes) {
                columnConfigs2[0] = new AdvColumnConfigUsesTreeNode2("", IRCPAdvancedViewerColumn.SPECIAL_PROPERTY_THIS, -1, SWT.DEFAULT);
            } else {
                columnConfigs2[0] = new AdvColumnConfigUsesLabelProvider("", IRCPAdvancedViewerColumn.SPECIAL_PROPERTY_THIS, -1, SWT.DEFAULT, firstColumnProvider);
            }
            return columnConfigs2;
        }
        return columnConfigs;
    }

    @Override
    protected Widget createWrappedWidget(final FormToolkit toolkit) {
        Tree result;
        if (getStyle() == SWT.DEFAULT) {
            result = getFormToolkitEx().createTree(getSWTParent());
        } else {
            result = getFormToolkit().createTree(getSWTParent(), getStyle());
        }
        createViewer(result);
        m_isCheckedTable = (getStyle() & SWT.CHECK) != 0 && m_columnViewer instanceof CheckboxTreeViewer;
        if (m_columnViewer instanceof IRCPAdvancedTreeViewer) {
            final IRCPAdvancedTreeViewer advViewer = (IRCPAdvancedTreeViewer) m_columnViewer;
            advViewer.addTreeInputChangedListener(new IRCPTreeInputChangedListener() {

                @Override
                public void viewerInputChanged(final RCPTreeInputChangedEvent event) {
                    proTreeInputChanged(event);
                }
            });
        }
        ((TreeViewer) m_columnViewer).setAutoExpandLevel(m_autoExpandLevel);
        return result;
    }

    protected void createViewer(final Tree widget) {
        m_columnViewer = getFormToolkitEx().createTreeViewer(widget);
    }

    @Override
    protected final void proViewerInputChanged(final RCPBaseInputChangedEvent event) {
        proTreeInputChanged((RCPTreeInputChangedEvent) event);
    }

    protected final void proTreeInputChanged(final RCPTreeInputChangedEvent event) {
    }

    @Override
    public void setInput(List data) {
        if (m_treeBuilder != null) {
            final TreeNode3[] nodes = m_treeBuilder.createNodes(null, data != null ? data.toArray() : new Object[0]);
            final List<TreeNode3> asList = CollectionUtil.toList(nodes);
            data = asList;
        }
        super.setInput(data);
    }

    @Override
    protected void proSetInput(final List data) {
        if (m_treeBuilder != null) {
            final TreeNode3[] nodes = m_treeBuilder.createNodes(null, data == null ? null : data.toArray());
            final List nodeList = CollectionUtil.toList(nodes);
            wireTreeModels(nodeList);
            super.proSetInput(nodeList);
            linkNodes(nodeList);
        } else {
            wireTreeModels(data);
            super.proSetInput(data);
            linkNodes(data);
        }
        final TreeViewer treeViewer = getTreeViewer();
        if (treeViewer != null) {
            treeViewer.setAutoExpandLevel(getAutoExpandLevel());
            treeViewer.expandToLevel(getAutoExpandLevel());
            notifyTreeExpanded(null);
            doRelayoutColumns();
        }
    }

    @Override
    protected void proReleaseInput(final List currentInput) {
        m_treeUpdateAdapter.unlinkAll();
        if (currentInput == null) {
            return;
        }
        disposeInput(currentInput.toArray());
        super.proReleaseInput(currentInput);
        System.gc();
        System.runFinalization();
    }

    protected void disposeInput(final Object[] array) {
        if (array == null) {
            return;
        }
        for (final Object object : array) {
            if (object instanceof TreeNode3) {
                final TreeNode3 treeNode3 = (TreeNode3) object;
                treeNode3.dispose();
            }
            if (object instanceof TreeNode) {
                disposeInput(((TreeNode) object).getChildren());
            }
        }
    }

    protected void wireTreeModels(final List data) {
        for (final Object object : data) {
            wireTreeModel(object);
        }
    }

    protected void wireTreeModels(final Object... array) {
        for (final Object object : array) {
            wireTreeModel(object);
        }
    }

    Object[] recurseOn = null;

    protected void wireTreeModel(Object object) {
        if (object != null && object instanceof TreeItem) {
            final TreeItem treeItem = (TreeItem) object;
            object = treeItem.getData();
        }
        if (object != null && object instanceof TreeNode) {
            final TreeNode node = (TreeNode) object;
            object = node.getValue();
            recurseOn = node.getChildren();
        }
        if (object != null && object instanceof IJavaBean) {
            m_treeUpdateAdapter.linkTo((IJavaBean) object);
        }
        if (recurseOn != null) {
            wireTreeModels(recurseOn);
        }
    }

    protected void linkNodes(final List asList) {
        TreeUtil.visitTreeItems(getSWTTree(), new ITreeItemVisitor() {

            @Override
            public Object visitItem(final TreeItem node) {
                final Object data = node.getData();
                if (data instanceof TreeNode3) {
                    final TreeNode3 treeNode3 = (TreeNode3) data;
                    treeNode3.linkTo(node);
                    treeNode3.linkTo(RCPAdvancedTree.this);
                }
                return ITreeItemVisitor.CONTINUE;
            }
        });
    }

    @Override
    public void setInput(final Object[] data) {
        super.setInput(data);
    }

    @Override
    public List<ROW_TYPE> getSelection() {
        final ISelection selection = m_columnViewer.getSelection();
        if (selection instanceof ITreeSelection) {
            return TreeUtil.flattenSelection(selection);
        }
        return super.getSelection();
    }

    @Override
    public ITreeBuilder<? extends TreeNode3> getTreeBuilder() {
        return m_treeBuilder;
    }

    @Override
    public RCPAdvancedTree<ROW_TYPE> setTreeBuilder(final ITreeBuilder<? extends TreeNode3> treeBuilder) {
        final boolean update = m_treeBuilder != treeBuilder && treeBuilder != null;
        m_treeBuilder = treeBuilder;
        if (update) {
            final List<ROW_TYPE> input = getInput();
            setInput(input);
        }
        return this;
    }

    @Override
    public IRCPTreeModfiyHandler getTreeModifier() {
        return m_treeModifier;
    }

    @Override
    public RCPAdvancedTree setTreeModifier(final IRCPTreeModfiyHandler treeModifier) {
        m_treeModifier = treeModifier;
        return this;
    }

    @Override
    public IRCPAdvancedTreeColumn<ROW_TYPE, ? extends IRCPAdvancedTreeColumn>[] getIColumns() {
        return ReflectionUtil.castArray(IRCPAdvancedTreeColumn.class, super.getIColumns(), false);
    }

    @Override
    protected Item getItemAtIndex(final int rowIndex) {
        return getSWTTree().getItem(rowIndex);
    }

    @Override
    public Item getItemAtPixelY(final int pixelY) {
        return AdvancedTableUtil.getPixelCoordYToItem(this, pixelY);
    }

    @Override
    public ROW_TYPE getModelAt(final int mouseY) {
        final TreeItem item = AdvancedTableUtil.getPixelCoordYToItem(this, mouseY);
        return item == null ? null : (ROW_TYPE) item.getData();
    }

    @Override
    public int getItemCount() {
        return getSWTTree().getItemCount();
    }

    @Override
    public boolean getLinesVisible() {
        return getSWTTree().getLinesVisible();
    }

    @Override
    public int getSelectedCount() {
        return getSWTTree().getSelectionCount();
    }

    @Override
    protected void repaintAll() {
        getTreeViewer().refresh();
    }

    @Override
    protected void setLinesVisible_noEv(final boolean linesVisible) {
        getSWTTree().setLinesVisible(linesVisible);
    }

    @Override
    public Tree getSWTTree() {
        return getTypedWidget();
    }

    @Override
    protected void postSpawnConfigureTableAndViewer() {
        final AdvancedTableUtil util = AdvancedTableUtil.newInstance();
        util.configureTreeViewer(this, getColumns(), false, true, false, null);
        triggerColumnRelayout(ETableColumnResizePolicy.PACK_INITIALLY);
    }

    @Override
    public final RCPAdvancedTreeColumn<ROW_TYPE>[] getColumns() {
        return (RCPAdvancedTreeColumn<ROW_TYPE>[]) m_columnConfigs;
    }

    @Override
    public TreeViewer getTreeViewer() {
        return (TreeViewer) m_columnViewer;
    }

    @Override
    public ContentViewer getViewer() {
        return getTreeViewer();
    }

    @Override
    protected void subUnlinkBeanSupport() {
        final Tree swtTree = getSWTTree();
        if (m_selectionListener != null) {
            swtTree.removeSelectionListener(m_selectionListener);
        }
        super.subUnlinkBeanSupport();
    }

    @Override
    protected void postSpawnInitBeanSupport() {
        super.postSpawnInitBeanSupport();
        final Tree swtTree = getSWTTree();
        if (swtTree != null && !swtTree.isDisposed()) {
            m_selectionListener = new SelectionListener() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    proWidgetSelected0(e);
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                }
            };
            swtTree.addSelectionListener(m_selectionListener);
        }
    }

    public int getIndexOf(final ROW_TYPE model) {
        return AdvancedTableUtil.getIndexOfTreeModel(getTreeViewer(), model);
    }

    /**
	 * <h3 style='color: #D22'><span style='font-size: 18pt'>NON-API</span>, not intended to be called by user!</h3> 
	* <p>
	* <ul><li><i>Is called by internal tree-listener.</i></li></ul>
	* <p>
	 */
    public void notifyTreeExpanded(final TreeEvent e) {
        computePreferedColumnWidth(1);
    }

    /**
	 * <h3 style='color: #D22'><span style='font-size: 18pt'>NON-API</span>, not intended to be called by user!</h3> 
	* <p>
	* <ul><li><i>Is called by internal tree-listener</i></li></ul>
	* <p>
	 */
    public void notifyTreeCollapsed(final TreeEvent e) {
        computePreferedColumnWidth(1);
    }

    @Override
    public void onDispose() {
        final TreeItem[] items = getSWTTree().getItems();
        final ArrayList input = new ArrayList(items.length);
        for (int i = 0; i < items.length; i++) {
            input.add(items[i].getData());
        }
        proReleaseInput(input);
        super.onDispose();
    }

    public void beanChanged(final PropertyChangeEvent evt) {
        if (!hasSpawned()) {
            return;
        }
        final Object model = evt.getSource();
        final TreeNode2 node2update = TreeUtil.findNode(getSWTTree(), model);
        if (node2update != null && node2update instanceof TreeNode3) {
            updateNode3((TreeNode3) node2update, evt.getPropertyName());
        }
    }

    /** <h3 style='color: #D22'><span style='font-size: 18pt'>NON-API</span>, not intended to be called by user!</h3> 
	* <p>
	* <ul><li><i>Is called by TreeNode3 on property-changes of its {@link TreeNode3#getObject() model}.</i></li></ul>
	* <p>*/
    @SuppressWarnings("null")
    public void updateNode3(final TreeNode3 node2Update, final String... properties) {
        final TreeViewer treeViewer = getTreeViewer();
        final ITreeBuilder treeBuilder = getTreeBuilder();
        if (treeBuilder == null) {
            if (treeViewer != null) {
                treeViewer.update(this, properties.length == 0 ? null : properties);
            }
            return;
        }
        boolean needsRefresh = properties == null || properties.length == 0;
        if (!needsRefresh) {
            for (final String property : properties) {
                if (treeBuilder.needsRefreshOnProperty(property)) {
                    needsRefresh = true;
                    break;
                }
            }
        }
        if (needsRefresh) {
            final TreeNode3 parent = (TreeNode3) node2Update.getParent();
            Set expandedModels = null;
            if (parent != null) {
                final TreeItem parentItem = TreeUtil.findItem(getTreeViewer(), parent);
                expandedModels = TreeUtil.getExpandedModels(true, parentItem);
                final TreeNode3 newSubtree = treeBuilder.createNode(parent, node2Update.getObject());
                TreeUtil.replaceTreeNode(treeViewer, parent, node2Update, newSubtree);
                wireTreeModel(newSubtree);
            } else {
                final List<TreeNode3> input = (List<TreeNode3>) getInput();
                final TreeItem item = TreeUtil.findItem(getTreeViewer(), node2Update);
                expandedModels = TreeUtil.getExpandedModels(true, item);
                final TreeNode3 newSubtree = treeBuilder.createNode(parent, node2Update.getObject());
                input.remove(node2Update);
                node2Update.dispose();
                input.add(newSubtree);
                treeViewer.refresh(newSubtree, true);
                wireTreeModel(newSubtree);
            }
            TreeUtil.setExpandedStateRecursively(expandedModels, false, treeViewer);
        }
    }

    public int getAutoExpandLevel() {
        return m_autoExpandLevel;
    }

    public void setAutoExpandLevel(final int autoExpandLevel) {
        final Object oldValue = this.m_autoExpandLevel;
        this.m_autoExpandLevel = autoExpandLevel;
        if (!hasSpawned()) {
            addSpawnHook4Property(PROP_AUTO_EXPAND_LEVEL, autoExpandLevel);
        } else {
            ((TreeViewer) getViewer()).setAutoExpandLevel(m_autoExpandLevel);
        }
        firePropertyChange(PROP_AUTO_EXPAND_LEVEL, oldValue, this.m_autoExpandLevel);
    }

    public RCPAdvancedTree<ROW_TYPE> set2AutoExpandLevel(final int autoExpandLevel) {
        setAutoExpandLevel(autoExpandLevel);
        return this;
    }
}
