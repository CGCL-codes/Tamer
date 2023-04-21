package com.fddtool.ui.view.projectteam;

import java.util.Collections;
import java.util.List;
import com.fddtool.exception.NotFoundException;
import com.fddtool.pd.account.Person;
import com.fddtool.pd.account.Role;
import com.fddtool.pd.fddproject.Project;
import com.fddtool.resource.MessageKey;
import com.fddtool.ui.faces.bean.ManagedBeans;
import com.fddtool.ui.faces.bean.RefreshableManagedBean;
import com.fddtool.ui.view.navigation.NavigationResults;
import com.fddtool.ui.view.tree.TreeProviderBean;
import com.fddtool.util.J2EETransaction;
import com.fddtool.util.Utils;

/**
 * This is a bean that supports "View Project Members" view. It holds
 * information about a single member of the project. Changing this information
 * does not automatically update the persistent storage. The view maintains a
 * list of such objects when displaying members either for a project or project
 * group.
 * 
 * @author Serguei Khramtchenko
 */
public class BaseProjectMemberBean extends RefreshableManagedBean {

    /**
     * Initial serial ID
     */
    private static final long serialVersionUID = 8156119906026801481L;

    /**
     * The name of the member.
     */
    private String name;

    /**
     * The email of the member.
     */
    private String email;

    /**
     * The identifier of the selected person. This is used when deleting the
     * member.
     */
    private String personId;

    /**
     * A comma-separated list of roles that the person has in the project.
     */
    private String roles;

    /**
     * A comma-separated list of projects
     */
    private String projects;

    /**
     * The user name of the member.
     */
    private String userName;

    /**
     * The first name of a member.
     */
    private String firstName;

    /**
     * The first name of a member.
     */
    private String lastName;

    /**
     * Indicates if the current user can delete a member represented by this
     * bean.
     */
    private boolean canDelete;

    /**
     * Returns email address of a member.
     * 
     * @return String containing email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the name of the member. This is usually a concatenation of first
     * and last name.
     * 
     * @return String containing member's full name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets email address for the member.
     * 
     * @param email
     *            String containing email address.
     */
    protected void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the full name of the member.
     * 
     * @param name
     *            String
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Returns identifier of a person who is a member of a project.
     * 
     * @return String containing person's id.
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Sets identifier of a person who is a member of a project. This method is
     * used when deleting project member from the project.
     * 
     * @param personId
     *            String containing person's id.
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * Deletes member from a project.
     * 
     * @return String that represents the result of operation. JSF framework
     *         uses this result to determine the page to forward to. This method
     *         add error messages to be displayed to the user in case of error.
     * 
     * @see com.fddtool.ui.view.NavigationResults#SUCCESS
     * @see com.fddtool.ui.view.NavigationResults#RETRY
     */
    public String delete() {
        J2EETransaction tx = null;
        try {
            Project project = TreeProviderBean.findInstance().findSelectedProject();
            Person person = Person.findById(getPersonId());
            if (project == null) {
                throw new NotFoundException(MessageKey.ERROR_PROJECT_NOT_FOUND);
            }
            if (person == null) {
                throw new NotFoundException(MessageKey.ERROR_ACCOUNT_NOT_FOUND);
            }
            tx = new J2EETransaction();
            tx.begin();
            List<Role> l = Collections.emptyList();
            person.updateProjectRoles(project, l);
            tx.commit();
            ManagedBeans.refresh();
            return NavigationResults.SUCCESS;
        } catch (Exception ex) {
            handleException(ex, tx);
            return NavigationResults.RETRY;
        }
    }

    /**
     * Indicates if the current member can be deleted. The member cannot be
     * deleted if a user does not have right to delete it, or this is the last
     * project manager of the project.
     * 
     * @return boolean value of <code>true</code> if the member can be deleted
     *         or <code>false</code> if it cannot be deleted.
     */
    public boolean isCanDelete() {
        return canDelete;
    }

    /**
     * Returns comma-separated list of projects that a person is a member of.
     * This method is used when displaying members that have projects in a
     * project group.
     * 
     * @return String containing comma-separated list of projects.
     */
    public String getProjects() {
        return projects;
    }

    /**
     * Returns comma-separated list of roles that a person has in a project.
     * This method is used when displaying members of a project.
     * 
     * @return String containing comma-separated list of roles.
     */
    public String getRoles() {
        return roles;
    }

    /**
     * Sets comma-separated list of projects that a person is a member of. This
     * method is used when displaying members that have projects in a project
     * group.
     * 
     * @param projects
     *            String containing comma-separated list of projects.
     */
    public void setProjects(String projects) {
        this.projects = projects;
    }

    /**
     * Sets comma-separated list of roles that a person has in a project.
     * 
     * @param roles
     *            String containing comma-separated list of roles.
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
     * Returns a java script that causes the web browser to display confirmation
     * before deleting a member.
     * 
     * @return String containing the script.
     */
    public String getDeleteConfirmationScript() {
        String personName = "?";
        if (!Utils.isEmpty(personId)) {
            Person person = Person.findById(personId);
            personName = person.getFirstName() + " " + person.getLastName();
        }
        return messageProvider.getMessage(MessageKey.SCRIPT_CONFIRM_DELETE_MEMBERSHIP, new String[] { personName });
    }

    /**
     * Sets the user name for the member.
     * 
     * @param value
     *            String containing the user name.
     */
    protected void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Returns the user name for the member.
     * 
     * @return String containing the user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the first name of the member.
     * 
     * @return String containing user's first name, or <code>null</code> if
     *         the first name was not yet provided.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the member.
     * 
     * @return String containing user's last name, or <code>null</code> if the
     *         last name was not yet provided.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets first name of the member.
     * 
     * @param firstName
     *            String containing user's first name.
     */
    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets last name of the member.
     * 
     * @param lastName
     *            String containing user's last name.
     */
    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This method is called when some bean calls for a global refresh.
     */
    public void refresh() {
    }

    /**
     * Specifies if the current user can delete the member. A user may not
     * delete a member if it is the last project manager or if the user does not
     * have enough permissions.
     * 
     * @param canDelete
     *            boolean value of <code>true</code> if the current user can
     *            delete a member represented by this bean and
     *            <code>false</code> if it cannot.
     */
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    /**
     * Populates person's information.
     * 
     * @param person
     *            the Person whose information is to be displayed.
     */
    public void setPerson(Person person) {
        setPersonId(person.getId());
        setUserName(person.getUserName());
        setEmail(person.getDisplayEmail());
        setName(person.getDisplayFullName());
        setFirstName(person.getDisplayFirstName());
        setLastName(person.getDisplayLastName());
    }
}
