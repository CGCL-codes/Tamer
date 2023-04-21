package org.openuss.security.acegi.acl;

import java.util.List;
import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.BasicAclDao;
import org.acegisecurity.acl.basic.BasicAclEntry;
import org.openuss.security.acl.ObjectIdentity;
import org.openuss.security.acl.ObjectIdentityDao;
import org.openuss.security.acl.PermissionDao;

/**
 * Adapter to connect the acegi dao layer with the hibernate permission dao objects. 
 * @author Ingo Dueppe
 */
public class PermissionAclDaoAdapter implements BasicAclDao {

    public static final String RECIPIENT_USED_FOR_INHERITENCE_MARKER = "___INHERITENCE_MARKER_ONLY___";

    private PermissionDao permissionDao;

    private ObjectIdentityDao objectIdentityDao;

    /**
	 * {@inheritDoc}
	 */
    public BasicAclEntry[] getAcls(AclObjectIdentity aclObjectIdentity) {
        if (aclObjectIdentity instanceof EntityObjectIdentity) {
            EntityObjectIdentity objIdentity = (EntityObjectIdentity) aclObjectIdentity;
            ObjectIdentity identity = objectIdentityDao.load(objIdentity.getIdentifier());
            if (identity == null) {
                return null;
            }
            List<BasicAclEntry> entries = getBasicAclPermissions(identity);
            if (entries.size() == 0) {
                return new BasicAclEntry[] { createBasicAclEntry(identity) };
            } else {
                return (BasicAclEntry[]) entries.toArray(new BasicAclEntry[entries.size()]);
            }
        }
        return null;
    }

    /**
	 * Create an inheritence marker of a known object identity
	 * @param identity
	 * @return BasicAclEntry
	 */
    private BasicAclEntry createBasicAclEntry(ObjectIdentity identity) {
        Long parentId = identity.getParent() == null ? null : identity.getParent().getId();
        return new AclPermissionAdapter(RECIPIENT_USED_FOR_INHERITENCE_MARKER, 0, identity.getId(), parentId);
    }

    /**
	 * @param identifier
	 * @return List<BasicAclEntry>
	 */
    private List<BasicAclEntry> getBasicAclPermissions(ObjectIdentity objectIdentity) {
        return permissionDao.findPermissionsWithRecipient(objectIdentity);
    }

    public PermissionDao getPermissionDao() {
        return permissionDao;
    }

    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    public ObjectIdentityDao getObjectIdentityDao() {
        return objectIdentityDao;
    }

    public void setObjectIdentityDao(ObjectIdentityDao objectIdentityDao) {
        this.objectIdentityDao = objectIdentityDao;
    }
}
