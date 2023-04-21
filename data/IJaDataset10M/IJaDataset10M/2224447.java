package org.deft.gui.widgets;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A composite which contains a TreeView and a Text(field) with autocomplete functionality.
 * This composite is used to select an item of hierarchical data, either by selecting it in the
 * TreeView or by typing it into the Text. TreeView and Text are synchronized, i.e. selecting
 * an item in one of them makes the other update its view.  
 */
public abstract class TreeAndAutoCompleteTextfieldComposite extends Composite {

    private TreeViewer treeViewer;

    private Tree tree;

    private Text text;

    private HierarchyDataAutoCompleteField autoComplete;

    private SelectedItemContainer siContainer;

    private boolean executingListeners = false;

    private List<SelectedItemContainerListener> selectedItemListeners = new LinkedList<SelectedItemContainerListener>();

    public TreeAndAutoCompleteTextfieldComposite(Composite parent, int style) {
        super(parent, style);
    }

    /**
	 * Creates the contents of the composite.
	 * This method must be manually called by subclasses.
	 */
    protected void createContents() {
        setLayout(new GridLayout(1, true));
        treeViewer = new TreeViewer(this, SWT.BORDER);
        tree = treeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        text = new Text(this, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        autoComplete = new HierarchyDataAutoCompleteField(text);
        treeViewer.setContentProvider(createContentProvider());
        treeViewer.setLabelProvider(createLabelProvider());
        addListeners();
    }

    public void setGuiData() {
        Object treeViewerInput = getInput();
        if (treeViewerInput != null) {
            treeViewer.setInput(getInput());
        }
        String[] proposals = getAllProposals();
        if (proposals != null) {
            autoComplete.setProposals(proposals);
        }
    }

    public TreeViewer getTreeViewer() {
        return treeViewer;
    }

    public Text getText() {
        return text;
    }

    public void addSelectedItemContainerListener(SelectedItemContainerListener listener) {
        selectedItemListeners.add(listener);
        if (siContainer != null) {
            siContainer.addSelectedItemContainerListener(listener);
        }
    }

    protected abstract String[] getAllProposals();

    protected abstract SelectedItemContainer createSelectedItemContainer();

    protected abstract IContentProvider createContentProvider();

    protected abstract ILabelProvider createLabelProvider();

    protected abstract Object getInput();

    protected SelectedItemContainer getSelectedItemContainer() {
        if (siContainer == null) {
            SelectedItemContainer container = createSelectedItemContainer();
            setSelectedItemContainer(container);
        }
        return siContainer;
    }

    /**
	 * Sets a container with the selected item.
	 * 
	 * Note that it is not possible to have a method setSelectedItem() only.
	 * The container contains both the selected item and the root item
	 * (remember, we have hierarchical data). Without the setSelectedItemContainer()
	 * method it would not be possible to set an item with a different root, because
	 * the root cannot be set.
	 */
    public void setSelectedItemContainer(SelectedItemContainer container) {
        if (container != null) {
            if (this.siContainer != null) {
                this.siContainer.clearListeners();
            }
            this.siContainer = container;
            for (SelectedItemContainerListener listener : selectedItemListeners) {
                this.siContainer.addSelectedItemContainerListener(listener);
            }
            if (container != null) {
                if (container.getSelectedItem() != null) {
                    getText().setText(container.getSelectedItemAsString());
                    Object item = siContainer.getSelectedItem();
                    IStructuredSelection selection = new StructuredSelection(item);
                    getTreeViewer().setExpandedElements(new Object[] { item });
                    getTreeViewer().setSelection(selection, true);
                }
            }
        }
    }

    private void addListeners() {
        tree.addKeyListener(new CtrlFKeyListener());
        text.addKeyListener(new EscInFilterKeyListener());
        text.addModifyListener(new ModifyTextListener());
        tree.addSelectionListener(new TreeNodeSelectionListener());
    }

    private class CtrlFKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (ctrlPressed(e) && e.keyCode == 'f') {
                text.setFocus();
                text.selectAll();
            }
        }

        private boolean ctrlPressed(KeyEvent e) {
            return (e.stateMask & SWT.CTRL) != 0;
        }
    }

    private class ModifyTextListener implements ModifyListener {

        @Override
        public void modifyText(ModifyEvent e) {
            if (!executingListeners) {
                executingListeners = true;
                Text text = (Text) e.getSource();
                String content = text.getText();
                Object item = getSelectedItemContainer().getRepresentedItem(content);
                if (item != null) {
                    getSelectedItemContainer().setSelectedItem(item);
                    IStructuredSelection selection = new StructuredSelection(item);
                    treeViewer.setExpandedElements(new Object[] { item });
                    treeViewer.setSelection(selection, true);
                }
                executingListeners = false;
            }
        }
    }

    private class EscInFilterKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.keyCode == SWT.ESC) {
                tree.setFocus();
                String siAsString = getSelectedItemContainer().getSelectedItemAsString();
                text.setText(siAsString);
            }
        }
    }

    private class TreeNodeSelectionListener implements SelectionListener {

        @Override
        public void widgetSelected(SelectionEvent e) {
            if (!executingListeners) {
                executingListeners = true;
                TreeItem item = (TreeItem) e.item;
                Object data = item.getData();
                getSelectedItemContainer().setSelectedItem(data);
                String itemAsString = getSelectedItemContainer().getSelectedItemAsString();
                text.setText(itemAsString);
                executingListeners = false;
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }

    @Override
    public void dispose() {
        treeViewer = null;
        tree = null;
        text = null;
        autoComplete = null;
        siContainer = null;
    }
}
