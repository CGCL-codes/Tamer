package com.liferay.portlet.blogs.service;

/**
 * <a href="BlogsCategoryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the <code>com.liferay.portlet.blogs.service.BlogsCategoryService</code>
 * bean. The static methods of this class calls the same methods of the bean instance.
 * It's convenient to be able to just write one line to call a method on a bean
 * instead of writing a lookup call and a method call.
 * </p>
 *
 * <p>
 * <code>com.liferay.portlet.blogs.service.BlogsCategoryServiceFactory</code> is
 * responsible for the lookup of the bean.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.blogs.service.BlogsCategoryService
 * @see com.liferay.portlet.blogs.service.BlogsCategoryServiceFactory
 *
 */
public class BlogsCategoryServiceUtil {

    public static com.liferay.portlet.blogs.model.BlogsCategory addCategory(long parentCategoryId, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        BlogsCategoryService blogsCategoryService = BlogsCategoryServiceFactory.getService();
        return blogsCategoryService.addCategory(parentCategoryId, name, description, addCommunityPermissions, addGuestPermissions);
    }

    public static com.liferay.portlet.blogs.model.BlogsCategory addCategory(long parentCategoryId, java.lang.String name, java.lang.String description, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        BlogsCategoryService blogsCategoryService = BlogsCategoryServiceFactory.getService();
        return blogsCategoryService.addCategory(parentCategoryId, name, description, communityPermissions, guestPermissions);
    }

    public static void deleteCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        BlogsCategoryService blogsCategoryService = BlogsCategoryServiceFactory.getService();
        blogsCategoryService.deleteCategory(categoryId);
    }

    public static com.liferay.portlet.blogs.model.BlogsCategory getCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        BlogsCategoryService blogsCategoryService = BlogsCategoryServiceFactory.getService();
        return blogsCategoryService.getCategory(categoryId);
    }

    public static com.liferay.portlet.blogs.model.BlogsCategory updateCategory(long categoryId, long parentCategoryId, java.lang.String name, java.lang.String description) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        BlogsCategoryService blogsCategoryService = BlogsCategoryServiceFactory.getService();
        return blogsCategoryService.updateCategory(categoryId, parentCategoryId, name, description);
    }
}
