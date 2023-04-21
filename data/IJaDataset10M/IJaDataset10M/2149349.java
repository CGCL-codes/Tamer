package org.akrogen.tkui.gui.swt.ex.viewers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.IViewerLabelProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

public class TreeCellsViewer extends AbstractTreeViewer {

    /**
	 * TreeColorAndFontCollector is an helper class for color and font support
	 * for trees that support the ITableFontProvider and the
	 * ITableColorProvider.
	 * 
	 * @see ITableColorProvider
	 * @see ITableFontProvider
	 */
    private class TreeColorAndFontCollector {

        ITableFontProvider fontProvider = null;

        ITableColorProvider colorProvider = null;

        /**
		 * Create an instance of the receiver. Set the color and font providers
		 * if provider can be cast to the correct type.
		 * 
		 * @param provider
		 *            IBaseLabelProvider
		 */
        public TreeColorAndFontCollector(IBaseLabelProvider provider) {
            if (provider instanceof ITableFontProvider) {
                fontProvider = (ITableFontProvider) provider;
            }
            if (provider instanceof ITableColorProvider) {
                colorProvider = (ITableColorProvider) provider;
            }
        }

        /**
		 * Create an instance of the receiver with no color and font providers.
		 */
        public TreeColorAndFontCollector() {
        }

        /**
		 * Set the fonts and colors for the treeItem if there is a color and
		 * font provider available.
		 * 
		 * @param treeItem
		 *            The item to update.
		 * @param element
		 *            The element being represented
		 * @param column
		 *            The column index
		 */
        public void setFontsAndColors(TreeItem treeItem, Object element, int column) {
            if (colorProvider != null) {
                treeItem.setBackground(column, colorProvider.getBackground(element, column));
                treeItem.setForeground(column, colorProvider.getForeground(element, column));
            }
            if (fontProvider != null) {
                treeItem.setFont(column, fontProvider.getFont(element, column));
            }
        }
    }

    /**
	 * Internal tree viewer implementation.
	 */
    private TreeEditorImpl treeViewerImpl;

    /**
	 * This viewer's control.
	 */
    private Tree tree;

    /**
	 * This viewer's tree editor.
	 */
    private TreeEditor treeEditor;

    /**
	 * The color and font collector for the cells.
	 */
    private TreeColorAndFontCollector treeColorAndFont = new TreeColorAndFontCollector();

    /**
	 * Flag for whether the tree has been disposed of.
	 */
    private boolean treeIsDisposed = false;

