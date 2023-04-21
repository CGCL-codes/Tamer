package woko.examples;

import org.hibernate.Session;
import woko.examples.library.Library;
import net.sf.woko.util.testing.WokoTestCase;

/**
 * Very basic CRUD unit test
 */
public class MyCrudTest extends WokoTestCase {

    public MyCrudTest() {
        openAndCloseSession = true;
    }

    public void testCrud() {
        Library l = new Library();
        l.setTitle("this is a test");
        Session s = getSession();
        s.save(l);
        s.flush();
        getPersistenceUtil().commit();
        Long id = l.getId();
        assertNotNull("id has not been set", id);
        closeSession();
        openSessionAndTx();
        l = (Library) getSession().load(Library.class, id);
        assertNotNull("could not retrieve entity", l);
        assertEquals("unexpected property value", "this is a test", l.getTitle());
        closeSession();
        openSessionAndTx();
        l = (Library) getSession().load(Library.class, id);
        l.setTitle("another test");
        getSession().update(l);
        getSession().flush();
        getPersistenceUtil().commit();
        closeSession();
        openSessionAndTx();
        l = (Library) getSession().load(Library.class, id);
        assertEquals("unexpected property value", "another test", l.getTitle());
        closeSession();
        openSessionAndTx();
        getSession().delete(l);
        getSession().flush();
        getPersistenceUtil().commit();
        closeSession();
        openSessionAndTx();
        l = null;
        l = (Library) getSession().get(Library.class, id);
        assertNull("load returned a deleted entity", l);
    }
}
