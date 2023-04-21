package org.nakedobjects.example.ecs;

import org.nakedobjects.applib.AbstractFactoryAndRepository;

public class CustomerFactory extends AbstractFactoryAndRepository {

    public Customer newCustomer() {
        return (Customer) newTransientInstance(Customer.class);
    }

    public String iconName() {
        return "Customer";
    }
}
