package com.fddtool.pd.fddproject;

import java.util.List;
import javax.swing.tree.TreeModel;
import com.fddtool.exception.DuplicateNameException;
import com.fddtool.exception.InvalidOperationException;
import com.fddtool.exception.InvalidStateException;
import com.fddtool.exception.NotFoundException;
import com.fddtool.pd.account.Person;
import com.fddtool.pd.common.AppEntry;
import com.fddtool.pd.common.AppEntryType;
import com.fddtool.resource.MessageKey;
import com.fddtool.si.system.SystemIntegrationFactory;
import com.fddtool.util.Utils;

/**
 * Project group is a collection of projects and sub- project groups. While
 * projects may be accessed by many users, the project groups are private. Only
 * creator of project group can see it. This allows the users of FDDPMA
 * applications to build their own hierarchies of groups and add the projects to
 * the groups at user's discretion, without affecting other users.
 * <p>
 * There is one special project group that exists for all users: root project
 * group. This group is not stored in the persistent storage. It exists only to
 * allow UI to display all the projects and groups under single root.
 * 
 * @author Serguei Khramtchenko
 */
public class ProjectGroup extends AppEntry {

    /**
     * The identifier for root project group.
     */
    protected static final String ROOT_PROJECT_GROUP_ID = "-1";

    /**
     * The cached root project group.
     */
    private static ProjectGroup ROOT_GROUP;

    /**
     * The person who created this project group.
     */
    private Person creator;

    /**
     * The parent project group. It may be <code>null</code> if this is a
     * top-level project group.
     */
    private ProjectGroup parent;

    /**
     * Private constructor that creates a root project group. Root project group
     * does not have parent, or creator. The creator of this group is always a
     * user who is currently logged in FDDPMA.
     * 
     * @param id
     *            String containing a unique identifier of the group.
     * @param name
     *            String name of the group.
     */
    private ProjectGroup(String id, MessageKey nameKey) {
        super(id, nameKey, AppEntryType.PROJECT_GROUP);
    }

    /**
     * Creates a new instance of project group.
     * 
     * @param id
     *            String containing a unique identifier of the group.
     * @param name
     *            String name of the group.
     * @param creator
     *            Person who will be recorded as a creator of the group.
     * @param parent
     *            ProjectGroup that this group will belong to. If this will be a
     *            top-level project group - specify root group here.
     */
    public ProjectGroup(String id, String name, Person creator, ProjectGroup parent) {
        super(id, name, AppEntryType.PROJECT_GROUP);
        Utils.assertNotNullParam(creator, "creator");
        Utils.assertNotNullParam(parent, "parent");
        this.creator = creator;
        this.parent = parent;
    }

    /**
     * Returns a person who created this group.
     * 
     * @return Person object who created the group. If this is a root group -
     *         then returns the logged in person.
     */
    public Person getCreator() {
        if (isRootGroup()) {
            return Person.findCurrent();
        } else {
            return creator;
        }
    }

    /**
     * Returns the project group that this group belongs to.
     * 
     * @return ProjectGroup object or <code>null</code> if this group is a
     *         top-level group.
     */
    public ProjectGroup getParent() {
        return parent;
    }

    public AppEntry findParent() {
        return getParent();
    }

    /**
     * Assigns a new parent to this project group. The modifications are not
     * saved into persistent storage until {@link #save() save} method is
     * called.
     * 
     * @param parent
     *            ProjectGroup to become a new parent for this group. If this
     *            group has to become a top-level group, specify root group
     *            here.
     * 
     * @throws InvalidOperationException
     *             if this instance is not mutable
     * 
     * @see #duplicateForModification()
     * @see #getRootProjectGroup()
     */
    public void setParent(ProjectGroup parent) {
        checkMutable();
        this.parent = parent;
    }

    /**
     * Assigns a new name to this project group. The modifications are not saved
     * into persistent storage until {@link #save() save} method is called.
     * 
     * @param name
     *            String containing a new name for this group.
     * 
     * @throws InvalidOperationException
     *             if this instance is not mutable
     * 
     * @see #duplicateForModification()
     */
    public void setName(String name) {
        Utils.assertNotNullParam(name, "name");
        checkMutable();
        super.setName(name);
    }

    /**
     * Indicates if this project represents a root project group. The root
     * project group does not represent a real group. It only exists to
     * logically group all the projects that a user has access to.
     * 
     * @return boolean value which is <code>true</code> if this is a root
     *         project group and <code>false</code> if it is not.
     */
    public boolean isRootGroup() {
        return Utils.nullSafeEquals(getId(), ROOT_PROJECT_GROUP_ID, true);
    }

    /**
     * Saves this project group in the persistent storage. This method may be
     * used to store a brand new group, or update an existing group. It tries to
     * update an existing group if the <code>id</code> property is non-zero.
     * Otherwise it tries to create a new group in the persistent storage.
     * 
     * @throws DuplicateNameException
     *             if a new group is being created, but a group with the same
     *             name already exists for the same creator. This exception may
     *             also be thrown if the group is being updated, and its new
     *             name is not unique for the creator user.
     * 
     * @throws NotFoundException
     *             if a group is being updated, but it does not exist in the
     *             persistent storage.
     * @throws InvalidOperationException
     *             if this group's parent is set to be this group.
     */
    public void save() throws DuplicateNameException, NotFoundException, InvalidOperationException {
        if (isRootGroup()) {
            throw new InvalidStateException(MessageKey.ERROR_CANNOT_SAVE_ROOT_PROJECT_GROUP);
        }
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        if (Utils.isEmpty(getId())) {
            String id = ps.addProjectGroup(this);
            setId(id);
        } else {
            checkMutable();
            ProjectGroup parent = getParent();
            while (parent != null) {
                if (parent.getId().equals(getId())) {
                    throw new InvalidOperationException(MessageKey.ERROR_GROUP_CANNOT_BE_PARENT_OF_ITSELF);
                }
                parent = parent.getParent();
            }
            ps.updateProjectGroup(this);
        }
    }

