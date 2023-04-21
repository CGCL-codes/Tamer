package org.datanucleus.tests.knownbugs;

import java.util.Collection;
import java.util.Iterator;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.datanucleus.tests.JDOPersistenceTestCase;
import org.jpox.samples.models.company.CompanyHelper;
import org.jpox.samples.models.company.Employee;
import org.jpox.samples.models.company.Manager;

/**
 * Series of tests for Attach/Detach functionality that are known bugs and expected to fail.
 */
public class AttachDetachTest extends JDOPersistenceTestCase {

    public AttachDetachTest(String name) {
        super(name);
    }

    /**
     * Test of detachCopyAll with fetch-depth.
     * See JIRA "NUCCORE-24"
     */
    public void testFetchDepthOnDetachCopyAll() {
        try {
            Manager bob = new Manager(1, "Bob", "Woodpecker", "bob@woodpecker.com", 13, "serial 1");
            Manager dave = new Manager(1, "Dave", "Woodpecker", "dave@woodpecker.com", 13, "serial 2");
            Employee mary = new Employee(1, "Mary", "Woodpecker", "mary@woodpecker.com", 13, "serial 3");
            mary.setManager(dave);
            dave.setManager(bob);
            PersistenceManager pm = pmf.getPersistenceManager();
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                pm.makePersistent(mary);
                tx.commit();
                tx.begin();
                pm.getFetchPlan().setGroup("groupA");
                Query query = pm.newQuery(pm.getExtent(Employee.class, true));
                query.setOrdering("firstName descending");
                Collection c = (Collection) query.execute();
                c = pm.detachCopyAll(c);
                tx.commit();
                assertEquals("c.size() == 3", c.size(), 3);
                for (Iterator i = c.iterator(); i.hasNext(); ) {
                    Employee em = (Employee) i.next();
                    try {
                        em.getManager();
                    } catch (Exception e) {
                        fail("Manager must be returned for maxFetchDepth of 1 : employee " + em.getFirstName() + " has no manager");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail(e.toString());
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
                pm.close();
            }
        } finally {
            CompanyHelper.clearCompanyData(pmf);
        }
    }
}
