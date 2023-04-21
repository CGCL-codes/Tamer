package net.sf.refactorit.ui.tree;

import net.sf.refactorit.classmodel.BinPackage;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.CompilationUnit;
import net.sf.refactorit.options.GlobalOptions;
import net.sf.refactorit.ui.module.ModuleManager;
import net.sf.refactorit.ui.module.RefactorItAction;
import net.sf.refactorit.ui.module.RefactorItActionUtils;
import net.sf.refactorit.ui.module.RefactorItContext;
import net.sf.refactorit.ui.panel.ResultArea;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class MultilineRowTree extends BinTree {

    final RefactorItContext context;

    private ListenerExposingModel listenerExposingModel;

    public MultilineRowTree(RefactorItContext context) {
        super();
        this.context = context;
        addMouseListener();
        optionsChanged();
    }

    private void makeCellRendererSetRowHeight() {
        setRowHeight(0);
    }

    public final void setModel(TreeModel model) {
        this.listenerExposingModel = new ListenerExposingModel(model);
        model = this.listenerExposingModel;
        setCellRenderer(null);
        super.setModel(model);
        setRootVisible(true);
        setShowsRootHandles(false);
        makeCellRendererSetRowHeight();
        setCellRenderer(new MultilineTextRenderer(model));
    }

    public final Iterator getModelListeners() {
        return this.listenerExposingModel.getListeners();
    }

    public final TreeModel getOriginalDelegateModel() {
        return this.listenerExposingModel.getDelegate();
    }

    private String getIdForNode(int row) {
        return this.getPathForRow(row).getLastPathComponent().toString();
    }

    private final HashMap expanded = new HashMap();

    public final void rememberExpansions() {
        this.expanded.clear();
        for (int row = 0; row < this.getRowCount(); row++) {
            if (getIdForNode(row) != null) {
                this.expanded.put(getIdForNode(row), new Boolean(this.isExpanded(row)));
            }
        }
    }

    public final void restoreExpansions() {
        for (int row = 0; row < this.getRowCount(); row++) {
            Boolean wasExpanded = (Boolean) this.expanded.get(getIdForNode(row));
            if (wasExpanded != null && wasExpanded.booleanValue()) {
                this.expandRow(row);
            } else {
                this.collapseRow(row);
            }
        }
    }

    private String selectedNodeId;

    private boolean somethingSelected() {
        return this.getMinSelectionRow() >= 0;
    }

    public final void rememberSelection() {
        if (somethingSelected()) {
            this.selectedNodeId = getIdForNode(this.getMinSelectionRow());
        } else {
            this.selectedNodeId = null;
        }
    }

    public final void restoreSelection() {
        if (this.selectedNodeId != null) {
            for (int row = 0; row < this.getRowCount(); row++) {
                if (getIdForNode(row).equals(this.selectedNodeId)) {
                    this.setSelectionRow(row);
                }
            }
        }
    }

    private final ActionListener keyListener = new KeyListener();

    final class KeyListener implements ActionListener {

        public final void actionPerformed(ActionEvent e) {
            int row;
            try {
                row = getSelectionRows()[0];
                if (row == 0) {
                    return;
                }
            } catch (ArrayIndexOutOfBoundsException exx) {
                return;
            }
            TreePath path = getPathForRow(row);
            if (path == null) {
                return;
            }
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (!(node.getUserObject() instanceof TitledValue)) {
                return;
            }
            TitledValue value = (TitledValue) node.getUserObject();
            Rectangle rect = getRowBounds(row);
            JPopupMenu popup = createPopup(value.getReferencedObject());
            if (popup != null) {
                popup.show(MultilineRowTree.this, (int) rect.getCenterX(), (int) rect.getCenterY());
                popup.requestFocus();
            }
        }
    }

    final JPopupMenu createPopup(Object o) {
        JPopupMenu result = ResultArea.createPopupForBinItem(replaceCompilationUnitWithItsFirstType(o), this, context, Collections.EMPTY_LIST);
        return (result.getSubElements().length > 0) ? result : null;
    }

    final void registerKeys(final Object o) {
        final Object obj = replaceCompilationUnitWithItsFirstType(o);
        resetKeyboardActions();
        try {
            registerKeyboardAction(keyListener, KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        } catch (Exception e) {
        }
        List actions = ModuleManager.getActions(obj);
        if (actions == null || actions.size() == 0) {
            return;
        }
        for (int i = 0; i < actions.size(); i++) {
            final RefactorItAction act = (RefactorItAction) actions.get(i);
            ActionListener actListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (RefactorItActionUtils.run(act, context, obj)) {
                        act.updateEnvironment(context);
                    } else {
                        act.raiseResultsPane(context);
                    }
                }
            };
            try {
                registerKeyboardAction(actListener, act.getKeyStroke(), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            } catch (Exception e) {
            }
        }
    }

    private Object replaceCompilationUnitWithItsFirstType(Object obj) {
        if (obj instanceof CompilationUnit) {
            CompilationUnit sf = (CompilationUnit) obj;
            List types = sf.getDefinedTypes();
            if (types.size() == 0) {
                return obj;
            }
            BinTypeRef firstType = (BinTypeRef) types.get(0);
            return firstType.getBinType();
        }
        return obj;
    }

    private void addMouseListener() {
        addMouseListener(new FooMouseListener(this));
        addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                if (path == null) {
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (!(node.getUserObject() instanceof TitledValue)) {
                    return;
                }
                TitledValue value = (TitledValue) node.getUserObject();
                registerKeys(value.getReferencedObject());
            }
        });
    }

    public final MultilineRowTree getThis() {
        return this;
    }

    private final class FooMouseListener extends MouseAdapter {

        private final JTree tree;

        public FooMouseListener(JTree tree) {
            this.tree = tree;
        }

        public final void mouseClicked(MouseEvent e) {
            handlePopup(e);
        }

        public final void mousePressed(MouseEvent e) {
            handlePopup(e);
        }

        public final void mouseReleased(MouseEvent e) {
            handlePopup(e);
        }

        public final void handlePopup(MouseEvent e) {
            if (!e.isPopupTrigger()) {
                return;
            }
            int row = getRowForLocation((int) e.getPoint().getX(), (int) e.getPoint().getY());
            TreePath path = getPathForRow(row);
            if (path == null) {
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (!(node.getUserObject() instanceof TitledValue)) {
                return;
            }
            TitledValue value = (TitledValue) node.getUserObject();
            JPopupMenu popup = createPopup(value.getReferencedObject());
            if (popup != null) {
                popup.show(this.tree, e.getX(), e.getY());
                popup.requestFocus();
            }
        }
    }

    private static final class MultilineTextRenderer implements TreeCellRenderer {

        final HashMap selectedNodeToRenderer = new HashMap();

        final HashMap notSelectedNodeToRenderer = new HashMap();

        final HashMap nodeLeafChildrenCounts = new HashMap();

        final List leafRenderers = new ArrayList();

        public MultilineTextRenderer(TreeModel treeModel) {
            countLeafChildrenForEachNode(treeModel);
            buildMapOfRenderers(treeModel);
        }

        private void countLeafChildrenForEachNode(TreeModel treeModel) {
            countLeafChildrenForEachNode(treeModel, treeModel.getRoot());
        }

        private int countLeafChildrenForEachNode(TreeModel treeModel, Object node) {
            int childCount = 0;
            for (int i = 0; i < treeModel.getChildCount(node); i++) {
                Object child = treeModel.getChild(node, i);
                if (treeModel.isLeaf(child)) {
                    childCount++;
                } else {
                    childCount += countLeafChildrenForEachNode(treeModel, child);
                }
            }
            this.nodeLeafChildrenCounts.put(node, new Integer(childCount));
            return childCount;
        }

        public final void setWidthOfEachLeafRenderer(int width) {
            for (Iterator i = this.leafRenderers.iterator(); i.hasNext(); ) {
                FixedWidthComponent area = (FixedWidthComponent) i.next();
                area.setWidth(width);
            }
        }

        private void buildMapOfRenderers(TreeModel treeModel) {
            buildMapOfRenderers(treeModel, treeModel.getRoot(), null);
        }

        private void buildMapOfRenderers(TreeModel treeModel, Object node, Object parent) {
            for (int i = 0; i < treeModel.getChildCount(node); i++) {
                Object child = treeModel.getChild(node, i);
                buildMapOfRenderers(treeModel, child, node);
            }
            buildMapOfRenderersForNode(treeModel, node, parent);
        }

        private void buildMapOfRenderersForNode(TreeModel treeModel, Object value, Object parent) {
            boolean isLeaf = treeModel.isLeaf(value);
            JComponent selectedNodeRenderer = getRenderer(value, parent, isLeaf, treeModel);
            selectedNodeRenderer.setOpaque(true);
            this.selectedNodeToRenderer.put(value, selectedNodeRenderer);
            JComponent notSelectedNodeRenderer = getRenderer(value, parent, isLeaf, treeModel);
            notSelectedNodeRenderer.setOpaque(true);
            this.notSelectedNodeToRenderer.put(value, notSelectedNodeRenderer);
            if (isLeaf) {
                this.leafRenderers.add(selectedNodeRenderer);
                this.leafRenderers.add(notSelectedNodeRenderer);
            }
        }

        private JComponent getRenderer(Object value, Object parent, boolean isLeaf, TreeModel treeModel) {
            String title;
            if (isLeaf) {
                title = value.toString();
            } else {
                title = value.toString() + " (" + this.nodeLeafChildrenCounts.get(value) + ")";
            }
            if (treeModel.getRoot() == value) {
                title += "     ";
            }
            JComponent result = new FixedWidthTextArea(title);
            if (isLeaf) {
                if (isLastSibling(value, parent, treeModel)) {
                    result.setBorder(TOP_AND_BOTTOM);
                } else {
                    result.setBorder(TOP_ONLY);
                }
            }
            Icon icon = getIcon(value, true);
            if (icon == null) {
                return result;
            } else {
                return combine(icon, result, title);
            }
        }

        public final Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Object renderer;
            if (selected) {
                renderer = this.selectedNodeToRenderer.get(value);
            } else {
                renderer = this.notSelectedNodeToRenderer.get(value);
            }
            if (renderer != null) {
                JComponent r = (JComponent) renderer;
                r.setFont(Font.decode(GlobalOptions.getOption("tree.font")));
                if (selected) {
                    r.setBackground(Color.decode(GlobalOptions.getOption("tree.selection.background")));
                    r.setForeground(Color.decode(GlobalOptions.getOption("tree.selection.foreground")));
                } else {
                    r.setBackground(Color.decode(GlobalOptions.getOption("tree.background")));
                    r.setForeground(Color.decode(GlobalOptions.getOption("tree.foreground")));
                }
            }
            if (renderer == null) {
                throw new NullPointerException("Renderer not found for \"" + value + "\" (selected: " + selected + ")");
            }
            return (Component) renderer;
        }

        private boolean isLastSibling(Object value, Object parent, TreeModel model) {
            if (parent == null) {
                return true;
            } else {
                return model.getIndexOfChild(parent, value) == model.getChildCount(parent) - 1;
            }
        }

        private JComponent combine(final Icon icon, final JComponent title, final String titleString) {
            final JLabel iconLabel = new JLabel(" ", icon, JLabel.LEFT);
            JPanel result = new JPanel() {

                public void setFont(Font f) {
                    super.setFont(f);
                    for (int i = 0; i < this.getComponentCount(); i++) {
                        this.getComponent(i).setFont(f);
                    }
                }

                public void setForeground(Color c) {
                    super.setForeground(c);
                    for (int i = 0; i < this.getComponentCount(); i++) {
                        this.getComponent(i).setForeground(c);
                    }
                }

                public void setBackground(Color c) {
                    super.setBackground(c);
                    for (int i = 0; i < this.getComponentCount(); i++) {
                        this.getComponent(i).setBackground(c);
                    }
                }
            };
            result.setLayout(new BorderLayout());
            result.add(iconLabel, BorderLayout.WEST);
            result.setOpaque(false);
            title.setOpaque(false);
            result.add(title, BorderLayout.CENTER);
            return result;
        }

        private Icon getIcon(Object value, boolean expanded) {
            value = ((DefaultMutableTreeNode) value).getUserObject();
            if (!(value instanceof TitledValue)) {
                return null;
            }
            Object referencedObject = ((TitledValue) value).getReferencedObject();
            if (referencedObject instanceof BinPackage) {
                return NodeIcons.getNodeIcons().getPackageIcon(expanded);
            }
            if (referencedObject instanceof CompilationUnit) {
                return NodeIcons.getNodeIcons().getClassIcon(NodeIcons.DEFAULT);
            }
            return null;
        }

        private static final Border TOP_AND_BOTTOM = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(204, 204, 255));

        private static final Border TOP_ONLY = BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(204, 204, 255));

        private final class FixedWidthTextArea extends JTextArea implements FixedWidthComponent {

            private int width;

            public FixedWidthTextArea(String text) {
                super(text);
                setFont(Font.decode(GlobalOptions.getOption("tree.font")));
                setWidth(super.getPreferredSize().width);
            }

            public final Dimension getMinimumSize() {
                return new Dimension(this.width, super.getMinimumSize().height);
            }

            public final Dimension getRealMinimumSize() {
                return super.getMinimumSize();
            }

            public final void setWidth(int width) {
                this.width = width;
            }
        }
    }

    private interface FixedWidthComponent {

        Dimension getRealMinimumSize();

        void setWidth(int width);

        void setFont(Font f);
    }

    public final void requestFocus() {
        super.requestFocus();
        setSelectionInterval(0, 0);
    }

    private static final class ListenerExposingModel implements TreeModel {

        private final TreeModel delegate;

        private final java.util.List listeners = new ArrayList();

        public ListenerExposingModel(TreeModel toDecorate) {
            this.delegate = toDecorate;
        }

        public final void addTreeModelListener(javax.swing.event.TreeModelListener treeModelListener) {
            this.listeners.add(treeModelListener);
            this.delegate.addTreeModelListener(treeModelListener);
        }

        public final Object getChild(Object obj, int param) {
            return this.delegate.getChild(obj, param);
        }

        public final int getChildCount(Object obj) {
            return this.delegate.getChildCount(obj);
        }

        public final int getIndexOfChild(Object obj, Object obj1) {
            return this.delegate.getIndexOfChild(obj, obj1);
        }

        public final Object getRoot() {
            return this.delegate.getRoot();
        }

        public final boolean isLeaf(Object obj) {
            return this.delegate.isLeaf(obj);
        }

        public final void removeTreeModelListener(javax.swing.event.TreeModelListener treeModelListener) {
            this.delegate.removeTreeModelListener(treeModelListener);
            this.listeners.remove(treeModelListener);
        }

        public final void valueForPathChanged(javax.swing.tree.TreePath treePath, Object obj) {
            this.delegate.valueForPathChanged(treePath, obj);
        }

        public final Iterator getListeners() {
            return this.listeners.iterator();
        }

        public final TreeModel getDelegate() {
            return this.delegate;
        }
    }
}
