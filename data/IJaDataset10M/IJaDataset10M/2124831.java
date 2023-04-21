package org.hip.vif.core.bom;

import java.sql.SQLException;
import java.util.Collection;
import org.hip.kernel.bitmap.IDPosition;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.exc.VException;
import org.hip.vif.core.exc.BOMChangeValueException;

/**
 * LinkPermissionRoleHome is responsible to manage instances
 * of class org.hip.vif.bom.LinkPermissionRole.
 *
 * @author: Benno Luthiger
 * @see org.hip.vif.core.bom.LinkPermissionRole
 */
public interface LinkPermissionRoleHome extends DomainObjectHome {

    public static final String KEY_PERMISSION_ID = "PermissionID";

    public static final String KEY_ROLE_ID = "RoleID";

    /**
	 * Retrieves the DomainObject identified by the specified IDPosition
	 * 
	 * @param inPosition org.hip.kernel.bitmap.IDPosition
	 * @return org.hip.kernel.bom.DomainObject
	 * @throws org.hip.vif.core.exc.bom.impl.BOMChangeValueException
	 */
    public DomainObject getEntry(IDPosition inPosition) throws BOMChangeValueException;

    /**
	 * Retrieves the DomainObject identified by the specified values.
	 * 
	 * @param inPermissionID Long
	 * @param inRoleID int
	 * @return {@link DomainObject}
	 * @throws BOMChangeValueException
	 */
    public DomainObject getEntry(Long inPermissionID, int inRoleID) throws BOMChangeValueException;

    /**
	 * Deletes the specified associations of permissions with roles.
	 * 
	 * @param inPositions Collection<IDPosition>
	 * @throws BOMChangeValueException
	 */
    void delete(Collection<IDPosition> inPositions) throws BOMChangeValueException;

    /**
	 * Deletes all roles associated with the specified permission.
	 * 
	 * @param inPermissionID Long
	 * @throws SQLException 
	 * @throws VException 
	 */
    void delete(Long inPermissionID) throws VException, SQLException;

    /**
	 * Creates the specified associations of permissions with roles.
	 * 
	 * @param inPositions Collection<IDPosition>
	 * @throws BOMChangeValueException
	 */
    public void create(Collection<IDPosition> inPositions) throws BOMChangeValueException;

    /**
	 * Associates the specified permission with the specified role.
	 * 
	 * @param inPermissionID Long the permission entry's id
	 * @param inRoleID int, the role entry's id, see {@link RolesConstants}. 
	 * @throws BOMChangeValueException
	 */
    public void createLink(Long inPermissionID, int inRoleID) throws BOMChangeValueException;
}
