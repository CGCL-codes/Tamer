package net.sf.jabs.ui.pages;

import java.util.ArrayList;
import java.util.List;
import net.sf.jabs.data.Fields;
import net.sf.jabs.data.Importer;
import net.sf.jabs.data.project.ProjectDAO;
import net.sf.jabs.task.ITaskDef;
import net.sf.jabs.task.InvalidTaskTypeException;
import net.sf.jabs.task.TaskFactory;
import net.sf.jabs.ui.ProjectHelper;
import net.sf.jabs.ui.ProjectTaskContainer;
import net.sf.jabs.util.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.dojo.html.Dialog;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.ofbiz.core.entity.GenericValue;
import org.quartz.CronExpression;

public abstract class Project extends AuthenticatedPage {

    private static Log _log = LogFactory.getLog(Project.class);

    @InjectObject("service:jabs.services.ProjectDAO")
    public abstract ProjectDAO getProjectDAO();

    public abstract void setMessage(String value);

    public abstract void setTaskEdit(ProjectTaskContainer task);

    public abstract ProjectTaskContainer getTaskEdit();

    public abstract void setTaskIndex(int i);

    public abstract int getTaskIndex();

    public abstract void setTaskEditId(int i);

    public abstract int getTaskEditId();

    public abstract void setTaskEditor(String editor);

    public abstract String getTaskEditor();

    public abstract void setTaskRunGroup(long i);

    public abstract long getTaskRunGroup();

    public abstract void setTask(ProjectTaskContainer task);

    public abstract ProjectTaskContainer getTask();

    public abstract ProjectHelper getProjectHelper();

    public abstract void setProjectHelper(ProjectHelper h);

    public abstract List<ProjectHelper> getProjectHelpers();

    public abstract void setProjectHelpers(List<ProjectHelper> l);

    public abstract Integer getTabIndex();

    public abstract void setTabIndex(Integer index);

    public abstract IValidationDelegate getDelegate();

    public abstract String getSubmitSource();

    @InjectPage("Preview")
    public abstract Preview getPreviewPage();

    @InjectComponent("typeDialog")
    public abstract Dialog getTypeDialog();

    public void pageBeginRender(PageEvent event) {
        super.pageBeginRender(event);
    }

    public List<ITaskDef> getTaskDefList() {
        return TaskFactory.getInstance().getTasks();
    }

    /**
     * Toggle the enable field
     * @param id
     */
    public void toggleTaskEnable(Integer id) {
        getProjectHelper().toggleTaskEnable(id);
    }

    /**
     * Toggle the on error field
     * @param id
     */
    public void toggleTaskOnError(Integer id) {
        getProjectHelper().toggleTaskOnError(id);
    }

    /**
     * Prepares a task for editing
     * @param cycle
     */
    public void editTask(Integer id, String taskType) {
        setTaskEdit(null);
        setTaskEditId(id);
        setTaskEdit(getProjectHelper().getTaskList().get(id));
        try {
            setTaskEditor(TaskFactory.getInstance().getTaskDef(taskType).getTaskEditor());
        } catch (InvalidTaskTypeException e) {
            _log.error(e);
            setMessage(e.getMessage());
        }
    }

    /**
     * Adds a new task
     * @param cycle
     */
    public void addTask(int taskDefIndex) {
        ITaskDef taskDef = getTaskDefList().get(taskDefIndex);
        setTaskEdit(new ProjectTaskContainer(ProjectDAO.getNewTask(), true));
        getTaskEdit().getTask().set(Fields.ProjectTask.TYPE, taskDef.getTaskType());
        setTaskEditor(taskDef.getTaskEditor());
        setTaskEditId(-1);
    }

    /**
     * Prepares the task for deletion. The actual delete happens when
     * the project is saved.
     * @param cycle
     */
    public void deleteTask(Integer id) {
        getProjectHelper().deleteTask(id);
    }

    /**
     * Copies a task as a new task
     * @param cycle
     */
    public void copyTask(Integer id) {
        getProjectHelper().copyTask(id);
    }

    /**
     * Moves a task up in the task order
     * @param cycle
     */
    public void moveTaskUp(Integer id) {
        getProjectHelper().swapTask(id, true);
    }

    /**
     * Moves a task down in the task order
     * @param cycle
     */
    public void moveTaskDown(Integer id) {
        getProjectHelper().swapTask(id, false);
    }

    /**
     * Refresh method that will validate the form
     * @param cycle
     */
    public void onRefresh(IRequestCycle cycle) {
        IValidationDelegate delegate = getDelegate();
        if (delegate.getHasErrors()) {
            setMessage("Please fill in all the required fields.");
        }
    }

    /**
     * Removes the current project.
     * @return
     */
    private int removeCurrentProject() {
        List<ProjectHelper> l = getProjectHelperList();
        l.remove(getProjectHelper());
        if (l.size() > 0) {
            setProjectHelper(l.get(0));
            setTabIndex(0);
        } else {
            return 0;
        }
        return l.size();
    }

