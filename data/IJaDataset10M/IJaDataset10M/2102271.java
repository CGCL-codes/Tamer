package org.openuss.security.ldap;

/**
 * @see org.openuss.security.ldap.UserDnPattern
 */
public class UserDnPatternDaoImpl extends UserDnPatternDaoBase {

    /**
	 * @see org.openuss.security.ldap.UserDnPatternDao#toUserDnPatternInfo(org.openuss.security.ldap.UserDnPattern,
	 *      org.openuss.security.ldap.UserDnPatternInfo)
	 */
    public void toUserDnPatternInfo(UserDnPattern sourceEntity, UserDnPatternInfo targetVO) {
        super.toUserDnPatternInfo(sourceEntity, targetVO);
    }

    /**
	 * @see org.openuss.security.ldap.UserDnPatternDao#toUserDnPatternInfo(org.openuss.security.ldap.UserDnPattern)
	 */
    public UserDnPatternInfo toUserDnPatternInfo(final UserDnPattern entity) {
        UserDnPatternInfo userDnPatternInfo = super.toUserDnPatternInfo(entity);
        return userDnPatternInfo;
    }

    /**
	 * Retrieves the entity object that is associated with the specified value
	 * object from the object store. If no such entity object exists in the
	 * object store, a new, blank entity is created
	 */
    private UserDnPattern loadUserDnPatternFromUserDnPatternInfo(UserDnPatternInfo userDnPatternInfo) {
        UserDnPattern userDnPattern = null;
        if (userDnPatternInfo.getId() != null) {
            userDnPattern = this.load(userDnPatternInfo.getId());
        }
        if (userDnPattern == null) {
            userDnPattern = org.openuss.security.ldap.UserDnPattern.Factory.newInstance();
        }
        return userDnPattern;
    }

    /**
	 * @see org.openuss.security.ldap.UserDnPatternDao#userDnPatternInfoToEntity(org.openuss.security.ldap.UserDnPatternInfo)
	 */
    public UserDnPattern userDnPatternInfoToEntity(UserDnPatternInfo userDnPatternInfo) {
        UserDnPattern entity = this.loadUserDnPatternFromUserDnPatternInfo(userDnPatternInfo);
        this.userDnPatternInfoToEntity(userDnPatternInfo, entity, true);
        return entity;
    }

    /**
	 * @see org.openuss.security.ldap.UserDnPatternDao#userDnPatternInfoToEntity(org.openuss.security.ldap.UserDnPatternInfo,
	 *      org.openuss.security.ldap.UserDnPattern)
	 */
    public void userDnPatternInfoToEntity(UserDnPatternInfo sourceVO, UserDnPattern targetEntity, boolean copyIfNull) {
        super.userDnPatternInfoToEntity(sourceVO, targetEntity, copyIfNull);
    }
}
