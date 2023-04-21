package datadog.sessions;

import datadog.auction.model.Category;
import datadog.caches.object.SessionCacheManager;
import datadog.changesets.ChangeRecord;
import datadog.changesets.SaveChangeRecord;
import datadog.criterion.Criteria;
import datadog.criterion.CriteriaBuilder;
import datadog.criterion.Query;
import datadog.exceptions.DatadogException;
import datadog.id.Identity;
import datadog.services.ClassService;
import datadog.test.util.*;
import junit.framework.TestCase;

/**
 * @author Justin Tomich
 */
public class JdbcTransactionTest extends TestCase {

    public void testSimpleDelete() throws DatadogException {
        SessionServer server = AuctionSetup.sessionServer();
        Session session = server.createSession();
        JdbcTransaction tran = (JdbcTransaction) session.createTransaction();
        Criteria c = new Query().get("id").equalTo(1001);
        Object category = tran.getObject(Category.class, c);
        assertNotNull(category);
        tran.deleteObject(category);
        tran.commit();
        ClassService service = server.getService(Category.class);
        boolean cached = session.isCached(service.identify(category));
        assertFalse(cached);
        Object shouldBeNull = tran.getObject(Category.class, c);
        assertNull(shouldBeNull);
    }

    public void testDeepRegister() throws DatadogException {
        SessionServer server = AuctionSetup.sessionServer();
        Session session = server.createSession();
        JdbcTransaction tran = (JdbcTransaction) session.createTransaction();
        Category c1 = new Category(101, "Papa");
        Category c2 = new Category(102, "Bill");
        Category c22 = new Category(103, "Bill Jr");
        Category c3 = new Category(104, "Timmy");
        Category c23 = new Category(105, "Bill III");
        c1.addChildCategory(c2);
        c1.addChildCategory(c3);
        c2.addChildCategory(c22);
        c22.addChildCategory(c23);
        tran.register(c1);
        tran.deepRegisterNew();
        assertEquals(5, tran.changeSet.size());
        assertTrue(tran.changeSet.contains(c1));
        assertTrue(tran.changeSet.contains(c2));
        assertTrue(tran.changeSet.contains(c22));
        assertTrue(tran.changeSet.contains(c3));
        assertTrue(tran.changeSet.contains(c23));
    }

    public void testOrdering() throws DatadogException {
        SessionServer server = AuctionSetup.sessionServer();
        Session session = server.createSession();
        Transaction tran = session.createTransaction();
        Category c1 = new Category(100, "Papa");
        Category c3 = new Category(102, "Bill");
        Category c32 = new Category(103, "Bill Jr");
        Category c4 = new Category(104, "Timmy");
        c1.addChildCategory(c3);
        c1.addChildCategory(c4);
        c3.addChildCategory(c32);
        tran.registerNew(c32);
        tran.registerNew(c3);
        tran.registerNew(c1);
        tran.registerNew(c4);
        tran.commit();
    }

    public void testRegisterNew() throws DatadogException {
        LocationHrac hrac = new LocationHrac("J", "Test LocationHrac");
        SessionServer server = TrivialSetup.sessionServer();
        ClassService service = server.getService(LocationHrac.class);
        JdbcTransaction transaction = (JdbcTransaction) server.createSession().createTransaction();
        transaction.registerNew(hrac);
        transaction.commit();
        Identity id = service.identify(hrac);
        assertTrue(transaction.isCached(id));
    }

