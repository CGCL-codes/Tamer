package gemini.basic.dao.impl;

import gemini.basic.dao.GmnDao;
import gemini.basic.dao.OrderDao;
import gemini.basic.model.AbstractDomain;
import gemini.basic.model.Order;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 *
 */
public class OrderDaoImpl extends HibernateDaoSupport implements OrderDao {

    private GmnDao gmnDao;

    public void setGmnDao(GmnDao gmnDao) {
        this.gmnDao = gmnDao;
    }

    @Override
    public Order saveOrUpdate(Order order, boolean flush) {
        Order result = null;
        if (flush) {
            result = (Order) gmnDao.saveOrUpdateAndFlush(order);
        } else {
            result = (Order) gmnDao.saveOrUpdate(order);
        }
        return result;
    }

    @Override
    public boolean delete(Order order) {
        getHibernateTemplate().delete(order);
        return true;
    }

    @Override
    public Order getById(int id) {
        DetachedCriteria dtCri = DetachedCriteria.forClass(Order.class);
        if (id != AbstractDomain.TRANSIENT_ENTITY_ID) {
            dtCri.add(Restrictions.eq("id", id));
        }
        List<Object> resultList = gmnDao.findByCriteria(dtCri);
        Order result = null;
        if (resultList != null && !resultList.isEmpty()) {
            result = (Order) resultList.get(0);
        }
        return result;
    }

    @Override
    public List<Order> findUnusedOrders() {
        DetachedCriteria dtCri = DetachedCriteria.forClass(Order.class);
        dtCri.createAlias("orderStatuses", "stt");
        dtCri.add(Restrictions.eq("stt.code", "UNU"));
        dtCri.add(Restrictions.isNull("stt.validTo"));
        List<Object> orderList = gmnDao.findByCriteria(dtCri);
        List<Order> result = null;
        if (orderList != null) {
            result = new ArrayList<Order>(orderList.size());
            for (Object cart : orderList) {
                result.add((Order) cart);
            }
        }
        return result;
    }
}
