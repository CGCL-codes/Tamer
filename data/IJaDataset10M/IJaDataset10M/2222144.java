package com.liferay.portal.service.spring;

/**
 * <a href="ImageLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ImageLocalServiceUtil {

    public static void deleteImage(java.lang.String imageId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            ImageLocalService imageLocalService = ImageLocalServiceFactory.getService();
            imageLocalService.deleteImage(imageId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void deleteImages(java.lang.String imageId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            ImageLocalService imageLocalService = ImageLocalServiceFactory.getService();
            imageLocalService.deleteImages(imageId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Image getImage(java.lang.String imageId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            ImageLocalService imageLocalService = ImageLocalServiceFactory.getService();
            return imageLocalService.getImage(imageId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static java.util.List search(java.lang.String imageId) throws com.liferay.portal.SystemException {
        try {
            ImageLocalService imageLocalService = ImageLocalServiceFactory.getService();
            return imageLocalService.search(imageId);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Image updateImage(java.lang.String imageId, byte[] bytes) throws com.liferay.portal.SystemException {
        try {
            ImageLocalService imageLocalService = ImageLocalServiceFactory.getService();
            return imageLocalService.updateImage(imageId, bytes);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
