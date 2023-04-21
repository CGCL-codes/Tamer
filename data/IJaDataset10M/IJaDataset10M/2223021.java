package org.vramework.test.objectmodel;

import java.util.Collection;
import java.util.List;
import org.vramework.commons.junit.VTestCase;

/**
 * @author thomas.mahringer
 *
 */
public class TestTestObjectModel extends VTestCase {

    public static void testTestObjectModel() {
        DatabaseMock dbMock = DatabaseMock.getMock();
        List<Customer> customers = dbMock.queryAllCustomersAsList();
        for (Customer c : customers) {
            log(c.getLastName() + ", " + c.getFirstName() + ", Advisor = " + c.getCustomerAdvisor());
            printCustomerContracts(c);
            printCustomerAddresses(c);
        }
    }

    protected static void printCustomerContracts(Customer c) {
        Collection<Contract> contracts = c.getContracts();
        log("  Contracts ----------------------------------");
        for (Contract contract : contracts) {
            log("  " + contract.toString());
        }
    }

    protected static void printCustomerAddresses(Customer c) {
        Collection<Address> addresses = c.getAddresses();
        log("  Addresses ----------------------------------");
        for (Address address : addresses) {
            log("  " + address.toString());
        }
    }
}
