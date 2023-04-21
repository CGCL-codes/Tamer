package org.norecess.nolatte.interpreters;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertSame;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IPrimitive;
import org.norecess.nolatte.ast.support.IParameters;
import org.norecess.nolatte.environments.IEnvironment;

public class PrimitiveApplicationTest {

    private IEnvironment myContextEnvironment;

    private IPrimitive myPrimitive;

    private IParameters myParameters;

    private IInterpreter myInterpreter;

    private Datum myAnswer;

    private PrimitiveApplication myPrimitiveApplication;

    @Before
    public void setUp() {
        myContextEnvironment = createMock(IEnvironment.class);
        myPrimitive = createMock(IPrimitive.class);
        myParameters = createMock(IParameters.class);
        myInterpreter = createMock(IInterpreter.class);
        myAnswer = createMock(Datum.class);
        myPrimitiveApplication = new PrimitiveApplication(myContextEnvironment, myPrimitive);
    }

    public void replay() {
        EasyMock.replay(myContextEnvironment, myPrimitive, myInterpreter, myAnswer);
    }

    public void verify() {
        EasyMock.verify(myContextEnvironment, myPrimitive, myInterpreter, myAnswer);
    }

    @Test
    public void should_have_initial_environment() {
        replay();
        assertSame(myContextEnvironment, myPrimitiveApplication.getInitialEnvironment());
        verify();
    }

    @Test
    public void should_have_parameters() {
        expect(myPrimitive.getParameters()).andReturn(myParameters);
        replay();
        assertSame(myParameters, myPrimitiveApplication.getParameters());
        verify();
    }

    @Test
    public void should_apply() {
        expect(myPrimitive.apply(myInterpreter)).andReturn(myAnswer);
        replay();
        assertSame(myAnswer, myPrimitiveApplication.apply(myInterpreter));
        verify();
    }
}
