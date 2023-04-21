package de.guhsoft.jinto.core.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import de.guhsoft.jinto.core.JIntoCorePlugin;
import de.guhsoft.jinto.core.Util;
import de.guhsoft.jinto.core.editor.actions.FindAction;
import de.guhsoft.jinto.core.editor.actions.RedoAction;
import de.guhsoft.jinto.core.editor.actions.UndoAction;
import de.guhsoft.jinto.core.editor.outline.ResourceBundleOutlinePage;
import de.guhsoft.jinto.core.model.AddRowOperation;
import de.guhsoft.jinto.core.model.RemoveRowOperation;
import de.guhsoft.jinto.core.model.ResourceBundleManager;
import de.guhsoft.jinto.core.model.ResourceBundleModel;
import de.guhsoft.jinto.core.model.ResourceFile;
import de.guhsoft.jinto.core.model.ResourceRow;

/**
 * @author mseele
 * 
 * WYSIYG-Editor for editing *.properties-files which are used for
 * internationalization in java programms.
 */
public class ResourceEditor extends EditorPart implements IResourceChangeListener, KeyListener, ISelectionProvider {

    private Map<String, IAction> fActions = new HashMap<String, IAction>();

    private ResourceBundleModel resourceDataModel;

    private boolean isDirty = false;

    private boolean isReadOnly;

    private ResourceEditorRenderer fRenderer;

    private IUndoableOperation fFirstOperation;

    /**
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
    public void doSave(IProgressMonitor monitor) {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        monitor.beginTask(JIntoCorePlugin.getResourceString("save.progressMonitor"), this.getResourceDataModel().getResourceFileCount() + 1);
        List<Object[]> objectList = new LinkedList<Object[]>();
        Object[] object;
        for (int i = 0; i < this.getResourceDataModel().getResourceFileCount(); i++) {
            object = new Object[3];
            Entry<String, ResourceFile> entry = this.getResourceDataModel().getResourceFileMapEntry(i);
            object[0] = entry.getKey();
            object[1] = entry.getValue();
            object[2] = new ResourceProperties();
            objectList.add(object);
        }
        boolean saveEmptyValues = JIntoCorePlugin.getDefault().getPluginPreferences().getBoolean("saving.saveEmptyValues");
        String key;
        for (Iterator<?> itr = this.getResourceDataModel().getRowKeys(); itr.hasNext(); ) {
            key = (String) itr.next();
            ResourceRow resourceRow = this.getResourceDataModel().getRow(key);
            for (int j = 0; j < objectList.size(); j++) {
                object = objectList.get(j);
                String columnValue = resourceRow.getColumnValue((String) object[0]);
                if (saveEmptyValues) {
                    ((Properties) object[2]).put(key, Util.unescapeForLabel(columnValue));
                } else if (columnValue.length() > 0) {
                    ((Properties) object[2]).put(key, Util.unescapeForLabel(columnValue));
                }
            }
        }
        monitor.worked(1);
        for (int i = 0; i < objectList.size(); i++) {
            object = objectList.get(i);
            try {
                ((ResourceFile) object[1]).saveProperties((Properties) object[2]);
            } catch (IOException e) {
                Status status = new Status(IStatus.ERROR, JIntoCorePlugin.getID(), IStatus.ERROR, "IOException occurs", e.fillInStackTrace());
                JIntoCorePlugin.getDefault().getLog().log(status);
                ErrorDialog.openError((Shell) null, JIntoCorePlugin.getResourceString("error.dialog.title"), JIntoCorePlugin.getResourceString("error.saving"), status);
                break;
            } catch (CoreException e) {
                Status status = new Status(IStatus.ERROR, JIntoCorePlugin.getID(), IStatus.ERROR, "CoreException occurs", e.fillInStackTrace());
                JIntoCorePlugin.getDefault().getLog().log(status);
                ErrorDialog.openError((Shell) null, JIntoCorePlugin.getResourceString("error.dialog.title"), JIntoCorePlugin.getResourceString("error.saving"), status);
                break;
            }
            monitor.worked(2 + i);
        }
        this.setDirty(false);
        this.fFirstOperation = null;
        clearOperationHistory();
        monitor.done();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        new ProblemsCheckingJob(this).schedule();
    }

    /**
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
    public void doSaveAs() {
    }

    /**
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (IGotoMarker.class.equals(adapter)) {
            return new GoToResource(getResourceDataModel(), this.fRenderer);
        }
        if (IContentOutlinePage.class.equals(adapter)) {
            return new ResourceBundleOutlinePage(this);
        }
        return super.getAdapter(adapter);
    }

    /**
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        IStorage storage = null;
        if (input instanceof IStorageEditorInput) {
            try {
                storage = ((IStorageEditorInput) input).getStorage();
            } catch (CoreException e) {
                throw new PartInitException("Exception while getting storage from IStorageEditorInput.", e);
            }
        } else {
            throw new PartInitException("Invalid Input: Must be IStorageEditorInput.");
        }
        this.setReadOnly(storage.isReadOnly());
        try {
            ResourceBundleModel model = ResourceBundleManager.createForFile(storage, true);
            if (model == null) {
                throw new PartInitException("Could not create ResourceBundle model from File " + storage.getFullPath().toString() + "!");
            }
            this.setResourceDataModel(model);
        } catch (CoreException e) {
            throw new PartInitException(e.getMessage());
        } catch (IOException e) {
            throw new PartInitException(e.getMessage());
        }
        site.setSelectionProvider(this);
        this.setSite(site);
        this.setInput(input);
        this.setPartName(this.getResourceDataModel().getName());
        this.setTitleToolTip(JIntoCorePlugin.getResourceString("editor.filename.addition", this.getResourceDataModel().getName()));
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    /**
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
    public boolean isDirty() {
        return this.isDirty;
    }

    public void setDirty(boolean isDirty) {
        if (this.isDirty != isDirty) {
            this.isDirty = isDirty;
            this.firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /**
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createPartControl(final Composite parent) {
        this.fRenderer = new ResourceEditorRenderer(this);
        this.fRenderer.createContents(parent);
        createActions();
        OperationHistoryFactory.getOperationHistory().addOperationHistoryListener(new IOperationHistoryListener() {

            public void historyNotification(OperationHistoryEvent event) {
                onHistoryNotification(event);
            }
        });
        new ProblemsCheckingJob(this).schedule();
    }

    void onHistoryNotification(OperationHistoryEvent event) {
        IUndoContext context = getResourceDataModel().getUndoContent();
        if (event.getOperation().hasContext(context)) {
            IOperationHistory history = event.getHistory();
            switch(event.getEventType()) {
                case OperationHistoryEvent.ABOUT_TO_EXECUTE:
                    if (this.fFirstOperation == null) {
                        this.fFirstOperation = event.getOperation();
                    } else if (!history.canUndo(context) && this.fFirstOperation.equals(history.getRedoOperation(context))) {
                        this.fFirstOperation = event.getOperation();
                    }
                    break;
                default:
                    boolean dirty = false;
                    if (this.fFirstOperation != null) {
                        if (history.canUndo(context)) {
                            dirty = true;
                        } else {
                            dirty = !this.fFirstOperation.equals(history.getRedoOperation(context));
                        }
                    }
                    setDirty(dirty);
                    break;
            }
        }
    }

    public void cancelEditing() {
        this.fRenderer.cancelEditing();
    }

    /**
	 * @see ResourceEditorRenderer#sizeColumnsToEditorWidth()
	 */
    public void sizeColumnsToEditorWidth() {
        this.fRenderer.sizeColumnsToEditorWidth();
    }

