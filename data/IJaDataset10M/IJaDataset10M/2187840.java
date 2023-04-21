package org.plazmaforge.studio.core.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.plazmaforge.studio.core.storage.Entry;
import org.plazmaforge.studio.core.storage.Storage;

public class RenameAction extends AbstractStorageAction {

    private Entry inlinedResource;

    private Text textEditor;

    private Composite textEditorParent;

    private final TreeEditor treeEditor;

    /**
     * Creates a new action.
     * 
     */
    public RenameAction(TreeViewer treeViewer) {
        super("Rename", treeViewer);
        setToolTipText("Rename");
        this.treeEditor = new TreeEditor(treeViewer.getTree());
    }

    Composite createParent() {
        Tree tree = getTree();
        Composite result = new Composite(tree, SWT.NONE);
        TreeItem[] selectedItems = tree.getSelection();
        treeEditor.horizontalAlignment = SWT.LEFT;
        treeEditor.grabHorizontal = true;
        treeEditor.setEditor(result, selectedItems[0]);
        return result;
    }

    /**
     * Create the text editor widget.
     * 
     * @param resource
     *            the resource to rename
     */
    private void createTextEditor(final Entry entry) {
        textEditorParent = createParent();
        textEditorParent.setVisible(false);
        textEditorParent.addListener(SWT.Paint, new Listener() {

            public void handleEvent(Event e) {
                Point textSize = textEditor.getSize();
                Point parentSize = textEditorParent.getSize();
                e.gc.drawRectangle(0, 0, Math.min(textSize.x + 4, parentSize.x - 1), parentSize.y - 1);
            }
        });
        textEditor = new Text(textEditorParent, SWT.NONE);
        textEditorParent.setBackground(textEditor.getBackground());
        textEditor.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event e) {
                Point textSize = textEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                textSize.x += textSize.y;
                Point parentSize = textEditorParent.getSize();
                textEditor.setBounds(2, 1, Math.min(textSize.x, parentSize.x - 4), parentSize.y - 2);
                textEditorParent.redraw();
            }
        });
        textEditor.addListener(SWT.Traverse, new Listener() {

            public void handleEvent(Event event) {
                switch(event.detail) {
                    case SWT.TRAVERSE_ESCAPE:
                        disposeTextWidget();
                        event.doit = true;
                        event.detail = SWT.TRAVERSE_NONE;
                        break;
                    case SWT.TRAVERSE_RETURN:
                        saveChangesAndDispose(entry);
                        event.doit = true;
                        event.detail = SWT.TRAVERSE_NONE;
                        break;
                }
            }
        });
        textEditor.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent fe) {
                saveChangesAndDispose(entry);
            }
        });
    }

    /**
     * Close the text widget and reset the editorText field.
     */
    private void disposeTextWidget() {
        if (textEditorParent != null) {
            textEditorParent.dispose();
            textEditorParent = null;
            textEditor = null;
            treeEditor.setEditor(null, null);
        }
    }

    /**
     * Get the Tree being edited. @returnTree
     */
    private Tree getTree() {
        return ((TreeViewer) viewer).getTree();
    }

    /**
     * Return the new name to be given to the target resource or <code>null<code>
     * if the query was canceled. Rename the currently selected resource using the table editor. 
     * Continue the action when the user is done.
     *       *
     * @return java.lang.String
     * @param resource the resource to rename
     */
    private void queryNewResourceNameInline(final Entry resource) {
        if (textEditorParent == null) {
            createTextEditor(resource);
        }
        textEditor.setText(resource.getName());
        textEditorParent.setVisible(true);
        Point textSize = textEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        textSize.x += textSize.y;
        Point parentSize = textEditorParent.getSize();
        textEditor.setBounds(2, 1, Math.min(textSize.x, parentSize.x - 4), parentSize.y - 2);
        textEditorParent.redraw();
        textEditor.selectAll();
        textEditor.setFocus();
    }

    public void run() {
        IStructuredSelection selection = getSelection();
        Entry currentResource = (Entry) selection.getFirstElement();
        queryNewResourceNameInline(currentResource);
    }

    /**
     * Save the changes and dispose of the text widget.
     * 
     * @param resource -
     *            the resource to move.
     */
    private void saveChangesAndDispose(Entry resource) {
        inlinedResource = resource;
        final String newName = textEditor.getText();
        Runnable query = new Runnable() {

            public void run() {
                disposeTextWidget();
                if (!newName.equals(inlinedResource.getName())) {
                    if (false) {
                    } else {
                        doRename(inlinedResource, newName);
                    }
                }
                inlinedResource = null;
            }
        };
        getTree().getShell().getDisplay().asyncExec(query);
    }

    protected void doRename(Entry entry, String newName) {
        entry.rename(newName);
        forceRefresh(entry);
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event) {
        disposeTextWidget();
        super.selectionChanged(event);
    }

    protected boolean isEnabledState(IStructuredSelection selection) {
        if (selection.size() == 1) {
            Object selected = selection.getFirstElement();
            if (selected instanceof Entry) {
                Entry entry = (Entry) selected;
                if (entry instanceof Storage) {
                    return false;
                }
                return entry.canRename();
            }
        }
        return false;
    }
}
