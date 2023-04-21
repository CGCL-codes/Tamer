package com.liferay.portlet.shopping.service;

/**
 * <a href="ShoppingCategoryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the <code>com.liferay.portlet.shopping.service.ShoppingCategoryService</code>
 * bean. The static methods of this class calls the same methods of the bean instance.
 * It's convenient to be able to just write one line to call a method on a bean
 * instead of writing a lookup call and a method call.
 * </p>
 *
 * <p>
 * <code>com.liferay.portlet.shopping.service.ShoppingCategoryServiceFactory</code>
 * is responsible for the lookup of the bean.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.shopping.service.ShoppingCategoryService
 * @see com.liferay.portlet.shopping.service.ShoppingCategoryServiceFactory
 *
 */
public class ShoppingCategoryServiceUtil {

    public static com.liferay.portlet.shopping.model.ShoppingCategory addCategory(long plid, long parentCategoryId, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        ShoppingCategoryService shoppingCategoryService = ShoppingCategoryServiceFactory.getService();
        return shoppingCategoryService.addCategory(plid, parentCategoryId, name, description, addCommunityPermissions, addGuestPermissions);
    }

    public static com.liferay.portlet.shopping.model.ShoppingCategory addCategory(long plid, long parentCategoryId, java.lang.String name, java.lang.String description, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        ShoppingCategoryService shoppingCategoryService = ShoppingCategoryServiceFactory.getService();
        return shoppingCategoryService.addCategory(plid, parentCategoryId, name, description, communityPermissions, guestPermissions);
    }

    public static void deleteCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        ShoppingCategoryService shoppingCategoryService = ShoppingCategoryServiceFactory.getService();
        shoppingCategoryService.deleteCategory(categoryId);
    }

    public static com.liferay.portlet.shopping.model.ShoppingCategory getCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        ShoppingCategoryService shoppingCategoryService = ShoppingCategoryServiceFactory.getService();
        return shoppingCategoryService.getCategory(categoryId);
    }

    public static com.liferay.portlet.shopping.model.ShoppingCategory updateCategory(long categoryId, long parentCategoryId, java.lang.String name, java.lang.String description, boolean mergeWithParentCategory) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        ShoppingCategoryService shoppingCategoryService = ShoppingCategoryServiceFactory.getService();
        return shoppingCategoryService.updateCategory(categoryId, parentCategoryId, name, description, mergeWithParentCategory);
    }
}
