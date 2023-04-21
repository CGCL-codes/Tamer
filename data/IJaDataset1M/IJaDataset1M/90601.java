package dao;

import bean.OrderItem;

public interface IOrderItemDao {

    public OrderItem findById(int id);
}
