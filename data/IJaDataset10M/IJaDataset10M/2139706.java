package org.norecess.nolatte.primitives.equals;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.GroupOfData;
import org.norecess.nolatte.ast.support.NoLatteBoolean;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.support.NoLatteMockControl;
import org.norecess.nolatte.system.NoLatteVariables;
import org.norecess.nolatte.types.IDataTypeFilter;

public class EqualPredicateTest {

    private NoLatteMockControl myControl;

    private IEqualPredicateVisitor myVisitor;

    private IInterpreter myInterpreter;

    private IDataTypeFilter myDataTypeFilter;

    private IEnvironment myEnvironment;

    private EqualPredicate myEqualPredicate;

    @Before
    public void setUp() {
        myControl = new NoLatteMockControl();
        myVisitor = myControl.createMock(IEqualPredicateVisitor.class);
        myInterpreter = myControl.createMock(IInterpreter.class);
        myDataTypeFilter = myControl.createMock(IDataTypeFilter.class);
        myEnvironment = myControl.createMock(IEnvironment.class);
        expectFreeAccessToInterpretersAccessors();
        myEqualPredicate = new EqualPredicate(myVisitor);
    }

    public void replay() {
        myControl.replay();
    }

    public void verify() {
        myControl.verify();
    }

    @Test
    public void should_return_true_for_empty_group() {
        Datum rawDatum = myControl.createMock(Datum.class);
        expect(myEnvironment.get(NoLatteVariables.ARGS)).andReturn(rawDatum);
        expect(myDataTypeFilter.getGroup(rawDatum)).andReturn(new GroupOfData());
        replay();
        assertSame(NoLatteBoolean.TRUE, myEqualPredicate.apply(myInterpreter));
        verify();
    }

    @Test
    public void should_return_true_for_singleton_group() {
        Datum rawDatum = myControl.createMock(Datum.class);
        Datum element = myControl.createMock(Datum.class);
        expect(myEnvironment.get(NoLatteVariables.ARGS)).andReturn(rawDatum);
        expect(myDataTypeFilter.getGroup(rawDatum)).andReturn(new GroupOfData(element));
        replay();
        assertSame(NoLatteBoolean.TRUE, myEqualPredicate.apply(myInterpreter));
        verify();
    }

    @Test
    public void should_return_true_for_this_interesting_group() {
        Datum rawDatum = myControl.createMock(Datum.class);
        Datum[] elements = myControl.createMocks(new Datum[3], Datum.class);
        expect(myEnvironment.get(NoLatteVariables.ARGS)).andReturn(rawDatum);
        expect(myDataTypeFilter.getGroup(rawDatum)).andReturn(new GroupOfData(elements));
        expect(myVisitor.areEqual(elements[0], elements[1])).andReturn(true);
        expect(myVisitor.areEqual(elements[0], elements[2])).andReturn(true);
        replay();
        assertSame(NoLatteBoolean.TRUE, myEqualPredicate.apply(myInterpreter));
        verify();
    }

    @Test
    public void should_return_false_for_this_interesting_group() {
        Datum myRawDatum = myControl.createMock(Datum.class);
        Datum[] elements = myControl.createMocks(new Datum[3], Datum.class);
        expect(myEnvironment.get(NoLatteVariables.ARGS)).andReturn(myRawDatum);
        expect(myDataTypeFilter.getGroup(myRawDatum)).andReturn(new GroupOfData(elements));
        expect(myVisitor.areEqual(elements[0], elements[1])).andReturn(false);
        replay();
        assertSame(NoLatteBoolean.FALSE, myEqualPredicate.apply(myInterpreter));
        verify();
    }

    private void expectFreeAccessToInterpretersAccessors() {
        expect(myInterpreter.getDataTypeFilter()).andReturn(myDataTypeFilter).anyTimes();
        expect(myInterpreter.getEnvironment()).andReturn(myEnvironment).anyTimes();
    }
}
