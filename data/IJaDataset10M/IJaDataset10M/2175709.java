package org.kablink.teaming.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.kablink.teaming.NoObjectByTheIdException;
import org.kablink.teaming.dao.util.FilterControls;
import org.kablink.teaming.dao.util.ObjectControls;
import org.kablink.teaming.dao.util.SFQuery;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.BinderQuota;
import org.kablink.teaming.domain.Dashboard;
import org.kablink.teaming.domain.DefinableEntity;
import org.kablink.teaming.domain.Definition;
import org.kablink.teaming.domain.EntityDashboard;
import org.kablink.teaming.domain.EntityIdentifier;
import org.kablink.teaming.domain.IndexNode;
import org.kablink.teaming.domain.LdapConnectionConfig;
import org.kablink.teaming.domain.LibraryEntry;
import org.kablink.teaming.domain.NotifyStatus;
import org.kablink.teaming.domain.PostingDef;
import org.kablink.teaming.domain.Principal;
import org.kablink.teaming.domain.SimpleName;
import org.kablink.teaming.domain.Subscription;
import org.kablink.teaming.domain.Tag;
import org.kablink.teaming.domain.TemplateBinder;
import org.kablink.teaming.domain.UserDashboard;
import org.kablink.teaming.domain.Workspace;
import org.kablink.teaming.domain.ZoneConfig;
import org.springframework.dao.DataAccessException;

/**
 * @author Jong Kim
 *
 */
public interface CoreDao {

    public void bulkLoadCollections(Collection entries);

    /**
	 * 
	 * @parm obj
	 * @throws DataAccessException
	 */
    public void clear();

    public void clearFileNames(Binder binder);

    public void clearTitles(Binder binder);

    public long countObjects(Class clazz, FilterControls filter, Long zoneId);

    public long countObjects(Class clazz, FilterControls filter, Long zoneId, StringBuffer buff);

    public void delete(Object obj);

    public void delete(Binder binder);

    public void delete(Binder binder, Class entryClass);

    public void delete(Definition def);

    public void deleteEntityAssociations(String whereClause);

    public List<Workspace> findCompanies();

    public Long findFileNameEntryId(Binder binder, String name);

    public Workspace findTopWorkspace(String zoneName);

    public void flush();

    public void evict(Object obj);

    public void executeUpdate(String update);

    public void executeUpdate(String update, Map values);

    public boolean isDirty();

    public void lock(Object obj);

    public Object load(Class className, String id);

    public Object load(Class className, Long id);

    public List<Tag> loadAllTagsByEntity(EntityIdentifier entityId);

    public Map<EntityIdentifier, List<Tag>> loadAllTagsByEntity(Collection<EntityIdentifier> entityIds);

    /**
     * 
     * @param binderId
     * @param zoneId
     * @return
     * @throws DataAccessException
     * @throws NoBinderByTheIdException
     */
    public Binder loadBinder(Long binderId, Long zoneId);

    public List<Tag> loadCommunityTagsByEntity(EntityIdentifier entityId);

    public List<TemplateBinder> loadTemplates(Long zoneId);

    public List<TemplateBinder> loadTemplates(Binder binder, Long zoneId, boolean includeAncestors);

    public List<TemplateBinder> loadTemplates(Long zoneId, int type);

    public List<TemplateBinder> loadTemplates(Binder binder, Long zoneId, int type, boolean includeAncestors);

    public Dashboard loadDashboard(String id, Long zoneId);

    public Definition loadDefinition(String defId, Long zoneId);

    public Definition loadDefinitionByName(Binder binder, String name, Long zoneId);

    public List<Definition> loadDefinitions(Long zoneId);

    public List<Definition> loadDefinitions(FilterControls filter, Long zoneId);

    public EntityDashboard loadEntityDashboard(EntityIdentifier ownerId);

    public List<Tag> loadEntityTags(EntityIdentifier entityIdentifier, EntityIdentifier ownerIdentifier);

    public NotifyStatus loadNotifyStatus(Binder binder, DefinableEntity entity);

    public List<NotifyStatus> loadNotifyStatus(String sinceField, Date begin, Date end, int maxResults, Long zoneId);

    public List<NotifyStatus> loadNotifyStatus(Binder binder, String sinceField, Date begin, Date end, int maxResults, Long zoneId);

    public List loadObjects(ObjectControls objs, FilterControls filter, Long zoneId);

    public List loadObjectsCacheable(ObjectControls objs, FilterControls filter, Long zoneId);

    public List loadObjects(Class className, FilterControls filter, Long zoneId);

    public List loadObjectsCacheable(Class className, FilterControls filter, Long zoneId);

    public List loadObjects(Collection ids, Class className, Long zoneId);

    public List loadObjects(String query, Map namedValues);

    public List loadObjects(String query, Map namedValues, Integer maxResults);

    /**
	 * Performance optimization.
	 * Load a list of objects and eagerly fetch listed collections
	 * 
	 * @param ids 
	 * @param className
	 * @param zoneId
	 * @param collections
	 * @return
	 */
    public List loadObjects(Collection ids, Class className, Long zoneId, List collections);

