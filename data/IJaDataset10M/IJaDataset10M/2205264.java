package ipmss.services.common;

import java.util.List;
import ipmss.entity.authorities.User;
import ipmss.entity.desktop.Desktop;
import ipmss.entity.planning.Project;

/**
 * The Interface UserContext.
 *
 * @author Michał Czarnik
 */
public interface UserContext {

    /**
     * Gets the current project.
     *
     * @return the current project
     */
    public Project getCurrentProject();

    /**
     * Sets the current project.
     *
     * @param currentProject the new current project
     */
    public void setCurrentProject(Project currentProject);

    /**
     * Gets the current view.
     *
     * @return the current view
     */
    public Desktop getCurrentView();

    /**
     * Sets the current view.
     *
     * @param currentView the new current view
     */
    public void setCurrentView(Desktop currentView);

    /**
     * Gets the projects.
     *
     * @return the projects
     */
    public List<Project> getProjects();

    /**
     * Sets the projects.
     *
     * @param projects the new projects
     */
    public void setProjects(List<Project> projects);

    /**
     * Gets the views.
     *
     * @return the views
     */
    public List<Desktop> getViews();

    /**
     * Sets the views.
     *
     * @param views the new views
     */
    public void setViews(List<Desktop> views);

    /**
     * Inits the.
     *
     * @param userName the user name
     */
    public void init(String userName);

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName();

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public int getUserId();

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser();

    /**
     * Change desktop.
     *
     * @param desktopName the desktop name
     * @return the desktop
     */
    public Desktop changeDesktop(String desktopName);
}