    /**
     * Saves the project
     * @param cycle
     * @return
     */
    public String onSave(IRequestCycle cycle) {
        IValidationDelegate delegate = getDelegate();
        if (getSubmitSource().equals("cancel")) {
            getProjectHelper().setModified(false);
            int i = removeCurrentProject();
            if (i == 0) {
                return "Home";
            }
            return null;
        }
        if (delegate.getHasErrors()) {
            setMessage("Please fill in all the required fields");
            return null;
        }
        String cronExpression = getProjectHelper().getProject().getString(Fields.Project.SCHEDULE);
        if (cronExpression != null) {
            if (!CronExpression.isValidExpression(cronExpression)) {
                setMessage("The schedule value has a syntax error.");
                return null;
            }
        }
        if (getSubmitSource().equals("saveCopy")) {
            List<GenericValue> projectCopy = getProjectHelper().getExportList();
            _log.info("Saving project copy containing " + projectCopy.size() + " item(s)");
            Importer importer = new Importer();
            importer.setCreatedBy(getUserObj().getUserId());
            try {
                Long newProjectId = importer.projectImport(projectCopy);
                _log.info("Created project: " + newProjectId);
                openProject(newProjectId);
                getProjectHelper().setCreatedBy(getUserObj().getUserId());
            } catch (Exception e) {
                _log.error("Error copying project entities", e);
            }
            return null;
        }
        _log.info("Saving project [" + getProjectHelper().getProject().get("name") + "]");
        getProjectHelper().setUpdatedBy(getUserObj().getUserId());
        getProjectDAO().saveList(getProjectHelper().getSaveAndUpdateList());
        getProjectDAO().removeList(getProjectHelper().getDeleteList());
        getProjectDAO().updateProjectTaskLinks(getProjectHelper().getProject());
        getProjectHelper().setModified(false);
        if (getSubmitSource().equals("save")) {
            int i = removeCurrentProject();
            if (i == 0) {
                return "Home";
            }
        }
        return null;
    }

    /**
     * Creates a new project
     */
    public void createProject() {
        setProjectHelper(new ProjectHelper());
        getProjectHelperList().add(getProjectHelper());
        getProjectHelper().create();
        getProjectHelper().setCreatedBy(getUserObj().getUserId());
    }

    /**
     * Opens a project for editing
     * @param project
     */
    public void openProject(Long project) {
        setTaskEdit(null);
        int idx;
        if ((idx = isProjectLoaded(project)) > -1) {
            changeProjectView(idx);
        } else {
            setProjectHelper(new ProjectHelper());
            getProjectHelper().load(project);
            getProjectHelperList().add(getProjectHelper());
        }
    }

    /**
     * Check if the project is already loaded.
     * @param project
     * @return
     */
    private int isProjectLoaded(Long project) {
        List<ProjectHelper> helpers = getProjectHelperList();
        for (ProjectHelper helper : helpers) {
            if (helper != null && helper.getProject().getLong(Fields.Project.ID).compareTo(project) == 0) {
                return helpers.indexOf(helper);
            }
        }
        return -1;
    }

    /**
     * Return the list of project helpers. This will initialize the list.
     * @return
     */
    private List<ProjectHelper> getProjectHelperList() {
        if (getProjectHelpers() == null) {
            setProjectHelpers(new ArrayList<ProjectHelper>());
        }
        return getProjectHelpers();
    }

    /**
     * Returns the css class for the active tab.
     * @return
     */
    public String getTabActiveClass() {
        if (getTabIndex() != null && getProjectHelper() != null && getProjectHelperList().size() >= getTabIndex()) {
            if (getProjectHelper().getProject().getLong(Fields.Project.ID) == getProjectHelperList().get(getTabIndex()).getProject().getLong(Fields.Project.ID)) {
                return "active";
            }
        }
        return null;
    }

    /**
     * Change the active editor view.
     * @param helperIndex
     */
    public void changeProjectView(int helperIndex) {
        setProjectHelper(getProjectHelpers().get(helperIndex));
        clearEditProperties();
    }

    /**
     * Clear the properties for task editing. 
     */
    private void clearEditProperties() {
        setTaskEdit(null);
        setTaskEditor(null);
        setTaskEditId(-1);
    }

    /**
     * Opens the Preview page
     * @param cycle
     */
    public void showPreview(IRequestCycle cycle) {
        cycle.activate(getPreviewPage());
    }

    /**
     * Returns the css class for disabled tasks. 
     * @return
     */
    public String getTaskEnabled() {
        if (getTask().getTask().getString("enabled").equals(Constants.VALUE_DISABLED)) {
            return "task-disabled";
        } else {
            return "";
        }
    }

    /**
     * Opens the type editor dialog.
     */
    public void showTypeDialog() {
        getTypeDialog().show();
    }

    /**
     * Toggle the task default option.
     * @param idx
     */
    public void toggle(int idx) {
        getProjectHelper().getTaskList().get(idx).toggleDefaultTask();
        getProjectHelper().setModified(true);
    }

    /**
     * Get the asset to display for the RG Only column.
     * @return
     */
    public IAsset getToggleState() {
        if (getProjectHelper().getTaskList().get(getTaskIndex()).isDefaultTask()) {
            return getAsset("toggle2On");
        } else {
            return getAsset("toggle2Off");
        }
    }

    /**
     * Check the list of helpers for changes.
     * @return
     */
    public boolean isModified() {
        List<ProjectHelper> helpers = getProjectHelperList();
        for (ProjectHelper helper : helpers) {
            if (helper != null && helper.isModified()) {
                return true;
            }
        }
        return false;
    }
}