    public List<Tag> loadPersonalTagsByEntity(EntityIdentifier entityId, EntityIdentifier ownerId);

    public List<Tag> loadPersonalTagsByOwner(EntityIdentifier ownerId);

    public List loadPostings(Long zoneId);

    public PostingDef loadPosting(String aliasId, Long zoneId);

    public PostingDef findPosting(String emailAddress, Long zoneId);

    public Binder loadReservedBinder(String reservedId, Long zoneId);

    public Definition loadReservedDefinition(String reservedId, Long zoneId);

    public List<Subscription> loadSubscriptionByEntity(final EntityIdentifier entityId);

    public Tag loadTag(String id, Long zoneId);

    public TemplateBinder loadTemplate(Long templateId, Long zoneId);

    public TemplateBinder loadTemplateByName(String name, Long zoneId);

    public UserDashboard loadUserDashboard(EntityIdentifier ownerId, Long binderId);

    public List<LdapConnectionConfig> loadLdapConnectionConfigs(Long zoneId);

    public ZoneConfig loadZoneConfig(Long zoneId);

    public Object merge(Object obj);

    public void move(Binder binder);

    public SFQuery queryObjects(ObjectControls objs, FilterControls filter, Long zoneId);

    public void refresh(Object obj);

    public void registerFileName(Binder binder, DefinableEntity entity, String name);

    public void registerTitle(Binder binder, DefinableEntity entity);

    public void replicate(Object obj);

    public void save(Object obj);

    public Object saveNewSession(Object obj);

    public Object saveNewSessionWithoutUpdate(Object obj);

    public Object updateNewSessionWithoutUpdate(Object obj);

    public void update(Object obj);

    public double averageColumn(Class clazz, String column, FilterControls filter, Long zoneId);

    public long sumColumn(Class clazz, String column, FilterControls filter, Long zoneId);

    public void updateFileName(Binder binder, DefinableEntity entity, String oldName, String newName);

    public void updateTitle(Binder binder, DefinableEntity entity, String oldName, String newName);

    public void unRegisterFileName(Binder binder, String name);

    public void unRegisterTitle(Binder binder, String name);

    public Long getEntityIdForMatchingTitle(Long binderId, String title);

    public List<Long> findZoneEntityIds(Long entityId, String zoneUUID, String entityType);

    public LibraryEntry getRegisteredTitle(Long binderId, String title);

    public LibraryEntry getRegisteredFileName(Long binderId, String fileName);

    public boolean isTitleRegistered(Long binderId, String title);

    public boolean isFileNameRegistered(Long binderId, String fileName);

    public void addExistingName(LibraryEntry le, DefinableEntity entity);

    public int daysSinceInstallation();

    /**
     * Returns <code>SimpleName</code> matching the criteria, or <code>null</code> if no match is found.
     * 
     * @param name
     * @param zoneId
     * @return
     */
    public SimpleName loadSimpleName(String name, Long zoneId);

    /**
     * Returns <code>SimpleName</code> matching the criteria, or <code>null</code> if no match is found.
     * 
     * @param emailAddress
     * @param zoneId
     * @return
     */
    public SimpleName loadSimpleNameByEmailAddress(String emailAddress, Long zoneId);

    public List<SimpleName> loadSimpleNames(Long binderId, Long zoneId);

    /**
     * Returns <code>IndexNode</code> matching the criteria, or <code>null</code> if no match is found.
     * 
     * @param nodeName
     * @param indexName
     * @return
     */
    public IndexNode findIndexNode(String nodeName, String indexName);

    /**
     * Purge all index nodes matching the specified index name. 
     * Used when deleting a zone.
     * 
     * @param indexName
     */
    public void purgeIndexNodeByIndexName(String indexName);

    /**
	 * Check to see if the definition is in use.
	 * Used to verify if a definition can be deleted.
	 * 
	 * @param def
	 * @return
	 */
    public boolean checkInUse(Definition def);

    public int getLoginCount(final Date startDate);

    public List<String> getLoginInfoIds(final Long zoneId, final Long userId, final String authenticatorName, final Date startDate, final Integer maxResult);

    public List<String> getOldFileVersions(final Long zoneId, final Date ageDate);

    public List getOldBinderFileVersions(final Long zoneId, final Date now);

    /**
	 * Computes disk usage for the binder. 
	 * The result is a sum of the sizes of all files associated with the binder 
	 * as well as the entries within the binder. The computation does NOT 
	 * include files associated with sub-binders. That is, this is one level only.
	 *   
	 * @param zoneId
	 * @param binderId
	 * @return
	 */
    public Long computeDiskSpaceUsed(Long zoneId, Long binderId);

    public BinderQuota loadBinderQuota(Long zoneId, Long binderId) throws NoObjectByTheIdException;

    public List<Binder> loadBindersByPathName(final String pathName, final Long zoneId);
}
