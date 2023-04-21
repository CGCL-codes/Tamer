package org.broadleafcommerce.gwt.server.security.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author jfischer
 *
 */
@Entity
@Table(name = "BLC_ADMIN_USER_PERMISSION_XREF")
public class AdminUserPermissionXref {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The category id. */
    @EmbeddedId
    AdminUserPermissionXrefPK adminUserPermissionXrefPK;

    @Column(name = "ADMIN_PERMISSION_ENABLED", nullable = false)
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AdminUserPermissionXrefPK getAdminUserPermissionXrefPK() {
        return adminUserPermissionXrefPK;
    }

    public static class AdminUserPermissionXrefPK implements Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        @Column(name = "ADMIN_USER_ID", nullable = false)
        private Long adminUserId;

        @Column(name = "ADMIN_PERMISSION_ID", nullable = false)
        private Long adminPermissionId;

        public Long getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(Long adminUserId) {
            this.adminUserId = adminUserId;
        }

        public Long getAdminPermissionId() {
            return adminPermissionId;
        }

        public void setAdminPermissionId(Long adminPermissionId) {
            this.adminPermissionId = adminPermissionId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false; else if (!(obj instanceof AdminUserPermissionXrefPK)) return false;
            return adminUserId.equals(((AdminUserPermissionXrefPK) obj).getAdminUserId()) && adminPermissionId.equals(((AdminUserPermissionXrefPK) obj).getAdminPermissionId());
        }

        @Override
        public int hashCode() {
            return adminUserId.hashCode() + adminPermissionId.hashCode();
        }
    }
}
