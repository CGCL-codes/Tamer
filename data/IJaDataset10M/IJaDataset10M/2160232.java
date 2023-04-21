package de.wieger.domaindriven.unitofwork;

import static org.junit.Assert.*;
import org.junit.Test;
import de.wieger.domaindriven.domain.test.Bar;
import de.wieger.domaindriven.domain.test.Foo;
import de.wieger.domaindriven.domain.test.Person;
import de.wieger.domaindriven.unitofwork.UnitOfWork;

public class TestUnitOfWork {

    private static final String ALFRED = "Alfred E. Neumann";

    private static final String HARALD = "Harald Schmidt";

    @Test
    public void testSnapshot() {
        Person entity = new Person();
        entity.setFirstName("Thomas");
        UnitOfWork uow = new UnitOfWork();
        assertFalse(uow.getChangedEntities().contains(entity));
        entity.setFirstName("Thomas E.");
        assertTrue(uow.hasChanged(entity));
        uow.rollback();
        assertEquals("Thomas", entity.getFirstName());
    }

    /**
     * tests that {@link UnitOfWork#rollback} reverts the
     * state of a managed object
     */
    @Test
    public void testRollback() {
        Foo foo = new Foo();
        foo.setName(ALFRED);
        UnitOfWork uow = new UnitOfWork();
        foo.setName(HARALD);
        uow.rollback();
        assertEquals(ALFRED, foo.getName());
    }

    /**
     * tests that {@link UnitOfWork#rollback} reverts the
     * state of a managed object
     */
    @Test
    public void testCommit() {
        Foo foo = new Foo();
        foo.setName(ALFRED);
        UnitOfWork uow = new UnitOfWork();
        foo.setName(HARALD);
        uow.commit();
        assertEquals(HARALD, foo.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testCommitTwiceFails() {
        UnitOfWork uow = new UnitOfWork();
        uow.commit();
        uow.commit();
    }

    @Test(expected = IllegalStateException.class)
    public void testCommitOrder() {
        UnitOfWork uow = new UnitOfWork();
        new UnitOfWork();
        uow.commit();
    }

    @Test
    public void testNestedUnitsOfWorkRollback() {
        Foo foo = new Foo();
        foo.setName("Thomas");
        UnitOfWork uow = new UnitOfWork();
        foo.setName("Thomas E.");
        UnitOfWork innerUow = new UnitOfWork();
        foo.setName("Thomas Erik");
        innerUow.rollback();
        assertEquals("Thomas E.", foo.getName());
        uow.rollback();
        assertEquals("Thomas", foo.getName());
    }

    @Test
    public void testNestedUnitsOfWorkInnerCommitOuterRollback() {
        Foo foo = new Foo();
        foo.setName("Thomas");
        Foo secondFoo = new Foo();
        secondFoo.setName("Nummer Zwo");
        UnitOfWork uow = new UnitOfWork();
        foo.setName("Thomas E.");
        UnitOfWork innerUow = new UnitOfWork();
        foo.setName("Thomas Erik");
        secondFoo.setName("Numero 2");
        innerUow.commit();
        assertEquals("Thomas Erik", foo.getName());
        assertEquals("Numero 2", secondFoo.getName());
        assertTrue(uow.hasChanged(foo));
        assertTrue(uow.hasChanged(secondFoo));
        uow.rollback();
        assertEquals("Thomas", foo.getName());
        assertEquals("Nummer Zwo", secondFoo.getName());
    }

    /**
     * tests that {@link UnitOfWork#rollback} rolls back
     * collections of managed objects
     */
    @Test
    public void testCollectionRollback() {
        Foo alfred = new Foo();
        Bar bar = getFooFixture(alfred);
        UnitOfWork uow = new UnitOfWork();
        Foo harald = new Foo();
        bar.addToFooSet(harald);
        bar.addToFooList(harald);
        uow.rollback();
        assertEquals(1, bar.getFooSetSize());
        assertTrue(bar.fooSetContains(alfred));
        assertEquals(1, bar.getFooListSize());
        assertTrue(bar.fooListContains(alfred));
    }

    @Test
    public void testTransitiveManagement() {
        Bar sonderbar = new Bar();
        Foo alfred = new Foo();
        alfred.setName("Alfred E.");
        sonderbar.addToFooList(alfred);
        UnitOfWork uow = new UnitOfWork();
        alfred.setName("Alfred");
        uow.rollback();
        assertEquals("Alfred E.", alfred.getName());
    }

    /**
     * @param pFoo
     * @return a new Bar with references set to pFoo
     */
    private Bar getFooFixture(Foo pFoo) {
        Bar bar = new Bar();
        bar.addToFooSet(pFoo);
        bar.addToFooList(pFoo);
        bar.setFoo(pFoo);
        return bar;
    }
}
