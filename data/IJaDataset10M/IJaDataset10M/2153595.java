package com.germinus.xpression.cms.service;

import com.germinus.util.UniTuple;
import com.germinus.xpression.cms.CMSQuery;
import com.germinus.xpression.cms.CMSResult;
import com.germinus.xpression.cms.CMSRuntimeException;
import com.germinus.xpression.cms.HibernatePersistanceManager;
import com.germinus.xpression.cms.PersistanceManager;
import com.germinus.xpression.cms.cache.ScribeRefreshPolicy;
import com.germinus.xpression.cms.cache.NeedsRefreshException;
import com.germinus.xpression.cms.cache.RefreshPolicy;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.contents.ContentIF;
import com.germinus.xpression.cms.contents.ContentManager;
import com.germinus.xpression.cms.contents.ContentNotFoundException;
import com.germinus.xpression.cms.contents.ContentStatus;
import com.germinus.xpression.cms.contents.ContentType;
import com.germinus.xpression.cms.contents.MalformedContentException;
import com.germinus.xpression.cms.directory.DirectoryItem;
import com.germinus.xpression.cms.hibernate.HQLCondition;
import com.germinus.xpression.cms.lucene.ScribeContentUrl;
import com.germinus.xpression.cms.model.ContentEntry;
import com.germinus.xpression.cms.model.SelectedContentsList;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.cms.worlds.World;
import com.germinus.xpression.cms.worlds.WorldManager;
import com.germinus.xpression.groupware.Authorizator;
import com.germinus.xpression.groupware.CommunityManager;
import com.germinus.xpression.groupware.GroupwareUser;
import com.germinus.xpression.groupware.NotAuthorizedException;
import com.germinus.xpression.groupware.communities.Community;
import com.germinus.xpression.groupware.util.GroupwareManagerRegistry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Obtains and persists lists of contents
 *
 * @author jferrer
 */
public class SelectedContentsService {

    private static PersistanceManager persister = new HibernatePersistanceManager();

    private Log log = LogFactory.getLog(SelectedContentsService.class);

    private static WorldManager worldManager = ManagerRegistry.getWorldManager();

    private static ContentManager contentManager = ManagerRegistry.getContentManager();

    private static final String IMPORTANT_CONTENTS_PORTLET = "important_contents_WAR_cms_tools";

