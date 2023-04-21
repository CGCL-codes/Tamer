package com.germinus.portlet.advanced_content_menu.model;

import javax.portlet.PortletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import com.germinus.xpression.cms.CMSRuntimeException;
import com.germinus.xpression.cms.contents.ContentNotFoundException;
import com.germinus.xpression.cms.contents.MalformedContentException;
import com.germinus.xpression.cms.model.Navigation;
import com.germinus.xpression.cms.service.NavigationHelper;
import com.germinus.xpression.groupware.LiferayException;
import com.germinus.xpression.groupware.communities.CommunityNotFoundException;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * This object will contain all request attributes that advanced_content_menu actions may need. 
 * 
 * @author dtohariar
 *
 */
public class AdvancedContentMenuNavigation extends Navigation {

    private static final long serialVersionUID = 5008367394862850002L;

    public static final String ADVANCED_CONTENT_MENU_NAVIGATION = "AdvancedContentMenuNavigation";

    /**
	 * Request attributes 
	 */
    private String searchText;

    private String contentTypeId;

    private String sortingCriteria;

    private String consignee;

    private Boolean moreResults = Boolean.FALSE;

    private int page;

    public AdvancedContentMenuNavigation() {
        searchText = "";
        contentTypeId = "allContents";
        sortingCriteria = "name";
        consignee = "all";
        page = 1;
    }

    /**
	 * Returns the navigation object. 
	 * 
	 * @param req
	 * @param form
	 * @return
	 * @throws CommunityNotFoundException 
	 * @throws LiferayException 
	 * @throws MalformedContentException 
	 * @throws ContentNotFoundException 
	 * @throws CMSRuntimeException 
	 */
    public static AdvancedContentMenuNavigation getNavigationObject(PortletRequest req, ActionForm form) {
        DynaActionForm dform = (DynaActionForm) form;
        String navigationId = ParamUtil.getString(req, "navigationId");
        if (req.getParameter("moreResults") == null) dform.set("moreResults", null);
        if (req.getParameter("pageNumber") == null) dform.set("pageNumber", null);
        AdvancedContentMenuNavigation advancedContentMenuNavigation = StringUtils.isEmpty(navigationId) ? new AdvancedContentMenuNavigation() : (AdvancedContentMenuNavigation) NavigationHelper.getNavigation(req, navigationId);
        if (advancedContentMenuNavigation == null) advancedContentMenuNavigation = new AdvancedContentMenuNavigation();
        populateFormToNavigationObject(req, dform, advancedContentMenuNavigation);
        NavigationHelper.saveNavigation(req, advancedContentMenuNavigation);
        return advancedContentMenuNavigation;
    }

    public static void populateFormToNavigationObject(PortletRequest req, DynaActionForm dform, AdvancedContentMenuNavigation advancedContentMenuNavigation) {
        if (dform != null) {
            advancedContentMenuNavigation.setSearchText((String) dform.get("searchText"));
            advancedContentMenuNavigation.setContentTypeId((String) dform.get("contentTypeId"));
            advancedContentMenuNavigation.setSortingCriteria((String) dform.get("sortingCriteria"));
            advancedContentMenuNavigation.setConsignee((String) dform.get("consignee"));
            if (dform.get("moreResults") != null) advancedContentMenuNavigation.setMoreResults((Boolean) dform.get("moreResults")); else advancedContentMenuNavigation.setMoreResults(Boolean.FALSE);
            if (dform.get("pageNumber") != null) advancedContentMenuNavigation.setPage((Integer) dform.get("pageNumber"));
        }
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public String getSortingCriteria() {
        return sortingCriteria;
    }

    public void setSortingCriteria(String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public Boolean getMoreResults() {
        return moreResults;
    }

    public void setMoreResults(Boolean moreResults) {
        this.moreResults = moreResults;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
