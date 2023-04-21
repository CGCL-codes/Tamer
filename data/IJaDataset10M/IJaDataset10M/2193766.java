package com.sitescape.team.module.binder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dom4j.Document;
import com.sitescape.team.domain.Binder;
import com.sitescape.team.domain.NoBinderByTheIdException;
import com.sitescape.team.domain.Subscription;
import com.sitescape.team.domain.EntityIdentifier.EntityType;
import com.sitescape.team.jobs.ScheduleInfo;
import com.sitescape.team.module.file.WriteFilesException;
import com.sitescape.team.module.shared.InputDataAccessor;
import com.sitescape.team.security.AccessControlException;

/**
 * @author Janet McCann
 *
 */
public interface BinderModule {

    /**
	 * Subscribe to a binder.  Use to request notification of changes.
	 * @param binderId
	 * @param style
	 */
    public void addSubscription(Long binderId, int style);

    /**
	 * Delete a binder including any sub-binders and entries
	 * Any errors deleting child-binders will be returned, but
	 * will continue deleting as much as possible
	 * @param binderId
	 * @return List of errors when deleting child binders
	 * @throws AccessControlException
	 */
    public List deleteBinder(Long binderId) throws AccessControlException;

    /**
	 * Stop receiveing notifications that you have explicity requested.
	 * @param binderId
	 */
    public void deleteSubscription(Long binderId);

    /**
	 * Delete a tag on a binder
	 * @param binderId
	 * @param tagId
	 * @throws AccessControlException
	 */
    public void deleteTag(Long binderId, String tagId) throws AccessControlException;

    /**
	 * Execute a search query.  Read access is automatically checked
	 * @param searchQuery
	 * @return
	 */
    public Map executeSearchQuery(Document searchQuery);

    /**
     * Same as <code>executeSearchQuery</code>
     * @param searchQuery
     * @param options
     * @return
     */
    public Map executeSearchQuery(Document searchQuery, Map options);

    /**
     * Get a binder
     * @param binderId
     * @return
     * @throws NoBinderByTheIdException
     * @throws AccessControlException
     */
    public Binder getBinder(Long binderId) throws NoBinderByTheIdException, AccessControlException;

    /**
     * Get the list of community tags on the binder
     * @param binderId
     * @return
     * @throws AccessControlException
     */
    public List getCommunityTags(Long binderId) throws AccessControlException;

    /**
     * Get a list of personal tags on a binder
     * @param binderId
     * @return
     * @throws AccessControlException
     */
    public List getPersonalTags(Long binderId) throws AccessControlException;

    /**
     * Finds a binder by path name. If no binder exists with the path name,
     * it returns <code>null</code>. If a matching binder exists but the
     * user has no access to it, it throws <code>AccessControlException</code>.
     * 
     * @param pathName
     * @return
     * @throws AccessControlException
     */
    public Binder getBinderByPathName(String pathName) throws AccessControlException;

    /**
     * Get the current schedule information for email notifications on this binder.
     * If the binder is a sub-folder, its schedule is handled by the topFolder
     * @param binderId
     * @return
     */
    public ScheduleInfo getNotificationConfig(Long binderId);

    /**
     * 
     * @param wordroot
     * @param type
     * @return
     */
    public ArrayList getSearchTags(String wordroot, String type);

    /**
     * Get your subscription to this binder
     * @param binderId
     * @return
     */
    public Subscription getSubscription(Long binderId);

    /**
	 * Get a list of team members for the given binder
	 * @param binderId
	 * @param explodeGroups
	 * @return
	 * @throws AccessControlException
	 */
    public List getTeamMembers(Long binderId, boolean explodeGroups) throws AccessControlException;

    public Set getTeamMemberIds(Long binderId, boolean explodeGroups) throws AccessControlException;

    /**
	 * Same as <code>getTeamMemberIds</code> except no access checks are
	 * performed.  This should only be used internally
	 * @param binder
	 * @param explodeGroups
	 * @return
	 */
    public Set getTeamMemberIds(Binder binder, boolean explodeGroups);

    /**
	 * See if have there are any child binders.  Checks access
	 * @param binder
	 * @return
	 */
    public boolean hasBinders(Binder binder);

    /**
	 * Same as <code>hasBinders</code>, but checks for a specific type
	 * @param binder
	 * @param binderType
	 * @return
	 */
    public boolean hasBinders(Binder binder, EntityType binderType);

    public boolean hasTeamMembers(Long binderId, boolean explodeGroups) throws AccessControlException;

