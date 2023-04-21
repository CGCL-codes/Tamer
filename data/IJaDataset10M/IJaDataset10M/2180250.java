package org.meruvian.yama.dao;

import java.util.List;
import org.meruvian.yama.dao.base.BaseDaoHibernate;
import org.meruvian.yama.entity.Item;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vick
 */
@Repository
public class ItemDao extends BaseDaoHibernate<Item> {

    @SuppressWarnings("unchecked")
    public List<Item> findItem(String name) {
        return sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName() + " where name like '%" + name + "%' ").list();
    }

    @SuppressWarnings("unchecked")
    public List<Item> itemCategory(String catagoryId) {
        return sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName() + " where category_id='" + catagoryId + "'").list();
    }

    @SuppressWarnings("unchecked")
    public List<Item> listItem() {
        return sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName() + " where category_id=null").list();
    }
}
