package com.germinus.merlin.mock;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.easymock.EasyMock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.portlet.MockPortletSession;
import org.springframework.mock.web.portlet.MockRenderRequest;
import com.germinus.merlin.manager.layout.ILayoutManager;
import com.germinus.merlin.manager.layout.ILayoutManagerFactory;
import com.germinus.merlin.page.PagePortletConfiguration;
import com.germinus.merlin.util.IMerlinUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

public class MockMerlinFactory extends MockLiferayFactory implements IMockMerlinFactory {

    private PagePortletConfiguration blogPageConfiguration;

    private PagePortletConfiguration documentPageConfiguration;

    private PagePortletConfiguration forumPageConfiguration;

    public MockRenderRequest getRenderRequest() {
        MockRenderRequest request = super.getRenderRequest();
        request.setParameter("folderId", new Long(MockMerlinKeys.DEFAULT_FOLDER_ID).toString());
        request.setParameter("folderId", new Long(MockMerlinKeys.DEFAULT_PARENT_FOLDER_ID).toString());
        MockRenderRequest renderRequest = new MockRenderRequest();
        renderRequest.addParameter("viewDetails", "true");
        renderRequest.setSession(new MockPortletSession(null));
        renderRequest.setContextPath("assignments/");
        return renderRequest;
    }

    public MockMerlinFactory() {
    }

    private ILayoutManager buildLayoutManager(PagePortletConfiguration pagePortletConfiguration, long pageId) {
        ILayoutManager layoutManager = EasyMock.createNiceMock(ILayoutManager.class);
        try {
            EasyMock.expect(layoutManager.addPage(getRenderRequest(), getRenderResponse(), pagePortletConfiguration)).andStubReturn(pageId);
            EasyMock.expect(layoutManager.addSubPage(getRenderRequest(), getRenderResponse(), pagePortletConfiguration)).andStubReturn(pageId);
            EasyMock.expect(layoutManager.addPortlet(getRenderRequest(), getRenderResponse(), MockMerlinKeys.DEFAULT_PORTLET_ID)).andStubReturn(MockMerlinKeys.DEFAULT_PORTLET_ID);
            EasyMock.expect(layoutManager.deletePage(MockMerlinKeys.DEFAULT_LAYOUT_ID)).andStubReturn(pageId);
            EasyMock.expect(layoutManager.getPageProperties()).andStubReturn(pagePortletConfiguration);
            EasyMock.replay(layoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layoutManager;
    }

    public PagePortletConfiguration getBlogPageConfiguration() {
        return blogPageConfiguration;
    }

    public PagePortletConfiguration getDocumentPageConfiguration() {
        return documentPageConfiguration;
    }

    public PagePortletConfiguration getForumPageConfiguration() {
        return forumPageConfiguration;
    }

    public ILayoutManager getLayoutManager(int type) {
        if (type == ILayoutManagerFactory.BLOG) {
            return buildLayoutManager(blogPageConfiguration, MockMerlinKeys.BLOG_PAGE_ID);
        } else if (type == ILayoutManagerFactory.DOCUMENT) {
            return buildLayoutManager(documentPageConfiguration, MockMerlinKeys.DOCUMENT_PAGE_ID);
        } else if (type == ILayoutManagerFactory.FORUM) {
            return buildLayoutManager(documentPageConfiguration, MockMerlinKeys.DOCUMENT_PAGE_ID);
        } else {
            return null;
        }
    }

    public ILayoutManagerFactory getLayoutManagerFactory() {
        ILayoutManagerFactory layoutManagerFactory = EasyMock.createNiceMock(ILayoutManagerFactory.class);
        EasyMock.expect(layoutManagerFactory.getLayoutManager(ILayoutManagerFactory.DOCUMENT)).andStubReturn(getLayoutManager(ILayoutManagerFactory.DOCUMENT));
        EasyMock.expect(layoutManagerFactory.getLayoutManager(ILayoutManagerFactory.FORUM)).andStubReturn(getLayoutManager(ILayoutManagerFactory.FORUM));
        EasyMock.expect(layoutManagerFactory.getLayoutManager(ILayoutManagerFactory.BLOG)).andStubReturn(getLayoutManager(ILayoutManagerFactory.BLOG));
        EasyMock.replay(layoutManagerFactory);
        return layoutManagerFactory;
    }

    public IMerlinUtil getMerlinUtil() throws PortalException, SystemException {
        IMerlinUtil merlinUtil = EasyMock.createNiceMock(IMerlinUtil.class);
        MockServletContext mockServletContext = new MockServletContext();
        EasyMock.expect(merlinUtil.getServletContext((RenderRequest) EasyMock.anyObject(), (RenderResponse) EasyMock.anyObject())).andStubReturn(mockServletContext);
        EasyMock.expect(merlinUtil.getServletContext((RenderRequest) EasyMock.anyObject(), (RenderResponse) EasyMock.anyObject(), (String) EasyMock.anyObject())).andStubReturn(mockServletContext);
        EasyMock.expect(merlinUtil.getHttpServletRequest((RenderRequest) EasyMock.anyObject(), (RenderResponse) EasyMock.anyObject())).andStubReturn(new MockHttpServletRequest());
        EasyMock.expect(merlinUtil.getHttpServletResponse((RenderRequest) EasyMock.anyObject(), (RenderResponse) EasyMock.anyObject())).andStubReturn(new MockHttpServletResponse());
        EasyMock.expect(merlinUtil.getUserType((RenderRequest) EasyMock.anyObject(), (RenderResponse) EasyMock.anyObject(), EasyMock.anyLong())).andStubReturn(MockMerlinKeys.TEACHER);
        EasyMock.replay(merlinUtil);
        return merlinUtil;
    }

    public void setBlogPageConfiguration(PagePortletConfiguration blogPageConfiguration) {
        this.blogPageConfiguration = blogPageConfiguration;
    }

    public void setDocumentPageConfiguration(PagePortletConfiguration documentPageConfiguration) {
        this.documentPageConfiguration = documentPageConfiguration;
    }

    public void setForumPageConfiguration(PagePortletConfiguration forumPageConfiguration) {
        this.forumPageConfiguration = forumPageConfiguration;
    }
}
