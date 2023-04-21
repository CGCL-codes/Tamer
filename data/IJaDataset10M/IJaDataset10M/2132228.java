package org.acegisecurity.acl.basic;

import org.springframework.dao.DataAccessException;

/**
 * Represents a more extensive data access object
 * for {@link BasicAclEntry}s.
 * 
 * <P>
 * <code>BasicAclExtendedDao</code> implementations are responsible for interpreting a
 * a given {@link AclObjectIdentity}.
 * </p>
 * 
 * @author Ben Alex
 * @version $Id: BasicAclExtendedDao.java,v 1.2 2005/11/17 00:55:47 benalex Exp $
 */
public interface BasicAclExtendedDao extends BasicAclDao {

    public void create(BasicAclEntry basicAclEntry) throws DataAccessException;

    /**
	 * Deletes <b>all</b> entries associated with the
	 * specified <code>AclObjectIdentity</code>.
	 * 
	 * @param aclObjectIdentity to delete, including any <code>BasicAclEntry</code>s
	 */
    public void delete(AclObjectIdentity aclObjectIdentity) throws DataAccessException;

    /**
	 * Deletes the <code>BasicAclEntry</code> associated with the specified
	 * <code>AclObjectIdentity</code> and recipient <code>Object</code>.
	 * 
	 * @param aclObjectIdentity to delete
	 * @param recipient to delete
	 */
    public void delete(AclObjectIdentity aclObjectIdentity, Object recipient) throws DataAccessException;

    /**
	 * Changes the permission mask assigned to the <code>BasicAclEntry</code>
	 * associated with the specified
	 * <code>AclObjectIdentity</code> and recipient <code>Object</code>.
	 * 
	 * @param aclObjectIdentity to locate the relevant <code>BasicAclEntry</code>
	 * @param recipient to locate the relevant <code>BasicAclEntry</code>
	 * @param newMask indicating the new permission
	 */
    public void changeMask(AclObjectIdentity aclObjectIdentity, Object recipient, Integer newMask) throws DataAccessException;
}
