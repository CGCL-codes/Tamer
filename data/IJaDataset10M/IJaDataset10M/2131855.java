package devbureau.fstore.common;

import devbureau.fstore.common.db.CustomerRegistryDBImpl;

public class CustomerRegistryFactory {

    private static CustomerRegistryDBImpl registry = null;

    public static CustomerRegistry getCustomerRegistry() throws Exception {
        if (registry == null) {
            registry = new CustomerRegistryDBImpl();
        }
        return registry;
    }
}
