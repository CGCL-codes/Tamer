package org.norecess.nolatte.primitives.group;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertSame;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IGroupOfData;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.NoLatteVariables;
import org.norecess.nolatte.types.IDataTypeFilter;

public class ConsPrimitiveTest {

    private IInterpreter myInterpreter;

    private IDataTypeFilter myFilter;

    private IEnvironment myEnvironment;

    private Datum myRawCar;

    private Datum myRawCdr;

    private Datum myCar;

    private IGroupOfData myCdr;

    private IGroupOfData myUnstrippedCdr;

    private IGroupOfData myAnswer;

    private ConsPrimitive myConsPrimitive;

    @Before
    public void setUp() {
        myInterpreter = createMock(IInterpreter.class);
        myFilter = createMock(IDataTypeFilter.class);
        myEnvironment = createMock(IEnvironment.class);
        expectFreeAccessToInterpretersAccessors();
        myRawCar = createMock(Datum.class);
        myRawCdr = createMock(Datum.class);
        myCar = createMock(Datum.class);
        myCdr = createMock(IGroupOfData.class);
        myUnstrippedCdr = createMock(IGroupOfData.class);
        myAnswer = createMock(IGroupOfData.class);
        myConsPrimitive = new ConsPrimitive();
    }

    public void replay() {
        EasyMock.replay(myInterpreter, myFilter, myEnvironment, myRawCar, myRawCdr, myCar, myCdr, myUnstrippedCdr, myAnswer);
    }

    public void verify() {
        EasyMock.verify(myInterpreter, myFilter, myEnvironment, myRawCar, myRawCdr, myCar, myCdr, myUnstrippedCdr, myAnswer);
    }

    @Test
    public void should_apply_cons_after_padding_car_of_cdr() {
        expect(myEnvironment.get(NoLatteVariables.CAR)).andReturn(myRawCar);
        expect(myEnvironment.get(NoLatteVariables.CDR)).andReturn(myRawCdr);
        expect(myFilter.strip(myRawCar)).andReturn(myCar);
        expect(myFilter.getGroup(myRawCdr)).andReturn(myCdr);
        expect(myFilter.padFirst(myCdr)).andReturn(myUnstrippedCdr);
        expect(myUnstrippedCdr.cons(myCar)).andReturn(myAnswer);
        replay();
        assertSame(myAnswer, myConsPrimitive.apply(myInterpreter));
        verify();
    }

    private void expectFreeAccessToInterpretersAccessors() {
        expect(myInterpreter.getDataTypeFilter()).andReturn(myFilter).anyTimes();
        expect(myInterpreter.getEnvironment()).andReturn(myEnvironment).anyTimes();
    }
}
