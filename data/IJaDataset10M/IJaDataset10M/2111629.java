package org.broadleafcommerce.core.order.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.broadleafcommerce.core.order.domain.GiftWrapOrderItem;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.domain.OrderItemImpl;
import org.broadleafcommerce.core.order.domain.PersonalMessage;
import org.broadleafcommerce.core.order.service.type.OrderItemType;
import org.broadleafcommerce.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

@Repository("blOrderItemDao")
public class OrderItemDaoImpl implements OrderItemDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public OrderItem save(final OrderItem orderItem) {
        return em.merge(orderItem);
    }

    public OrderItem readOrderItemById(final Long orderItemId) {
        return em.find(OrderItemImpl.class, orderItemId);
    }

    public void delete(OrderItem orderItem) {
        if (!em.contains(orderItem)) {
            orderItem = readOrderItemById(orderItem.getId());
        }
        if (GiftWrapOrderItem.class.isAssignableFrom(orderItem.getClass())) {
            final GiftWrapOrderItem giftItem = (GiftWrapOrderItem) orderItem;
            for (OrderItem wrappedItem : giftItem.getWrappedItems()) {
                wrappedItem.setGiftWrapOrderItem(null);
                wrappedItem = save(wrappedItem);
            }
        }
        em.remove(orderItem);
    }

    public OrderItem create(final OrderItemType orderItemType) {
        final OrderItem item = (OrderItem) entityConfiguration.createEntityInstance(orderItemType.getType());
        item.setOrderItemType(orderItemType);
        return item;
    }

    public PersonalMessage createPersonalMessage() {
        PersonalMessage personalMessage = (PersonalMessage) entityConfiguration.createEntityInstance(PersonalMessage.class.getName());
        return personalMessage;
    }

    public OrderItem saveOrderItem(final OrderItem orderItem) {
        return em.merge(orderItem);
    }
}
