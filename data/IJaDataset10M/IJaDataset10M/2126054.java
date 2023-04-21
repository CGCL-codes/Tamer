package de.linwave.junit.inheritance;

import org.odbms.FUNCTION;
import org.odbms.OP;
import org.odbms.ObjectSet;
import org.odbms.Query;
import de.linwave.junit.AbstractTestCase;

public class InheritanceQuery extends AbstractTestCase {

    static int PERSONS = 30;

    /**
	 * Find all Persons (10 Persons, 10 Customer, 10 GoldCustomers)
	 */
    public void testAll() {
        int cnt = 0;
        ObjectSet<Person> items = db.query(Person.class).execute();
        for (Person p : items) {
            cnt++;
        }
        assertEquals(PERSONS, cnt);
    }

    /**
	 * Find Person 'joeckel'
	 */
    public void testPersonJoeckel() {
        int cnt = 0;
        Query query = db.query(Person.class);
        query.constrain("lastName", OP.EQUALS, FUNCTION.TO_UPPER, "joeckel");
        ObjectSet<Person> joeckels = query.execute();
        for (Person p : joeckels) {
            if (!"Lothar".equals(p.getFirstName()) || !"Joeckel".equals(p.getLastName())) fail("Invalid data in firstName || lastName");
            cnt++;
        }
        assertEquals(PERSONS / 3, cnt);
    }

    /**
	 * Find all customers (Customer extends Person)
	 */
    public void testCustomers() {
        int cnt = 0;
        ObjectSet<Customer> customers = db.query(Customer.class).execute();
        for (Customer p : customers) {
            if ("Lothar".equals(p.getFirstName()) || "Joeckel".equals(p.getLastName())) fail("Invalid data in firstName || lastName");
            cnt++;
        }
        assertEquals(20, cnt);
    }

    /**
	 * Find all customers with company=CUSTOMER (Customer extends Person)
	 */
    public void testCustomerWithName() {
        int cnt = 0;
        Query query = db.query(Customer.class);
        query.constrain("companyName", OP.EQUALS, FUNCTION.TO_UPPER, "MI6");
        ObjectSet<Customer> customers = query.execute();
        for (Customer p : customers) {
            if (!"GOLD4712".equals(p.getCustNr()) || !"MI6".equals(p.getCompanyName())) fail("Invalid data in custNr || companyName");
            cnt++;
        }
        assertEquals(PERSONS / 3, cnt);
    }

    /**
	 * Find all GOLD customers (GoldCustomer extends Customer extends Person)
	 */
    public void testGoldCustomer() {
        int cnt = 0;
        ObjectSet<GoldCustomer> goldCustomers = db.query(GoldCustomer.class).execute();
        for (GoldCustomer p : goldCustomers) {
            if (!p.isAmericanExpress() || p.isMasterCard() || !p.isVisa()) fail("Invalid boolean for AMEX,MC or VISA");
            cnt++;
        }
        assertEquals(PERSONS / 3, cnt);
    }

    public static void main(String[] args) {
        InheritanceQuery test = new InheritanceQuery();
        test.testPersonJoeckel();
        test.testCustomerWithName();
        test.testGoldCustomer();
    }
}
