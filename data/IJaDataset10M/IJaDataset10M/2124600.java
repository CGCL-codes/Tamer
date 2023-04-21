package org.jboss.tutorial.relationships.bean;

import java.util.HashSet;
import java.util.Set;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 61136 $
 */
@Stateless
@Remote(EntityTest.class)
public class EntityTestBean implements EntityTest {

    @PersistenceContext
    private EntityManager manager;

    public void manyToManyCreate() throws Exception {
        Flight firstOne = new Flight();
        firstOne.setId(new Long(1));
        firstOne.setName("AF0101");
        manager.persist(firstOne);
        Flight second = new Flight();
        second.setId(new Long(2));
        second.setName("US1");
        Set<Customer> customers1 = new HashSet<Customer>();
        Set<Customer> customers2 = new HashSet<Customer>();
        Customer bill = new Customer();
        bill.setName("Bill");
        Address address = new Address();
        address.setStreet("Clarendon Street");
        address.setCity("Boston");
        address.setState("MA");
        address.setZip("02116");
        bill.setAddress(address);
        customers1.add(bill);
        Customer monica = new Customer();
        monica.setName("Monica");
        address = new Address();
        address.setStreet("Beach Street");
        address.setCity("Somerville");
        address.setState("MA");
        address.setZip("02116");
        monica.setAddress(address);
        customers1.add(monica);
        Customer molly = new Customer();
        molly.setName("Molly");
        address = new Address();
        address.setStreet("Main Street");
        address.setCity("Billerica");
        address.setState("MA");
        address.setZip("02116");
        customers2.add(molly);
        firstOne.setCustomers(customers1);
        second.setCustomers(customers2);
        manager.persist(second);
    }

    public Flight findFlightById(Long id) throws Exception {
        return manager.find(Flight.class, id);
    }
}
