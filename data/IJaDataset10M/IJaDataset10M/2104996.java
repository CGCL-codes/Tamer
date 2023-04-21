package com.germinus.portlet.content_browser.action;

import com.germinus.portlet.content_admin.model.PaginatedListBean;
import com.germinus.portlet.content_browser.ContentBrowserNavigation;
import com.germinus.util.PaginationData;
import com.germinus.xpression.cms.CMSQuery;
import com.germinus.xpression.cms.CMSResult;
import com.germinus.xpression.cms.CmsConfig;
import com.germinus.xpression.cms.action.CMSPortletAction;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.contents.ContentManager;
import com.germinus.xpression.cms.contents.ContentNotFoundException;
import com.germinus.xpression.cms.contents.ContentStatus;
import com.germinus.xpression.cms.contents.MalformedContentException;
import com.germinus.xpression.cms.directory.DirectoryFolder;
import com.germinus.xpression.cms.directory.DirectoryItem;
import com.germinus.xpression.cms.directory.DirectoryItemNotFoundException;
import com.germinus.xpression.cms.directory.DirectoryPersister;
import com.germinus.xpression.cms.lucene.beans.ContentBean;
import com.germinus.xpression.cms.service.FilterContentService;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.cms.worlds.World;
import com.germinus.xpression.cms.worlds.WorldManager;
import com.germinus.xpression.groupware.Authorizator;
import com.germinus.xpression.groupware.CommunityManager;
import com.germinus.xpression.groupware.GroupwareUser;
import com.germinus.xpression.groupware.LiferayException;
import com.germinus.xpression.groupware.NotAuthorizedException;
import com.germinus.xpression.groupware.action.GroupwareHelper;
import com.germinus.xpression.groupware.communities.Community;
import com.germinus.xpression.groupware.communities.CommunityNotFoundException;
import com.germinus.xpression.groupware.service.UserCommunitiesService;
import com.germinus.xpression.groupware.util.GroupwareManagerRegistry;
import com.germinus.xpression.groupware.util.LiferayHelperFactory;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContentBrowserAction extends CMSPortletAction {

    private static final Log log = LogFactory.getLog(ContentBrowserAction.class);

    private static final String NONE = "NONE";

    public static final String CMS_QUERY_KEY = "content_browser_query";

    public static final String SEARCH_RESULTS_KEY = "content_browser_search_results";

    public static final String VIEW_KEY = "view";

    public static final String CRITERIA_VIEW = "criteria";

    public static final String RESULT_VIEW = "result";

    public static final String TAG_VIEW = "tag";

    public static final String SPECIAL_SEARCH = "specialSearch";

    private static final String ERROR_FORM = "error_form";

    public static final String UPDATE_SEARCH_CATEGORIES_VIEW = "update_searchcategories";

    private static final String CONTENT_ID = "contentId";

    private static final String WORKSPACE = "workspace";

    private static final String COMMUNITY_CONTENTS = "communityContents";

    private static final String CONTENTS_OF_A_MONTH = "contentsOfAMonth";

    private static final int PAGES_LINK_SHOWED = 9;

    private static WorldManager worldManager = ManagerRegistry.getWorldManager();

    private static CommunityManager communityManager = GroupwareManagerRegistry.getCommunityManager();

    private static final String FROM_COMMUNITY_CONTENTS = "fromCommunityContents";

    private static final String EXCLUDED_CONTENTS_FROM_LIST = "excludedContents";

    private static final String SEARCH_WORLD = "searchWorld";

    private static final String SEARCH_IN_FOLDER = "searchInFolder";

    private static final String TAGS = "tags";

    private static final String CURRENT_WORLD_ID = "currentWorldId";

    private static final String SEARCH_COMMUNITIES = "searchCommunities";

    private static final String IS_IDC_CONTENT_TYPE_SELECT = "isIdcContentTypeSelect";

    private static final String SELECTED_CONTENT_TYPE = "selectedContentType";

    private static final String FILTER_ID = "filterId";

    private static final String ORIGINAL_PORTLETID_PARAM = "originalPortletId";

    private static final String FIELD_ID = "fieldId";

    private static final String UNIQUE = "unique";

    private static final String MULTIPLE_SELECT = "multipleSelect";

    private static final String COMMON_CATEGORIES = "commonCategories";

    private static final String SEARCH_CATEGORIES = "searchCategories";

    public static final String TEXT = "text";

    private static final String TYPE = "type";

    @SuppressWarnings("unchecked")
    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        DynaValidatorForm dynaForm = (DynaValidatorForm) form;
        String searchWorld = dynaForm.getString(SEARCH_WORLD);
        if (!validate(req, form)) {
            ContentBrowserNavigation contentBrowserNavigation = ContentBrowserNavigation.getNavigationObject(req, form);
            req.setAttribute(ContentBrowserNavigation.CONTENT_BROWSER_NAVIGATION, contentBrowserNavigation);
            setForward(req, ERROR_FORM);
            return;
        }
        DirectoryPersister directoryPersister = ManagerRegistry.getDirectoryPersister();
        Authorizator authorizator = GroupwareManagerRegistry.getAuthorizator();
        ActionMessages errors = new ActionMessages();
        String searchInFolder = dynaForm.getString(SEARCH_IN_FOLDER);
        String tags = dynaForm.getString(TAGS);
        boolean specialSearch = ParamUtil.getBoolean(req, SPECIAL_SEARCH);
        GroupwareUser groupwareUser = getGroupwareUser(req);
        DirectoryFolder folderToSearch = null;
        List<World> userWorlds = new ArrayList<World>();
        if (StringUtils.isNotEmpty(searchInFolder)) {
            if (isValidWorld(searchWorld)) {
                World worldToSearch = worldManager.findWorldById(searchWorld);
                try {
                    folderToSearch = (DirectoryFolder) directoryPersister.getItemByUUIDWorkspace(searchInFolder, worldToSearch.getWorkspace());
                } catch (RuntimeException e) {
                    log.error("Error obtainig folder with uuid " + searchInFolder);
                }
            }
        } else {
            if (isValidWorld(searchWorld)) {
                World worldToSearch = worldManager.findWorldById(searchWorld);
                Community currentCommunity = communityManager.getOwnerCommunity(worldToSearch);
                userWorlds.add(worldToSearch);
                try {
                    authorizator.assertViewAuthorization(currentCommunity, groupwareUser);
                } catch (NotAuthorizedException e) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("content_admin.error.you-are-not-authorize-to-view-this-content"));
                    saveErrors(req, errors);
                    setForward(req, ERROR);
                    return;
                }
            } else {
                if (!specialSearch) {
                    Collection<Community> userCommunities = UserCommunitiesService.getInstance().getUserCommunities(groupwareUser, req);
                    Iterator<Community> it = userCommunities.iterator();
                    while (it.hasNext()) {
                        Community community = it.next();
                        userWorlds.add(community.getWorld());
                    }
                    for (String externalWorldId : CmsConfig.getExternalWorldIdsToSearch()) {
                        World externalWorld = new World();
                        externalWorld.setId(externalWorldId);
                        userWorlds.add(externalWorld);
                    }
                }
            }
        }
        Map<String, String> preferences = (Map) dynaForm.get("preferences");
        String onlyPublished = preferences.get("onlyPublished");
        boolean includeDrafts = true;
        if (StringUtils.isNotEmpty(onlyPublished)) {
            if (onlyPublished.equals(TRUE)) {
                includeDrafts = false;
            }
        }
        if (specialSearch) {
            includeDrafts = false;
            userWorlds.addAll(FilterContentService.getSpecialWorlds());
            req.setAttribute(SPECIAL_SEARCH, specialSearch);
        }
        String typeString = dynaForm.getString(TYPE);
        String text = dynaForm.getString(TEXT);
        Map commonCategories = (Map) dynaForm.get(COMMON_CATEGORIES);
        Map searchCategories = (Map) dynaForm.get(SEARCH_CATEGORIES);
        CMSQuery cmsQuery = FilterContentService.buildSearchQuery(userWorlds, folderToSearch, typeString, text, commonCategories, searchCategories, null, null, includeDrafts, tags);
        req.getPortletSession().setAttribute(CMS_QUERY_KEY, cmsQuery);
        req.getPortletSession().setAttribute(VIEW_KEY, RESULT_VIEW);
        req.setAttribute(FILTER_ID, getParamValue(req, FILTER_ID));
        req.setAttribute(ORIGINAL_PORTLETID_PARAM, getParamValue(req, ORIGINAL_PORTLETID_PARAM));
        req.setAttribute(CURRENT_WORLD_ID, req.getParameter(CURRENT_WORLD_ID));
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return doRender(mapping, form, req, Boolean.FALSE);
    }

    private boolean isValidWorld(String searchWorld) {
        return StringUtils.isNotEmpty(searchWorld) && !(NONE.equals(searchWorld)) && (!searchWorld.equals("-1"));
    }

    protected ActionForward doRender(ActionMapping mapping, ActionForm form, RenderRequest req, Boolean isCommunityContentBrowser) throws LiferayException, CommunityNotFoundException, ContentNotFoundException, MalformedContentException, DirectoryItemNotFoundException {
        String forward = getForward(req);
        if (StringUtils.isNotEmpty(forward)) {
            return mapping.findForward(forward);
        }
        ContentBrowserNavigation contentBrowserNavigation = ContentBrowserNavigation.getNavigationObject(req, form);
        req.setAttribute(ContentBrowserNavigation.CONTENT_BROWSER_NAVIGATION, contentBrowserNavigation);
        ContentManager contentManager = ManagerRegistry.getContentManager();
        CMSQuery cmsQuery = (CMSQuery) req.getPortletSession().getAttribute(CMS_QUERY_KEY);
        if (cmsQuery != null && StringUtils.isNotEmpty(cmsQuery.getCurrentFolderPath())) {
            String currentFolderPath = cmsQuery.getCurrentFolderPath();
            try {
                DirectoryItem currentFolder;
                try {
                    currentFolder = ManagerRegistry.getDirectoryPersister().getItemFromPath(currentFolderPath);
                } catch (DirectoryItemNotFoundException e) {
                    currentFolder = null;
                }
                if (currentFolder != null) req.setAttribute("selectedFolderName", currentFolder.getName());
            } catch (RuntimeException e) {
                log.error("Error obtainig folder with uuid " + currentFolderPath);
            }
        }
        String filterId = getParamValue(req, FILTER_ID);
        String originalPortletId = getParamValue(req, ORIGINAL_PORTLETID_PARAM);
        String view = getView(req);
        DynaValidatorForm dynaForm = (DynaValidatorForm) form;
        if (isCommunityContentBrowser && cmsQuery != null) {
            World worldToSearch = getCurrentWorld(req);
            List<World> worldOfSearch = new ArrayList<World>();
            worldOfSearch.add(worldToSearch);
            String text = dynaForm.getString(TEXT);
            cmsQuery = FilterContentService.buildSearchQuery(worldOfSearch, null, null, text, null, null, null, null, Boolean.FALSE, null);
            req.getPortletSession().setAttribute(CMS_QUERY_KEY, cmsQuery);
        } else {
            if (COMMUNITY_CONTENTS.equals(view)) {
                WorldManager worldManager = ManagerRegistry.getWorldManager();
                World worldToSearch = worldManager.findWorldById(dynaForm.getString(SEARCH_WORLD));
                List<World> worldOfSearch = new ArrayList<World>();
                worldOfSearch.add(worldToSearch);
                cmsQuery = FilterContentService.buildSearchQuery(worldOfSearch, null, null, null, null, null, null, null, Boolean.FALSE, null);
                req.getPortletSession().setAttribute(CMS_QUERY_KEY, cmsQuery);
                req.getPortletSession().setAttribute(FROM_COMMUNITY_CONTENTS, true);
                List<String> excluded = new ArrayList<String>();
                String contentIds = req.getParameter("contentIds");
                if (contentIds != null) {
                    String[] contentIdsParam = contentIds.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").split(",");
                    excluded = Arrays.asList(contentIdsParam);
                }
                req.getPortletSession().setAttribute(EXCLUDED_CONTENTS_FROM_LIST, excluded);
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("contents.of.current.community"));
                saveMessages(req, messages);
            }
            if (CONTENTS_OF_A_MONTH.equals(view)) {
                World worldToSearch = getCurrentWorld(req);
                List<World> worldOfSearch = new ArrayList<World>();
                worldOfSearch.add(worldToSearch);
                cmsQuery = FilterContentService.buildContentsBetweenDatesQuery(worldOfSearch, Long.valueOf(req.getParameter("startTime")), Long.valueOf(req.getParameter("endTime")));
                req.getPortletSession().setAttribute(CMS_QUERY_KEY, cmsQuery);
            }
            if (TAG_VIEW.equals(view)) {
                String contentId = dynaForm.getString(CONTENT_ID);
                String workspace = dynaForm.getString(WORKSPACE);
                Content content = contentManager.getContentById(contentId, workspace);
                World contentWorld = worldManager.getOwnerWorld(content);
                String tags = dynaForm.getString(TAGS);
                List<World> worldOfSearch = new ArrayList<World>();
                worldOfSearch.add(contentWorld);
                cmsQuery = FilterContentService.buildSearchQuery(worldOfSearch, null, null, null, null, null, null, null, Boolean.FALSE, tags);
                req.getPortletSession().setAttribute(CMS_QUERY_KEY, cmsQuery);
                req.setAttribute("searchedTag", tags);
            }
            if ((CRITERIA_VIEW.equals(view) || cmsQuery == null) && !UPDATE_SEARCH_CATEGORIES_VIEW.equals(view)) {
                req.setAttribute(FILTER_ID, filterId);
                req.setAttribute(ORIGINAL_PORTLETID_PARAM, originalPortletId);
                if (isSelectContentsView(req)) {
                    List<Community> communities = currentCommunities(req);
                    req.setAttribute(SEARCH_COMMUNITIES, communities);
                    if (communities.size() == 1) {
                        req.setAttribute("oneCommunity", Boolean.TRUE);
                        req.setAttribute("searchCommunity", communities.get(0));
                    } else req.setAttribute(SEARCH_COMMUNITIES, communities);
                } else {
                    GroupwareUser user = GroupwareHelper.getUser(req);
                    Collection<Community> communities = UserCommunitiesService.getInstance().getUserCommunities(user, req);
                    if (cmsQuery == null || communities.size() == 0) {
                        Community currentCommunity = LiferayHelperFactory.getLiferayHelper().getCurrentCommunity(req);
                        ((DynaActionForm) form).set(SEARCH_WORLD, currentCommunity.getWorld().getId());
                        communities.add(currentCommunity);
                        req.setAttribute("oneCommunity", Boolean.TRUE);
                        req.setAttribute("searchCommunity", currentCommunity);
                    } else req.setAttribute(SEARCH_COMMUNITIES, communities);
                }
                return mapping.findForward(CRITERIA_VIEW);
            }
            String isIdcContentTypeSelect = req.getParameter(IS_IDC_CONTENT_TYPE_SELECT);
            if (UPDATE_SEARCH_CATEGORIES_VIEW.equals(view)) {
                if (StringUtils.equals(isIdcContentTypeSelect, TRUE)) {
                    req.setAttribute(IS_IDC_CONTENT_TYPE_SELECT, true);
                    req.setAttribute(SELECTED_CONTENT_TYPE, req.getParameter(SELECTED_CONTENT_TYPE));
                }
                return mapping.findForward(UPDATE_SEARCH_CATEGORIES_VIEW);
            }
        }
        getPageSize(req);
        Integer pageSize = (Integer) req.getPortletSession().getAttribute(PaginationData.PAGE_SIZE_PARAM);
        String sortProperty = ParamUtil.getString(req, "sort", null);
        String sortOrder = ParamUtil.getString(req, "dir", CMSQuery.DESC_SORTING);
        cmsQuery.setPageSize(pageSize);
        int currentPage = ParamUtil.get(req, "page", 1);
        cmsQuery.setPageNumber(currentPage);
        if (sortProperty == null) sortProperty = "lastModificationDate";
        cmsQuery.setSortingCriteria(sortProperty);
        cmsQuery.setSortingOrder(sortOrder);
        CMSResult cmsResult = contentManager.searchContents(cmsQuery);
        if (StringUtils.isNotEmpty(filterId)) {
            req.setAttribute(FILTER_ID, filterId);
            req.setAttribute(ORIGINAL_PORTLETID_PARAM, originalPortletId);
            List<ContentBean> items = (List<ContentBean>) cmsResult.getItems();
            List<String> excluded = (List<String>) req.getPortletSession().getAttribute(EXCLUDED_CONTENTS_FROM_LIST);
            for (ContentBean currentContent : items) {
                if (excluded.contains(currentContent.getContentId())) {
                    currentContent.setAlreadySelected(true);
                    continue;
                }
                if (currentContent.calculateContentStatus() == ContentStatus.expired) {
                    currentContent.setAlreadySelected(true);
                }
            }
        } else {
            req.getPortletSession().removeAttribute(EXCLUDED_CONTENTS_FROM_LIST);
        }
        if (StringUtils.isEmpty(view) && cmsResult.getItems().size() == 0) {
            return mapping.findForward(CRITERIA_VIEW);
        }
        req.setAttribute(PaginationData.PAGE_SIZE_PARAM, pageSize);
        req.setAttribute(SEARCH_RESULTS_KEY, new PaginatedListBean(cmsResult));
        req.setAttribute(CURRENT_WORLD_ID, req.getParameter(CURRENT_WORLD_ID));
        int maxPageLinks = PAGES_LINK_SHOWED;
        int fullSize = Integer.parseInt(Long.toString(cmsResult.getFullSize()));
        PaginationData paginationData = new PaginationData(pageSize, currentPage, fullSize, maxPageLinks);
        paginationData.setParametersMap(new HashMap<String, String>());
        req.setAttribute("paginationData", paginationData);
        String fieldId = req.getParameter(FIELD_ID);
        if (fieldId != null && (fieldId.equals(UNIQUE) || fieldId.equals(MULTIPLE_SELECT))) {
            req.setAttribute(FIELD_ID, fieldId);
        }
        GroupwareUser groupwareUser = getGroupwareUser(req);
        req.setAttribute(GroupwareUser.USER_ATTRIBUTE, groupwareUser);
        return mapping.findForward(RESULT_VIEW);
    }

    private String getParamValue(PortletRequest req, String paramName) {
        String paramValue = req.getParameter(paramName);
        if (StringUtils.isEmpty(paramValue)) paramValue = (String) req.getAttribute(paramName);
        return paramValue;
    }

    private List<Community> currentCommunities(RenderRequest req) {
        GroupwareUser user = GroupwareHelper.getUser(req);
        String currentWorldId = req.getParameter(CURRENT_WORLD_ID);
        Community currentCommunityWeb = null;
        if (StringUtils.isNotEmpty(currentWorldId) && !"null".equals(currentWorldId)) {
            currentCommunityWeb = communityManager.getOwnerCommunity(worldManager.findWorldById(currentWorldId));
            req.setAttribute(CURRENT_WORLD_ID, currentWorldId);
        } else {
            try {
                currentCommunityWeb = LiferayHelperFactory.getLiferayHelper().getCurrentCommunity(req, user);
            } catch (CommunityNotFoundException ce) {
            }
        }
        Collection<Community> pageCommunities = LiferayHelperFactory.getLiferayHelper().getPageCommunities(req, user);
        List<Community> currentCommunities = new ArrayList<Community>();
        currentCommunities.add(currentCommunityWeb);
        currentCommunities.addAll(pageCommunities);
        return currentCommunities;
    }

    private boolean isSelectContentsView(RenderRequest request) {
        String fieldId = (String) request.getAttribute(FIELD_ID);
        if (fieldId == null) fieldId = request.getParameter(FIELD_ID);
        return UNIQUE.equals(fieldId) || MULTIPLE_SELECT.equals(fieldId);
    }

    /**
	 * 
	 * @param req
	 * @param form
	 * @return true if all is ok, false if ther are errors
	 */
    @SuppressWarnings("unchecked")
    private boolean validate(ActionRequest req, ActionForm form) {
        if (isFromCommunityContents(req)) {
            return true;
        }
        boolean hasErrors = false;
        ActionErrors errors = new ActionErrors();
        DynaValidatorForm dynaForm = (DynaValidatorForm) form;
        String text = dynaForm.getString(TEXT);
        if (!StringUtils.isEmpty(text) && (text.length() < 3)) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.minlength", "Texto", "3"));
            saveErrors(req, errors);
            return false;
        }
        String typeString = dynaForm.getString(TYPE);
        Map commonCategories = (Map) dynaForm.get(COMMON_CATEGORIES);
        boolean noCommons = true;
        Iterator<String> valores = commonCategories.values().iterator();
        while (noCommons && valores.hasNext()) {
            noCommons = StringUtils.isEmpty(valores.next());
        }
        boolean noFolderPath = StringUtils.isEmpty((String) dynaForm.get(SEARCH_IN_FOLDER));
        boolean noTags = StringUtils.isEmpty((String) dynaForm.get(TAGS));
        if (noCommons && StringUtils.isEmpty(typeString) && StringUtils.isEmpty(text) && noFolderPath && noTags) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.you-must-select-any-search-field"));
            hasErrors = true;
        }
        saveErrors(req, errors);
        return !hasErrors;
    }

    private boolean isFromCommunityContents(ActionRequest req) {
        try {
            return (Boolean) req.getPortletSession().getAttribute(FROM_COMMUNITY_CONTENTS);
        } catch (Exception e) {
            return false;
        }
    }

    public String getView(RenderRequest req) {
        String view = req.getParameter(VIEW_KEY);
        if (view == null) {
            view = (String) req.getPortletSession().getAttribute(VIEW_KEY);
            req.getPortletSession().removeAttribute(VIEW_KEY);
        }
        return view;
    }

    private void getPageSize(RenderRequest req) {
        String pageSize = ParamUtil.getString(req, PaginationData.PAGE_SIZE_PARAM);
        if (StringUtils.isNotEmpty(pageSize)) {
            req.getPortletSession().setAttribute(PaginationData.PAGE_SIZE_PARAM, new Integer(pageSize));
        } else {
            if (req.getPortletSession().getAttribute(PaginationData.PAGE_SIZE_PARAM) == null) {
                req.getPortletSession().setAttribute(PaginationData.PAGE_SIZE_PARAM, 10);
            }
        }
    }

    protected PortletPreferences getPortletSetup(PortletRequest req, PortletConfig portletConfig) {
        PortletPreferences preferences;
        try {
            preferences = obtainPortletSetup(req, portletConfig);
        } catch (SystemException e1) {
            throw new RuntimeException("Cannot obtain portlet resource to configure");
        } catch (PortalException e) {
            throw new RuntimeException("Cannot obtain portlet resource to configure");
        }
        return preferences;
    }
}
