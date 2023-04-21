package riceSystem.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import riceSystem.dao.DaoTemplateInterface;
import riceSystem.entity.Order;

@Component("orderDao")
public class OrderDaoImpl implements DaoTemplateInterface<Order> {

    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public void delete(Order entity) {
        Order order = (Order) hibernateTemplate.load(entity.getClass(), entity.getId());
        hibernateTemplate.delete(order);
    }

    public void deleteById(long id) {
        Order order = (Order) hibernateTemplate.load(Order.class, id);
        hibernateTemplate.delete(order);
    }

    @SuppressWarnings("unchecked")
    public List<Order> loadAll() {
        return hibernateTemplate.loadAll(Order.class);
    }

    public Order loadById(long id) {
        return (Order) hibernateTemplate.load(Order.class, id);
    }

    public void save(Order entity) {
        hibernateTemplate.save(entity);
    }

    public long update(Order entity) {
        hibernateTemplate.update(entity);
        return entity.getId();
    }
}
