package com.liferay.portlet.imagegallery.service.spring;

/**
 * <a href="IGImageLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface IGImageLocalService {

    public com.liferay.portlet.imagegallery.model.IGImage addImage(java.lang.String userId, java.lang.String folderId, java.lang.String description, java.io.File file, java.lang.String contentType, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void addImageResources(java.lang.String folderId, java.lang.String imageId, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void addImageResources(com.liferay.portlet.imagegallery.model.IGFolder folder, com.liferay.portlet.imagegallery.model.IGImage image, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void deleteImage(java.lang.String companyId, java.lang.String imageId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void deleteImage(com.liferay.portlet.imagegallery.model.IGImage image) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void deleteImages(java.lang.String folderId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public int getFoldersImagesCount(java.util.List folderIds) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.imagegallery.model.IGImage getImage(java.lang.String companyId, java.lang.String imageId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public java.util.List getImages(java.lang.String folderId) throws com.liferay.portal.SystemException;

    public java.util.List getImages(java.lang.String folderId, int begin, int end) throws com.liferay.portal.SystemException;

    public java.util.List getImages(java.lang.String folderId, int begin, int end, com.liferay.util.dao.hibernate.OrderByComparator obc) throws com.liferay.portal.SystemException;

    public int getImagesCount(java.lang.String folderId) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.imagegallery.model.IGImage updateImage(java.lang.String companyId, java.lang.String imageId, java.lang.String folderId, java.lang.String description, java.io.File file, java.lang.String contentType) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;
}
