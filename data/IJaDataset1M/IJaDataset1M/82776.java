package com.jacum.cms.rcp.ui.views.navigation;

import java.text.MessageFormat;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import com.jacum.cms.rcp.model.Item;
import com.jacum.cms.rcp.ui.Messages;
import com.jacum.cms.rcp.ui.actions.RenameItemAction;
import com.jacum.cms.rcp.ui.utils.ProgressBarUtils;

/**
 * Class is responsible for in-palce rename of the items in the tree on the navigation view.
 * In-place rename is ability to create editor for name on the place where element located 
 * (straight in the tree)
 * 
 * UC3.13. In-place rename
 * 
 * @author rich
 */
public class InPlaceRenameEditor {

    private Tree tree;

    private TreeViewer viewer;

    private TreeEditor treeEditor;

    private Text editText;

    private Item item;

    private boolean textModified;

    /**
	 * Initialize tree editor:
	 * Create tree editor for tree viewer.
	 * Added listeners for creation and disposing text editor.
	 * 
	 * @param viewer tree viewer with items tree
	 */
    public void init(final TreeViewer viewer) {
        this.viewer = viewer;
        tree = viewer.getTree();
        treeEditor = new TreeEditor(tree);
        treeEditor.grabHorizontal = true;
        tree.addMouseListener(new MouseAdapter() {

            private Object previousSelection;

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                previousSelection = null;
            }

            public void mouseUp(MouseEvent e) {
                TreeItem treeItem = tree.getItem(new Point(e.x, e.y));
                if (treeItem == null) {
                    renameItem();
                    disposeEditor();
                } else {
                    disposeEditor();
                    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                    if (e.button == 1) {
                        final Object firstElement = selection.getFirstElement();
                        if (selection.size() == 1) {
                            if (firstElement instanceof Item && firstElement == previousSelection) {
                                item = (Item) firstElement;
                                editItemName(item, tree);
                            }
                        }
                        previousSelection = firstElement;
                    }
                }
            }
        });
        tree.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (editText != null && !editText.isDisposed()) {
                    renameItem();
                    disposeEditor();
                }
            }
        });
    }

    /**
     * Create text editor, set item name and set text editor to the tree editor.
     * 
     * @param item item to edit
     * @param tree tree which holds the item
     */
    private void editItemName(final Item item, final Tree tree) {
        textModified = false;
        editText = new Text(tree, SWT.BORDER);
        editText.setText(item.getRemoteItem().getName());
        editText.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                switch(e.detail) {
                    case SWT.TRAVERSE_RETURN:
                        renameItem();
                    case SWT.TRAVERSE_ESCAPE:
                        disposeEditor();
                        break;
                }
            }
        });
        editText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                textModified = true;
            }
        });
        editText.setFocus();
        treeEditor.setEditor(editText, tree.getSelection()[0]);
    }

    /**
     * Rename item if name has been changed. Performs with progress bar
     */
    private void renameItem() {
        if (textModified) {
            String newName = editText.getText();
            String renameLabel = MessageFormat.format(Messages.getString("InPlaceRenameEditor.0"), new Object[] { item.getName() });
            ProgressBarUtils.executeActionWithProgress(tree.getShell(), renameLabel, new RenameItemAction(item, newName, item.getContentSource().getHost()));
            viewer.update(item, null);
        }
    }

    /**
     * Dispose editor
     */
    private void disposeEditor() {
        if (editText != null) {
            editText.dispose();
            editText = null;
            treeEditor.setEditor(null);
            tree.setFocus();
        }
    }
}
