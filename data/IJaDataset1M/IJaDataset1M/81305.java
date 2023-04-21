package com.liferay.portlet.shopping.service.base;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.DynamicQueryInitializer;
import com.liferay.portlet.shopping.service.ShoppingCartLocalService;
import com.liferay.portlet.shopping.service.ShoppingCategoryLocalService;
import com.liferay.portlet.shopping.service.ShoppingCategoryLocalServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingCategoryService;
import com.liferay.portlet.shopping.service.ShoppingCategoryServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingCouponLocalService;
import com.liferay.portlet.shopping.service.ShoppingCouponLocalServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingCouponService;
import com.liferay.portlet.shopping.service.ShoppingCouponServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingItemFieldLocalService;
import com.liferay.portlet.shopping.service.ShoppingItemFieldLocalServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingItemLocalService;
import com.liferay.portlet.shopping.service.ShoppingItemLocalServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingItemPriceLocalService;
import com.liferay.portlet.shopping.service.ShoppingItemPriceLocalServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingItemService;
import com.liferay.portlet.shopping.service.ShoppingItemServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingOrderItemLocalService;
import com.liferay.portlet.shopping.service.ShoppingOrderItemLocalServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingOrderLocalService;
import com.liferay.portlet.shopping.service.ShoppingOrderLocalServiceFactory;
import com.liferay.portlet.shopping.service.ShoppingOrderService;
import com.liferay.portlet.shopping.service.ShoppingOrderServiceFactory;
import com.liferay.portlet.shopping.service.persistence.ShoppingCartPersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingCartUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingCategoryPersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingCategoryUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingCouponPersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingCouponUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemFieldPersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemFieldUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemPersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemPricePersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemPriceUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemPersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingOrderItemUtil;
import com.liferay.portlet.shopping.service.persistence.ShoppingOrderPersistence;
import com.liferay.portlet.shopping.service.persistence.ShoppingOrderUtil;
import org.springframework.beans.factory.InitializingBean;
import java.util.List;