    /**
	 * @see ResourceEditorRenderer#sizeColumnsToOptimalWidth()
	 */
    public void sizeColumnsToOptimalWidth() {
        this.fRenderer.sizeColumnsToOptimalWidth();
    }

    public boolean isDisposed() {
        return this.fRenderer.isDisposed();
    }

    public void addColumn(String columnKey) {
        this.fRenderer.addColumn(columnKey);
    }

    public void removeColumn(String columnKey) {
        this.fRenderer.removeColumn(columnKey);
    }

    public void refreshTable() {
        this.fRenderer.refreshTable();
    }

    private void createActions() {
        setAction(ActionFactory.FIND.getId(), new FindAction(this, this.fRenderer.getFocusCellManager()));
        setAction(ActionFactory.UNDO.getId(), new UndoAction(this));
        setAction(ActionFactory.REDO.getId(), new RedoAction(this));
    }

    private void setAction(String actionID, IAction action) {
        if (action == null) {
            this.fActions.remove(actionID);
        } else {
            this.fActions.put(actionID, action);
        }
    }

    public IAction getAction(String actionID) {
        return this.fActions.get(actionID);
    }

    /**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
    public void setFocus() {
        this.fRenderer.setFocus();
    }

    public void dispose() {
        super.dispose();
        if (this.fActions != null) {
            this.fActions.clear();
            this.fActions = null;
        }
        if (this.fRenderer != null) {
            this.fRenderer.dispose();
        }
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }

    /**
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
    public void resourceChanged(IResourceChangeEvent event) {
        ResourceBundleModel model = getResourceDataModel();
        for (int i = 0; i < model.getResourceFileCount(); i++) {
            if (event.getDelta() != null) {
                IResourceDelta resourceDelta = event.getDelta().findMember(model.getResourceFileMapEntry(i).getValue().getFileStorage().getFullPath());
                if (resourceDelta != null) {
                    if (resourceDelta.getKind() == IResourceDelta.CHANGED && (resourceDelta.getFlags() & IResourceDelta.CONTENT) != 0 || resourceDelta.getKind() == IResourceDelta.REMOVED) {
                        new UpdateEditorJob(this, resourceDelta, model.getResourceFileMapEntry(i).getValue()).schedule();
                    }
                }
            }
        }
        IResourceDelta delta = event.getDelta().findMember(model.getSelectedStorage().getFullPath().removeLastSegments(1));
        if (delta != null && delta.getKind() == IResourceDelta.CHANGED) {
            IResourceDelta[] affectedItems = delta.getAffectedChildren(IResourceDelta.ADDED);
            for (int i = 0; i < affectedItems.length; i++) {
                if (affectedItems[i].getResource().getType() == IResource.FILE && affectedItems[i].getKind() == IResourceDelta.ADDED) {
                    ResourceFile resourceFile = ResourceBundleManager.checkAndGetMatchingFile(model.getName(), (IFile) affectedItems[i].getResource());
                    if (resourceFile != null) {
                        new UpdateEditorJob(this, affectedItems[i], resourceFile).schedule();
                    }
                }
            }
        }
    }

    void addNewRow() {
        cancelEditing();
        String newRowKey = "";
        ResourceRow row = getResourceDataModel().getRow(newRowKey);
        if (row == null) {
            execute(new AddRowOperation(getResourceDataModel(), newRowKey));
        } else {
            this.fRenderer.setSelection(new StructuredSelection(row));
        }
    }

    @SuppressWarnings("unchecked")
    void removeSelectedRows() {
        ISelection selection = this.fRenderer.getSelection();
        if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
            List<String> rowsToRemove = new ArrayList<String>();
            for (Iterator<ResourceRow> iterator = ((IStructuredSelection) selection).iterator(); iterator.hasNext(); ) {
                ResourceRow row = iterator.next();
                rowsToRemove.add(row.getKey());
            }
            if (rowsToRemove.size() > 0) {
                execute(new RemoveRowOperation(getResourceDataModel(), rowsToRemove.toArray(new String[rowsToRemove.size()])));
            }
        }
    }

    void execute(final IUndoableOperation operation) {
        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

            public void run() {
                try {
                    IOperationHistory operationHistory = OperationHistoryFactory.getOperationHistory();
                    operationHistory.execute(operation, new NullProgressMonitor(), null);
                } catch (ExecutionException e) {
                    String pluginID = JIntoCorePlugin.getID();
                    Status status = new Status(IStatus.ERROR, pluginID, IStatus.ERROR, "ExecutionException occurs", e);
                    JIntoCorePlugin.getDefault().getLog().log(status);
                    String title = JIntoCorePlugin.getResourceString("error.dialog.title");
                    String message = JIntoCorePlugin.getResourceString("error.operation");
                    ErrorDialog.openError(getEditorSite().getShell(), title, message, status);
                }
            }
        });
    }

    public void clearOperationHistory() {
        OperationHistoryFactory.getOperationHistory().dispose(getResourceDataModel().getUndoContent(), true, true, true);
    }

    public void selectRow(String key) {
        ResourceRow row = getResourceDataModel().getRow(key);
        if (row != null) {
            this.fRenderer.setSelection(new StructuredSelection(row));
        }
    }

    /**
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
    public void keyPressed(KeyEvent e) {
        if (!this.isReadOnly) {
            if (e.character == '+') {
                addNewRow();
            } else if (e.character == '-') {
                removeSelectedRows();
            }
        }
    }

    /**
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
    public void keyReleased(KeyEvent e) {
    }

    /**
	 * Returns the selection provider from the table to add
	 * selectionChangedListener's on the table.
	 */
    public ISelectionProvider getTableSelectionProvider() {
        return this.fRenderer;
    }

    public ResourceBundleModel getResourceDataModel() {
        return this.resourceDataModel;
    }

    private void setResourceDataModel(ResourceBundleModel resourceDataModel) {
        this.resourceDataModel = resourceDataModel;
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    private void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
    }

    /**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
    public ISelection getSelection() {
        return new StructuredSelection(new IStorage[] { this.getResourceDataModel().getSelectedStorage() });
    }

    /**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
    }

    /**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
    public void setSelection(ISelection selection) {
    }
}
