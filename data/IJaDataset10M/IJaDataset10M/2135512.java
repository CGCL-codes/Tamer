package com.liferay.portlet.shopping.service;

/**
 * <a href="ShoppingCartLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the <code>com.liferay.portlet.shopping.service.ShoppingCartLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean instance.
 * It's convenient to be able to just write one line to call a method on a bean
 * instead of writing a lookup call and a method call.
 * </p>
 *
 * <p>
 * <code>com.liferay.portlet.shopping.service.ShoppingCartLocalServiceFactory</code>
 * is responsible for the lookup of the bean.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.shopping.service.ShoppingCartLocalService
 * @see com.liferay.portlet.shopping.service.ShoppingCartLocalServiceFactory
 *
 */
public class ShoppingCartLocalServiceUtil {

    public static java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.dynamicQuery(queryInitializer);
    }

    public static java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.dynamicQuery(queryInitializer, begin, end);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingCartPersistence getShoppingCartPersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingCartPersistence();
    }

    public static void setShoppingCartPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingCartPersistence shoppingCartPersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingCartPersistence(shoppingCartPersistence);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingCategoryPersistence getShoppingCategoryPersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingCategoryPersistence();
    }

    public static void setShoppingCategoryPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingCategoryPersistence shoppingCategoryPersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingCategoryPersistence(shoppingCategoryPersistence);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingCouponPersistence getShoppingCouponPersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingCouponPersistence();
    }

    public static void setShoppingCouponPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingCouponPersistence shoppingCouponPersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingCouponPersistence(shoppingCouponPersistence);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingItemPersistence getShoppingItemPersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingItemPersistence();
    }

    public static void setShoppingItemPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingItemPersistence shoppingItemPersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingItemPersistence(shoppingItemPersistence);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingItemFieldPersistence getShoppingItemFieldPersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingItemFieldPersistence();
    }

    public static void setShoppingItemFieldPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingItemFieldPersistence shoppingItemFieldPersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingItemFieldPersistence(shoppingItemFieldPersistence);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingItemPricePersistence getShoppingItemPricePersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingItemPricePersistence();
    }

    public static void setShoppingItemPricePersistence(com.liferay.portlet.shopping.service.persistence.ShoppingItemPricePersistence shoppingItemPricePersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingItemPricePersistence(shoppingItemPricePersistence);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingOrderPersistence getShoppingOrderPersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingOrderPersistence();
    }

    public static void setShoppingOrderPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingOrderPersistence shoppingOrderPersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingOrderPersistence(shoppingOrderPersistence);
    }

    public static com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemPersistence getShoppingOrderItemPersistence() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getShoppingOrderItemPersistence();
    }

    public static void setShoppingOrderItemPersistence(com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemPersistence shoppingOrderItemPersistence) {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.setShoppingOrderItemPersistence(shoppingOrderItemPersistence);
    }

    public static void afterPropertiesSet() {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.afterPropertiesSet();
    }

    public static void deleteGroupCarts(long groupId) throws com.liferay.portal.SystemException {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.deleteGroupCarts(groupId);
    }

    public static void deleteUserCarts(long userId) throws com.liferay.portal.SystemException {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        shoppingCartLocalService.deleteUserCarts(userId);
    }

    public static com.liferay.portlet.shopping.model.ShoppingCart getCart(long userId, long groupId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getCart(userId, groupId);
    }

    public static java.util.Map getItems(long groupId, java.lang.String itemIds) throws com.liferay.portal.SystemException {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.getItems(groupId, itemIds);
    }

    public static com.liferay.portlet.shopping.model.ShoppingCart updateCart(long userId, long groupId, java.lang.String itemIds, java.lang.String couponCodes, int altShipping, boolean insure) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        ShoppingCartLocalService shoppingCartLocalService = ShoppingCartLocalServiceFactory.getService();
        return shoppingCartLocalService.updateCart(userId, groupId, itemIds, couponCodes, altShipping, insure);
    }
}
