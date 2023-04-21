package net.algid.purchase.interfaces;

import java.util.List;
import net.algid.purchase.valueObject.Customer;

public interface ICustomerLogic {

    public abstract int add(Customer customer);

    public abstract List<Customer> getList(Customer customerFilter);

    public abstract void remove(int code);

    public abstract void save(Customer customer);

    public abstract Customer getObject(int code);
}
