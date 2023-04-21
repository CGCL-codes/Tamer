package com.ncs.flex.server.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.ncs.flex.to.OrderItemTO;

@SuppressWarnings("unchecked")
public class OrderItemDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(OrderItemDAO.class);

    public static final String ORDER_ID = "orderId";

    public static final String ORDER_ITEM_CODE = "orderItemCode";

    public static final String ORDER_ITEM_NAME = "orderItemName";

    public static final String ITEM_DETAILS = "itemDetails";

    public static final String ITEM_QTY = "itemQty";

    public static final String ITEM_SEQ = "itemSeq";

    public static final String UNIT_AMT = "unitAmt";

    public static final String TOTAL_AMT = "totalAmt";

    protected void initDao() {
    }

    public void save(OrderItemTO transientInstance) {
        log.debug("saving OrderItem instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(OrderItemTO persistentInstance) {
        log.debug("deleting OrderItem instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public OrderItemTO findById(java.lang.String id) {
        log.debug("getting OrderItem instance with id: " + id);
        try {
            OrderItemTO instance = (OrderItemTO) getHibernateTemplate().get("OrderItem", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<OrderItemTO> findByExample(OrderItemTO instance) {
        log.debug("finding OrderItem instance by example");
        try {
            List<OrderItemTO> results = (List<OrderItemTO>) getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding OrderItem instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from OrderItem as model where model." + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List<OrderItemTO> findByOrderId(Object orderId) {
        return findByProperty(ORDER_ID, orderId);
    }

    public List<OrderItemTO> findByOrderItemCode(Object orderItemCode) {
        return findByProperty(ORDER_ITEM_CODE, orderItemCode);
    }

    public List<OrderItemTO> findByOrderItemName(Object orderItemName) {
        return findByProperty(ORDER_ITEM_NAME, orderItemName);
    }

    public List<OrderItemTO> findByItemDetails(Object itemDetails) {
        return findByProperty(ITEM_DETAILS, itemDetails);
    }

    public List<OrderItemTO> findByItemQty(Object itemQty) {
        return findByProperty(ITEM_QTY, itemQty);
    }

    public List<OrderItemTO> findByItemSeq(Object itemSeq) {
        return findByProperty(ITEM_SEQ, itemSeq);
    }

    public List<OrderItemTO> findByUnitAmt(Object unitAmt) {
        return findByProperty(UNIT_AMT, unitAmt);
    }

    public List<OrderItemTO> findByTotalAmt(Object totalAmt) {
        return findByProperty(TOTAL_AMT, totalAmt);
    }

    public List findAll() {
        log.debug("finding all OrderItem instances");
        try {
            String queryString = "from OrderItem";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public OrderItemTO merge(OrderItemTO detachedInstance) {
        log.debug("merging OrderItem instance");
        try {
            OrderItemTO result = (OrderItemTO) getHibernateTemplate().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(OrderItemTO instance) {
        log.debug("attaching dirty OrderItem instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(OrderItemTO instance) {
        log.debug("attaching clean OrderItem instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public static OrderItemDAO getFromApplicationContext(ApplicationContext ctx) {
        return (OrderItemDAO) ctx.getBean("OrderItemDAO");
    }
}
