package com.openthinks.woms.order.service;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import com.openthinks.woms.order.OrderForm;
import com.openthinks.woms.order.dao.OrderDao;

/**
 * Order service implementing class
 * 
 * @author Zhang Junlong
 * 
 */
public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<OrderForm> readProductOrderByAccountId(String accountId, long productId) throws Exception {
        return this.orderDao.readProductOrderByAccountId(accountId, productId);
    }

    @Override
    public void create(List<OrderForm> orders) throws Exception {
        try {
            if (CollectionUtils.isNotEmpty(orders)) {
                OrderForm orderForm = orders.get(0);
                orderDao.delete(orderForm.getAccount().getId(), orderForm.getProduct().getId());
                for (OrderForm order : orders) {
                    orderDao.create(order);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void delete(int accountId, long productId) throws Exception {
        orderDao.delete(accountId, productId);
    }

    @Override
    public List<OrderForm> read(String accountId) throws Exception {
        return this.orderDao.read(accountId);
    }
}
