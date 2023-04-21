package edu.vt.middleware.gator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import edu.vt.middleware.gator.validation.UniqueName;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

/**
 * Stores security permissions for principals/roles on a project.
 *
 * @author  Middleware Services
 * @version  $Revision: 1452 $
 */
@Entity
@Table(name = "log_permissions")
@SequenceGenerator(name = "permission_sequence", sequenceName = "log_seq_permissions", initialValue = 1, allocationSize = 1)
@UniqueName(message = "{permission.uniqueName}")
public class PermissionConfig extends Config {

    /** All relevant Spring security permissions. */
    public static final Permission[] ALL_PERMISSIONS = new Permission[] { BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE };

    /** PermissionConfig.java. */
    private static final long serialVersionUID = 8440240083147241221L;

    /** Hash code seed. */
    private static final int HASH_CODE_SEED = 65536;

    /** Permission bits that have been or'ed together. */
    private int permissionBits;

    private ProjectConfig project;

    /** Creates a new instance. */
    public PermissionConfig() {
    }

    /**
   * Creates a new instance with the given permissions for the given SID.
   *
   * @param  sid  Security identifier, either user principal name or role name.
   * @param  permBits  Security permissions.
   */
    public PermissionConfig(final String sid, final int permBits) {
        setName(sid);
        setPermissionBits(permBits);
    }

    /**
   * Parses a permission string of the form 'rwd' into an integer where each of
   * the permission bits is set according to the given string.
   *
   * @param  permissionString  Unix-style permission string, e.g. rwd.
   *
   * @return  Integer whose bits are set according to the given string. Returns
   * 0 for a null or empty string.
   */
    public static int parsePermissions(final String permissionString) {
        int bits = 0;
        if (permissionString != null) {
            for (int i = 0; i < permissionString.length(); i++) {
                final char c = permissionString.charAt(i);
                if (c == 'r') {
                    bits += BasePermission.READ.getMask();
                } else if (c == 'w') {
                    bits += BasePermission.WRITE.getMask();
                } else if (c == 'd') {
                    bits += BasePermission.DELETE.getMask();
                } else {
                    throw new IllegalArgumentException("Invalid permission character " + c);
                }
            }
        }
        return bits;
    }

    /** {@inheritDoc}. */
    @Id
    @Column(name = "perm_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_sequence")
    public int getId() {
        return id;
    }

    /** @return  the permissionBits */
    @Column(name = "perm_bits", nullable = false)
    public int getPermissionBits() {
        return permissionBits;
    }

    /** @param  bits  the permissionBits to set */
    public void setPermissionBits(final int bits) {
        this.permissionBits = bits;
    }

    /**
   * Gets the permissions as a unix-style string, e.g. rwd.
   *
   * <ul>
   *   <li>r - Read permission</li>
   *   <li>w - Write permission</li>
   *   <li>d - Delete permission</li>
   * </ul>
   *
   * @return  Permissions as a unix-style string.
   */
    @Transient
    @NotEmpty(message = "{permission.permissions.notEmpty}")
    public String getPermissions() {
        final StringBuilder sb = new StringBuilder(3);
        if (hasPermission(BasePermission.READ)) {
            sb.append('r');
        }
        if (hasPermission(BasePermission.WRITE)) {
            sb.append('w');
        }
        if (hasPermission(BasePermission.DELETE)) {
            sb.append('d');
        }
        return sb.toString();
    }

    /**
   * Sets the permission bits from a unix-style permission string representing
   * the bits to set.
   *
   * @param  perms  Unix-style permission string, e.g. rwd.
   */
    public void setPermissions(final String perms) {
        this.permissionBits = parsePermissions(perms);
    }

    /**
   * Determines whether this instance has the given permission.
   *
   * @param  perm  Permission object.
   *
   * @return  True if this instance has the given permission, false otherwise.
   */
    public boolean hasPermission(final Permission perm) {
        return hasPermission(perm.getMask());
    }

    /**
   * Determines whether this instance has the given permission.
   *
   * @param  perm  Integer value of permission.
   *
   * @return  True if this instance has the given permission, false otherwise.
   */
    public boolean hasPermission(int perm) {
        return (perm & permissionBits) > 0;
    }

    /** @return  the project */
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false, updatable = false)
    public ProjectConfig getProject() {
        return project;
    }

    /** @param  p  the project to set */
    public void setProject(final ProjectConfig p) {
        this.project = p;
    }

    /** {@inheritDoc}. */
    @Transient
    protected int getHashCodeSeed() {
        return HASH_CODE_SEED;
    }
}
