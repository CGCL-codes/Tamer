package com.quesofttech.business.domain.sales;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.quesofttech.business.common.exception.BusinessException;
import com.quesofttech.business.common.exception.DoesNotExistException;
import com.quesofttech.business.domain.base.BaseService;
import com.quesofttech.business.domain.sales.Customer;
import com.quesofttech.business.domain.sales.iface.ICustomerServiceLocal;
import com.quesofttech.business.domain.sales.iface.ICustomerServiceRemote;

@Stateless
@Local(ICustomerServiceLocal.class)
@Remote(ICustomerServiceRemote.class)
public class CustomerService extends BaseService implements ICustomerServiceLocal, ICustomerServiceRemote {

    public Customer findCustomer(Long id) throws DoesNotExistException {
        Customer customer = (Customer) find(Customer.class, id);
        return customer;
    }

    @SuppressWarnings("unchecked")
    public List<Customer> findCustomers() throws DoesNotExistException {
        Query q = _em.createQuery("select customer from Customer customer where customer.rowInfo.recordStatus='A' order by customer.id");
        List l = q.getResultList();
        return l;
    }

    public void updateCustomer(Customer customer) throws BusinessException {
        customer = (Customer) merge(customer);
    }

    public void logicalDeleteCustomer(Customer customer) throws BusinessException {
        customer.rowInfo.setRecordStatus("D");
        updateCustomer(customer);
    }

    public void addCustomer(Customer customer) throws BusinessException {
        System.out.println("just before persist in CustomerService");
        persist(customer);
        System.out.println("just after persist in CustomerService");
    }
}
