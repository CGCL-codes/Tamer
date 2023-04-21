package com.ivis.xprocess.ui.editors;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import com.ivis.xprocess.core.SchedulingType;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.ui.datawrappers.process.SchemaWrapper;
import com.ivis.xprocess.ui.datawrappers.process.TaskPrototypeWrapper;
import com.ivis.xprocess.ui.editors.dynamic.elements.EditorPage;
import com.ivis.xprocess.ui.editors.dynamic.elements.UserDefinedPropertyPage;
import com.ivis.xprocess.ui.editors.dynamic.elements.specific.PrioritizedPage;
import com.ivis.xprocess.ui.editors.dynamic.elements.specific.TaskGatewayPage;
import com.ivis.xprocess.ui.editors.outline.TaskOutLine;
import com.ivis.xprocess.ui.factories.ViewerStrategyFactory;
import com.ivis.xprocess.ui.factories.strategies.StandardTableViewStrategy;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.util.TestHarness;

public class TaskPrototypeEditor extends ElementEditor implements IShowInSource, ISelectionProvider {

    private String parentSchemaName = "parenttask_prototype_schema.xml";

    private String childSchemaName = "task_prototype_schema.xml";

    private String doWhileSchemaName = "dowhile_task_prototype_schema.xml";

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        ViewerStrategyFactory.getInstance().registerStrategy(TaskPrototypeEditor.class, StandardTableViewStrategy.class);
    }

    @Override
    protected void createPages() {
        super.createPages();
        if (elementWrapper != null) {
            Composite editorPage = getContainer();
            if (!editorPage.isDisposed()) {
                TestHarness.name(editorPage, "Editor:" + elementWrapper.getLabel());
                attachHooks();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if ((elementWrapper != null) && adapter.getName().equals(IContentOutlinePage.class.getName())) {
            return new TaskOutLine(this);
        }
        return super.getAdapter(adapter);
    }

    @Override
    public String getSchemaName() {
        if (elementWrapper instanceof TaskPrototypeWrapper) {
            TaskPrototypeWrapper taskPrototypeWrapper = (TaskPrototypeWrapper) elementWrapper;
            Xtask task = taskPrototypeWrapper.getTask();
            if (task.getSchedulingType() == SchedulingType.OVERHEAD) {
                return doWhileSchemaName;
            }
            if ((taskPrototypeWrapper.getTasks().length > 0) || taskPrototypeWrapper.getTask().isDesignatedAsParent()) {
                return parentSchemaName;
            }
        }
        return childSchemaName;
    }

    @Override
    public void refreshEvent(ChangeRecord changeRecord) {
        if (hasBeenExternallyChanged() || (elementWrapper == null)) {
            return;
        }
        super.refreshEvent(changeRecord);
        boolean continueRefresh = false;
        if (elementWrapper.getElement().getUuid().equals(changeRecord.getElement().getUuid())) {
            continueRefresh = true;
        }
        if (continueRefresh) {
            if (changeRecord.hasProperty("Prioritized") || changeRecord.hasProperty("Unprioritized")) {
                if (canRefresh()) {
                    if (changeRecord.hasProperty("Prioritized")) {
                        switchToPage = PrioritizedPage.class;
                    }
                    recreatePagesAsync();
                } else {
                    setNeedsRefreshAfterEditing(true);
                }
            }
            if (changeRecord.hasProperty("Parent Designation")) {
                if (canRefresh() && !getContainer().isDisposed()) {
                    recreatePagesAsync();
                } else {
                    setNeedsRefreshAfterEditing(true);
                }
                return;
            }
            if (changeRecord.hasChange(ChangeEvent.FIRST_CHILD) || changeRecord.hasChange(ChangeEvent.LASTCHILD_REMOVED)) {
                if (canRefresh() && !getContainer().isDisposed()) {
                    recreatePagesAsync();
                } else {
                    setNeedsRefreshAfterEditing(true);
                }
                return;
            }
            if (changeRecord.hasChange(ChangeEvent.NEW_CHILD)) {
                Xtask task = (Xtask) elementWrapper.getElement();
                if (task.getTasks().size() == changeRecord.numberOfChangeEvents(ChangeEvent.NEW_CHILD)) {
                    if (canRefresh() && !getContainer().isDisposed()) {
                        recreatePagesAsync();
                    } else {
                        setNeedsRefreshAfterEditing(true);
                    }
                    return;
                }
            }
        }
        if (changeRecord.getElementWrapper() instanceof SchemaWrapper) {
            UserDefinedPropertyPage userDefinedPropertyPage = getUserDefinedPage(changeRecord.getElementWrapper().getUuid());
            if (userDefinedPropertyPage != null) {
                if (changeRecord.getElementWrapper().getUuid().equals(userDefinedPropertyPage.getSchemaUuid())) {
                    if (changeRecord.hasChange(ChangeEvent.ELEMENT_DELETED)) {
                        if (canRefresh()) {
                            removePage(userDefinedPropertyPage.getPageIndex());
                        } else {
                            setNeedsRefreshAfterEditing(true);
                        }
                    }
                }
            }
            if (changeRecord.hasChange(ChangeEvent.USERDEFINED_PROPERTY_ADDED)) {
                if (changeRecord.getElementWrapper().getParent() != null) {
                    if (changeRecord.getElementWrapper().getParentUuid().equals(getLocalTransientElement().getUuid())) {
                        if (getPageCount() == 1) {
                            if (canRefresh()) {
                                recreatePagesAsync();
                            } else {
                                setNeedsRefreshAfterEditing(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private UserDefinedPropertyPage getUserDefinedPage(String schemaUuid) {
        UserDefinedPropertyPage userDefinedPage = null;
        for (EditorPage editorPage : editorRefreshManager.getSaveableEditorPages()) {
            if (editorPage instanceof UserDefinedPropertyPage) {
                UserDefinedPropertyPage userDefinedPropertyPage = (UserDefinedPropertyPage) editorPage;
                if ((userDefinedPropertyPage.getSchemaUuid() != null) && userDefinedPropertyPage.getSchemaUuid().equals(schemaUuid)) {
                    userDefinedPage = userDefinedPropertyPage;
                    break;
                }
            }
        }
        return userDefinedPage;
    }

    /**
     * @return the Gateway editor page
     */
    public EditorPage getGatewayPage() {
        EditorPage gatewayPage = null;
        for (EditorPage editorPage : editorRefreshManager.getSaveableEditorPages()) {
            if (editorPage instanceof TaskGatewayPage) {
                gatewayPage = editorPage;
                break;
            }
        }
        return gatewayPage;
    }
}
