package org.eclipse.mylyn.tasks.ui.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylyn.internal.tasks.core.LocalTask;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.internal.tasks.ui.editors.EditorBusyIndicator;
import org.eclipse.mylyn.internal.tasks.ui.editors.IBusyEditor;
import org.eclipse.mylyn.internal.tasks.ui.editors.TaskEditorActionContributor;
import org.eclipse.mylyn.internal.tasks.ui.editors.TaskPlanningEditor;
import org.eclipse.mylyn.monitor.core.StatusHandler;
import org.eclipse.mylyn.tasks.core.AbstractTask;
import org.eclipse.mylyn.tasks.core.RepositoryTaskData;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.TasksUiPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

/**
 * @author Mik Kersten
 * @author Eric Booth (initial prototype)
 * @author Rob Elves
 */
public class TaskEditor extends SharedHeaderFormEditor implements IBusyEditor {

    public static final String ID_EDITOR = "org.eclipse.mylyn.tasks.ui.editors.task";

    protected AbstractTask task;

    private TaskPlanningEditor taskPlanningEditor;

    private TaskEditorInput taskEditorInput;

    private List<IEditorPart> editors = new ArrayList<IEditorPart>();

    private Menu contextMenu;

    private IEditorPart contentOutlineProvider = null;

    public final Object FAMILY_SUBMIT = new Object();

    private EditorBusyIndicator editorBusyIndicator;

    public TaskEditor() {
        super();
        taskPlanningEditor = new TaskPlanningEditor(this);
        taskPlanningEditor.setParentEditor(this);
    }

    protected void contextMenuAboutToShow(IMenuManager manager) {
        TaskEditorActionContributor contributor = getContributor();
        if (contributor != null) contributor.contextMenuAboutToShow(manager);
    }

    public TaskEditorActionContributor getContributor() {
        return (TaskEditorActionContributor) getEditorSite().getActionBarContributor();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        return getAdapterDelgate(adapter);
    }

    public Object getAdapterDelgate(Class<?> adapter) {
        if (contentOutlineProvider != null) {
            return contentOutlineProvider.getAdapter(adapter);
        } else {
            return super.getAdapter(adapter);
        }
    }

    @Override
    public IEditorPart getActiveEditor() {
        return super.getActiveEditor();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        for (IFormPage page : getPages()) {
            if (page.isDirty()) {
                page.doSave(monitor);
            }
        }
        editorDirtyStateChanged();
    }

    @SuppressWarnings("unchecked")
    IFormPage[] getPages() {
        ArrayList formPages = new ArrayList();
        if (pages != null) {
            for (int i = 0; i < pages.size(); i++) {
                Object page = pages.get(i);
                if (page instanceof IFormPage) formPages.add(page);
            }
        }
        return (IFormPage[]) formPages.toArray(new IFormPage[formPages.size()]);
    }

    /**
	 * Refresh editor with new contents (if any)
	 */
    public void refreshEditorContents() {
        for (IFormPage page : getPages()) {
            if (page instanceof AbstractRepositoryTaskEditor) {
                AbstractRepositoryTaskEditor editor = (AbstractRepositoryTaskEditor) page;
                editor.refreshEditor();
            }
        }
    }

    /**
	 * Saves the multi-page editor's document as another file. Also updates the text for page 0's tab, and updates this
	 * multi-page editor's input to correspond to the nested editor's.
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
    @Override
    public void doSaveAs() {
        IEditorPart editor = getEditor(0);
        if (editor != null) {
            editor.doSaveAs();
            setPageText(0, editor.getTitle());
            setInput(editor.getEditorInput());
        }
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        setSite(site);
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public boolean isDirty() {
        for (IFormPage page : getPages()) {
            if (page.isDirty()) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Updates the tab title
	 */
    public void changeTitle() {
        this.setPartName(taskEditorInput.getLabel());
    }

    public void markDirty() {
        firePropertyChange(PROP_DIRTY);
        return;
    }

    public void setFocusOfActivePage() {
        if (this.getActivePage() > -1) {
            IFormPage page = this.getPages()[this.getActivePage()];
            if (page != null) {
                page.setFocus();
            }
        }
    }

