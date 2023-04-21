package com.liferay.portlet.messageboards.service.spring;

/**
 * <a href="MBCategoryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class MBCategoryServiceUtil {

    public static com.liferay.portlet.messageboards.model.MBCategory addCategory(java.lang.String plid, java.lang.String parentCategoryId, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            MBCategoryService mbCategoryService = MBCategoryServiceFactory.getService();
            return mbCategoryService.addCategory(plid, parentCategoryId, name, description, addCommunityPermissions, addGuestPermissions);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void deleteCategory(java.lang.String categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            MBCategoryService mbCategoryService = MBCategoryServiceFactory.getService();
            mbCategoryService.deleteCategory(categoryId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portlet.messageboards.model.MBCategory getCategory(java.lang.String categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            MBCategoryService mbCategoryService = MBCategoryServiceFactory.getService();
            return mbCategoryService.getCategory(categoryId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portlet.messageboards.model.MBCategory updateCategory(java.lang.String categoryId, java.lang.String parentCategoryId, java.lang.String name, java.lang.String description) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            MBCategoryService mbCategoryService = MBCategoryServiceFactory.getService();
            return mbCategoryService.updateCategory(categoryId, parentCategoryId, name, description);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