    public void testDoubleRegister() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        SimpleUser queryUser = new SimpleUser(DbSetup.TOBERT_USER_ID, "foo", "G", "Good Job Category", 123456789, new Location());
        SimpleUser cachedUser = (SimpleUser) transaction.getObject(queryUser);
        try {
            transaction.register(cachedUser);
            fail("should have crapped!");
        } catch (TransactionAbortedException e) {
            ;
        }
    }

    public void testGetObject() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        String jobGrade = "G";
        SimpleUser newUser = new SimpleUser(DbSetup.TOBERT_USER_ID, "foo", jobGrade, "Good Job Category", 123456789, new Location());
        SimpleUser cachedUser = (SimpleUser) transaction.getObject(newUser);
        assertNotSame(newUser, cachedUser);
        assertFalse(cachedUser.getJobGrade().equals(jobGrade));
    }

    public void testGetObjectWithNull() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        SimpleUser user = new SimpleUser("NOT_IN_DB", "w", null, null, 1, null);
        Object shouldBeNull = transaction.getObject(user);
        assertNull(shouldBeNull);
    }

    public void testGetObjectChangeRecord() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcTransaction transaction = (JdbcTransaction) server.createSession().createTransaction();
        Criteria c = new CriteriaBuilder().get("userId").equalTo(DbSetup.TOBERT_USER_ID);
        SimpleUser user = (SimpleUser) transaction.getObject(SimpleUser.class, c);
        assertNotNull(user);
        ChangeRecord record = transaction.changeSet.lookup(user);
        assertNotNull(record);
        assertSame(user, ((SaveChangeRecord) record).getWorking());
    }

    public void testGetObjectFromCache() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Criteria c = new CriteriaBuilder().get("userId").equalTo(DbSetup.TOBERT_USER_ID);
        Session s = server.createSession();
        SimpleUser existingUser = (SimpleUser) s.getObject(SimpleUser.class, c);
        assertNotNull(existingUser);
        SimpleUser newUser = new SimpleUser(DbSetup.TOBERT_USER_ID, "foo", "G", "Good Job Category", 123456789, new Location());
        Transaction transaction = s.createTransaction();
        SimpleUser cachedUser = (SimpleUser) transaction.getObject(newUser);
        assertEquals(existingUser, cachedUser);
    }

    public void testRegisterCloneNoRelational() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        LocationHrac hrac = new LocationHrac("A", "Test LocationHrac");
        LocationHrac hracClone = (LocationHrac) transaction.register(hrac);
        assertEqualNotSame(hrac, hracClone);
    }

    public void testRegisterWithExisting() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        Criteria c = new CriteriaBuilder().get("userId").equalTo(DbSetup.TOBERT_USER_ID);
        SimpleUser existingUser = (SimpleUser) transaction.getObject(SimpleUser.class, c);
        assertNotNull(existingUser);
        Location l = (Location) transaction.getObject(Location.class, null);
        assertNotNull(l);
        SimpleUser newUser = new SimpleUser(DbSetup.TOBERT_USER_ID, "foo", "G", "Good Job Category", 123456789, l);
        try {
            transaction.register(newUser);
            fail("should have hucked! user already exists!");
        } catch (TransactionAbortedException tae) {
        }
    }

    public void testRegisterAssumeExisting() {
    }

    public void testRegisterExisting() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        ClassService service = server.getService(SimpleUser.class);
        Session session = server.createSession();
        Criteria tobyCriteria = new CriteriaBuilder().get("userId").equalTo(DbSetup.TOBERT_USER_ID);
        SimpleUser existing = (SimpleUser) session.getObject(SimpleUser.class, tobyCriteria);
        String jobCategory = "feckin a";
        existing.setJobCategory(jobCategory);
        JdbcTransaction transaction = (JdbcTransaction) server.createSession().createTransaction();
        Identity id = service.identify(existing);
        assertTrue(transaction.isCached(id));
        SessionCacheManager cache = (SessionCacheManager) transaction.getCache();
        cache.clearCache();
        assertFalse(transaction.isCached(id));
        transaction.registerExisting(existing);
        assertFalse(transaction.parent.isCached(service.identify(existing)));
        transaction.commit();
        assertTrue(transaction.parent.isCached(service.identify(existing)));
        session = server.createSession();
        SimpleUser updated = (SimpleUser) session.getObject(SimpleUser.class, tobyCriteria);
        assertTrue(jobCategory.equals(updated.getJobCategory()));
    }

    public void testSimpleRelease() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        String originalDescription = "Test LocationHrac";
        LocationHrac hrac = new LocationHrac("A", originalDescription);
        LocationHrac hracClone = (LocationHrac) transaction.register(hrac);
        hracClone.setDescription("Updated Description.");
        assertEquals(originalDescription, hrac.getDescription());
        transaction.release();
        assertEquals(originalDescription, hrac.getDescription());
    }

    public void testSimpleCommit() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        LocationHrac hrac = new LocationHrac("A", "Test LocationHrac");
        LocationHrac hracClone = (LocationHrac) transaction.register(hrac);
        String updated = "Updated Description.";
        hracClone.setDescription(updated);
        assertFalse(updated.equals(hrac.getDescription()));
        transaction.commit();
        assertTrue(updated.equals(hrac.getDescription()));
    }

    private void assertEqualNotSame(LocationHrac first, LocationHrac second) {
        assertNotSame(first, second);
        assertEquals(first, second);
    }

    public void testRegisterCloneWithRelational() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Transaction transaction = server.createSession().createTransaction();
        LocationHrac hrac = new LocationHrac("A", "Test LocationHrac");
        Location loc = new Location(12, "Some Location", hrac);
        Location locClone = (Location) transaction.register(loc);
        assertSame(hrac, locClone.getHrac());
        assertEquals(loc.getId(), locClone.getId());
        assertEquals(loc.getType(), locClone.getType());
    }

    public void testCachedCopyUpdated() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Session session = server.createSession();
        Transaction transaction = session.createTransaction();
        String updatedDescription = "NEW AND SPECIAL UPDATED";
        Criteria c = new CriteriaBuilder();
        LocationHrac hrac = (LocationHrac) session.getObject(LocationHrac.class, c);
        assertNotNull(hrac);
        assertFalse(updatedDescription.equals(hrac.getDescription()));
        LocationHrac hracClone = (LocationHrac) transaction.register(hrac);
        hracClone.setDescription(updatedDescription);
        transaction.commit();
        assertEquals(updatedDescription, hrac.getDescription());
    }

    public void testCommit() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        Session session = server.createSession();
        Transaction transaction = session.createTransaction();
        Criteria getB = new CriteriaBuilder().get("type").equalTo("B");
        LocationHrac before = (LocationHrac) session.getObject(LocationHrac.class, getB);
        assertNull(before);
        LocationHrac hrac = new LocationHrac("B", "Transaction Test");
        transaction.register(hrac);
        transaction.commit();
        LocationHrac after = (LocationHrac) session.getObject(LocationHrac.class, getB);
        assertNotNull(after);
        assertEquals(hrac, after);
    }
}
