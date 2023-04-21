package org.doclet.db4o;

import org.doclet.db4o.ObjectStoreTest;
import org.doclet.db4o.model.Pilot;
import org.junit.Test;
import com.db4o.ObjectSet;
import static junit.framework.Assert.*;

/**
 * We are going to demonstrate how to store, retrieve, update and delete
 * instances of a single class that only contains primitive and String members.
 */
public class BasicOperationsTest extends ObjectStoreTest {

    @Test
    public void test() {
        Pilot michael = new Pilot("Michael Schumacher", 100);
        container().set(michael);
        Pilot rubens = new Pilot("Rubens Barrichello", 99);
        container().set(rubens);
        ObjectSet set = container().get(new Pilot("Michael Schumacher", 0));
        assertEquals(1, set.size());
        assertEquals(michael, set.next());
        set = container().get(new Pilot(null, 99));
        assertEquals(1, set.size());
        assertEquals(rubens, set.next());
        assertEquals(2, container().get(Pilot.class).size());
        container().close();
        michael.setName("Michael clone");
        container().set(michael);
        assertEquals(3, container().get(Pilot.class).size());
        container().delete(michael);
        michael = (Pilot) container().get(new Pilot("Michael Schumacher", 0)).next();
        michael.addPoints(11);
        container().set(michael);
        assertEquals(2, container().get(Pilot.class).size());
        container().close();
        assertEquals(111, ((Pilot) container().get(new Pilot("Michael Schumacher", 0)).next()).getPoints());
        container().close();
        container().delete(michael);
        assertEquals(2, container().get(Pilot.class).size());
        container().delete(container().get(new Pilot("Michael Schumacher", 0)).next());
        assertEquals(1, container().get(Pilot.class).size());
        assertEquals("Rubens Barrichello", ((Pilot) container().get(Pilot.class).next()).getName());
    }
}
