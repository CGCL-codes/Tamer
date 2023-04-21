package pl.tyszka.rolemanager.rbac;

import java.util.Set;
import java.util.Collection;
import pl.tyszka.rolemanager.exceptions.RepositoryException;

/**
 * This class defines the administrative operations for the creation 
 * and maintenance of data and instances that represents the role's in the system.
 * 
 * @author  <a href="mailto:daras@users.sourceforge.net">Dariusz Tyszka</a>
 * @version $Revision: 1.1 $
 * 
 * @see <a href="http://csrc.nist.gov/rbac/">Role-Based Access Control</a>
 * 
 * @since 1.0
 */
public abstract class RoleManager {

    /**
     * Creates a new role with a given name.
     * <p>
     * The operation is valid only if the new role does not already exist.
     * Initially, no user or permission is assigned to the new role.
     * </p>
     * 
     * @param name The name of the new role to be created.
     * @return The <code>Role</code> that was created.
     * 
     * @throws RoleExistsException If a role with the specified name already exists. 
     * @throws RepositoryException If a repository exception is encountered.
     */
    public abstract Role addRole(String name) throws RepositoryException, RoleExistsException;

    /**
     * Deletes an existing role from the underlying repository.
     * <p>
     * The operation is valid if and only if the role to be deleted already exists. 
     * </p>
     * <p>
     * The RBAC reference model does not specify how to proceed with the sessions 
     * in which the role to be deleted is active. The implementation of this operation
     * could wait for such a session to terminate normally, it could force the termination 
     * of that session, or it could delete the role from that session while allowing the session to continue.
     * </p>
     * 
     * @param name The name of the role to be deleted.
     * 
     * @throws NoSuchRoleException If a role with the specified name does not exists.
     * @throws RepositoryException If a repository exception is encountered.
     */
    public abstract void deleteRole(String name) throws RepositoryException, NoSuchRoleException;

    /**
     * Returns a set of all roles defined in the system.
     * <p>
     * The Set of the <code>Roles</code> is updated by invocations of 
     * <code>{@link #addRole(String)}</code> and <code>{@link #deleteRole(String)}</code> operations.
     * A new Set is created and returned for each method invocation. 
     * The returned Set may be empty but may not be <code>null</code>.
     * </p>
     * 
     * @return A Set of the <code>Roles</code> defined in the system (never <code>null</code>).
     * 
     * @throws RepositoryException If a repository exception is encountered.
     * 
     * @see #addRole(String)
     * @see #deleteRole(String)
     */
    public abstract Set getRoles();

    public abstract RoleSet createSsdRoleSet();

    public abstract void deleteSsdRoleSet();

    public abstract RoleSet createDsdRoleSet();

    public abstract void deleteDsdRoleSet();

    public abstract Collection getSsdRoleSets();

    public abstract Collection getDsdRoleSets();
}
