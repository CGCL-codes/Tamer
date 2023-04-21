package org.libreplan.web.common.converters;

import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.business.orders.daos.IOrderDAO;
import org.libreplan.business.orders.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A {@link IConverter} for {@link Order} <br />
 * @author Óscar González Fernández <ogonzalez@igalia.com>
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class OrderConverter implements IConverter<Order> {

    @Autowired
    private IOrderDAO orderDAO;

    @Override
    @Transactional(readOnly = true)
    public Order asObject(String stringRepresentation) {
        try {
            return orderDAO.find(Long.parseLong(stringRepresentation));
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String asString(Order entity) {
        return entity.getId() + "";
    }

    @Override
    public String asStringUngeneric(Object entity) {
        return asString((Order) entity);
    }

    @Override
    public Class<Order> getType() {
        return Order.class;
    }
}
