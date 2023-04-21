package wilos.presentation.web.task;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import wilos.business.services.misc.concreteactivity.ConcreteActivityService;
import wilos.business.services.misc.project.ProjectService;
import wilos.business.services.spem2.activity.ActivityService;
import wilos.business.services.spem2.role.RoleDescriptorService;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.project.Project;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.model.spem2.process.Process;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.presentation.web.tree.TreeBean;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class ConcreteTaskBean {

    /** project service*/
    private ProjectService projectService;

    /** role descriptior service*/
    private RoleDescriptorService roleDescriptorService;

    /** concrete activity service*/
    private ConcreteActivityService concreteActivityService;

    /** activity service*/
    private ActivityService activityService;

    /** the new name for the task*/
    private String newTaskName;

    /** recur activities*/
    private boolean recurActivities;

    /** visibilty of new task panel*/
    private boolean isVisibleNewTaskPanel = false;

    /** id of the selected role descriptor*/
    private String selectedRoleDescriptorId = "default";

    /** id of the selected concrete activity*/
    private String selectedConcreteActivityId = "default";

    /** the new description of the task*/
    private String newTaskDescription;

    /** add a task*/
    private boolean addTaskRendered = false;

    /**
	 * Method to add an out of process task
	 */
    public String createNewTaskActionListener() {
        Project project = this.projectService.getProject((String) WebSessionService.getAttribute(WebSessionService.PROJECT_ID));
        if (this.newTaskName.equals("")) {
            WebCommonService.addErrorMessage(LocaleBean.getText("component.tableparticipantProcessProject.taskNameMissing"));
        } else if (this.selectedConcreteActivityId.equals("default")) {
            WebCommonService.addErrorMessage(LocaleBean.getText("component.tableparticipantProcessProject.taskActivityMissing"));
        } else if (!this.selectedConcreteActivityId.equals("default")) {
            RoleDescriptor rd;
            if (this.selectedRoleDescriptorId.equals("default")) {
                rd = null;
            } else {
                rd = this.roleDescriptorService.getRoleDescriptor(this.selectedRoleDescriptorId);
            }
            ConcreteActivity cact = this.concreteActivityService.getConcreteActivity(this.selectedConcreteActivityId);
            if (this.projectService.createTask(this.newTaskName, this.newTaskDescription, project, rd, cact, recurActivities)) {
                WebCommonService.addInfoMessage(LocaleBean.getText("component.tableparticipantProcessProject.taskCreated"));
            } else {
                WebCommonService.addErrorMessage(LocaleBean.getText("component.tableparticipantProcessProject.taskNotCreated"));
            }
            TreeBean treeBean = (TreeBean) WebCommonService.getBean("TreeBean");
            treeBean.rebuildProjectTree();
            this.newTaskDescription = "";
            this.newTaskName = "";
            this.selectedConcreteActivityId = "default";
            this.selectedRoleDescriptorId = "default";
            this.addTaskRendered = false;
            this.recurActivities = false;
        }
        return "";
    }

    /**
	 * ChangeListener on the Combobox including the roles
	 * 
	 * @param e
	 */
    public void changeRolesListener(ValueChangeEvent evt) {
        this.selectedRoleDescriptorId = (String) evt.getNewValue();
        this.addTaskRendered = !(this.selectedRoleDescriptorId.equals("default")) && !(this.selectedConcreteActivityId.equals("default"));
    }

    /**
	 * ChangeListener on the Combobox including the concrete activities
	 * 
	 * @param e
	 */
    public void changeConcreteActivitiesListener(ValueChangeEvent evt) {
        this.selectedConcreteActivityId = (String) evt.getNewValue();
        this.addTaskRendered = !(this.selectedRoleDescriptorId.equals("default")) && !(this.selectedConcreteActivityId.equals("default"));
    }

    /**
	 * Give all the roles save in the database for the given process
	 * 
	 * @return
	 */
    public List<SelectItem> getRoles() {
        List<SelectItem> rolesList = new ArrayList<SelectItem>();
        rolesList.add(new SelectItem("default", LocaleBean.getText("component.tableparticipantProcessProject.concreteRolesComboBox")));
        Project project = this.projectService.getProject((String) WebSessionService.getAttribute(WebSessionService.PROJECT_ID));
        if (project != null) {
            Process process = project.getProcess();
            if (process != null) {
                if (!this.selectedConcreteActivityId.equals("default")) {
                    ConcreteActivity cact = this.concreteActivityService.getConcreteActivity(this.selectedConcreteActivityId);
                    SortedSet<BreakdownElement> bdEs = this.activityService.getAllBreakdownElements(cact.getActivity());
                    for (BreakdownElement bde : bdEs) {
                        if (bde instanceof RoleDescriptor) {
                            rolesList.add(new SelectItem(bde.getId(), bde.getPresentationName()));
                        }
                    }
                }
            }
        }
        return rolesList;
    }

    /**
	 * Give all the activities saved in the database for the given process
	 * 
	 * @return
	 */
    public List<SelectItem> getConcreteActivities() {
        List<SelectItem> activityList = new ArrayList<SelectItem>();
        activityList.add(new SelectItem("default", LocaleBean.getText("component.tableparticipantProcessProject.concreteActivityComboBox")));
        Project project = this.projectService.getProject((String) WebSessionService.getAttribute(WebSessionService.PROJECT_ID));
        if (project != null) {
            for (ConcreteActivity cact : this.concreteActivityService.getConcreteActivitiesFromProject(project)) {
                activityList.add(new SelectItem(cact.getId(), cact.getConcreteName()));
            }
        }
        return activityList;
    }

    /**
	 * getter of isVisibleNewTaskPanel boolean attribute
	 * 
	 * @return the isVisibleNewTaskPanel
	 */
    public boolean getIsVisibleNewTaskPanel() {
        Project project = this.projectService.getProject((String) WebSessionService.getAttribute(WebSessionService.PROJECT_ID));
        Process process = this.projectService.getProcessFromProject(project);
        if (process != null) {
            this.isVisibleNewTaskPanel = true;
        } else {
            this.isVisibleNewTaskPanel = false;
        }
        return this.isVisibleNewTaskPanel;
    }

    /**
	 * setter of isVisibleNewTaskPanel boolean attribute
	 * 
	 * @param _isVisibleNewTaskPanel
	 *            the isVisibleNewTaskPanel to set
	 */
    public void setIsVisibleNewTaskPanel(boolean _isVisibleNewTask) {
        this.isVisibleNewTaskPanel = _isVisibleNewTask;
    }

    /**
	 * this method allow to return the current instance of
	 * ProjectService
	 * 
	 * @return ProjectService
	 */
    public ProjectService getProjectService() {
        return projectService;
    }

    /**
	 * this method allow to set the current instance of
	 * ProjectService
	 * 
	 * @param projectService
	 */
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
	 * this method allow to return the current instance of
	 * ConcreteActivityService
	 * 
	 * @return ConcreteActivityService
	 */
    public ConcreteActivityService getConcreteActivityService() {
        return concreteActivityService;
    }

    /**
	 * this method allow to set the current instance of
	 * ConcreteActivityService
	 * 
	 * @param concreteActivityService
	 */
    public void setConcreteActivityService(ConcreteActivityService concreteActivityService) {
        this.concreteActivityService = concreteActivityService;
    }

    /**
	 * this method allow to return the current instance of
	 * ActivityService
	 * 
	 * @return ActivityService
	 */
    public ActivityService getActivityService() {
        return activityService;
    }

    /**
	 * this method allow to set the current instance of
	 * ActivityService
	 * 
	 * @param activityService
	 */
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
	 * setter of newTaskName String attribute
	 * 
	 * @return String
	 */
    public String getNewTaskName() {
        return newTaskName;
    }

    /**
	 * setter of newTaskName String attribute
	 * 
	 * @param newTaskName
	 */
    public void setNewTaskName(String newTaskName) {
        this.newTaskName = newTaskName;
    }

    /**
	 * getter of selectedRoleDescriptorId String attribute
	 * 
	 * @return String
	 */
    public String getSelectedRoleDescriptorId() {
        return selectedRoleDescriptorId;
    }

    /**
	 * setter of selectedRoleDescriptorId String attribute
	 * 
	 * @param selectedRoleDescriptorId
	 */
    public void setSelectedRoleDescriptorId(String selectedRoleDescriptorId) {
        this.selectedRoleDescriptorId = selectedRoleDescriptorId;
    }

    /**
	 * getter of selectedConcreteActivityId String attribute
	 * 
	 * @return String
	 */
    public String getSelectedConcreteActivityId() {
        return selectedConcreteActivityId;
    }

    /**
	 * setter of selectedConcreteActivityId String attribute
	 * 
	 * @param selectedConcreteActivityId
	 */
    public void setSelectedConcreteActivityId(String selectedConcreteActivityId) {
        this.selectedConcreteActivityId = selectedConcreteActivityId;
    }

    /**
	 * getter of newTaskDescription boolean attribute
	 * 
	 * @return String
	 */
    public String getNewTaskDescription() {
        return newTaskDescription;
    }

    /**
	 * setter of newTaskDescription String attribute
	 * 
	 * @param newTaskDescription
	 */
    public void setNewTaskDescription(String newTaskDescription) {
        this.newTaskDescription = newTaskDescription;
    }

    /**
	 * this method allow to return the current instance of 
	 * roleDescriptorService 
	 *  
	 * @return RoleDescriptorService
	 */
    public RoleDescriptorService getRoleDescriptorService() {
        return roleDescriptorService;
    }

    /**
	 * this method allow to set the current instance of 
	 * roleDescriptorService 
	 * 
	 * @param roleDescriptorService
	 */
    public void setRoleDescriptorService(RoleDescriptorService roleDescriptorService) {
        this.roleDescriptorService = roleDescriptorService;
    }

    /**
	 * setter of visibleRoleComboBox boolean attribute
	 * 
	 * @return boolean the current value of visibleRoleComboBox attribute
	 */
    public boolean getRecurActivities() {
        return recurActivities;
    }

    /**
	 * setter of recurActivities boolean attribute
	 * 
	 * @param recurActivities
	 */
    public void setRecurActivities(boolean recurActivities) {
        this.recurActivities = recurActivities;
    }

    /**
	 * getter of addTaskRendered boolean attribute
	 *  
	 * @return boolean the current value of addTaskRendered attribute
	 */
    public boolean getAddTaskRendered() {
        return addTaskRendered;
    }

    /**
	 * setter of addTaskRendered boolean attribute
	 * 
	 * @param addTaskRendered
	 */
    public void setAddTaskRendered(boolean addTaskRendered) {
        this.addTaskRendered = addTaskRendered;
    }
}
