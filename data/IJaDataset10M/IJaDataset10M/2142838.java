package com.liferay.portlet.shopping.service;

/**
 * <a href="ShoppingCartLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is <code>com.liferay.portlet.shopping.service.impl.ShoppingCartLocalServiceImpl</code>.
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
 * @see com.liferay.portlet.shopping.service.ShoppingCartServiceFactory
 * @see com.liferay.portlet.shopping.service.ShoppingCartServiceUtil
 *
 */
public interface ShoppingCartLocalService {

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException;

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.shopping.service.persistence.ShoppingCartPersistence getShoppingCartPersistence();

    public void setShoppingCartPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingCartPersistence shoppingCartPersistence);

    public com.liferay.portlet.shopping.service.persistence.ShoppingCategoryPersistence getShoppingCategoryPersistence();

    public void setShoppingCategoryPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingCategoryPersistence shoppingCategoryPersistence);

    public com.liferay.portlet.shopping.service.persistence.ShoppingCouponPersistence getShoppingCouponPersistence();

    public void setShoppingCouponPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingCouponPersistence shoppingCouponPersistence);

    public com.liferay.portlet.shopping.service.persistence.ShoppingItemPersistence getShoppingItemPersistence();

    public void setShoppingItemPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingItemPersistence shoppingItemPersistence);

    public com.liferay.portlet.shopping.service.persistence.ShoppingItemFieldPersistence getShoppingItemFieldPersistence();

    public void setShoppingItemFieldPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingItemFieldPersistence shoppingItemFieldPersistence);

    public com.liferay.portlet.shopping.service.persistence.ShoppingItemPricePersistence getShoppingItemPricePersistence();

    public void setShoppingItemPricePersistence(com.liferay.portlet.shopping.service.persistence.ShoppingItemPricePersistence shoppingItemPricePersistence);

    public com.liferay.portlet.shopping.service.persistence.ShoppingOrderPersistence getShoppingOrderPersistence();

    public void setShoppingOrderPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingOrderPersistence shoppingOrderPersistence);

    public com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemPersistence getShoppingOrderItemPersistence();

    public void setShoppingOrderItemPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemPersistence shoppingOrderItemPersistence);

    public void afterPropertiesSet();

    public void deleteGroupCarts(long groupId) throws com.liferay.portal.SystemException;

    public void deleteUserCarts(long userId) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.shopping.model.ShoppingCart getCart(long userId, long groupId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;

    public java.util.Map getItems(long groupId, java.lang.String itemIds) throws com.liferay.portal.SystemException;

    public com.liferay.portlet.shopping.model.ShoppingCart updateCart(long userId, long groupId, java.lang.String itemIds, java.lang.String couponCodes, int altShipping, boolean insure) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException;
}