    /**
	 * Creates a tree viewer on a newly-created tree control under the given
	 * parent. The tree control is created using the SWT style bits
	 * <code>MULTI, H_SCROLL, V_SCROLL,</code> and <code>BORDER</code>. The
	 * viewer has no input, no content provider, a default label provider, no
	 * sorter, and no filters.
	 * 
	 * @param parent
	 *            the parent control
	 */
    public TreeCellsViewer(Composite parent) {
        this(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
    }

    /**
	 * Creates a tree viewer on a newly-created tree control under the given
	 * parent. The tree control is created using the given SWT style bits. The
	 * viewer has no input, no content provider, a default label provider, no
	 * sorter, and no filters.
	 * 
	 * @param parent
	 *            the parent control
	 * @param style
	 *            the SWT style bits used to create the tree.
	 */
    public TreeCellsViewer(Composite parent, int style) {
        this(new Tree(parent, style));
    }

    /**
	 * Creates a tree viewer on the given tree control. The viewer has no input,
	 * no content provider, a default label provider, no sorter, and no filters.
	 * 
	 * @param tree
	 *            the tree control
	 */
    public TreeCellsViewer(Tree tree) {
        super();
        this.tree = tree;
        hookControl(tree);
        treeEditor = new TreeEditor(tree);
        initTreeViewerImpl();
    }

    protected void addTreeListener(Control c, TreeListener listener) {
        ((Tree) c).addTreeListener(listener);
    }

    /**
	 * Cancels a currently active cell editor. All changes already done in the
	 * cell editor are lost.
	 * 
	 * @since 3.1
	 */
    public void cancelEditing() {
        treeViewerImpl.cancelEditing();
    }

    protected void doUpdateItem(final Item item, Object element) {
        if (!(item instanceof TreeItem)) {
            return;
        }
        TreeItem treeItem = (TreeItem) item;
        if (treeItem.isDisposed()) {
            unmapElement(element, treeItem);
            return;
        }
        getColorAndFontCollector().setFontsAndColors(element);
        IBaseLabelProvider prov = getLabelProvider();
        ITableLabelProvider tprov = null;
        ILabelProvider lprov = null;
        IViewerLabelProvider vprov = null;
        ITreePathLabelProvider pprov = null;
        if (prov instanceof ILabelProvider) {
            lprov = (ILabelProvider) prov;
        }
        if (prov instanceof IViewerLabelProvider) {
            vprov = (IViewerLabelProvider) prov;
        }
        if (prov instanceof ITableLabelProvider) {
            tprov = (ITableLabelProvider) prov;
        }
        if (prov instanceof ITreePathLabelProvider) {
            pprov = (ITreePathLabelProvider) prov;
        }
        int columnCount = tree.getColumnCount();
        if (columnCount == 0) {
            ViewerLabel updateLabel = new ViewerLabel(treeItem.getText(), treeItem.getImage());
            if (pprov != null) {
                TreePath path = getTreePathFromItem(item);
            } else if (vprov != null) {
            } else {
                if (lprov != null) {
                }
            }
            if (treeItem.isDisposed()) {
                unmapElement(element, treeItem);
                return;
            }
            if (updateLabel.hasNewText()) {
                treeItem.setText(updateLabel.getText());
            }
            if (updateLabel.hasNewImage()) {
                treeItem.setImage(updateLabel.getImage());
            }
        } else {
            for (int column = 0; column < columnCount; column++) {
                String text = "";
                Image image = null;
                treeColorAndFont.setFontsAndColors(treeItem, element, column);
                if (tprov == null) {
                    if (column == 0) {
                        ViewerLabel updateLabel = new ViewerLabel(treeItem.getText(), treeItem.getImage());
                        if (vprov != null) {
                        } else {
                            if (lprov != null) {
                            }
                        }
                        if (treeItem.isDisposed()) {
                            unmapElement(element, treeItem);
                            return;
                        }
                        text = updateLabel.getText();
                        image = updateLabel.getImage();
                    }
                } else {
                    text = tprov.getColumnText(element, column);
                    image = tprov.getColumnImage(element, column);
                }
                if (text == null) {
                    text = "";
                }
                treeItem.setText(column, text);
                if (treeItem.getImage(column) != image) {
                    treeItem.setImage(column, image);
                }
            }
            getColorAndFontCollector().applyFontsAndColors(treeItem);
        }
    }

    /**
	 * Override to handle tree paths.
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#buildLabel(org.eclipse.jface.viewers.ViewerLabel,
	 *      java.lang.Object)
	 */
    protected void buildLabel(ViewerLabel updateLabel, Object elementOrPath) {
        Object element;
        if (elementOrPath instanceof TreePath) {
            TreePath path = (TreePath) elementOrPath;
            IBaseLabelProvider provider = getLabelProvider();
            if (provider instanceof ITreePathLabelProvider) {
                ITreePathLabelProvider pprov = (ITreePathLabelProvider) provider;
                return;
            }
            element = path.getLastSegment();
        } else {
            element = elementOrPath;
        }
        super.buildLabel(updateLabel, element);
    }

    /**
	 * Starts editing the given element.
	 * 
	 * @param element
	 *            the element
	 * @param column
	 *            the column number
	 * @since 3.1
	 */
    public void editElement(Object element, int column) {
        treeViewerImpl.editElement(element, column);
    }

    /**
	 * Returns the cell editors of this tree viewer.
	 * 
	 * @return the list of cell editors
	 * @since 3.1
	 */
    public CellEditor[] getCellEditors() {
        return null;
    }

    /**
	 * Returns the cell modifier of this tree viewer.
	 * 
	 * @return the cell modifier
	 * @since 3.1
	 */
    public ICellModifier getCellModifier() {
        return treeViewerImpl.getCellModifier();
    }

    protected Item[] getChildren(Widget o) {
        if (o instanceof TreeItem) {
            return ((TreeItem) o).getItems();
        }
        if (o instanceof Tree) {
            return ((Tree) o).getItems();
        }
        return null;
    }

    /**
	 * Returns the column properties of this tree viewer. The properties must
	 * correspond with the columns of the tree control. They are used to
	 * identify the column in a cell modifier.
	 * 
	 * @return the list of column properties
	 * @since 3.1
	 */
    public Object[] getColumnProperties() {
        return treeViewerImpl.getColumnProperties();
    }

    public Control getControl() {
        return tree;
    }

    protected boolean getExpanded(Item item) {
        return ((TreeItem) item).getExpanded();
    }

    protected Item getItem(int x, int y) {
        return getTree().getItem(getTree().toControl(new Point(x, y)));
    }

    protected int getItemCount(Control widget) {
        return ((Tree) widget).getItemCount();
    }

    protected int getItemCount(Item item) {
        return ((TreeItem) item).getItemCount();
    }

    protected Item[] getItems(Item item) {
        return ((TreeItem) item).getItems();
    }

    /**
	 * The tree viewer implementation of this <code>Viewer</code> framework
	 * method ensures that the given label provider is an instance of either
	 * <code>ITableLabelProvider</code> or <code>ILabelProvider</code>. If
	 * it is an <code>ITableLabelProvider</code>, then it provides a separate
	 * label text and image for each column. If it is an
	 * <code>ILabelProvider</code>, then it provides only the label text and
	 * image for the first column, and any remaining columns are blank.
	 */
    public IBaseLabelProvider getLabelProvider() {
        return super.getLabelProvider();
    }

    protected Item getParentItem(Item item) {
        return ((TreeItem) item).getParentItem();
    }

    protected Item[] getSelection(Control widget) {
        return ((Tree) widget).getSelection();
    }

    /**
	 * Returns this tree viewer's tree control.
	 * 
	 * @return the tree control
	 */
    public Tree getTree() {
        return tree;
    }

    protected void hookControl(Control control) {
        super.hookControl(control);
        Tree treeControl = (Tree) control;
        treeControl.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                treeViewerImpl.handleMouseDown(e);
            }
        });
        if ((treeControl.getStyle() & SWT.VIRTUAL) != 0) {
            treeControl.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    treeIsDisposed = true;
                    unmapAllElements();
                }
            });
            treeControl.addListener(SWT.SetData, new Listener() {

                public void handleEvent(Event event) {
                    if (getContentProvider() instanceof ILazyTreeContentProvider) {
                        ILazyTreeContentProvider lazyContentProvider = (ILazyTreeContentProvider) getContentProvider();
                        TreeItem item = (TreeItem) event.item;
                        TreeItem parentItem = item.getParentItem();
                        Object parent;
                        int index;
                        if (parentItem != null) {
                            parent = parentItem.getData();
                            index = parentItem.indexOf(item);
                        } else {
                            parent = getInput();
                            index = getTree().indexOf(item);
                        }
                        lazyContentProvider.updateElement(parent, index);
                    }
                }
            });
        }
    }

    /**
	 * Initializes the tree viewer implementation.
	 */
    private void initTreeViewerImpl() {
        treeViewerImpl = new TreeEditorImpl(this) {

            Rectangle getBounds(Item item, int columnNumber) {
                return ((TreeItem) item).getBounds(columnNumber);
            }

            int getColumnCount() {
                return getTree().getColumnCount();
            }

            Item[] getSelection() {
                return getTree().getSelection();
            }

            void setEditor(Control w, Item item, int columnNumber) {
                treeEditor.setEditor(w, (TreeItem) item, columnNumber);
            }

            void setSelection(IStructuredSelection selection, boolean b) {
                TreeCellsViewer.this.setSelection(selection, b);
            }

            void showSelection() {
                getTree().showSelection();
            }

            void setLayoutData(CellEditor.LayoutData layoutData) {
                treeEditor.grabHorizontal = layoutData.grabHorizontal;
                treeEditor.horizontalAlignment = layoutData.horizontalAlignment;
                treeEditor.minimumWidth = layoutData.minimumWidth;
            }

            void handleDoubleClickEvent() {
                Viewer viewer = getViewer();
                fireDoubleClick(new DoubleClickEvent(viewer, viewer.getSelection()));
                fireOpen(new OpenEvent(viewer, viewer.getSelection()));
            }
        };
    }

    /**
	 * Returns whether there is an active cell editor.
	 * 
	 * @return <code>true</code> if there is an active cell editor, and
	 *         <code>false</code> otherwise
	 * @since 3.1
	 */
    public boolean isCellEditorActive() {
        return treeViewerImpl.isCellEditorActive();
    }

    protected Item newItem(Widget parent, int flags, int ix) {
        TreeItem item;
        if (ix >= 0) {
            if (parent instanceof TreeItem) {
                item = new TreeItem((TreeItem) parent, flags, ix);
            } else {
                item = new TreeItem((Tree) parent, flags, ix);
            }
        } else {
            if (parent instanceof TreeItem) {
                item = new TreeItem((TreeItem) parent, flags);
            } else {
                item = new TreeItem((Tree) parent, flags);
            }
        }
        return item;
    }

    protected void removeAll(Control widget) {
        ((Tree) widget).removeAll();
    }

    /**
	 * Sets the cell editors of this tree viewer.
	 * 
	 * @param editors
	 *            the list of cell editors
	 * @since 3.1
	 */
    public void setCellEditors(CellEditor[] editors) {
    }

    /**
	 * Sets the cell modifier of this tree viewer.
	 * 
	 * @param modifier
	 *            the cell modifier
	 * @since 3.1
	 */
    public void setCellModifier(ICellIndexModifier modifier) {
        treeViewerImpl.setCellModifier(modifier);
    }

    /**
	 * Sets the column properties of this tree viewer. The properties must
	 * correspond with the columns of the tree control. They are used to
	 * identify the column in a cell modifier.
	 * 
	 * @param columnProperties
	 *            the list of column properties
	 * @since 3.1
	 */
    public void setColumnProperties(String[] columnProperties) {
        treeViewerImpl.setColumnProperties(columnProperties);
    }

    protected void setExpanded(Item node, boolean expand) {
        ((TreeItem) node).setExpanded(expand);
        if (getContentProvider() instanceof ILazyTreeContentProvider) {
            getControl().update();
        }
    }

    /**
	 * The tree viewer implementation of this <code>Viewer</code> framework
	 * method ensures that the given label provider is an instance of either
	 * <code>ITableLabelProvider</code> or <code>ILabelProvider</code>.
	 * <p>
	 * If the label provider is an {@link ITableLabelProvider}, then it
	 * provides a separate label text and image for each column. Implementers of
	 * <code>ITableLabelProvider</code> may also implement
	 * {@link ITableColorProvider} and/or {@link ITableFontProvider} to provide
	 * colors and/or fonts. Note that the underlying {@link Tree} must be
	 * configured with {@link TreeColumn} objects in this case.
	 * </p>
	 * <p>
	 * If the label provider is an <code>ILabelProvider</code>, then it
	 * provides only the label text and image for the first column, and any
	 * remaining columns are blank. Implementers of <code>ILabelProvider</code>
	 * may also implement {@link IColorProvider} and/or {@link IFontProvider} to
	 * provide colors and/or fonts.
	 * </p>
	 */
    public void setLabelProvider(IBaseLabelProvider labelProvider) {
        Assert.isTrue(labelProvider instanceof ITableLabelProvider || labelProvider instanceof ILabelProvider);
        super.setLabelProvider(labelProvider);
        treeColorAndFont = new TreeColorAndFontCollector(labelProvider);
    }

    protected void setSelection(List items) {
        Item[] current = getSelection(getTree());
        if (isSameSelection(items, current)) {
            return;
        }
        TreeItem[] newItems = new TreeItem[items.size()];
        items.toArray(newItems);
        getTree().setSelection(newItems);
    }

    /**
	 * Returns <code>true</code> if the given list and array of items refer to
	 * the same model elements. Order is unimportant.
	 * 
	 * @param items
	 *            the list of items
	 * @param current
	 *            the array of items
	 * @return <code>true</code> if the refer to the same elements,
	 *         <code>false</code> otherwise
	 * 
	 * @since 3.1
	 */
    protected boolean isSameSelection(List items, Item[] current) {
        int n = items.size();
        if (n != current.length) {
            return false;
        }
        CustomHashtable itemSet = newHashtable(n * 2 + 1);
        for (Iterator i = items.iterator(); i.hasNext(); ) {
            Item item = (Item) i.next();
            Object element = item.getData();
            itemSet.put(element, element);
        }
        for (int i = 0; i < current.length; i++) {
            if (current[i].getData() == null || !itemSet.containsKey(current[i].getData())) {
                return false;
            }
        }
        return true;
    }

    CustomHashtable newHashtable(int capacity) {
        return new CustomHashtable(capacity, getComparer());
    }

    protected void showItem(Item item) {
        getTree().showItem((TreeItem) item);
    }

    protected Item getChild(Widget widget, int index) {
        if (widget instanceof TreeItem) {
            return ((TreeItem) widget).getItem(index);
        }
        if (widget instanceof Tree) {
            return ((Tree) widget).getItem(index);
        }
        return null;
    }

    protected void assertContentProviderType(IContentProvider provider) {
        if (provider instanceof ILazyTreeContentProvider) {
            return;
        }
        super.assertContentProviderType(provider);
    }

    protected Object[] getRawChildren(Object parent) {
        if (getContentProvider() instanceof ILazyTreeContentProvider) {
            return new Object[0];
        }
        return super.getRawChildren(parent);
    }

    /**
	 * For a TreeViewer with a tree with the VIRTUAL style bit set, set the
	 * number of children of the given element. To set the number of children of
	 * the invisible root of the tree, the input object is passed as the
	 * element.
	 * 
	 * @param element
	 * @param count
	 * 
	 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
	 * part of a work in progress. There is no guarantee that this API will
	 * remain unchanged during the 3.2 release cycle. Please do not use this API
	 * without consulting with the Platform/UI team.
	 * </p>
	 * 
	 * @since 3.2
	 */
    public void setChildCount(Object element, int count) {
        Tree tree = (Tree) doFindInputItem(element);
        if (tree != null) {
            tree.setItemCount(count);
            return;
        }
        Widget[] items = findItems(element);
        for (int i = 0; i < items.length; i++) {
            TreeItem treeItem = (TreeItem) items[i];
            treeItem.setItemCount(count);
        }
    }

    /**
	 * For a TreeViewer with a tree with the VIRTUAL style bit set, replace the
	 * given parent's child at index with the given element. If the given parent
	 * is this viewer's input, this will replace the root element at the given
	 * index.
	 * <p>
	 * This method should be called by implementers of ILazyTreeContentProvider
	 * to populate this viewer.
	 * </p>
	 * 
	 * @param parent
	 *            the parent of the element that should be updated
	 * @param index
	 *            the index in the parent's children
	 * @param element
	 *            the new element
	 * 
	 * @see #setChildCount(Object, int)
	 * @see ILazyTreeContentProvider
	 * 
	 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
	 * part of a work in progress. There is no guarantee that this API will
	 * remain unchanged during the 3.2 release cycle. Please do not use this API
	 * without consulting with the Platform/UI team.
	 * </p>
	 * 
	 * @since 3.2
	 */
    public void replace(Object parent, int index, Object element) {
        if (parent.equals(getInput())) {
            if (index < tree.getItemCount()) {
                updateItem(tree.getItem(index), element);
            }
        } else {
            Widget[] parentItems = findItems(parent);
            for (int i = 0; i < parentItems.length; i++) {
                TreeItem parentItem = (TreeItem) parentItems[i];
                if (index < parentItem.getItemCount()) {
                    updateItem(parentItem.getItem(index), element);
                }
            }
        }
    }

    public boolean isExpandable(Object element) {
        if (getContentProvider() instanceof ILazyTreeContentProvider) {
            TreeItem treeItem = (TreeItem) internalExpand(element, false);
            if (treeItem == null) {
                return false;
            }
            virtualMaterializeItem(treeItem);
            return treeItem.getItemCount() > 0;
        }
        return super.isExpandable(element);
    }

    protected Object getParentElement(Object element) {
        if (!(element instanceof TreePath) && (getContentProvider() instanceof ILazyTreeContentProvider)) {
            ILazyTreeContentProvider lazyTreeContentProvider = (ILazyTreeContentProvider) getContentProvider();
            return lazyTreeContentProvider.getParent(element);
        }
        return super.getParentElement(element);
    }

    protected void createChildren(Widget widget) {
        if (getContentProvider() instanceof ILazyTreeContentProvider) {
            final Item[] tis = getChildren(widget);
            if (tis != null && tis.length > 0) {
                for (int i = 0; i < tis.length; i++) {
                    tis[i].getText();
                }
                return;
            }
            ILazyTreeContentProvider lazyTreeContentProvider = (ILazyTreeContentProvider) getContentProvider();
            Object element = widget.getData();
            if (element == null && widget instanceof TreeItem) {
                virtualMaterializeItem((TreeItem) widget);
                element = widget.getData();
            }
            TreeItem[] children;
            if (widget instanceof Tree) {
                children = ((Tree) widget).getItems();
            } else {
                children = ((TreeItem) widget).getItems();
            }
            if (element != null && children.length > 0) {
                for (int i = 0; i < children.length; i++) {
                    lazyTreeContentProvider.updateElement(element, i);
                }
            }
            return;
        }
        super.createChildren(widget);
    }

    protected void internalAdd(Widget widget, Object parentElement, Object[] childElements) {
        if (getContentProvider() instanceof ILazyTreeContentProvider) {
            if (widget instanceof TreeItem) {
                TreeItem ti = (TreeItem) widget;
                int count = ti.getItemCount() + childElements.length;
                ti.setItemCount(count);
                ti.clearAll(false);
            } else {
                Tree t = (Tree) widget;
                t.setItemCount(t.getItemCount() + childElements.length);
                t.clearAll(false);
            }
            return;
        }
        super.internalAdd(widget, parentElement, childElements);
    }

    private void virtualMaterializeItem(TreeItem treeItem) {
        if (treeItem.getData() != null) {
            return;
        }
        if (!(getContentProvider() instanceof ILazyTreeContentProvider)) {
            return;
        }
        ILazyTreeContentProvider lazyTreeContentProvider = (ILazyTreeContentProvider) getContentProvider();
        int index;
        Widget parent = treeItem.getParentItem();
        if (parent == null) {
            parent = treeItem.getParent();
        }
        Object parentElement = parent.getData();
        if (parentElement != null) {
            if (parent instanceof Tree) {
                index = ((Tree) parent).indexOf(treeItem);
            } else {
                index = ((TreeItem) parent).indexOf(treeItem);
            }
            lazyTreeContentProvider.updateElement(parentElement, index);
        }
    }

    protected void internalRefreshStruct(Widget widget, Object element, boolean updateLabels) {
        if (getContentProvider() instanceof ILazyTreeContentProvider) {
            virtualRefreshChildCounts(widget, element);
            if (updateLabels) {
                if (widget instanceof Tree) {
                    ((Tree) widget).clearAll(true);
                } else if (widget instanceof TreeItem) {
                    ((TreeItem) widget).clearAll(true);
                }
            }
            return;
        }
    }

    /**
	 * Traverses the visible (expanded) part of the tree and updates child
	 * counts.
	 * 
	 * @param widget
	 * @param element
	 * @param updateLabels
	 */
    private void virtualRefreshChildCounts(Widget widget, Object element) {
        ILazyTreeContentProvider lazyTreeContentProvider = (ILazyTreeContentProvider) getContentProvider();
        if (widget instanceof Tree || ((TreeItem) widget).getExpanded()) {
            if (element != null) {
                lazyTreeContentProvider.updateChildCount(element, getChildren(widget).length);
            } else {
                if (widget instanceof Tree) {
                    ((Tree) widget).setItemCount(0);
                } else {
                    ((TreeItem) widget).setItemCount(0);
                }
            }
            Item[] items = getChildren(widget);
            for (int i = 0; i < items.length; i++) {
                Item item = items[i];
                Object data = item.getData();
                if (data != null) {
                    virtualRefreshChildCounts(item, data);
                }
            }
        }
    }

    protected void mapElement(Object element, final Widget item) {
        super.mapElement(element, item);
        if ((getTree().getStyle() & SWT.VIRTUAL) != 0) {
            item.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    if (!treeIsDisposed) {
                        Object data = item.getData();
                        if (usingElementMap() && data != null) {
                            unmapElement(data, item);
                        }
                    }
                }
            });
        }
    }

    public void setExpandedElements(Object elements[]) {
        assertElementsNotNull(elements);
        CustomHashtable expandedElements = newHashtable(elements.length * 2 + 1);
        for (int i = 0; i < elements.length; i++) {
            Object element = elements[i];
            internalExpand(element, false);
            expandedElements.put(element, element);
        }
        internalSetExpanded(expandedElements, getControl());
    }

    private void internalSetExpanded(CustomHashtable expandedElements, Widget widget) {
        Item items[] = getChildren(widget);
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            Object data = item.getData();
            if (data != null) {
                boolean expanded = expandedElements.remove(data) != null;
                if (expanded != getExpanded(item)) {
                    if (expanded) createChildren(item);
                    setExpanded(item, expanded);
                }
            }
            if (expandedElements.size() > 0) internalSetExpanded(expandedElements, ((Widget) (item)));
        }
    }

    protected Widget internalExpand(Object elementOrPath, boolean expand) {
        if (elementOrPath == null) return null;
        Widget w = internalGetWidgetToSelect(elementOrPath);
        if (w == null) {
            if (equals(elementOrPath, getRoot())) return null;
            Object parent = getParentElement(elementOrPath);
            if (parent != null) {
                Widget pw = internalExpand(parent, false);
                if (pw != null) {
                    createChildren(pw);
                    Object element = internalToElement(elementOrPath);
                    w = internalFindChild(pw, element);
                    if (expand && (pw instanceof Item)) {
                        Item item = (Item) pw;
                        LinkedList toExpandList = new LinkedList();
                        for (; item != null && !getExpanded(item); item = getParentItem(item)) toExpandList.addFirst(item);
                        Item toExpand;
                        for (Iterator it = toExpandList.iterator(); it.hasNext(); setExpanded(toExpand, true)) toExpand = (Item) it.next();
                    }
                }
            }
        }
        return w;
    }

    private Object internalToElement(Object elementOrPath) {
        if (elementOrPath instanceof TreePath) return ((TreePath) elementOrPath).getLastSegment(); else return elementOrPath;
    }

    private Widget internalFindChild(Widget parent, Object element) {
        Item items[] = getChildren(parent);
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            Object data = item.getData();
            if (data != null && equals(data, element)) return item;
        }
        return null;
    }
}
