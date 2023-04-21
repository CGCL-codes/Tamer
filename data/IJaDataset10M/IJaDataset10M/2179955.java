package org.exist.security;

import java.util.Set;
import org.exist.security.realm.Realm;
import org.exist.xmldb.XmldbURI;

public interface Account extends Principal, User {

    public static final int UNDEFINED_ID = -1;

    public static final int PLAIN_ENCODING = 0;

    public static final int SIMPLE_MD5_ENCODING = 1;

    public static final int MD5_ENCODING = 2;

    /**
	 * Add the user to a group
	 *
	 * @param  name  The feature to be added to the Group attribute
	 * @throws PermissionDeniedException 
	 */
    @Override
    public Group addGroup(String name) throws PermissionDeniedException;

    /**
	 * Add the user to a group
	 *
	 * @param  group  The feature to be added to the Group attribute
	 * @throws PermissionDeniedException 
	 */
    @Override
    public Group addGroup(Group group) throws PermissionDeniedException;

    /**
	 *  Remove the user to a group
	 *  Added by {Marco.Tampucci and Massimo.Martinelli}@isti.cnr.it  
	 *
	 *@param  group  The feature to be removed to the Group attribute
	 */
    @Override
    public void remGroup(String group) throws PermissionDeniedException;

    /**
	 *  Get all groups this user belongs to
	 *
	 *@return    The groups value
	 */
    @Override
    public String[] getGroups();

    @Override
    public int[] getGroupIds();

    @Override
    public boolean hasDbaRole();

    /**
	 *  Get the primary group this user belongs to
	 *
	 *@return    The primaryGroup value
	 */
    @Override
    public String getPrimaryGroup();

    @Override
    public Group getDefaultGroup();

    /**
	 *  Is the user a member of group?
	 *
	 *@param  group  Description of the Parameter
	 *@return        Description of the Return Value
	 */
    @Override
    public boolean hasGroup(String group);

    /**
	 *  Sets the password attribute of the User object
	 *
	 * @param  passwd  The new password value
	 */
    @Override
    public void setPassword(String passwd);

    @Override
    public void setHome(XmldbURI homeCollection);

    @Override
    public XmldbURI getHome();

    @Override
    public Realm getRealm();

    /**
	 * Get the user's password
	 * 
	 * @return Description of the Return Value
	 * @deprecated
	 */
    @Override
    public String getPassword();

    @Deprecated
    @Override
    public String getDigestPassword();

    @Deprecated
    @Override
    public void setGroups(String[] groups);

    /**
     * Returns the person full name or account name.
     *
     * @return the person full name or account name
     */
    @Override
    String getUsername();

    /**
     * Indicates whether the account has expired. Authentication on an expired account is not possible.
     *
     * @return <code>true</code> if the account is valid (ie non-expired), <code>false</code> if no longer valid (ie expired)
     */
    @Override
    boolean isAccountNonExpired();

    /**
     * Indicates whether the account is locked or unlocked. Authentication on a locked account is not possible.
     *
     * @return <code>true</code> if the account is not locked, <code>false</code> otherwise
     */
    @Override
    boolean isAccountNonLocked();

    /**
     * Indicates whether the account's credentials has expired. Expired credentials prevent authentication.
     *
     * @return <code>true</code> if the account's credentials are valid (ie non-expired), <code>false</code> if no longer valid (ie expired)
     */
    @Override
    boolean isCredentialsNonExpired();

    /**
     * Indicates whether the account is enabled or disabled. Authentication on a disabled account is not possible.
     *
     * @return <code>true</code> if the account is enabled, <code>false</code> otherwise
     */
    @Override
    boolean isEnabled();

    public void assertCanModifyAccount(Account user) throws PermissionDeniedException;

    /**
     * Get the umask of the user
     */
    public int getUserMask();

    public void setMetadataValue(SchemaType schemaType, String value);

    public String getMetadataValue(SchemaType schemaType);

    public Set<SchemaType> getMetadataKeys();

    public void clearMetadata();
}
