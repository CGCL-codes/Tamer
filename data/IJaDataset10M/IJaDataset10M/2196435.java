package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import data.entities.Entity;
import data.entities.EntityContainer;
import data.Role;

/**
 * Classe di test per EntityContainer
 * @author Simona
 * @version 1.0
 */
public class EntityTest {

    private EntityContainer container;

    private Entity entity;

    private Entity e;

    @Before
    public void initialSetup() {
        container = new EntityContainer();
        entity = new Entity("Linus", "localhost", Role.NOT_YET_ASSIGNED);
        e = new Entity("Ciccio", "192.168.1.100", Role.FIRST_CONTROLLER);
        container.addEntity(entity);
        container.addEntity(e);
    }

    @Test
    public void testAddEntity() {
        assertEquals(2, container.size());
    }

    @Test
    public void testGetEntityString() {
        assertEquals(entity, container.getEntity("localhost"));
        assertEquals(e, container.getEntity("192.168.1.100"));
    }

    @Test
    public void testGetEntityRole() {
        assertEquals(entity, container.getEntity(Role.NOT_YET_ASSIGNED));
        assertEquals(e, container.getEntity(Role.FIRST_CONTROLLER));
    }

    @Test
    public void testIsEmpty() {
        container.removeEntity(entity);
        container.removeEntity(e);
        assertTrue(container.isEmpty());
    }

    @Test
    public void testIsEntityPresentEntity() {
        assertTrue(container.isEntityPresent(e));
        assertTrue(container.isEntityPresent(entity));
    }

    @Test
    public void testIsEntityPresentString() {
        assertTrue(container.isEntityPresent("192.168.1.100"));
        assertTrue(container.isEntityPresent("localhost"));
    }

    @Test
    public void testHasRoleUndefined() {
        assertTrue(container.hasRoleUndefined(entity));
    }

    @Test
    public void testUpdateRole() {
        container.updateRole(entity, Role.MASTER);
        assertFalse(container.hasRoleUndefined(entity));
    }
}
