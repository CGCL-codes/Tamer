package ch.exm.storm.test.domain;

import java.util.Set;

public interface Order extends DomainObject {

    String getCode();

    void setCode(String code);

    Customer getCustomer();

    void setCustomer(Customer customer);

    Set<OrderedItem> getOrderedItems();

    void setOrderedItems(Set<OrderedItem> orderedItems);

    double getTotal();
}
