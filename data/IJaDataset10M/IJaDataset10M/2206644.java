package test.lib.db.entity;

import static org.junit.Assert.*;
import lablog.lib.control.Controller;
import lablog.lib.db.entity.Directory;
import lablog.lib.db.entity.Group;
import lablog.lib.db.entity.Person;
import org.hibernate.LazyInitializationException;

public class DirectoryTest extends RestrictedTreeNodeTest<Directory> {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        existentId = 1L;
        inexistentId = Long.MAX_VALUE;
        notReadableId = 2L;
        notWritableId = 3L;
        eValid = new Directory("01 Valid Name");
        eValid.setOwner(new Person());
        eValid.setOwnerGroup(new Group());
        eWithNull = new Directory("03 Valid Name");
        eEqual01 = new Directory("02 Equal Name");
        eEqual02 = new Directory("02 Equal Name");
        entityClass = Directory.class;
    }

    @Override
    public void testFetch() {
        Directory dir = Controller.find(Directory.class, existentId);
        dir = Controller.fetch(dir, "parent");
        try {
            Directory p = dir.getParent();
            if (p != null) p.inspect();
        } catch (LazyInitializationException e) {
            fail("unexpected LazyInitializationException");
        }
    }

    @Override
    public void testFetchAll() {
        Directory dir = Controller.find(Directory.class, existentId);
        dir = Controller.fetchAll(dir);
        try {
            Directory p = dir.getParent();
            if (p != null) p.inspect();
        } catch (LazyInitializationException e) {
            fail("unexpected LazyInitializationException");
        }
    }

    @Override
    public void testSaveDelete() {
        eValid.setOwner(Controller.getConnection().getUser());
        eValid.setOwnerGroup(Controller.getConnection().getUserGroups()[0]);
        Controller.save(eValid);
        String stmt = "SELECT g FROM Directory g WHERE g.name = '" + eValid.getName() + "'";
        Directory gSaved = Controller.find(Directory.class, stmt).get(0);
        assertNotNull(gSaved.getId());
        assertEquals(gSaved, eValid);
        Controller.delete(gSaved);
        assertEquals(0, Controller.find(Directory.class, stmt).size());
    }
}