    /**
     * Lists the project groups that are created by a given person.
     * 
     * @param person
     *            Person for whom to return available project groups.
     * 
     * @return TreeModel of project groups. The root of the tree is a dummy
     *         project group.
     */
    public static TreeModel findForPerson(Person person) {
        Utils.assertNotNullParam(person, "person");
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        return ps.constructTreeOfProjectGroupsForPerson(person);
    }

    /**
     * Returns a project group that serves as a root of project hierarchy. This
     * root does not represent a real project. It only serves as a logical
     * grouping of all the projects and groups that a user has access to.
     * 
     * @return ProjectGroup object to be used as a root of project hierarchy.
     */
    public static ProjectGroup getRootProjectGroup() {
        if (ROOT_GROUP == null) {
            ROOT_GROUP = new ProjectGroup(ROOT_PROJECT_GROUP_ID, MessageKey.LBL_ROOT_PROJECT_GROUP);
        }
        return ROOT_GROUP;
    }

    /**
     * Finds project group by the unique identifier.
     * 
     * @param id
     *            String containing an identifier of a group to search for.
     * @return ProjectGroup with given id or <code>null</code> if nothing is
     *         found.
     */
    public static ProjectGroup findById(String id) {
        Utils.assertNotNullParam(id, "id");
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        return ps.findProjectGroupById(id);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ProjectGroup) {
            ProjectGroup other = (ProjectGroup) o;
            return Utils.nullSafeEquals(getId(), other.getId(), true) && Utils.nullSafeEquals(getParent(), other.getParent(), true) && Utils.nullSafeEquals(getName(), other.getName(), true) && Utils.nullSafeEquals(getCreator(), other.getCreator(), true);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = getId() == null ? 0 : getId().hashCode();
        result ^= getParent() == null ? 0 : getParent().hashCode();
        result ^= getName() == null ? 0 : getName().hashCode();
        result ^= getCreator() == null ? 0 : getCreator().hashCode();
        return result;
    }

    /**
     * Returns a list of active projects that belong to this project group only.
     * It does not include projects that belong to sub-groups. It does not
     * include archived projects.
     * 
     * @return List of <code>Project</code> objects.
     */
    public List<Project> listActiveProjects() {
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        return ps.listActiveProjectsForGroup(Person.findCurrent(), this);
    }

    /**
     * Returns a list of projects that belong to this group, or to sub-groups.
     * 
     * @return List of <code>Project</code> objects that are either direct- or
     *         grand-children of this group.
     */
    public List<Project> listSubProjects() {
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        return ps.listSubProjects(Person.findCurrent(), this);
    }

    /**
     * Returns a list of project groups that are children of this group.
     * 
     * @param includeGrandChildren
     *            boolean value which should be <code>false</code> to include
     *            only direct children of this group, and <code>true</code> to
     *            include all the grandchildren.
     * 
     * @return List of <code>ProjectGroup</code> objects.
     */
    public List<ProjectGroup> listGroups(boolean includeGrandChildren) {
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        return ps.listSubGroups(Person.findCurrent(), this, includeGrandChildren);
    }

    /**
     * Adds a specified project into this project group. If the project was a
     * member of any other group - it will be deleted from there.
     * 
     * @param project
     *            Project to be added to this group.
     */
    public void addProject(Project project) {
        Utils.assertNotNullParam(project, "project");
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        ps.assignProjectToGroup(Person.findCurrent(), project, this);
    }

    /**
     * Creates a mutable instance of this class for the purpose of modifying it
     * and saving in the persistent storage. This method provides the way to
     * update project group's properties, as the attempt to call any setter
     * methods on an immutable instance will fail.
     * 
     * @return ProjectGroup which is a mutable instance of this class that has
     *         the same values of properties as this instance.
     * 
     * @throws IllegalStateException
     *             if this instance is already mutable.
     */
    public ProjectGroup duplicateForModification() {
        if (isMutable()) {
            throw new InvalidStateException(MessageKey.ERROR_INSTANCE_ALREADY_MUTABLE);
        }
        if (isRootGroup()) {
            throw new InvalidStateException(MessageKey.ERROR_CANNOT_CHANGE_ROOT_GROUP);
        }
        if (getId() == null) {
            throw new InvalidStateException(MessageKey.ERROR_CANNOT_MODIFY_NOT_PERSISTED_OBJECT);
        }
        ProjectGroup result = new ProjectGroup(getId(), getName(), getCreator(), getParent());
        result.setMutable();
        return result;
    }

    /**
     * Permanently deletes this group from the persistent storage. All the
     * sub-groups and projects will be assigned to a parent of this group.
     * 
     * @throws NotFoundException
     *             if this group is not found (already deleted?)
     * @throws InvalidStateException
     *             if this is a root group.
     */
    public void delete() throws NotFoundException {
        if (isRootGroup()) {
            throw new InvalidStateException(MessageKey.ERROR_CANNOT_DELETE_ROOT_GROUP);
        }
        IProjectSystem ps = SystemIntegrationFactory.getProjectSystem();
        ps.deleteProjectGroup(this);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName()).append(" [");
        buf.append("\n id:").append(getId());
        buf.append("\n name:").append(getName());
        buf.append("\n creator:").append(getCreator());
        buf.append("\n parent:").append(getParent());
        buf.append("]");
        return buf.toString();
    }
}