    /**
	 * Index only the binder and its attachments.  Do not include entries or sub-binders
	 * @param binderId
	 */
    public void indexBinder(Long binderId);

    /**
	 * Index the binder and its attachments and optionally its entries.  Do not index sub-binders
	 * @param binderId
	 * @param includeEntries
	 */
    public void indexBinder(Long binderId, boolean includeEntries);

    /**
     * Index a binder and its child binders, including all entries
     * @param binderId
     * @return
     */
    public Collection indexTree(Long binderId);

    /**
     * Same as <code>indexTree</code> except can include a list of sub-binders to skip.
     * @param binderId
     * @param exclusions - sub-binders to skip
     * @return Collection binderIds indexed
     */
    public Collection indexTree(Long binderId, Collection exclusions);

    /**
     * Modify a binder
     * @param binderId
     * @param inputData
     * @throws AccessControlException
     * @throws WriteFilesException
     */
    public void modifyBinder(Long binderId, InputDataAccessor inputData) throws AccessControlException, WriteFilesException;

    /**
     * Same as <code>modifyBinder</code>.  Optionally include files to add and attachments to delete 
     * @param binderId
     * @param inputData
     * @param fileItems
     * @param deleteAttachments
     * @throws AccessControlException
     * @throws WriteFilesException
     */
    public void modifyBinder(Long binderId, InputDataAccessor inputData, Map fileItems, Collection deleteAttachments) throws AccessControlException, WriteFilesException;

    /**
     * Modify who gets email notifications.  The upates are applied to the <code>NotificationDef</code> for this binder
     * @param binderId
     * @param updates
     * @param principals
     */
    public void modifyNotification(Long binderId, Map updates, Collection principals);

    /**
     * Change the name of an existing tag. 
     * @param binderId
     * @param tagId
     * @param newTag
     * @throws AccessControlException
     */
    public void modifyTag(Long binderId, String tagId, String newTag) throws AccessControlException;

    /**
	 * Move a binder, all of its entries and sub-binders
	 * @param fromId - the binder to move
	 * @param toId - destination id
	 */
    public void moveBinder(Long binderId, Long toId);

    /**
	 * Set the definition inheritance on a binder
	 * @param binderId
	 * @param inheritFromParent
	 * @return
	 * @throws AccessControlException
	 */
    public Binder setDefinitions(Long binderId, boolean inheritFromParent) throws AccessControlException;

    /**
     * Modify the list of definitions assocated with a binder
     * @param binderId
     * @param definitionIds
     * @throws AccessControlException
     */
    public Binder setDefinitions(Long binderId, List definitionIds) throws AccessControlException;

    /**
     * Modify the list of definitions and workflows assocated with a binder
     * @param binderId
     * @param definitionIds
     * @param workflowAssociations
     * @throws AccessControlException
     */
    public Binder setDefinitions(Long binderId, List definitionIds, Map workflowAssociations) throws AccessControlException;

    /**
     * Set the schedule by which notifications are sent.  Use this to both enable and disable notifications
     * @param binderId
     * @param config
     */
    public void setNotificationConfig(Long binderId, ScheduleInfo config);

    /**
	 * Update the <code>com.sitescape.team.domain.PostingDef</code> associated with this binder.
	 * If one doesn't exists, create one.  
	 * If emailAddress is null, delete the current <code>PostingDef</code>
	 * @param binderId
	 * @param updates
	 */
    public void setPosting(Long binderId, Map updates);

    /**
     * Same as <code>setPosting</code>
     * @param binderId
     * @param emailAddress
     */
    public void setPosting(Long binderId, String emailAddress);

    /**
     * Set a property to be associated with this binder
     * @param binderId
     * @param property
     * @param value
     */
    public void setProperty(Long binderId, String property, Object value);

    /**
     * Create a new tag for this binder
     * @param binderId
     * @param newtag
     * @param community
     * @throws AccessControlException
     */
    public void setTag(Long binderId, String newtag, boolean community) throws AccessControlException;

    /**
	 * Test access to a binder.  The method name to be called is used as the operation.   This
	 * allows the binderModule to check for multiple rights or change requirments in the future.
	 * @param binderId
	 * @param operation - the method name
	 * @return
	 */
    public boolean testAccess(Long binderId, String operation);

    /**
	 * Same as <code>testAccess</code> 
	 * @param binder
	 * @param operation
	 * @return
	 */
    public boolean testAccess(Binder binder, String operation);
}