/**
 * <a href="ShoppingCartLocalServiceBaseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class ShoppingCartLocalServiceBaseImpl implements ShoppingCartLocalService, InitializingBean {

    public List dynamicQuery(DynamicQueryInitializer queryInitializer) throws SystemException {
        return ShoppingCartUtil.findWithDynamicQuery(queryInitializer);
    }

    public List dynamicQuery(DynamicQueryInitializer queryInitializer, int begin, int end) throws SystemException {
        return ShoppingCartUtil.findWithDynamicQuery(queryInitializer, begin, end);
    }

    public ShoppingCartPersistence getShoppingCartPersistence() {
        return shoppingCartPersistence;
    }

    public void setShoppingCartPersistence(ShoppingCartPersistence shoppingCartPersistence) {
        this.shoppingCartPersistence = shoppingCartPersistence;
    }

    public ShoppingCategoryLocalService getShoppingCategoryLocalService() {
        return shoppingCategoryLocalService;
    }

    public void setShoppingCategoryLocalService(ShoppingCategoryLocalService shoppingCategoryLocalService) {
        this.shoppingCategoryLocalService = shoppingCategoryLocalService;
    }

    public ShoppingCategoryService getShoppingCategoryService() {
        return shoppingCategoryService;
    }

    public void setShoppingCategoryService(ShoppingCategoryService shoppingCategoryService) {
        this.shoppingCategoryService = shoppingCategoryService;
    }

    public ShoppingCategoryPersistence getShoppingCategoryPersistence() {
        return shoppingCategoryPersistence;
    }

    public void setShoppingCategoryPersistence(ShoppingCategoryPersistence shoppingCategoryPersistence) {
        this.shoppingCategoryPersistence = shoppingCategoryPersistence;
    }

    public ShoppingCouponLocalService getShoppingCouponLocalService() {
        return shoppingCouponLocalService;
    }

    public void setShoppingCouponLocalService(ShoppingCouponLocalService shoppingCouponLocalService) {
        this.shoppingCouponLocalService = shoppingCouponLocalService;
    }

    public ShoppingCouponService getShoppingCouponService() {
        return shoppingCouponService;
    }

    public void setShoppingCouponService(ShoppingCouponService shoppingCouponService) {
        this.shoppingCouponService = shoppingCouponService;
    }

    public ShoppingCouponPersistence getShoppingCouponPersistence() {
        return shoppingCouponPersistence;
    }

    public void setShoppingCouponPersistence(ShoppingCouponPersistence shoppingCouponPersistence) {
        this.shoppingCouponPersistence = shoppingCouponPersistence;
    }

    public ShoppingItemLocalService getShoppingItemLocalService() {
        return shoppingItemLocalService;
    }

    public void setShoppingItemLocalService(ShoppingItemLocalService shoppingItemLocalService) {
        this.shoppingItemLocalService = shoppingItemLocalService;
    }

    public ShoppingItemService getShoppingItemService() {
        return shoppingItemService;
    }

    public void setShoppingItemService(ShoppingItemService shoppingItemService) {
        this.shoppingItemService = shoppingItemService;
    }

    public ShoppingItemPersistence getShoppingItemPersistence() {
        return shoppingItemPersistence;
    }

    public void setShoppingItemPersistence(ShoppingItemPersistence shoppingItemPersistence) {
        this.shoppingItemPersistence = shoppingItemPersistence;
    }

    public ShoppingItemFieldLocalService getShoppingItemFieldLocalService() {
        return shoppingItemFieldLocalService;
    }

    public void setShoppingItemFieldLocalService(ShoppingItemFieldLocalService shoppingItemFieldLocalService) {
        this.shoppingItemFieldLocalService = shoppingItemFieldLocalService;
    }

    public ShoppingItemFieldPersistence getShoppingItemFieldPersistence() {
        return shoppingItemFieldPersistence;
    }

    public void setShoppingItemFieldPersistence(ShoppingItemFieldPersistence shoppingItemFieldPersistence) {
        this.shoppingItemFieldPersistence = shoppingItemFieldPersistence;
    }

    public ShoppingItemPriceLocalService getShoppingItemPriceLocalService() {
        return shoppingItemPriceLocalService;
    }

    public void setShoppingItemPriceLocalService(ShoppingItemPriceLocalService shoppingItemPriceLocalService) {
        this.shoppingItemPriceLocalService = shoppingItemPriceLocalService;
    }

    public ShoppingItemPricePersistence getShoppingItemPricePersistence() {
        return shoppingItemPricePersistence;
    }

    public void setShoppingItemPricePersistence(ShoppingItemPricePersistence shoppingItemPricePersistence) {
        this.shoppingItemPricePersistence = shoppingItemPricePersistence;
    }

    public ShoppingOrderLocalService getShoppingOrderLocalService() {
        return shoppingOrderLocalService;
    }

    public void setShoppingOrderLocalService(ShoppingOrderLocalService shoppingOrderLocalService) {
        this.shoppingOrderLocalService = shoppingOrderLocalService;
    }

    public ShoppingOrderService getShoppingOrderService() {
        return shoppingOrderService;
    }

    public void setShoppingOrderService(ShoppingOrderService shoppingOrderService) {
        this.shoppingOrderService = shoppingOrderService;
    }

    public ShoppingOrderPersistence getShoppingOrderPersistence() {
        return shoppingOrderPersistence;
    }

    public void setShoppingOrderPersistence(ShoppingOrderPersistence shoppingOrderPersistence) {
        this.shoppingOrderPersistence = shoppingOrderPersistence;
    }

    public ShoppingOrderItemLocalService getShoppingOrderItemLocalService() {
        return shoppingOrderItemLocalService;
    }

    public void setShoppingOrderItemLocalService(ShoppingOrderItemLocalService shoppingOrderItemLocalService) {
        this.shoppingOrderItemLocalService = shoppingOrderItemLocalService;
    }

    public ShoppingOrderItemPersistence getShoppingOrderItemPersistence() {
        return shoppingOrderItemPersistence;
    }

    public void setShoppingOrderItemPersistence(ShoppingOrderItemPersistence shoppingOrderItemPersistence) {
        this.shoppingOrderItemPersistence = shoppingOrderItemPersistence;
    }

    public void afterPropertiesSet() {
        if (shoppingCartPersistence == null) {
            shoppingCartPersistence = ShoppingCartUtil.getPersistence();
        }
        if (shoppingCategoryLocalService == null) {
            shoppingCategoryLocalService = ShoppingCategoryLocalServiceFactory.getImpl();
        }
        if (shoppingCategoryService == null) {
            shoppingCategoryService = ShoppingCategoryServiceFactory.getImpl();
        }
        if (shoppingCategoryPersistence == null) {
            shoppingCategoryPersistence = ShoppingCategoryUtil.getPersistence();
        }
        if (shoppingCouponLocalService == null) {
            shoppingCouponLocalService = ShoppingCouponLocalServiceFactory.getImpl();
        }
        if (shoppingCouponService == null) {
            shoppingCouponService = ShoppingCouponServiceFactory.getImpl();
        }
        if (shoppingCouponPersistence == null) {
            shoppingCouponPersistence = ShoppingCouponUtil.getPersistence();
        }
        if (shoppingItemLocalService == null) {
            shoppingItemLocalService = ShoppingItemLocalServiceFactory.getImpl();
        }
        if (shoppingItemService == null) {
            shoppingItemService = ShoppingItemServiceFactory.getImpl();
        }
        if (shoppingItemPersistence == null) {
            shoppingItemPersistence = ShoppingItemUtil.getPersistence();
        }
        if (shoppingItemFieldLocalService == null) {
            shoppingItemFieldLocalService = ShoppingItemFieldLocalServiceFactory.getImpl();
        }
        if (shoppingItemFieldPersistence == null) {
            shoppingItemFieldPersistence = ShoppingItemFieldUtil.getPersistence();
        }
        if (shoppingItemPriceLocalService == null) {
            shoppingItemPriceLocalService = ShoppingItemPriceLocalServiceFactory.getImpl();
        }
        if (shoppingItemPricePersistence == null) {
            shoppingItemPricePersistence = ShoppingItemPriceUtil.getPersistence();
        }
        if (shoppingOrderLocalService == null) {
            shoppingOrderLocalService = ShoppingOrderLocalServiceFactory.getImpl();
        }
        if (shoppingOrderService == null) {
            shoppingOrderService = ShoppingOrderServiceFactory.getImpl();
        }
        if (shoppingOrderPersistence == null) {
            shoppingOrderPersistence = ShoppingOrderUtil.getPersistence();
        }
        if (shoppingOrderItemLocalService == null) {
            shoppingOrderItemLocalService = ShoppingOrderItemLocalServiceFactory.getImpl();
        }
        if (shoppingOrderItemPersistence == null) {
            shoppingOrderItemPersistence = ShoppingOrderItemUtil.getPersistence();
        }
    }

    protected ShoppingCartPersistence shoppingCartPersistence;

    protected ShoppingCategoryLocalService shoppingCategoryLocalService;

    protected ShoppingCategoryService shoppingCategoryService;

    protected ShoppingCategoryPersistence shoppingCategoryPersistence;

    protected ShoppingCouponLocalService shoppingCouponLocalService;

    protected ShoppingCouponService shoppingCouponService;

    protected ShoppingCouponPersistence shoppingCouponPersistence;

    protected ShoppingItemLocalService shoppingItemLocalService;

    protected ShoppingItemService shoppingItemService;

    protected ShoppingItemPersistence shoppingItemPersistence;

    protected ShoppingItemFieldLocalService shoppingItemFieldLocalService;

    protected ShoppingItemFieldPersistence shoppingItemFieldPersistence;

    protected ShoppingItemPriceLocalService shoppingItemPriceLocalService;

    protected ShoppingItemPricePersistence shoppingItemPricePersistence;

    protected ShoppingOrderLocalService shoppingOrderLocalService;

    protected ShoppingOrderService shoppingOrderService;

    protected ShoppingOrderPersistence shoppingOrderPersistence;

    protected ShoppingOrderItemLocalService shoppingOrderItemLocalService;

    protected ShoppingOrderItemPersistence shoppingOrderItemPersistence;
}
