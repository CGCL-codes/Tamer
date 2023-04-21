package com.liferay.portlet.shopping.service;

/**
 * <a href="ShoppingCategoryService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is <code>com.liferay.portlet.shopping.service.impl.ShoppingCategoryServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be accessed
 * remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.shopping.service.ShoppingCategoryServiceFactory
 * @see com.liferay.portlet.shopping.service.ShoppingCategoryServiceUtil
 *
 */
public interface ShoppingCategoryService {

    public com.liferay.portlet.shopping.model.ShoppingCategory addCategory(long plid, long parentCategoryId, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException;

    public com.liferay.portlet.shopping.model.ShoppingCategory addCategory(long plid, long parentCategoryId, java.lang.String name, java.lang.String description, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException;

    public void deleteCategory(long categoryId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException;

    public com.liferay.portlet.shopping.model.ShoppingCategory getCategory(long categoryId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException;

    public com.liferay.portlet.shopping.model.ShoppingCategory updateCategory(long categoryId, long parentCategoryId, java.lang.String name, java.lang.String description, boolean mergeWithParentCategory) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException;
}