    @Override
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
    }

    @Override
    public void dispose() {
        if (editorBusyIndicator != null) {
            editorBusyIndicator.dispose();
        }
        for (IEditorPart part : editors) {
            part.dispose();
        }
        if (taskPlanningEditor != null) {
            taskPlanningEditor.dispose();
        }
        super.dispose();
    }

    public TaskEditorInput getTaskEditorInput() {
        return taskEditorInput;
    }

    @Override
    protected void addPages() {
        editorBusyIndicator = new EditorBusyIndicator(this);
        try {
            MenuManager manager = new MenuManager();
            IMenuListener listener = new IMenuListener() {

                public void menuAboutToShow(IMenuManager manager) {
                    contextMenuAboutToShow(manager);
                }
            };
            manager.setRemoveAllWhenShown(true);
            manager.addMenuListener(listener);
            contextMenu = manager.createContextMenu(getContainer());
            getContainer().setMenu(contextMenu);
            int index = -1;
            if (getEditorInput() instanceof TaskEditorInput) {
                addPage(taskPlanningEditor);
                index++;
                taskEditorInput = (TaskEditorInput) getEditorInput();
                task = taskEditorInput.getTask();
                setPartName(taskEditorInput.getLabel());
                setPageImage(0, TasksUiImages.getImage(TasksUiImages.CALENDAR_SMALL));
            }
            int selectedIndex = index;
            List<AbstractTaskEditorFactory> factories = new ArrayList<AbstractTaskEditorFactory>(TasksUiPlugin.getDefault().getTaskEditorFactories());
            Collections.sort(factories, new Comparator<AbstractTaskEditorFactory>() {

                public int compare(AbstractTaskEditorFactory o1, AbstractTaskEditorFactory o2) {
                    return o1.getTabOrderPriority() - o2.getTabOrderPriority();
                }
            });
            for (AbstractTaskEditorFactory factory : TasksUiPlugin.getDefault().getTaskEditorFactories()) {
                if (factory.canCreateEditorFor(task) || factory.canCreateEditorFor(getEditorInput())) {
                    try {
                        IEditorPart editor = factory.createEditor(this, getEditorInput());
                        IEditorInput input = task != null ? factory.createEditorInput(task) : getEditorInput();
                        if (editor != null && input != null) {
                            FormPage taskEditor = (FormPage) editor;
                            editor.init(getEditorSite(), input);
                            index = addPage(taskEditor);
                            if (input.getImageDescriptor() != null) {
                                setPageImage(index, TasksUiImages.getImage(input.getImageDescriptor()));
                            }
                            if (editor instanceof AbstractRepositoryTaskEditor) {
                                ((AbstractRepositoryTaskEditor) editor).setParentEditor(this);
                                if (getEditorInput() instanceof RepositoryTaskEditorInput) {
                                    RepositoryTaskEditorInput existingInput = (RepositoryTaskEditorInput) getEditorInput();
                                    setPartName(existingInput.getName());
                                } else if (getEditorInput() instanceof NewTaskEditorInput) {
                                    String label = ((NewTaskEditorInput) getEditorInput()).getName();
                                    setPartName(label);
                                }
                                setPageText(index, factory.getTitle());
                                selectedIndex = index;
                            }
                        }
                        if (factory.providesOutline()) {
                            contentOutlineProvider = editor;
                        }
                    } catch (Exception e) {
                        StatusHandler.fail(e, "Could not create editor via factory: " + factory, true);
                    }
                }
            }
            if (selectedIndex != -1) {
                setActivePage(selectedIndex);
            }
            if (task != null) {
                setTitleImage(TasksUiImages.getImage(TasksUiImages.TASK_REPOSITORY));
            } else if (getEditorInput() instanceof AbstractRepositoryTaskEditorInput) {
                this.setTitleImage(TasksUiImages.getImage(TasksUiImages.TASK_REMOTE));
            } else {
                setTitleImage(TasksUiImages.getImage(TasksUiImages.TASK_REPOSITORY));
            }
        } catch (PartInitException e) {
            StatusHandler.fail(e, "failed to create task editor pages", false);
        }
    }

    @Override
    public void setFocus() {
        if (getActivePageInstance() instanceof AbstractNewRepositoryTaskEditor) {
            getActivePageInstance().setFocus();
        } else {
            super.setFocus();
        }
    }

    /**
	 * Update the title of the editor
	 */
    public void updateTitle(String name) {
        setPartName(name);
        setTitleToolTip(name);
        updateFormTitle();
    }

    @Override
    public void showBusy(boolean busy) {
        if (busy) {
            editorBusyIndicator.startBusy();
        } else {
            editorBusyIndicator.stopBusy();
        }
        for (IFormPage page : getPages()) {
            if (page instanceof AbstractRepositoryTaskEditor) {
                AbstractRepositoryTaskEditor editor = (AbstractRepositoryTaskEditor) page;
                editor.showBusy(busy);
            }
        }
    }

    public ISelection getSelection() {
        if (getSite() != null && getSite().getSelectionProvider() != null) {
            return getSite().getSelectionProvider().getSelection();
        } else {
            return StructuredSelection.EMPTY;
        }
    }

    @Override
    protected void createHeaderContents(IManagedForm headerForm) {
        getToolkit().decorateFormHeading(headerForm.getForm().getForm());
        headerForm.getForm().setImage(TasksUiImages.getImage(TasksUiImages.TASK));
        updateFormTitle();
    }

    protected void updateFormTitle() {
        IEditorInput input = getEditorInput();
        if (input instanceof TaskEditorInput) {
            AbstractTask task = ((TaskEditorInput) input).getTask();
            if (task instanceof LocalTask) {
                getHeaderForm().getForm().setText("Task: " + task.getSummary());
            } else {
                setFormHeaderImage(task.getConnectorKind());
                setFormHeaderLabel(task);
                return;
            }
        } else if (input instanceof RepositoryTaskEditorInput) {
            AbstractTask task = ((RepositoryTaskEditorInput) input).getRepositoryTask();
            RepositoryTaskData data = ((RepositoryTaskEditorInput) input).getTaskData();
            if (task != null && data != null && !data.isNew()) {
                setFormHeaderImage(task.getConnectorKind());
                setFormHeaderLabel(task);
                return;
            } else {
                if (data != null) {
                    setFormHeaderImage(data.getRepositoryKind());
                    setFormHeaderLabel(data);
                }
            }
        }
    }

    private void setFormHeaderImage(String repositoryKind) {
        ImageDescriptor overlay = TasksUiPlugin.getDefault().getOverlayIcon(repositoryKind);
        Image image = TasksUiImages.getImageWithOverlay(TasksUiImages.REPOSITORY, overlay, false, false);
        if (getHeaderForm() != null) {
            getHeaderForm().getForm().setImage(image);
        }
    }

    public Form getTopForm() {
        return this.getHeaderForm().getForm().getForm();
    }

    public void setMessage(String message, int type) {
        if (this.getHeaderForm() != null && this.getHeaderForm().getForm() != null) {
            if (!this.getHeaderForm().getForm().isDisposed()) {
                this.getHeaderForm().getForm().setMessage(message, type);
            }
        }
    }

    protected IWorkbenchSiteProgressService getProgressService() {
        Object siteService = getEditorSite().getAdapter(IWorkbenchSiteProgressService.class);
        if (siteService != null) return (IWorkbenchSiteProgressService) siteService;
        return null;
    }

    private void setFormHeaderLabel(RepositoryTaskData taskData) {
        AbstractRepositoryConnectorUi connectorUi = TasksUiPlugin.getConnectorUi(taskData.getRepositoryKind());
        String kindLabel = taskData.getTaskKind();
        if (connectorUi != null) {
            kindLabel = connectorUi.getTaskKindLabel(taskData);
        }
        String idLabel = taskData.getTaskKey();
        if (taskData.isNew()) {
            if (connectorUi != null) {
                kindLabel = "New " + connectorUi.getTaskKindLabel(taskData);
            } else {
                kindLabel = "New " + taskData.getTaskKind();
            }
            idLabel = "";
        }
        if (idLabel != null) {
            if (getHeaderForm().getForm() != null) {
                getHeaderForm().getForm().setText(kindLabel + " " + idLabel);
            }
        } else if (getHeaderForm().getForm() != null) {
            getHeaderForm().getForm().setText(kindLabel);
        }
    }

    private void setFormHeaderLabel(AbstractTask repositoryTask) {
        AbstractRepositoryConnectorUi connectorUi = TasksUiPlugin.getConnectorUi(repositoryTask.getConnectorKind());
        String kindLabel = "";
        if (connectorUi != null) {
            kindLabel = connectorUi.getTaskKindLabel(repositoryTask);
        }
        String idLabel = repositoryTask.getTaskKey();
        if (idLabel != null) {
            if (getHeaderForm().getForm() != null) {
                getHeaderForm().getForm().setText(kindLabel + " " + idLabel);
            }
        } else if (getHeaderForm().getForm() != null) {
            getHeaderForm().getForm().setText(kindLabel);
        }
    }

    @Override
    public void setTitleImage(Image titleImage) {
        super.setTitleImage(titleImage);
    }
}
