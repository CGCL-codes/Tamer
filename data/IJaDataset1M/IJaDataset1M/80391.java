package org.norecess.nolatte.ast;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.support.IGroup;
import org.norecess.nolatte.support.NoLatteMockControl;

public class GroupOfStatementsTest {

    private static final Set<IIdentifier> GENERIC_SET = new HashSet<IIdentifier>();

    private NoLatteMockControl myControl;

    private IGroup<Statement> myGroup;

    private GroupOfStatements myGroupOfStatements;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        myControl = new NoLatteMockControl();
        myGroup = myControl.createMock(IGroup.class);
        myGroupOfStatements = new GroupOfStatements(myGroup);
    }

    public void replay() {
        myControl.replay();
    }

    public void verify() {
        myControl.verify();
    }

    @Test
    public void should_defer_add() {
        Statement statement = myControl.createMock(Statement.class);
        myGroup.add(statement);
        replay();
        myGroupOfStatements.add(statement);
        verify();
    }

    @Test
    public void should_defer_car() {
        Statement statement = myControl.createMock(Statement.class);
        expect(myGroup.car()).andReturn(statement);
        replay();
        assertSame(statement, myGroupOfStatements.car());
        verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_defer_cdr() {
        IGroup<Statement> cdr = myControl.createMock(IGroup.class);
        expect(myGroup.cdr()).andReturn(cdr);
        replay();
        assertEquals(new GroupOfStatements(cdr), myGroupOfStatements.cdr());
        verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_defer_cons() {
        Statement statement = myControl.createMock(Statement.class);
        IGroup<Statement> consed = myControl.createMock(IGroup.class);
        expect(myGroup.cons(statement)).andReturn(consed);
        replay();
        assertEquals(new GroupOfStatements(consed), myGroupOfStatements.cons(statement));
        verify();
    }

    @Test
    public void should_defer_get_length() {
        expect(myGroup.getLength()).andReturn(54);
        replay();
        assertEquals(54, myGroupOfStatements.getLength());
        verify();
    }

    @Test
    public void should_defer_is_empty_for_true() {
        expect(myGroup.isEmpty()).andReturn(true);
        replay();
        assertTrue(myGroupOfStatements.isEmpty());
        verify();
    }

    @Test
    public void should_defer_is_empty_for_false() {
        expect(myGroup.isEmpty()).andReturn(false);
        replay();
        assertFalse(myGroupOfStatements.isEmpty());
        verify();
    }

    @Test
    public void should_defer_nth() {
        Statement statement = myControl.createMock(Statement.class);
        expect(myGroup.nth(45)).andReturn(statement);
        replay();
        assertSame(statement, myGroupOfStatements.nth(45));
        verify();
    }

    @Test
    public void should_defer_rac() {
        Statement statement = myControl.createMock(Statement.class);
        expect(myGroup.rac()).andReturn(statement);
        replay();
        assertSame(statement, myGroupOfStatements.rac());
        verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_defer_rdc() {
        IGroup<Statement> rdc = myControl.createMock(IGroup.class);
        expect(myGroup.rdc()).andReturn(rdc);
        replay();
        assertEquals(new GroupOfStatements(rdc), myGroupOfStatements.rdc());
        verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_defer_snoc() {
        Statement statement = myControl.createMock(Statement.class);
        IGroup<Statement> snoced = myControl.createMock(IGroup.class);
        expect(myGroup.snoc(statement)).andReturn(snoced);
        replay();
        assertEquals(new GroupOfStatements(snoced), myGroupOfStatements.snoc(statement));
        verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_defer_subgroup_from() {
        IGroup<Statement> subgroup = myControl.createMock(IGroup.class);
        expect(myGroup.subGroup(99)).andReturn(subgroup);
        replay();
        assertEquals(new GroupOfStatements(subgroup), myGroupOfStatements.subGroup(99));
        verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_defer_subgroup_from_and_to() {
        IGroup<Statement> subgroup = myControl.createMock(IGroup.class);
        expect(myGroup.subGroup(123, 456)).andReturn(subgroup);
        replay();
        assertEquals(new GroupOfStatements(subgroup), myGroupOfStatements.subGroup(123, 456));
        verify();
    }

    @Test
    public void should_defer_lookup() {
        IIdentifier identifier = myControl.createMock(IIdentifier.class);
        Statement statement = myControl.createMock(Statement.class);
        expect(myGroup.lookup(identifier)).andReturn(statement);
        replay();
        assertSame(statement, myGroupOfStatements.lookup(identifier));
        verify();
    }

    @Test
    public void should_defer_hash() {
        IIdentifier identifier = myControl.createMock(IIdentifier.class);
        Statement statement = myControl.createMock(Statement.class);
        myGroup.hash(identifier, statement);
        replay();
        assertSame(myGroupOfStatements, myGroupOfStatements.hash(identifier, statement));
        verify();
    }

    @Test
    public void should_defer_getHashKeys() {
        expect(myGroup.getHashKeys()).andReturn(GENERIC_SET);
        replay();
        assertSame(GENERIC_SET, myGroupOfStatements.getHashKeys());
        verify();
    }
}
