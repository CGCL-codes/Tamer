package datadog.sessions;

import datadog.criterion.Criteria;
import datadog.criterion.CriteriaBuilder;
import datadog.exceptions.DatadogException;
import datadog.id.Identity;
import datadog.log.Log;
import datadog.log.LogFactory;
import datadog.services.ClassService;
import datadog.test.util.*;
import datadog.auction.model.Category;
import datadog.auction.model.Item;
import datadog.auction.model.MonetaryAmount;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Justin Tomich
 */
public class JdbcSessionTest extends TestCase {

    Log log = LogFactory.getLog(this.getClass());

    public void testCloneObjectWithComposite() throws DatadogException {
        SessionServer server = AuctionSetup.sessionServer();
        JdbcSession session = (JdbcSession) server.createSession();
        Item item = (Item) session.getObject(Item.class, null);
        MonetaryAmount initialPrice = item.getInitialPrice();
        assertNotNull(item);
        assertNotNull(initialPrice);
        Item itemClone = (Item) session.cloneObject(item);
        assertNotNull(itemClone);
        MonetaryAmount clonedInitialPrice = itemClone.getInitialPrice();
        assertNotNull(clonedInitialPrice);
        assertTrue(initialPrice == clonedInitialPrice);
    }

    public void testCachingOnRelational() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession session = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder().get("id").equalTo(1);
        Location l = (Location) session.getObject(Location.class, c);
        assertTrue(session.getCache().contains(l));
        assertTrue(session.getCache().contains(l.getHrac()));
    }

    public void testReEntrant() throws DatadogException {
        SessionServer server = AuctionSetup.sessionServer();
        JdbcSession session = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder().get("id").equalTo(1000);
        Category category = (Category) session.getObject(Category.class, c);
        Category parent = category.getParentCategory();
        assertNotNull(category);
        assertEquals(1, parent.getId().intValue());
        assertEquals(1000, category.getId().intValue());
    }

    public void testCacheClearing() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession session = (JdbcSession) server.createSession();
        Criteria tobyCriteria = new CriteriaBuilder().get("userId").equalTo(DbSetup.TOBERT_USER_ID);
        SimpleUser first = (SimpleUser) session.getObject(SimpleUser.class, tobyCriteria);
        SimpleUser second = (SimpleUser) session.getObject(SimpleUser.class, tobyCriteria);
        assertSame(first, second);
        session.clearCache();
        SimpleUser third = (SimpleUser) session.getObject(SimpleUser.class, tobyCriteria);
        assertNotSame(first, third);
    }

    public void testIsCachedInstance() {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        ClassService service = server.getService(LocationHrac.class);
        LocationHrac notCached = new LocationHrac();
        notCached.setType("ABC");
        LocationHrac cached = new LocationHrac();
        cached.setType("E");
        cached.setDescription("FOO DIDDLY");
        dbSession.getCache().update(cached);
        assertFalse(dbSession.getCache().contains(notCached));
        assertTrue(dbSession.getCache().contains(cached));
    }

    public void testDelete() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        LocationHrac hrac = new LocationHrac("D", "Toby Location");
        dbSession.saveObject(hrac);
        assertInCache(hrac, dbSession, server);
        dbSession.deleteObject(hrac);
        assertDeleted(hrac, dbSession);
    }

    public void testDeleteCollection() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        LocationHrac hracC = new LocationHrac("C", "Toby Location0");
        LocationHrac hracD = new LocationHrac("D", "Toby Location1");
        LocationHrac hracE = new LocationHrac("E", "Toby Location2");
        Collection hracs = new ArrayList();
        hracs.add(hracC);
        hracs.add(hracD);
        hracs.add(hracE);
        dbSession.saveCollection(hracs);
        assertInCache(hracC, dbSession, server);
        assertInCache(hracD, dbSession, server);
        assertInCache(hracE, dbSession, server);
        dbSession.deleteCollection(hracs);
        assertDeleted(hracC, dbSession);
        assertDeleted(hracD, dbSession);
        assertDeleted(hracE, dbSession);
    }

    private void assertDeleted(LocationHrac hrac, JdbcSession dbSession) throws DatadogException {
        String hracType = hrac.getType();
        Criteria c = new CriteriaBuilder().get("type").equalTo(hracType);
        LocationHrac nullHrac = (LocationHrac) dbSession.getObject(LocationHrac.class, c);
        assertNull(nullHrac);
        assertNotInCache(hrac, dbSession.getSessionServer(), dbSession);
    }

    public void testGetObjectWithOneToOne() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder().get("userId").equalTo("TOBERT");
        SimpleUser user1 = (SimpleUser) dbSession.getObject(SimpleUser.class, c);
        log.debug("SimpleUser user1=" + user1);
        assertNotNull("tried to load SimpleUser, but it's null", user1);
        assertNotNull("user has no location, 1to1 mapping failed", user1.getHomeLocation());
    }

    public void testGetObjectOneToMany() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder().get("userId").equalTo("TOBERT");
        ComplexUser user = (ComplexUser) dbSession.getObject(ComplexUser.class, c);
        log.debug("ComplexUser user1=" + user);
        assertNotNull("tried to load user1, but it's null", user);
    }

    /**
     * Tests that get works. Tests caching by running the same query twice, and
     * then checking to make sure that the same instance of SimpleUser is
     * returned both times.
     *
     * @throws DatadogException
     */
    public void testGet() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder().get("userId").equalTo("TOBERT");
        SimpleUser user1 = (SimpleUser) dbSession.getObject(SimpleUser.class, c);
        assertNotNull("tried to load user1, but it's null", user1);
        SimpleUser user2 = (SimpleUser) dbSession.getObject(SimpleUser.class, c);
        assertNotNull("tried to load user2, but it's null", user2);
        assertSame("user1 and user2 should be the same object", user1, user2);
        assertNotNull("userId is null, but shouldn't be", user1.getUserId());
        assertNotNull("homeLocation is null, but shouldn't be", user1.getHomeLocation());
    }

    public void testGetCollectionSelectAll() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder();
        Collection users = dbSession.getCollection(SimpleUser.class, c);
        log.debug("users=" + users);
        assertEquals("expecting 3 users total", 3, users.size());
    }

    public void testGetCollectionWithCriteria() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria getToby = new CriteriaBuilder().get("ownerName").equalTo("TOBERT");
        Collection tobyDogs = dbSession.getCollection(Dog.class, getToby);
        log.debug("toby dogs=" + tobyDogs);
        assertNotNull(tobyDogs);
        assertEquals(3, tobyDogs.size());
        Criteria getJt = new CriteriaBuilder().get("ownerName").equalTo("TOMICHJ");
        Collection jtDogs = dbSession.getCollection(Dog.class, getJt);
        log.debug("jt dogs=" + jtDogs);
        assertNotNull(jtDogs);
        assertEquals(2, jtDogs.size());
    }

    public void testSimpleSave() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        LocationHrac hrac = new LocationHrac("R", "Regional Compost");
        dbSession.saveObject(hrac);
        LocationHrac hracCopy = (LocationHrac) dbSession.getObject(LocationHrac.class, new CriteriaBuilder().get("type").equalTo("R"));
        assertNotNull(hracCopy);
        assertEquals(hrac, hracCopy);
        dbSession.deleteObject(hrac);
    }

    public void testCaching() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria criteria = new CriteriaBuilder().get("type").equalTo("R");
        LocationHrac nullHrac = (LocationHrac) dbSession.getObject(LocationHrac.class, criteria);
        assertNull(nullHrac);
        LocationHrac hrac = new LocationHrac("R", "Regional Compost");
        dbSession.saveObject(hrac);
        dbSession.commit();
        LocationHrac cachedHrac = (LocationHrac) dbSession.getObject(LocationHrac.class, criteria);
        assertNotNull(cachedHrac);
        assertSame(hrac, cachedHrac);
        dbSession.deleteObject(hrac);
    }

    public void testUpdateFromCache() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        ClassService service = server.getService(LocationHrac.class);
        String originalDescription = "Regional Compost";
        LocationHrac hrac = new LocationHrac("R", originalDescription);
        dbSession.saveObject(hrac);
        dbSession.commit();
        Criteria criteria = new CriteriaBuilder().get("type").equalTo("R");
        LocationHrac cachedHrac = (LocationHrac) dbSession.getObject(LocationHrac.class, criteria);
        assertNotNull(cachedHrac);
        assertSame(hrac, cachedHrac);
        String newDescription = "NEW VALUE";
        LocationHrac hracClone = (LocationHrac) service.cloneObject(hrac);
        hracClone.setDescription(newDescription);
        dbSession.saveObject(hracClone);
        dbSession.commit();
        nukeFromCache(hrac, server, dbSession);
        LocationHrac hracCopy2 = (LocationHrac) dbSession.getObject(LocationHrac.class, criteria);
        assertNotNull(hracCopy2);
        assertEquals(newDescription, hracCopy2.getDescription());
        dbSession.deleteObject(hracCopy2);
    }

    public void testUpdateWithMultiTableService() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder().get("userId").equalTo("TOBERT");
        SimpleUser u1 = (SimpleUser) dbSession.getObject(SimpleUser.class, c);
        assertNotNull(u1);
        assertNotNull(u1.getJobCategory());
        final String newCategory = "NEW Developizer";
        assertFalse(newCategory.equals(u1.getJobCategory()));
        SimpleUser u1Clone = (SimpleUser) dbSession.cloneObject(u1);
        u1Clone.setJobCategory(newCategory);
        dbSession.saveObject(u1Clone);
        ClassService service = server.getService(SimpleUser.class);
        Identity idU1 = service.identify(u1);
        dbSession.getCache().remove(idU1);
        SimpleUser u2 = (SimpleUser) dbSession.getObject(SimpleUser.class, c);
        assertEquals(newCategory, u2.getJobCategory());
        Criteria c3 = new CriteriaBuilder().get("userId").equalTo("BOB");
        SimpleUser u3 = (SimpleUser) dbSession.getObject(SimpleUser.class, c3);
        assertFalse(newCategory.equals(u3.getJobCategory()));
    }

    public void testSelectWithDeepJoinTables() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        String type = "A";
        Criteria c = new CriteriaBuilder().get("type").equalTo(type);
        PotatoAndGravy pag = (PotatoAndGravy) dbSession.getObject(PotatoAndGravy.class, c);
        assertNotNull(pag);
        assertEquals(type, pag.getType());
    }

    public void testUpdateWithDeepJoinTables() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        Criteria c = new CriteriaBuilder().get("type").equalTo("A");
        PotatoAndGravy pag = (PotatoAndGravy) dbSession.getObject(PotatoAndGravy.class, c);
        PotatoAndGravy pagClone = (PotatoAndGravy) dbSession.cloneObject(pag);
        String updatedGravyAnimalDesc = "Ch-Ch-Chicken";
        pagClone.setGravyAnimalDescription(updatedGravyAnimalDesc);
        dbSession.saveObject(pagClone);
        dbSession.commit();
        ClassService service = server.getService(PotatoAndGravy.class);
        Identity pagId = service.identify(pag);
        dbSession.getCache().remove(pagId);
        PotatoAndGravy pagCopy = (PotatoAndGravy) dbSession.getObject(PotatoAndGravy.class, c);
        assertNotNull(pagCopy);
        assertEquals(updatedGravyAnimalDesc, pagCopy.getGravyAnimalDescription());
    }

    public void testSaveRelationalIntegrity() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        LocationHrac hrac = new LocationHrac("L", "Lame Place");
        Location location = new Location(100, "R", hrac);
        try {
            dbSession.saveObject(location);
            fail("should have failed with an integrity violation!");
        } catch (DatadogException de) {
            ;
        }
        try {
            dbSession.saveObject(hrac);
            dbSession.saveObject(location);
        } catch (DatadogException de) {
            fail("saves should have worked! crap!");
        }
    }

    public void testDeepSave() throws DatadogException {
        SessionServer server = TrivialSetup.sessionServer();
        JdbcSession dbSession = (JdbcSession) server.createSession();
        LocationHrac hrac = (LocationHrac) dbSession.getObject(LocationHrac.class, new CriteriaBuilder().get("type").equalTo("X"));
        assertEquals("X", hrac.getType());
        int locationId = 123;
        Location loc = new Location(locationId, "P", hrac);
        dbSession.saveObject(loc);
        String userId = "MEL";
        SimpleUser user = new SimpleUser(userId, "123mcn", "H", "Dumb Job", 572121543, loc);
        dbSession.saveObject(user);
        dbSession.commit();
        nukeFromCache(user, server, dbSession);
        SimpleUser loadedUser = (SimpleUser) dbSession.getObject(SimpleUser.class, new CriteriaBuilder().get("userId").equalTo(userId));
        log.debug("savedUser:" + user);
        log.debug("loadedUser:" + loadedUser);
        assertNotNull(loadedUser);
        assertEquals(user, loadedUser);
        assertEquals(loadedUser.getHomeLocation().getId(), locationId);
    }

    private void assertInCache(Object instance, JdbcSession session, SessionServer server) {
        ClassService service = server.getService(instance.getClass());
        Identity pk = service.identify(instance);
        assertTrue("instance is not cached! should be! instance=" + instance, session.isCached(pk));
    }

    private void assertNotInCache(Object instance, SessionServer server, Session session) {
        ClassService service = server.getService(instance.getClass());
        Identity pk = service.identify(instance);
        assertFalse("instance is cached! instance=" + instance, session.isCached(pk));
    }

    private void nukeFromCache(Object instance, SessionServer server, JdbcSession dbSession) {
        ClassService service = server.getService(instance.getClass());
        Identity id = service.identify(instance);
        dbSession.getCache().remove(id);
    }
}
