package com.sush.webstore.store.data.dao;

import java.util.Set;
import com.sush.webstore.store.domain.IOrder;

public interface IOrderDAO {

    IOrder getOrder(long id);

    Set<IOrder> getOrders(String userName);

    boolean addOrder(IOrder order);
}
