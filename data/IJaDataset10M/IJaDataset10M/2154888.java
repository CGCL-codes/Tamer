package org.aspectbrains.contractj.internal.paramprecondition;

import static org.junit.Assert.fail;
import org.aspectbrains.contractj.paramprecondition.IParamPreconditionContext;
import org.aspectbrains.contractj.paramprecondition.NotNullParamPreconditionHandler;
import org.aspectbrains.contractj.paramprecondition.ParamPreconditionViolationException;
import org.junit.Before;
import org.junit.Test;

public class NotNullParamPreconditionHandlerTest {

    private NotNullParamPreconditionHandler notNullParamPreconditionHandler;

    @Before
    public void setUp() {
        notNullParamPreconditionHandler = new NotNullParamPreconditionHandler();
    }

    @Test
    public void testHandle() {
        try {
            notNullParamPreconditionHandler.handle(new IParamPreconditionContext() {

                public Object getArg() {
                    return null;
                }

                public Integer getParameterNo() {
                    return new Integer(0);
                }
            });
            fail("ParamPreconditionViolationException expected!");
        } catch (final ParamPreconditionViolationException e) {
        }
        try {
            notNullParamPreconditionHandler.handle(new IParamPreconditionContext() {

                public Object getArg() {
                    return "";
                }

                public Integer getParameterNo() {
                    return new Integer(0);
                }
            });
        } catch (final ParamPreconditionViolationException e) {
            fail("No ParamPreconditionViolationException expected!");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleEx() throws ParamPreconditionViolationException {
        notNullParamPreconditionHandler.handle(null);
    }
}
