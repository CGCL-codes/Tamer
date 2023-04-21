package net.shopxx.dao.impl;

import java.util.List;
import java.util.Set;
import net.shopxx.bean.Pager;
import net.shopxx.bean.Pager.OrderType;
import net.shopxx.dao.DeliveryTypeDao;
import net.shopxx.entity.DeliveryType;
import net.shopxx.entity.Order;
import net.shopxx.entity.Reship;
import net.shopxx.entity.Shipping;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

/**
 * Dao实现类 - 配送方式
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX8FA9BB4F72C2C9F3D643830EB2B318C3
 * ============================================================================
 */
@Repository
public class DeliveryTypeDaoImpl extends BaseDaoImpl<DeliveryType, String> implements DeliveryTypeDao {

    public DeliveryType getDefaultDeliveryType() {
        String hql = "from DeliveryType as deliveryType where deliveryType.isDefault = ?";
        return (DeliveryType) getSession().createQuery(hql).setParameter(0, true).setMaxResults(1).uniqueResult();
    }

    @Override
    public void delete(DeliveryType deliveryType) {
        Set<Order> orderSet = deliveryType.getOrderSet();
        if (orderSet != null) {
            for (Order order : orderSet) {
                order.setDeliveryType(null);
            }
        }
        Set<Shipping> shippingSet = deliveryType.getShippingSet();
        if (shippingSet != null) {
            for (Shipping shipping : shippingSet) {
                shipping.setDeliveryType(null);
            }
        }
        Set<Reship> reshipSet = deliveryType.getReshipSet();
        if (reshipSet != null) {
            for (Reship reship : reshipSet) {
                reship.setDeliveryType(null);
            }
        }
        super.delete(deliveryType);
    }

    @Override
    public void delete(String id) {
        DeliveryType deliveryType = super.load(id);
        this.delete(deliveryType);
    }

    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            DeliveryType deliveryType = super.load(id);
            this.delete(deliveryType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DeliveryType> getAll() {
        String hql = "from DeliveryType deliveryType order by deliveryType.orderList asc deliveryType.createDate desc";
        return getSession().createQuery(hql).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DeliveryType> getList(String propertyName, Object value) {
        String hql = "from DeliveryType deliveryType where deliveryType." + propertyName + "=? order by deliveryType.orderList asc deliveryType.createDate desc";
        return getSession().createQuery(hql).setParameter(0, value).list();
    }

    @Override
    public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria) {
        if (pager == null) {
            pager = new Pager();
            pager.setOrderBy("orderList");
            pager.setOrderType(OrderType.asc);
        }
        return super.findByPager(pager, detachedCriteria);
    }

    @Override
    public Pager findByPager(Pager pager) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeliveryType.class);
        return this.findByPager(pager, detachedCriteria);
    }
}