    public List<ContentIF> getListContentsByQuery(GroupwareUser groupwareUser, CMSQuery query) throws NotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("Query" + query);
        }
        if (log.isDebugEnabled()) log.debug("User " + groupwareUser.getId() + " is getting contents from world");
        CMSResult cmsResult = contentManager.searchContents(query);
        if (log.isDebugEnabled()) log.debug("Size of Contents: " + cmsResult.getItems().size());
        return (List<ContentIF>) cmsResult.getItems();
    }

    public CMSQuery buildCMSQuery(ContentType contentType, String searchText, String worldId, Integer pagesize, String currentFolderPath, String searchCategory, Integer page) {
        CMSQuery query = contentManager.createQuery();
        log.info("Retrieving contents of type " + contentType.getId());
        List<World> targetWorlds = new ArrayList<World>();
        targetWorlds.add(worldManager.findWorldById(worldId));
        query.setWorlds(targetWorlds);
        query.setFullTextSearch(searchText.trim());
        query.setCurrentFolderPath(currentFolderPath);
        query.setPublicationState(CMSQuery.PUBLISHED_STATE);
        query.setPageSize(pagesize);
        query.setPageNumber(page);
        if (StringUtils.isNotEmpty(contentType.getName())) {
            query.setType(contentType);
        }
        if (StringUtils.isNotEmpty(searchCategory)) {
            List<String> categoryList = new ArrayList<String>();
            categoryList.add(searchCategory);
            query.setCategories(categoryList);
        }
        return query;
    }

    public CMSResult getCMSResultContentsByWorldId(GroupwareUser groupwareUser, ContentType contentType, String worldId, CMSQuery query) throws NotAuthorizedException {
        assertViewWorldAuthorization(worldId, groupwareUser);
        if (log.isDebugEnabled()) log.debug("User " + groupwareUser.getId() + " is getting contents from world");
        CMSResult cmsResult = contentManager.searchContents(query);
        if (log.isDebugEnabled()) log.debug("Size of Contents: " + cmsResult.getItems().size());
        return cmsResult;
    }

    public List getListContentsByWorldId(GroupwareUser groupwareUser, String worldId, ContentType contentType, String searchText, String publicationState, String searchCategory) throws NotAuthorizedException {
        log.info("Retrieving contents of type " + contentType.getId());
        assertViewWorldAuthorization(worldId, groupwareUser);
        CMSQuery query = contentManager.createQuery();
        List<World> targetWorlds = new ArrayList<World>();
        targetWorlds.add(worldManager.findWorldById(worldId));
        query.setWorlds(targetWorlds);
        query.setFullTextSearch(searchText);
        query.setPublicationState(publicationState);
        if (StringUtils.isNotEmpty(contentType.getName())) {
            query.setType(contentType);
        }
        if (StringUtils.isNotEmpty(searchCategory)) {
            List<String> categoryList = new ArrayList<String>();
            categoryList.add(searchCategory);
            query.setCategories(categoryList);
        }
        query.setPageSize(50);
        log.info("User " + groupwareUser.getId() + " is getting contents from world");
        CMSResult cmsResult = contentManager.searchContents(query);
        if (log.isDebugEnabled()) log.debug("Size of Contents: " + cmsResult.getItems().size());
        return cmsResult.getItems();
    }

    public List getSelectedContents(Long plid, String portletId, Long companyId) throws NotAuthorizedException {
        SelectedContentsList selectedContentIdsList = getContentIdsList(plid, portletId, companyId);
        if (selectedContentIdsList == null) {
            return Collections.EMPTY_LIST;
        }
        List contentEntries = selectedContentIdsList.getContentEntries();
        List contentList = findSelectedContents(contentEntries);
        return contentList;
    }

    public List<ContentIF> getVisibleSelectedContents(Long plid, String portletId, Long companyId) throws NotAuthorizedException {
        SelectedContentsList selectedContentIdsList = getContentIdsList(plid, portletId, companyId);
        if (selectedContentIdsList == null) {
            return Collections.EMPTY_LIST;
        }
        List contentIds = selectedContentIdsList.getVisibleContentIds();
        List<ContentIF> contentList = findContents(contentIds);
        return contentList;
    }

    public UniTuple<ContentIF> findVisibleContent(Long plid, ScribeContentUrl scribeContentUrl, String portletId, Long companyId) throws NotAuthorizedException {
        UniTuple<ContentIF> found = new UniTuple<ContentIF>(false, null);
        List<ContentIF> visibleSelectedContents = getVisibleSelectedContents(plid, portletId, companyId);
        for (ContentIF contentIF : visibleSelectedContents) {
            if (contentIF.getScribeContentUrl().equals(scribeContentUrl)) return new UniTuple<ContentIF>(true, contentIF);
        }
        return found;
    }

    /**
     * Search contents based on a stored search.
     * Contents are filtered based on publication Date.
     * @param query
     * @param pageSize Number of contents to be shown
     * @return
     */
    public List executeQueryAndFilter(String query, int pageSize) {
        if (StringUtils.isNotEmpty(query)) {
            String queryKey = buildQueryKey(query, pageSize);
            try {
                return (List) ManagerRegistry.getCacheAdmin().getFromCache(queryKey);
            } catch (NeedsRefreshException e) {
                try {
                    List<? extends DirectoryItem> contents = doSearch(query, pageSize);
                    ManagerRegistry.getCacheAdmin().putInCache(queryKey, contents, queryRefreshPolycy());
                    return contents;
                } catch (Throwable t) {
                    ManagerRegistry.getCacheAdmin().cancelUpdate(queryKey);
                    throw new CMSRuntimeException(t);
                }
            }
        } else return new ArrayList();
    }

    /**
     * Build a RefreshPolicy Object for contents queries
     * @return
     */
    private RefreshPolicy queryRefreshPolycy() {
        return new ScribeRefreshPolicy();
    }

    private List<? extends DirectoryItem> doSearch(String query, int pageSize) {
        ContentManager contentManager = ManagerRegistry.getContentManager();
        CMSQuery cmsQuery = contentManager.createQuery(query);
        CMSResult cmsResult = new CMSResult();
        cmsQuery.setPageSize(pageSize);
        cmsQuery.setPageNumber(1);
        cmsQuery.setFilterByPublicationDate(true);
        long initTime = 0;
        long endTime = 0;
        if (log.isDebugEnabled()) {
            initTime = System.currentTimeMillis();
            log.debug("Finding contents for portlet");
        }
        cmsResult = contentManager.searchContents(cmsQuery);
        if (log.isDebugEnabled()) {
            endTime = System.currentTimeMillis();
            log.debug("Contents found in " + (endTime - initTime));
        }
        return cmsResult.getItems();
    }

    private String buildQueryKey(String query, int pageSize) {
        return "" + pageSize + ":" + query;
    }

    /**
     * Get a list of content ids (String instances) for the given parameters
     * which represent the location of portlet data.
     *
     * @param plid
     * @param portletId
     * @return a <code>SelectedContentsList</code> isntance or null if there
     * is no content for the given parameters
     */
    public SelectedContentsList getContentIdsList(Long plid, String portletId, Long companyId) {
        List<HQLCondition> conditions = new ArrayList<HQLCondition>();
        HQLCondition conditionOne = new HQLCondition("portletId", portletId, HQLCondition.EQUALS);
        conditions.add(conditionOne);
        HQLCondition conditionTwo = new HQLCondition("plid", plid, HQLCondition.EQUALS);
        conditions.add(conditionTwo);
        HQLCondition conditionThree = new HQLCondition("companyId", companyId, HQLCondition.EQUALS);
        conditions.add(conditionThree);
        List selectedContentIdsLists;
        selectedContentIdsLists = persister.findList(SelectedContentsList.class, conditions);
        if (selectedContentIdsLists.isEmpty()) {
            return null;
        }
        return (SelectedContentsList) selectedContentIdsLists.get(0);
    }

    public void setContentIds(Long plid, String portletId, String[] contentIds, Long companyId) {
        SelectedContentsList selectedContentsIds = getContentIdsList(plid, portletId, companyId);
        selectedContentsIds.setContentIds(Arrays.asList(contentIds));
        persister.save(selectedContentsIds);
    }

    public void addContentIds(Long plid, String portletId, String[] contentIds, Long companyId) {
        SelectedContentsList contentIdsList = getContentIdsList(plid, portletId, companyId);
        if (contentIdsList == null) {
            contentIdsList = new SelectedContentsList(portletId, plid, companyId);
            log.info("New contents list created");
        }
        contentIdsList.addContentIds(contentIds);
        persister.save(contentIdsList);
    }

    public void removeContentIds(Long plid, String portletId, String[] contentIds, Long companyId) {
        SelectedContentsList contentIdsList = getContentIdsList(plid, portletId, companyId);
        contentIdsList.removeContentIds(contentIds);
        persister.save(contentIdsList);
    }

    /**
     * @param world
     * @param groupwareUser
     * @throws NotAuthorizedException
     */
    private void assertViewWorldAuthorization(World world, GroupwareUser groupwareUser) throws NotAuthorizedException {
        CommunityManager communityManager = GroupwareManagerRegistry.getCommunityManager();
        Authorizator authorizator = GroupwareManagerRegistry.getAuthorizator();
        Community community = communityManager.getOwnerCommunity(world);
        authorizator.assertViewAuthorization(community, groupwareUser);
    }

    /**
     * @param worldId
     * @param groupwareUser
     * @throws NotAuthorizedException
     */
    private void assertViewWorldAuthorization(String worldId, GroupwareUser groupwareUser) throws NotAuthorizedException {
        WorldManager worldManager = ManagerRegistry.getWorldManager();
        World world = worldManager.findWorldById(worldId);
        assertViewWorldAuthorization(world, groupwareUser);
    }

    private List<ContentIF> findContents(List<String> contentIds) throws NotAuthorizedException {
        Iterator<String> contentIdsIt = contentIds.iterator();
        ContentManager contentManager = ManagerRegistry.getContentManager();
        List<ContentIF> contentList = new ArrayList<ContentIF>();
        while (contentIdsIt.hasNext()) {
            String contentId = contentIdsIt.next();
            try {
                Content content = contentManager.findContentInWorkspaces(contentId);
                Integer contentStatus = content.calculateContentStatus();
                if (!content.isDeleted()) {
                    if (contentStatus != ContentStatus.expired && contentStatus != ContentStatus.notPublished) contentList.add(content);
                }
            } catch (MalformedContentException e) {
                log.info("Ignoring contentId " + contentId + " because it is malformed");
            } catch (ContentNotFoundException e) {
                log.info("Ignoring contentId " + contentId + " because it has not been found");
            }
        }
        return contentList;
    }

    private List findSelectedContents(List contentEntries) throws NotAuthorizedException {
        Iterator contentEntiesIt = contentEntries.iterator();
        ContentManager contentManager = ManagerRegistry.getContentManager();
        List<SelectedContent> selectedContentList = new ArrayList<SelectedContent>();
        ContentIF content;
        while (contentEntiesIt.hasNext()) {
            ContentEntry contentEntry = (ContentEntry) contentEntiesIt.next();
            if (contentEntry != null) {
                try {
                    content = contentManager.findContentInWorkspaces(contentEntry.getContentId());
                    selectedContentList.add(new SelectedContent(content, contentEntry.isVisible()));
                } catch (ContentNotFoundException e) {
                    log.info("Ignoring contentId " + contentEntry.getContentId() + " because it does not exist");
                } catch (MalformedContentException e) {
                    log.info("Ignoring contentId " + contentEntry.getContentId() + " because it is malformed");
                } catch (CMSRuntimeException e) {
                    log.info("Ignoring contentId " + contentEntry.getContentId() + " because it repository error.");
                }
            }
        }
        return selectedContentList;
    }

    public void changeVisibleStatus(Long plid, String portletId, String contentId, Long companyId) {
        SelectedContentsList contentIdsList = getContentIdsList(plid, portletId, companyId);
        contentIdsList.changeStatus(contentId);
        persister.save(contentIdsList);
    }
}
