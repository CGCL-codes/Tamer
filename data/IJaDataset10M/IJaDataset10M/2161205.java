package com.liferay.portlet.wiki.service;

/**
 * <a href="WikiPageLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is <code>com.liferay.portlet.wiki.service.impl.WikiPageLocalServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be accessed
 * from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.wiki.service.WikiPageServiceFactory
 * @see com.liferay.portlet.wiki.service.WikiPageServiceUtil
 *
 */
public interface WikiPageLocalService {

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException;

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.wiki.service.persistence.WikiNodePersistence getWikiNodePersistence();

    public void setWikiNodePersistence(com.liferay.portlet.wiki.service.persistence.WikiNodePersistence wikiNodePersistence);

    public com.liferay.portlet.wiki.service.persistence.WikiPagePersistence getWikiPagePersistence();

    public void setWikiPagePersistence(com.liferay.portlet.wiki.service.persistence.WikiPagePersistence wikiPagePersistence);

    public com.liferay.portlet.wiki.service.persistence.WikiPageResourcePersistence getWikiPageResourcePersistence();

    public void setWikiPageResourcePersistence(com.liferay.portlet.wiki.service.persistence.WikiPageResourcePersistence wikiPageResourcePersistence);

    public void afterPropertiesSet();

    public com.liferay.portlet.wiki.model.WikiPage addPage(long userId, long nodeId, java.lang.String title) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addPageResources(long nodeId, java.lang.String title, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addPageResources(com.liferay.portlet.wiki.model.WikiNode node, com.liferay.portlet.wiki.model.WikiPage page, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addPageResources(long nodeId, java.lang.String title, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addPageResources(com.liferay.portlet.wiki.model.WikiNode node, com.liferay.portlet.wiki.model.WikiPage page, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deletePage(long nodeId, java.lang.String title) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deletePage(com.liferay.portlet.wiki.model.WikiPage page) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deletePages(long nodeId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public java.util.List getLinks(long nodeId, java.lang.String title) throws com.liferay.portal.SystemException;

    public java.util.List getOrphans(long nodeId) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.wiki.model.WikiPage getPage(long nodeId, java.lang.String title) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public com.liferay.portlet.wiki.model.WikiPage getPage(long nodeId, java.lang.String title, double version) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public java.util.List getPages(long nodeId, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List getPages(long nodeId, java.lang.String title, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List getPages(long nodeId, boolean head, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List getPages(long nodeId, java.lang.String title, boolean head, int begin, int end) throws com.liferay.portal.SystemException;

    public int getPagesCount(long nodeId) throws com.liferay.portal.SystemException;

    public int getPagesCount(long nodeId, java.lang.String title) throws com.liferay.portal.SystemException;

    public int getPagesCount(long nodeId, boolean head) throws com.liferay.portal.SystemException;

    public int getPagesCount(long nodeId, java.lang.String title, boolean head) throws com.liferay.portal.SystemException;

    public java.util.List getRecentChanges(long nodeId, int begin, int end) throws com.liferay.portal.SystemException;

    public int getRecentChangesCount(long nodeId) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.wiki.model.WikiPage revertPage(long userId, long nodeId, java.lang.String title, double version) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public com.liferay.portlet.wiki.model.WikiPage updatePage(long userId, long nodeId, java.lang.String title, java.lang.String content, java.lang.String format, java.lang.String[] tagsEntries) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;
}
