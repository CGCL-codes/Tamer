package org.broadleafcommerce.profile.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.broadleafcommerce.profile.dataprovider.CustomerDataProvider;
import org.broadleafcommerce.profile.domain.Customer;
import org.broadleafcommerce.profile.domain.IdGeneration;
import org.broadleafcommerce.profile.domain.IdGenerationImpl;
import org.broadleafcommerce.test.BaseTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

public class CustomerTest extends BaseTest {

    @Resource
    private CustomerService customerService;

    List<Long> userIds = new ArrayList<Long>();

    List<String> userNames = new ArrayList<String>();

    @Test(groups = { "createCustomerIdGeneration" })
    @Rollback(false)
    public void createCustomerIdGeneration() {
        IdGeneration idGeneration = new IdGenerationImpl();
        idGeneration.setType("org.broadleafcommerce.profile.domain.Customer");
        idGeneration.setBatchStart(1L);
        idGeneration.setBatchSize(10L);
        em.persist(idGeneration);
    }

    @Test(groups = "createCustomers", dependsOnGroups = "createCustomerIdGeneration", dataProvider = "setupCustomers", dataProviderClass = CustomerDataProvider.class)
    @Rollback(false)
    public void createCustomer(Customer customerInfo) {
        Customer customer = customerService.createCustomerFromId(null);
        customer.setPassword(customerInfo.getPassword());
        customer.setUsername(customerInfo.getUsername());
        Long customerId = customer.getId();
        assert customerId != null;
        customer = customerService.saveCustomer(customer);
        assert customer.getId() == customerId;
        userIds.add(customer.getId());
        userNames.add(customer.getUsername());
    }

    @Test(groups = { "readCustomer" }, dependsOnGroups = { "createCustomers" })
    public void readCustomersById() {
        for (Long userId : userIds) {
            Customer customer = customerService.readCustomerById(userId);
            assert customer.getId() == userId;
        }
    }

    @Test(groups = { "changeCustomerPassword" }, dependsOnGroups = { "readCustomer" })
    @Transactional
    @Rollback(false)
    public void changeCustomerPasswords() {
        for (String userName : userNames) {
            Customer customer = customerService.readCustomerByUsername(userName);
            customer.setPassword(customer.getPassword() + "-Changed");
            customerService.saveCustomer(customer);
        }
    }
}
