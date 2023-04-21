package com.germinus.merlin.util;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.germinus.merlin.page.PagePortletConfiguration;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;

public class ManageLayout {

    /**
	 * 
	 */
    private static final Log log = LogFactory.getLog(ManageLayout.class);

    public static final long DEFAULT_PARENT_LAYOUT_ID = 0;

    private Layout layout;

    private ThemeDisplay themeDisplay;

    private LayoutTypePortlet layoutTypePortlet;

    @SuppressWarnings("unused")
    private LiferayUtil liferayUtil;

    private PagePortletConfiguration forumPageProperties;

    private PagePortletConfiguration blogPageProperties;

    private PagePortletConfiguration documentPageProperties;

    final String NAME_FORUM_PAGE = "forumPage";

    final String NAME_BLOG_PAGE = "blogPage";

    final String NAME_DOCUMENT_PAGE = "documentPage";

    /**
	 * 
	 * 
	 * @author David Jiménez, Germinus XXI
	 * @since 1.0
	 */
    public ManageLayout() {
    }

    /**
	 * 
	 * @param layout
	 * @return
	 * @throws SystemException
	 * @throws PortalException
	 * @author David Jiménez, Germinus XXI
	 * @since 1.0
	 */
    @SuppressWarnings("unchecked")
    public int listLayoutChildren(Layout layout) throws SystemException, PortalException {
        List list = null;
        list = layout.getChildren();
        if (list != null) {
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                log.debug(i.next().toString());
            }
            return list.size();
        } else {
            return 0;
        }
    }

    /**
	 * 
	 * @return
	 */
    public int deleteLayout(long layoutId) {
        return 1;
    }

    /**
	 * 
	 * @param request
	 */
    private void instanceCommonObject(RenderRequest request) {
        layout = (Layout) request.getAttribute(WebKeys.LAYOUT);
        themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        layoutTypePortlet = themeDisplay.getLayoutTypePortlet();
    }

    public int addPage(RenderRequest request, RenderResponse response, String name) throws Exception {
        long parentLayoutId = layout.getParentLayoutId();
        addLayoutChildren(request, response, parentLayoutId, name);
        return 1;
    }

    private Layout addChildrenPage(RenderRequest request, RenderResponse response, String name) throws Exception {
        long parentLayoutId = layout.getLayoutId();
        return addLayoutChildren(request, response, parentLayoutId, name);
    }

    /**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    private Layout addLayoutChildren(RenderRequest request, RenderResponse response, long parentLayoutId, String name) throws Exception {
        boolean privateLayout = false;
        boolean hidden = false;
        String description = "esto es una descripcion por defecto";
        String type = "portlet";
        String friendlyURL = "";
        String title = "";
        long companyId = liferayUtil.getCompanyId(layout);
        Layout layoutNew;
        if (companyId != 0) {
            layoutNew = LayoutServiceUtil.addLayout(companyId, privateLayout, parentLayoutId, name, title, description, type, hidden, friendlyURL);
        } else {
            throw (new Exception("Incorrect companyID"));
        }
        log.debug("New layout add correctly with PLID : " + layoutNew.getPlid());
        return layoutNew;
    }

    /**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public int deleteForumPage(RenderRequest request, RenderResponse response) throws Exception {
        return 1;
    }

    /**
	 * 
	 * @param request
	 * @param response
	 * @param portletId
	 * @return
	 * @throws Exception
	 */
    public String addPortlet(RenderRequest request, RenderResponse response, String portletId) throws Exception {
        long userId = themeDisplay.getUserId();
        log.debug("User id: " + userId);
        String ppid = layoutTypePortlet.addPortletId(userId, portletId);
        if (layout.isShared()) {
            layoutTypePortlet.resetStates();
        }
        String cm = layout.getGroup().getName();
        log.debug("Community : " + cm);
        long companyId = liferayUtil.getCompanyId(layout);
        Portlet portlet = PortletLocalServiceUtil.getPortletById(companyId, portletId);
        log.debug("new portlet: " + ppid + " added to layout: " + layout.getLayoutId() + "-" + layout.getPlid() + " portletID: " + portlet.getPortletId());
        return ppid;
    }

    private String createPortletList(RenderRequest request, RenderResponse response, String portletString) throws Exception {
        String[] array = portletString.split(",");
        StringMaker resultString = new StringMaker();
        for (int i = 0; i < array.length; i++) {
            resultString.append(addPortlet(request, response, array[i]));
            resultString.append(",");
        }
        if (array.length != 0) {
            return resultString.substring(0, resultString.length() - 1);
        }
        return "";
    }

    /**
	 * @throws Exception 
	 * 
	 */
    private Properties fillLayoutProperties(RenderRequest request, RenderResponse response, PagePortletConfiguration pageConfiguration) throws Exception {
        Properties layoutProperties = new Properties();
        if (pageConfiguration.getColumn1().length() != 0) {
            layoutProperties.setProperty("column-1", createPortletList(request, response, pageConfiguration.getColumn1()));
        }
        if (pageConfiguration.getColumn2().length() != 0) {
            layoutProperties.setProperty("column-2", createPortletList(request, response, pageConfiguration.getColumn2()));
        }
        if (pageConfiguration.getColumn3().length() != 0) {
            layoutProperties.setProperty("column-3", createPortletList(request, response, pageConfiguration.getColumn3()));
        }
        layoutProperties.setProperty("state-max-previous", "88");
        layoutProperties.setProperty("state-max", "");
        layoutProperties.setProperty("state-min", "");
        layoutProperties.setProperty("layout-template-id", pageConfiguration.getLayoutTemplate());
        return layoutProperties;
    }

    /**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public long addPage(RenderRequest request, RenderResponse response, String namePage, PagePortletConfiguration pagePortletConfiguration) throws Exception {
        this.instanceCommonObject(request);
        Layout newLayout = addChildrenPage(request, response, namePage);
        Properties layoutProperties = fillLayoutProperties(request, response, pagePortletConfiguration);
        newLayout.setTypeSettingsProperties(layoutProperties);
        LayoutServiceUtil.updateLayout(newLayout.getGroupId(), newLayout.isPrivateLayout(), newLayout.getLayoutId(), newLayout.getTypeSettings());
        return newLayout.getLayoutId();
    }

    public long addForumPage(RenderRequest request, RenderResponse response) throws Exception {
        return addPage(request, response, this.NAME_FORUM_PAGE, forumPageProperties);
    }

    public long addBlogPage(RenderRequest request, RenderResponse response) throws Exception {
        return addPage(request, response, this.NAME_BLOG_PAGE, blogPageProperties);
    }

    public long addDocumentPage(RenderRequest request, RenderResponse response) throws Exception {
        return addPage(request, response, this.NAME_DOCUMENT_PAGE, documentPageProperties);
    }

    /**
	 * @return the liferayUtil
	 */
    public LiferayUtil getLiferayUtil() {
        return liferayUtil;
    }

    /**
	 * @param liferayUtil the liferayUtil to set
	 */
    public void setLiferayUtil(LiferayUtil liferayUtil) {
        this.liferayUtil = liferayUtil;
    }

    /**
	 * @return the forumPageProperties
	 */
    public PagePortletConfiguration getForumPageProperties() {
        return forumPageProperties;
    }

    /**
	 * @param forumPageProperties the forumPageProperties to set
	 */
    public void setForumPageProperties(PagePortletConfiguration forumPageProperties) {
        this.forumPageProperties = forumPageProperties;
    }

    /**
	 * @return the blogPageProperties
	 */
    public PagePortletConfiguration getBlogPageProperties() {
        return blogPageProperties;
    }

    /**
	 * @param blogPageProperties the blogPageProperties to set
	 */
    public void setBlogPageProperties(PagePortletConfiguration blogPageProperties) {
        this.blogPageProperties = blogPageProperties;
    }

    /**
	 * @return the documentPageProperties
	 */
    public PagePortletConfiguration getDocumentPageProperties() {
        return documentPageProperties;
    }

    /**
	 * @param documentPageProperties the documentPageProperties to set
	 */
    public void setDocumentPageProperties(PagePortletConfiguration documentPageProperties) {
        this.documentPageProperties = documentPageProperties;
    }
}
