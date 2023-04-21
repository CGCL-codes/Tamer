package com.javaeye.delivery.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.javaeye.delivery.dto.OrderDetailBatchInfo;

public class OrderDetailBatchInfoDAOHibernate extends HibernateDaoSupport implements OrderDetailBatchInfoDAO {

    public void save(OrderDetailBatchInfo batchInfo) {
        getHibernateTemplate().saveOrUpdate(batchInfo);
    }

    @SuppressWarnings("unchecked")
    public OrderDetailBatchInfo getBatchInfo(String orderId, int productId, String batchNo) {
        final String queryString = "from OrderDetailBatchInfo bi where bi.orderId=? and bi.productId=? and bi.batchNo=?";
        List result = getHibernateTemplate().find(queryString, new Object[] { orderId, productId, batchNo });
        if (result == null || result.size() == 0) {
            return null;
        }
        return (OrderDetailBatchInfo) result.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<OrderDetailBatchInfo> getBatchInfo(int orderDetailId) {
        final String queryString = "from OrderDetailBatchInfo bi where bi.orderDetailId=?";
        List result = getHibernateTemplate().find(queryString, orderDetailId);
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<OrderDetailBatchInfo> getBatchInfo(String orderId, int productId) {
        final String queryString = "from OrderDetailBatchInfo bi where bi.orderId=? and bi.productId=? order by bi.batchNo";
        List result = getHibernateTemplate().find(queryString, new Object[] { orderId, productId });
        return result;
    }

    public void removeOrderDetailBatchInfo(int batchInfoId) {
        OrderDetailBatchInfo o = (OrderDetailBatchInfo) getHibernateTemplate().load(OrderDetailBatchInfo.class, batchInfoId);
        getHibernateTemplate().delete(o);
    }
}
