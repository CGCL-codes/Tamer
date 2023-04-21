package org.datanucleus.tests.directory.attribute_unidir;

import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.datanucleus.tests.JDOPersistenceTestCase;

public class OneManyTest extends JDOPersistenceTestCase {

    public OneManyTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        clean(Account.class);
        clean(Person.class);
        clean(Department.class);
    }

    protected void tearDown() throws Exception {
        clean(Account.class);
        clean(Person.class);
        clean(Department.class);
        super.tearDown();
    }

    public void testPersistWithoutRef() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Department randd = new Department("R&D");
            pm.makePersistent(randd);
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            pm.makePersistent(daffyDuck);
            Account dduck = new Account("dduck", "secret1");
            pm.makePersistent(dduck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNull(daffyDuck.getAddress());
            assertNull(daffyDuck.getComputer());
            assertNotNull(daffyDuck.getAccounts());
            assertTrue(daffyDuck.getAccounts().isEmpty());
            randd = pm.getObjectById(Department.class, "R&D");
            assertEquals("R&D", randd.getName());
            assertNotNull(randd.getMembers());
            assertTrue(randd.getMembers().isEmpty());
            dduck = pm.getObjectById(Account.class, "dduck");
            assertEquals("dduck", dduck.getUid());
            assertEquals("secret1", dduck.getPassword());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    public void testPersistPersonWithRef() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Department randd = new Department("R&D");
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Account dduck = new Account("dduck", "secret1");
            randd.getMembers().add(daffyDuck);
            daffyDuck.getAccounts().add(dduck);
            pm.makePersistent(daffyDuck);
            pm.makePersistent(randd);
            pm.makePersistent(dduck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            dduck = pm.getObjectById(Account.class, "dduck");
            randd = pm.getObjectById(Department.class, "R&D");
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            assertEquals("Daffy", daffyDuck.getFirstName());
            assertEquals("Duck", daffyDuck.getLastName());
            assertNull(daffyDuck.getAddress());
            assertNull(daffyDuck.getComputer());
            assertNotNull(daffyDuck.getAccounts());
            assertEquals(1, daffyDuck.getAccounts().size());
            assertTrue(daffyDuck.getAccounts().contains(dduck));
            randd = pm.getObjectById(Department.class, "R&D");
            assertEquals("R&D", randd.getName());
            assertNotNull(randd.getMembers());
            assertEquals(1, randd.getMembers().size());
            assertTrue(randd.getMembers().contains(daffyDuck));
            dduck = pm.getObjectById(Account.class, "dduck");
            assertEquals("dduck", dduck.getUid());
            assertEquals("secret1", dduck.getPassword());
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Department-(1)------------------------(N)-Person
     * <ul>
     * <li>The Department class has a Collection<Person> members
     * <li>In LDAP the relation is stored at the Department side (attribute members, multi-valued)
     * </ul>
     */
    public void testOwnerAtReferencingSide() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Department randd = new Department("R&D");
            randd.getMembers().add(daffyDuck);
            pm.makePersistent(daffyDuck);
            pm.makePersistent(randd);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("R&D", randd.getName());
            assertNotNull(randd.getMembers());
            assertEquals(1, randd.getMembers().size());
            assertTrue(randd.getMembers().contains(daffyDuck));
            randd.getMembers().remove(daffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("R&D", randd.getName());
            assertNotNull(randd.getMembers());
            assertEquals(0, randd.getMembers().size());
            assertEquals("Daffy Duck", daffyDuck.getFullName());
            Person speedyGonzales = new Person("Speedy", "Gonzales", "Speedy Gonzales", null, null);
            randd.getMembers().add(daffyDuck);
            randd.getMembers().add(speedyGonzales);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
            assertEquals("R&D", randd.getName());
            assertNotNull(randd.getMembers());
            assertEquals(2, randd.getMembers().size());
            assertTrue(randd.getMembers().contains(daffyDuck));
            assertTrue(randd.getMembers().contains(speedyGonzales));
            randd.getMembers().remove(daffyDuck);
            Department sales = new Department("Sales");
            sales.getMembers().add(daffyDuck);
            pm.makePersistent(sales);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            sales = pm.getObjectById(Department.class, "Sales");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
            assertNotNull(randd.getMembers());
            assertEquals(1, randd.getMembers().size());
            assertTrue(randd.getMembers().contains(speedyGonzales));
            assertNotNull(sales.getMembers());
            assertEquals(1, sales.getMembers().size());
            assertTrue(sales.getMembers().contains(daffyDuck));
            pm.deletePersistent(randd);
            pm.deletePersistent(daffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            try {
                randd = pm.getObjectById(Department.class, "R&D");
                fail("Object 'R&D' should not exist any more!");
            } catch (JDOObjectNotFoundException e) {
            }
            try {
                daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
                fail("Object 'Daffy Duck' should not exist any more!");
            } catch (JDOObjectNotFoundException e) {
            }
            sales = pm.getObjectById(Department.class, "Sales");
            assertNotNull(sales.getMembers());
            assertEquals(0, sales.getMembers().size());
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Department-(1)------------------------(N)-Person
     * <ul>
     * <li>The Department class has a Collection<Person> members
     * <li>In LDAP the relation is stored at the Department side (attribute members, multi-valued)
     * </ul>
     */
    public void testOwnerAtReferencingSideDetached() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Department randd = new Department("R&D");
            randd.getMembers().add(daffyDuck);
            pm.makePersistent(daffyDuck);
            pm.makePersistent(randd);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().addGroup("members");
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            Department detachedRandd = pm.detachCopy(randd);
            Person detachedDaffyDuck = pm.detachCopy(daffyDuck);
            tx.commit();
            pm.close();
            assertEquals("R&D", detachedRandd.getName());
            assertNotNull(detachedRandd.getMembers());
            assertEquals(1, detachedRandd.getMembers().size());
            assertTrue(detachedRandd.getMembers().contains(detachedDaffyDuck));
            detachedRandd.getMembers().remove(detachedDaffyDuck);
            JDOHelper.makeDirty(detachedRandd, "members");
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(detachedRandd);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().addGroup("members");
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            detachedRandd = pm.detachCopy(randd);
            detachedDaffyDuck = pm.detachCopy(daffyDuck);
            tx.commit();
            pm.close();
            assertEquals("R&D", detachedRandd.getName());
            assertNotNull(detachedRandd.getMembers());
            assertEquals(0, detachedRandd.getMembers().size());
            assertEquals("Daffy Duck", detachedDaffyDuck.getFullName());
            Person speedyGonzales = new Person("Speedy", "Gonzales", "Speedy Gonzales", null, null);
            detachedRandd.getMembers().add(detachedDaffyDuck);
            detachedRandd.getMembers().add(speedyGonzales);
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(detachedRandd);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().addGroup("members");
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
            detachedRandd = pm.detachCopy(randd);
            detachedDaffyDuck = pm.detachCopy(daffyDuck);
            Person detachedSpeedyGonzales = pm.detachCopy(speedyGonzales);
            tx.commit();
            pm.close();
            assertEquals("R&D", detachedRandd.getName());
            assertNotNull(detachedRandd.getMembers());
            assertEquals(2, detachedRandd.getMembers().size());
            assertTrue(detachedRandd.getMembers().contains(detachedDaffyDuck));
            assertTrue(detachedRandd.getMembers().contains(detachedSpeedyGonzales));
            Department sales = new Department("Sales");
            detachedRandd.getMembers().remove(detachedDaffyDuck);
            sales.getMembers().add(detachedDaffyDuck);
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(detachedRandd);
            pm.makePersistent(sales);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().addGroup("members");
            tx = pm.currentTransaction();
            tx.begin();
            randd = pm.getObjectById(Department.class, "R&D");
            sales = pm.getObjectById(Department.class, "Sales");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
            detachedRandd = pm.detachCopy(randd);
            Department detachedSales = pm.detachCopy(sales);
            detachedDaffyDuck = pm.detachCopy(daffyDuck);
            detachedSpeedyGonzales = pm.detachCopy(speedyGonzales);
            tx.commit();
            pm.close();
            assertNotNull(detachedRandd.getMembers());
            assertEquals(1, detachedRandd.getMembers().size());
            assertTrue(detachedRandd.getMembers().contains(detachedSpeedyGonzales));
            assertNotNull(detachedSales.getMembers());
            assertEquals(1, detachedSales.getMembers().size());
            assertTrue(detachedSales.getMembers().contains(detachedDaffyDuck));
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Person-(1)----------------------------(N)-Account
     * <ul>
     * <li>The Person class has a Collection<Account> accounts
     * <li>In LDAP the relation is stored at the *Account* side (attribute seeAlso, single-valued)
     * </ul>
     */
    public void testOwnerAtReferencedSide() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Account dduck = new Account("dduck", "secret1");
            daffyDuck.getAccounts().add(dduck);
            pm.makePersistent(daffyDuck);
            pm.makePersistent(dduck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            dduck = pm.getObjectById(Account.class, "dduck");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertEquals("dduck", dduck.getUid());
            assertNotNull(daffyDuck.getAccounts());
            assertEquals(1, daffyDuck.getAccounts().size());
            assertTrue(daffyDuck.getAccounts().contains(dduck));
            daffyDuck.getAccounts().remove(dduck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            assertNotNull(daffyDuck.getAccounts());
            assertEquals(0, daffyDuck.getAccounts().size());
            try {
                dduck = pm.getObjectById(Account.class, "dduck");
                fail("Object 'dduck' should not exist any more!");
            } catch (JDOObjectNotFoundException e) {
            }
            Account dduck2 = new Account("dduck2", "secret2");
            Account sgonzales2 = new Account("sgonzales2", "secret22");
            daffyDuck.getAccounts().add(dduck2);
            daffyDuck.getAccounts().add(sgonzales2);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            dduck2 = pm.getObjectById(Account.class, "dduck2");
            sgonzales2 = pm.getObjectById(Account.class, "sgonzales2");
            assertNotNull(daffyDuck.getAccounts());
            assertEquals(2, daffyDuck.getAccounts().size());
            assertTrue(daffyDuck.getAccounts().contains(dduck2));
            assertTrue(daffyDuck.getAccounts().contains(sgonzales2));
            Person speedyGonzales = new Person("Speedy", "Gonzales", "Speedy Gonzales", null, null);
            speedyGonzales.getAccounts().add(sgonzales2);
            daffyDuck.getAccounts().remove(sgonzales2);
            pm.makePersistent(speedyGonzales);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
            dduck2 = pm.getObjectById(Account.class, "dduck2");
            sgonzales2 = pm.getObjectById(Account.class, "sgonzales2");
            assertNotNull(daffyDuck.getAccounts());
            assertEquals(1, daffyDuck.getAccounts().size());
            assertTrue(daffyDuck.getAccounts().contains(dduck2));
            assertNotNull(speedyGonzales.getAccounts());
            assertEquals(1, speedyGonzales.getAccounts().size());
            assertTrue(speedyGonzales.getAccounts().contains(sgonzales2));
            pm.deletePersistent(daffyDuck);
            pm.deletePersistent(sgonzales2);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
            assertNotNull(speedyGonzales.getAccounts());
            assertEquals(0, speedyGonzales.getAccounts().size());
            try {
                daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
                fail("Object 'Daffy Duck' should not exist any more!");
            } catch (JDOObjectNotFoundException e) {
            }
            try {
                dduck = pm.getObjectById(Account.class, "sgonzales2");
                fail("Object 'sgonzales2' should not exist any more!");
            } catch (JDOObjectNotFoundException e) {
            }
            try {
                dduck = pm.getObjectById(Account.class, "dduck");
                fail("Object 'dduck' should not exist any more!");
            } catch (JDOObjectNotFoundException e) {
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Person-(1)----------------------------(N)-Account
     * <ul>
     * <li>The Person class has a Collection<Account> accounts
     * <li>In LDAP the relation is stored at the *Account* side (attribute seeAlso, single-valued)
     * </ul>
     */
    public void testOwnerAtReferencedSideDetached() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Person daffyDuck = new Person("Daffy", "Duck", "Daffy Duck", null, null);
            Account dduck = new Account("dduck", "secret1");
            daffyDuck.getAccounts().add(dduck);
            pm.makePersistent(daffyDuck);
            pm.makePersistent(dduck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().setGroup(FetchPlan.ALL);
            tx = pm.currentTransaction();
            tx.begin();
            dduck = pm.getObjectById(Account.class, "dduck");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            Account detachedDduck = pm.detachCopy(dduck);
            Person detachedDaffyDuck = pm.detachCopy(daffyDuck);
            tx.commit();
            pm.close();
            assertEquals("dduck", detachedDduck.getUid());
            assertNotNull(detachedDaffyDuck.getAccounts());
            assertEquals(1, detachedDaffyDuck.getAccounts().size());
            assertTrue(detachedDaffyDuck.getAccounts().contains(detachedDduck));
            detachedDaffyDuck.getAccounts().remove(detachedDduck);
            JDOHelper.makeDirty(detachedDaffyDuck, "accounts");
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().addGroup(FetchPlan.ALL);
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(detachedDaffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().setGroup(FetchPlan.ALL);
            tx = pm.currentTransaction();
            tx.begin();
            try {
                dduck = pm.getObjectById(Account.class, "dduck");
                fail("Object 'dduck' should not exist any more!");
            } catch (JDOObjectNotFoundException e) {
            }
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            detachedDaffyDuck = pm.detachCopy(daffyDuck);
            tx.commit();
            pm.close();
            assertNotNull(detachedDaffyDuck.getAccounts());
            assertEquals(0, detachedDaffyDuck.getAccounts().size());
            Account dduck2 = new Account("dduck2", "secret2");
            Account sgonzales2 = new Account("sgonzales2", "secret22");
            detachedDaffyDuck.getAccounts().add(dduck2);
            detachedDaffyDuck.getAccounts().add(sgonzales2);
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().addGroup(FetchPlan.ALL);
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(detachedDaffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().setGroup(FetchPlan.ALL);
            tx = pm.currentTransaction();
            tx.begin();
            dduck2 = pm.getObjectById(Account.class, "dduck2");
            sgonzales2 = pm.getObjectById(Account.class, "sgonzales2");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            Account detachedDduck2 = pm.detachCopy(dduck2);
            Account detachedSgonzales2 = pm.detachCopy(sgonzales2);
            detachedDaffyDuck = pm.detachCopy(daffyDuck);
            tx.commit();
            pm.close();
            assertNotNull(detachedDaffyDuck.getAccounts());
            assertEquals(2, detachedDaffyDuck.getAccounts().size());
            assertTrue(detachedDaffyDuck.getAccounts().contains(detachedDduck2));
            assertTrue(detachedDaffyDuck.getAccounts().contains(detachedSgonzales2));
            Person speedyGonzales = new Person("Speedy", "Gonzales", "Speedy Gonzales", null, null);
            speedyGonzales.getAccounts().add(detachedSgonzales2);
            detachedDaffyDuck.getAccounts().remove(detachedSgonzales2);
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().addGroup(FetchPlan.ALL);
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(speedyGonzales);
            pm.makePersistent(detachedDaffyDuck);
            tx.commit();
            pm.close();
            pm = pmf.getPersistenceManager();
            pm.getFetchPlan().setGroup(FetchPlan.ALL);
            tx = pm.currentTransaction();
            tx.begin();
            dduck2 = pm.getObjectById(Account.class, "dduck2");
            sgonzales2 = pm.getObjectById(Account.class, "sgonzales2");
            daffyDuck = pm.getObjectById(Person.class, "Daffy Duck");
            speedyGonzales = pm.getObjectById(Person.class, "Speedy Gonzales");
            detachedDduck2 = pm.detachCopy(dduck2);
            detachedSgonzales2 = pm.detachCopy(sgonzales2);
            detachedDaffyDuck = pm.detachCopy(daffyDuck);
            Person detachedSpeedyGonzales = pm.detachCopy(speedyGonzales);
            tx.commit();
            pm.close();
            assertNotNull(detachedDaffyDuck.getAccounts());
            assertEquals(1, detachedDaffyDuck.getAccounts().size());
            assertTrue(detachedDaffyDuck.getAccounts().contains(detachedDduck2));
            assertNotNull(detachedSpeedyGonzales.getAccounts());
            assertEquals(1, detachedSpeedyGonzales.getAccounts().size());
            assertTrue(detachedSpeedyGonzales.getAccounts().contains(detachedSgonzales2));
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
