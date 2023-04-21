package ipmss.ui.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import ipmss.dao.planning.ProjectDao;
import ipmss.dao.planning.ProjectDaoBean;
import ipmss.entity.authorities.User;
import ipmss.entity.planning.Project;
import ipmss.entity.planning.Task;
import ipmss.security.Authorities;
import ipmss.services.authorities.UsersManagementService;
import ipmss.services.schedule.ProjectsService;
import ipmss.ui.common.Messages;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;

/**
 * The Class NewProjectViewBean.
 *
 * @author Michał Czarnik class to change when Primefaces bug is resolve
 * (picklist errors)
 */
@Named("newProjectViewBean")
@Scope("session")
public class NewProjectViewBean implements Serializable {

    /** The show content. */
    private boolean showContent = true;

    /** The project name. */
    private String projectName;

    /** The calendar name. */
    private String calendarName;

    /** The project start date. */
    private Date projectStartDate;

    /** The users. */
    private DualListModel<String> users;

    /** The projects service. */
    @Inject
    private ProjectsService projectsService;

    /** The users service. */
    @Inject
    private UsersManagementService usersService;

    /** The project dao. */
    @Inject
    private ProjectDao projectDao;

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        List<String> source = new ArrayList<String>();
        List<String> target = new ArrayList<String>();
        for (User u : usersService.getUsers()) {
            source.add(u.getProfile().getFirstName() + " " + u.getProfile().getSurname());
        }
        setUsers(new DualListModel<String>(source, target));
    }

    /**
     * Gets the project name.
     *
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the project name.
     *
     * @param projectName the new project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * On flow process.
     *
     * @param event the event
     * @return the string
     */
    public String onFlowProcess(FlowEvent event) {
        setShowContent(true);
        return event.getNewStep();
    }

    /**
     * Gets the calendar name.
     *
     * @return the calendar name
     */
    public String getCalendarName() {
        return calendarName;
    }

    /**
     * Sets the calendar name.
     *
     * @param calendarName the new calendar name
     */
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    /**
     * Gets the users.
     *
     * @return the users
     */
    public DualListModel<String> getUsers() {
        return users;
    }

    /**
     * Sets the users.
     *
     * @param users the new users
     */
    public void setUsers(DualListModel<String> users) {
        this.users = users;
    }

    /**
     * Creates the project.
     */
    public void createProject() {
        Project newProject = new Project();
        newProject.setName(projectName);
        List<User> members = new ArrayList<User>();
        for (String su : users.getTarget()) {
            for (User u : usersService.getUsers()) {
                if (su.equals(u.getProfile().getFirstName() + " " + u.getProfile().getSurname())) {
                    members.add(u);
                }
            }
        }
        for (User u : members) {
            newProject.addUser(u);
            u.addProject(newProject);
        }
        Task schedule = new Task();
        schedule.setName(projectName);
        Calendar c = Calendar.getInstance();
        c.setTime(projectStartDate);
        schedule.setStartDate(c);
        newProject.setSchedule(schedule);
        projectDao.addNewProject(newProject);
        for (User u : members) {
            usersService.saveUser(u);
        }
        projectName = "";
        FacesMessage msg = Messages.getMessage(FacesMessage.SEVERITY_INFO, "newProjectMainView.info.projectCreated");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        setShowContent(false);
    }

    /**
     * Gets the show content.
     *
     * @return the show content
     */
    public boolean getShowContent() {
        return showContent;
    }

    /**
     * Sets the show content.
     *
     * @param showContent the new show content
     */
    public void setShowContent(boolean showContent) {
        this.showContent = showContent;
    }

    /**
     * Gets the project start date.
     *
     * @return the project start date
     */
    public Date getProjectStartDate() {
        return projectStartDate;
    }

    /**
     * Sets the project start date.
     *
     * @param projectStartDate the new project start date
     */
    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }
}
