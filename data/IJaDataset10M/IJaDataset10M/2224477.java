package com.liferay.portlet.wiki.service;

/**
 * <a href="WikiNodeLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is <code>com.liferay.portlet.wiki.service.impl.WikiNodeLocalServiceImpl</code>.
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
 * @see com.liferay.portlet.wiki.service.WikiNodeServiceFactory
 * @see com.liferay.portlet.wiki.service.WikiNodeServiceUtil
 *
 */
public interface WikiNodeLocalService {

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException;

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.wiki.service.persistence.WikiNodePersistence getWikiNodePersistence();

    public void setWikiNodePersistence(com.liferay.portlet.wiki.service.persistence.WikiNodePersistence wikiNodePersistence);

    public com.liferay.portlet.wiki.service.persistence.WikiPagePersistence getWikiPagePersistence();

    public void setWikiPagePersistence(com.liferay.portlet.wiki.service.persistence.WikiPagePersistence wikiPagePersistence);

    public com.liferay.portlet.wiki.service.persistence.WikiPageResourcePersistence getWikiPageResourcePersistence();

    public void setWikiPageResourcePersistence(com.liferay.portlet.wiki.service.persistence.WikiPageResourcePersistence wikiPageResourcePersistence);

    public void afterPropertiesSet();

    public com.liferay.portlet.wiki.model.WikiNode addNode(long userId, long plid, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public com.liferay.portlet.wiki.model.WikiNode addNode(long userId, long plid, java.lang.String name, java.lang.String description, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public com.liferay.portlet.wiki.model.WikiNode addNode(long userId, long plid, java.lang.String name, java.lang.String description, java.lang.Boolean addCommunityPermissions, java.lang.Boolean addGuestPermissions, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addNodeResources(long nodeId, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addNodeResources(com.liferay.portlet.wiki.model.WikiNode node, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addNodeResources(long nodeId, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void addNodeResources(com.liferay.portlet.wiki.model.WikiNode node, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deleteNode(long nodeId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deleteNode(com.liferay.portlet.wiki.model.WikiNode node) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public void deleteNodes(long groupId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public com.liferay.portlet.wiki.model.WikiNode getNode(long nodeId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public java.util.List getNodes(long groupId) throws com.liferay.portal.SystemException;

    public java.util.List getNodes(long groupId, int begin, int end) throws com.liferay.portal.SystemException;

    public int getNodesCount(long groupId) throws com.liferay.portal.SystemException;

    public void reIndex(java.lang.String[] ids) throws com.liferay.portal.SystemException;

    public com.liferay.portal.kernel.search.Hits search(long companyId, long groupId, long[] nodeIds, java.lang.String keywords) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.wiki.model.WikiNode updateNode(long nodeId, java.lang.String name, java.lang.String description) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;
}
